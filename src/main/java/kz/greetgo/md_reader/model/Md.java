package kz.greetgo.md_reader.model;

public class Md {
  public String caption;

  public static Md withCaption(String caption) {
    Md md = new Md();
    md.caption = caption;
    return md;
  }
}
