package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtil {

  public static final Path UP_DIR = Paths.get("..");

  public static boolean isParent(Path parent, File file) {
    return !parent.relativize(file.toPath()).startsWith(UP_DIR);
  }

  public static File copyToDir(File file, Path dir) {
    File destination = dir.resolve(file.getName()).toFile();
    destination.getParentFile().mkdirs();
    try {
      Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
      return destination;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void removeFileOrDir(Path fileOrDir) {
    try {
      if (!Files.exists(fileOrDir)) {
        return;
      }

      if (Files.isDirectory(fileOrDir)) {
        Files.list(fileOrDir)
             .forEach(FileUtil::removeFileOrDir);
      }

      Files.delete(fileOrDir);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
