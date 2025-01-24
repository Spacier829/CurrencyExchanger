package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ExchangeRateResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrenciesService;
import service.ExchangeRatesService;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
  private final ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String pathInfo = req.getPathInfo().replaceFirst("/", "");
    String baseCurrencyCode = pathInfo.substring(0, 3).toUpperCase();
    String targetCurrencyCode = pathInfo.substring(3).toUpperCase();

    Optional<ExchangeRateResponseDto> exchangeRateResponseDto = exchangeRatesService.findByCodes(baseCurrencyCode,
        targetCurrencyCode);
    objectMapper.writeValue(resp.getWriter(), exchangeRateResponseDto.get());


    int a = 123;
  }
}
