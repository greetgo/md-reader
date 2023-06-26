package kz.greetgo.md_reader.core.md_converter_data;

import java.io.InputStream;
import java.util.Objects;
import lombok.SneakyThrows;

public class MdConverterAnchor {
  @SneakyThrows
  public byte[] asBytes(String name) {
    try (InputStream resourceAsStream = getClass().getResourceAsStream(name)) {
      Objects.requireNonNull(resourceAsStream, "NE5MVC0dod :: No resource " + name);
      return resourceAsStream.readAllBytes();
    }
  }
}
