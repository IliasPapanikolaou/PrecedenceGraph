package com.unipi.precedence_graph;

import java.util.ArrayList;
import java.util.List;

public class BlockChain {

    public static List<Block> blockChain = new ArrayList<>();
    public static int prefix = 3;
    public static boolean isGenesisBlock = true;

    public static synchronized void createBlock(Process process, String emulationName){
        //Dependency processes to String
        String dependencies = "";
        if(!process.getDependencies().isEmpty()){
            dependencies = process.getDependencies().toString();
        }
        //Create Genesis Block

        if(process.isGenesisProcess() && isGenesisBlock){
            Block genesisBlock = new Block("0", emulationName, process.getProcessName(),
                    String.valueOf(process.getWaitTime()), dependencies , process.getExecutionTimeStamp());
            genesisBlock.mineBlock(prefix);
            blockChain.add(genesisBlock);
            System.out.println("Node: " +(blockChain.size()-1) +" created");
            //Add genesis block to blockChain list
            PrecedenceGraph.blockChain.add(genesisBlock);
            isGenesisBlock = false;
        }
        else{
            Block block = new Block(blockChain.get(blockChain.size()-1).getHash(),
                    emulationName, process.getProcessName(),
                    String.valueOf(process.getWaitTime()),
                    dependencies, process.getExecutionTimeStamp());
            block.mineBlock(prefix);
            blockChain.add(block);
            System.out.println("Node: " +(blockChain.size()-1) +" created");
            //Add block to blockChain list
            PrecedenceGraph.blockChain.add(block);
        }

    }
}
