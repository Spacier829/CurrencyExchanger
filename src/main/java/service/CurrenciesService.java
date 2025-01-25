package service;

import dao.CurrencyDaoImpl;
import dto.CurrencyRequestDto;
import dto.CurrencyResponseDto;
import entity.CurrencyEntity;
import util.Mapper;

import java.util.List;
import java.util.Optional;
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
    return currencyDao.findAll().stream().map(Mapper::currencyToResponseDto).collect(Collectors.toList());
  }

  public Optional<CurrencyResponseDto> findByCode(String code) {
    return currencyDao.findByCode(code).map(Mapper::currencyToResponseDto);
  }

  public CurrencyResponseDto add(CurrencyRequestDto currencyRequestDto) {
    CurrencyEntity currency = Mapper.dtoToCurrency(currencyRequestDto);
    return Mapper.currencyToResponseDto(currencyDao.add(currency));
  }
}
