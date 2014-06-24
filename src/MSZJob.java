public class MSZJob
{
  public String job;
  public String comment;

  public MSZJob()
  {

  }

  public MSZJob(String job, String comment)
  {
    this.job = job;
    this.comment = comment;
  }

  public String toString()
  {
    return " job: " + job + " comment: " + comment;
  }

  public String getJob()
  {
    return job;
  }

  public void setJob(String job)
  {
    this.job = job;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }
  
}