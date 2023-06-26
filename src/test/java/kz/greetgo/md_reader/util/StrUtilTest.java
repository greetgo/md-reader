package kz.greetgo.md_reader.util;

import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StrUtilTest {

  @Test
  void extractExt() {

    //
    //
    String ext = FileUtil.extractExt(Paths.get("name1/name2/hi.png"));
    //
    //


    assertThat(ext).isEqualTo(".png");

  }

  @Test
  void extractExt_noExt() {

    //
    //
    String ext = FileUtil.extractExt(Paths.get("name1/name2/hi"));
    //
    //


    assertThat(ext).isEmpty();

  }
}
