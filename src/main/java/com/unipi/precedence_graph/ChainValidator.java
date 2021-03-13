package com.unipi.precedence_graph;

import java.util.List;

/**
 *
 *  Blockchain validator
 *  *********************
 *  - If saved hash of current block is not equal to hash calculated based on
 *    calculateBlockHash() then blockchain is not valid
 *  - If saved hash of previous block is not equal to hash of previous
 *    block calculated based on calculateBlockHash() then blockchain is not valid
 *  - If hash of current block does not have the defined prefix then
 *    blockchain is not valid
 *
 */

public class ChainValidator {
    public static Boolean isChainValid(int prefix, List<Block> blockChain){
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[prefix]).replace('\0','0');
        for (int i=1;i<blockChain.size();i++){
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i-1);
            if (!currentBlock.getHash().equals(currentBlock.calculateBlockHash())){
                System.out.println("Current Hashes not equal");
                return false;
            }
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())){
                System.out.println("Previous hash not valid");
                return false;
            }
            if (!currentBlock.getHash().substring(0,prefix).equals(hashTarget)){
                System.out.println("Block is not properly mined");
                return false;
            }
        }
        return true;
    }
}
