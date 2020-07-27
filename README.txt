Assignment 5
Project management
Schedule driver-
handle project/user/job- new object of respective type are created and all the required information are stored in the object and inserted into their respective data structures
execute a job-it takes out a job from the priority queue in which jobs are stored and if job is'nt there it will return null and if not it will check whether the budget the of the corresponding project is more than the run time, if yes then the job is executed and the respective if not the whole procedure is repeated again.
handle empty line- execute a job
handle add- adds the given budget to the given project and moves all the unfinished jobs which were removed from priority queue back to the priority queue.
handle query- if searches for the given job and prints the status of the job.
schedule- execute a job
run to completion- a job is executed till the priority queue having jobs is empty.
handle new project- it checks for the project given in the trie in which projects are stored and then the list of jobs which is stored in the project are checked one by one that whether their arrival time is within the given interval if yes then the job is added into the arraylist to be returned
handle new user- it iterates for every job and matches the user and the checks whether the arrival time is within the given range.
handle new project user- it iterates  for every job same as in handle new user but checks for the project too. and it is sorted according to the endtime of the job.
handle top consumer- adds all the users into the a new maxheap and then takes top no of users out
handle flush- increases the priority of the jobs satisfying the given conditions and then executes them in the order of original priority.
