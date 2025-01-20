package dao;

import dataBaseConnectionPool.DataBaseConnectionPool;
import entity.Currency;
import exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDaoImpl implements CurrencyDao {
  private static final String FIND_ALL_SQL = "SELECT * FROM currencies";
  private static final String FIND_BY_CODE_SQL = "SELECT * FROM currencies WHERE code = ?";
  private static final String ADD_SQL = "INSERT INTO currencies (code, full_name, sign) VALUES (?, ?, ?) RETURNING *";

  @Override
  public List<Currency> findAll() {
    List<Currency> currencies = new ArrayList<>();
    try (Connection connection = DataBaseConnectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        currencies.add(getCurrency(resultSet));
      }
    } catch (SQLException e) {
      throw new DaoException("Failed to find all Currencies.");
    }
    return currencies;
  }

  @Override
  public Optional<Currency> findByCode(String code) {
    try (Connection connection = DataBaseConnectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE_SQL)) {
      preparedStatement.setString(1, code);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return Optional.of(getCurrency(resultSet));
      }
    } catch (SQLException e) {
      throw new DaoException("Failed to find Currency by code: " + code + ".");
    }
    return Optional.empty();
  }

  @Override
  public Currency add(Currency entity) {
    return null;
  }

  private Currency getCurrency(ResultSet resultSet) throws SQLException {
    return new Currency(
        resultSet.getInt("id"),
        resultSet.getString("code"),
        resultSet.getString("full_name"),
        resultSet.getString("sign")
    );
  }
}
