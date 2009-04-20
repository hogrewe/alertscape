/**
 * 
 */
package com.alertscape.pump.onramp;

import java.util.Arrays;

import junit.framework.TestCase;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class MultiClearCriteriaTest extends TestCase {
  MultiClearCriteria crit = new MultiClearCriteria();

  protected void setUp() throws Exception {
    crit.setClearCriteriaField("type");
    crit.setClearCriteriaValue("okay");
    crit.setToClearFields(Arrays.asList("item"));
  }

  /**
   * Test method for
   * {@link com.alertscape.pump.onramp.MultiClearCriteria#shouldClear(com.alertscape.common.model.Alert)}.
   */
  public void testShouldClear() {
    Alert a = new Alert();
    a.setType("okay");
    a.setItem("foo");

    assertTrue("Should have been true for shouldClear", crit.shouldClear(a));

    a.setType("bar");

    assertFalse("Should have been false for shouldClear", crit.shouldClear(a));
  }

}
