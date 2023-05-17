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

  public static Optional<String> gitRepo() {
    return EnvReader.str("MD_READER_GIT_REPO");
  }

  public static String uriTop() {
    return EnvReader.str("MD_READER_URI_TOP").orElse("index");
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
