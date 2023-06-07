package kz.greetgo.md_reader.interceptors;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TextReplaceFilterTest {

  @Test
  void convertText_001() {

    String text = "asd wow <div> asd <a href='ref to some'> Hello <b>C</b> BY </a> sin </div> HI".replace('\'', '"');

    //
    //
    String convertedText = TextReplaceFilter.convertText(text);
    //
    //

    assertThat(convertedText).isEqualTo(
      "asd wow <div> asd <a href='ref to some'> Hello <b>C</b> BY </a> sin </div> HI"
        .replace('\'', '"'));

  }

  @Test
  void convertText_002() {

    String text = (
      "asd wow <div> asd <a href='ref to some1'> Hello <b>C</b> BY </a> sin </div> HI" +
        "asd wow <div> asd <a href='ref to some2'> Hello <b>C</b> BY </a> sin </div> HI"
    ).replace('\'', '"');

    //
    //
    String convertedText = TextReplaceFilter.convertText(text);
    //
    //

    assertThat(convertedText).isEqualTo(
      (
        "asd wow <div> asd <a href='ref to some1'> Hello <b>C</b> BY </a> sin </div> HI" +
          "asd wow <div> asd <a href='ref to some2'> Hello <b>C</b> BY </a> sin </div> HI"
      )
        .replace('\'', '"'));

  }

  @Test
  void convertText_003() {

    String text = (
      "asd wow <div> asd <a href=\"ref to some1\"> Hello <b>C</b> BY </a> sin </div> HI" +
        "asd wow <div> asd <a href='ref to some2'> Hello <b>C</b> BY </a> sin </div> HI"
    );

    //
    //
    String convertedText = TextReplaceFilter.convertText(text);
    //
    //

    assertThat(convertedText).isEqualTo(
      (
        "asd wow <div> asd <a href=\"ref to some1\"> Hello <b>C</b> BY </a> sin </div> HI" +
          "asd wow <div> asd <a href='ref to some2'> Hello <b>C</b> BY </a> sin </div> HI"
      ));

  }

  @Test
  void convertText_004() {

    String text = (
      "asd wow <div> asd <a href='ref to some1'> Hello <b>C</b> BY </a> sin </div> HI" +
        "asd wow <div> asd <a href=\"ref to some2\"> Hello <b>C</b> BY </a> sin </div> HI"
    );

    //
    //
    String convertedText = TextReplaceFilter.convertText(text);
    //
    //

    assertThat(convertedText).isEqualTo(
      (
        "asd wow <div> asd <a href='ref to some1'> Hello <b>C</b> BY </a> sin </div> HI" +
          "asd wow <div> asd <a href=\"ref to some2\"> Hello <b>C</b> BY </a> sin </div> HI"
      ));

  }


  @Test
  void convertText_005() {

    String text = (
      "asd wow <div> asd <a href='ref to some1'> Hello <b>C</b> BY "
    );

    //
    //
    String convertedText = TextReplaceFilter.convertText(text);
    //
    //

    assertThat(convertedText).isEqualTo("asd wow <div> asd <a href='ref to some1'> Hello <b>C</b> BY ");

  }

  @Test
  void convertText_006() {

    String text = (
      "asd wow <div> asd <a href='ref to some'> Hello <b>C</b> BY </a> sin </div> HI" +
        "asd wow <div> asd <a href=\"ref to some\"> Hello <b>C</b> BY </a> sin </div> HI"
    );

    //
    //
    String convertedText = TextReplaceFilter.convertText(text);
    //
    //

    assertThat(convertedText).isEqualTo(
      (
        "asd wow <div> asd <a href='ref to some'> Hello <b>C</b> BY </a> sin </div> HI" +
          "asd wow <div> asd <md-ref href=\"ref to some\"> Hello <b>C</b> BY </md-ref> sin </div> HI"
      ));

  }

  @Test
  void convertText_007() {

    String text = (
      "asd wow <div> asd <a href='ref to some'> Hello <b>C</b> BY </a> sin </div> HI" +
        "asd wow <div> asd <a href=\"ref to some\"> Hello <b>C</b> BY </a> sin </div> HI" +
        "asd wow <div> asd <a href=\"ref to some\"> Hello <b>C</b> BY </a> sin </div> HI"
    );

    //
    //
    String convertedText = TextReplaceFilter.convertText(text);
    //
    //

    assertThat(convertedText).isEqualTo(
      (
        "asd wow <div> asd <a href='ref to some'> Hello <b>C</b> BY </a> sin </div> HI" +
          "asd wow <div> asd <md-ref href=\"ref to some\"> Hello <b>C</b> BY </md-ref> sin </div> HI" +
          "asd wow <div> asd <md-ref href=\"ref to some\"> Hello <b>C</b> BY </md-ref> sin </div> HI"
      ));

  }

}
