package ProjectManagement;

public class Job implements Comparable<Job> {

    String name;
    Project project;
    User user;
    Integer runtime;
    Integer endtime;
    Integer k;
    String status="NOT COMPLETED";
    @Override
    public int compareTo(Job job) {
        if (project.priority.compareTo(job.project.priority)!=0)
            return project.priority.compareTo(job.project.priority);

        else return job.k.compareTo(k);
    }
    @Override
    public String toString(){
        return "Job{user='"+user.name+"', project='"+project.name+"', jobstatus="+status+", execution_time="+runtime+", end_time="+endtime+", name='"+name+"'}";
    }
}