package kz.greetgo.md_reader.core.sitemap;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kz.greetgo.md_reader.core.NowSource;
import lombok.SneakyThrows;

public class Sitemap {

  public String uriTop;
  public String host;
  public Path   workDir;

  public int refLimit;
  public long sizeLimitBytes;

  public Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));

  public long updateTimeoutMs = 30L * 60L * 1000L;

  public static final String ROOT = "root.xml";

  public NowSource nowSource = NowSource.CURRENT_TIME_MS;

  private final AtomicLong lastModifiedMs = new AtomicLong(0);

  private final AtomicReference<Path> sitemapDir = new AtomicReference<>(null);


  @SneakyThrows
  public String load(String name) {
    update();

    Path file = sitemapDir.get().resolve(name);
    if (!Files.exists(file)) {
      throw new RuntimeException("I1N64wD81X :: No file");
    }

    return Files.readString(file, StandardCharsets.UTF_8);
  }

  private void update() {

    if (skipUpdate()) {
      return;
    }

    prepareSitemapDir();
  }

  private boolean skipUpdate() {
    if (sitemapDir.get() == null) {
      return false;
    }

    return nowSource.nowMs() - lastModifiedMs.get() <= updateTimeoutMs;
  }

  @SneakyThrows
  private void prepareSitemapDir() {
    sitemapDir.compareAndSet(null, tmpDir.resolve("md-reader-sitemap"));
    sitemapDir.get().toFile().mkdirs();

    try (SiteMapIndex siteMapIndex = new SiteMapIndex(sitemapDir.get().resolve(ROOT))) {

      siteMapIndex.appendLoc("https://www.youtube.com/ads/sitemap.xml");
      siteMapIndex.appendLoc("https://www.youtube.com/ads/sitemap2.xml");
      siteMapIndex.appendLoc("https://www.youtube.com/ads/sitemap3.xml");
      siteMapIndex.appendLoc("https://www.youtube.com/ads/sitemap4.xml");

    }

  }
}
