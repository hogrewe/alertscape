/**
 * 
 */
package com.alertscape.pump.onramp.equator;

import com.alertscape.common.model.Alert;

final class MajorTagEquator extends AbstractAlertPropertyEquator {
  private String majorTagName;

  public MajorTagEquator(String majorTagName) {
    this.majorTagName = majorTagName;
  }

  @Override
  protected Object getValue(Alert a) {
    return a.getMajorTag(majorTagName);
  }

  @Override
  public String toString() { 
    return "MajorTag:" + majorTagName;
  }
  

}