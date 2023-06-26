package kz.greetgo.md_reader.core;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import kz.greetgo.md_reader.core.md_converter_data.MdConverterAnchor;
import kz.greetgo.md_reader.util.DomVisitor;
import kz.greetgo.md_reader.util.MdUtil;
import kz.greetgo.md_reader.util.XmlDomVisiting;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.assertj.core.api.Assertions.assertThat;

class MdConverterTest extends MdConverterTestParent {

  private final MdConverterAnchor res = new MdConverterAnchor();

  @Test
  void prepareMdFileList() {

    file("some/toc2/.toc");
    file("some/toc2/left_root1.md");
    file("some/toc2/left_root1/cap11.md");
    file("some/toc2/left_root1/cap12.md");
    file("some/toc2/left_root1/.hidden_file.txt");
    file("some/toc2/left_root2.md");
    file("some/toc2/left_root2/cap21.md");

    file("some/toc1/.toc");
    Path f0 = file("some/toc1/good_root1.md");
    Path f1 = file("some/toc1/good_root1/cap11.md");
    Path f2 = file("some/toc1/good_root1/cap12.md");
    Path f3 = file("some/toc1/good_root1/cap13.md");
    Path f4 = file("some/toc1/good_root2.md");
    Path f5 = file("some/toc1/good_root2/01_cap21.md");
    Path f6 = file("some/toc1/good_root2/02_cap22.md");
    Path f7 = file("some/toc1/good_root2/03_stone/cap23.md");
    Path f8 = file("some/toc1/good_root2/03_stone/cap24.md");
    Path f9 = file("some/toc1/good_root2/04_cap25.md");

    converter.toc.uriNoSlash = "some/toc1/good_root1/cap12.md";

    converter.toc.populate();

    //
    //
    converter.prepareMdFileList();
    //
    //

    List<String> fileList = converter.mdFileList;

    assertIsSameFile(converter.parentPath, fileList.get(0), f0);
    assertIsSameFile(converter.parentPath, fileList.get(1), f1);
    assertIsSameFile(converter.parentPath, fileList.get(2), f2);
    assertIsSameFile(converter.parentPath, fileList.get(3), f3);
    assertIsSameFile(converter.parentPath, fileList.get(4), f4);
    assertIsSameFile(converter.parentPath, fileList.get(5), f5);
    assertIsSameFile(converter.parentPath, fileList.get(6), f6);
    assertIsSameFile(converter.parentPath, fileList.get(7), f7);
    assertIsSameFile(converter.parentPath, fileList.get(8), f8);
    assertIsSameFile(converter.parentPath, fileList.get(9), f9);

    assertThat(fileList).hasSize(10);
  }

  @SneakyThrows
  private void assertIsSameFile(Path parent, String actualFile, Path expectedFile) {
    assertThat(Files.isSameFile(parent.resolve(actualFile), expectedFile))
      .describedAs("\nActual   File : " + actualFile + "\nExpected File : " + expectedFile)
      .isTrue();
  }

