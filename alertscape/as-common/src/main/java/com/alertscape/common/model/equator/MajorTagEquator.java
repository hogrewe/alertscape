/**
 * 
 */
package com.alertscape.common.model.equator;

import com.alertscape.common.model.Alert;

final class MajorTagEquator extends AbstractAlertPropertyEquator {
  private static final long serialVersionUID = -6574801858968644791L;
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