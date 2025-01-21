package dao;

import entity.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateDao extends Dao<ExchangeRate> {
  Optional<ExchangeRate> findByCodes(String baseCode, String targetCode);

  Optional<ExchangeRate> update(ExchangeRate exchangeRate);
}
