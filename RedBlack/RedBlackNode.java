package RedBlack;
import Util.RBNodeInterface;
import java.util.ArrayList;
import java.util.List;

public class RedBlackNode<T extends Comparable, E> implements RBNodeInterface<E> {

    List<E> list;
    public T key;
    String color;
    public RedBlackNode<T,E> left=null,right=null,parent=null;

    public RedBlackNode() {
        list = new ArrayList<>();
    }

    @Override
    public E getValue() {
        return null;
    }

    @Override
    public List<E> getValues() {
        return list;
    }
}
