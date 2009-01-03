/*
 * Created on Oct 21, 2006
 */
package com.alertscape.common.model;

import java.util.HashMap;
import java.util.Map;

import com.alertscape.common.logging.ASLogger;


import ca.odell.glazedlists.EventList;

/**
 * @author josh
 * @version $Version: $
 */
public class IndexedAlertCollection extends AbstractAlertCollection
{
  private static final ASLogger LOG = ASLogger.getLogger(IndexedAlertCollection.class);
  private Map<Long, Integer> idToIndex = new HashMap<Long, Integer>( );
  
  public IndexedAlertCollection()
  {
  }
  
  public IndexedAlertCollection(EventList<Alert> alertList)
  {
    super(alertList);
  }

  @Override
  protected void processSingleAlert(Alert alert)
  {
    Integer index = idToIndex.get(alert.getAlertId( ));
    if (alert.isStanding( ))
    {
      // Is it new?
      if (index == null)
      {
        LOG.debug("N");
        alerts.add(alert);
        idToIndex.put(alert.getAlertId( ), alerts.size( )-1);
      }
      // It's already in the list, just update it
      else
      {
        LOG.debug("U");
        alerts.set(index, alert);
      }
    }
    else
    {
      if (index != null)
      {
        LOG.debug("C");
        alerts.remove(index);
        // Ok, we removed the item at index, so we have to update the map for
        // everything that comes after index
        for (int i = index; i < alerts.size( ); i++)
        {
          Alert e = alerts.get(i);
          int nextIndex = idToIndex.get(e.getAlertId( ));
          nextIndex--;
          idToIndex.put(e.getAlertId( ), nextIndex);
        }
      }
      else
      {
        LOG.error("index is null on a delete");
      }
    }
  }

  @Override
  public void clearAlerts( )
  {
    super.clearAlerts( );
    idToIndex.clear( );
  }

  @Override
  public Alert getAlert(long id)
  {
    Alert e = null;
    Integer index = idToIndex.get(id);
    if (index != null)
    {
      e = alerts.get(index);
    }

    return e;
  }
}
