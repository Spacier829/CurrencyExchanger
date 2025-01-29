package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ExchangeRateRequestDto;
import dto.ExchangeRateResponseDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRatesService;
import util.ValidationUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
  private final ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    List<ExchangeRateResponseDto> exchangeRates = exchangeRatesService.findAll();
    resp.setStatus(HttpServletResponse.SC_OK);
    objectMapper.writeValue(resp.getWriter(), exchangeRates);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String baseCurrencyCode = req.getParameter("baseCurrencyCode").toUpperCase();
    String targetCurrencyCode = req.getParameter("targetCurrencyCode").toUpperCase();
    BigDecimal rate = objectMapper.readValue(req.getParameter("rate"), BigDecimal.class);

    ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCurrencyCode,
        targetCurrencyCode,
        rate);
    ValidationUtil.validateExchangeRateRequest(exchangeRateRequestDto);
    ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.add(exchangeRateRequestDto);
    resp.setStatus(HttpServletResponse.SC_CREATED);
    objectMapper.writeValue(resp.getWriter(), exchangeRateResponseDto);
  }
}
