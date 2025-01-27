package util;

import dto.CurrencyRequestDto;
import dto.CurrencyResponseDto;
import dto.ExchangeResponseDto;
import entity.CurrencyEntity;
import entity.ExchangeRateEntity;

public class Mapper implements MapperMapStruct {

  MapperMapStruct mapper = MapperMapStruct.INSTANCE;

  @Override
  public CurrencyResponseDto entityToResponseDto(CurrencyEntity currencyEntity) {
    return mapper.entityToResponseDto(currencyEntity);
  }

  @Override
  public CurrencyEntity requestDtoToEntity(CurrencyRequestDto currencyRequestDto) {
    return mapper.requestDtoToEntity(currencyRequestDto);
  }

  @Override
  public ExchangeResponseDto entityToResponseDto(ExchangeRateEntity exchangeRateEntity) {
    return mapper.entityToResponseDto(exchangeRateEntity);
  }
}
