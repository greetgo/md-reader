package kz.greetgo.md_reader.controller;

import am.ik.marked4j.Marked;
import am.ik.marked4j.MarkedBuilder;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import kz.greetgo.md_reader.core.Breadcrumbs;
import kz.greetgo.md_reader.core.DirList;
import kz.greetgo.md_reader.core.MdConverter;
import kz.greetgo.md_reader.core.MimeTypeManager;
import kz.greetgo.md_reader.core.Toc;
import kz.greetgo.md_reader.core.env.Env;
import kz.greetgo.md_reader.core.sitemap.Sitemap;
import kz.greetgo.md_reader.util.DownUtil;
import kz.greetgo.md_reader.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;

import static kz.greetgo.md_reader.core.sitemap.Sitemap.SITEMAPS;

@Log
@Controller
public class RenderController {

  private static final String FAVICON      = "/favicon.ico";
  private static final String LOGO         = "/logo.png";
  private static final String ROBOTS_TXT   = "robots.txt";
  private static final String DOWNLOAD_TOC = "__download_toc";

  @SneakyThrows
  @GetMapping("/**")
  public String request(ServletWebRequest request, Model model, HttpServletResponse response) {
    String requestURI = request.getRequest().getRequestURI();

    if ("/".equals(requestURI)) {
      return "redirect:" + Env.uriTop();
    }
    if ("/yandex_5de2b922f288eb40.html".equals(requestURI)) {
      return yandexVerify(response);
    }

    if (("/" + ROBOTS_TXT).equals(requestURI)) {
      return robots(response);
    }

    if (FAVICON.equals(requestURI)) {
      //noinspection DuplicatedCode
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

    if (LOGO.equals(requestURI)) {
      //noinspection DuplicatedCode
      try (InputStream inputStream = getClass().getResourceAsStream(LOGO)) {
        if (inputStream != null) {
          String contentType = MimeTypeManager.probeMimeType(LOGO);
          response.setHeader("Content-Type", contentType);
          response.getOutputStream().write(inputStream.readAllBytes());
          response.flushBuffer();
        }
        return null;
      }
    }

    String lowRequestUri = requestURI.toLowerCase();

    if (lowRequestUri.startsWith("/" + SITEMAPS + "/")) {
      return sitemap(response, requestURI.substring(SITEMAPS.length() + 2));
    }

    {
      String prefix = "/" + DOWNLOAD_TOC + "/";
      if (lowRequestUri.startsWith(prefix)) {
        String rest             = requestURI.substring(prefix.length());
        String uriNoBorderSlash = StrUtil.cutBorderSlash(rest);
        return downloadToc(uriNoBorderSlash, response);
      }
    }

    String uriNoBorderSlash = StrUtil.cutBorderSlash(requestURI);

    Path workDir = Env.workDir();

    Path filePath = workDir.resolve(uriNoBorderSlash);

    if (uriNoBorderSlash.startsWith("static/")) {
      String resourcePath = "/" + uriNoBorderSlash;

      try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {

        if (inputStream == null) {
          return noFile(filePath, request, model, StrUtil.cutBorderSlash(resourcePath));
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

  private Toc populateToc(String uriNoBorderSlash) {
    Toc toc = new Toc();
    toc.workDir     = Env.workDir();
    toc.targetExt   = ".md";
    toc.uriNoSlash  = uriNoBorderSlash;
    toc.tocFileName = ".toc";

    toc.populate();

    return toc;
  }

  private void appendToc(Model model, String uriNoBorderSlash) {

    Toc toc = populateToc(uriNoBorderSlash);

    model.addAttribute("tocItems", toc.items);
    model.addAttribute("tocDownloadReference", "/" + DOWNLOAD_TOC + "/" + uriNoBorderSlash);
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
    model.addAttribute("title", caption + " - " + Env.headerCaption());
    model.addAttribute("headerCaption", Env.headerCaption());
    model.addAttribute("host", Env.host());
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
    sb.append("Sitemap: ").append(Env.host()).append('/').append(SITEMAPS).append('/').append(Sitemap.ROOT).append("\n");

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
    sitemap.host           = Env.host();
  }

  @SneakyThrows
  private String sitemap(HttpServletResponse response, String name) {

    String responseXml = sitemap.load(name);

    response.addHeader("Content-Type", "text/xml");

    response.getOutputStream().write(responseXml.getBytes(StandardCharsets.UTF_8));

    return null;
  }

  public static String makeOptions(String bold, String italic, String boldItalic) {
    LinkedHashMap<String, String> m = new LinkedHashMap<>();
    m.put("BoldFont", bold);
    m.put("ItalicFont", italic);
    m.put("BoldItalicFont", boldItalic);

    return m.entrySet()
            .stream()
            .map(x -> x.getKey() + "=" + x.getValue())
            .collect(Collectors.joining(", "));
  }

  @SneakyThrows
  private void copyRes(String resName, Path targetFile) {
    try (InputStream resourceAsStream = getClass().getResourceAsStream(resName)) {
      if (resourceAsStream == null) {
        return;
      }

      File target = targetFile.toFile();
      target.getParentFile().mkdirs();

      try (FileOutputStream out = new FileOutputStream(target)) {
        out.write(resourceAsStream.readAllBytes());
      }
    }
  }

  @SneakyThrows
  private String downloadToc(String uriNoBorderSlash, HttpServletResponse response) {

    Toc toc = populateToc(uriNoBorderSlash);

    try (MdConverter converter = new MdConverter()) {
      converter.toc         = toc;
      converter.tmpDir      = Env.tmpDir();
      converter.clearTmpDir = Env.clearTmpDir();

      converter.convert();

      DownUtil.downloadFile(response,
                            converter.downloadFile,
                            converter.contentType,
                            converter.downloadFileName);
    }

    return null;
  }

  @SneakyThrows
  private String yandexVerify(HttpServletResponse response) {
    StringBuilder sb = new StringBuilder();
    sb.append("""
                <html>
                    <head>
                        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                    </head>
                    <body>Verification: 5de2b922f288eb40</body>
                </html>
                """);

    response.addHeader("Content-Type", "text/html; charset=UTF-8");

    response.getOutputStream().write(sb.toString().getBytes(StandardCharsets.UTF_8));

    response.flushBuffer();

    return null;
  }


}
