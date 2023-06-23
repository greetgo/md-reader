package kz.greetgo.md_reader.core;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import kz.greetgo.md_reader.model.TocItem;
import kz.greetgo.md_reader.util.ContentType;
import lombok.SneakyThrows;

import static kz.greetgo.md_reader.util.StrUtil.cutBorderSlash;

public class MdConverter implements AutoCloseable {
  // input
  public Path    tmpDir;
  public Toc     toc;
  public boolean clearTmpDir;

  // output
  public Path        downloadFile;
  public ContentType contentType;
  public String      downloadFileName;

  //inner

  private Path tmpWorkDir;

  final List<String> mdFileList = new ArrayList<>();
  Path parentPath;

  @Override
  @SneakyThrows
  public void close() {
    {
      Path d = tmpWorkDir;
      if (d != null && clearTmpDir) {
        Files.walkFileTree(d, new FileVisitor<>() {
          @Override
          public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFileFailed(Path file, IOException exc) {
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
          }
        });
      }
    }
  }

  void prepareMdFileList() {
    parentPath = toc.workDir;
    for (final TocItem item : toc.items) {
      Path file = toc.workDir.resolve(cutBorderSlash(item.reference));
      if (Files.isRegularFile(file)) {

        String path = parentPath.toUri().relativize(file.toUri()).getPath();

        mdFileList.add(path);
      }
    }
  }


  public static int execCmd(Path dir,
                            Path outputErrTxt,
                            List<String> cmd,
                            List<String> outL,
                            List<String> inL) throws IOException, InterruptedException {

    List<String> res = new ArrayList<>();
    res.addAll(cmd);
    res.addAll(outL);
    res.addAll(inL);

    Process pandoc = new ProcessBuilder().directory(dir.toFile())
                                         .command(res)
                                         .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                                         .redirectError(ProcessBuilder.Redirect.to(outputErrTxt.toFile()))
                                         .start();

    return pandoc.waitFor();
  }

  @SneakyThrows
  public void convert() {

    prepareMdFileList();

    System.out.println("A6M7Nz5S9A :: WORK DIR: " + toc.workDir);
    for (final String path : mdFileList) {
      System.out.println("wtB8Qs1eYB :: md - " + path);
    }


    String caption = Toc.toCaption(toc.startDir, toc.targetExt);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'_HH_mm_ss_SSS");
    tmpWorkDir = tmpDir.toAbsolutePath().resolve("converter-" + sdf.format(new Date()));

    System.out.println("1AZ7vc0r0I :: caption = " + caption.replaceAll("\"", ""));

    List<String> cmd = new ArrayList<>();
    cmd.add("pandoc");
    cmd.add("--metadata");
    //noinspection SpellCheckingInspection
    cmd.add("pagetitle=\"" + caption.replaceAll("\"", "") + "\"");
    cmd.add("--pdf-engine=wkhtmltopdf");
    cmd.add("--css=main.css");

    List<String> outL = new ArrayList<>();

    Path resultPdf = tmpWorkDir.resolve("__output_ok__.pdf");
    resultPdf.toFile().getParentFile().mkdirs();

    outL.add("-o " + resultPdf);

    {
      Execute exec = Execute.of(parentPath, tmpWorkDir.resolve("__output_err1__.txt"))
                            .cmd(cmd)
                            .cmd(outL)
                            .cmd(mdFileList)
                            .execute();

      if (exec.exitCode != 0) {
        downloadFileName = "ERR-" + toc.uriNoSlash.replace('/', '_').replace('.', '_') + ".txt";
        contentType      = ContentType.Text;
        downloadFile     = exec.err();
        return;
      }
    }

    outL.clear();
    outL.add("-s");
    outL.add("-o __output_ok__.html");

    {
      Execute exec = Execute.of(parentPath, tmpWorkDir.resolve("__output_err2__.txt"))
                            .cmd(cmd)
                            .cmd(outL)
                            .cmd(mdFileList)
                            .execute();

      if (exec.exitCode != 0) {
        downloadFileName = "ERR-" + toc.uriNoSlash.replace('/', '_').replace('.', '_') + ".txt";
        contentType      = ContentType.Text;
        downloadFile     = exec.err();
        return;
      }
    }

    downloadFile     = resultPdf;
    contentType      = ContentType.Pdf;
    downloadFileName = caption + ".pdf";

  }
}
