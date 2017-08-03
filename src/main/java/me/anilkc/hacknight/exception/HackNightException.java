package me.anilkc.hacknight.exception;

public class HackNightException extends Exception {

  private static final long serialVersionUID = -1328908828154648794L;

  public HackNightException() {
    super();
  }

  public HackNightException(String msg) {
    super(msg);
  }

  public HackNightException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public HackNightException(Throwable cause) {
    super(cause);
  }
}
