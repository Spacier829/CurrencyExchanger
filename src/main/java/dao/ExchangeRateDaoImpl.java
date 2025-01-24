package dao;

import exception.ConflictException;
import util.DataBaseConnectionPool;
import entity.CurrencyEntity;
import entity.ExchangeRateEntity;
import exception.DataBaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDaoImpl implements ExchangeRateDao {
  private static final ExchangeRateDaoImpl INSTANCE = new ExchangeRateDaoImpl();
  private static final String FIND_ALL_SQL = "SELECT er.id," +
                                             "bc.id bc_id, bc.code bc_code, bc.full_name bc_full_name, bc.sign bc_sign,"
                                             +
                                             "tc.id tc_id, tc.code tc_code, tc.full_name tc_full_name, tc.sign tc_sign,"
                                             +
                                             "er.rate " +
                                             " FROM exchange_rates AS er" +
                                             " JOIN currencies as bc on bc.id = base_currency_id" +
                                             " JOIN currencies as tc on tc.id = target_currency_id";
  private static final String FIND_BY_CODES_SQL = "SELECT er.id," +
                                                  "bc.id bc_id, bc.code bc_code, bc.full_name bc_full_name, bc.sign bc_sign,"
                                                  +
                                                  "tc.id tc_id, tc.code tc_code, tc.full_name tc_full_name, tc.sign tc_sign,"
                                                  +
                                                  "er.rate " +
                                                  " FROM exchange_rates AS er" +
                                                  " JOIN currencies as bc on bc.id = base_currency_id" +
                                                  " JOIN currencies as tc on tc.id = target_currency_id" +
                                                  " WHERE bc.code = ? AND tc.code = ?";
  private static final String ADD_SQL = "INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) " +
                                        "VALUES (?,?,?) RETURNING id";
  private static final String UPDATE_SQL = "UPDATE exchange_rates SET rate = ? " +
                                           "WHERE base_currency_id = ? AND target_currency_id = ? RETURNING id";

  private ExchangeRateDaoImpl() {
  }

  @Override
  public List<ExchangeRateEntity> findAll() {
    List<ExchangeRateEntity> exchangeRates = new ArrayList<>();
    try (Connection connection = DataBaseConnectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        exchangeRates.add(buildExchangeRate(resultSet));
      }
    } catch (SQLException e) {
      throw new DataBaseException("Failed to find all Exchange Rates");
    }
    return exchangeRates;
  }

  @Override
  public Optional<ExchangeRateEntity> findByCodes(String baseCode, String targetCode) {
    try (Connection connection = DataBaseConnectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODES_SQL)) {
      preparedStatement.setString(1, baseCode);
      preparedStatement.setString(2, targetCode);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return Optional.of(buildExchangeRate(resultSet));
      }
    } catch (SQLException e) {
      throw new DataBaseException("Failed to find Exchange Rate by Codes:" + baseCode + ":" + targetCode);
    }
    return Optional.empty();
  }

  @Override
  public Optional<ExchangeRateEntity> update(ExchangeRateEntity exchangeRate) {
    try (Connection connection = DataBaseConnectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
      preparedStatement.setBigDecimal(1, exchangeRate.getRate());
      preparedStatement.setLong(2, exchangeRate.getBaseCurrency().getId());
      preparedStatement.setLong(3, exchangeRate.getTargetCurrency().getId());
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        exchangeRate.setId(resultSet.getLong("id"));
        return Optional.of(exchangeRate);
      }
    } catch (SQLException e) {
      throw new DataBaseException("Failed to update Exchange Rate by Codes:" + exchangeRate.getBaseCurrency().getCode() +
                                  ":" + exchangeRate.getTargetCurrency().getCode());
    }
    return Optional.empty();
  }

  @Override
  public ExchangeRateEntity add(ExchangeRateEntity exchangeRate) {
    try (Connection connection = DataBaseConnectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(ADD_SQL)) {
      preparedStatement.setLong(1, exchangeRate.getBaseCurrency().getId());
      preparedStatement.setLong(2, exchangeRate.getTargetCurrency().getId());
      preparedStatement.setBigDecimal(3, exchangeRate.getRate());
      ResultSet resultSet = preparedStatement.executeQuery();
      if (!resultSet.next()) {
        throw new DataBaseException("Failed to add Exchange Rate");
      }
      exchangeRate.setId(resultSet.getLong("id"));
      return exchangeRate;
    } catch (SQLException exception) {
      String exceptionMessage = exception.getMessage();
      if (exceptionMessage.contains("[SQLITE_CONSTRAINT_UNIQUE]")) {
        throw new ConflictException("Failed to add exchange rate. Exchange rate already exists.");
      } else if (exceptionMessage.contains("[SQLITE_CONSTRAINT_FOREIGNKEY]")) {
        throw new ConflictException("Failed to add exchange rate. Currency does not exist.");
      }
      throw new DataBaseException("Failed to add Exchange Rate.");
    }
  }

  public static ExchangeRateDaoImpl getInstance() {
    return INSTANCE;
  }

  private ExchangeRateEntity buildExchangeRate(ResultSet resultSet) throws SQLException {
    return new ExchangeRateEntity(
        resultSet.getLong("id"),
        new CurrencyEntity(
            resultSet.getLong("bc_id"),
            resultSet.getString("bc_code"),
            resultSet.getString("bc_full_name"),
            resultSet.getString("bc_sign")
        ),
        new CurrencyEntity(
            resultSet.getLong("tc_id"),
            resultSet.getString("tc_code"),
            resultSet.getString("tc_full_name"),
            resultSet.getString("tc_sign")
        ),
        resultSet.getBigDecimal("rate"));
  }
}
