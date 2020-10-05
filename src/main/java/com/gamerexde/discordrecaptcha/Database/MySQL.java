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

    public static final String USER_ERROR = "[DiscordReCaptcha] Error while creating tables, verify user permissions and then try again...";
    public static final String CONNECTION_USER_ERROR = "[DiscordReCaptcha] The connection cannot be established due to a misconfiguration in the config.json or the MySQL Server configuration. Fix the error and try again.";
    public static final String INTERNAL_ERROR = "[DiscordReCaptcha] Internal Error. This is a fatal error! Do not try to ignore it piece of shit";


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

        System.out.println("[DiscordReCaptcha] Trying to connect to MySQL server " + host + ":" + port + "");

        setConnection(hikari);
        if (testConnection()) {
            System.out.println("[DiscordReCaptcha] The MySQL connection to the server " + host + ":" + port + " has been successfully established!");
        } else {
            System.out.println("[DiscordReCaptcha] Something went wrong trying to connect.");
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
            PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS " + Bot.getInstance().getConfig().getString("MySQLtable") + " ("
                    + "USER_ID VARCHAR(45) NOT NULL,"
                    + "USER VARCHAR(45) NOT NULL,"
                    + "VERIFY_ID VARCHAR(45) NOT NULL,"
                    + "isMuted BOOLEAN NOT NULL,"
                    + "isBanned BOOLEAN NOT NULL,"
                    + "isVerified BOOLEAN NOT NULL,"
                    + "MUTE_COUNTER INT NOT NULL,"
                    + "PRIMARY KEY (USER_ID))");

            PreparedStatement create2 = con.prepareStatement("CREATE TABLE IF NOT EXISTS " + Bot.getInstance().getConfig().getString("MySQLtable") + "_privateChannel ("
                    + "USER_ID VARCHAR(45) NOT NULL,"
                    + "VOICE_CHANNEL VARCHAR(45) NOT NULL,"
                    + "isActive BOOLEAN NOT NULL,"
                    + "PRIMARY KEY (USER_ID))");

            PreparedStatement create3 = con.prepareStatement("CREATE TABLE IF NOT EXISTS " + Bot.getInstance().getConfig().getString("MySQLtable") + "_xp ("
                    + "USER_ID VARCHAR(45) NOT NULL,"
                    + "XP INT(45) NOT NULL,"
                    + "LEVEL INT(45) NOT NULL,"
                    + "ROLE VARCHAR(45) NOT NULL,"
                    + "PRIMARY KEY (USER_ID))");

            create.executeUpdate();
            create2.executeUpdate();
            create3.executeUpdate();

        } catch(SQLException e){
            System.out.println(USER_ERROR);
            e.printStackTrace();
        }
    }

    public void setConnection(HikariDataSource connection) {
        this.connection = connection;
    }
}