package kz.greetgo.md_reader.util;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;

public class FileUtil {
  @SneakyThrows
  public static void copyFile(Path from, Path to) {
    Files.copy(from, to);
  }

  public static String extractExt(Path path) {
    String name  = path.toFile().getName();
    int    index = name.lastIndexOf('.');
    return index < 0 ? "" : name.substring(index);
  }

  @SneakyThrows
  public static void saveFile(Path file, String content) {
    Files.writeString(file, content, StandardCharsets.UTF_8);
  }
}
