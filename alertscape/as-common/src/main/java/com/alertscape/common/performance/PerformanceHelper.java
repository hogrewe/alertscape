/*
 * Created on Sep 30, 2006
 */
package com.alertscape.common.performance;

import java.util.HashMap;
import java.util.Map;

import com.alertscape.common.logging.ASLogger;

/**
 * @author josh
 * @version $Version: $
 */
public class PerformanceHelper
{
  private static final ASLogger LOG = ASLogger.getLogger(PerformanceHelper.class);
  private static Map<String, PerformanceRun> runs = new HashMap<String, PerformanceRun>();
  
  public static void start(String id) {
    PerformanceRun run = runs.get(id);
    if(run == null) {
      run = new PerformanceRun();
      runs.put(id, run);
    }
    run.start();
  }
  
  public static void end(String id) {
    PerformanceRun run = runs.get(id);
    run.end();
    LOG.info(id + ":" + run.getMillisElapsed());
  }
}
