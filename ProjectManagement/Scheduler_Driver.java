package ProjectManagement;


import PriorityQueue.MaxHeap;
import RedBlack.RBTree;
import RedBlack.RedBlackNode;
import Trie.Trie;
import Trie.TrieNode;

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
    ArrayList<Job> alljobsarray=new ArrayList<Job>();
    Integer jobs_order=0;
    Integer projects_order=0;
    public static void main(String[] args) throws IOException {
//

        Scheduler_Driver scheduler_driver = new Scheduler_Driver();
        File file;
        if (args.length == 0) {
            URL url = Scheduler_Driver.class.getResource("INP");
            file = new File(url.getPath());
        } else {
            file = new File(args[0]);
        }

        scheduler_driver.execute(file);
    }
    public void execute(File commandFile) throws IOException {


        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(commandFile));

            String st;
            while ((st = br.readLine()) != null) {
                String[] cmd = st.split(" ");
                if (cmd.length == 0) {
                    System.err.println("Error parsing: " + st);
                    return;
                }
                String project_name, user_name;
                Integer start_time, end_time;

                long qstart_time, qend_time;

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
                    case "": // HANDLE EMPTY LINE
                        handle_empty_line();
                        break;
                    case "ADD":
                        handle_add(cmd);
                        break;
                    //--------- New Queries
                    case "NEW_PROJECT":
                    case "NEW_USER":
                    case "NEW_PROJECTUSER":
                    case "NEW_PRIORITY":
                        timed_report(cmd);
                        break;
                    case "NEW_TOP":
                        qstart_time = System.nanoTime();
                        timed_top_consumer(Integer.parseInt(cmd[1]));
                        qend_time = System.nanoTime();
                        System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                        break;
                    case "NEW_FLUSH":
                        qstart_time = System.nanoTime();
                        timed_flush( Integer.parseInt(cmd[1]));
                        qend_time = System.nanoTime();
                        System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                        break;
                    default:
                        System.err.println("Unknown command: " + cmd[0]);
                }

            }


            run_to_completion();
            print_stats();

        } catch (FileNotFoundException e) {
            System.err.println("Input file Not found. " + commandFile.getAbsolutePath());
        } catch (NullPointerException ne) {
            ne.printStackTrace();

        }
    }
    @Override
    public ArrayList<JobReport_> timed_report(String[] cmd) {
        long qstart_time, qend_time;
        ArrayList<JobReport_> res = null;
        switch (cmd[0]) {
            case "NEW_PROJECT":
                qstart_time = System.nanoTime();
                res = handle_new_project(cmd);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
            case "NEW_USER":
                qstart_time = System.nanoTime();
                res = handle_new_user(cmd);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));

                break;
            case "NEW_PROJECTUSER":
                qstart_time = System.nanoTime();
                res = handle_new_projectuser(cmd);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
            case "NEW_PRIORITY":
                qstart_time = System.nanoTime();
                res = handle_new_priority(cmd[1]);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
        }

        return res;
    }
    @Override
    public ArrayList<UserReport_> timed_top_consumer(int top) {
        System.out.println("Top query");
        ArrayList<UserReport_> list=new ArrayList<>();
        MaxHeap<User> temp= new MaxHeap<>();
        for (UserReport_ K:users)
            temp.insert((User) K);
        User user=temp.extractMax();
        int i=0;
        while (i<top&&user!=null){
            list.add(user);
            user=temp.extractMax();
            i++;
        }
        /*ArrayList<User> user= new ArrayList<User>();
        user.addAll(users);
        //System.out.println(user);
        for (int i=0;i<top;i++){
            if (user.size()==0)
                break;
            int m=0;
            //System.out.println(user.size());
            for (int j=0;j<user.size();j++){
                if (user.get(j).compareTo(user.get(m))>0){
                    m=j;
                }
            }

            list.add(user.remove(m));
        }*/


        return list;
    }
    @Override
    public void timed_flush(int waittime) {
        System.out.println("Flush query");
        Project tempproject=new Project();
        MaxHeap<Job> tempjobs=new MaxHeap<>();
        tempproject.setPriority(9999);
        int gtime=globaltime;
        for (int i=0;i<alljobsarray.size();i++){
            Job job=alljobsarray.get(i);
            if (job.status.equals("NOT FINISHED")&&gtime-job.arrivaltime>=waittime&&job.project.budget>=job.runtime){
                //System.out.println(p);
                Project pro=job.project;
                tempproject.k=pro.k;
                job.project=tempproject;
                jobs.rearrange(0,job);
                Job jobx=jobs.extractMax();
                //System.out.println("p ="+jobx.project.priority);
                if (jobx!=null){
                    job.project=pro;
                    tempjobs.insert(job);

                    //System.out.println("budget ="+pro.budget);
                    //System.out.println("Flushed: "+job);
                }/*else jobs.insert(jobx);*/
            }
            //System.out.println(alljobsarray.get(i));
        }
        Job temp=tempjobs.extractMax();
        while (temp!=null){
            if (temp.project.budget>=temp.runtime){
                execute_a_job(temp);
                //System.out.println(temp);
            }
            else jobs.insert(temp);
            temp=tempjobs.extractMax();
        }
        //System.out.println(a+" and "+b+" hj"+c);
       /* Job job=jobs.extractMax();
        while (job!=null){
            if (job.project.priority==9999){
                System.out.println(job.project.temppriority);
                job.project.priority=job.project.temppriority;
                System.out.println("Flushed: "+job);
                execute_a_job(job);
            }else {
                jobs.insert(job);
                break;
            }
            job=jobs.extractMax();
        }*/
    }
    private ArrayList<JobReport_> handle_new_priority(String s) {
        System.out.println("Priority query");
        ArrayList<JobReport_> list=new ArrayList<>();
        //priority(Integer.valueOf(s),notready.root,list);
        int j=Integer.valueOf(s);
        MaxHeap<Job> temp=new MaxHeap<>();
        for (int i=0;i<alljobsarray.size();i++){
            if (!alljobsarray.get(i).status.equals("COMPLETED")&&alljobsarray.get(i).project.priority>=j)
            temp.insert(alljobsarray.get(i));
        }
        Job k=temp.extractMax();
        while (k!=null){
            list.add(k);
            k=temp.extractMax();
        }

        /*for (Object item:list){
            System.out.println(item);
        }*/

        return list;
    }
    private void priority(int s,RedBlackNode<Project,Job> node,ArrayList list) {
        if (node==null)
            return;
        if (node.key.priority.compareTo(s)==0)
            for (Object item:node.getValues())
            {
                list.add(item);

            }
        else if (node.key.priority.compareTo(s)<0)
            priority(s,node.right,list);
        else if (node.key.priority.compareTo(s)>0){
            for (Object item:node.getValues())
            {
                list.add(item);
                //System.out.println(item);
            }
            priority(s,node.left,list);
            priority(s,node.right,list);
        }
    }
    private ArrayList<JobReport_> handle_new_projectuser(String[] cmd) {
        System.out.println("Project User query");
        ArrayList<JobReport_> list=new ArrayList<>();
        int t1=Integer.parseInt(cmd[3]);
        int t2=Integer.parseInt(cmd[4]);
        for (int i=0;i<alljobsarray.size();i++){
            Job job =alljobsarray.get(i);
            if (job.project.name.compareTo(cmd[1])==0&&job.user().compareTo(cmd[2])==0&&job.arrivaltime>=t1&&job.arrivaltime<=t2) {
                list.add(job);
                //System.out.println(job);
            }
        }

        //System.out.println("size ;"+list.size());
        return list;
    }
    private ArrayList<JobReport_> handle_new_user(String[] cmd) {
        System.out.println("User query");
            ArrayList<JobReport_> list=new ArrayList<>();
            int t1=Integer.parseInt(cmd[2]);
            int t2=Integer.parseInt(cmd[3]);

            for (int i=0;i<alljobsarray.size();i++){
                Job job =alljobsarray.get(i);
                if (job.user().compareTo(cmd[1])==0&&job.arrivaltime>=t1&&job.arrivaltime<=t2) {
                    list.add(job);
                    //System.out.println(job);
                }
            }
        //System.out.println("size ;"+list.size());

        return list;

    }
    private ArrayList<JobReport_> handle_new_project(String[] cmd) {
        System.out.println("Project query");
            ArrayList<JobReport_> list=new ArrayList<>();
            int t1=Integer.parseInt(cmd[2]);
            int t2=Integer.parseInt(cmd[3]);
            for (int i=0;i<alljobsarray.size();i++){
                Job job =alljobsarray.get(i);
                if (job.project.name.compareTo(cmd[1])==0&&job.arrivaltime>=t1&&job.arrivaltime<=t2)
                {
                    list.add(job);
                    //System.out.println(job);
                }
            }
        //System.out.println("size ;"+list.size());

        return list;

    }
    public void schedule() {
        Job job=jobs.extractMax();
        if (job!=null){

            //Project project=(Project) projects.search(job.project.name).getValue();
            Project project=job.project;
            System.out.println("Executing: "+job.name+/*" "+job.project.priority*/" from: "+project.name );
            if (project.budget.compareTo(job.runtime)<0){
                //to be printed something that budget is more than runtime
                //System.out.print(project.budget+" and "+job.runtime);
                jobscount--;
                job.status="REQUESTED";
                notready.insert(project,job);
                System.out.println("Un-sufficient budget.");
                schedule();
            }else {
                execute_a_job(job);
                System.out.println( "Project: "+project.name+" budget remaining: "+project.budget);
            }
        }else {
            //to be printed something that no jobs are there
            return;
        }
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
        project.setPriority(Integer.valueOf(cmd[2]));
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
            job.arrivaltime=globaltime;
            alljobs.insert(job.name,job);
            alljobsarray.add(job);
            //projectNode.getValue().jobs.add(job);
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
        TrieNode node= projects.search(cmd[1]);
        //handle nullity of
        if (node==null)
            return;
        Project project= (Project) node.getValue();
        project.budget+=Integer.parseInt(cmd[2]);
        System.out.println("ADDING Budget");
        List<Job> list;
        try {
            list=notready.search(project).getValues();
        }catch (NullPointerException e){
            return;
        }
        if (list==null)
            return;
        for (int i=0;i<list.size();i++){
            list.get(i).status="NOT FINISHED";
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
        Integer j=printstats(notready.root);
        System.out.println("Total unfinished jobs: "+j);
        System.out.println("--------------STATS DONE---------------");
    }
    public int printstats(RedBlackNode<Project,Job> node){
        int j=0;
        if (node==null)
            return 0;
        int a=printstats(node.right);
        for (Job person1 : node.getValues()) {
            System.out.println(person1);
            j++;
        }
        //System.out.println(node.getValues());
        return a+printstats(node.left)+j;

    }
    public void execute_a_job(Job job) {
        /*Job job=jobs.extractMax();
        if (job!=null){

            //Project project=(Project) projects.search(job.project.name).getValue();
            Project project=job.project;
            System.out.println("Executing: "+job.name+" from: "+project.name );
            if (project.budget.compareTo(job.runtime)<0){
                //to be printed something that budget is more than runtime
                //System.out.print(project.budget+" and "+job.runtime);
                job.status="REQUESTED";
                notready.insert(project,job);
                System.out.println("Un-sufficient budget.");
                execute_a_job();
            }else {
                job.status="COMPLETED";*/
        jobscount--;
        job.status="COMPLETED";
        globaltime+=job.runtime;
        job.project.budget-=job.runtime;
        job.endtime=globaltime;
        job.user.consumed+=job.runtime;
        job.user.ltime=globaltime;
        finishedjobs.add(job);
               /* System.out.println( "Project: "+project.name+" budget remaining: "+project.budget);
            }
        }else {
            //to be printed something that no jobs are there
            return;
        }*/
    }
    @Override
    public void timed_handle_user(String name){
        users.add(new User(name));
    }
    @Override
    public void timed_handle_job(String[] cmd){
        TrieNode<Project> projectNode=projects.search(cmd[2]);
        User user=null;
        for (int i=0;i<users.size();i++){
            if (users.get(i).name.equals(cmd[3])) {
                user=users.get(i);
                break;
            }
        }
        if (projectNode==null);
            //System.out.println("No such project exists. "+cmd[2]);
        else if (user==null);
            //System.out.println("No such user exists: "+cmd[3]);
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
            job.arrivaltime=globaltime;
            alljobs.insert(job.name,job);
            alljobsarray.add(job);
            //projectNode.getValue().jobs.add(job);
            jobscount++;
        }
    }
    @Override
    public void timed_handle_project(String[] cmd){
        Project project=new Project();
        projects_order++;
        project.k=projects_order;
        project.name=cmd[1];
        project.setPriority(Integer.valueOf(cmd[2]));
        project.budget=Integer.parseInt(cmd[3]);
        projects.insert(cmd[1],project);
    }
    @Override
    public void timed_run_to_completion(){
        while (true){
            //System.out.println("Running code\n" + "Remaining jobs: " + jobscount);
            schedule();
            //System.out.println("System execution completed");
            if (jobscount<=0)
                break;
        }
    }
}
