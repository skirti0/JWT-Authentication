package com.example.demo.dto;

import java.util.Date;

public class AuthenticationResponse {
  private String jwtToken;
  private String token;
  private Date expirationTime;


  public AuthenticationResponse() {
  }
  public AuthenticationResponse(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public AuthenticationResponse(String jwtToken, String token, Date expirationTime) {
    this.jwtToken = jwtToken;
    this.token = token;
    this.expirationTime = expirationTime;
  }

  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Date getExpirationTime() {
    return expirationTime;
  }

  public void setExpirationTime(Date expirationTime) {
    this.expirationTime = expirationTime;
  }
}
