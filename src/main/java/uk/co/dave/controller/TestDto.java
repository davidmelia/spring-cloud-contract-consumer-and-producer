package uk.co.dave.controller;

public class TestDto {
  private String status;

  public TestDto(String status) {
    this.status = status;
  }

  public TestDto() {}

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}
