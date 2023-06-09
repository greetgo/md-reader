package kz.greetgo.md_reader.core.env;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

class EnvReader {

  public static Optional<String> str(String envName) {
    requireNonNull(envName, "xfP5T7hUEJ :: envName");

    {
      String value = System.getenv(envName);
      if (value != null && value.length() > 0) {
        return Optional.of(value);
      }
    }

    {
      String value = System.getProperty(envName);
      if (value != null && value.length() > 0) {
        return Optional.of(value);
      }
    }

    return Optional.empty();
  }

  public static boolean bool(String envName) {
    return str(envName).map(EnvReader::strToBool).orElse(false);
  }

  private static boolean strToBool(String str) {
    if (str == null) {
      return false;
    }
    return switch (str.trim()) {
      case "y", "yes", "1", "true", "t" -> true;
      default -> false;
    };
  }
}
