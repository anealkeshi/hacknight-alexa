package me.anilkc.hacknight.util;

import java.util.concurrent.ThreadLocalRandom;

public class UniqueIdGenerator {

  private static final int MIN = 1000;

  private static final int MAX = 9999;

  public static int generate4DigitRandomNumber() {
    return ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
  }
}
