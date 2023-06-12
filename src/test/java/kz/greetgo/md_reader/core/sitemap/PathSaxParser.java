package kz.greetgo.md_reader.core.sitemap;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class PathSaxParser extends DefaultHandler {

  private StringBuilder text;

  private final List<String> tagStack = new ArrayList<>();

  protected String path() {
    return "/" + String.join("/", tagStack);
  }

  protected abstract void startTag(Attributes attributes) throws Exception;

  protected abstract void endTag() throws Exception;

  protected String text() {
    var x = text;
    return x == null ? "" : x.toString();
  }

  @Override
  public final void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    tagStack.add(qName);
    try {
      startTag(attributes);
    } catch (SAXException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public final void characters(char[] ch, int start, int length) {
    var x = text;
    if (x == null) {
      x = text = new StringBuilder();
    }
    x.append(ch, start, length);
  }

  @Override
  public final void endElement(String uri, String localName, String qName) throws SAXException {
    try {
      endTag();
    } catch (SAXException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    tagStack.remove(tagStack.size() - 1);
    text = null;
  }
}
