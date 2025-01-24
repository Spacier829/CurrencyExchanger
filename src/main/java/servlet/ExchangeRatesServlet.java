package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ExchangeRateResponseDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRatesService;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
  private final ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    List<ExchangeRateResponseDto> exchangeRates = exchangeRatesService.findAll();
    objectMapper.writeValue(resp.getWriter(), exchangeRates);
  }
}
