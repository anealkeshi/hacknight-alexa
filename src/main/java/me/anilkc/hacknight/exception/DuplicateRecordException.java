package me.anilkc.hacknight.exception;

public class DuplicateRecordException extends HackNightException {


  private static final long serialVersionUID = -7299796091568423936L;

  public DuplicateRecordException() {
    super();
  }

  public DuplicateRecordException(String msg) {
    super(msg);
  }

  public DuplicateRecordException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public DuplicateRecordException(Throwable cause) {
    super(cause);
  }

}
