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
public class RandomTransportAlertGenerator implements AlertGenerator {
  private static final String[] bandwidths = { "OC48", "OC12", "OC3", "T3", "T1", "DS3", "DS1" };
  private static final String[] cities = { "DENCO", "LAXCA", "CHIIL", "NYCNY", "SEAWA", "MIAFL" };
  private static final String[] devices = { "15454", "DMX", "OPT", "INF" };
  private static final String[] types = { "ThresholdCrossing", "LOS", "LOF" };

  private Random rand = new Random();

  public Alert readAlert() {
    Alert a = new Alert();

    String device = devices[rand.nextInt(devices.length)];
    String city = cities[rand.nextInt(cities.length)];
    String bandwidth = bandwidths[rand.nextInt(bandwidths.length)];
    String port = bandwidth + "-" + (rand.nextInt(3) + 1) + "-" + (rand.nextInt(3) + 1) + "-" + (rand.nextInt(24) + 1);
    String type = types[rand.nextInt(types.length)];

    String itemManager = city + device + "00" + (rand.nextInt(9) + 1);

    a.setType(type);
    a.setItem(itemManager + "-" + port);
    a.setItemType(device + "-" + bandwidth);
    a.setItemManager(itemManager);
    a.setItemManagerType(device);
    a.setLongDescription(itemManager + "-" + port + " " + type);

    return a;
  }
}
