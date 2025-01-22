package util;

import dto.CurrencyResponseDto;
import entity.CurrencyEntity;

public class Mapper {
  public static CurrencyResponseDto toResponseDto(CurrencyEntity currency) {
    return new CurrencyResponseDto(
        currency.getId(),
        currency.getFullName(),
        currency.getCode(),
        currency.getSign());
  }
}