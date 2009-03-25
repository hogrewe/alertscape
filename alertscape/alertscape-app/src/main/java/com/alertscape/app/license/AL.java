/**
 * 
 */
package com.alertscape.app.license;

import java.io.Serializable;

/**
 * @author josh
 * 
 */
public class AL implements Serializable {
  private static final long serialVersionUID = 4L;

  private int on;
  private int of;
  private int au;

  /**
   * @return the onramps
   */
  public int getOn() {
    return on;
  }

  /**
   * @param onramps
   *          the onramps to set
   */
  public void setOn(int onramps) {
    this.on = onramps;
  }

  /**
   * @return the offramps
   */
  public int getOf() {
    return of;
  }

  /**
   * @param offramps
   *          the offramps to set
   */
  public void setOf(int offramps) {
    this.of = offramps;
  }

  /**
   * @return the ampUsers
   */
  public int getAu() {
    return au;
  }

  /**
   * @param ampUsers
   *          the ampUsers to set
   */
  public void setAu(int ampUsers) {
    this.au = ampUsers;
  }
}
