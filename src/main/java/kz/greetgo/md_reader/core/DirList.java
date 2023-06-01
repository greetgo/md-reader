package kz.greetgo.md_reader.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import kz.greetgo.md_reader.model.DirItem;
import lombok.SneakyThrows;

public class DirList {
  public Path   workDir;
  public Path   dir;
  public String targetExt = ".md";

  public List<DirItem> items = new ArrayList<>();

  @SneakyThrows
  private List<Path> listDir() {
    try (Stream<Path> list = Files.list(dir)) {
      return list.toList();

    }
  }

  public void populate() {


    for (final Path itemPath : listDir()) {

      if (itemPath.toFile().getName().startsWith(".")) {
        continue;
      }

      if (Files.isDirectory(itemPath)) {

        Path   parent = itemPath.toFile().getParentFile().toPath();
        String mdName = itemPath.toFile().getName() + targetExt;

        Path newPath = parent.resolve(mdName);

        if (Files.isRegularFile(newPath)) {
          continue;
        }

        if (Files.isRegularFile(itemPath.resolve(Toc.HIDE_THIS_DIR))) {
          continue;
        }

      }

      if (Files.isRegularFile(itemPath) && !itemPath.toFile().getName().endsWith(targetExt)) {
        continue;
      }

      DirItem item = new DirItem();
      item.caption = Toc.toCaption(itemPath, targetExt);

      {
        String reference = workDir.toFile().toURI().relativize(itemPath.toUri()).toString();
        if (reference.endsWith("/")) {
          reference = reference.substring(0, reference.length() - 1);
        }
        item.reference = "/" + reference;
      }

      items.add(item);

    }

    items.sort(Comparator.comparing(x -> x.reference));

  }
}
