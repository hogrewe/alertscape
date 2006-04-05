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
    
    private List<Event> addEvents;
    private List<Event> removeEvents;
    private List<Integer> removeIndexes;
    private int changeType;
    
    public EventChange(int type, List<Event> addEvents)
    {
        this.changeType = type;
        this.addEvents = addEvents;
    }

    public int getChangeType( )
    {
        return changeType;
    }

    public List<Event> getAddEvents( )
    {
        return addEvents;
    }

    public List<Event> getRemoveEvents( )
    {
        return removeEvents;
    }

    public void setRemoveEvents(List<Event> removeEvents)
    {
        this.removeEvents = removeEvents;
    }

    public List<Integer> getRemoveIndexes( )
    {
        return removeIndexes;
    }

    public void setRemoveIndexes(List<Integer> removeIndexes)
    {
        this.removeIndexes = removeIndexes;
    }
}
