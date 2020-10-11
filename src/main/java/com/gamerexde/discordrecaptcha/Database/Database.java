package com.gamerexde.discordrecaptcha.Database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {
    Connection connection;

    public abstract HikariDataSource getHikari();

    public abstract void load();

    public boolean verifyUser(String userid) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getHikari().getConnection();
            stmt = conn.createStatement();

            String query = "SELECT * FROM `oauth_discord` WHERE `USER_ID` LIKE '" + userid + "'";

            rs = stmt.executeQuery(query);

            if (rs.next()) {
                int isVerified = rs.getInt("isVerified");
                return isVerified == 1;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
