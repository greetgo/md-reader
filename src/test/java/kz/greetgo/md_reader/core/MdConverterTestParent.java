package kz.greetgo.md_reader.core;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public abstract class MdConverterTestParent extends TestCommon {
  protected static String parentWorkDir;

  @BeforeAll
  static void prepareParentWorkDir() {
    parentWorkDir = rndTestDir + "MdConverter/";
  }

  protected MdConverter converter;

  @BeforeEach
  void prepareBreadcrumbs(TestInfo testInfo) {
    Path systemDir = Paths.get(parentWorkDir + testInfo.getTestMethod().map(Method::getName).orElse("Unknown"));

    converter               = new MdConverter();
    converter.toc           = new Toc();
    converter.toc.workDir   = systemDir.resolve("work");
    converter.toc.targetExt = ".md";
    converter.tmpDir        = systemDir.resolve("tmp");
    converter.clearTmpDir   = false;
  }


  @Override
  protected Path workDir() {
    return converter.toc.workDir;
  }

}
