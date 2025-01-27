package service;

import dao.ExchangeRateDaoImpl;
import dto.CurrencyResponseDto;
import dto.ExchangeRateResponseDto;
import dto.ExchangeRequestDto;
import dto.ExchangeResponseDto;

import java.math.BigDecimal;
import java.util.Optional;

import static java.math.RoundingMode.HALF_DOWN;

public class ExchangeService {
  private static final ExchangeService INSTANCE = new ExchangeService();
  private final ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();
  private final CurrenciesService currenciesService = CurrenciesService.getInstance();

  private ExchangeService() {
  }

  public static ExchangeService getInstance() {
    return INSTANCE;
  }

  public ExchangeResponseDto calculate(ExchangeRequestDto exchangeRequestDto) {
    ExchangeResponseDto exchangeResponseDto = new ExchangeResponseDto();
    Optional<CurrencyResponseDto> baseCurrency = currenciesService.findByCode(exchangeRequestDto.getBaseCurrencyCode());
    Optional<CurrencyResponseDto> targetCurrency =
        currenciesService.findByCode(exchangeRequestDto.getTargetCurrencyCode());
    if (baseCurrency.isPresent() && targetCurrency.isPresent()) {
      exchangeResponseDto.setBaseCurrency(baseCurrency.get());
      exchangeResponseDto.setTargetCurrency(targetCurrency.get());
    }
    BigDecimal rate = getRate(exchangeRequestDto.getBaseCurrencyCode(), exchangeRequestDto.getTargetCurrencyCode());
    exchangeResponseDto.setRate(rate);
    exchangeResponseDto.setAmount(exchangeRequestDto.getAmount());
    exchangeResponseDto.setConvertedAmount(exchangeRequestDto.getAmount().multiply(rate));
    return exchangeResponseDto;
  }

  private BigDecimal getRate(String baseCurrency, String targetCurrency) {
    Optional<ExchangeRateResponseDto> exchangeRateResponseDto = exchangeRatesService.findByCodes(baseCurrency,
        targetCurrency);
    if (exchangeRateResponseDto.isPresent()) {
      return exchangeRateResponseDto.get().getRate();
    }
    exchangeRateResponseDto = exchangeRatesService.findByCodes(targetCurrency, baseCurrency);
    if (exchangeRateResponseDto.isPresent()) {
      return new BigDecimal(1).divide(exchangeRateResponseDto.get().getRate(), 6, HALF_DOWN);
    }
    exchangeRateResponseDto = exchangeRatesService.findByCodes("USD", baseCurrency);
    if (exchangeRateResponseDto.isPresent()) {
      Optional<ExchangeRateResponseDto> exchangeRateResponseDto2 = exchangeRatesService.findByCodes("USD",
          targetCurrency);
      if (exchangeRateResponseDto2.isPresent()) {
        return
            exchangeRateResponseDto2.get().getRate().divide(exchangeRateResponseDto.get().getRate(), 6, HALF_DOWN);
      }
    }
    return null;
  }
}
