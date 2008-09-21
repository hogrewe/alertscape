/**
 * 
 */
package com.alertscape.tester;

import java.util.Random;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class IPAlertGenerator implements AlertGenerator {
  private static final String[] types = { "ThresholdCrossing", "InterfaceDown" };
  private static final String[] cities = { "DENCO", "LAXCA", "CHIIL", "NYCNY", "SEAWA", "MIAFL" };
  private static final String[] devices = { "C2511", "C1841", "C7600", "BN800", "CRS-1" };

  private Random rand = new Random();

  public Alert readAlert() {
    Alert a = new Alert();
    
    String type = types[rand.nextInt(types.length)];
    String city = cities[rand.nextInt(cities.length)];
    String device = devices[rand.nextInt(devices.length)];
    String item = city + device + "00" + (rand.nextInt(9) + 1);
    
    a.setItem(item);
    a.setItemType(device);
    a.setLongDescription(type + " on " + item);
    a.setType(type);

    return a;
  }

}
