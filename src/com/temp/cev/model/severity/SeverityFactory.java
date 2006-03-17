/*
 * Created on Mar 15, 2006
 */
package com.temp.cev.model.severity;

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
