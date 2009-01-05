/*
 * Created on Oct 17, 2006
 */
package com.alertscape.common.model;

import java.util.Collections;
import java.util.Comparator;

import ca.odell.glazedlists.EventList;

import com.alertscape.common.logging.ASLogger;


/**
 * @author josh
 * @version $Version: $
 */
public class BinarySortAlertCollection extends AbstractAlertCollection
{
  private static final ASLogger LOG = ASLogger.getLogger(BinarySortAlertCollection.class);
  private static final Comparator<Alert> comparator = new AlertComparator( );

  public BinarySortAlertCollection(EventList<Alert> alertList) {
    super(alertList);
  }
  
  /**
   * 
   */
  public BinarySortAlertCollection() {
  }

  @Override
  protected void processSingleAlert(Alert alert)
  {
    int index = indexOf(alert);
    if (alert.isStanding( ))
    {
      // It's a new event
      if (index < 0)
      {
        alerts.add(-index - 1, alert);
      }
      else
      {
        LOG.debug("U");
        alerts.set(index, alert);
      }
    }
    else
    {
      if (index < 0)
      {
        LOG.error("index is " + index + " on a delete");
      }
      else
      {
        LOG.debug("C");
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
      long e1 = o1.getCompositeAlertId( );
      long e2 = o2.getCompositeAlertId( );
      return (e1 == e2 ? 0 : (e1 < e2) ? -1 : 1);
    }
  }
}
