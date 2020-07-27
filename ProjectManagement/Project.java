package ProjectManagement;


import java.util.ArrayList;

public class Project implements Comparable<Project>{
    String name;
    Integer priority;
    Integer k;
    Integer budget;
    ArrayList<Job> jobs;
    public Project() {
        jobs=new ArrayList<>();
    }
    public void setPriority(int a){
        priority=a;
    }

    @Override
    public int compareTo(Project project) {
        if (priority.compareTo(project.priority)!=0)
            return priority.compareTo(project.priority);
        else return project.k.compareTo( k);
    }
}
