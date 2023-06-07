package kz.greetgo.md_reader.interceptors;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.PrintWriter;

public class ResponseConverter extends HttpServletResponseWrapper {
  private final PrintWriter      writer;
  private final ByteOutputStream output;

  public byte[] getBytes() {
    writer.flush();
    return output.getBytes();
  }

  public ResponseConverter(HttpServletResponse response) {
    super(response);
    output = new ByteOutputStream();
    writer = new PrintWriter(output);
  }

  @Override
  public PrintWriter getWriter() {
    return writer;
  }

  @Override
  public ServletOutputStream getOutputStream() {
    return output;
  }
}
