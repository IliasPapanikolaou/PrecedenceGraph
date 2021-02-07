package com.unipi.precedence_graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class PrecedenceGraph {

    public static void main(String[] args) {

        try {
            Scanner p_precedence = new Scanner(new File("src/main/resources/p_precedence.txt"));
            Scanner p_timings = new Scanner(new File("src/main/resources/p_timings.txt"));

            while(p_precedence.hasNext("(?i)(P\\d+)")){
                System.out.print("I am " +p_precedence.next());
                if(p_precedence.hasNext("(?i)\\s*waitfor\\s*")){
                    System.out.print(" and I am waiting for ");
                    p_precedence.next();

                    if(p_precedence.hasNext("(?i)(P\\d+)\\s*(,\\s*P\\d+)+")){
                        List<String> precedenceList = Arrays.asList(p_precedence.next().split("\\s*,"));

                        for(String p : precedenceList){
                            if (precedenceList.indexOf(p)==0) System.out.print(p);
                            else if (precedenceList.indexOf(p) == precedenceList.size()-1) System.out.println(" and " +p);
                            else System.out.print(" and " +p);
                        }
                    }
                    else {
                        System.out.println(p_precedence.next());
                    }
                }
                else System.out.println(" and I am not waiting anyone!");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
