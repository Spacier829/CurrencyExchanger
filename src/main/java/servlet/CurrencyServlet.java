package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CurrencyResponseDto;
import dto.ErrorDto;
import exception.DataBaseException;
import exception.NotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrenciesService;
import exception.InvalidParameterException;
import util.Mapper;

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
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      throw new InvalidParameterException("Code is empty");
    } else {
      if (!pathInfo.get().matches("^/[a-zA-Z]{3}$")) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        throw new InvalidParameterException("Invalid code");
      }
      String code = pathInfo.get().substring(1);
      Optional<CurrencyResponseDto> currencies = currenciesService.findByCode(code);
      if (currencies.isPresent()) {
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), currencies.get());
      } else {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        throw new NotFoundException("Currency not found");
      }
    }
  }
}
