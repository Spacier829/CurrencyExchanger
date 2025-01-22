package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDaoImpl;
import dto.CurrencyResponseDto;
import entity.CurrencyEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesServlet extends HttpServlet {

  private final CurrencyDaoImpl currencyDao = CurrencyDaoImpl.getInstance();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    List<CurrencyEntity> currencies = currencyDao.findAll();
    List<CurrencyResponseDto> currenciesResponseDtos = new ArrayList<>();
    for (CurrencyEntity currency : currencies) {
      currenciesResponseDtos.add(objectMapper.convertValue(currency, CurrencyResponseDto.class));
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    super.doPost(req, resp);
  }
}
