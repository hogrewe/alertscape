/**
 * 
 */
package com.alertscape.pump.onramp;

/**
 * @author josh
 * 
 */
public interface AlertCacheMBean {
  float getHitRatio();

  int getSize();

  void clear();
}
