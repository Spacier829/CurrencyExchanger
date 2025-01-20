package dataBaseConnectionPool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseConnectionPool {
  private static final HikariConfig config = new HikariConfig();
  private static final HikariDataSource dataSource;

  static {
    config.setJdbcUrl("jdbc:sqlite::resource:currency_exchanger.db");
    config.setDriverClassName("org.sqlite.JDBC");
    dataSource = new HikariDataSource(config);
  }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}
