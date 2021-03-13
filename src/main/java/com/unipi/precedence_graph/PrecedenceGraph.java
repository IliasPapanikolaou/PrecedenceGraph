package com.unipi.precedence_graph;

import java.sql.Connection;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PrecedenceGraph {

    public static Instant globalTimerStart;
    public static volatile List<Process> blockOrder = new ArrayList<>();
    public static  List<Block> blockChainList = new ArrayList<>();

    public static void main(String[] args){

        List<Process> processes = new ArrayList<>(new Parser().readPrecedenceFiles());

        System.out.println("================== Precedence ====================");
        int countGenesis = 0;
        int countSecondary = 0;
        for (Process p : processes){
            if (p.isGenesisProcess()) countGenesis++;
            else countSecondary++;
        }

        System.out.println("Number of genesis processes: " +countGenesis);
        System.out.println("Number of secondary processes: " +countSecondary);

        for (Process p: processes){
            if (p.getWaitTime() != 0) System.out.println(p.getProcessName() +" has to work for " +p.getWaitTime() +"ms");
            if (!p.getDependencies().isEmpty()){
                System.out.print(p.getProcessName() +" has to wait for ");
                for (Process proc : p.getDependencies()){
                    System.out.print(proc.getProcessName() +" ");
                }
                System.out.println();
            }
        }

        System.out.println("=============== Starting Threads =================");
        globalTimerStart = Instant.now();
        for (Process p: processes){
            p.start();
        }

        //Wait for processes to finish before continue
        processes.forEach(process -> {
            try {
                process.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        System.out.println("============= Creating BlockChain ================");
       BlockChain blockChain = new BlockChain();
        //DB Connection
        Repository repository = new Repository();
//        repository.createNewDatabase();
//        repository.createNewTable();
        Connection conn = repository.connect();
        //If exists, retrieve the last Block from DB
        Block lastBlock = repository.retrieveLastBlock(conn);
        if (lastBlock != null) {
            blockChain.setIsGenesisBlock(false);
            //retrieve previous Emulation Name
            int count = Integer.parseInt(lastBlock.getEmulationName().substring(10));
            //Create new Emulation Name - Auto Increment
            String emulationName = String.format("emulation_%d", ++count);
            //Continue the BlockChain from the last Block that already exists in DB
            blockChain.createBlock(blockOrder.get(0), emulationName, lastBlock.getPreviousHash());
            for (int i=1; i < blockOrder.size(); i++){
                blockChain.createBlock(blockOrder.get(i), emulationName);
            }
        }
        //Else if there is not BlockChain in DB, create a new one with GenesisBlock
        else {
            for (Process p: blockOrder){
                blockChain.createBlock(p, "emulation_1");
            }
        }

        //print BlockChain
        blockChainList.forEach(System.out::println);

        //Save Block Chain to DB
        System.out.println("============= Saving to DataBase ================");
        for (Block b : blockChainList){
            repository.insert(conn, b.getEmulationName(), b.getHash(), b.getPreviousHash(),
                    b.getProcessName(), b.getExecutionTime(), b.getDependencies(),
                    b.getTimeStamp(), b.getNonce());
        }
        System.out.println("BlockChain saved Successfully");

        System.out.println("============= Validate BlockChain ================");

        Boolean isValid = repository.verifyBlockChain(conn, blockChain.getPrefix());
        System.out.println("BlockChain Valid: " +isValid);
        repository.close(conn);

    }
}
