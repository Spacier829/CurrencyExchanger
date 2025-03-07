package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ExchangeRequestDto;
import dto.ExchangeResponseDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeService;
import util.ValidationUtil;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
  private final ExchangeService exchangeService = ExchangeService.getInstance();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String baseCode = req.getParameter("from").toUpperCase();
    String targetCode = req.getParameter("to").toUpperCase();
    BigDecimal amount = new BigDecimal(req.getParameter("amount"));

    ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(baseCode, targetCode, amount);
    ValidationUtil.validateExchange(exchangeRequestDto);
    ExchangeResponseDto exchangeResponseDto = exchangeService.getConvertedAmount(exchangeRequestDto);
    resp.setStatus(HttpServletResponse.SC_OK);
    objectMapper.writeValue(resp.getWriter(), exchangeResponseDto);
  }
}
