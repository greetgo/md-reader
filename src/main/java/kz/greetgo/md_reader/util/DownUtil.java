package kz.greetgo.md_reader.util;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.nio.file.Path;
import kz.greetgo.md_reader.interceptors.TextReplaceFilter;
import lombok.SneakyThrows;

public class DownUtil {

  private static final int BUFFER_SIZE = 8 * 1024;

  @SneakyThrows
  public static void downloadFile(HttpServletResponse response, Path downFile, ContentType contentType, String fileName) {

    TextReplaceFilter.skipConvert();

    response.setHeader(
      "Content-Disposition",
      "attachment; filename=\"" + UriEscape.escape(fileName) + "\""
    );
    //response.setHeader("Content-Type", "text/plain; charset=UTF-8");
    response.setHeader("Content-Type", contentType.mimeType);

    {
      byte[] buffer = new byte[BUFFER_SIZE];
      try (FileInputStream input = new FileInputStream(downFile.toFile())) {
        ServletOutputStream output = response.getOutputStream();
        while (true) {
          int count = input.read(buffer, 0, BUFFER_SIZE);
          if (count < 0) {
            output.flush();
            response.flushBuffer();
            return;
          }
          output.write(buffer, 0, count);
        }
      }
    }

  }

}
