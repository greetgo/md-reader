package kz.greetgo.md_reader.core.env;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class Env {
  public static Path workDir() {
    return Paths.get(
      EnvReader.str("MD_READER_WORK_DIR")
               .orElseGet(() -> System.getProperty("java.io.tmpdir"))
    );
  }

  private static final String HOST="MD_READER_HOST";

  public static String host() {
    return EnvReader.str(HOST).orElse("http://please-define-env-" + HOST);
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
}
