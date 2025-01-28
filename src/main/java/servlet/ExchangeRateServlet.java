package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ExchangeRateRequestDto;
import dto.ExchangeRateResponseDto;
import exception.NotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRatesService;
import util.ValidationUtil;

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
    Optional<String> pathInfo = Optional.ofNullable(req.getPathInfo());
    ValidationUtil.validatePath(pathInfo.orElse(""));

    if (pathInfo.isPresent()) {
      String baseCurrencyCode = pathInfo.get().substring(1, 4).toUpperCase();
      ValidationUtil.validateCode(baseCurrencyCode);
      String targetCurrencyCode = pathInfo.get().substring(4).toUpperCase();
      ValidationUtil.validateCode(targetCurrencyCode);

      Optional<ExchangeRateResponseDto> exchangeRateResponseDto = exchangeRatesService.findByCodes(baseCurrencyCode,
          targetCurrencyCode);
      if (exchangeRateResponseDto.isEmpty()) {
        throw new NotFoundException("Exchange rate not found");
      }
      resp.setStatus(HttpServletResponse.SC_OK);
      objectMapper.writeValue(resp.getWriter(), exchangeRateResponseDto.get());
    }
  }

  protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Optional<String> pathInfo = Optional.ofNullable(req.getPathInfo());
    ValidationUtil.validatePath(pathInfo.orElse(""));

    if (pathInfo.isPresent()) {
      String baseCurrencyCode = pathInfo.get().substring(1, 4).toUpperCase();
      String targetCurrencyCode = pathInfo.get().substring(4).toUpperCase();

      BigDecimal rate = objectMapper.readValue(req.getParameter("rate"), BigDecimal.class);

      ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCurrencyCode,
          targetCurrencyCode,
          rate);
      ValidationUtil.validateExchangeRateRequest(exchangeRateRequestDto);
      ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.update(exchangeRateRequestDto);
      resp.setStatus(HttpServletResponse.SC_OK);
      objectMapper.writeValue(resp.getWriter(), exchangeRateResponseDto);
    }
  }
}
