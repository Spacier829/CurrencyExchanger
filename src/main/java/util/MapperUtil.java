package util;

import dto.CurrencyRequestDto;
import dto.CurrencyResponseDto;
import dto.ExchangeRateResponseDto;
import entity.CurrencyEntity;
import entity.ExchangeRateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapperUtil {
  MapperUtil INSTANCE = Mappers.getMapper(MapperUtil.class);

  @Mapping(source = "fullName", target = "name")
  CurrencyResponseDto entityToDto(CurrencyEntity currencyEntity);

  @Mapping(target = "id", constant = "0L")
  CurrencyEntity dtoToEntity(CurrencyRequestDto currencyRequestDto);

  ExchangeRateResponseDto entityToDto(ExchangeRateEntity exchangeRateEntity);
}
