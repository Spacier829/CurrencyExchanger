package util;

import dto.CurrencyRequestDto;
import dto.CurrencyResponseDto;
import dto.ExchangeRateResponseDto;
import entity.CurrencyEntity;
import entity.ExchangeRateEntity;

public class oldMapper {
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

  public static ExchangeRateResponseDto exchangeRateToResponseDto(ExchangeRateEntity exchangeRate) {
    return new ExchangeRateResponseDto(
        exchangeRate.getId(),
        currencyToResponseDto(exchangeRate.getBaseCurrency()),
        currencyToResponseDto(exchangeRate.getTargetCurrency()),
        exchangeRate.getRate());
  }
}