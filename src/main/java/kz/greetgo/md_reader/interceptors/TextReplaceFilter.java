package kz.greetgo.md_reader.interceptors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class TextReplaceFilter extends GenericFilterBean {

  private static final ThreadLocal<Boolean> skipConvert = new ThreadLocal<>();

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    skipConvert.set(false);

    String requestURI = ((HttpServletRequest) request).getRequestURI();

    if (!isText(requestURI)) {
      chain.doFilter(request, response);
      return;
    }

    {
      ResponseConverter brw = new ResponseConverter((HttpServletResponse) response);
      chain.doFilter(request, brw);

      if (skipConvert.get()) {
        response.getOutputStream().write(brw.getBytes());
        return;
      }

      String str = new String(brw.getBytes(), StandardCharsets.UTF_8);

      response.getWriter().print(convertText(str));
      return;
    }

  }

  public static void skipConvert() {
    skipConvert.set(true);
  }

  private static final Pattern PATTERN_OPEN1 = Pattern.compile("<\\s*a\\s+href\\s*=\\s*\"([^\"]+)\"\\s*>");
  private static final Pattern PATTERN_OPEN2 = Pattern.compile("<\\s*a\\s+href\\s*=\\s*'([^']+)'\\s*>");
  private static final Pattern PATTERN_CLOSE = Pattern.compile("<\\s*/\\s*a\\s*>");

  static String convertText(String text) {

    StringBuilder sb = new StringBuilder(text.length() + 1000);

    Set<String> passedHrefs = new HashSet<>();

    while (true) {
      String href1     = null, href2 = null;
      String rest1     = null, rest2 = null;
      String left1     = null, left2 = null;
      String find1     = null, find2 = null;
      int    href1_pos = 0, href2_pos = 0;
      {
        Matcher matcher = PATTERN_OPEN1.matcher(text);
        if (matcher.find()) {
          href1_pos = matcher.start(1);
          href1     = matcher.group(1);
          rest1     = text.substring(matcher.end());
          left1     = text.substring(0, matcher.start());
          find1     = matcher.group();
        }
      }
      {
        Matcher matcher = PATTERN_OPEN2.matcher(text);
        if (matcher.find()) {
          href2_pos = matcher.start(1);
          href2     = matcher.group(1);
          rest2     = text.substring(matcher.end());
          left2     = text.substring(0, matcher.start());
          find2     = matcher.group();
        }
      }

      if (href1 == null && href2 == null) {
        sb.append(text);
        return sb.toString();
      }

      final String href, rest, left, find;
      final char   kav;

      if (href1 == null) {
        href = href2;
        rest = rest2;
        left = left2;
        find = find2;
        kav  = '\'';
      } else if (href2 == null) {
        href = href1;
        rest = rest1;
        left = left1;
        find = find1;
        kav  = '"';
      } else {
        href = href1_pos < href2_pos ? href1 : href2;
        rest = href1_pos < href2_pos ? rest1 : rest2;
        left = href1_pos < href2_pos ? left1 : left2;
        find = href1_pos < href2_pos ? find1 : find2;
        kav  = href1_pos < href2_pos ? '"' : '\'';
      }

      Matcher closer = PATTERN_CLOSE.matcher(rest);
      if (!closer.find()) {
        sb.append(text);
        return sb.toString();
      }

      String content = rest.substring(0, closer.start());

      if (passedHrefs.contains(href)) {

        text = rest.substring(closer.end());

        sb.append(left).append("<md-ref href=").append(kav).append(href).append(kav).append(">").append(content).append("</md-ref>");

        continue;
      }

      {
        passedHrefs.add(href);

        sb.append(left).append(find).append(content).append(closer.group());

        text = rest.substring(closer.end());
      }

    }//while(true)
  }

  private boolean isText(String requestURI) {
    int idx = requestURI.lastIndexOf("/");

    final String last;

    if (idx < 0) {
      last = requestURI;
    } else {
      last = requestURI.substring(idx + 1);
    }

    String lowerLast = last.toLowerCase();
    if (lowerLast.endsWith(".md")) {
      return true;
    }

    if (last.indexOf('.') < 0) {
      return true;
    }

    return false;
  }
}