  @Test
  @SneakyThrows
  void probe() {

    file("some/pics/wow/buttonAddingNewProcess.png", res.asBytes("buttonAddingNewProcess.png"));
    file("some/pics/wow/status/sysctl-on-kubernetes.png", res.asBytes("sysctl-on-kubernetes.png"));
    Path md1 = file("some/toc2/file1.md", """
      ## Супер заголовок
            
      Какой-то текст и ещё чё-то.
            
      <img width="" src="../pics/wow/buttonAddingNewProcess.png">
            
      Текст между картинками с <b>жирным</b> и <i>курсивным</i> <b><i>текстом</i></b>.
            
      ![](../pics/wow/status/sysctl-on-kubernetes.png)
            
      Это какая-то картинка
            
      ### Это подзаголовок
            
      Текст под подзаголовком. На карточку объекта можно добавлять неограниченное количество
      вложенных объектов Департаменты и Пользователи. Подробно по вложенным объектам и их
      настройкам описано во главе Вложенные объекты.
            
      """);

    file("some/toc10/inner/pics/sixSettings.png", res.asBytes("sixSettings.png"));
    Path md2 = file("some/toc10/inner/file2.md", """
      ## Заголовок второго файла
            
      Текст второго файла.
            
      ![](pics/sixSettings.png)
            
      Это картинка второго файла.
            
      ### Это подзаголовок второго файла
            
      Текст под подзаголовком второго файла. Подчиненные - подчиненные автора, объекте,
      будут иметь права на указанные операции при настройке прав, если автор указан как
      руководитель в департаменте в которую он входит.
            
      """);

    Document htmlDoc1 = MdUtil.xmlTextToDoc(MdUtil.correctHtml("<div>" + MdUtil.mdToHtml(md1) + "</div>"));
    Document htmlDoc2 = MdUtil.xmlTextToDoc(MdUtil.correctHtml("<div>" + MdUtil.mdToHtml(md2) + "</div>"));

    Element body1 = MdUtil.extractFirstTagInBody(htmlDoc1);
    Element body2 = MdUtil.extractFirstTagInBody(htmlDoc2);

    XmlDomVisiting.visit(body1, new DomVisitor() {
      @Override
      public void visitElement(Element element) {
        if ("img".equalsIgnoreCase(element.getTagName())) {
          String src = element.getAttribute("src");
          if (!src.isBlank()) {
            System.out.println("Vwc0oz2kaD :: img.src = " + src);
            element.setAttribute("src", "asd/" + src);
          }
        }
      }
    });

    Document resultDoc = DocumentBuilderFactory.newInstance()
                                               .newDocumentBuilder()
                                               .newDocument();

    Element resultBody = resultDoc.createElement("body");
    resultDoc.appendChild(resultBody);

    {
      NodeList childNodes = body1.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        Node child = resultDoc.importNode(childNodes.item(i), true);
        resultBody.appendChild(child);
      }
    }
    {
      NodeList childNodes = body2.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        Node child = resultDoc.importNode(childNodes.item(i), true);
        resultBody.appendChild(child);
      }
    }

    Transformer transformer = TransformerFactory.newInstance()
                                                .newTransformer();

    DOMSource             source = new DOMSource(resultDoc);
    ByteArrayOutputStream out    = new ByteArrayOutputStream();
    StreamResult          result = new StreamResult(out);
    transformer.transform(source, result);

    System.out.println("IEt1cWrWxr :: OUT :\n\n" + out.toString(StandardCharsets.UTF_8) + "\n\n");
  }


  @Test
  @SneakyThrows
  void convert() {

    file("some/pics/wow/buttonAddingNewProcess.png", res.asBytes("buttonAddingNewProcess.png"));
    file("some/pics/wow/status/sysctl-on-kubernetes.png", res.asBytes("sysctl-on-kubernetes.png"));
    Path md1 = file("some/toc2/file1.md", """
      ## Супер заголовок
            
      Какой-то текст и ещё чё-то.
            
      <img width="" src="../pics/wow/buttonAddingNewProcess.png">
            
      Текст между картинками с <b>жирным</b> и <i>курсивным</i> <b><i>текстом</i></b>.
            
      ![](../pics/wow/status/sysctl-on-kubernetes.png)
            
      Это какая-то картинка
            
      ### Это подзаголовок
            
      Текст под подзаголовком. На карточку объекта можно добавлять неограниченное количество
      вложенных объектов Департаменты и Пользователи. Подробно по вложенным объектам и их
      настройкам описано во главе Вложенные объекты.
            
      """);

    file("some/toc10/inner/pics/sixSettings.png", res.asBytes("sixSettings.png"));
    Path md2 = file("some/toc10/inner/file2.md", """
      ## Заголовок второго файла
            
      Текст второго файла.
            
      ![](pics/sixSettings.png)
            
      Это картинка второго файла.
            
      ### Это подзаголовок второго файла
            
      Текст под подзаголовком второго файла. Подчиненные - подчиненные автора, объекте,
      будут иметь права на указанные операции при настройке прав, если автор указан как
      руководитель в департаменте в которую он входит.
            
      """);

    converter.toc.uriNoSlash = "some/toc10/inner/file2.md";

    converter.toc.populate();

    //
    //
    converter.convert1();
    //
    //


  }
}
