package util;

import dto.CurrencyResponseDto;
import dto.ErrorDto;
import entity.CurrencyEntity;

public class Mapper {
  public static CurrencyResponseDto toResponseDto(CurrencyEntity currency) {
    return new CurrencyResponseDto(
        currency.getId(),
        currency.getFullName(),
        currency.getCode(),
        currency.getSign());
  }

  public static ErrorDto errorDto(String message) {
    return new ErrorDto(message);
  }
}