package service;

import dao.CurrencyDaoImpl;
import dto.CurrencyResponseDto;
import entity.CurrencyEntity;
import util.Mapper;

import java.util.List;
import java.util.stream.Collectors;

public class CurrenciesService {
  private static final CurrenciesService INSTANCE = new CurrenciesService();

  private final CurrencyDaoImpl currencyDao = CurrencyDaoImpl.getInstance();

  private CurrenciesService() {
  }

  public static CurrenciesService getInstance() {
    return INSTANCE;
  }

  public List<CurrencyResponseDto> findAll() {
    return currencyDao.findAll().stream().map(Mapper::toResponseDto).collect(Collectors.toList());
  }

  public CurrencyResponseDto findByCode(String code) {
    return currencyDao.findByCode(code).map(Mapper::toResponseDto).orElse(null);
  }
}
