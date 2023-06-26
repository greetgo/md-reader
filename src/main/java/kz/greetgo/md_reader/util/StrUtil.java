package kz.greetgo.md_reader.util;

public class StrUtil {
  public static String cutBorderSlash(String requestUri) {
    if (requestUri == null) {
      requestUri = "/";
    }

    String uriNoBorderSlash = requestUri;
    while (uriNoBorderSlash.startsWith("/")) {
      uriNoBorderSlash = uriNoBorderSlash.substring(1);
    }
    while (uriNoBorderSlash.length() > 1 && uriNoBorderSlash.endsWith("/")) {
      uriNoBorderSlash = uriNoBorderSlash.substring(uriNoBorderSlash.length() - 1);
    }
    return uriNoBorderSlash;
  }

  private static final String SPACES = "00000000000000000000000000000000000000000000000000000000000000000000000000000000";

  public static String toLen(long i, int len) {
    if (len > SPACES.length()) {
      throw new RuntimeException("6ehT3OJTJt :: len must be <= " + SPACES.length());
    }
    String        s  = "" + i;
    StringBuilder sb = new StringBuilder(10);
    sb.append(s);
    int sLen = s.length();
    if (sLen < len) {
      sb.insert(0, SPACES, 0, len - sLen);
    }
    return sb.toString();
  }

}
