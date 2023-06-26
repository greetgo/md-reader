package kz.greetgo.md_reader.util;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

public interface DomVisitor {

  void visitElement(Element element);

  default void visitText(Text text) {}

}
