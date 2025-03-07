package dto;

public class CurrencyResponseDto {
  private Long id;
  private String name;
  private String code;
  private String sign;

  public CurrencyResponseDto() {
  }

  public CurrencyResponseDto(Long id, String name, String code, String sign) {
    this.id = id;
    this.name = name;
    this.code = code;
    this.sign = sign;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }
}
