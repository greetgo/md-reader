package kz.greetgo.md_reader.core;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    parentWorkDir = rndTestDir + "DirList/";
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
