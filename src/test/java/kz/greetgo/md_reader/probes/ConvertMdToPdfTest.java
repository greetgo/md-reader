package kz.greetgo.md_reader.probes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import kz.greetgo.md_reader.controller.RenderController;
import kz.greetgo.md_reader.test_utils.ParentTestClass;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static kz.greetgo.md_reader.controller.RenderController.makeOptions;

class ConvertMdToPdfTest extends ParentTestClass {


  @SneakyThrows
  private void copyRes(String resName, Path targetFile) {
    try (InputStream resourceAsStream = getClass().getResourceAsStream(resName)) {
      if (resourceAsStream == null) {
        return;
      }

      File target = targetFile.toFile();
      target.getParentFile().mkdirs();

      try (FileOutputStream out = new FileOutputStream(target)) {
        out.write(resourceAsStream.readAllBytes());
      }
    }
  }

  @Test
  @SneakyThrows
  void pandoc() {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    Path dir = Paths.get("build/ConvertMdToPdfTest/md-files/" + sdf.format(new Date()));
    copyRes("/tmp/001-hello.md", dir.resolve("001-hello.md"));
    copyRes("/tmp/002-Boom.md", dir.resolve("002-Boom.md"));
    copyRes("/tmp/pic-001.png", dir.resolve("pic-001.png"));

    String fd = "/usr/share/fonts/truetype/liberation/";

    String f1_regular     = fd + "LiberationSansNarrow-Regular.ttf";
    String f1_bold        = fd + "LiberationSansNarrow-Bold.ttf";
    String f1_bold_italic = fd + "LiberationSansNarrow-BoldItalic.ttf";
    String f1_italic      = fd + "LiberationSansNarrow-Italic.ttf";

    String f2_regular     = fd + "LiberationMono-Regular.ttf";
    String f2_bold        = fd + "LiberationMono-Bold.ttf";
    String f2_bold_italic = fd + "LiberationMono-BoldItalic.ttf";
    String f2_italic      = fd + "LiberationMono-Italic.ttf";

    Path outputErrTxt = dir.resolve("output_err.txt");
    Path outputPdf    = dir.resolve("__output__.pdf");

    List<String> cmd = new ArrayList<>();
    cmd.add("pandoc");
    cmd.add("-V");
    cmd.add("mainfont:" + f1_regular);
    cmd.add("-V");
    cmd.add("mainfontoptions:" + RenderController.makeOptions(f1_bold, f1_italic, f1_bold_italic));
    cmd.add("-V");
    cmd.add("monofont:" + f2_regular);
    cmd.add("-V");
    cmd.add("monofontoptions:" + RenderController.makeOptions(f2_bold, f2_italic, f2_bold_italic));
//    cmd.add("-V");
//    cmd.add("sansfont:" + f1_regular);
//    cmd.add("-V");
//    cmd.add("sansfontoptions:" + RenderController.makeOptions(f1_bold, f1_italic, f1_bold_italic));
    cmd.add("-V");
    cmd.add("mathfont:" + f1_regular);
    cmd.add("-V");
    cmd.add("mathfontoptions:" + RenderController.makeOptions(f1_bold, f1_italic, f1_bold_italic));
    cmd.add("--pdf-engine=xelatex");
//    cmd.add("--pdf-engine=lualatex");
    cmd.add("-o __output__.pdf");
    cmd.add("./001-hello.md");
    cmd.add("./002-Boom.md");

    System.out.println("1lmHzIwPJ8 :: CMD " + cmd.stream().map(s -> "'" + s + "'").collect(Collectors.joining(" ")));

    Process pandoc = new ProcessBuilder().directory(dir.toFile())
                                         .command(cmd)
                                         .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                                         .redirectError(ProcessBuilder.Redirect.to(outputErrTxt.toFile()))
                                         .start();

    int exitCode = pandoc.waitFor();

    if (exitCode != 0) {
      String errorText = Files.readString(outputErrTxt, StandardCharsets.UTF_8);
      throw new RuntimeException("ZRM1xfDG8x :: Run exitCode = " + exitCode + "\n\n" + errorText);
    }

  }


  public static String makeOptions(String dir, String bold, String italic, String boldItalic) {
    LinkedHashMap<String, String> m = new LinkedHashMap<>();
    m.put("Path", dir);
    m.put("BoldFont", bold);
    m.put("ItalicFont", italic);
    m.put("BoldItalicFont", boldItalic);

    return m.entrySet()
            .stream()
            .map(x -> x.getKey() + "=" + x.getValue())
            .collect(Collectors.joining(", "));
  }

