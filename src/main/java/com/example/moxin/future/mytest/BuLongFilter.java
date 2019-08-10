package com.example.moxin.future.mytest;


import java.util.ArrayList;
import java.util.List;

public class BuLongFilter {

    public static void main(String []args){
        byte[] byteArray = new byte[128];
        List<String> myList = new ArrayList<String>();
        for (int i = 0;i<70;i++){
            myList.add("targer"+i);
        }

        for (String myKey:myList){
            int p =JSHash(myKey)&(byteArray.length-1);
            byteArray[p]=1;
        }
        int count=0;
        for (int i = 0;i<byteArray.length;i++){
            System.out.println(i+"----"+byteArray[i]);
            if(byteArray[i]==1){
                count++;
            }
        }
        System.out.println(count);


    }

    /**
     * JS算法
     */
    public static int JSHash(String str) {
        int hash = 1315423911;
        for (int i = 0; i < str.length(); i++) {
            hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
        }
        return (hash & 0x7FFFFFFF);
    }

}
