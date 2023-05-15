package kz.greetgo.md_reader.model;

public class Settings {
  public String caption;

  public static Settings withCaption(String caption) {
    Settings settings = new Settings();
    settings.caption = caption;
    return settings;
  }

  public boolean hasCaption() {
    return caption != null && caption.length() > 0;
  }
}
