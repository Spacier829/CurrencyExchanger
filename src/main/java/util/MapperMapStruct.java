package util;

import dto.CurrencyRequestDto;
import dto.CurrencyResponseDto;
import dto.ExchangeResponseDto;
import entity.CurrencyEntity;
import entity.ExchangeRateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapperMapStruct {
  MapperMapStruct INSTANCE = Mappers.getMapper(MapperMapStruct.class);

  @Mapping(source = "fullName", target = "name")
  CurrencyResponseDto entityToResponseDto(CurrencyEntity currencyEntity);

  @Mapping(target = "id", constant = "0L")
  CurrencyEntity requestDtoToEntity(CurrencyRequestDto currencyRequestDto);

  ExchangeResponseDto entityToResponseDto(ExchangeRateEntity exchangeRateEntity);
}
