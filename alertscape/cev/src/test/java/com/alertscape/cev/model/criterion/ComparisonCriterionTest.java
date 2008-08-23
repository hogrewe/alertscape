/*
 * Created on Jul 7, 2006
 */
package com.alertscape.cev.model.criterion;

import static com.alertscape.cev.model.criterion.ComparisonCriterion.ComparisonType.EQUAL;
import static com.alertscape.cev.model.criterion.ComparisonCriterion.ComparisonType.GREATER_THAN;
import static com.alertscape.cev.model.criterion.ComparisonCriterion.ComparisonType.GREATER_THAN_OR_EQUAL;
import static com.alertscape.cev.model.criterion.ComparisonCriterion.ComparisonType.LESS_THAN;
import static com.alertscape.cev.model.criterion.ComparisonCriterion.ComparisonType.LESS_THAN_OR_EQUAL;
import static com.alertscape.cev.model.criterion.ComparisonCriterion.ComparisonType.NOT_EQUAL;
import static com.alertscape.common.model.Alert.AlertStatus.STANDING;
import junit.framework.TestCase;

import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.severity.SeverityFactory;

/**
 * @author josh
 * @version $Version: $
 */
public class ComparisonCriterionTest extends TestCase
{
  private Alert event;

  public ComparisonCriterionTest( )
  {
    super("ComparisonCriterionTest");
  }

  @Override
  protected void setUp( ) throws Exception
  {
    event = new Alert( );
    event.setCount(100);
    event.setAlertId(20);
    // event.setFirstOccurence(firstOccurence);
    event.setItem("Item1");
    event.setItemType("ItemType1");
    event.setItemManager("ItemManager1");
    event.setItemManagerType("ItemManagerType1");
    // event.setLastOccurence(lastOccurence);
    event.setLongDescription("Long Description");
    event.setSeverity(SeverityFactory.getInstance( ).getSeverity(1));
    event.setShortDescription("Short Description");
    event.setSource(new AlertSource(3, "AS 3"));
    event.setStatus(STANDING);
    event.setType("Type");
  }

  public void testLong( )
  {
    ComparisonCriterion cEqual = new ComparisonCriterion("alertId", "20", EQUAL);
    ComparisonCriterion cNotEqual = new ComparisonCriterion("alertId", "20",
        NOT_EQUAL);
    ComparisonCriterion cGreaterThan = new ComparisonCriterion("alertId", "20",
        GREATER_THAN);
    ComparisonCriterion cLessThan = new ComparisonCriterion("alertId", "20",
        LESS_THAN);
    ComparisonCriterion cGreaterThanEqual = new ComparisonCriterion("alertId",
        "20", GREATER_THAN_OR_EQUAL);
    ComparisonCriterion cLessThanEqual = new ComparisonCriterion("alertId",
        "20", LESS_THAN_OR_EQUAL);

    event.setAlertId(20);
    assertTrue("The event ID should be equal", cEqual.matches(event));
    assertFalse("The event ID should not be equal", cNotEqual.matches(event));
    assertFalse("The event ID should not be greater than", cGreaterThan
        .matches(event));
    assertFalse("The event ID should not be less than", cLessThan
        .matches(event));
    assertTrue("The event ID should be greater than or equal",
        cGreaterThanEqual.matches(event));
    assertTrue("The event ID should be less than or equal", cLessThanEqual
        .matches(event));

    event.setAlertId(19);
    assertFalse("The event ID should not be equal", cEqual.matches(event));
    assertTrue("The event ID should not be equal", cNotEqual.matches(event));
    assertFalse("The event ID should not be greater than", cGreaterThan
        .matches(event));
    assertTrue("The event ID should be less than", cLessThan.matches(event));
    assertFalse("The event ID should not be greater than or equal",
        cGreaterThanEqual.matches(event));
    assertTrue("The event ID should be less than or equal", cLessThanEqual
        .matches(event));

    event.setAlertId(21);
    assertFalse("The event ID should not be equal", cEqual.matches(event));
    assertTrue("The event ID should not be equal", cNotEqual.matches(event));
    assertTrue("The event ID should be greater than", cGreaterThan
        .matches(event));
    assertFalse("The event ID should not be less than", cLessThan
        .matches(event));
    assertTrue("The event ID should be greater than or equal",
        cGreaterThanEqual.matches(event));
    assertFalse("The event ID should not be less than or equal", cLessThanEqual
        .matches(event));
  }
}
