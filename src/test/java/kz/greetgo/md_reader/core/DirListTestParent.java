package kz.greetgo.md_reader.core;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import kz.greetgo.md_reader.model.DirItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import static org.assertj.core.api.Assertions.assertThat;

class DirListTestParent extends TestCommon {

  protected DirList dt;

  protected static String parentWorkDir;

  @BeforeAll
  static void prepareParentWorkDir() {

    Random rnd = new Random();
    int    i   = rnd.nextInt();
    if (i < 0) {
      i = -i;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-'T'-HH-mm-ss-SSS");

    parentWorkDir = "build/tests/DirList/wd_" + sdf.format(new Date()) + "_" + i + "/";
  }

  @BeforeEach
  void prepareDirList(TestInfo testInfo) {
    Path workDir = Paths.get(parentWorkDir + testInfo.getTestMethod().map(Method::getName).orElse("Unknown"));

    dt         = new DirList();
    dt.workDir = workDir;
  }

  @Override
  protected Path workDir() {
    return dt.workDir;
  }

  protected void assertItem(String place, int index, String caption, String reference) {
    String d = place + " :: " + index;

    assertThat(dt.items.size()).describedAs(d + " : size").isGreaterThan(index);

    DirItem item = dt.items.get(index);

    assertThat(item.reference).describedAs(d + " : reference").isEqualTo(reference);
    assertThat(item.caption).describedAs(d + " : caption").isEqualTo(caption);

  }
}
