package com.unipi.precedence_graph;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Blockchain creation.
 * ********************
 * - If block is genesis, previous hash is set to 0,
 * - If block is not genesis previous hash is previous block's hash
 *  In each block emulationName, process name, process waiting time, dependencies
 *  and ExecutionTimeStamp are saved in the block and added to blockchain
 *  and blockChainList
 *
 */

public class BlockChain {

    private List<Block> blockChain = new ArrayList<>();
    private final int prefix = 3;
    private boolean isGenesisBlock = true;

    //Default Constructor
    public BlockChain(){}

    //Setter
    public void setIsGenesisBlock(Boolean isGenesisBlock){
        this.isGenesisBlock = isGenesisBlock;
    }
    //Getter
    public int getPrefix(){
        return this.prefix;
    }

    public void createBlock(Process process, String emulationName){
        //Dependency processes to String
        String dependencies = "";
        if(!process.getDependencies().isEmpty()){
            dependencies = process.getDependencies().toString();
        }
        //Create Genesis Block
        if(process.isGenesisProcess() && isGenesisBlock){
            Block genesisBlock = new Block("0", emulationName, process.getProcessName(),
                    process.getWaitTime(), dependencies , process.getExecutionTimeStamp());
            genesisBlock.mineBlock(prefix);
            blockChain.add(genesisBlock);
            System.out.println("Node: " +(blockChain.size()-1) +" created");
            //Add genesis block to blockChain list
            PrecedenceGraph.blockChainList.add(genesisBlock);
            isGenesisBlock = false;
        }
        else{
            Block block = new Block(blockChain.get(blockChain.size()-1).getHash(),
                    emulationName, process.getProcessName(),
                    process.getWaitTime(),
                    dependencies, process.getExecutionTimeStamp());
            block.mineBlock(prefix);
            blockChain.add(block);
            System.out.println("Node: " +(blockChain.size()-1) +" created");
            //Add block to blockChain list
            PrecedenceGraph.blockChainList.add(block);
        }
    }

    //Method Overload
    public void createBlock(Process process, String emulationName, String previousHash){
        //Dependency processes to String
        String dependencies = "";
        if(!process.getDependencies().isEmpty()){
            dependencies = process.getDependencies().toString();
        }
        Block genesisBlock = new Block(previousHash, emulationName, process.getProcessName(),
                process.getWaitTime(), dependencies , process.getExecutionTimeStamp());
        genesisBlock.mineBlock(prefix);
        blockChain.add(genesisBlock);
        System.out.println("Node: " +(blockChain.size()-1) +" created");
        //Add genesis block to blockChain list
        PrecedenceGraph.blockChainList.add(genesisBlock);
    }
}
