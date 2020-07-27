package RedBlack;


public class RBTree<T extends Comparable, E> implements RBTreeInterface<T, E>  {
    public RedBlackNode<T,E> root;

   /* public void print(RedBlackNode<T,E> node){
        if (node==null)
            return;
        else for (E person1 : node.getValues()) {
            System.out.println(person1);
        }
            //System.out.println(node.getValues());
        print(node.left);
        print(node.right);

    }*/
    public void arrange(RedBlackNode<T, E> node,RedBlackNode<T, E> child) {
        //base case is to be written
        if (node==null){
            child.color="BLACK";
            return;
        }

        if (node.color.equals("RED")){
            RedBlackNode<T, E> g=node.parent;
            if (g.left!=null&&g.left.equals(node)){
                if (g.right!=null&&g.right.color.equals("RED")/*CHECKING UNCLE COLOR*/){
                    node.color="BLACK";
                    g.right.color="BLACK";
                    g.color="RED";
                    arrange(node.parent.parent,node.parent);
                }else if (g.right==null||g.right.color.equals("BLACK")){
                    if (node.left!=null&&node.left.equals(child)){
                        //Right rotation
                        g.left=node.right;
                        if (node.right!=null)
                            node.right.parent=g.left;
                        node.right=g;

                            node.parent=g.parent;
                            g.parent=node;
                        if (node.parent!=null){
                            if (node.parent.left.equals(g))
                                node.parent.left=node;
                            if (node.parent.right.equals(g))
                                node.parent.right=node;
                        }else root=node;
                        g.color="RED";
                        node.color="BLACK";
                    }else if (node.right!=null&&node.right.equals(child)){
                        //Left rotation
                        node.right=child.left;
                        if (child.left!=null)
                            child.left.parent=node;
                        child.left=node;
                        node.parent=child;
                        g.left=child;
                        child.parent=g;
                        //Right rotation
                        g.left=child.right;
                        if (child.right!=null)
                            child.right.parent=g.left;
                        child.right=g;

                            child.parent=g.parent;
                            g.parent=child;
                        if (child.parent!=null){
                            if (child.parent.left.equals(g))
                                child.parent.left=child;
                            if (child.parent.right.equals(g))
                                child.parent.right=child;
                        }else root=child;
                        g.color="RED";
                        child.color="BLACK";
                    }
                }
            }else if (g.right!=null&&g.right.equals(node)){
                if (g.left!=null&&g.left.color.equals("RED")/*CHECKING UNCLE COLOR*/){
                    node.color="BLACK";
                    g.left.color="BLACK";
                    g.color="RED";
                    arrange(node.parent.parent,node.parent);
                }else if (g.left==null||g.left.color.equals("BLACK")){
                    if (node.right!=null&&node.right.equals(child)){
                        //Left rotation
                        g.right=node.left;
                        if (node.left!=null)
                            node.right.parent=g.right;
                        node.left=g;
                            node.parent=g.parent;
                            g.parent=node;
                        if (node.parent!=null){
                            if (node.parent.left.equals(g))
                                node.parent.left=node;
                            if (node.parent.right.equals(g))
                                node.parent.right=node;
                        }else root=node;
                        g.color="RED";
                        node.color="BLACK";
                    }else if (node.left!=null&&node.left.equals(child)){
                        //Right rotation
                        node.left=child.right;
                        if (child.right!=null)
                            child.right.parent=node;
                        child.right=node;
                        node.parent=child;
                        g.right=child;
                        child.parent=g;
                        //Left rotation
                        g.right=child.left;
                        if (child.left!=null)
                            child.right.parent=g.right;
                        child.left=g;

                            child.parent=g.parent;
                            g.parent=child;
                        if (child.parent!=null){
                            if (child.parent.left.equals(g))
                                child.parent.left=child;
                            if (child.parent.right.equals(g))
                                child.parent.right=child;
                        }else root=child;
                    }
                }
            }
        }
    }
    public void insert(RedBlackNode<T, E> node, T key, E value) {
       /* if (node==null){
            node=new RedBlackNode<>();
            node.key=key;
            node.value=value;
            node.color="RED";

        }*/

        if (key.compareTo(node.key)<0){
            if (node.left==null){
                node.left=new RedBlackNode<>();
                node.left.key=key;
                node.left.getValues().add(value);
                node.left.color="RED";
                node.left.parent=node;
               /* if (node.color.equals("RED")){
                    if (node.parent.left.equals(node)){
                     if (node.parent.right.color.equals("RED")/------------CHECKING UNCLE COLOR){

                     }
                    }

                }*/arrange(node,node.left);
            }else insert(node.left,key,value);

        }else if (key.compareTo(node.key)>0){
            if (node.right==null){
                node.right=new RedBlackNode<>();
                node.right.key=key;
                node.right.getValues().add(value);
                node.right.color="RED";
                node.right.parent=node;
                arrange(node,node.right);
            }else insert(node.right,key,value);
        }else {
            //equality case is to be written
            node.getValues().add(value);
        }
    }

    @Override
    public void insert(T key, E value) {
        //write if root==null
        if (root==null){
            root=new RedBlackNode<>();
            root.key=key;
            root.getValues().add(value);
            root.color="BLACK";
            return;
        }
        insert(root,key,value);

       /* RedBlackNode<T,E> node=root;
        while(node!=null){
            if (key.compareTo(node.key)<0){
                if (node.left!=null)
                    node=node.left;
                else {
                    node.left=new RedBlackNode<>();
                }
            }else if (key.compareTo(node.key)>0){
                //if (node.)
                    node=node.right;
            }else {

            }
        }*/


    }
    public  RedBlackNode<T,E> search(RedBlackNode<T,E> node,T key){
        if (node==null)
            return null;
        if (key.compareTo(node.key)<0)
            return search(node.left,key);
        else if (key.compareTo(node.key)>0)
            return search(node.right,key);
        else return node;
    }

    @Override
    public RedBlackNode<T, E> search(T key) {
        return search(root,key);
    }
}