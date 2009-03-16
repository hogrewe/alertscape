/**
 * 
 */
package com.alertscape.pump.onramp.file;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class SimpleRegexLineProcessorTest extends TestCase {
  private SimpleRegexLineProcessor processor;

  protected void setUp() throws Exception {
    processor = new SimpleRegexLineProcessor();
    Map<String, String> fieldMappings = new HashMap<String, String>();
    fieldMappings.put("item", "$1");
    fieldMappings.put("itemManager", "ItemManager");
    fieldMappings.put("type", "$2");
    processor.setFieldMappings(fieldMappings);
    Map<String, String> categoryMappings = new HashMap<String, String>();
    categoryMappings.put("customer", "$3 ~~ Test");
    categoryMappings.put("region", "$4");
    processor.setCategoryMappings(categoryMappings);
    // processor.setSeverityDeterminedField("type");
    //    
    // Map<Integer, List<String>> severityMappings = new HashMap<Integer, List<String>>();
    // List<String> sevTypes = new ArrayList<String>();
    // sevTypes.add("NormalType");
    // severityMappings.put(0, sevTypes);
    // sevTypes = new ArrayList<String>();
    // sevTypes.add("WarningType");
    // sevTypes.add("WarningType2");
    // sevTypes.add("WarningType3");
    // severityMappings.put(1, sevTypes);
    // sevTypes = new ArrayList<String>();
    // sevTypes.add("CriticalType");
    // sevTypes.add("CriticalType2");
    // sevTypes.add("CriticalType3");
    // severityMappings.put(4, sevTypes);
    // processor.setSeverityMappings(severityMappings );
    //    
    // processor.init();
  }

  /**
   * Test method for
   * {@link com.alertscape.pump.onramp.file.SimpleRegexLineProcessor#createAlert(java.util.regex.Matcher)}.
   */
  public void testCreateAlert() {
    processor.setRegex("(\\w*) (\\w*) (\\w*) (.*)");

    Alert alert = processor.createAlert("ItemTest ItemType1 CustomerTest Some more stuff at the end region");
    System.out.println("Alert: " + alert);

    alert = processor.createAlert("ItemTest WarningType2 CustomerTest Some more stuff at the end region");
    System.out.println("Alert: " + alert);

    alert = processor.createAlert("ItemTest NormalType CustomerTest Some more stuff at the end region");
    System.out.println("Alert: " + alert);

    alert = processor.createAlert("ItemTest CriticalType CustomerTest Some more stuff at the end region");
    System.out.println("Alert: " + alert);

    alert = processor.createAlert("ItemTest CriticalType3 CustomerTest Some more stuff at the end region");
    System.out.println("Alert: " + alert);
  }

}
