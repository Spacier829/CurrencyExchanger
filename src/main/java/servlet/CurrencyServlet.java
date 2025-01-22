package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDaoImpl;
import dto.CurrencyResponseDto;
import exception.DaoException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrenciesService;
import util.InvalidParameterException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
  private final CurrenciesService currenciesService = CurrenciesService.getInstance();
  private final CurrencyDaoImpl currencyDao = CurrencyDaoImpl.getInstance();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    try {
      String pathInfo = req.getPathInfo();
      if (!pathInfo.matches("^/[a-z]{3}$")) {
        throw new InvalidParameterException("Invalid code");
      }
      String code = pathInfo.substring(1).toUpperCase();
      Optional<CurrencyResponseDto> currency = currenciesService.findByCode(code);
      if (currency.isPresent()) {
        objectMapper.writeValue(resp.getWriter(), currency.get());
      } else {
        throw new DaoException("Currency not found");
      }
    } catch (DaoException exception) {
      objectMapper.writeValue(resp.getWriter(), exception.getMessage());
    }
  }
}
