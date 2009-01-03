/*
 * Created on Oct 17, 2006
 */
package com.alertscape.common.model;


import ca.odell.glazedlists.util.concurrent.Lock;

import com.alertscape.common.logging.ASLogger;

/**
 * @author josh
 * @version $Version: $
 */
public class NaiveAlertCollection extends AbstractAlertCollection
{
  private static final ASLogger LOG = ASLogger.getLogger(NaiveAlertCollection.class);
  @Override
  protected void processSingleAlert(Alert alert)
  {
    int existingAlertIndex = indexOf(alert);
    if (alert.isStanding( ))
    {
      // A new event, never seen
      if (existingAlertIndex < 0)
      {
        LOG.debug("N");
        alerts.add(alert);
      }
      // We already have it, update it
      else
      {
        LOG.debug("U");
        alerts.set(existingAlertIndex, alert);
      }
    }
    else
    {
      assert (existingAlertIndex >= 0);
      LOG.debug("C");

      alerts.remove(alert);
      alerts.remove(existingAlertIndex);
    }
  }

  protected int indexOf(Alert alert)
  {
    Lock lock = alerts.getReadWriteLock( ).readLock( );
    try
    {
      lock.lock( );
      for (int i = 0, size = alerts.size( ); i < size; i++)
      {
        Alert a = alerts.get(i);
        if (a.getAlertId( ) == alert.getAlertId( ))
        {
          return i;
        }
      }
    }
    finally
    {
      lock.unlock( );
    }

    return -1;
  }
}
