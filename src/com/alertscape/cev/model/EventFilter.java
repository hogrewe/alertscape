/*
 * Created on Aug 11, 2006
 */
package com.alertscape.cev.model;

/**
 * @author josh
 * @version $Version: $
 */
public interface EventFilter
{
  public void setMasterCollection(EventCollection master);

  public void setSubCollection(EventCollection sub);
}
