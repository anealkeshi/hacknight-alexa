package me.anilkc.hacknight.exception;

public class VoterNotFoundException extends HackNightException {

  private static final long serialVersionUID = 6186852484394424888L;

  public VoterNotFoundException() {
    super();
  }

  public VoterNotFoundException(String msg) {
    super(msg);
  }

  public VoterNotFoundException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public VoterNotFoundException(Throwable cause) {
    super(cause);
  }
}
