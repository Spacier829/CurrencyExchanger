package util;

import dto.CurrencyRequestDto;
import exception.InvalidParameterException;

public class ValidationUtil {


  public static void validateCurrencyRequest(CurrencyRequestDto currencyRequestDto) {
    validateParameter(currencyRequestDto.getName(), "Name");
    validateParameter(currencyRequestDto.getCode(), "Code");
    validateCode(currencyRequestDto.getCode());
    validateParameter(currencyRequestDto.getSign(), "Sign");
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
}
