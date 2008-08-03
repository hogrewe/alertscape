/*
 * Created on Mar 15, 2006
 */
package com.alertscape.common.model.severity;

import java.awt.Color;

/**
 * @author josh
 * @version $Version: $
 */
public class SeverityFactory
{
  private static SeverityFactory inst = new SeverityFactory( );

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
    sevNum++;
    
    severities[sevNum] = new Severity( );
    severities[sevNum].setLevel(sevNum);
    severities[sevNum].setBackgroundColor(Color.blue);
    severities[sevNum].setForegroundColor(Color.white);
    severities[sevNum].setName("Warning");
    sevNum++;

    severities[sevNum] = new Severity( );
    severities[sevNum].setLevel(sevNum);
    severities[sevNum].setBackgroundColor(new Color(236, 233, 0));
    severities[sevNum].setForegroundColor(Color.BLACK);
    severities[sevNum].setName("Minor");
    sevNum++;

    severities[sevNum] = new Severity( );
    severities[sevNum].setLevel(sevNum);
    severities[sevNum].setBackgroundColor(Color.orange);
    severities[sevNum].setForegroundColor(Color.BLACK);
    severities[sevNum].setName("Major");
    sevNum++;

    severities[sevNum] = new Severity( );
    severities[sevNum].setLevel(sevNum);
    severities[sevNum].setBackgroundColor(new Color(198, 65, 0));
    severities[sevNum].setForegroundColor(Color.BLACK);
    severities[sevNum].setName("Critical");
    sevNum++;

    // TODO: Load the severities from a config file
  }

  public Severity getSeverity(int level)
  {
    Severity sev = null;
    if (level < severities.length)
    {
      sev = severities[level];
    }
    return sev;
  }

  public int getNumSeverities( )
  {
    return severities.length;
  }
}
