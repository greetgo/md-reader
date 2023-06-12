package kz.greetgo.md_reader.core.sitemap;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import lombok.SneakyThrows;

public class SiteMapIndex implements AutoCloseable {
  private final FileOutputStream fileOut;
  private final XMLStreamWriter  xmlOut;

  @SuppressWarnings("SpellCheckingInspection")
  private static final String SITE_MAP_INDEX = "sitemapindex";
  private static final String SITE_MAP       = "sitemap";
  private static final String LOC            = "loc";

  @SneakyThrows
  public SiteMapIndex(Path file) {
    fileOut = new FileOutputStream(file.toFile());
    xmlOut  = XMLOutputFactory.newInstance().createXMLStreamWriter(new OutputStreamWriter(fileOut, StandardCharsets.UTF_8));

    xmlOut.writeStartDocument("UTF-8", "1.0");

    xmlOut.writeStartElement(SITE_MAP_INDEX);
    xmlOut.writeAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");
  }

  @SneakyThrows
  public void appendLoc(String loc) {
    xmlOut.writeStartElement(SITE_MAP);
    {
      xmlOut.writeStartElement(LOC);
      xmlOut.writeCharacters(loc);
      xmlOut.writeEndElement();
    }
    xmlOut.writeEndElement();
  }

  @Override
  @SneakyThrows
  public void close() {
    xmlOut.writeEndElement();
    xmlOut.close();
    fileOut.close();
  }
}
