/*
 * Created on Mar 26, 2006
 */
package com.alertscape.cev.model;

import java.util.List;


/**
 * @author josh
 * @version $Version: $
 */
public class EventChange
{
    public static final int PARTIAL = 1;
    public static final int FULL = 2;
    
    private List<Event> events;
    private int changeType;
    
    public EventChange(int type, List<Event> events)
    {
        this.changeType = type;
        this.events = events;
    }

    public int getChangeType( )
    {
        return changeType;
    }

    public List<Event> getEvents( )
    {
        return events;
    }
}
