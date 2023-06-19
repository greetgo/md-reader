package kz.greetgo.md_reader.test_utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public abstract class ParentTestClass {

  @BeforeEach
  void setUp(TestInfo testInfo) {
    String testClassName = testInfo.getTestClass().orElseThrow().getSimpleName();
    String displayName   = testInfo.getDisplayName();

    System.out.println("qgN06gng24 :: dn = " + displayName);
  }

}
