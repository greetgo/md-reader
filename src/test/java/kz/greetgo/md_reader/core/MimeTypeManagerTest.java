package kz.greetgo.md_reader.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MimeTypeManagerTest {

  @Test
  public void probeMimeType__txt() {
    String fileName = "/asd/dsa/wow.txt";

    //
    //
    String mimeType = MimeTypeManager.probeMimeType(fileName);
    //
    //

    assertThat(mimeType).isEqualTo("text/plain");
  }

  @Test
  public void probeMimeType__png() {
    String fileName = "/asd/dsa/pic.png";

    //
    //
    String mimeType = MimeTypeManager.probeMimeType(fileName);
    //
    //

    assertThat(mimeType).isEqualTo("image/png");
  }

}
