package ProjectManagement;

public class User implements Comparable<User> {

    String name;
    public User(String name){
        this.name=name;
    }
    @Override
    public int compareTo(User user) {
        return 0;
    }

}
