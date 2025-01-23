package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CurrencyResponseDto;
import exception.DataBaseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrenciesService;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
  private final CurrenciesService currenciesService = CurrenciesService.getInstance();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      List<CurrencyResponseDto> currencies = currenciesService.findAll();
      resp.setContentType("application/json");
      resp.setCharacterEncoding("UTF-8");
      objectMapper.writeValue(resp.getWriter(), currencies);
    } catch (DataBaseException exception) {
      resp.setStatus(500);
      objectMapper.writeValue(resp.getWriter(), exception.getMessage());
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    super.doPost(req, resp);
  }
}
