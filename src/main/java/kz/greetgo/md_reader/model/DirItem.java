package kz.greetgo.md_reader.model;

public class DirItem {
  public String caption;
  public String reference;

  public static DirItem of(String caption, String reference) {
    DirItem ret = new DirItem();
    ret.caption   = caption;
    ret.reference = reference;
    return ret;
  }

  public String getCaption() {
    return caption;
  }

  public String getReference() {
    return reference;
  }
}
