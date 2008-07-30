/*
 * Created on Mar 15, 2006
 */
package com.alertscape.cev.model;

import java.util.Date;

import com.alertscape.cev.model.severity.Severity;

/**
 * @author josh
 * @version $Version: $
 */
public class Event
{
  public enum EventStatus
  {
    STANDING,
    CLEARED;
  }

  private long eventId;
  private EventStatus status;
  private String shortDescription;
  private String longDescription;
  private String type;
  private Severity severity;
  private long count;
  private long sourceId;
  private String item;
  private String itemManager;
  private String itemType;
  private String itemManagerType;
  private Date firstOccurence;
  private Date lastOccurence;

  public long getCount( )
  {
    return count;
  }

  public void setCount(long count)
  {
    this.count = count;
  }

  public Date getFirstOccurence( )
  {
    return firstOccurence;
  }

  public void setFirstOccurence(Date firstOccurence)
  {
    this.firstOccurence = firstOccurence;
  }

  public String getItem( )
  {
    return item;
  }

  public void setItem(String item)
  {
    this.item = item;
  }

  public String getItemManager( )
  {
    return itemManager;
  }

  public void setItemManager(String itemManager)
  {
    this.itemManager = itemManager;
  }

  public String getItemManagerType( )
  {
    return itemManagerType;
  }

  public void setItemManagerType(String itemManagerType)
  {
    this.itemManagerType = itemManagerType;
  }

  public String getItemType( )
  {
    return itemType;
  }

  public void setItemType(String itemType)
  {
    this.itemType = itemType;
  }

  public Date getLastOccurence( )
  {
    return lastOccurence;
  }

  public void setLastOccurence(Date lastOccurence)
  {
    this.lastOccurence = lastOccurence;
  }

  public Severity getSeverity( )
  {
    return severity;
  }

  public void setSeverity(Severity severity)
  {
    this.severity = severity;
  }

  public long getSourceId( )
  {
    return sourceId;
  }

  public void setSourceId(long sourceId)
  {
    this.sourceId = sourceId;
  }

  public String getType( )
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public boolean isStanding( )
  {
    return getStatus( ) == EventStatus.STANDING;
  }

  public long getEventId( )
  {
    return eventId;
  }

  public void setEventId(long eventId)
  {
    this.eventId = eventId;
  }

  public String getLongDescription( )
  {
    return longDescription;
  }

  public void setLongDescription(String longDescription)
  {
    this.longDescription = longDescription;
  }

  public String getShortDescription( )
  {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription)
  {
    this.shortDescription = shortDescription;
  }

  public EventStatus getStatus( )
  {
    return status;
  }

  public void setStatus(EventStatus status)
  {
    this.status = status;
  }

  @Override
  public boolean equals(Object o)
  {
    return getEventId( ) == ((Event) o).getEventId( );
  }

  @Override
  public int hashCode( )
  {
    return Long.valueOf(getEventId( )).hashCode( );
  }

}
