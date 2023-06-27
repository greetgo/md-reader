package kz.greetgo.md_reader.util;

import am.ik.marked4j.Marked;
import am.ik.marked4j.MarkedBuilder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import lombok.SneakyThrows;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

public class MdUtil {


  @SneakyThrows
  public static String mdToHtml(Path mdFile) {
    try (Marked marked = new MarkedBuilder().gfm(true).pedantic(true).build()) {
      return marked.marked(Files.readString(mdFile, StandardCharsets.UTF_8));
    }
  }

  public static String correctHtml(String illegalHtml) {
    Tidy tidy = new Tidy();
    tidy.setCharEncoding(Configuration.UTF8);
    tidy.setXHTML(true);

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    tidy.parse(new ByteArrayInputStream(illegalHtml.getBytes(StandardCharsets.UTF_8)), out);

    return out.toString(StandardCharsets.UTF_8);
  }

  public static Element extractFirstTagInBody(Document htmlDoc) {
    NodeList childNodes = htmlDoc.getDocumentElement().getChildNodes();

    int length = childNodes.getLength();
    for (int i = 0; i < length; i++) {
      Node item = childNodes.item(i);
      if ("body".equals(item.getNodeName()) && item instanceof Element body) {
        NodeList bodyNodes = body.getChildNodes();
        int      bodyLen   = bodyNodes.getLength();
        for (int j = 0; j < bodyLen; j++) {
          Node bodyItem = bodyNodes.item(j);
          if (bodyItem instanceof Element ret) {
            return ret;
          }
        }
        throw new RuntimeException("YLB7Yu3vdJ :: Body is empty");
      }
    }
    throw new RuntimeException("6UYkN537g2 :: Bo body");
  }

  public static Document xmlTextToDoc(String html) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder        builder = factory.newDocumentBuilder();
      return builder.parse(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception e) {
      StringBuilder sb         = new StringBuilder();
      int           n          = 1;
      String[]      lines      = html.split("\n");
      String        linesCount = "" + lines.length;
      for (final String line : lines) {
        sb.append(StrUtil.toLen(n++, linesCount.length())).append("  ").append(line).append("\n");
      }
      throw new RuntimeException("wXb6Vnl31u :: Ошибка для HTML=\n\n" + sb + "\n\n", e);
    }
  }

  @SneakyThrows
  public static String xmlDocToText(Element doc) {

    Transformer transformer = TransformerFactory.newInstance()
                                                .newTransformer();

    DOMSource             source = new DOMSource(doc);
    ByteArrayOutputStream out    = new ByteArrayOutputStream();
    StreamResult          result = new StreamResult(out);
    transformer.transform(source, result);

    return out.toString(StandardCharsets.UTF_8);
  }
}
