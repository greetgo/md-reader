package kz.greetgo.md_reader.core;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kz.greetgo.md_reader.model.BreadcrumbsItem;
import lombok.SneakyThrows;

public class Breadcrumbs {
  public Path   workDir;
  public String uriNoSlash;
  public String tocFileName = ".toc";
  public String rootCaption = "Корень";
  public String targetExt   = ".md";

  public List<BreadcrumbsItem> items = new ArrayList<>();

  @SneakyThrows
  public void populate() {

    if (uriNoSlash.isEmpty()) {
      uriNoSlash = null;
    }

    Path filePath = uriNoSlash == null ? workDir : workDir.resolve(uriNoSlash);

    if (Toc.isOut(workDir, filePath)) {
      items.add(BreadcrumbsItem.of(rootCaption, "/"));
      return;
    }

    while (!Files.exists(filePath)) {
      filePath = filePath.toFile().getParentFile().toPath();
      if (Toc.isOut(workDir, filePath)) {
        items.add(BreadcrumbsItem.of(rootCaption, "/"));
        return;
      }
    }

//    if (Files.isRegularFile(filePath)) {
//      filePath = filePath.toFile().getParentFile().toPath();
//    }

    var added = filePath;
    {
      BreadcrumbsItem i = new BreadcrumbsItem();
      i.caption   = Toc.toCaption(filePath, targetExt);
      i.reference = extractRef(filePath);
      items.add(i);
    }

    while (!Files.isSameFile(workDir, filePath)) {

      if (Files.exists(filePath.resolve(tocFileName)) && !Files.isSameFile(filePath, added)) {
        BreadcrumbsItem i = new BreadcrumbsItem();
        i.caption   = Toc.toCaption(filePath, targetExt);
        i.reference = extractRef(filePath);
        items.add(i);
      }

      filePath = filePath.toFile().getParentFile().toPath();

    }

    items.add(BreadcrumbsItem.of(rootCaption, "/"));

    Collections.reverse(items);
  }

  private String extractRef(Path filePath) {

    if (!Files.isDirectory(filePath)) {
      return toReference(filePath);
    }

    File   file       = filePath.toFile();
    String name       = file.getName();
    Path   targetPath = file.getParentFile().toPath().resolve(name + targetExt);
    if (Files.isRegularFile(targetPath)) {
      return toReference(targetPath);
    }

    return toReference(filePath);
  }

  private String toReference(Path filePath) {
    String reference = workDir.toFile().toURI().relativize(filePath.toUri()).toString();
    if (reference.endsWith("/")) {
      reference = reference.substring(0, reference.length() - 1);
    }
    return "/" + reference;
  }
}
