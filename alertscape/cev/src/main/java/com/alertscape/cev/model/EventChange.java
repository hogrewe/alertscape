/*
 * Created on Mar 26, 2006
 */
package com.alertscape.cev.model;

import java.util.List;

import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;

/**
 * @author josh
 * @version $Version: $
 */
public class EventChange
{
  private EventChangeType type;
  private List<Alert> events;
  private List<Integer> indices;
  private AlertCollection source;

  public EventChange(EventChangeType type, List<Alert> events,
      List<Integer> indices, AlertCollection source)
  {
    this.type = type;
    this.events = events;
    this.indices = indices;
    this.source = source;
  }

  public AlertCollection getSource( )
  {
    return source;
  }

  public List<Alert> getEvents( )
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
