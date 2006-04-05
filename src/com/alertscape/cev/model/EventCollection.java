/*
 * Created on Mar 26, 2006
 */
package com.alertscape.cev.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author josh
 * @version $Version: $
 */
public class EventCollection
{
    private byte[] lock = new byte[0];
    private Map<Long, Event> eventMap = new HashMap<Long,Event>();
    private List<Event> events = new ArrayList<Event>();
    private EventChangeSupport support = new EventChangeSupport( );

    public void processEvents(List<Event> events)
    {
        List<Event> adds = new ArrayList<Event>(0);
        List<Event> removes = new ArrayList<Event>(0);
        List<Integer> removeindices = new ArrayList<Integer>(0);
        synchronized (lock) 
        {            
            for (int i = 0; i < events.size( ); i++) 
            {
                int removeindex = -1;
                Event e = events.get(i);
                if (e.isStanding( )) 
                {    
                    removeindex = addEvent(e);
                    adds.add(e);
                } 
                else 
                {
                    removeindex = removeEvent(e);
                    removes.add(e);
                }
                if (removeindex>=0)
                {
                    removeindices.add(removeindex);  
                }
                
            }
        }
        fireEventChange(adds, removes, removeindices);
    }

    public List<Event> getAllEvents( )
    {
        List<Event> all = null;
        synchronized (lock) {
            all = new ArrayList<Event>(eventMap.values( ));
        }
        return all;
    }
    
    public int getEventCount()
    {
        return eventMap.size();
    }

    public Event getEvent(long id)
    {
        Event e = null;
        synchronized (lock) {
            e = eventMap.get(id);
        }
        return e;
    }
    
    public Event getEventAt(int index)
    {
        return events.get(index);
    }
    
    public void clearEvents()
    {
        eventMap.clear();
        fireFullEventChange(new ArrayList<Event>());
    }
    
    public void addEventChangeListener(EventChangeListener l)
    {
        support.addListener(l);
    }

    public void removeEventChangeListener(EventChangeListener l)
    {
        support.removeListener(l);
    }

    protected void fireEventChange(List<Event> addEvents, List<Event> removeEvents, List<Integer> indexes)
    {
        EventChange change = new EventChange(EventChange.PARTIAL, addEvents);
        change.setRemoveEvents(removeEvents);
        change.setRemoveIndexes(indexes);
        support.fireEventChange(change);
    }
    
    protected void fireFullEventChange(List<Event> events)
    {
        EventChange change = new EventChange(EventChange.FULL, events);
        support.fireEventChange(change);
    }

    protected int addEvent(Event e)
    {
        eventMap.put(e.getEventId( ), e);        
        int index = events.indexOf(e);
        if (index >= 0)
        {
            events.remove(index);        

        }
        events.add(e);
        return index;
    }

    protected int removeEvent(Event e)
    {
        eventMap.remove(e.getEventId( ));
        int index = events.indexOf(e);
        if (index >= 0)
        {
          events.remove(index);
          //events.remove(e);
        }
        return index;
    }

}
