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

  public static  String uriTop() {
    return EnvReader.str("MD_READER_URI_TOP").orElse("index");
  }
}
