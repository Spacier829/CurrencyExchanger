package dao;

import models.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateDao extends Dao<ExchangeRate> {
  Optional<ExchangeRate> findByCodes(String baseCode, String targetCode);
}
