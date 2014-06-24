import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;


public class WriteExcel
{

  /**
   * @param args
   * @throws FileNotFoundException 
   */
  public static void main(String[] args) throws Exception
  {
    XMLDecoder e = new XMLDecoder(new FileInputStream("users.xml"));
    List<User> users = (List<User>)e.readObject();
    e.close();

    MailFetch.writeExcel(users);
    
    System.out.println("DONE.......");
  }

}
