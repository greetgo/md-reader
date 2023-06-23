package kz.greetgo.md_reader.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MdConverterTest extends MdConverterTestParent {

  @Test
  void prepareMdFileList() {

    file("some/toc2/.toc");
    file("some/toc2/left_root1.md");
    file("some/toc2/left_root1/cap11.md");
    file("some/toc2/left_root1/cap12.md");
    file("some/toc2/left_root1/.hidden_file.txt");
    file("some/toc2/left_root2.md");
    file("some/toc2/left_root2/cap21.md");

    file("some/toc1/.toc");
    Path f0 = file("some/toc1/good_root1.md");
    Path f1 = file("some/toc1/good_root1/cap11.md");
    Path f2 = file("some/toc1/good_root1/cap12.md");
    Path f3 = file("some/toc1/good_root1/cap13.md");
    Path f4 = file("some/toc1/good_root2.md");
    Path f5 = file("some/toc1/good_root2/01_cap21.md");
    Path f6 = file("some/toc1/good_root2/02_cap22.md");
    Path f7 = file("some/toc1/good_root2/03_stone/cap23.md");
    Path f8 = file("some/toc1/good_root2/03_stone/cap24.md");
    Path f9 = file("some/toc1/good_root2/04_cap25.md");

    converter.toc.uriNoSlash = "some/toc1/good_root1/cap12.md";

    converter.toc.populate();

    //
    //
    converter.prepareMdFileList();
    //
    //

    List<String> fileList = converter.mdFileList;

    assertIsSameFile(converter.parentPath, fileList.get(0), f0);
    assertIsSameFile(converter.parentPath, fileList.get(1), f1);
    assertIsSameFile(converter.parentPath, fileList.get(2), f2);
    assertIsSameFile(converter.parentPath, fileList.get(3), f3);
    assertIsSameFile(converter.parentPath, fileList.get(4), f4);
    assertIsSameFile(converter.parentPath, fileList.get(5), f5);
    assertIsSameFile(converter.parentPath, fileList.get(6), f6);
    assertIsSameFile(converter.parentPath, fileList.get(7), f7);
    assertIsSameFile(converter.parentPath, fileList.get(8), f8);
    assertIsSameFile(converter.parentPath, fileList.get(9), f9);

    assertThat(fileList).hasSize(10);
  }

  @SneakyThrows
  private void assertIsSameFile(Path parent, String actualFile, Path expectedFile) {
    assertThat(Files.isSameFile(parent.resolve(actualFile), expectedFile))
      .describedAs("\nActual   File : " + actualFile + "\nExpected File : " + expectedFile)
      .isTrue();
  }

}
