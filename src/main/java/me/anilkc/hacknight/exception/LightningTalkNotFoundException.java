package me.anilkc.hacknight.exception;

public class LightningTalkNotFoundException extends HackNightException {

  private static final long serialVersionUID = 8088504953034086313L;

  public LightningTalkNotFoundException() {
    super();
  }

  public LightningTalkNotFoundException(String msg) {
    super(msg);
  }

  public LightningTalkNotFoundException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public LightningTalkNotFoundException(Throwable cause) {
    super(cause);
  }
}
