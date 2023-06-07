package kz.greetgo.md_reader.interceptors;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import java.io.ByteArrayOutputStream;

public class ByteOutputStream extends ServletOutputStream {
  private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

  @Override
  public void write(int b) {
    bos.write(b);
  }

  public byte[] getBytes() {
    return bos.toByteArray();
  }


  @Override
  public boolean isReady() {
    return true;
  }

  @Override
  public void setWriteListener(WriteListener writeListener) {
    System.out.println("n4jnHz8Ft0 :: setWriteListener : " + writeListener);
  }
}
