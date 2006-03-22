/*
 * Created on Mar 15, 2006
 */
package com.alertscape.cev.model.severity;

/**
 * @author josh
 * @version $Version: $
 */
public class Severity
{
    private int level;
    private String smallIcon;
    private String largeIcon;
    private String name;
    private String backgroundColor;
    private String foregroundColor;

    /**
     * @return Returns the backgroundColor.
     */
    public String getBackgroundColor( )
    {
        return backgroundColor;
    }

    /**
     * @param backgroundColor
     *            The backgroundColor to set.
     */
    void setBackgroundColor(String backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return Returns the foregroundColor.
     */
    public String getForegroundColor( )
    {
        return foregroundColor;
    }

    /**
     * @param foregroundColor
     *            The foregroundColor to set.
     */
    void setForegroundColor(String foregroundColor)
    {
        this.foregroundColor = foregroundColor;
    }

    /**
     * @return Returns the largeIcon.
     */
    public String getLargeIcon( )
    {
        return largeIcon;
    }

    /**
     * @param largeIcon
     *            The largeIcon to set.
     */
    void setLargeIcon(String largeIcon)
    {
        this.largeIcon = largeIcon;
    }

    /**
     * @return Returns the level.
     */
    public int getLevel( )
    {
        return level;
    }

    /**
     * @param level
     *            The level to set.
     */
    void setLevel(int level)
    {
        this.level = level;
    }

    /**
     * @return Returns the name.
     */
    public String getName( )
    {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Returns the smallIcon.
     */
    public String getSmallIcon( )
    {
        return smallIcon;
    }

    /**
     * @param smallIcon
     *            The smallIcon to set.
     */
    void setSmallIcon(String smallIcon)
    {
        this.smallIcon = smallIcon;
    }
}
