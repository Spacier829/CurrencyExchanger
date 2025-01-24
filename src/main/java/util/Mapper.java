package util;

import dto.CurrencyRequestDto;
import dto.CurrencyResponseDto;
import dto.ErrorDto;
import dto.ExchangeRateResponseDto;
import entity.CurrencyEntity;
import entity.ExchangeRateEntity;

public class Mapper {
  public static CurrencyResponseDto currencyToResponseDto(CurrencyEntity currency) {
    return new CurrencyResponseDto(
        currency.getId(),
        currency.getFullName(),
        currency.getCode(),
        currency.getSign());
  }

  public static CurrencyEntity dtoToCurrency(CurrencyRequestDto currencyRequestDto) {
    return new CurrencyEntity(
        0L,
        currencyRequestDto.getCode(),
        currencyRequestDto.getName(),
        currencyRequestDto.getSign());
  }

  public static ErrorDto errorDto(String message) {
    return new ErrorDto(message);
  }

  public static ExchangeRateResponseDto exchangeRateToResponseDto(ExchangeRateEntity exchangeRate) {
    return new ExchangeRateResponseDto(
        exchangeRate.getId(),
        currencyToResponseDto(exchangeRate.getBaseCurrency()),
        currencyToResponseDto(exchangeRate.getTargetCurrency()),
        exchangeRate.getRate());
  }
}