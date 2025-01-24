package filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ErrorDto;
import exception.DataBaseException;
import exception.ConflictException;
import exception.InvalidParameterException;
import exception.NotFoundException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlerFilter extends HttpFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException {
    try {
      super.doFilter(req, res, chain);
    } catch (DataBaseException dataBaseException) {
      writeExceptionResponse(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, dataBaseException);
    } catch (InvalidParameterException invalidParameterException) {
      writeExceptionResponse(res, HttpServletResponse.SC_BAD_REQUEST, invalidParameterException);
    } catch (NotFoundException notFoundException) {
      writeExceptionResponse(res, HttpServletResponse.SC_NOT_FOUND, notFoundException);
    } catch (ConflictException conflictException) {
      writeExceptionResponse(res, HttpServletResponse.SC_CONFLICT, conflictException);
    } catch (Exception exception) {
      writeExceptionResponse(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception);
    }
  }

  private void writeExceptionResponse(HttpServletResponse resp, int responseCode, Exception exception) throws
      IOException {
    resp.setStatus(responseCode);
    objectMapper.writeValue(resp.getWriter(), new ErrorDto(exception.getMessage()));
  }
}
