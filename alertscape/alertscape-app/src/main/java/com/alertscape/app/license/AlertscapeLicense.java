/**
 * 
 */
package com.alertscape.app.license;

import java.io.Serializable;

/**
 * @author josh
 * 
 */
public class AlertscapeLicense implements Serializable {
  private static final long serialVersionUID = 1L;

  private int onramps;
  private int offramps;
  private int ampUsers;

  /**
   * @return the onramps
   */
  public int getOnramps() {
    return onramps;
  }

  /**
   * @param onramps
   *          the onramps to set
   */
  public void setOnramps(int onramps) {
    this.onramps = onramps;
  }

  /**
   * @return the offramps
   */
  public int getOfframps() {
    return offramps;
  }

  /**
   * @param offramps
   *          the offramps to set
   */
  public void setOfframps(int offramps) {
    this.offramps = offramps;
  }

  /**
   * @return the ampUsers
   */
  public int getAmpUsers() {
    return ampUsers;
  }

  /**
   * @param ampUsers
   *          the ampUsers to set
   */
  public void setAmpUsers(int ampUsers) {
    this.ampUsers = ampUsers;
  }
}
