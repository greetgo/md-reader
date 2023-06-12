package kz.greetgo.md_reader.core.sitemap;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;
import lombok.SneakyThrows;
import org.xml.sax.Attributes;

public class PathTagParser extends PathSaxParser {

  private final String tagPathName;

  public PathTagParser(String tagPathName) {
    this.tagPathName = tagPathName;
  }

  public final List<String> list = new ArrayList<>();

  @Override
  protected void startTag(Attributes attributes) {
    // do nothing
  }

  @Override
  protected void endTag() {
    if (tagPathName.equals(path())) {
      list.add(text());
    }
  }

  @SneakyThrows
  protected static List<String> parseByTagPathName(String xmlText, String tagPathName) {

    PathTagParser ret = new PathTagParser(tagPathName);

    SAXParserFactory.newInstance()
                    .newSAXParser()
                    .parse(new ByteArrayInputStream(xmlText.getBytes(StandardCharsets.UTF_8)), ret);

    return ret.list;
  }

  public static List<String> parseSitemapIndex(String xmlText) {
    //noinspection SpellCheckingInspection
    return parseByTagPathName(xmlText, "/sitemapindex/sitemap/loc");
  }

  public static List<String> parseUrlSet(String xmlText) {
    //noinspection SpellCheckingInspection
    return parseByTagPathName(xmlText, "/urlset/url/loc");
  }
}
