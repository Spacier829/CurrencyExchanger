package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CurrencyRequestDto;
import dto.CurrencyResponseDto;
import entity.CurrencyEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrenciesService;
import util.Mapper;

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

    CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(name, code, sign);
    CurrencyEntity currency = currenciesService.add(Mapper.dtoToCurrencyEntity(currencyRequestDto));
    CurrencyResponseDto currencyResponseDto = Mapper.currencyToResponseDto(currency);
    objectMapper.writeValue(resp.getWriter(), currencyResponseDto);
  }
}
