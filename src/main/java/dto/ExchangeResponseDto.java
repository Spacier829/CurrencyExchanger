package dto;

import java.math.BigDecimal;

public class ExchangeResponseDto {
  private Long id;
  private CurrencyResponseDto baseCurrency;
  private CurrencyResponseDto targetCurrency;
  private BigDecimal rate;

  public ExchangeResponseDto() {
  }

  public ExchangeResponseDto(Long id, CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency,
                             BigDecimal rate) {
    this.id = id;
    this.baseCurrency = baseCurrency;
    this.targetCurrency = targetCurrency;
    this.rate = rate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CurrencyResponseDto getBaseCurrency() {
    return baseCurrency;
  }

  public void setBaseCurrency(CurrencyResponseDto baseCurrency) {
    this.baseCurrency = baseCurrency;
  }

  public CurrencyResponseDto getTargetCurrency() {
    return targetCurrency;
  }

  public void setTargetCurrency(CurrencyResponseDto targetCurrency) {
    this.targetCurrency = targetCurrency;
  }

  public BigDecimal getRate() {
    return rate;
  }

  public void setRate(BigDecimal rate) {
    this.rate = rate;
  }
}

