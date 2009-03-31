/*
 * Created on Mar 15, 2006
 */
package com.alertscape.common.model.severity;

import java.awt.Color;

import com.alertscape.common.logging.ASLogger;

/**
 * @author josh
 * @version $Version: $
 */
public class SeverityFactory
{
  private static SeverityFactory inst = new SeverityFactory( );
  private static final ASLogger LOG = ASLogger.getLogger(SeverityFactory.class);

  private Severity[] severities;

  public static final SeverityFactory getInstance( )
  {
    return inst;
  }

  protected SeverityFactory( )
  {
    int sevNum = 0;
    severities = new Severity[5];

    severities[sevNum] = new Severity( );
    severities[sevNum].setLevel(sevNum);
    severities[sevNum].setBackgroundColor(new Color(16, 187, 12));
    severities[sevNum].setForegroundColor(Color.BLACK);
    severities[sevNum].setName("Normal");
    severities[sevNum].setSmallIcon("/com/alertscape/images/function/circle_green.png");
    sevNum++;
    
    severities[sevNum] = new Severity( );
    severities[sevNum].setLevel(sevNum);
    severities[sevNum].setBackgroundColor(Color.blue);
    severities[sevNum].setForegroundColor(Color.white);
    severities[sevNum].setName("Warning");
    severities[sevNum].setSmallIcon("/com/alertscape/images/function/circle_blue.png");
    sevNum++;

    severities[sevNum] = new Severity( );
    severities[sevNum].setLevel(sevNum);
    severities[sevNum].setBackgroundColor(new Color(236, 233, 0));
    severities[sevNum].setForegroundColor(Color.BLACK);
    severities[sevNum].setName("Minor");
    severities[sevNum].setSmallIcon("/com/alertscape/images/function/circle_yellow.png");
    sevNum++;

    severities[sevNum] = new Severity( );
    severities[sevNum].setLevel(sevNum);
    severities[sevNum].setBackgroundColor(new Color(255, 130, 0));
    severities[sevNum].setForegroundColor(Color.BLACK);
    severities[sevNum].setName("Major");
    severities[sevNum].setSmallIcon("/com/alertscape/images/function/circle_orange.png");
    sevNum++;

    severities[sevNum] = new Severity( );
    severities[sevNum].setLevel(sevNum);
    severities[sevNum].setBackgroundColor(new Color(255, 65, 0));
    severities[sevNum].setForegroundColor(Color.BLACK);
    severities[sevNum].setSmallIcon("/com/alertscape/images/function/circle_red.png");
    severities[sevNum].setName("Critical");
    sevNum++;

    // TODO: Load the severities from a config file
  }

  public Severity getSeverity(int level)
  {
    if (level >= severities.length)
    {
      LOG.error("Tried to get severity higher than the configured max severity, defaulting to max severity");
      level = severities.length - 1;
    }
    return severities[level];
  }

  public int getNumSeverities( )
  {
    return severities.length;
  }
}
