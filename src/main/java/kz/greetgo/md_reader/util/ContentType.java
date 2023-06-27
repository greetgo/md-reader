package kz.greetgo.md_reader.util;

public enum ContentType {
  Pdf("application/pdf", ".pdf"),
  Text("text/plain; charset=UTF-8", ".txt"),
  Html("text/html; charset=UTF-8", ".html"),
  @SuppressWarnings("SpellCheckingInspection")
  DocX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx"),

  ;

  public final String mimeType, dotExt;

  ContentType(String mimeType, String dotExt) {
    this.mimeType = mimeType;
    this.dotExt   = dotExt;
  }
}
