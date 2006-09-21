/*
 * Created on Mar 15, 2006
 */
package com.alertscape.cev.model.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alertscape.cev.model.Event;
import com.alertscape.cev.model.criterion.EventCriterion;
import com.alertscape.cev.model.severity.SeverityFactory;

/**
 * @author josh
 * @version $Version: $
 */
public class CevTreeNode
{
    public static final String CHILD_ADDED_PROP = "ChildAdded";
    public static final String CHILD_REMOVED_PROP = "ChildRemoved";
    public static final String DISPLAY_CHANGED_PROP = "DisplayChanged";
    public static final String EVENT_ADDED_PROP = "EventAdded";
    public static final String EVENT_REMOVED_PROP = "EventRemoved";

    private byte[] eventLock = new byte[0];

    private CevTreeNode parent;
    private List<CevTreeNode> children;
    /** This is the order that we should try to add the events to the children */
    private List<CevTreeNode> childAddOrder;
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

    @SuppressWarnings("unchecked")
    public CevTreeNode( )
    {
        int numsevs = SeverityFactory.getInstance( ).getMaxSeverity( );
        severityEvents = new Map[numsevs];
        for (int i = 0; i < numsevs; i++)
        {
            severityEvents[i] = new HashMap<String, Event>( );
        }
        children = new ArrayList<CevTreeNode>( );
    }

    /**
     * This method will look to see if the event should be added to this node,
     * given this node's criteria, and will return true if it was added, and
     * false if it was not.
     * 
     * @param e The event to add to the node
     * @return true if the event was added, false otherwise
     */
    public boolean addEvent(Event e)
    {
        boolean added = false;

        synchronized (eventLock)
        {
        }

        return added;
    }

    public void removeEvent(Event e)
    {
        synchronized (eventLock)
        {

        }
    }

    public List<CevTreeNode> getChildren( )
    {
        synchronized (eventLock)
        {
            return Collections.unmodifiableList(children);
        }
    }

    public int getChildCount( )
    {
        synchronized (eventLock)
        {
            return children.size( );
        }
    }

    public void addChild(CevTreeNode child)
    {
        children.add(child);
    }

    public void addChild(int index, CevTreeNode child)
    {
        children.add(index, child);
    }

    public void setChildAddOrder(List<CevTreeNode> addOrder)
    {
        this.childAddOrder = addOrder;
    }

    protected List<CevTreeNode> getChildAddOrder( )
    {
        if (childAddOrder == null)
        {
            return children;
        } else
        {
            return childAddOrder;
        }
    }

    public void clearEvents( )
    {
        synchronized (eventLock)
        {
            events.clear( );
            for (Map<String, Event> element : severityEvents)
            {
                element.clear( );
            }
            maxSeverity = 0;
        }

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
     * @param noMatchNode
     *            The noMatchNode to set.
     */
    public void setNoMatchNode(CevTreeNode noMatchNode)
    {
        this.noMatchNode = noMatchNode;
        if (!children.contains(noMatchNode))
        {
            children.add(noMatchNode);
        }
    }
}
