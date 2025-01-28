package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CurrencyResponseDto;
import exception.InvalidParameterException;
import exception.NotFoundException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrenciesService;
import util.ValidationUtil;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
  private final CurrenciesService currenciesService = CurrenciesService.getInstance();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Optional<String> pathInfo = Optional.ofNullable(req.getPathInfo());
    if (pathInfo.isEmpty()) {
      throw new InvalidParameterException("Code is empty");
    }

    String code = pathInfo.get().substring(1).toUpperCase();
    ValidationUtil.validateCode(code);
    Optional<CurrencyResponseDto> currencies = currenciesService.findByCode(code);
    if (currencies.isEmpty()) {
      throw new NotFoundException("Currency not found");
    }
    resp.setStatus(HttpServletResponse.SC_OK);
    objectMapper.writeValue(resp.getWriter(), currencies.get());
  }
}
