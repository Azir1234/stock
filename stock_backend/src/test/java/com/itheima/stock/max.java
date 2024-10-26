package com.itheima.stock;

public class max {
    public static void main(String[] args) {
        String a="14444112442aa44442244311222222244445444243334444ccc4444";
        int max=0;
        int mi=0;
        for (int j = 0; j < a.length(); j++) {

            if( j+1<a.length()-1 &&a.charAt(j+1)==a.charAt(j)){
                int index=j;
                while(index+1<a.length()&&a.charAt(index)==a.charAt(index+1)){
                    index++;
                }
                if(max<index-j+1){
                    max=index-j+1;
                    mi=j;
                }
                j=index;
            }
        }
        System.out.println(max);
        System.out.println(a.charAt(mi));
    }



}
