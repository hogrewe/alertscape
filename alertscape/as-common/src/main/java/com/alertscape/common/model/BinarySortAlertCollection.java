/*
 * Created on Oct 17, 2006
 */
package com.alertscape.common.model;

import java.util.Collections;
import java.util.Comparator;


/**
 * @author josh
 * @version $Version: $
 */
public class BinarySortAlertCollection extends AbstractAlertCollection
{
  private static final Comparator<Alert> comparator = new AlertComparator( );

  @Override
  protected void processSingleAlert(Alert alert)
  {
    int index = indexOf(alert);
    if (alert.isStanding( ))
    {
      // It's a new event
      if (index < 0)
      {
        System.out.print("N");
        alerts.add(-index - 1, alert);
      }
      else
      {
        System.out.print("U");
        alerts.set(index, alert);
      }
    }
    else
    {
      if (index < 0)
      {
        System.out.println("Doh, index is " + index + " on a delete");
      }
      else
      {
        System.out.print("C");
        alerts.remove(index);
      }
    }
  }

  @Override
  public Alert getAlert(long id)
  {
    Alert temp = new Alert( );
    temp.setAlertId(id);
    int index = indexOf(temp);
    if (index >= 0)
    {
      return alerts.get(index);
    }
    else
    {
      return null;
    }
  }

  protected int indexOf(Alert alert)
  {
    return Collections.binarySearch(alerts, alert, comparator);
  }

  private static class AlertComparator implements Comparator<Alert>
  {
    public int compare(Alert o1, Alert o2)
    {
      long e1 = o1.getAlertId( );
      long e2 = o2.getAlertId( );
      return (e1 == e2 ? 0 : (e1 < e2) ? -1 : 1);
    }
  }
}
