import java.beans.XMLEncoder;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.*;
import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import javax.mail.*;
import javax.mail.internet.MimeUtility;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import sun.misc.BASE64Decoder;

public class MailFetch
{
  String host = "imap.googlemail.com";
  String username = "msz.@gmail.com";
  String password = "S...........a";

  public MailFetch(String... args) throws Exception
  {
    init(args.length == 0 ? null : args[0]);
  }

  private void init(String filter) throws Exception
  {
    // new File(currentDir).mkdir();

    if (filter != null)
      System.out.println("filter: " + filter);

    java.security.Security
        .addProvider(new com.sun.net.ssl.internal.ssl.Provider());

    Properties props = System.getProperties();
    props.put("mail.imap.host", host);
    java.security.Security.setProperty("ssl.SocketFactory.provider",
        "DummySSLSocketFactory");

    Session session = Session.getInstance(props);

    // Get the store
    Store store = session.getStore("imap");
    // Store store = session.getStore("imaps");
    store.connect(host, username, password);

    // Get folder
    Folder folder = store.getFolder("[Gmail]/Összes levél");
    folder.open(Folder.READ_WRITE);

    List<User> users = new LinkedList<User>();
    // Get directory
    for (Message message : folder.getMessages())
    {
      // System.out.println(message.getFrom()[0] + " Subject: " +
      // message.getSubject());

      if (message.getSubject() != null
          && message.getSubject().startsWith("MSZ -")
          && message.getSubject().indexOf("jelentkez") > -1)
      {
        if (message.getContent() instanceof String)
        {
          String body = (String) message.getContent();

          if (filter != null && body.indexOf(filter) == -1)
            continue;

          User user = parseBody(message, body);
          users.add(user);
          System.out.println(users.size() + ". " + user);

          // System.out.println(user);
          // saveFile(body);

//           break;
        }
      }
    }

    // Close connection
    folder.close(false);
    store.close();

    saveUsers(users);

    writeExcel(users);
  }

  private void saveUsers(List<User> users) throws FileNotFoundException
  {
    XMLEncoder e = new XMLEncoder(new FileOutputStream("users.xml"));
    e.writeObject(users);
    e.close();
  }

  public static void writeExcel(List<User> users) throws Exception
  {
    HSSFWorkbook wb = new HSSFWorkbook();
    // Workbook wb = new XSSFWorkbook();
    CreationHelper createHelper = wb.getCreationHelper();
    Sheet sheet = wb.createSheet("new sheet");

    Row row = sheet.createRow((short) 0);
    row.createCell(0).setCellValue("Érkezett");
    row.createCell(1).setCellValue("Név");
    row.createCell(2).setCellValue("E-mail");
    row.createCell(3).setCellValue("Mobil");
    row.createCell(4).setCellValue("Vezetékes");
    row.createCell(5).setCellValue("Szakképzettség");
    row.createCell(6).setCellValue("Foglalkozás");
    row.createCell(7).setCellValue("Lakhely");
    row.createCell(8).setCellValue("Egyéb");
    row.createCell(9).setCellValue("Tevékenység");
    row.createCell(10).setCellValue("Feladat");
    row.createCell(11).setCellValue("Megjegyzés");

    int rowCnt = 1;
    for (User user : users)
    {
      int columnCnt = 0;
      try
      {
        row = sheet.createRow((short) rowCnt);

        row.createCell(columnCnt++).setCellValue(user.receivedDate);
        row.createCell(columnCnt++).setCellValue(user.name);
        row.createCell(columnCnt++).setCellValue(user.email);
        row.createCell(columnCnt++).setCellValue(user.mobile);
        row.createCell(columnCnt++).setCellValue(user.landline);
        row.createCell(columnCnt++).setCellValue(user.education);
        row.createCell(columnCnt++).setCellValue(user.job);
        row.createCell(columnCnt++).setCellValue(user.address);
        row.createCell(columnCnt++).setCellValue(user.other);
        row.createCell(columnCnt++).setCellValue(user.occupation);

        for (MSZJob mszJob : user.mszJobs)
        {
          row.createCell(columnCnt++).setCellValue(mszJob.job);
          row.createCell(columnCnt++).setCellValue(mszJob.comment);
        }

        rowCnt++;
      }
      catch (Exception e)
      {
        System.out.println(user);
        e.printStackTrace();
      }
    }

    // Write the output to a file
    FileOutputStream fileOut = new FileOutputStream("workbook.xls");
    wb.write(fileOut);
    fileOut.close();

  }

  private User parseBody(Message message, String body) throws IOException,
      MessagingException
  {
    User user = new User();

    user.receivedDate = message.getReceivedDate();

    BufferedReader br = new BufferedReader(new StringReader(body));
    String line = null;
    MSZJob mszJob = null;
    while ((line = br.readLine()) != null)
    {
      line = line.trim();
      if (line.isEmpty())
        continue;

      String paramName = line.indexOf(' ') > -1 ? line.substring(0, line
          .indexOf(' ')) : line;
      paramName = paramName.substring(0, paramName.length() - 1);

      String paramValue = line.indexOf(' ') > -1 ? line.substring(line
          .indexOf(' ') + 1) : "";

      // System.out.println("paramName: " + paramName +
      // " paramValue: " + paramValue);

      if (paramName.equals("Név"))
        user.name = paramValue;
      else if (paramName.equals("E-mail"))
        user.email = paramValue;
      else if (paramName.equals("Mobil"))
        user.mobile = paramValue;
      else if (paramName.equals("Vezetekes"))
        user.landline = paramValue;
      else if (paramName.equals("Szakképzettség"))
        user.education = paramValue;
      else if (paramName.equals("Foglalkozás"))
        user.job = paramValue;
      else if (paramName.equals("Lakhely"))
        user.address = paramValue;
      else if (paramName.equals("Egyéb"))
        user.other = paramValue;
      else if (paramName.equals("Tevékenység"))
        user.occupation = paramValue;
      else if (paramName.equals("Feladat"))
      {
        if (user.mszJobs == null)
          user.mszJobs = new LinkedList<MSZJob>();

        mszJob = new MSZJob();
        user.mszJobs.add(mszJob);

        mszJob.job = paramValue;
      }
      else if (paramName.equals("Megjegyzés"))
      {
        mszJob.comment = paramValue;
      }
    }

    // StringTokenizer st = new StringTokenizer(body);
    // while(st.hasMoreTokens())
    // {
    // System.out.println(">>>>> " + st.nextToken());
    // }

    return user;
  }

  private void saveFile(String body) throws Exception
  {
    try
    {
      long startTime = System.currentTimeMillis();

      File file = new File("filename.txt");

      FileOutputStream output = new FileOutputStream(file);

      output.write(body.getBytes());

      output.close();

      System.out.println("Save finished in "
          + (System.currentTimeMillis() - startTime) + " ms. filesize: "
          + file.length());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception
  {
    new MailFetch(args);
  }
}