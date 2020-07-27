package ProjectManagement;


public class Project implements Comparable<Project>{
    String name;
    Integer priority;
    Integer k;
    Integer budget;

    @Override
    public int compareTo(Project project) {
        if (priority.compareTo(project.priority)!=0)
            return priority.compareTo(project.priority);
        else return project.k.compareTo( k);
    }
}
