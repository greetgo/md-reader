package kz.greetgo.md_reader.core.sitemap;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SitemapTest extends SitemapTestParent {

  @Test
  void simple_refLimit() {

    sitemap.refLimit       = 3;
    sitemap.sizeLimitBytes = 45_000_000;
    sitemap.host           = "http://test.kz";

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
    assertThat(list.get(0)).isEqualTo("http://test.kz/sitemaps/001.xml");
    assertThat(list.get(1)).isEqualTo("http://test.kz/sitemaps/002.xml");

    String f1 = sitemap.load("001.xml");
    String f2 = sitemap.load("002.xml");

    List<String> refs1 = PathTagParser.parseUrlSet(f1);
    List<String> refs2 = PathTagParser.parseUrlSet(f2);

    Set<String> urls = new HashSet<>();
    urls.addAll(refs1);
    urls.addAll(refs2);

    assertThat(urls).isEqualTo(Set.of(
      "http://test.kz/index.md",
      "http://test.kz/010_Top/010_Head.md",
      "http://test.kz/010_Top/020_Status.md",
      "http://test.kz/020_Content/010_Head.md",
      "http://test.kz/020_Content/020_Status.md",
      "http://test.kz/020_Content.md"
    ));

    Set<String> retained = new HashSet<>(refs1);
    retained.retainAll(refs2);

    assertThat(retained).isEmpty();
  }

  @Test
  void trimSlash() {

    //
    //
    String result = Sitemap.trimSlash("  ///http://hello.world/status/quo//////   ");
    //
    //

    assertThat(result).isEqualTo("http://hello.world/status/quo");

  }

  private String extractUrlName(String url) {
    int i = url.lastIndexOf('/');
    if (i < 0) {
      throw new RuntimeException("O19jMbV0kv :: No slashes in " + url);
    }
    return url.substring(i + 1);
  }

  private static String toLen3(int i) {
    if (i < 10) {
      return "00" + i;
    }
    if (i < 100) {
      return "0" + i;
    }
    return "" + i;
  }

  @Test
  void simple_sizeLimit() {

    sitemap.refLimit       = 50_000;
    sitemap.sizeLimitBytes = 1_000;
    sitemap.host           = "http://test.kz";

    file("index.md");

    for (int i = 1; i <= 100; i++) {
      file("010_Top/file_" + toLen3(i) + ".md", "## Файл номер " + i + " - Начинается новый день");
    }

    String rootXml = sitemap.load(Sitemap.ROOT);

    List<String> list = PathTagParser.parseSitemapIndex(rootXml);


    Set<String> refs = list.stream()
                           .map(this::extractUrlName)
                           .map(sitemap::load)
                           .map(PathTagParser::parseUrlSet)
                           .flatMap(Collection::stream)
                           .map(this::extractUrlName)
                           .collect(Collectors.toSet());

    for (int i = 1; i <= 100; i++) {
      assertThat(refs).contains("file_" + toLen3(i) + ".md");
    }

    assertThat(list.size()).describedAs("S4RCVOy1mH :: должно быть несколько файлов из-за ограничения размера файла")
                           .isGreaterThan(3);

  }
}
