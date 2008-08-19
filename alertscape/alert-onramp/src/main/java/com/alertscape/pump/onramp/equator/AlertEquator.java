/**
 * 
 */
package com.alertscape.pump.onramp.equator;

import java.util.List;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class AlertEquator {

  private List<AlertPropertyEquator> equators;

  private AlertEquator(List<String> attributes, List<String> majorTags, List<String> minorTags) {
    for (String attr : attributes) {
      AttributeEquator eq = new AttributeEquator(attr);
      equators.add(eq);
    }
    
    for (String majorTag : majorTags) {
      MajorTagEquator eq = new MajorTagEquator(majorTag);
      equators.add(eq);
    }
    
    for (String minorTag : minorTags) {
      MinorTagEquator eq = new MinorTagEquator(minorTag);
      equators.add(eq);
    }
  }

  public boolean equal(Alert a1, Alert a2) {
    for (AlertPropertyEquator eq : equators) {
      if (!eq.equal(a1, a2)) {
        return false;
      }
    }
    return true;
  }

  public int hashAlert(Alert a) {
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
