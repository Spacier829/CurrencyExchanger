package dao;

import entity.ExchangeRateEntity;

import java.util.Optional;

public interface ExchangeRateDao extends Dao<ExchangeRateEntity> {
  Optional<ExchangeRateEntity> findByCodes(String baseCode, String targetCode);

  Optional<ExchangeRateEntity> update(ExchangeRateEntity exchangeRate);
}
