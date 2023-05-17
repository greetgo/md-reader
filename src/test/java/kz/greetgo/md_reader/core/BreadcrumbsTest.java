package kz.greetgo.md_reader.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BreadcrumbsTest extends BreadcrumbsTestParent {

  @Test
  void populate_001() {

    file("status/work/item1.md");
    file("status/work/item2.md");
    file("status/work/item3/a1.md");
    file("status/work/item3/a2.md");
    file("status/work/item4.md");

    b.rootCaption = "Root Test";
    b.uriNoSlash  = "status/work/item3/a1.md";

    //
    //
    b.populate();
    //
    //

    assertItem("nb9o7sRC9k", 0, "Root Test", "/");
    assertItem("DD8V8Z2B7w", 1, "a1", "/status/work/item3/a1.md");

    assertThat(b.items).hasSize(2);
  }

  @Test
  void populate_002() {

    file("status/hi/work.md", "## Work Title 0G5j1Av8EH");
    file("status/hi/work/.toc");
    file("status/hi/work/item1.md");
    file("status/hi/work/item2.md");
    file("status/hi/work/item3/a1.md");
    file("status/hi/work/item3/a2.md");
    file("status/hi/work/item3/in_toc.md", "## In toc title e7RLBaWY");
    file("status/hi/work/item3/in_toc/.toc");
    file("status/hi/work/item3/in_toc/i1.md");
    file("status/hi/work/item3/in_toc/i2.md");
    file("status/hi/work/item3/in_toc/sin/state.md");
    file("status/hi/work/item4.md");
    file("status/hi/bond.md");
    file("status/hi/bond/.toc");
    file("status/hi/bond/hello.md");

    b.rootCaption = "Root Test1 DT7A1Epw93";
    b.uriNoSlash  = "status/hi/work/item3/in_toc/sin/state.md";

    //
    //
    b.populate();
    //
    //

    assertItem("P88dEt4Yvy", 0, "Root Test1 DT7A1Epw93", "/");
    assertItem("4gQ9JxLZ5q", 1, "Work Title 0G5j1Av8EH", "/status/hi/work.md");
    assertItem("UZ7JChUD7I", 2, "In toc title e7RLBaWY", "/status/hi/work/item3/in_toc.md");
    assertItem("179LVTv1cx", 3, "state", "/status/hi/work/item3/in_toc/sin/state.md");

    assertThat(b.items).hasSize(4);
  }

  @Test
  void populate_003() {

    file("status/hi/work.md", "## Work Title ejNQB5ZA5h");
    file("status/hi/work/.toc");
    file("status/hi/work/item1.md");
    file("status/hi/work/item2.md");
    file("status/hi/work/item3/a1.md");
    file("status/hi/work/item3/a2.md");
    file("status/hi/work/item3/in_toc.md", "## In toc title 5ZXZ7kc9");
    file("status/hi/work/item3/in_toc/.toc");
    file("status/hi/work/item3/in_toc/i1.md");
    file("status/hi/work/item3/in_toc/i2.md");
    file("status/hi/work/item3/in_toc/sin/state.md");
    file("status/hi/work/item4.md");

    b.rootCaption = "Root Test1 M124PFst3l";
    b.uriNoSlash  = "status/hi/work/item3/in_toc/sin/left_ref/ref_left.md";

    //
    //
    b.populate();
    //
    //

    assertItem("qi7zBJnTSi", 0, "Root Test1 M124PFst3l", "/");
    assertItem("1303M3x67R", 1, "Work Title ejNQB5ZA5h", "/status/hi/work.md");
    assertItem("6O8gjy4buZ", 2, "In toc title 5ZXZ7kc9", "/status/hi/work/item3/in_toc.md");
    assertItem("GXC8a7r3hT", 3, "sin", "/status/hi/work/item3/in_toc/sin");

    assertThat(b.items).hasSize(4);
  }

  @Test
  void populate_004() {

    file("status/hi/work/.toc");
    file("status/hi/work/item1.md");
    file("status/hi/work/item2.md");
    file("status/hi/work/item3/a1.md");
    file("status/hi/work/item3/a2.md");
    file("status/hi/work/item3/in_toc.md", "## In toc title e7RLBaWY");
    file("status/hi/work/item3/in_toc/.toc");
    file("status/hi/work/item3/in_toc/i1.md");
    file("status/hi/work/item3/in_toc/i2.md");
    file("status/hi/work/item3/in_toc/sin/state.md");
    file("status/hi/work/item4.md");
    file("status/hi/bond.md");
    file("status/hi/bond/.toc");
    file("status/hi/bond/hello.md");

    b.rootCaption = "Root Test1 DT7A1Epw93";
    b.uriNoSlash  = "status/hi/work/item3/in_toc/sin/state.md";

    //
    //
    b.populate();
    //
    //

    assertItem("G4KzJ0iWzH", 0, "Root Test1 DT7A1Epw93", "/");
    assertItem("nSSI0ve9WB", 1, "work" /*            */, "/status/hi/work");
    assertItem("Ntp2guZYdt", 2, "In toc title e7RLBaWY", "/status/hi/work/item3/in_toc.md");
    assertItem("cKDi1n0FTE", 3, "state", "/status/hi/work/item3/in_toc/sin/state.md");

    assertThat(b.items).hasSize(4);
  }
}
