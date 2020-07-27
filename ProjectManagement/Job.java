package ProjectManagement;

public class Job implements Comparable<Job> , JobReport_ {

    String name;
    Project project;
    User user;
    Integer arrivaltime;
    Integer runtime;
    Integer temppriority=9999;
    Integer endtime;
    Integer k;
    String status="NOT FINISHED";
    @Override
    public int compareTo(Job job) {
        if (project.priority.compareTo(job.project.priority)!=0)
            return project.priority.compareTo(job.project.priority);

        else return job.k.compareTo(k);
    }
    @Override
    public String toString(){
        return "Job{user='"+user.name+"', project='"+project.name+"', jobstatus="+status+", execution_time="+runtime+", start_time="+arrivaltime+", end_time="+endtime+", priority="+project.priority+", name='"+name+"'}";
    }

    @Override
    public String user() {
        return user.name;
    }

    @Override
    public String project_name() {
        return project.name;
    }

    @Override
    public int budget() {
        return 0;
    }

    @Override
    public int arrival_time() {
        return arrivaltime;
    }

    @Override
    public int completion_time() {
        return endtime;
    }
}