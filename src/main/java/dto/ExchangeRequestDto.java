package dto;

import java.math.BigDecimal;

public class ExchangeRequestDto {
  String baseCurrencyCode;
  String targetCurrencyCode;
  BigDecimal amount;

  public ExchangeRequestDto() {
  }

  public ExchangeRequestDto(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
    this.baseCurrencyCode = baseCurrencyCode;
    this.targetCurrencyCode = targetCurrencyCode;
    this.amount = amount;
  }

  public String getBaseCurrencyCode() {
    return baseCurrencyCode;
  }

  public void setBaseCurrencyCode(String baseCurrencyCode) {
    this.baseCurrencyCode = baseCurrencyCode;
  }

  public String getTargetCurrencyCode() {
    return targetCurrencyCode;
  }

  public void setTargetCurrencyCode(String targetCurrencyCode) {
    this.targetCurrencyCode = targetCurrencyCode;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}
