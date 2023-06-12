package kz.greetgo.md_reader.core.sitemap;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.GregorianCalendar;
import kz.greetgo.md_reader.core.TestCommon;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public class SitemapTestParent extends TestCommon {

  protected Sitemap sitemap;

  protected static String parentWorkDir;

  protected GregorianCalendar time;

  @BeforeAll
  static void prepareParentWorkDir() {
    parentWorkDir = rndTestDir + "Sitemap/";
  }

  @BeforeEach
  void prepareSitemap(TestInfo testInfo) {
    Path workDir = Paths.get(parentWorkDir + testInfo.getTestMethod().map(Method::getName).orElse("Unknown"));

    sitemap         = new Sitemap();
    sitemap.uriTop  = "index.md";
    sitemap.workDir = workDir.resolve("work-dir");
    sitemap.tmpDir  = workDir;

    time              = new GregorianCalendar();
    sitemap.nowSource = () -> time.getTimeInMillis();

    sitemap.host = "http://text.kz";
  }

  @Override
  protected Path workDir() {
    return sitemap.workDir;
  }
}
