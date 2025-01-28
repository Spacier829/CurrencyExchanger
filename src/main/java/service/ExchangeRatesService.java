package service;

import dao.CurrencyDaoImpl;
import dao.ExchangeRateDaoImpl;
import dto.ExchangeRateRequestDto;
import dto.ExchangeRateResponseDto;
import entity.CurrencyEntity;
import entity.ExchangeRateEntity;
import exception.NotFoundException;
import util.MapperUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExchangeRatesService {
  private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();
  private final ExchangeRateDaoImpl exchangeRateDao = ExchangeRateDaoImpl.getInstance();
  private final MapperUtil mapper = MapperUtil.INSTANCE;

  private ExchangeRatesService() {
  }

  public static ExchangeRatesService getInstance() {
    return INSTANCE;
  }

  public List<ExchangeRateResponseDto> findAll() {
    return exchangeRateDao.findAll().stream().map(mapper::entityToDto).collect(Collectors.toList());
  }

  public Optional<ExchangeRateResponseDto> findByCodes(String baseCode, String targetCode) {
    return exchangeRateDao.findByCodes(baseCode, targetCode).map(mapper::entityToDto);
  }

  public ExchangeRateResponseDto add(ExchangeRateRequestDto exchangeRateRequestDto) {
    return mapper.entityToDto(exchangeRateDao.addByRequestDto(exchangeRateRequestDto));
  }

  public ExchangeRateResponseDto update(ExchangeRateRequestDto exchangeRateRequestDto) {
    CurrencyDaoImpl currencyDao = CurrencyDaoImpl.getInstance();
    Optional<CurrencyEntity> baseCurrency = currencyDao.findByCode(exchangeRateRequestDto.getBaseCurrencyCode());
    if (baseCurrency.isEmpty()) {
      throw new NotFoundException("Currency with code:" + exchangeRateRequestDto.getBaseCurrencyCode() + " not found");
    }
    Optional<CurrencyEntity> targetCurrency = currencyDao.findByCode(exchangeRateRequestDto.getTargetCurrencyCode());
    if (targetCurrency.isEmpty()) {
      throw new NotFoundException("Currency with code:" + exchangeRateRequestDto.getTargetCurrencyCode()
                                  + " not found");
    }
    ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity();
    exchangeRateEntity.setBaseCurrency(baseCurrency.get());
    exchangeRateEntity.setTargetCurrency(targetCurrency.get());
    exchangeRateEntity.setRate(exchangeRateRequestDto.getRate());
    exchangeRateEntity = exchangeRateDao.update(exchangeRateEntity).orElse(null);

    return mapper.entityToDto(exchangeRateEntity);
  }
}
