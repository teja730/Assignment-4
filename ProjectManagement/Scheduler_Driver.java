package ProjectManagement;

import PriorityQueue.MaxHeap;
import PriorityQueue.PriorityQueueDriverCode;
import RedBlack.RBTree;
import RedBlack.RedBlackNode;
import Trie.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Scheduler_Driver extends Thread implements SchedulerInterface {

    ArrayList<User> users= new ArrayList<User>();
    MaxHeap<Job> jobs=new MaxHeap<Job>();
    Trie<Project> projects=new Trie<Project>();
    RBTree<Project,Job> notready= new RBTree<Project,Job>();
    ArrayList<Job> finishedjobs= new ArrayList<Job>();
    Integer globaltime=0;
    Integer jobscount=0;
    Trie<Job> alljobs=new Trie<Job>();
    Integer jobs_order=0;
    Integer projects_order=0;

    public static void main(String[] args) throws IOException {
        Scheduler_Driver scheduler_driver = new Scheduler_Driver();

        File file;
        if (args.length == 0) {
            URL url = PriorityQueueDriverCode.class.getResource("INP");
            file = new File(url.getPath());
        } else {
            file = new File(args[0]);
        }

        scheduler_driver.execute(file);
    }

    public void execute(File file) throws IOException {

        URL url = Scheduler_Driver.class.getResource("INP");
       // file = new File(url.getPath());

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.err.println("Input file Not found. "+file.getAbsolutePath());
        }
        String st;
        while ((st = br.readLine()) != null) {
            String[] cmd = st.split(" ");
            if (cmd.length == 0) {
                System.err.println("Error parsing: " + st);
                return;
            }

            switch (cmd[0]) {
                case "PROJECT":
                    handle_project(cmd);
                    break;
                case "JOB":
                    handle_job(cmd);
                    break;
                case "USER":
                    handle_user(cmd[1]);
                    break;
                case "QUERY":
                    handle_query(cmd[1]);
                    break;
                case "":
                    handle_empty_line();
                    break;
                case "ADD":
                    handle_add(cmd);
                    break;
                default:
                    System.err.println("Unknown command: " + cmd[0]);
            }
        }


        run_to_completion();

        print_stats();

    }




    @Override
    public void run() {
        // till there are JOBS
        schedule();
    }


    @Override
    public void run_to_completion() {
        while (true){
                System.out.println("Running code\n" + "Remaining jobs: " + jobscount);
                schedule();
                System.out.println("System execution completed");
           if (jobscount<=0)
               break;
        }
    }

    @Override
    public void handle_project(String[] cmd) {
        System.out.println("Creating project");
        Project project=new Project();
        projects_order++;
        project.k=projects_order;
        project.name=cmd[1];
        project.priority=Integer.parseInt(cmd[2]);
        project.budget=Integer.parseInt(cmd[3]);
        projects.insert(cmd[1],project);
    }

    @Override
    public void handle_job(String[] cmd) {
        System.out.println("Creating job");
        TrieNode<Project> projectNode=projects.search(cmd[2]);
        User user=null;
        for (int i=0;i<users.size();i++){
            if (users.get(i).name.equals(cmd[3])) {
                user=users.get(i);
                break;
            }
        }
        if (projectNode==null)
            System.out.println("No such project exists. "+cmd[2]);
        else if (user==null)
            System.out.println("No such user exists: "+cmd[3]);
        else {
            Job job = new Job();
            job.name=cmd[1];
            job.runtime=Integer.parseInt(cmd[4]);
            job.user=user;
            jobs_order++;
            job.k=jobs_order;
            //System.out.println(k);
            job.project=projectNode.getValue();
            jobs.insert(job);
            alljobs.insert(job.name,job);
            jobscount++;
        }
    }

    @Override
    public void handle_user(String name) {
        System.out.println("Creating user");
        users.add(new User(name));
    }

    @Override
    public void handle_query(String key) {
        System.out.println("Querying");
        TrieNode<Job> node=alljobs.search(key);
        if (node==null) {
            System.out.println(key+": NO SUCH JOB");
            return;
        }
        Job job= node.getValue();
        //System.out.println(job.name);
        if (job.status.equals("COMPLETED"))
            System.out.println(job.name+": COMPLETED");
        else System.out.println(job.name+": NOT FINISHED");
    }


    @Override
    public void handle_empty_line() {
        System.out.println("Running code\n" +"Remaining jobs: "+jobscount);
        schedule();
        System.out.println("Execution cycle completed");
    }

    @Override
    public void handle_add(String[] cmd) {
        Project project= (Project) projects.search(cmd[1]).getValue();
        //handle nullity of
        project.budget+=Integer.parseInt(cmd[2]);
        System.out.println("ADDING Budget");
        List<Job> list;
        try {
            list=notready.search(project).getValues();
        }catch (NullPointerException e){
            return;
        }
        for (int i=0;i<list.size();i++){
            jobs.insert(list.get(i));
            jobscount++;
        }
        list.clear();
        //shift elments from rbtree to maxheap
    }

    @Override
    public void print_stats() {
        System.out.println("--------------STATS---------------");
        System.out.println("Total jobs done: "+finishedjobs.size());
        for (int i=0;i<finishedjobs.size();i++){
            System.out.println(finishedjobs.get(i));
        }
        System.out.println("------------------------");
        System.out.println("Unfinished jobs: ");
        Integer j=print(notready.root);
        System.out.println("Total unfinished jobs: "+j);
        System.out.println("--------------STATS DONE---------------");
    }
    public int print(RedBlackNode<Project,Job> node){
        int j=0;
        if (node==null)
            return 0;
        int a=print(node.right);
        for (Job person1 : node.getValues()) {
            System.out.println(person1);
            j++;
        }
        //System.out.println(node.getValues());
        return a+print(node.left)+j;

    }

    @Override
    public void schedule() {

        Job job=jobs.extractMax();
        if (job!=null){
            jobscount--;
            //Project project=(Project) projects.search(job.project.name).getValue();
            Project project=job.project;
            System.out.println("Executing: "+job.name+" from: "+project.name );
            if (project.budget.compareTo(job.runtime)<0){
                //to be printed something that budget is more than runtime
                //System.out.print(project.budget+" and "+job.runtime);
                job.status="REQUESTED";
                notready.insert(project,job);
                System.out.println("Un-sufficient budget.");
                schedule();
            }else {
                job.status="COMPLETED";
                globaltime+=job.runtime;
                project.budget-=job.runtime;
                job.endtime=globaltime;
                finishedjobs.add(job);
                System.out.println( "Project: "+project.name+" budget remaining: "+project.budget);
            }
        }else {
            //to be printed something that no jobs are there
            return;
        }
    }
}
