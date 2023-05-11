package kz.greetgo.md_reader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;

@Controller
public class RenderController {
  @GetMapping("/*")
  public String root(ServletWebRequest request, Model model) {
    String uri = request.getRequest().getRequestURI();
    model.addAttribute("uri", uri);
    return "show";
  }

//  @GetMapping("/index")
//  public String top() {
//    return "redirect:" + Env.uriTop();
//  }
}
