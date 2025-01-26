package service;

import dao.CurrencyDaoImpl;
import dao.ExchangeRateDaoImpl;
import dto.ExchangeRateRequestDto;
import dto.ExchangeRateResponseDto;
import entity.CurrencyEntity;
import entity.ExchangeRateEntity;
import util.Mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExchangeRatesService {
  private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();
  private final ExchangeRateDaoImpl exchangeRateDao = ExchangeRateDaoImpl.getInstance();

  private ExchangeRatesService() {
  }

  public static ExchangeRatesService getInstance() {
    return INSTANCE;
  }

  public List<ExchangeRateResponseDto> findAll() {
    return exchangeRateDao.findAll().stream().map(Mapper::exchangeRateToResponseDto).collect(Collectors.toList());
  }

  public Optional<ExchangeRateResponseDto> findByCodes(String baseCode, String targetCode) {
    return exchangeRateDao.findByCodes(baseCode, targetCode).map(Mapper::exchangeRateToResponseDto);
  }

  public ExchangeRateResponseDto add(ExchangeRateRequestDto exchangeRateRequestDto) {
    CurrencyDaoImpl currencyDao = CurrencyDaoImpl.getInstance();
    Optional<CurrencyEntity> baseCurrency = currencyDao.findByCode(exchangeRateRequestDto.getBaseCurrencyCode());
    Optional<CurrencyEntity> targetCurrency = currencyDao.findByCode(exchangeRateRequestDto.getTargetCurrencyCode());
    ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity();
    if (baseCurrency.isPresent() && targetCurrency.isPresent()) {
      exchangeRateEntity.setBaseCurrency(baseCurrency.get());
      exchangeRateEntity.setTargetCurrency(targetCurrency.get());
      exchangeRateEntity.setRate(exchangeRateRequestDto.getRate());
    }
    return Mapper.exchangeRateToResponseDto(exchangeRateDao.add(exchangeRateEntity));
  }

  public ExchangeRateResponseDto update(ExchangeRateRequestDto exchangeRateRequestDto) {
    CurrencyDaoImpl currencyDao = CurrencyDaoImpl.getInstance();
    Optional<CurrencyEntity> baseCurrency = currencyDao.findByCode(exchangeRateRequestDto.getBaseCurrencyCode());
    Optional<CurrencyEntity> targetCurrency = currencyDao.findByCode(exchangeRateRequestDto.getTargetCurrencyCode());
    ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity();
    if (baseCurrency.isPresent() && targetCurrency.isPresent()) {
      exchangeRateEntity.setBaseCurrency(baseCurrency.get());
      exchangeRateEntity.setTargetCurrency(targetCurrency.get());
      exchangeRateEntity.setRate(exchangeRateRequestDto.getRate());
    }
    return Mapper.exchangeRateToResponseDto(exchangeRateDao.update(exchangeRateEntity).get());
  }
}
