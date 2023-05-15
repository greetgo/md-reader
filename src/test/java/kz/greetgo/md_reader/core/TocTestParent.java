package kz.greetgo.md_reader.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import kz.greetgo.md_reader.model.TocItem;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import static org.assertj.core.api.Assertions.assertThat;

class TocTestParent {

  private static String parentWorkDir;

  protected Toc toc;

  @BeforeAll
  static void prepareParentWorkDir() {

    Random rnd = new Random();
    int    i   = rnd.nextInt();
    if (i < 0) {
      i = -i;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-'T'-HH-mm-ss-SSS");

    parentWorkDir = "build/tests/toc/wd_" + sdf.format(new Date()) + "_" + i + "/";
  }

  @BeforeEach
  void prepareTocWithWorkingDir(TestInfo testInfo) {
    Path workDir = Paths.get(parentWorkDir + testInfo.getTestMethod().map(Method::getName).orElse("Unknown"));

    toc         = new Toc();
    toc.workDir = workDir;

    toc.targetExt = ".md";
  }

  protected Path file(String pathAndName, Object content) throws Exception {
    Path filePath = toc.workDir.resolve(pathAndName);
    filePath.toFile().getParentFile().mkdirs();
    if (content == null) {
      Files.createFile(filePath);
    } else {
      String contentStr = contentToStr(content);
      try (FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile())) {
        fileOutputStream.write(contentStr.getBytes(StandardCharsets.UTF_8));
      }
    }
    return filePath;
  }

  @SneakyThrows
  private static String contentToStr(Object content) {
    if (content instanceof CharSequence) {
      return content.toString();
    }
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(content);
  }

  protected Path file(String pathAndName) throws Exception {
    return file(pathAndName, null);
  }

  protected void assertItem(String place, int index, String caption, int level, String ref, boolean selected) {
    String d = place + " :: index = " + index;

    assertThat(toc.items.size()).describedAs(d).isGreaterThan(index);

    TocItem tocItem = toc.items.get(index);

    assertThat(tocItem).describedAs(d + " : tocItem == null").isNotNull();
    assertThat(tocItem.reference).describedAs(d + " : reference").isEqualTo(ref);
    assertThat(tocItem.caption).describedAs(d + " : caption").isEqualTo(caption);
    assertThat(tocItem.level).describedAs(d + " : level").isEqualTo(level);
    assertThat(tocItem.selected).describedAs(d + " : selected").isEqualTo(selected);
  }
}
