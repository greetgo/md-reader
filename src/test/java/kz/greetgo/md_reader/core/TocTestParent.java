package kz.greetgo.md_reader.core;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import kz.greetgo.md_reader.model.TocItem;
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

    toc.ext = ".md";
  }

  protected void file(String pathAndName) throws Exception {
    Path filePath = toc.workDir.resolve(pathAndName);
    filePath.toFile().getParentFile().mkdirs();
    Files.createFile(filePath);
  }

  protected void assertItem(String place, int index, String caption, int level, String ref, boolean selected) {
    String d = place + " :: index = " + index;

    assertThat(toc.items.size()).describedAs(d).isGreaterThan(index);

    TocItem tocItem = toc.items.get(index);

    assertThat(tocItem).describedAs(d).isNotNull();
    assertThat(tocItem.level).describedAs(d).isEqualTo(level);
    assertThat(tocItem.caption).describedAs(d).isEqualTo(caption);
    assertThat(tocItem.reference).describedAs(d).isEqualTo(ref);
    assertThat(tocItem.selected).describedAs(d).isEqualTo(selected);
  }
}
