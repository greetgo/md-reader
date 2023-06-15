package kz.greetgo.md_reader.core.sitemap;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import lombok.SneakyThrows;

public class SiteMapUrlSet implements AutoCloseable {
  private final FileOutputStream        fileOut;
  private final XMLStreamWriter         xmlOut;
  private final ByteCounterOutputStream counterOut;

  private int locCount = 0;

  @SuppressWarnings("SpellCheckingInspection")
  private static final String URL_SET  = "urlset";
  private static final String URL      = "url";
  private static final String LOC      = "loc";
  @SuppressWarnings("SpellCheckingInspection")
  private static final String LAST_MOD = "lastmod";

  @SneakyThrows
  public SiteMapUrlSet(Path file) {
    fileOut    = new FileOutputStream(file.toFile());
    counterOut = new ByteCounterOutputStream(fileOut);
    xmlOut     = XMLOutputFactory.newInstance().createXMLStreamWriter(new OutputStreamWriter(counterOut, StandardCharsets.UTF_8));

    xmlOut.writeStartDocument("UTF-8", "1.0");

    xmlOut.writeStartElement(URL_SET);
    xmlOut.writeAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");
  }

  @SneakyThrows
  public void appendLoc(String loc, Date lastMod) {
    xmlOut.writeStartElement(URL);
    {
      xmlOut.writeStartElement(LOC);
      xmlOut.writeCharacters(loc);
      xmlOut.writeEndElement();
    }
    if (lastMod != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      xmlOut.writeStartElement(LAST_MOD);
      xmlOut.writeCharacters(sdf.format(lastMod));
      xmlOut.writeEndElement();
    }
    xmlOut.writeEndElement();
    xmlOut.flush();
    locCount++;
  }

  public int getLocCount() {
    return locCount;
  }

  public long bytesCount() {
    return counterOut.count();
  }

  @Override
  @SneakyThrows
  public void close() {
    xmlOut.writeEndElement();
    xmlOut.close();
    fileOut.close();
  }
}
