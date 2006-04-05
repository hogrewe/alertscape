/*
 * Created on Mar 15, 2006
 */
package com.alertscape.cev.model.severity;

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
        severities = new Severity[3];
        severities[0] = new Severity();
        severities[0].setLevel(0);
        severities[0].setBackgroundColor(new Color(16,187,12) );
        severities[0].setForegroundColor(Color.BLACK);
        severities[0].setName("Normal");

        severities[1] = new Severity();
        severities[1].setLevel(1);
        severities[1].setBackgroundColor(new Color(236,233,0));
        severities[1].setForegroundColor(Color.BLACK);
        severities[1].setName("Minor");

        severities[2] = new Severity();
        severities[2].setLevel(2);
        severities[2].setBackgroundColor(new Color(198,65,0));
        severities[2].setForegroundColor(Color.BLACK);
        severities[2].setName("Critical");

        // TODO: Load the severities from a config file
    }

    public Severity getSeverity(int level)
    {
        Severity sev = null;
        if (level < severities.length) {
            sev = severities[level];
        }
        return sev;
    }
    
    public int getMaxSeverity()
    {
        return severities.length;
    }
}
