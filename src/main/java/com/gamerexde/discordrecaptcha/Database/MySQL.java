package com.gamerexde.discordrecaptcha.Database;

import com.gamerexde.discordrecaptcha.Bot;
import com.gamerexde.discordrecaptcha.DiscordOAuthAuthenticator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class MySQL extends Database {
    private HikariDataSource connection;
    private HikariDataSource hikari;

    public String host, database, username, password;
    public int port;

    @Override
    public void load() { this.initMySQLDatabase(); }

    /*
    public void reconnect() {
        reconnectMySQL();
    }
     */

    public static final String USER_ERROR = "[DiscordOAuth] Error while creating tables, verify user permissions and then try again...";
    public static final String CONNECTION_USER_ERROR = "[DiscordOAuth] The connection cannot be established due to a misconfiguration in the config.json or the MySQL Server configuration. Fix the error and try again.";
    public static final String INTERNAL_ERROR = "[DiscordOAuth] Internal Error. This is a fatal error! Do not try to ignore it piece of shit";


    public void initMySQLDatabase() {
        host = Bot.getInstance().getConfig().getString("MySQLhost");
        port = Bot.getInstance().getConfig().getInt("MySQLport");
        database = Bot.getInstance().getConfig().getString("MySQLdatabase");
        username = Bot.getInstance().getConfig().getString("MySQLuser");
        password = Bot.getInstance().getConfig().getString("MySQLpassword");

        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(10);
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        hikari.addDataSourceProperty("serverName", host);
        hikari.addDataSourceProperty("port", port);
        hikari.addDataSourceProperty("databaseName", database);
        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);

        //Logger
        /*
        hikari.addDataSourceProperty("logger", "com.mysql.jdbc.log.Slf4JLogger");
        hikari.addDataSourceProperty("profileSQL", true);
         */

        hikari.setMinimumIdle(5);
        hikari.setLeakDetectionThreshold(15000);
        hikari.setConnectionTestQuery("SELECT 1");
        hikari.setConnectionTimeout(1000);
        hikari.setPoolName("discordrecaptcha-pool");

        System.out.println("[DiscordOAuth] Trying to connect to MySQL server " + host + ":" + port + "");

        setConnection(hikari);
        if (testConnection()) {
            System.out.println("[DiscordOAuth] The MySQL connection to the server " + host + ":" + port + " has been successfully established!");
        } else {
            System.out.println("[DiscordOAuth] Something went wrong trying to connect.");
        }
        createTable();

    }

    public HikariDataSource getHikari() {
        return connection;
    }

    private boolean testConnection() {
        try {
            Connection con = getHikari().getConnection();
            Statement create = con.createStatement();
            ResultSet rs = create.executeQuery("SELECT 1;");
            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(CONNECTION_USER_ERROR);
            e.printStackTrace();
        }
        return false;
    }

    public void createTable() {
        try {
            Connection con = getHikari().getConnection();

            con.prepareStatement("CREATE TABLE IF NOT EXISTS " + Bot.getInstance().getConfig().getString("MySQLtable") + "_users ("
                    + "USER_ID VARCHAR(45) NOT NULL,"
                    + "USERNAME VARCHAR(45) NOT NULL,"
                    + "PRIMARY KEY (USER_ID))").executeUpdate();

            con.prepareStatement("CREATE TABLE IF NOT EXISTS " + Bot.getInstance().getConfig().getString("MySQLtable") + "_discord ("
                    + "USER_ID VARCHAR(45) NOT NULL,"
                    + "USERNAME VARCHAR(45) NOT NULL,"
                    + "isVerified BOOLEAN NOT NULL,"
                    + "isBanned BOOLEAN NOT NULL,"
                    + "PRIMARY KEY (USER_ID))").executeUpdate();

            con.prepareStatement("CREATE TABLE IF NOT EXISTS " + Bot.getInstance().getConfig().getString("MySQLtable") + "_profile ("
                    + "USER_ID VARCHAR(45) NOT NULL,"
                    + "USERNAME VARCHAR(45) NOT NULL,"
                    + "BIO MEDIUMTEXT NOT NULL,"
                    + "COUNTRY VARCHAR(255) NOT NULL,"
                    + "WEBSITE VARCHAR(255) NOT NULL,"
                    + "DISCORD VARCHAR(255) NOT NULL,"
                    + "YOUTUBE VARCHAR(255) NOT NULL,"
                    + "TWITTER VARCHAR(255) NOT NULL,"
                    + "TWITCH VARCHAR(255) NOT NULL,"
                    + "PRIMARY KEY (USER_ID))").executeUpdate();

        } catch(SQLException e){
            System.out.println(USER_ERROR);
            e.printStackTrace();
        }
    }

    public void setConnection(HikariDataSource connection) {
        this.connection = connection;
    }
}