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
public class NagiosAlertGenerator implements AlertGenerator {
  private static final String[] errorCodes = { "HTTP 404", "HTTP 500", "Connection Timed Out" };
  private static final String[] domains = { "www.example.com", "site1.example.com", "site2.example.com",
      "ws.example.com" };

  private Random rand = new Random();

  public Alert readAlert() {
    Alert a = new Alert();

    String type = errorCodes[rand.nextInt(errorCodes.length)];
    String item = domains[rand.nextInt(domains.length)];
    String itemType = "HTTPResponder";
    String manager = "Nagios@nagios.example.com";
    String managerType = "Nagios";
    
    a.setType(type);
    a.setItem(item);
    a.setItemType(itemType);
    a.setItemManager(manager);
    a.setItemManagerType(managerType);
    a.setLongDescription(type + " trying to access " + item);

    return a;
  }

}
