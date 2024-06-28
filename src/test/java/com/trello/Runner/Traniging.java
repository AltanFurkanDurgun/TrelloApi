package com.trello.Runner;

import java.util.ArrayList;
import java.util.List;

public class Traniging {

    public static void main(String[] args) {

        List<String> list= new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            String a= "abc";
            list.add(a);

        }
        System.out.println("list.get(0) = " + list.get(0));
        System.out.println("list.get(1) = " + list.get(1));

    }


}
