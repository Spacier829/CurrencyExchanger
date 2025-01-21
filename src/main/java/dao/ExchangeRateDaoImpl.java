package dao;

import dataBaseConnectionPool.DataBaseConnectionPool;
import entity.Currency;
import entity.ExchangeRate;
import exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDaoImpl implements ExchangeRateDao {
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
                                        "VALUES (?,?,?) RETURNING *";

  private static final String UPDATE_SQL = "UPDATE exchange_rates SET rate = ?" +
                                           "WHERE base_currency_id = ? AND target_currency_id = ? RETURNING *";

  @Override
  public List<ExchangeRate> findAll() {
    List<ExchangeRate> exchangeRates = new ArrayList<>();
    try (Connection connection = DataBaseConnectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        exchangeRates.add(buildExchangeRate(resultSet));
      }
    } catch (SQLException e) {
      throw new DaoException("Failed to find all Exchange Rates");
    }
    return exchangeRates;
  }

  @Override
  public Optional<ExchangeRate> findByCodes(String baseCode, String targetCode) {
    try (Connection connection = DataBaseConnectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODES_SQL)) {
      preparedStatement.setString(1, baseCode);
      preparedStatement.setString(2, targetCode);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return Optional.of(buildExchangeRate(resultSet));
      }
    } catch (SQLException e) {
      throw new DaoException("Failed to find Exchange Rate by Codes:" + baseCode + ":" + targetCode);
    }
    return Optional.empty();
  }

  @Override
  public Optional<ExchangeRate> update(ExchangeRate exchangeRate) {
    try (Connection connection = DataBaseConnectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
      preparedStatement.setBigDecimal(1, exchangeRate.getRate());
      preparedStatement.setInt(2, exchangeRate.getBaseCurrency().getId());
      preparedStatement.setInt(3, exchangeRate.getTargetCurrency().getId());
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return Optional.of(buildExchangeRate(resultSet));
      }
    } catch (SQLException e) {
      throw new DaoException("Failed to update Exchange Rate by Codes:" + exchangeRate.getBaseCurrency().getCode() +
                             ":" + exchangeRate.getTargetCurrency().getCode());
    }
    return Optional.empty();
  }

  @Override
  public ExchangeRate add(ExchangeRate entity) {
    try (Connection connection = DataBaseConnectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(ADD_SQL)) {
      preparedStatement.setInt(1, entity.getBaseCurrency().getId());
      preparedStatement.setInt(2, entity.getTargetCurrency().getId());
      preparedStatement.setBigDecimal(3, entity.getRate());
      ResultSet resultSet = preparedStatement.executeQuery();
      return buildExchangeRate(resultSet);
    } catch (SQLException e) {
      // Добавить исключения на добавку данных с разными параметрами
      throw new DaoException("Failed to add Exchange Rate.");
    }
  }

  private ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
    return new ExchangeRate(
        resultSet.getInt("id"),
        new Currency(
            resultSet.getInt("bc_id"),
            resultSet.getString("bc_code"),
            resultSet.getString("bc_full_name"),
            resultSet.getString("bc_sign")
        ),
        new Currency(
            resultSet.getInt("tc_id"),
            resultSet.getString("tc_code"),
            resultSet.getString("tc_full_name"),
            resultSet.getString("tc_sign")
        ),
        resultSet.getBigDecimal("rate"));
  }
}
