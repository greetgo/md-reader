package kz.greetgo.md_reader.util;

public enum ContentType {
  Pdf("application/pdf"),
  Text("text/plain; charset=UTF-8"),
  Html("text/html; charset=UTF-8"),

  ;

  public final String mimeType;

  ContentType(String mimeType) {
    this.mimeType = mimeType;
  }
}
