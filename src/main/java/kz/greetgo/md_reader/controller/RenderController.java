package kz.greetgo.md_reader.controller;

import am.ik.marked4j.Marked;
import am.ik.marked4j.MarkedBuilder;
import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import kz.greetgo.md_reader.core.MimeTypeManager;
import kz.greetgo.md_reader.core.env.Env;
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
        return listDirectory(filePath, model);
      }
      if ("text/markdown".equals(contentType)) {
        return renderMarkdownFile(filePath, model);
      }
      {
        if (contentType != null) {
          response.setHeader("Content-Type", contentType);
        }
        response.getOutputStream().write(Files.readAllBytes(filePath));
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
  private String renderMarkdownFile(Path filePath, Model model) {
    model.addAttribute("filePath", filePath);

    String fileText = Files.readString(filePath);

    try (Marked marked = new MarkedBuilder().gfm(true).build()) {

      String html = marked.marked(fileText);

      model.addAttribute("html", html);
      return "renderMarkdownFile";

    }
  }

  private String noFile(ServletWebRequest request, Model model, String uriNoBorderSlash) {
    String uri = request.getRequest().getRequestURI();
    model.addAttribute("uriNoBorderSlash", uriNoBorderSlash);
    model.addAttribute("requestUri", uri);
    return "noFile";
  }

  private String listDirectory(Path dirPath, Model model) {
    model.addAttribute("dirPath", dirPath);
    return "listDirectory";
  }

}
