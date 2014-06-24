import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;


public class FilterUsers
{

  /**
   * @param args
   * @throws Exception 
   */
  public static void main(String[] args) throws Exception
  {
    String keyword = args.length == 0 ?  "rendszergazda" : args[0];
    
    XMLDecoder e = new XMLDecoder(new FileInputStream("users.xml"));
    List<User> users = (List<User>)e.readObject();
    e.close();
    
    List<User> filteredUsers = new LinkedList<User>();
    
    for (User user : users)
    {
      if(user.job.toLowerCase().indexOf(keyword) > -1 ||
          user.education.toLowerCase().indexOf(keyword) > -1
          )
        filteredUsers.add(user);
      
    }

    MailFetch.writeExcel(filteredUsers);
    
    System.out.println("DONE.......");


  }

}
