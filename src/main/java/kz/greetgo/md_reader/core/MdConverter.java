package kz.greetgo.md_reader.core;

import java.io.IOException;
import java.io.InputStream;
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
import kz.greetgo.md_reader.util.FileUtil;
import kz.greetgo.md_reader.util.MdUtil;
import kz.greetgo.md_reader.util.StrUtil;
import kz.greetgo.md_reader.util.XmlDomVisiting;
import lombok.SneakyThrows;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static java.util.Objects.requireNonNull;
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

  private int htmlFileIndex;
  private int imgFileIndex;

  private static String htmlFileByIndex(int index) {
    return "html-" + StrUtil.toLen(index, 5) + ".html";
  }

  private String prepareHtmlFileFrom(Path mdFilePath) {

    String htmlFileName = htmlFileByIndex(++htmlFileIndex);

    Path htmlFile = tmpWorkDir.resolve(htmlFileName);
    htmlFile.toFile().getParentFile().mkdirs();


    Path htmlFileParent = mdFilePath.toFile().getParentFile().toPath();

    Document htmlDoc = MdUtil.xmlTextToDoc(MdUtil.correctHtml("<div class=\"markdown\">" + MdUtil.mdToHtml(mdFilePath) + "</div>"));

    Element top = htmlDoc.getDocumentElement();

    XmlDomVisiting.visit(top, element -> {

      if ("img".equalsIgnoreCase(element.getTagName())) {
        String src = element.getAttribute("src");
        if (!src.isBlank()) {
          Path srcFile = htmlFileParent.resolve(src);
          if (Files.isRegularFile(srcFile)) {
            String ext     = FileUtil.extractExt(srcFile);
            Path   imgFile = tmpWorkDir.resolve("img-" + StrUtil.toLen(++imgFileIndex, 5) + ext);
            FileUtil.copyFile(srcFile, imgFile);
            element.setAttribute("src", imgFile.toFile().getName());
          }
        }
      }

      if (!element.getAttribute("id").isBlank()) {
        element.removeAttribute("id");
      }

    });

    String htmlText = MdUtil.xmlDocToText(MdUtil.extractFirstTagInBody(htmlDoc));

    FileUtil.saveFile(htmlFile, htmlText.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".length()));

    return htmlFileName;
  }

  private void copyResourceFiles() {
    copyResource("/static/markdown.css", "markdown.css");
    copyResource("/static/font.css", "font.css");
    copyResource("/static/fonts/PTRootUI_Bold.woff", "fonts/PTRootUI_Bold.woff");
    copyResource("/static/fonts/PTRootUI_Light.woff", "fonts/PTRootUI_Light.woff");
    copyResource("/static/fonts/PTRootUI_Medium.woff", "fonts/PTRootUI_Medium.woff");
    copyResource("/static/fonts/PTRootUI_Regular.woff", "fonts/PTRootUI_Regular.woff");
    copyResource("/download/main.css", "main.css");
  }

  @SneakyThrows
  private void copyResource(String resourceName, String destinationName) {
    try (InputStream resourceAsStream = getClass().getResourceAsStream(resourceName)) {
      requireNonNull(resourceAsStream, "P8fGOrj61L :: No resource " + resourceName);
      Path targetFile = tmpWorkDir.resolve(destinationName);
      targetFile.toFile().getParentFile().mkdirs();
      Files.write(targetFile, resourceAsStream.readAllBytes());
    }
  }

  public void convert() {
    prepareMdFileList();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'_HH_mm_ss_SSS");
    tmpWorkDir = tmpDir.toAbsolutePath().resolve("converter-" + sdf.format(new Date()));

    copyResourceFiles();

    htmlFileIndex = imgFileIndex = 0;

    List<String> htmlFileNameList = new ArrayList<>();

    for (final String mdFileName : mdFileList) {
      Path mdFilePath = parentPath.resolve(mdFileName);
      htmlFileNameList.add(prepareHtmlFileFrom(mdFilePath));
    }

    String caption = Toc.toCaption(toc.startDir, toc.targetExt);

    List<String> cmd = new ArrayList<>();
    cmd.add("pandoc");
    cmd.add("--metadata");
    //noinspection SpellCheckingInspection
    cmd.add("pagetitle=\"" + caption.replaceAll("\"", "") + "\"");
    cmd.add("--pdf-engine=wkhtmltopdf");
    cmd.add("--css=font.css");
    cmd.add("--css=main.css");
    cmd.add("--css=markdown.css");

    List<String> outL = new ArrayList<>();

    Path resultPdf = tmpWorkDir.resolve("__output_ok__.pdf");
    resultPdf.toFile().getParentFile().mkdirs();

    outL.add("-o");
    outL.add(resultPdf.toFile().getName());

    {
      Execute exec = Execute.of(tmpWorkDir, tmpWorkDir.resolve("__output_err__.txt"))
                            .cmd(cmd)
                            .cmd(outL)
                            .cmd(htmlFileNameList)
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
