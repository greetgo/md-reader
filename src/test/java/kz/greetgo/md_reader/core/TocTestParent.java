package kz.greetgo.md_reader.core;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import kz.greetgo.md_reader.model.TocItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import static org.assertj.core.api.Assertions.assertThat;

abstract class TocTestParent extends TestCommon {

  protected Toc toc;

  protected static String parentWorkDir;

  @BeforeAll
  static void prepareParentWorkDir() {
    parentWorkDir = rndTestDir + "Toc/";
  }

  @Override
  protected Path workDir() {
    return toc.workDir;
  }

  @BeforeEach
  void prepareTocWithWorkingDir(TestInfo testInfo) {
    Path workDir = Paths.get(parentWorkDir + testInfo.getTestMethod().map(Method::getName).orElse("Unknown"));

    toc = new Toc();

    toc.workDir   = workDir;
    toc.targetExt = ".md";
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
