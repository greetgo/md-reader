package kz.greetgo.md_reader.core;

import org.junit.jupiter.api.Test;

class DirListTest extends DirListTestParent {

  @Test
  void populate_001() {

    file("status/work/item1.md");
    file("status/work/item2.md");
    file("status/work/item3/a1.md");
    file("status/work/item3/a2.md");
    file("status/work/item4.md");

    dt.dir = dt.workDir.resolve("status/work");

    //
    //
    dt.populate();
    //
    //

    assertItem("6mooYo1sP4", 0, "item1", "/status/work/item1.md");
    assertItem("5zDHl1hJL1", 1, "item2", "/status/work/item2.md");
    assertItem("QqZuCN5Gcc", 2, "item3", "/status/work/item3");
    assertItem("44UNJs7VLD", 3, "item4", "/status/work/item4.md");
  }

  @Test
  void populate_002() {

    file("status/work/001_item1.md");
    file("status/work/002_item2.md");
    file("status/work/003_item3/a1.md");
    file("status/work/003_item3/a2.md");
    file("status/work/_004_item4.md");

    dt.dir = dt.workDir.resolve("status/work");

    //
    //
    dt.populate();
    //
    //

    assertItem("sQDF20FjkZ", 0, "item1", "/status/work/001_item1.md");
    assertItem("eOglUTj5YT", 1, "item2", "/status/work/002_item2.md");
    assertItem("EU0G1371dS", 2, "item3", "/status/work/003_item3");
    assertItem("kL9akEt6t2", 3, "item4", "/status/work/_004_item4.md");
  }

  @Test
  void populate_003() {

    file("status/work/001_item1.md");
    file("status/work/002_item2.md");
    file("status/work/003_item3/a1.md");
    file("status/work/003_item3/a2.md");
    file("status/work/003_item3.md");
    file("status/work/_004_item4.md");

    dt.dir = dt.workDir.resolve("status/work");

    //
    //
    dt.populate();
    //
    //

    assertItem("b1dsH29s0A", 0, "item1", "/status/work/001_item1.md");
    assertItem("UIgDjk10xC", 1, "item2", "/status/work/002_item2.md");
    assertItem("Q7JKEg108I", 2, "item3", "/status/work/003_item3.md");
    assertItem("2Y3kC4LZVg", 3, "item4", "/status/work/_004_item4.md");
  }

  @Test
  void populate_004() {

    file("status/work/001_item1.md");
    file("status/work/002_item2.md");
    file("status/work/003_item3.md", "## Привет мир");
    file("status/work/_004_item4.md");

    dt.dir = dt.workDir.resolve("status/work");

    //
    //
    dt.populate();
    //
    //

    assertItem("z4BfIO1WJO", 0, "item1", "/status/work/001_item1.md");
    assertItem("AUN8QkuI5e", 1, "item2", "/status/work/002_item2.md");
    assertItem("9kvI26er3j", 2, "Привет мир", "/status/work/003_item3.md");
    assertItem("DH1497KVu2", 3, "item4", "/status/work/_004_item4.md");
  }

  @Test
  void populate_005() {

    file("status/work/001_item1.md");
    file("status/work/002_item2.md");
    file("status/work/003_item3/a1.md");
    file("status/work/003_item3/a2.md");
    file("status/work/_004_item4.md");

    dt.dir = dt.workDir.resolve("status/work");

    //
    //
    dt.populate();
    //
    //

    assertItem("b1dsH29s0A", 0, "item1", "/status/work/001_item1.md");
    assertItem("UIgDjk10xC", 1, "item2", "/status/work/002_item2.md");
    assertItem("Q7JKEg108I", 2, "item3", "/status/work/003_item3");
    assertItem("2Y3kC4LZVg", 3, "item4", "/status/work/_004_item4.md");
  }

  @Test
  void populate_006() {

    file("status/work/item1.md");
    file("status/work/item2.md");
    file("status/work/.hidden_item.md");
    file("status/work/item3/a1.md");
    file("status/work/item3/a2.md");
    file("status/work/z_item4.md");

    dt.dir = dt.workDir.resolve("status/work");

    //
    //
    dt.populate();
    //
    //

    assertItem("6mooYo1sP4", 0, "item1", "/status/work/item1.md");
    assertItem("5zDHl1hJL1", 1, "item2", "/status/work/item2.md");
    assertItem("QqZuCN5Gcc", 2, "item3", "/status/work/item3");
    assertItem("44UNJs7VLD", 3, "z item4", "/status/work/z_item4.md");
  }

  @Test
  void populate_007() {

    file("status/work/item1.md");
    file("status/work/item2.md");
    file("status/work/001_left_ext.jpg");
    file("status/work/item3/a1.md");
    file("status/work/item3/a2.md");
    file("status/work/item4.md");

    dt.dir = dt.workDir.resolve("status/work");

    //
    //
    dt.populate();
    //
    //

    assertItem("6mooYo1sP4", 0, "item1", "/status/work/item1.md");
    assertItem("5zDHl1hJL1", 1, "item2", "/status/work/item2.md");
    assertItem("QqZuCN5Gcc", 2, "item3", "/status/work/item3");
    assertItem("44UNJs7VLD", 3, "item4", "/status/work/item4.md");
  }

}
