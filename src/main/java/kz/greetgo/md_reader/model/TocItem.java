package kz.greetgo.md_reader.model;

@SuppressWarnings("unused")
public class TocItem {
  public String  caption;
  public String  reference;
  public int     level;
  public boolean selected;

  public static TocItem of(int level, String caption, String reference) {
    TocItem ret = new TocItem();
    ret.level     = level;
    ret.caption   = caption;
    ret.reference = reference;
    return ret;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
}
