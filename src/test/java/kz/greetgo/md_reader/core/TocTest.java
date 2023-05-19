package kz.greetgo.md_reader.core;

import java.nio.file.Path;
import kz.greetgo.md_reader.model.Settings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TocTest extends TocTestParent {

  @Test
  void populate__001() {

    file("some/toc1/root1.md");
    file("some/toc1/root1/cap11.md");
    file("some/toc1/root1/cap12.md");
    file("some/toc1/root1/.hidden_file.md");
//    file("some/toc1/root1/.hidden_dir/ok.md");
//    file("some/toc1/root1/pic.jpg");
    file("some/toc1/root2.md");
    file("some/toc1/root2/cap21.md");
    file("some/toc1/root2/stone/cap22.md");

    toc.uriNoSlash = "some/toc1/root1/cap12.md";

    //
    //
    toc.populate();
    //
    //

    assertItem("SDkYVl0L2v", 0, "some", 1, "/some", true);
    assertItem("2LPdm1zz7y", 1, "toc1", 2, "/some/toc1", true);
    assertItem("qE0ZiCAO3c", 2, "root1", 3, "/some/toc1/root1.md", true);
    assertItem("8RP9jdqWMt", 3, "cap11", 4, "/some/toc1/root1/cap11.md", false);
    assertItem("5b5h5txI51", 4, "cap12", 4, "/some/toc1/root1/cap12.md", true);
    assertItem("7jRAUds5sd", 5, "root2", 3, "/some/toc1/root2.md", false);
    assertItem("zHfm3z704g", 6, "cap21", 4, "/some/toc1/root2/cap21.md", false);
    assertItem("2emKz80AII", 7, "stone", 4, "/some/toc1/root2/stone", false);
    assertItem("S0pv3pKX42", 8, "cap22", 5, "/some/toc1/root2/stone/cap22.md", false);

  }

  @Test
  void populate__002() {

    file("some/toc2/.toc");
    file("some/toc2/left_root1.md");
    file("some/toc2/left_root1/cap11.md");
    file("some/toc2/left_root1/cap12.md");
    file("some/toc2/left_root1/.hidden_file.txt");
    file("some/toc2/left_root2.md");
    file("some/toc2/left_root2/cap21.md");

    file("some/toc1/.toc");
    file("some/toc1/good_root1.md");
    file("some/toc1/good_root1/cap11.md");
    file("some/toc1/good_root1/cap12.md");
    file("some/toc1/good_root1/cap13.md");
    file("some/toc1/good_root1/.hidden_file.txt");
    file("some/toc1/good_root2.md");
    file("some/toc1/good_root2/01_cap21.md");
    file("some/toc1/good_root2/02_cap22.md");
    file("some/toc1/good_root2/03_stone/cap23.md");
    file("some/toc1/good_root2/03_stone/cap24.md");
    file("some/toc1/good_root2/04_cap25.md");
    file("some/toc1/good_root2/05_cap26.md");


    toc.uriNoSlash = "some/toc1/good_root1/cap12.md";

    //
    //
    toc.populate();
    //
    //

    assertItem("ipd1cebM64", 0, "good root1", 1, "/some/toc1/good_root1.md", true);
    assertItem("nz99Ks6vaO", 1, "cap11", 2, "/some/toc1/good_root1/cap11.md", false);
    assertItem("4T4isq3kL3", 2, "cap12", 2, "/some/toc1/good_root1/cap12.md", true);
    assertItem("BH5f8kafKo", 3, "cap13", 2, "/some/toc1/good_root1/cap13.md", false);
    assertItem("uk3R1LSZcF", 4, "good root2", 1, "/some/toc1/good_root2.md", false);
    assertItem("i9jgOgDtKz", 5, "cap21", 2, "/some/toc1/good_root2/01_cap21.md", false);
    assertItem("hk1ED2n8Bj", 6, "cap22", 2, "/some/toc1/good_root2/02_cap22.md", false);
    assertItem("KpoM6aQK1Q", 7, "stone", 2, "/some/toc1/good_root2/03_stone", false);
    assertItem("2qcOi72T3T", 8, "cap23", 3, "/some/toc1/good_root2/03_stone/cap23.md", false);
    assertItem("t98l8lic3d", 9, "cap24", 3, "/some/toc1/good_root2/03_stone/cap24.md", false);
    assertItem("fyZPO1kmm", 10, "cap25", 2, "/some/toc1/good_root2/04_cap25.md", false);
    assertItem("z2R95zPX8", 11, "cap26", 2, "/some/toc1/good_root2/05_cap26.md", false);

  }

  @Test
  void populate__003() {

    file("some/toc1/.toc");
    file("some/toc1/good_root1.md");
    file("some/toc1/good_root1/cap11.md");
    file("some/toc1/good_root1/cap12.md");
    file("some/toc1/good_root1/cap13.md");
    file("some/toc1/good_root1/.hidden_file.txt");
    file("some/toc1/good_root2.md");
    file("some/toc1/good_root2/01_cap21.md");
    file("some/toc1/good_root2/02_cap22.md");
    file("some/toc1/good_root2/03_stone/cap23.md");
    file("some/toc1/good_root2/03_stone/cap24.md");
    file("some/toc1/good_root2/04_cap25.md");
    file("some/toc1/good_root2/05_cap26.md");


    toc.uriNoSlash = "some/toc1/good_root2/03_stone/cap24.md";

    //
    //
    toc.populate();
    //
    //

    assertItem("lkH1N7PCr4", 0, "good root1", 1, "/some/toc1/good_root1.md", false);
    assertItem("4o1nd69687", 1, "cap11", 2, "/some/toc1/good_root1/cap11.md", false);
    assertItem("FjePEj59B2", 2, "cap12", 2, "/some/toc1/good_root1/cap12.md", false);
    assertItem("224rIScX9P", 3, "cap13", 2, "/some/toc1/good_root1/cap13.md", false);
    assertItem("V59p30POv1", 4, "good root2", 1, "/some/toc1/good_root2.md", true);
    assertItem("k0pc9D4AG2", 5, "cap21", 2, "/some/toc1/good_root2/01_cap21.md", false);
    assertItem("qzkJK7tANp", 6, "cap22", 2, "/some/toc1/good_root2/02_cap22.md", false);
    assertItem("75KzJyu1R5", 7, "stone", 2, "/some/toc1/good_root2/03_stone", true);
    assertItem("R7GFD3hWO4", 8, "cap23", 3, "/some/toc1/good_root2/03_stone/cap23.md", false);
    assertItem("94QLZTdz62", 9, "cap24", 3, "/some/toc1/good_root2/03_stone/cap24.md", true);
    assertItem("WgLzPZQ1P", 10, "cap25", 2, "/some/toc1/good_root2/04_cap25.md", false);
    assertItem("QNfPxsA1H", 11, "cap26", 2, "/some/toc1/good_root2/05_cap26.md", false);

  }

  @Test
  void populate__004() {

    file("some/toc1/.toc");
    file("some/toc1/good_root1.md");
    file("some/toc1/good_root1/cap11.md");
    file("some/toc1/good_root1/cap12.md");
    file("some/toc1/good_root1/cap13.md");
    file("some/toc1/good_root1/.hidden_file.txt");
    file("some/toc1/good_root2.md");
    file("some/toc1/good_root2/01_cap21.md");
    file("some/toc1/good_root2/02_cap22.md");
    file("some/toc1/good_root2/03_stone/cap23.md");
    file("some/toc1/good_root2/03_stone/cap24.md");
    file("some/toc1/good_root2/04_cap25.md");
    file("some/toc1/good_root2/05_cap26.md");

    toc.uriNoSlash = "some/toc1/good_root2/03_stone/someAbsent.md";

    //
    //
    toc.populate();
    //
    //

    assertItem("4A8I8ijJQR", 0, "good root1", 1, "/some/toc1/good_root1.md", false);
    assertItem("H2l6fkqI6r", 1, "cap11", 2, "/some/toc1/good_root1/cap11.md", false);
    assertItem("FxwPIsKw08", 2, "cap12", 2, "/some/toc1/good_root1/cap12.md", false);
    assertItem("4hTkB661TC", 3, "cap13", 2, "/some/toc1/good_root1/cap13.md", false);
    assertItem("hmeOfx7YDm", 4, "good root2", 1, "/some/toc1/good_root2.md", true);
    assertItem("1iOoXn2hXA", 5, "cap21", 2, "/some/toc1/good_root2/01_cap21.md", false);
    assertItem("OD6IPUQnEj", 6, "cap22", 2, "/some/toc1/good_root2/02_cap22.md", false);
    assertItem("q4ahCkVm19", 7, "stone", 2, "/some/toc1/good_root2/03_stone", true);
    assertItem("9Tm1u7IS6l", 8, "cap23", 3, "/some/toc1/good_root2/03_stone/cap23.md", false);
    assertItem("Fq2vX75cr7", 9, "cap24", 3, "/some/toc1/good_root2/03_stone/cap24.md", false);
    assertItem("p974jx4n4", 10, "cap25", 2, "/some/toc1/good_root2/04_cap25.md", false);
    assertItem("g2468FO2u", 11, "cap26", 2, "/some/toc1/good_root2/05_cap26.md", false);

  }

  @Test
  void populate__005() {

    file("some/toc1/.toc");
    file("some/toc1/good_root1.md");
    file("some/toc1/good_root1/cap11.md");
    file("some/toc1/good_root2.md");
    file("some/toc1/good_root2/01_cap21.md");
    file("some/toc1/good_root2/02_cap22.md");
    file("some/toc1/good_root2/03_stone.md");
    file("some/toc1/good_root2/03_stone/.toc");
    file("some/toc1/good_root2/03_stone/01_stone_cap1.md");
    file("some/toc1/good_root2/03_stone/02_stone_cap2.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc/01_hello_world.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc/02_stone_block.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc/03_state_locks.md");
    file("some/toc1/good_root2/03_stone/04_stone_cap_x.md");
    file("some/toc1/good_root2/04_cap23.md");
    file("some/toc1/good_root2/05_cap24.md");


    toc.uriNoSlash = "some/toc1/good_root2/03_stone/03_stone_toc/02_stone_block.md";

    //
    //
    toc.populate();
    //
    //

    assertItem("2mdS74bLlY", 0, "stone cap1", 1, "/some/toc1/good_root2/03_stone/01_stone_cap1.md", false);
    assertItem("gvsRMlKMqb", 1, "stone cap2", 1, "/some/toc1/good_root2/03_stone/02_stone_cap2.md", false);
    assertItem("OVnTdZl1it", 2, "stone toc", 1, "/some/toc1/good_root2/03_stone/03_stone_toc.md", true);
    assertItem("2hdgEwBm3x", 3, "hello world", 2, "/some/toc1/good_root2/03_stone/03_stone_toc/01_hello_world.md", false);
    assertItem("49CbZKgp1i", 4, "stone block", 2, "/some/toc1/good_root2/03_stone/03_stone_toc/02_stone_block.md", true);
    assertItem("K4pnE9s2X8", 5, "state locks", 2, "/some/toc1/good_root2/03_stone/03_stone_toc/03_state_locks.md", false);
    assertItem("403N9H9BUc", 6, "stone cap x", 1, "/some/toc1/good_root2/03_stone/04_stone_cap_x.md", false);
  }

  @Test
  void populate__006() {

    file("some/toc1/.toc");
    file("some/toc1/good_root1.md");
    file("some/toc1/good_root1/cap11.md");
    file("some/toc1/good_root2.md");
    file("some/toc1/good_root2/01_cap21.md");
    file("some/toc1/good_root2/02_cap22.md");
    file("some/toc1/good_root2/03_stone.md");
    file("some/toc1/good_root2/03_stone/.toc");
    file("some/toc1/good_root2/03_stone/01_stone_cap1.md");
    file("some/toc1/good_root2/03_stone/02_stone_cap2.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc/01_hello_world.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc/02_stone_block.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc/03_state_locks.md");
    file("some/toc1/good_root2/03_stone/04_stone_cap_x.md");
    file("some/toc1/good_root2/04_cap23.md");
    file("some/toc1/good_root2/05_cap24.md");


    toc.uriNoSlash = "some/toc1/good_root2/03_stone.md";

    //
    //
    toc.populate();
    //
    //

    assertItem("wEJSCk1kCI", 0, "stone cap1", 1, "/some/toc1/good_root2/03_stone/01_stone_cap1.md", false);
    assertItem("P8Wz1NSlM1", 1, "stone cap2", 1, "/some/toc1/good_root2/03_stone/02_stone_cap2.md", false);
    assertItem("vI3sdHznZ4", 2, "stone toc", 1, "/some/toc1/good_root2/03_stone/03_stone_toc.md", false);
    assertItem("6YcC9QhDG6", 3, "hello world", 2, "/some/toc1/good_root2/03_stone/03_stone_toc/01_hello_world.md", false);
    assertItem("RxGVo0LuU9", 4, "stone block", 2, "/some/toc1/good_root2/03_stone/03_stone_toc/02_stone_block.md", false);
    assertItem("syx7mR2p0W", 5, "state locks", 2, "/some/toc1/good_root2/03_stone/03_stone_toc/03_state_locks.md", false);
    assertItem("dv1uy86oOv", 6, "stone cap x", 1, "/some/toc1/good_root2/03_stone/04_stone_cap_x.md", false);
  }

  @Test
  void populate__007() {

    file("some/toc1/.toc");
    file("some/toc1/good_root1.md");
    file("some/toc1/good_root1/cap11.md");
    file("some/toc1/good_root2.md");
    file("some/toc1/good_root2/01_cap21.md");
    file("some/toc1/good_root2/02_cap22.md");
    file("some/toc1/good_root2/03_stone.md");
    file("some/toc1/good_root2/03_stone/.toc");
    file("some/toc1/good_root2/03_stone/01_stone_cap_1.md");
    file("some/toc1/good_root2/03_stone/02_stone_cap_2.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc_x.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc_x/01_hello_world.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc_x/02_stone_block.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc_x/03_stake_other.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc_x/03_stake_other/.toc");
    file("some/toc1/good_root2/03_stone/03_stone_toc_x/03_stake_other/cap1.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc_x/03_stake_other/cap2.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc_x/03_stake_other/cap3.md");
    file("some/toc1/good_root2/03_stone/03_stone_toc_x/04_state_locks.md");
    file("some/toc1/good_root2/03_stone/04_stone_cap_x.md");
    file("some/toc1/good_root2/04_cap23.md");
    file("some/toc1/good_root2/05_cap24.md");


    toc.uriNoSlash = "some/toc1/good_root2/03_stone/03_stone_toc/02_stone_block.md";

    //
    //
    toc.populate();
    //
    //

    assertItem("ZJ16sYx3hs", 0, "stone cap 1", 1, "/some/toc1/good_root2/03_stone/01_stone_cap_1.md", false);
    assertItem("a74WFYFxW6", 1, "stone cap 2", 1, "/some/toc1/good_root2/03_stone/02_stone_cap_2.md", false);
    assertItem("5Sic2Epk14", 2, "stone toc x", 1, "/some/toc1/good_root2/03_stone/03_stone_toc_x.md", false);
    assertItem("P3CCnr0Q3f", 3, "hello world", 2, "/some/toc1/good_root2/03_stone/03_stone_toc_x/01_hello_world.md", false);
    assertItem("wW0yJ45qDd", 4, "stone block", 2, "/some/toc1/good_root2/03_stone/03_stone_toc_x/02_stone_block.md", false);
    assertItem("LhMiG61Lpe", 5, "stake other", 2, "/some/toc1/good_root2/03_stone/03_stone_toc_x/03_stake_other.md", false);
    assertItem("JAsQQ1HDoS", 6, "state locks", 2, "/some/toc1/good_root2/03_stone/03_stone_toc_x/04_state_locks.md", false);
    assertItem("ULLmOZxObo", 7, "stone cap x", 1, "/some/toc1/good_root2/03_stone/04_stone_cap_x.md", false);
  }

  @Test
  void populate__008() {

    file("some/toc1/root1.md");
    file("some/toc1/root1/cap11.md");
    file("some/toc1/root1/cap12.md");
    file("some/toc1/root1/.hidden_file.md");
    file("some/toc1/root1/.hidden_dir/ok.md");
//    file("some/toc1/root1/pic.jpg");
    file("some/toc1/root2.md");
    file("some/toc1/root2/cap21.md");
    file("some/toc1/root2/stone/cap22.md");

    toc.uriNoSlash = "some/toc1/root1/cap12.md";

    //
    //
    toc.populate();
    //
    //

    assertItem("SDkYVl0L2v", 0, "some", 1, "/some", true);
    assertItem("2LPdm1zz7y", 1, "toc1", 2, "/some/toc1", true);
    assertItem("qE0ZiCAO3c", 2, "root1", 3, "/some/toc1/root1.md", true);
    assertItem("8RP9jdqWMt", 3, "cap11", 4, "/some/toc1/root1/cap11.md", false);
    assertItem("5b5h5txI51", 4, "cap12", 4, "/some/toc1/root1/cap12.md", true);
    assertItem("7jRAUds5sd", 5, "root2", 3, "/some/toc1/root2.md", false);
    assertItem("zHfm3z704g", 6, "cap21", 4, "/some/toc1/root2/cap21.md", false);
    assertItem("2emKz80AII", 7, "stone", 4, "/some/toc1/root2/stone", false);
    assertItem("S0pv3pKX42", 8, "cap22", 5, "/some/toc1/root2/stone/cap22.md", false);

  }

  @Test
  void populate__009() {

    file("some/toc1/root1.md");
    file("some/toc1/root1/cap11.md");
    file("some/toc1/root1/cap12.md");
    file("some/toc1/root1/.hidden_file.md");
    file("some/toc1/root1/pic.jpg");
    file("some/toc1/root2.md");
    file("some/toc1/root2/cap21.md");
    file("some/toc1/root2/stone/cap22.md");

    toc.uriNoSlash = "some/toc1/root1/cap12.md";

    //
    //
    toc.populate();
    //
    //

    assertItem("SDkYVl0L2v", 0, "some", 1, "/some", true);
    assertItem("2LPdm1zz7y", 1, "toc1", 2, "/some/toc1", true);
    assertItem("qE0ZiCAO3c", 2, "root1", 3, "/some/toc1/root1.md", true);
    assertItem("8RP9jdqWMt", 3, "cap11", 4, "/some/toc1/root1/cap11.md", false);
    assertItem("5b5h5txI51", 4, "cap12", 4, "/some/toc1/root1/cap12.md", true);
    assertItem("7jRAUds5sd", 5, "root2", 3, "/some/toc1/root2.md", false);
    assertItem("zHfm3z704g", 6, "cap21", 4, "/some/toc1/root2/cap21.md", false);
    assertItem("2emKz80AII", 7, "stone", 4, "/some/toc1/root2/stone", false);
    assertItem("S0pv3pKX42", 8, "cap22", 5, "/some/toc1/root2/stone/cap22.md", false);

  }

  @Test
  void isOut_001() {

    String f1 = "some/toc1/good_root2/04_cap23.md";
    String f2 = "some/toc1/good_root3/05_cap24.md";

    file(f1);
    file(f2);

    Path F1 = toc.workDir.resolve(f1);
    Path F2 = toc.workDir.resolve(f2);

    //
    //
    boolean out = Toc.isOut(F1, F2);
    //
    //

    assertThat(out).isTrue();

  }

  @Test
  void isOut_002() {

    String f1 = "some/toc1";
    String f2 = "some/toc1/good_root3/05_cap24.md";

    file(f2);

    Path F1 = toc.workDir.resolve(f1);
    Path F2 = toc.workDir.resolve(f2);

    //
    //
    boolean out = Toc.isOut(F1, F2);
    //
    //

    assertThat(out).isFalse();

  }

  @Test
  void toCaption_002() {

    Path file = file("some/cap21.md");

    //
    //
    String actualCaption = Toc.toCaption(file, ".md");
    //
    //

    assertThat(actualCaption).isEqualTo("cap21");
  }

  @Test
  void toCaption_003() {

    Path file = file("some/_001_Status_of_Door.md");

    //
    //
    String actualCaption = Toc.toCaption(file, ".md");
    //
    //

    assertThat(actualCaption).isEqualTo("Status of Door");
  }

  @Test
  void toCaption_004() {

    String cap = "Caption for test Pg1i1Ae2G9";

    Path file = file("some/Hello/_001_Status_of_Door.md");
    file("some/Hello/.settings.json", Settings.withCaption(cap));

    //
    //
    String actualCaption = Toc.toCaption(file.toFile().getParentFile().toPath(), ".md");
    //
    //

    assertThat(actualCaption).isEqualTo(cap);
  }

  @Test
  void toCaption_005() {

    String cap = "Caption for test Pg1i1Ae2G9";

    Path file = file("some/Hello/_001_Status_of_Door.md", "## " + cap + "\n\nkh0yR4nOfS\nxGqPLI1PRX");

    //
    //
    String actualCaption = Toc.toCaption(file, ".md");
    //
    //

    assertThat(actualCaption).isEqualTo(cap);
  }

  @Test
  void toCaption_006() {

    Path file = file("some/Hello/_001_Status_Object.md");

    //
    //
    String actualCaption = Toc.toCaption(file, ".md");
    //
    //

    assertThat(actualCaption).isEqualTo("Status Object");
  }
}
