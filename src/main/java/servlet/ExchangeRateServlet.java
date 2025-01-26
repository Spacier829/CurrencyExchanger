package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ExchangeRateRequestDto;
import dto.ExchangeRateResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrenciesService;
import service.ExchangeRatesService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
  private final ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String method = req.getMethod();
    if (!method.equals("PATCH")) {
      super.service(req, resp);
    }
    this.doPatch(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String pathInfo = req.getPathInfo().replaceFirst("/", "");
    String baseCurrencyCode = pathInfo.substring(0, 3).toUpperCase();
    String targetCurrencyCode = pathInfo.substring(3).toUpperCase();

//    Добавить валидатор полей

    Optional<ExchangeRateResponseDto> exchangeRateResponseDto = exchangeRatesService.findByCodes(baseCurrencyCode,
        targetCurrencyCode);
    if (exchangeRateResponseDto.isPresent()) {
      objectMapper.writeValue(resp.getWriter(), exchangeRateResponseDto.get());
    }
  }

  protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String pathInfo = req.getPathInfo().replaceFirst("/", "");
    String baseCurrencyCode = pathInfo.substring(0, 3).toUpperCase();
    String targetCurrencyCode = pathInfo.substring(3).toUpperCase();

    BigDecimal rate = objectMapper.readValue(req.getParameter("rate"), BigDecimal.class);

    ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCurrencyCode,
        targetCurrencyCode,
        rate);

    ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.update(exchangeRateRequestDto);
    resp.setStatus(HttpServletResponse.SC_OK);
    objectMapper.writeValue(resp.getWriter(), exchangeRateResponseDto);
  }
}
