/*
 * Created on Mar 15, 2006
 */
package com.alertscape.cev.model.severity;

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
        severities = new Severity[1];
        severities[0] = new Severity();
        severities[0].setLevel(0);
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
