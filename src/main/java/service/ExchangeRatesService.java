package service;

import dao.ExchangeRateDaoImpl;
import dto.ExchangeRateResponseDto;
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
}
