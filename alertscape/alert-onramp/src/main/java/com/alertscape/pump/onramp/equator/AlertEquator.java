/**
 * 
 */
package com.alertscape.pump.onramp.equator;

import java.util.ArrayList;
import java.util.List;

import com.alertscape.AlertscapeException;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class AlertEquator {
  private static final ASLogger LOG = ASLogger.getLogger(AlertEquator.class);

  private List<AlertPropertyEquator> equators = new ArrayList<AlertPropertyEquator>();

  public AlertEquator() {

  }

  public AlertEquator(List<String> attributes, List<String> majorTags, List<String> minorTags)
      throws AlertscapeException {
    setAttributes(attributes);
    for (String majorTag : majorTags) {
      MajorTagEquator eq = new MajorTagEquator(majorTag);
      equators.add(eq);
    }
    for (String minorTag : minorTags) {
      MinorTagEquator eq = new MinorTagEquator(minorTag);
      equators.add(eq);
    }
  }

  public void setAttributes(List<String> attributes) throws AlertscapeException {
    if (attributes == null) {
      return;
    }
    for (String attr : attributes) {
      AttributeEquator eq = new AttributeEquator(attr);
      equators.add(eq);
      LOG.info("Setting new attribute equator: " + eq);
    }
  }

  public boolean equal(Alert a1, Alert a2) {
    if (a1 == a2) {
      return true;
    }
    if (equators == null || equators.isEmpty()) {
      return false;
    }
    for (AlertPropertyEquator eq : equators) {
      if (!eq.equal(a1, a2)) {
        return false;
      }
    }
    return true;
  }

  public int hashAlert(Alert a) {
    if (equators == null || equators.isEmpty()) {
      return a.hashCode();
    }

    final int prime = 31;
    int result = 1;
    for (AlertPropertyEquator eq : equators) {
      Object val = eq.getHashableValue(a);
      result = prime * result + ((val == null) ? 0 : val.hashCode());
    }
    return result;
  }

  @Override
  public String toString() {
    return "AlertEquator:" + equators;
  }
}
