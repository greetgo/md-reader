package kz.greetgo.md_reader.core;

import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;

public class MimeTypeManager {

  @SneakyThrows
  public static String probeMimeType(String fileName) {
    return Files.probeContentType(Paths.get(fileName));
  }
}
