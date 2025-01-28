package util;

import dto.CurrencyRequestDto;
import dto.ExchangeRateRequestDto;
import exception.InvalidParameterException;

import java.math.BigDecimal;

public class ValidationUtil {


  public static void validateCurrencyRequest(CurrencyRequestDto currencyRequestDto) {
    validateParameter(currencyRequestDto.getName(), "Name");
    validateCode(currencyRequestDto.getCode());
    validateParameter(currencyRequestDto.getSign(), "Sign");
  }

  public static void validateExchangeRateRequest(ExchangeRateRequestDto exchangeRateRequestDto) {
    validateCode(exchangeRateRequestDto.getBaseCurrencyCode());
    validateCode(exchangeRateRequestDto.getTargetCurrencyCode());
    validateRate(exchangeRateRequestDto.getRate());
  }

  public static void validateCode(String code) {
    if (!code.matches("^[a-zA-Z]{3}$")) {
      throw new InvalidParameterException("Invalid code. Code must contains 3 letters.");
    }
  }

  private static void validateParameter(String parameter, String parameterName) {
    if (parameter.isEmpty() || parameter.isBlank()) {
      throw new InvalidParameterException(parameterName + " cannot be empty");
    }
  }

  public static void validatePath(String path) {
    if (path.isEmpty() || path.isBlank()) {
      throw new InvalidParameterException("Codes are empty");
    }
    if (!path.matches("^/[a-zA-Z]{6}$")) {
      throw new InvalidParameterException("Invalid path. Codes must contain 6 letters.");
    }
  }

  public static void validateRate(BigDecimal rate) {
    if (rate.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidParameterException("Rate must be greater than zero");
    }
  }
}
