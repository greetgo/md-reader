package kz.greetgo.md_reader.util;

public enum RequestContentType {
  PDF(ContentType.Pdf),
  DOCX(ContentType.DocX),

  ;

  public final ContentType contentType;

  RequestContentType(ContentType contentType) {
    this.contentType = contentType;
  }
}
