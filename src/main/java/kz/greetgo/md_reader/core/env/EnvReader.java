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

}
