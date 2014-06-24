import java.util.Date;
import java.util.List;

public class User
{
  public Date receivedDate;
  public String name;
  public String email;
  public String mobile;
  public String landline;
  public String education;
  public String job;
  public String address;
  public String other;
  public String occupation;

  List<MSZJob> mszJobs;

  public User()
  {
  }

  public User(Date receivedDate, String name, String email, String mobile,
      String landline, String education, String job, String address,
      String other, String occupation, List<MSZJob> mszJobs)
  {
    this.receivedDate = receivedDate;

    this.name = name;
    this.email = email;
    this.mobile = mobile;
    this.landline = landline;
    this.education = education;
    this.job = job;
    this.address = address;
    this.other = other;
    this.occupation = occupation;

    this.mszJobs = mszJobs;
  }

  public String toString()
  {
    return " receivedDate: " + receivedDate + " name: " + name + " email: "
        + email + " mobile: " + mobile + " landline: " + landline
        + " education: " + education + " job: " + job + " address: " + address
        + " other: " + other + " occupation: " + occupation +

        " mszJobs: " + mszJobs;
  }

  public Date getReceivedDate()
  {
    return receivedDate;
  }

  public void setReceivedDate(Date receivedDate)
  {
    this.receivedDate = receivedDate;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getMobile()
  {
    return mobile;
  }

  public void setMobile(String mobile)
  {
    this.mobile = mobile;
  }

  public String getLandline()
  {
    return landline;
  }

  public void setLandline(String landline)
  {
    this.landline = landline;
  }

  public String getEducation()
  {
    return education;
  }

  public void setEducation(String education)
  {
    this.education = education;
  }

  public String getJob()
  {
    return job;
  }

  public void setJob(String job)
  {
    this.job = job;
  }

  public String getAddress()
  {
    return address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public String getOther()
  {
    return other;
  }

  public void setOther(String other)
  {
    this.other = other;
  }

  public String getOccupation()
  {
    return occupation;
  }

  public void setOccupation(String occupation)
  {
    this.occupation = occupation;
  }

  public List<MSZJob> getMszJobs()
  {
    return mszJobs;
  }

  public void setMszJobs(List<MSZJob> mszJobs)
  {
    this.mszJobs = mszJobs;
  }
  
}
