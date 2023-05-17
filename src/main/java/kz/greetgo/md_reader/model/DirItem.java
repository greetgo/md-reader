package kz.greetgo.md_reader.model;

public class DirItem {
  public String caption;
  public String reference;

  public String getCaption() {
    return caption;
  }

  public String getReference() {
    return reference;
  }

  @Override
  public String toString() {
    return "DirItem{[" + caption + "] " + reference + '}';
  }
}
