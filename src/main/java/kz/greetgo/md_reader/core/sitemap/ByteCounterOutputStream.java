package kz.greetgo.md_reader.core.sitemap;

import java.io.IOException;
import java.io.OutputStream;

public class ByteCounterOutputStream extends OutputStream {

  private final OutputStream delegateOutput;

  private long count = 0;

  public ByteCounterOutputStream(OutputStream delegateOutput) {
    this.delegateOutput = delegateOutput;
  }

  @Override
  public void write(int b) throws IOException {
    delegateOutput.write(b);
    count++;
  }

  @Override
  @SuppressWarnings("NullableProblems")
  public void write(byte[] b, int off, int len) throws IOException {
    delegateOutput.write(b, off, len);
    count += len;
  }

  public long count() {
    return count;
  }
}
