/*
 * Created on Mar 26, 2006
 */
package com.alertscape.common.model;

import java.util.List;


import ca.odell.glazedlists.EventList;

/**
 * @author josh
 * @version $Version: $
 */
public interface AlertCollection
{
  public void processAlerts(List<Alert> newEvents);

  public EventList<Alert> getEventList( );

  public void setEventList(EventList<Alert> list);

  public int getAlertCount( );

  public Alert getAlert(long id);

  public Alert getAlertAt(int index);

  public void clearAlerts( );
}
