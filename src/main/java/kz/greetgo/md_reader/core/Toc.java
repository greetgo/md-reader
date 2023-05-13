package kz.greetgo.md_reader.core;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import kz.greetgo.md_reader.model.TocItem;

public class Toc {

  public Path   workDir;
  public String uriNoSlash;
  public String ext;

  public List<TocItem> items = new ArrayList<>();

  public void populate() {

  }

}
