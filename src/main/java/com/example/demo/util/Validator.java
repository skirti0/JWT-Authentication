package com.example.demo.util;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

  private Validator() {
  }

  public static boolean validateEmail(String email) {
    if (StringUtils.isBlank(email)) {
      return false;
    }

    Pattern validEmailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    Matcher matcher = validEmailRegex.matcher(email);
    return matcher.find();
  }
}
