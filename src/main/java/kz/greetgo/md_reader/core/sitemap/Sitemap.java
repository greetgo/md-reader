package kz.greetgo.md_reader.core.sitemap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kz.greetgo.md_reader.core.NowSource;
import lombok.SneakyThrows;

public class Sitemap {

  public String uriTop;
  public String host;
  public Path   workDir;

  public int  refLimit;
  public long sizeLimitBytes;

  public Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));

  public long updateTimeoutMs = 30L * 60L * 1000L;

  public static final String ROOT = "root.xml";

  public NowSource nowSource = NowSource.CURRENT_TIME_MS;

  private final AtomicLong lastModifiedMs = new AtomicLong(0);

  private final AtomicReference<Path> sitemapDir = new AtomicReference<>(null);

  public static final String SITEMAPS = "sitemaps";

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
    try (SitemapDirPreparation x = new SitemapDirPreparation(sitemapDir.get())) {
      x.prepare();
    }
  }

  static String trimSlash(String s) {
    if (s == null) {
      return "";
    }
    s = s.trim();
    if (s.isEmpty()) {
      return "";
    }
    int i1 = 0, len = s.length(), i2 = len;
    while (i2 >= 0 && i1 < i2) {
      boolean out = true;
      if (s.charAt(i1) == '/') {
        i1++;
        out = false;
      }
      if (s.charAt(i2 - 1) == '/') {
        i2--;
        out = false;
      }
      if (out) {
        break;
      }
    }

    return s.substring(i1, i2);
  }

  private class SitemapDirPreparation implements AutoCloseable {
    private final Path          targetDir;
    private final SiteMapIndex  siteMapIndex;
    private       SiteMapUrlSet siteMapUrlSet;
    private       int           index;


    public SitemapDirPreparation(Path targetDir) {
      this.targetDir = targetDir;
      targetDir.toFile().mkdirs();
      siteMapIndex = new SiteMapIndex(targetDir.resolve(ROOT));
      index        = 0;
      createNewFile();
    }

    private void createNewFile() {
      if (siteMapUrlSet != null) {
        siteMapUrlSet.close();
      }

      index++;
      siteMapUrlSet = new SiteMapUrlSet(targetDir.resolve(indexName(index)));
      siteMapIndex.appendLoc(indexUrl(index));
    }

    private static String indexName(int index) {
      StringBuilder s = new StringBuilder(10);
      s.append(index);
      while (s.length() < 3) {
        s.insert(0, '0');
      }
      s.append(".xml");
      return s.toString();
    }

    private String indexUrl(int index) {
      StringBuilder sb = new StringBuilder(host.length() + SITEMAPS.length() + 20);
      sb.append(trimSlash(host));
      sb.append('/');
      sb.append(SITEMAPS);
      sb.append('/');
      sb.append(indexName(index));
      return sb.toString();
    }

    @Override
    public void close() {
      siteMapIndex.close();
      siteMapUrlSet.close();
    }

    @SneakyThrows
    public void prepare() {

      Files.walkFileTree(workDir, Set.of(FileVisitOption.FOLLOW_LINKS), 1000, new FileVisitor<>() {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
          if (file.toFile().getName().toLowerCase().endsWith(".md")) {
            appendFile(file);
          }
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
          return FileVisitResult.CONTINUE;
        }
      });

    }

    private boolean reachTheLimit() {
      return siteMapUrlSet.getLocCount() >= refLimit;
    }

    private void appendFile(Path file) {
      if (reachTheLimit()) {
        createNewFile();
      }

      String reference = workDir.toFile().toURI().relativize(file.toUri()).toString();
      while (reference.startsWith("/")) {
        reference = reference.substring(1);
      }

      siteMapUrlSet.appendLoc(trimSlash(host) + '/' + trimSlash(reference));
    }
  }
}