  @Test
  @SneakyThrows
  void pandoc2() {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    Path dir = Paths.get("build/ConvertMdToPdfTest/md-files-2/" + sdf.format(new Date()));
    copyRes("/tmp/001-hello.md", dir.resolve("001-hello.md"));
    copyRes("/tmp/002-Boom.md", dir.resolve("002-Boom.md"));
    copyRes("/tmp/pic-001.png", dir.resolve("pic-001.png"));

    String fd = "/usr/share/fonts/truetype/liberation/";

    String f1_regular     = "LiberationSansNarrow-Regular.ttf";
    String f1_bold        = "LiberationSansNarrow-Bold.ttf";
    String f1_bold_italic = "LiberationSansNarrow-BoldItalic.ttf";
    String f1_italic      = "LiberationSansNarrow-Italic.ttf";

    String f2_regular     = "LiberationMono-Regular.ttf";
    String f2_bold        = "LiberationMono-Bold.ttf";
    String f2_bold_italic = "LiberationMono-BoldItalic.ttf";
    String f2_italic      = "LiberationMono-Italic.ttf";

    Path outputErrTxt = dir.resolve("output_err.txt");
    Path outputPdf    = dir.resolve("__output__.pdf");

    //    sudo apt install wkhtmltopdf

    List<String> cmd = new ArrayList<>();
    cmd.add("pandoc");
    cmd.add("-V");
    cmd.add("mainfont:" + f1_regular);
    cmd.add("-V");
    cmd.add("mainfontoptions:" + makeOptions(fd, f1_bold, f1_italic, f1_bold_italic));
    cmd.add("-V");
    cmd.add("monofont:" + f2_regular);
    cmd.add("-V");
    cmd.add("monofontoptions:" + makeOptions(fd, f2_bold, f2_italic, f2_bold_italic));
    cmd.add("-V");
    cmd.add("sansfont:" + f1_regular);
    cmd.add("-V");
    cmd.add("sansfontoptions:" + makeOptions(fd, f1_bold, f1_italic, f1_bold_italic));
    cmd.add("-V");
    cmd.add("mathfont:" + f1_regular);
    cmd.add("-V");
    cmd.add("mathfontoptions:" + makeOptions(fd, f1_bold, f1_italic, f1_bold_italic));
    cmd.add("--pdf-engine=xelatex");
//    cmd.add("--pdf-engine=lualatex");


//    cmd.add("--metadata");
//    cmd.add("pagetitle=\"ASD\"");
//    cmd.add("--pdf-engine=wkhtmltopdf");

    cmd.add("-o __output__.pdf");
    cmd.add("./001-hello.md");
    cmd.add("./002-Boom.md");

    System.out.println("CaMpRrB9MO :: CMD " + cmd.stream().map(s -> "'" + s + "'").collect(Collectors.joining(" ")));

    Process pandoc = new ProcessBuilder().directory(dir.toFile())
                                         .command(cmd)
                                         .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                                         .redirectError(ProcessBuilder.Redirect.to(outputErrTxt.toFile()))
                                         .start();

    int exitCode = pandoc.waitFor();

    if (exitCode != 0) {
      String errorText = Files.readString(outputErrTxt, StandardCharsets.UTF_8);
      throw new RuntimeException("ZRM1xfDG8x :: Run exitCode = " + exitCode + "\n\n" + errorText);
    }

  }

  @Test
  @SneakyThrows
  void pandoc3() {



    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    Path dir = Paths.get("build/ConvertMdToPdfTest/md-files-3/" + sdf.format(new Date()));
    copyRes("/tmp/001-hello.md", dir.resolve("001-hello.md"));
    copyRes("/tmp/002-Boom.md", dir.resolve("002-Boom.md"));
    copyRes("/tmp/pic-001.png", dir.resolve("pic-001.png"));
    copyRes("/tmp/main.css", dir.resolve("main.css"));

    String fd = "/usr/share/fonts/truetype/liberation/";

    String f1_regular     = "LiberationSansNarrow-Regular.ttf";
    String f1_bold        = "LiberationSansNarrow-Bold.ttf";
    String f1_bold_italic = "LiberationSansNarrow-BoldItalic.ttf";
    String f1_italic      = "LiberationSansNarrow-Italic.ttf";

    String f2_regular     = "LiberationMono-Regular.ttf";
    String f2_bold        = "LiberationMono-Bold.ttf";
    String f2_bold_italic = "LiberationMono-BoldItalic.ttf";
    String f2_italic      = "LiberationMono-Italic.ttf";

    Path outputErrTxt = dir.resolve("output_err.txt");
    Path outputPdf    = dir.resolve("__output__.pdf");

    //    sudo apt install wkhtmltopdf

    List<String> cmd = new ArrayList<>();
    cmd.add("pandoc");
//    cmd.add("-V");
//    cmd.add("mainfont:" + f1_regular);
//    cmd.add("-V");
//    cmd.add("mainfontoptions:" + makeOptions(fd, f1_bold, f1_italic, f1_bold_italic));
//    cmd.add("-V");
//    cmd.add("monofont:" + f2_regular);
//    cmd.add("-V");
//    cmd.add("monofontoptions:" + makeOptions(fd, f2_bold, f2_italic, f2_bold_italic));
//    cmd.add("-V");
//    cmd.add("sansfont:" + f1_regular);
//    cmd.add("-V");
//    cmd.add("sansfontoptions:" + makeOptions(fd, f1_bold, f1_italic, f1_bold_italic));
//    cmd.add("-V");
//    cmd.add("mathfont:" + f1_regular);
//    cmd.add("-V");
//    cmd.add("mathfontoptions:" + makeOptions(fd, f1_bold, f1_italic, f1_bold_italic));
//    cmd.add("--pdf-engine=xelatex");
//    cmd.add("--pdf-engine=lualatex");


    cmd.add("--metadata");
    cmd.add("pagetitle=\"ASD\"");
    cmd.add("--pdf-engine=wkhtmltopdf");
    cmd.add("--css=main.css");

    cmd.add("-o __output__.pdf");
//    cmd.add("-s");
//    cmd.add("-o __output__.html");

    cmd.add("001-hello.md");
    cmd.add("002-Boom.md");

    System.out.println("CaMpRrB9MO :: CMD " + cmd.stream().map(s -> "'" + s + "'").collect(Collectors.joining(" ")));

    Process pandoc = new ProcessBuilder().directory(dir.toFile())
                                         .command(cmd)
                                         .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                                         .redirectError(ProcessBuilder.Redirect.to(outputErrTxt.toFile()))
                                         .start();

    int exitCode = pandoc.waitFor();

    if (exitCode != 0) {
      String errorText = Files.readString(outputErrTxt, StandardCharsets.UTF_8);
      throw new RuntimeException("ZRM1xfDG8x :: Run exitCode = " + exitCode + "\n\n" + errorText);
    }

  }

}
