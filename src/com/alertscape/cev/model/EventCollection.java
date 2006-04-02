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
        synchronized (lock) {
            for (int i = 0; i < events.size( ); i++) {
                Event e = events.get(i);
                if (e.isStanding( )) {
                    addEvent(e);
                } else {
                    removeEvent(e);
                }
            }
        }
        fireEventChange(events);
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

    protected void fireEventChange(List<Event> events)
    {
        EventChange change = new EventChange(EventChange.PARTIAL, events);
        support.fireEventChange(change);
    }
    
    protected void fireFullEventChange(List<Event> events)
    {
        EventChange change = new EventChange(EventChange.PARTIAL, events);
        support.fireEventChange(change);
    }

    protected void addEvent(Event e)
    {
        eventMap.put(e.getEventId( ), e);
        events.remove(e);
        events.add(e);
    }

    protected void removeEvent(Event e)
    {
        eventMap.remove(e.getEventId( ));
        events.remove(e);
    }

}
