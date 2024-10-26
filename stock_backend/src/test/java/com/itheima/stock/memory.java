package com.itheima.stock;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class memory {

    public static void main(String[] args) {


       /* int num[]={5,10,3,3,9,2,6,4,4,1};

        num=sortPart(0,num.length-1,num);

        System.out.println(Arrays.toString(num));*/
        int n=2;
        if(n==1) System.out.println("true");;
        while(n%2==0){
            n/=2;
            if(n==1){
                System.out.println("true");
            }
        }
        System.out.println("false");

    }
    //
    public static int[] sortPart(int le,int ri,int[]num){
        int dex=num[le];
        int l=le;
        int r=ri;
        while(true){
            if(r<=l){
                break;
            }
            while(num[r]>dex)
            {
                if(r<=l){
                    num[r]=dex;
                    break;
                }
                r--;
            }
            num[l]=num[r];
            while(num[l]<=dex){
                if(r<=l){
                    num[r]=dex;
                    break;
                }
                l++;
            }
            num[r]=num[l];
        }
        if(le<l-1){
            sortPart(le,l-1,num);
        }
        if(l+1<ri) {
            sortPart(l + 1, ri, num);
        }
        return num;
    }
}
