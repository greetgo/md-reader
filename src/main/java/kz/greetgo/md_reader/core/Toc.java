package kz.greetgo.md_reader.core;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kz.greetgo.md_reader.model.TocItem;

public class Toc {

  public Path   workDir;
  public String uriNoSlash;
  public String targetExt;

  public String tocFileName = ".toc";

  public List<TocItem> items = new ArrayList<>();

  public void populate() throws Exception {

    if (!Files.exists(workDir)) {
      return;
    }

    if (uriNoSlash == null) {
      populateOn(workDir, workDir);
      return;
    }

    if (uriNoSlash.startsWith("/")) {
      throw new RuntimeException("S9aKN1tdEt :: uriNoSlash cannot be started with slash (/)");
    }

    Path innerFile = workDir.resolve(uriNoSlash);

    int count = 1;

    while (!Files.exists(innerFile)) {
      innerFile = innerFile.toFile().getParentFile().toPath();
      if (count++ > 1000) {
        throw new RuntimeException("KZ4Yl7n1gy :: file system is too deep");
      }
    }

    populateOn(workDir, innerFile);
  }

  private Path extractContentDir(Path file) {
    String name = file.toFile().getName();
    if (!name.endsWith(targetExt)) {
      return null;
    }
    {
      String newName = name.substring(0, name.length() - targetExt.length());
      return file.toFile().getParentFile().toPath().resolve(newName);
    }
  }

  private Path extractDir(Path innerFile) {
    if (innerFile == null) {
      return null;
    }

    if (Files.isDirectory(innerFile)) {
      return innerFile;
    }

    if (!Files.isRegularFile(innerFile)) {
      throw new RuntimeException("54j7kcE52v :: I do not know what is it: " + innerFile);
    }

    return extractContentDir(innerFile);
  }

  static boolean isOut(Path parent, Path inner) throws Exception {
    String parentPath = parent.toFile().getCanonicalPath();
    String innerPath  = inner.toFile().getCanonicalPath();
    return !innerPath.startsWith(parentPath);
  }

  private void populateOn(Path workDir, Path innerFile) throws Exception {

    {
      Path pointToDir = extractDir(innerFile);
      if (pointToDir != null && Files.exists(pointToDir.resolve(tocFileName))) {
        populateInner(pointToDir, null);
        return;
      }
    }

    if (innerFile == null) {
      populateInner(workDir, null);
      return;
    }

    {
      Path currentDir = innerFile.toFile().getParentFile().toPath();
      if (isOut(workDir, currentDir)) {
        populateInner(workDir, null);
        return;
      }

      while (!Files.isSameFile(workDir, currentDir)) {

        if (Files.exists(currentDir.resolve(tocFileName))) {
          populateInner(currentDir, innerFile);
          return;
        }

        currentDir = currentDir.toFile().getParentFile().toPath();
      }

      populateInner(workDir, innerFile);
    }
  }

  class Element {
    final Path    path;
    final int     level;
    final boolean dir;

    boolean ignore = false;

    Path refPath;

    final Map<String, Element> children = new HashMap<>();

    String reference;

    Element(Path path, int level) {
      this.path  = path;
      this.level = level;

      dir = path == null ? true : Files.isDirectory(path);

      refPath = path;
    }

    @Override
    public String toString() {
      return "level " + level + (path == null ? "" : ", " + ((Files.isDirectory(path) ? "DIR " : "FIL ") + path.toFile().getName()));
    }

    public void correctRefPathInChildren() {
      for (final Map.Entry<String, Element> e : children.entrySet()) {
        Element element = e.getValue();
        if (element.ignore) {
          continue;
        }
        if (element.dir) {
          String  dirName    = e.getKey();
          String  extName    = dirName + targetExt;
          Element extElement = children.get(extName);
          if (extElement != null && !extElement.dir) {
            element.refPath   = extElement.path;
            extElement.ignore = true;
          }
        }
        element.correctRefPathInChildren();
      }
    }

    public void fillReference() {
      if (refPath != null) {
        reference = workDir.toFile().toURI().relativize(refPath.toUri()).toString();
        if (reference.endsWith("/")) {
          reference = reference.substring(0, reference.length() - 1);
        }
        reference = "/" + reference;
      }
      children.values().forEach(Element::fillReference);
    }

    public void appendItems() {
      appendItem();
      children.values().forEach(Element::appendItems);
    }

    private void appendItem() {
      if (path == null) {
        return;
      }
      if (ignore) {
        return;
      }

      TocItem item = new TocItem();

      item.path      = path;
      item.caption   = toCaption(refPath.toFile().getName());
      item.reference = reference;
      item.level     = level;

      items.add(item);

    }

    public void fillIgnore() {
      fillIgnore1();
      children.values().forEach(Element::fillIgnore);
    }

    private void fillIgnore1() {
      if (ignore) {
        return;
      }

      if (path == null || path.toFile().getName().startsWith(".")) {
        ignore = true;
      }

    }
  }

  private static final Pattern START_DIGIT = Pattern.compile("(\\d+\\s+).*");

  private String toCaption(String name) {
    name = name.replace('_', ' ');
    name = name.trim();
    Matcher matcher = START_DIGIT.matcher(name);
    if (matcher.matches()) {
      name = name.substring(matcher.group(1).length());
    }
    if (name.endsWith(targetExt)) {
      name = name.substring(0, name.length() - targetExt.length());
    }
    return name;
  }

  private void fillRoot(Path startDir, Element root) throws IOException {
    List<Element> dirs = new ArrayList<>();
    dirs.add(root);

    Files.walkFileTree(startDir, Set.of(FileVisitOption.FOLLOW_LINKS), 1000, new FileVisitor<Path>() {
      Element last() {
        return dirs.get(dirs.size() - 1);
      }

      int level() {
        return dirs.size() - 1;
      }

      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

        Element current = new Element(dir, level());

        last().children.put(dir.toFile().getName(), current);

        if (Files.exists(dir.resolve(tocFileName))) {
          if (Files.isSameFile(dir, startDir)) {
            dirs.add(current);
            return FileVisitResult.CONTINUE;
          }
          return FileVisitResult.SKIP_SUBTREE;
        }

        {
          dirs.add(current);
          return FileVisitResult.CONTINUE;
        }
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        last().children.put(file.toFile().getName(), new Element(file, level()));
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        dirs.remove(dirs.size() - 1);
        return FileVisitResult.CONTINUE;
      }
    });
  }

  private void populateInner(Path startDir, Path innerFile) throws Exception {

    Element root = new Element(null, 0);

    fillRoot(startDir, root);

    root.correctRefPathInChildren();

    root.fillReference();

    root.fillIgnore();

    root.children.values().forEach(x -> x.children.values().forEach(Element::appendItems));

    for (final TocItem item : items) {
      if (innerFile != null) {
        item.selected = !isOut(item.path, innerFile);
      }
    }

    items.sort(Comparator.comparing(x -> x.reference));

  }

}
