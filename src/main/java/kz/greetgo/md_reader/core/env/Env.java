package kz.greetgo.md_reader.core.env;

import java.nio.file.Path;
import java.nio.file.Paths;
import kz.greetgo.md_reader.core.sitemap.Sitemap;

public class Env {
  public static Path workDir() {
    return Paths.get(
      EnvReader.str("MD_READER_WORK_DIR")
               .orElseGet(() -> System.getProperty("java.io.tmpdir"))
    );
  }

  private static final String HOST = "MD_READER_HOST";

  public static String host() {
    return Sitemap.trimSlash(EnvReader.str(HOST).orElse("http://please-define-env-" + HOST));
  }

  public static String uriTop() {
    return EnvReader.str("MD_READER_URI_TOP").orElse("index.md");
  }

  /**
   * Наименование корня в хлебных крошках
   *
   * @return наименование
   */
  public static String breadcrumbsRoot() {
    return EnvReader.str("MD_READER_BREADCRUMBS_ROOT").orElse("Документация");
  }

  public static String headerCaption() {
    return EnvReader.str("MD_READER_HEADER_CAPTION").orElse("MyBPM");
  }

  public static Path tmpDir() {
    return Paths.get(
      EnvReader.str("MD_READER_TMP_DIR")
               .orElseGet(() -> System.getProperty("java.io.tmpdir"))
    );
  }

  public static boolean clearTmpDir() {
    return !EnvReader.bool("MD_READER_TMP_DIR_LEAVE");
  }
}
