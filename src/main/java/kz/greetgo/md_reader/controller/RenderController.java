package kz.greetgo.md_reader.controller;

import am.ik.marked4j.Marked;
import am.ik.marked4j.MarkedBuilder;
import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import kz.greetgo.md_reader.core.MimeTypeManager;
import kz.greetgo.md_reader.core.Toc;
import kz.greetgo.md_reader.core.env.Env;
import kz.greetgo.md_reader.model.DirItem;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;

@Log
@Controller
public class RenderController {

  private static String cutBorderSlash(String requestUri) {
    if (requestUri == null) {
      requestUri = "/";
    }

    String uriNoBorderSlash = requestUri;
    while (uriNoBorderSlash.startsWith("/")) {
      uriNoBorderSlash = uriNoBorderSlash.substring(1);
    }
    while (uriNoBorderSlash.endsWith("/")) {
      uriNoBorderSlash = uriNoBorderSlash.substring(uriNoBorderSlash.length() - 1);
    }
    return uriNoBorderSlash;
  }

  @SneakyThrows
  @GetMapping("/**")
  public String request(ServletWebRequest request, Model model, HttpServletResponse response) {
    if ("/".equals(request.getRequest().getRequestURI())) {
      return "redirect:" + Env.uriTop();
    }

    String uriNoBorderSlash = cutBorderSlash(request.getRequest().getRequestURI());

    if (uriNoBorderSlash.startsWith("static/")) {
      String resourcePath = "/" + uriNoBorderSlash;

      try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {

        if (inputStream == null) {
          return noFile(request, model, cutBorderSlash(resourcePath));
        }

        String contentType = MimeTypeManager.probeMimeType(resourcePath);
        log.info(() -> "G5U3zs0Hj8 :: resourcePath = " + resourcePath + ", contentType = " + contentType);
        response.setHeader("Content-Type", contentType);
        response.getOutputStream().write(inputStream.readAllBytes());
        response.flushBuffer();
        return null;
      }

    }

    Path workDir = Env.workDir();

    Path filePath = workDir.resolve(uriNoBorderSlash);

    String contentType = MimeTypeManager.probeMimeType(filePath.toFile().getName());

    if (Files.exists(filePath)) {
      if (Files.isDirectory(filePath)) {
        return listDirectory(filePath, model, uriNoBorderSlash);
      }
      if ("text/markdown".equals(contentType)) {
        return renderMarkdownFile(filePath, model, uriNoBorderSlash);
      }
      {
        if (contentType != null) {
          response.setHeader("Content-Type", contentType);
        }

        Files.copy(filePath, response.getOutputStream());
        response.flushBuffer();
        return null;
      }
    }

    if (uriNoBorderSlash.toLowerCase().endsWith(".md")) {
      return noFile(request, model, uriNoBorderSlash);
    }

    for (String ext : new String[]{".md", ".MD"}) {
      Path filePathExt = workDir.resolve(uriNoBorderSlash + ext);
      if (Files.exists(filePathExt)) {
        return "redirect:/" + uriNoBorderSlash + ext;
      }
    }

    return noFile(request, model, uriNoBorderSlash);
  }

  @SneakyThrows
  private String renderMarkdownFile(Path filePath, Model model, String uriNoBorderSlash) {
    model.addAttribute("filePath", filePath);
    model.addAttribute("title", "Документация по MyBPM");

    appendToc(model, uriNoBorderSlash);

    String fileText = Files.readString(filePath);

    try (Marked marked = new MarkedBuilder().gfm(true).build()) {

      String html = marked.marked(fileText);

      model.addAttribute("html", html);
      return "renderMarkdownFile";

    }
  }

  private void appendToc(Model model, String uriNoBorderSlash) {
    Toc toc = new Toc();
    toc.workDir     = Env.workDir();
    toc.targetExt   = ".md";
    toc.uriNoSlash  = uriNoBorderSlash;
    toc.tocFileName = ".toc";

    toc.populate();

    model.addAttribute("tocItems", toc.items);
  }

  private String noFile(ServletWebRequest request, Model model, String uriNoBorderSlash) {
    String uri = request.getRequest().getRequestURI();
    model.addAttribute("uriNoBorderSlash", uriNoBorderSlash);
    model.addAttribute("requestUri", uri);
    model.addAttribute("title", "Ой, не найден файл");
    log.warning(() -> "lHs1st8YWy :: No file " + uriNoBorderSlash);
    return "noFile";
  }

  private String listDirectory(Path dirPath, Model model, String uriNoBorderSlash) {
    model.addAttribute("title", "List directory");
    model.addAttribute("dirPath", dirPath);

    appendToc(model, uriNoBorderSlash);

    String caption = Toc.toCaption(dirPath, ".md");
    model.addAttribute("caption", caption);

    List<DirItem> items = new ArrayList<>();

    items.add(DirItem.of("Caption 001", "/reference1"));
    items.add(DirItem.of("Caption 002", "/reference3"));
    items.add(DirItem.of("Caption 003", "/reference4"));
    items.add(DirItem.of("Caption 004", "/reference5"));

    model.addAttribute("dirItems", items);
    return "listDirectory";
  }

}
