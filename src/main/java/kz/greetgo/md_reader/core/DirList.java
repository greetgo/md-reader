package kz.greetgo.md_reader.core;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import kz.greetgo.md_reader.model.DirItem;

public class DirList {
  public Path   workDir;
  public Path   dir;
  public String targetExt = ".md";

  public List<DirItem> items = new ArrayList<>();

  public void populate() {

  }
}
