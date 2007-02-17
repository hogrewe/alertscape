/*
 * Created on Mar 15, 2006
 */
package com.alertscape.cev.model.tree;

import java.util.List;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.matchers.Matcher;

import com.alertscape.cev.model.Event;
import com.alertscape.cev.model.EventCollection;
import com.alertscape.cev.model.SeverityCounter;


/**
 * @author josh
 * @version $Version: $
 */
public class CevTreeNode implements EventCollection
{
  private SeverityCounter severityCounter = new SeverityCounter();
  private Matcher<Event> eventMatcher;
  
  public void clearEvents( )
  {
    // TODO Auto-generated method stub
    
  }
  public Event getEvent(long id)
  {
    // TODO Auto-generated method stub
    return null;
  }
  public Event getEventAt(int index)
  {
    // TODO Auto-generated method stub
    return null;
  }
  public int getEventCount( )
  {
    // TODO Auto-generated method stub
    return 0;
  }
  public EventList<Event> getEventList( )
  {
    // TODO Auto-generated method stub
    return null;
  }
  public void processEvents(List<Event> newEvents)
  {
    // TODO Auto-generated method stub
    
  }
  public void setEventList(EventList<Event> list)
  {
    // TODO Auto-generated method stub
    
  }
}
