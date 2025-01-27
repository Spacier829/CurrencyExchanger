package service;

import dao.CurrencyDaoImpl;
import dto.CurrencyRequestDto;
import dto.CurrencyResponseDto;
import entity.CurrencyEntity;
import util.MapperMapStruct;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CurrenciesService {
  private static final CurrenciesService INSTANCE = new CurrenciesService();
  private final CurrencyDaoImpl currencyDao = CurrencyDaoImpl.getInstance();
  private final MapperMapStruct mapper = MapperMapStruct.INSTANCE;

  private CurrenciesService() {
  }

  public static CurrenciesService getInstance() {
    return INSTANCE;
  }

  public List<CurrencyResponseDto> findAll() {
    return currencyDao.findAll().stream().map(mapper::entityToResponseDto).collect(Collectors.toList());
  }

  public Optional<CurrencyResponseDto> findByCode(String code) {
    return currencyDao.findByCode(code).map(mapper::entityToResponseDto);
  }

  public CurrencyResponseDto add(CurrencyRequestDto currencyRequestDto) {
    CurrencyEntity currency = mapper.requestDtoToEntity(currencyRequestDto);
    return mapper.entityToResponseDto(currencyDao.add(currency));
  }
}
