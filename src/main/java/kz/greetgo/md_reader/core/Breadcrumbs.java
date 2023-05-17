package kz.greetgo.md_reader.core;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import kz.greetgo.md_reader.model.BreadcrumbsItem;

public class Breadcrumbs {
  public Path   workDir;
  public String uriNoSlash;
  public String tocFileName = ".toc";
  public String rootCaption = "Корень";

  public List<BreadcrumbsItem> items = new ArrayList<>();

  public void populate() {

  }
}
