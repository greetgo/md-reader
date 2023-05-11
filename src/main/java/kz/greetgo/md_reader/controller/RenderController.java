package kz.greetgo.md_reader.controller;

import am.ik.marked4j.Marked;
import am.ik.marked4j.MarkedBuilder;
import java.nio.file.Files;
import java.nio.file.Path;
import kz.greetgo.md_reader.core.env.Env;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;

@Controller
public class RenderController {

  @GetMapping("/**")
  public String request(ServletWebRequest request, Model model) {
    String requestUri = request.getRequest().getRequestURI();
    if (requestUri == null) {
      requestUri = "/";
    }
    if ("/".equals(requestUri)) {
      return "redirect:" + Env.uriTop();
    }

    String uriNoBorderSlash = requestUri;
    while (uriNoBorderSlash.startsWith("/")) {
      uriNoBorderSlash = uriNoBorderSlash.substring(1);
    }
    while (uriNoBorderSlash.endsWith("/")) {
      uriNoBorderSlash = uriNoBorderSlash.substring(uriNoBorderSlash.length() - 1);
    }

    Path workDir = Env.workDir();

    Path filePath = workDir.resolve(uriNoBorderSlash);

    if (Files.exists(filePath)) {
      if (Files.isDirectory(filePath)) {
        return listDirectory(filePath, model);
      }
      return renderMarkdownFile(filePath, model);
    }

    if (uriNoBorderSlash.toLowerCase().endsWith(".md")) {
      return noFile(request, model, uriNoBorderSlash);
    }

    for (String ext : new String[]{".md", ".MD"}) {
      Path filePathExt = workDir.resolve(uriNoBorderSlash + ext);
      if (Files.exists(filePathExt)) {
        return renderMarkdownFile(filePathExt, model);
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
      return "renderFile";

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
