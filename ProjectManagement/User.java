package ProjectManagement;

public class User implements Comparable<User> ,UserReport_{

    String name;
    Integer consumed;
    Integer ltime;
    public User(String name){
        this.name=name;
        consumed=0;
        ltime=0;
    }
    @Override
    public int compareTo(User user) {
        if (consumed.compareTo(user.consumed)==0)
            return ltime.compareTo(user.ltime);
        else return consumed.compareTo(user.consumed);
    }

    @Override
    public String user() {
        return name;
    }

    @Override
    public int consumed() {
        return consumed;
    }
}
