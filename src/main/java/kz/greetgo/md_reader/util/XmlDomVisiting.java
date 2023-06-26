package kz.greetgo.md_reader.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XmlDomVisiting {
  public static void visit(Element root, DomVisitor visitor) {

    visitor.visitElement(root);

    visitNodes(root.getChildNodes(), visitor);

  }

  private static void visitNodes(NodeList nodes, DomVisitor visitor) {
    for (int i = 0; i < nodes.getLength(); i++) {
      Node item = nodes.item(i);
      if (item instanceof Text) {
        visitor.visitText((Text) item);
        continue;
      }
      if (item instanceof Element) {
        visit((Element) item, visitor);
        continue;
      }
    }
  }
}
