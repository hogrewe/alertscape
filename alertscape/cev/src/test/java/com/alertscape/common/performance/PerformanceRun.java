/*
 * Created on Sep 30, 2006
 */
package com.alertscape.common.performance;

/**
 * @author josh
 * @version $Version: $
 */
public class PerformanceRun
{
  private long startTime;
  private long endTime;
  // Default to 1 operation
  private int numOperations = 1;
  private boolean running;

  public void start( )
  {
    if (running)
    {
      throw new IllegalStateException(
          "Cannot start a PerformanceRun if it is already running");
    }
    this.startTime = System.currentTimeMillis( );
  }

  public void end( )
  {
    if (running)
    {
      throw new IllegalStateException(
          "Cannot end a PerformanceRun that hasn't been started");
    }
    this.endTime = System.currentTimeMillis( );
  }

  public long getMillisElapsed( )
  {
    return getEndTime( ) - getStartTime( );
  }

  public float getOperationsPerMilli( )
  {
    return (float) getNumOperations( ) / (float) getMillisElapsed( );
  }

  public float getMillisPerOperation( )
  {
    return (float) getMillisElapsed( ) / (float) getNumOperations( );
  }

  /**
   * @return Returns the endTime.
   */
  public long getEndTime( )
  {
    return endTime;
  }

  /**
   * @return Returns the numOperations.
   */
  public int getNumOperations( )
  {
    return numOperations;
  }

  /**
   * @param numOperations
   *          The numOperations to set.
   */
  public void setNumOperations(int numOperations)
  {
    this.numOperations = numOperations;
  }

  /**
   * @return Returns the startTime.
   */
  public long getStartTime( )
  {
    return startTime;
  }
}
