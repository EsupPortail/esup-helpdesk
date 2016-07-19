package net.suberic.crypto;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

/**
 * A MimeBodyPart that allows for updateHeaders() to be called.
 */
public class UpdatableMBP extends MimeBodyPart {

  /**
   * Create a new UpdatableMimeBodyPart.
   */
  public UpdatableMBP() {
    super();
  }

  /**
   * Create a new UpdatableMimeBodyPart.
   */
  public UpdatableMBP(java.io.InputStream is) throws MessagingException{
    super(is);
  }

  /**
   * Calls updateHeaders().
   */
  public void updateMyHeaders() throws MessagingException {
    updateHeaders();
  }

}
