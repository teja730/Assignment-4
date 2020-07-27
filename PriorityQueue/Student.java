package PriorityQueue;

public class Student implements Comparable<Student> {
    private String name;
    private Integer marks;

    public Student(String trim, int parseInt) {
        name=trim;
        marks=parseInt;
    }


    @Override
    public int compareTo(Student student) {
        return marks.compareTo(student.marks);
    }
    @Override
    public String toString(){
        return "Student{name='"+getName()+"', marks="+marks+"}";
    }
    public String getName() {
        return name;
    }
}
