/*
 * Created on Mar 15, 2006
 */
package com.alertscape.cev.model.criterion;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * @version $Version: $
 */
public interface EventCriterion
{
    /**
     * This will test the <code>Event</code> to determine if it matches this
     * criterion.
     * 
     * @param e
     *            The <code>Event</code> to test
     * @return true if the <code>Event</code> matches this criterion, false
     *         otherwise
     */
    public boolean matches(Alert e);
}
