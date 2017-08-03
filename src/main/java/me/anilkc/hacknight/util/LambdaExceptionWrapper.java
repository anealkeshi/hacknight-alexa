package me.anilkc.hacknight.util;

import java.util.function.Consumer;

public class LambdaExceptionWrapper {

  @FunctionalInterface
  public interface ConsumerException<T, E extends Exception> {
    void accept(T t) throws E;
  }

  public static <T, E extends Exception> Consumer<T> acceptWithException(ConsumerException<T, E> consumer) {
    return t -> {
      try {
        consumer.accept(t);
      } catch (Exception exception) {
        throwActualException(exception);
      }
    };
  }

  @SuppressWarnings("unchecked")
  private static <E extends Exception> void throwActualException(Exception exception) throws E {
    throw (E) exception;
  }
}
