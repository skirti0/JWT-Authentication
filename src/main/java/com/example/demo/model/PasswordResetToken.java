package com.example.demo.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String token;

  @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  @Column(nullable = false)
  private Date expiryDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  public void setExpiryDate(int minutes){
    Calendar now = Calendar.getInstance();
    now.add(Calendar.MINUTE, minutes);
    this.expiryDate = now.getTime();
  }

  public boolean isExpired() {
    return new Date().after(this.expiryDate);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PasswordResetToken that = (PasswordResetToken) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(token, that.token) &&
        Objects.equals(user, that.user) &&
        Objects.equals(expiryDate, that.expiryDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, token, user, expiryDate);
  }
}
