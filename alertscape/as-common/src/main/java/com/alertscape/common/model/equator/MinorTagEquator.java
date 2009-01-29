/**
 * 
 */
package com.alertscape.common.model.equator;

import com.alertscape.common.model.Alert;

final class MinorTagEquator extends AbstractAlertPropertyEquator {
  private static final long serialVersionUID = 7011369706232458318L;
  private String minorTagName;

  public MinorTagEquator(String minorTagName) {
    this.minorTagName = minorTagName;
  }

  @Override
  protected Object getValue(Alert a) {
    return a.getMinorTag(minorTagName);
  }

  @Override
  public String toString() {
    return "MinorTag:" + minorTagName;
  }

}