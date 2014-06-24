import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class testbed
{

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    java.security.Security
    .addProvider(new com.sun.net.ssl.internal.ssl.Provider());

    Properties props = System.getProperties();
    props.setProperty("mail.store.protocol", "imaps");
    try
    {
      Session session = Session.getDefaultInstance(props, null);
      Store store = session.getStore("imaps");
      store.connect("imap.gmail.com", 
          "msz.informatika@gmail.com"
      , "Sicambria");
      
      System.out.println(store);

      Folder inbox = store.getFolder("Inbox");
      inbox.open(Folder.READ_ONLY);
      Message messages[] = inbox.getMessages();
      for (Message message : messages)
      {
        System.out.println(message.getReceivedDate());
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

}
