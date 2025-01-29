package service;

import dto.ExchangeRateResponseDto;
import dto.ExchangeRequestDto;
import dto.ExchangeResponseDto;
import exception.NotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeService {
  private static final String USD_CODE = "USD";
  private static final ExchangeService INSTANCE = new ExchangeService();
  private final ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();
  private final CurrenciesService currenciesService = CurrenciesService.getInstance();

  private ExchangeService() {
  }

  public static ExchangeService getInstance() {
    return INSTANCE;
  }

  public ExchangeResponseDto getResult(ExchangeRequestDto exchangeRequestDto) {
    Optional<ExchangeRateResponseDto> exchangeRateResponseDto = getExchangeRate(exchangeRequestDto);
    if (exchangeRateResponseDto.isEmpty()) {
      exchangeRateResponseDto = getReverseExchangeRate(exchangeRequestDto);
    }
    if (exchangeRateResponseDto.isEmpty()) {
      exchangeRateResponseDto = getCrossExchangeRate(exchangeRequestDto);
    }
    if (exchangeRateResponseDto.isEmpty()) {
      throw new NotFoundException("No exchange rate found");
    }
    return buildExchangeResponseDto(exchangeRateResponseDto.get(), exchangeRequestDto.getAmount());
  }

  private Optional<ExchangeRateResponseDto> getExchangeRate(ExchangeRequestDto exchangeRequestDto) {
    return exchangeRatesService.findByCodes(exchangeRequestDto.getBaseCurrencyCode(),
        exchangeRequestDto.getTargetCurrencyCode());
  }

  private Optional<ExchangeRateResponseDto> getReverseExchangeRate(ExchangeRequestDto exchangeRequestDto) {
    Optional<ExchangeRateResponseDto> reverseExchangeRateResponseDto = exchangeRatesService.findByCodes(
        exchangeRequestDto.getTargetCurrencyCode(),
        exchangeRequestDto.getBaseCurrencyCode());
    if (reverseExchangeRateResponseDto.isEmpty()) {
      return Optional.empty();
    } else {
      reverseExchangeRateResponseDto.get().setRate(
          new BigDecimal(1).divide(reverseExchangeRateResponseDto.get().getRate(), 2, RoundingMode.HALF_DOWN));
      return reverseExchangeRateResponseDto;
    }
  }

  private Optional<ExchangeRateResponseDto> getCrossExchangeRate(ExchangeRequestDto exchangeRequestDto) {
    Optional<ExchangeRateResponseDto> usdToBaseCurrency = exchangeRatesService.findByCodes(USD_CODE,
        exchangeRequestDto.getBaseCurrencyCode());
    Optional<ExchangeRateResponseDto> usdToTargetCurrency = exchangeRatesService.findByCodes(USD_CODE,
        exchangeRequestDto.getTargetCurrencyCode());
    if (usdToBaseCurrency.isEmpty() || usdToTargetCurrency.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(buildCrossRate(usdToBaseCurrency.get(), usdToTargetCurrency.get()));
  }


  private BigDecimal calculateAmount(BigDecimal rate, BigDecimal amount) {
    return rate.multiply(amount).setScale(2, RoundingMode.HALF_DOWN);
  }

  private BigDecimal calculateCrossRate(BigDecimal baseRate, BigDecimal targetRate) {
    return baseRate.divide(targetRate, 2, RoundingMode.HALF_DOWN);
  }

  private ExchangeResponseDto buildExchangeResponseDto(ExchangeRateResponseDto exchangeRateResponseDto,
                                                       BigDecimal amount) {
    ExchangeResponseDto exchangeResponseDto = new ExchangeResponseDto();
    exchangeResponseDto.setBaseCurrency(exchangeRateResponseDto.getBaseCurrency());
    exchangeResponseDto.setTargetCurrency(exchangeRateResponseDto.getTargetCurrency());
    exchangeResponseDto.setRate(exchangeRateResponseDto.getRate());
    exchangeResponseDto.setAmount(amount);
    exchangeResponseDto.setConvertedAmount(calculateAmount(amount, exchangeRateResponseDto.getRate()));
    return exchangeResponseDto;
  }

  private ExchangeRateResponseDto buildCrossRate(ExchangeRateResponseDto baseExchangeRate,
                                                 ExchangeRateResponseDto targetExchangeRate) {
    return new ExchangeRateResponseDto(
        0L,
        baseExchangeRate.getTargetCurrency(),
        targetExchangeRate.getTargetCurrency(),
        calculateCrossRate(baseExchangeRate.getRate(), targetExchangeRate.getRate()));
  }
}
