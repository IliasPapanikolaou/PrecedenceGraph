package com.unipi.precedence_graph;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repository {

    public void createNewDatabase(){
        String url = "jdbc:sqlite:src/main/resources/BlockChain.db";

        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null){
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " +meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Connection connect(){
        // db parameters
        String url = "jdbc:sqlite:src/main/resources/BlockChain.db";
        // create a connection to the database
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public void close(Connection conn){
        if (conn != null) {
            try {
                conn.close();
                //System.out.println("Connection to SQLite has been closed.");
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void createNewTable(){
        // db parameters
        String url = "jdbc:sqlite:src/main/resources/BlockChain.db";
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS blockchain (\n" +
                "id integer PRIMARY KEY AUTOINCREMENT, \n" +
                "emulationName text, \n" +
                "hash text, \n" +
                "previousHash text, \n" +
                "processName text, \n" +
                "executionTime integer, \n" +
                "dependencies text, \n" +
                "timeStamp integer, \n" +
                "nonce integer);";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void insert(Connection conn, String emulationName, String hash, String previousHash, String processName,
                        int executionTime, String dependencies, long timeStamp, long nonce){

        String sql = "INSERT INTO blockchain(emulationName, hash, previousHash," +
                "processName, executionTime, dependencies, timeStamp," +
                "nonce) VALUES(?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, emulationName);
            preparedStatement.setString(2, hash);
            preparedStatement.setString(3, previousHash);
            preparedStatement.setString(4, processName);
            preparedStatement.setInt(5, executionTime);
            preparedStatement.setString(6, dependencies);
            preparedStatement.setLong(7, timeStamp);
            preparedStatement.setLong(8, nonce);
            preparedStatement.executeUpdate();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Block retrieveLastBlock(Connection conn){

        String sql = "SELECT emulationName, hash FROM blockchain ORDER BY id DESC LIMIT 1";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                Block lastBlock = new Block(resultSet.getString("emulationName"),
                        resultSet.getString("hash"));
                return lastBlock;
            }
            else return null;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public Boolean verifyBlockChain(Connection conn, int prefix){

        String sql = "SELECT * FROM blockchain ORDER BY id ASC";
        List<Block> blocks = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                Block block = new Block(
                        resultSet.getString("hash"),
                        resultSet.getString("previousHash"),
                        resultSet.getString("emulationName"),
                        resultSet.getString("processName"),
                        resultSet.getInt("executionTime"),
                        resultSet.getString("dependencies"),
                        resultSet.getLong("timeStamp"),
                        resultSet.getLong("nonce"));

                blocks.add(block);
            }
            System.out.println("Total blocks in BlockChain DataBase: " +blocks.size());
            return ChainValidator.isChainValid(prefix, blocks);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}
