/**
 * 
 */
package com.alertscape.pump.onramp.line;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author josh
 * 
 */
public abstract class AbstractRegexLineProcessor implements AlertLineProcessor {
  private String regex;
  private Pattern pattern;
  private boolean matchRequired = true;
  private String name;

  public boolean matches(String line) {
    Matcher matcher = makeMatcher(line);

    return matcher.matches();
  }

  protected Matcher makeMatcher(String line) {
    return pattern.matcher(line);
  }

  /**
   * @return the regex
   */
  public String getRegex() {
    return regex;
  }

  /**
   * @param regex
   *          the regex to set
   */
  public void setRegex(String regex) {
    if (regex == null) {
      this.regex = null;
      pattern = null;
    } else {
      this.regex = regex.trim();
      pattern = Pattern.compile(this.regex);
    }
  }

  /**
   * @param matchRequired
   *          the matchRequired to set
   */
  public void setMatchRequired(boolean matchRequired) {
    this.matchRequired = matchRequired;
  }

  /**
   * @return the matchRequired
   */
  public boolean isMatchRequired() {
    return matchRequired;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

}
