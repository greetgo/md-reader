package kz.greetgo.md_reader.model;

@SuppressWarnings("unused")
public class BreadcrumbsItem {
  public String caption;
  public String reference;

  public String getCaption() {
    return caption;
  }

  public String getReference() {
    return reference;
  }

  public static BreadcrumbsItem of(String caption, String reference) {
    final BreadcrumbsItem ret = new BreadcrumbsItem();
    ret.caption   = caption;
    ret.reference = reference;
    return ret;
  }
}
