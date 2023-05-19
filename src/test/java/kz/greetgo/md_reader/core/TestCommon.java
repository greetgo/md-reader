package kz.greetgo.md_reader.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import lombok.SneakyThrows;

public abstract class TestCommon {

  protected static String rndTestDir;

  static {
    Random rnd = new Random();
    int    i   = rnd.nextInt();
    if (i < 0) {
      i = -i;
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-'T'-HH-mm-ss-SSS");

    rndTestDir = "build/tests/wd_" + sdf.format(new Date()) + "_" + i + "/";
  }

  protected abstract Path workDir();


  protected Path file(String pathAndName) {
    return file(pathAndName, null);
  }

  @SneakyThrows
  protected Path file(String pathAndName, Object content) {
    Path filePath = workDir().resolve(pathAndName);
    filePath.toFile().getParentFile().mkdirs();
    if (content == null) {
      Files.createFile(filePath);
    } else if (content instanceof CharSequence) {
      String contentStr = content.toString();
      try (FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile())) {
        fileOutputStream.write(contentStr.getBytes(StandardCharsets.UTF_8));
      }
    } else {
      String contentStr = TestCommon.contentToStr(content);
      try (FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile())) {
        fileOutputStream.write(contentStr.getBytes(StandardCharsets.UTF_8));
      }
    }
    return filePath;
  }

  @SneakyThrows
  public static String contentToStr(Object content) {
    if (content instanceof CharSequence) {
      return content.toString();
    }
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(content);
  }
}
