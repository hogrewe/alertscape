/*
 * Created on Mar 26, 2006
 */
package com.alertscape.cev.model;

import java.util.List;

import com.alertscape.common.model.Event;
import com.alertscape.common.model.EventCollection;

/**
 * @author josh
 * @version $Version: $
 */
public class EventChange
{
  private EventChangeType type;
  private List<Event> events;
  private List<Integer> indices;
  private EventCollection source;

  public EventChange(EventChangeType type, List<Event> events,
      List<Integer> indices, EventCollection source)
  {
    this.type = type;
    this.events = events;
    this.indices = indices;
    this.source = source;
  }

  public EventCollection getSource( )
  {
    return source;
  }

  public List<Event> getEvents( )
  {
    return events;
  }

  public List<Integer> getIndices( )
  {
    return indices;
  }

  public EventChangeType getType( )
  {
    return type;
  }

  public enum EventChangeType
  {
    INSERT("INSERT"),
    UPDATE("UPDATE"),
    REMOVE("REMOVE");

    private String type;

    private EventChangeType(String type)
    {
      this.type = type;
    }

    @Override
    public String toString( )
    {
      return type;
    }
  }

}
