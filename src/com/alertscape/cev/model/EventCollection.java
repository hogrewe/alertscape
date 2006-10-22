/*
 * Created on Mar 26, 2006
 */
package com.alertscape.cev.model;

import java.util.List;

import ca.odell.glazedlists.EventList;

/**
 * @author josh
 * @version $Version: $
 */
public interface EventCollection
{
  public void processEvents(List<Event> newEvents);

  public EventList<Event> getEventList( );

  public void setEventList(EventList<Event> list);

  public int getEventCount( );

  public Event getEvent(long id);

  public Event getEventAt(int index);

  public void clearEvents( );
}
