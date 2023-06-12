package kz.greetgo.md_reader.controller;

import am.ik.marked4j.Marked;
import am.ik.marked4j.MarkedBuilder;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import kz.greetgo.md_reader.core.Breadcrumbs;
import kz.greetgo.md_reader.core.DirList;
import kz.greetgo.md_reader.core.MimeTypeManager;
import kz.greetgo.md_reader.core.Toc;
import kz.greetgo.md_reader.core.env.Env;
import kz.greetgo.md_reader.core.sitemap.Sitemap;
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

  private static final String FAVICON    = "/favicon.ico";
  private static final String ROBOTS_TXT = "robots.txt";
  private static final String SITEMAPS   = "/sitemaps/";

  @SneakyThrows
  @GetMapping("/**")
  public String request(ServletWebRequest request, Model model, HttpServletResponse response) {
    String requestURI = request.getRequest().getRequestURI();

    if ("/".equals(requestURI)) {
      return "redirect:" + Env.uriTop();
    }

    if (("/" + ROBOTS_TXT).equals(requestURI)) {
      return robots(response);
    }

    if (FAVICON.equals(requestURI)) {
      try (InputStream inputStream = getClass().getResourceAsStream(FAVICON)) {
        if (inputStream != null) {
          String contentType = MimeTypeManager.probeMimeType(FAVICON);
          response.setHeader("Content-Type", contentType);
          response.getOutputStream().write(inputStream.readAllBytes());
          response.flushBuffer();
        }
        return null;
      }
    }

    if (requestURI.toLowerCase().startsWith(SITEMAPS)) {
      return sitemap(response, requestURI.substring(SITEMAPS.length()));
    }

    String uriNoBorderSlash = cutBorderSlash(requestURI);

    Path workDir = Env.workDir();

    Path filePath = workDir.resolve(uriNoBorderSlash);

    if (uriNoBorderSlash.startsWith("static/")) {
      String resourcePath = "/" + uriNoBorderSlash;

      try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {

        if (inputStream == null) {
          return noFile(filePath, request, model, cutBorderSlash(resourcePath));
        }

        String contentType = MimeTypeManager.probeMimeType(resourcePath);
        response.setHeader("Content-Type", contentType);
        response.getOutputStream().write(inputStream.readAllBytes());
        response.flushBuffer();
        return null;
      }

    }

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
      return noFile(filePath, request, model, uriNoBorderSlash);
    }

    for (String ext : new String[]{".md", ".MD"}) {
      Path filePathExt = workDir.resolve(uriNoBorderSlash + ext);
      if (Files.exists(filePathExt)) {
        return "redirect:/" + uriNoBorderSlash + ext;
      }
    }

    return noFile(filePath, request, model, uriNoBorderSlash);
  }


  @SneakyThrows
  private String renderMarkdownFile(Path filePath, Model model, String uriNoBorderSlash) {
    appendCommonAttributes(filePath, model, uriNoBorderSlash);

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

  private String noFile(Path filePath, ServletWebRequest request, Model model, String uriNoBorderSlash) {

    appendCommonAttributes(filePath, model, uriNoBorderSlash);

    String uri = request.getRequest().getRequestURI();
    model.addAttribute("resourceName", "/" + uriNoBorderSlash);
    model.addAttribute("requestUri", uri);
    model.addAttribute("title", "Ой, ресурс не найден: " + new File(uriNoBorderSlash).getName());
    return "noFile";
  }

  private String listDirectory(Path filePath, Model model, String uriNoBorderSlash) {

    appendCommonAttributes(filePath, model, uriNoBorderSlash);

    DirList dirList = new DirList();
    dirList.workDir   = Env.workDir();
    dirList.dir       = filePath;
    dirList.targetExt = ".md";

    dirList.populate();

    model.addAttribute("dirItems", dirList.items);

    return "listDirectory";
  }

  private void appendCommonAttributes(Path filePath, Model model, String uriNoBorderSlash) {
    appendToc(model, uriNoBorderSlash);
    appendBreadcrumbs(model, uriNoBorderSlash);

    String caption = Toc.toCaption(filePath, ".md");
    model.addAttribute("caption", caption);
    model.addAttribute("title", Env.headerCaption() + " - " + caption);
    model.addAttribute("headerCaption", Env.headerCaption());
  }

  private void appendBreadcrumbs(Model model, String uriNoBorderSlash) {
    Breadcrumbs b = new Breadcrumbs();
    b.workDir     = Env.workDir();
    b.uriNoSlash  = uriNoBorderSlash;
    b.rootCaption = Env.breadcrumbsRoot();
    b.targetExt   = ".md";
    b.useLast     = false;

    b.populate();

    model.addAttribute("breadcrumbsItems", b.items);
  }

  @SneakyThrows
  private String robots(HttpServletResponse response) {
    StringBuilder sb = new StringBuilder();
    sb.append("Sitemap: ").append(Env.host()).append(SITEMAPS).append(Sitemap.ROOT).append("\n");

    response.addHeader("Content-Type", "text/plain");

    response.getOutputStream().write(sb.toString().getBytes(StandardCharsets.UTF_8));

    response.flushBuffer();

    return null;
  }

  private final Sitemap sitemap = new Sitemap();

  {
    sitemap.workDir        = Env.workDir();
    sitemap.uriTop         = Env.uriTop();
    sitemap.sizeLimitBytes = 45_000_000;
    sitemap.refLimit       = 50_000;
  }

  @SneakyThrows
  private String sitemap(HttpServletResponse response, String name) {

    String responseXml = sitemap.load(name);

    response.addHeader("Content-Type", "text/xml");

    response.getOutputStream().write(responseXml.getBytes(StandardCharsets.UTF_8));

    return null;
  }

}
