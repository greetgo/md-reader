package kz.greetgo.md_reader.core.sitemap;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SitemapTest extends SitemapTestParent {

  @Test
  void simple_refLimit() {

    sitemap.refLimit       = 3;
    sitemap.sizeLimitBytes = 45_000_000;

    file("index.md");

    file("010_Top/.caption.txt", "Верхушка");
    file("010_Top/010_Head.md", "## Заголовок верхушки");
    file("010_Top/020_Status.md", "## Статус верхушки");

    file("020_Content/010_Head.md", "## Заголовок содержания");
    file("020_Content/020_Status.md", "## Статус содержания");
    file("020_Content.md", "## Содержание");

    String rootXml = sitemap.load(Sitemap.ROOT);

    List<String> list = PathTagParser.parseSitemapIndex(rootXml);

    assertThat(list).hasSize(2);
    assertThat(list.get(0)).isEqualTo("001.xml");
    assertThat(list.get(1)).isEqualTo("002.xml");


  }
}
