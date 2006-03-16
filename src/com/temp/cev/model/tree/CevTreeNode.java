/*
 * Created on Mar 15, 2006
 */
package com.temp.cev.model.tree;

import java.util.List;
import java.util.Map;

import com.temp.cev.model.Event;

/**
 * @author josh
 * @version $Version: $
 */
public class CevTreeNode
{
    private CevTreeNode parent;
    private List<CevTreeNode> children;
    private CevTreeNode noMatchNode;
    private List<Event> events;
    /** Trade off space for speed in handling severity changes */
    private Map<String, Event>[] severityEvents;
    private EventCriterion eventCriterion;
    private BlinkCriterion blinkCriterion;
    private int maxSeverity;
    private String icon;
    private String text;
    private String description;
    private boolean blink;
    
    private boolean addEvent(Event e)
    {
        boolean added = false;
        
        return added;
    }
    
    private void removeEvent(Event e)
    {
        
    }
    
    /**
     * @return Returns the blink.
     */
    public boolean isBlink( )
    {
        return blink;
    }

    /**
     * @param blink
     *            The blink to set.
     */
    public void setBlink(boolean blink)
    {
        this.blink = blink;
    }

    /**
     * @return Returns the blinkCriterion.
     */
    public BlinkCriterion getBlinkCriterion( )
    {
        return blinkCriterion;
    }

    /**
     * @param blinkCriterion
     *            The blinkCriterion to set.
     */
    public void setBlinkCriterion(BlinkCriterion blinkCriterion)
    {
        this.blinkCriterion = blinkCriterion;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription( )
    {
        return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return Returns the eventCriterion.
     */
    public EventCriterion getEventCriterion( )
    {
        return eventCriterion;
    }

    /**
     * @param eventCriterion
     *            The eventCriterion to set.
     */
    public void setEventCriterion(EventCriterion eventCriterion)
    {
        this.eventCriterion = eventCriterion;
    }

    /**
     * @return Returns the events.
     */
    public List<Event> getEvents( )
    {
        return events;
    }

    /**
     * @param events
     *            The events to set.
     */
    public void setEvents(List<Event> events)
    {
        this.events = events;
    }

    /**
     * @return Returns the icon.
     */
    public String getIcon( )
    {
        return icon;
    }

    /**
     * @param icon
     *            The icon to set.
     */
    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    /**
     * @return Returns the maxSeverity.
     */
    public int getMaxSeverity( )
    {
        return maxSeverity;
    }

    /**
     * @param maxSeverity
     *            The maxSeverity to set.
     */
    public void setMaxSeverity(int maxSeverity)
    {
        this.maxSeverity = maxSeverity;
    }

    /**
     * @return Returns the parent.
     */
    public CevTreeNode getParent( )
    {
        return parent;
    }

    /**
     * @param parent
     *            The parent to set.
     */
    public void setParent(CevTreeNode parent)
    {
        this.parent = parent;
    }

    /**
     * @return Returns the text.
     */
    public String getText( )
    {
        return text;
    }

    /**
     * @param text
     *            The text to set.
     */
    public void setText(String text)
    {
        this.text = text;
    }

    /**
     * @return Returns the noMatchNode.
     */
    public CevTreeNode getNoMatchNode( )
    {
        return noMatchNode;
    }

    /**
     * @param noMatchNode The noMatchNode to set.
     */
    public void setNoMatchNode(CevTreeNode noMatchNode)
    {
        this.noMatchNode = noMatchNode;
    }
}
