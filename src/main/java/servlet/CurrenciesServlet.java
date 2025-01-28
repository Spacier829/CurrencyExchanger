package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CurrencyRequestDto;
import dto.CurrencyResponseDto;
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
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    List<CurrencyResponseDto> currencies = currenciesService.findAll();
    objectMapper.writeValue(resp.getWriter(), currencies);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String name = req.getParameter("name");
    String code = req.getParameter("code");
    String sign = req.getParameter("sign");

// Добавить валидатор полей

    CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(name, code, sign);
    CurrencyResponseDto currencyResponseDto = currenciesService.add(currencyRequestDto);
    resp.setStatus(HttpServletResponse.SC_CREATED);
    objectMapper.writeValue(resp.getWriter(), currencyResponseDto);
  }
}
