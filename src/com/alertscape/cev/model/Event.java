/*
 * Created on Mar 15, 2006
 */
package com.alertscape.cev.model;

/**
 * @author josh
 * @version $Version: $
 */
public interface Event
{
    public final static int STANDING = 1;
    public final static int CLEARED = 2;
    
    public String getEventId();
    public int getStatus();
    public boolean isStanding();
}
