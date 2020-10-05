package com.gamerexde.discordrecaptcha.Database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;

public abstract class Database {
    Connection connection;

    public abstract HikariDataSource getHikari();

    public abstract void load();
}
