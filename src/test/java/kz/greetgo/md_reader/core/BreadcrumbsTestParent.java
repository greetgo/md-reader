package kz.greetgo.md_reader.core;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import kz.greetgo.md_reader.model.BreadcrumbsItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import static org.assertj.core.api.Assertions.assertThat;

public class BreadcrumbsTestParent extends TestCommon {

  protected Breadcrumbs b;

  protected static String parentWorkDir;

  @BeforeAll
  static void prepareParentWorkDir() {
    parentWorkDir = rndTestDir + "Breadcrumbs/";
  }

  @Override
  protected Path workDir() {
    return b.workDir;
  }


  @BeforeEach
  void prepareBreadcrumbs(TestInfo testInfo) {
    Path workDir = Paths.get(parentWorkDir + testInfo.getTestMethod().map(Method::getName).orElse("Unknown"));

    b         = new Breadcrumbs();
    b.workDir = workDir;
  }

  protected void assertItem(String place, int index, String caption, String reference) {
    String d = place + " :: " + index;

    assertThat(b.items.size()).describedAs(d + " : size").isGreaterThan(index);

    BreadcrumbsItem item = b.items.get(index);

    assertThat(item.reference).describedAs(d + " : reference").isEqualTo(reference);
    assertThat(item.caption).describedAs(d + " : caption").isEqualTo(caption);

  }
}
