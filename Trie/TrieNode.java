package Trie;


import Util.NodeInterface;


public class TrieNode<T> implements NodeInterface<T> {
    TrieNode<T>[] ar;
    boolean b=false;
    public TrieNode(int size){
        ar=new TrieNode[size];
        value=null;
    //    ar=new TrieNode[size];
    }

    T value;
    @Override
    public T getValue() {
        return value;
    }


}