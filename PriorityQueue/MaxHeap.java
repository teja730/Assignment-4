package PriorityQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MaxHeap<T extends Comparable> implements PriorityQueueInterface<T> {
    ArrayList<T>[] ar;
    int size;

    public MaxHeap() {
        ar= (ArrayList<T>[]) new ArrayList[10000];
        size=0;
    }


    @Override
    public void insert(T element) {
       /* int k=0;
        for (int i=0;i<ar.length;i++) {
            if (ar[i]==null){
                k=i;
                break;
            }
        }*/
       for (int i=0;i<size;i++){
           if (ar[i].get(0).compareTo(element)==0){
               ar[i].add(element);

               return;
           }
       }

        ArrayList<T> t=new ArrayList<T>();
       t.add(element);
        ar[size]=t;
        arrange(size);
        size++;
    }
    public void arrange(int k) {
        if (k==0)
            return;
        /*if (ar[(k-1)/2]==null)
            ar[(k-1)/2]=ar[k];*/
        if (ar[k].get(0).compareTo(ar[(k-1)/2].get(0))>0){
            swap(k,(k-1)/2);
            arrange((k-1)/2);
        }

    }


    @Override
    public T extractMax()
    {
        if (size==0)
            return null;
        T popped = ar[0].remove(0);

        if (!ar[0].isEmpty())
            return popped;
        ar[0] = ar[size-1];
        ar[size-1]=null;
        size--;
      //  System.out.println("ar[0]="+ar[0]);
        maxHeapify(0);
        return popped;
    }
    private void swap(int fpos, int spos) {
        ArrayList tmp;
        tmp = ar[fpos];
        ar[fpos] = ar[spos];
        ar[spos] = tmp;
    }
    private int leftChild(int pos)
    {
        return (2 * pos)+1;
    }
    private int rightChild(int pos)
    {
        return (2 * pos) + 2;
    }
    private boolean isLeaf(int pos) {
        if (pos >= ((size) / 2) && pos <= size) {
            return true;
        }
        return false;
    }
    private void maxHeapify(int pos) {
        if (isLeaf(pos))
            return;
        if (( ar[rightChild(pos)]!=null&&ar[pos].get(0) .compareTo(ar[rightChild(pos)].get(0))<0)
                ||ar[pos].get(0).compareTo( ar[leftChild(pos)].get(0))<0 ) {
            if (ar[rightChild(pos)]!=null&&ar[leftChild(pos)].get(0).compareTo(ar[rightChild(pos)].get(0))<0) {
                swap(pos, rightChild(pos));
                maxHeapify(rightChild(pos));
            }
            else {
                swap(pos, leftChild(pos));
                maxHeapify(leftChild(pos));
            }
        }
    }

    /*public T extractMax() {
        T temp=ar[0];
        rearrange(0);
        return temp;
    }
    public void rearrange(int k) {
        ar[k]=null;
        if (ar[2*k+1]!=null&&ar[2*k+2]!=null){
            if (ar[2*k+1].compareTo(ar[2*k+2])>0){
                ar[k]=ar[2*k+1];
                rearrange(2*k+1);
            }else{
                ar[k]=ar[2*k+2];
                rearrange(2*k+2);
            }
        }else if (ar[2*k+1]==null&&ar[2*k+2]!=null){
            ar[k]=ar[2*k+2];
            rearrange(2*k+2);

        }else if (ar[2*k+1]!=null&&ar[2*k+2]==null){
            ar[k]=ar[2*k+1];
            rearrange(2*k+1);
        }else if (ar[2*k+1]==null&&ar[2*k+2]==null){
            return;
        }
    }*/

}