package Trie;


import java.util.LinkedList;
import java.util.Queue;

public class Trie<T> implements TrieInterface<T> {

    TrieNode<T> root;
    int size_of_array=94;

    public Trie() {
        root = new TrieNode<T>(size_of_array);
    }

    public boolean isempty(TrieNode<T>[] ar){
       for (int i=0;i<94;i++)
            if (ar[i]!=null)
                return false;
        return true;
    }

    public boolean deletehelper(String word,TrieNode<T> temp,int depth) {
        int index=word.charAt(depth) - 32;
        if (depth==word.length()-1){
            if (temp.ar[index].b) {
                temp.ar[index].value=null;
                temp.ar[index].b = false;
            }
            if (isempty(temp.ar[index].ar))
                temp.ar[index]=null;
            return true;
        }
        boolean b=deletehelper(word,temp.ar[index],depth+1);
        if (isempty(temp.ar[index].ar))
            temp.ar[index]=null;
        return b;
    }

    @Override
    public boolean delete(String word) {
        TrieNode<T> check=search(word);
        boolean b;
        if (check==null)
            b= false;
        else b= deletehelper(word,root,0);
        /*if (b)
            System.out.println("DELETED");
        else System.out.println("ERROR DELETING");*/
        return b;
    }

    @Override
    public TrieNode search(String word) {
        int length =word.length();
        TrieNode<T> temp=root;

        for (int i=0;i<length;i++){
            int index=word.charAt(i) - 32;
            if (temp.ar[index]==null) {
                return null;
            }
            if (i==length-1&&temp.ar[index].b){
                return temp.ar[index];
            }
            temp=temp.ar[index];
        }
        return null;
    }

    @Override
    public TrieNode startsWith(String prefix) {
        int length =prefix.length();
        TrieNode<T> temp=root;

        for (int i=0;i<length;i++){
            int index=prefix.charAt(i) - 32;
            if (temp.ar[index]==null) {
                return null;
            }
            if (i==length-1){
                return temp.ar[index];
            }
            temp=temp.ar[index];
        }
        return null;
    }

    @Override
    public void printTrie(TrieNode trieNode) {
        if (trieNode.b)
            System.out.println(trieNode.getValue());
        for (int i=0;i<size_of_array;i++){
            if (trieNode.ar[i]!=null)
                printTrie(trieNode.ar[i]);
        }
    }



    public boolean insert(String word, T value) {
        int length =word.length();
        TrieNode<T> temp=root;

        for (int i=0;i<length;i++){
            int index=word.charAt(i) - 32;
            if (temp.ar[index]==null) {
                temp.ar[index] = new TrieNode<T>(size_of_array);
            }

            if (i==length-1){
                temp.ar[index].value=value;
                temp.ar[index].b=true;
                return true;
            }
            temp=temp.ar[index];
        }
        return false;
    }

    @Override
    public void printLevel(int level) {

        Queue<TrieNode<T>[]> o=new LinkedList<TrieNode<T>[]>();
        o.add(root.ar);
        int i=1;
        while(i<=level){
            Queue<TrieNode<T>[]> n=new LinkedList<TrieNode<T>[]>();
            Queue<Integer> list = new LinkedList<Integer>();

            while(!o.isEmpty()){
                TrieNode<T>[] x=o.remove();
                for (int k=0;k<size_of_array;k++){
                 //   System.out.println('Y');
                    if (x[k]!=null){
                     //   System.out.println('k'+'='+k);
                        n.add(x[k].ar);
                        //char c= (char)(k+32);
                        if (i==level&&k!=0)
                            list.add(k);
                       // System.out.print(c+",");
                    }
                }
            }
            String x="";
            while (!list.isEmpty()){
                x=x+(char)(list.remove()+32);
            }
            int[] temp=new int[x.length()];
            for (int k=0;k<x.length();k++){
                temp[k]=(int)x.charAt(k)-32;
            }
            for (int k=0;k<temp.length;k++){
                for (int j=0;j<temp.length-k-1;j++){
                    if (temp[j]>temp[j+1]){
                        Integer tem=temp[j];
                        temp[j]=temp[j+1];
                        temp[j+1]=tem;
                    }
                }
            }
            for (int k=0;k<temp.length;k++)
                list.add(temp[k]);
            //System.out.println();
            o=n;
            if (i==level) {
                System.out.print("Level " + i + ": ");
                if (!list.isEmpty()) {
                    int asd=list.remove()+32;
                    System.out.print((char)asd);
                }
                while (!list.isEmpty()) {
                    char c = (char) (list.remove() + 32);
                    System.out.print(',');
                    System.out.print( c);
                }
                System.out.println();
            }
            i++;
        }

    }

    @Override
    public void print() {
        System.out.println("-------------");
        System.out.println("Printing Trie");
        Queue<TrieNode<T>[]> o=new LinkedList<TrieNode<T>[]>();
        o.add(root.ar);
        int i=1;
        while(!o.isEmpty()){
            Queue<TrieNode<T>[]> n=new LinkedList<TrieNode<T>[]>();
            Queue<Integer> list = new LinkedList<>();
            while(!o.isEmpty()){
                TrieNode<T>[] x=o.remove();
                for (int k=0;k<size_of_array;k++){
                    //   System.out.println('Y');
                    if (x[k]!=null){
                        //   System.out.println('k'+'='+k);
                        n.add(x[k].ar);
                        //char c= (char)(k+32);
                        if (k!=0)
                            list.add(k);
                        // System.out.print(c+",");
                    }
                }
            }
            String x="";
            while (!list.isEmpty()){
                x=x+(char)(list.remove()+32);
            }
            int[] temp=new int[x.length()];
            for (int k=0;k<x.length();k++){
                temp[k]=x.charAt(k)-32;
            }
            for (int k=0;k<temp.length;k++){
                for (int j=0;j<temp.length-k-1;j++){
                    if (temp[j]>temp[j+1]){
                        Integer tem=temp[j];
                        temp[j]=temp[j+1];
                        temp[j+1]=tem;
                    }
                }
            }
            for (int k=0;k<temp.length;k++)
                list.add(temp[k]);
            //System.out.println();
            o=n;
             {
                System.out.print("Level " + i + ": ");
                if (!list.isEmpty()) {
                    int asd=list.remove()+32;
                    System.out.print((char)asd);
                }
                while (!list.isEmpty()) {
                    char c = (char) (list.remove() + 32);
                    System.out.print(',');
                    System.out.print( c);
                }
                System.out.println();
            }
            i++;
        }
        System.out.println("-------------");
    }
}