package dao;

import entity.CurrencyEntity;
import exception.ConflictException;
import exception.DataBaseException;
import util.DataBaseConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDaoImpl implements CurrencyDao {
  private static final CurrencyDaoImpl INSTANCE = new CurrencyDaoImpl();
  private static final String FIND_ALL_SQL = "SELECT id, code, full_name, sign " +
                                             "FROM currencies";
  private static final String FIND_BY_CODE_SQL = FIND_ALL_SQL + " WHERE code = ?";
  private static final String ADD_SQL = "INSERT INTO currencies (code, full_name, sign) " +
                                        "VALUES (?,?,?) " +
                                        "RETURNING id, code, full_name, sign";

  private CurrencyDaoImpl() {
  }

  @Override
  public List<CurrencyEntity> findAll() {
    List<CurrencyEntity> currencies = new ArrayList<>();
    try (Connection connection = DataBaseConnectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        currencies.add(buildCurrency(resultSet));
      }
    } catch (SQLException e) {
      throw new DataBaseException("Failed to find all currencies.");
    }
    return currencies;
  }

  @Override
  public Optional<CurrencyEntity> findByCode(String code) {
    try (Connection connection = DataBaseConnectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE_SQL)) {
      preparedStatement.setString(1, code);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return Optional.of(buildCurrency(resultSet));
      }
    } catch (SQLException e) {
      throw new DataBaseException("Failed to find currency by code: " + code + ".");
    }
    return Optional.empty();
  }

  @Override
  public CurrencyEntity add(CurrencyEntity entity) {
    try (Connection connection = DataBaseConnectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(ADD_SQL)) {
      preparedStatement.setString(1, entity.getCode());
      preparedStatement.setString(2, entity.getFullName());
      preparedStatement.setString(3, entity.getSign());
      ResultSet resultSet = preparedStatement.executeQuery();
      if (!resultSet.next()) {
        throw new DataBaseException("Failed to add currency with code: " + entity.getCode() + ".");
      }
      return buildCurrency(resultSet);
    } catch (SQLException exception) {
      String exceptionMessage = exception.getMessage();
      if (exceptionMessage.contains("[SQLITE_CONSTRAINT_UNIQUE]")) {
        if (exceptionMessage.contains("full_name")) {
          throw new ConflictException("Failed to add currency. Name:" + entity.getFullName() + " already exists.");
        } else if (exceptionMessage.contains("code")) {
          throw new ConflictException("Failed to add currency. Code:" + entity.getCode() + " already exists.");
        }
      }
      throw new DataBaseException("Failed to add currency by code:" + entity.getCode() + ".");
    }
  }

  public static CurrencyDaoImpl getInstance() {
    return INSTANCE;
  }

  private CurrencyEntity buildCurrency(ResultSet resultSet) throws SQLException {
    return new CurrencyEntity(
        resultSet.getLong("id"),
        resultSet.getString("code"),
        resultSet.getString("full_name"),
        resultSet.getString("sign")
    );
  }
}
