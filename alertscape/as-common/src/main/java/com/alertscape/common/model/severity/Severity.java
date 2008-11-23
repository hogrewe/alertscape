/*
 * Created on Mar 15, 2006
 */
package com.alertscape.common.model.severity;

import java.awt.Color;
import java.io.Serializable;

/**
 * @author josh
 * @version $Version: $
 */
public class Severity implements Comparable<Severity>, Serializable {
  private static final long serialVersionUID = -4475963054463898427L;
  private int level;
  private String smallIcon;
  private String largeIcon;
  private String name;
  private Color backgroundColor;
  private Color foregroundColor;
  private Color selectionForegroundColor;
  private Color selectionBackgroundColor;

  Severity() {

  }

  /**
   * @return Returns the backgroundColor.
   */
  public Color getBackgroundColor() {
    return backgroundColor;
  }

  /**
   * @param backgroundColor
   *          The backgroundColor to set.
   */
  void setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  /**
   * @return Returns the foregroundColor.
   */
  public Color getForegroundColor() {
    return foregroundColor;
  }

  /**
   * @param foregroundColor
   *          The foregroundColor to set.
   */
  void setForegroundColor(Color foregroundColor) {
    this.foregroundColor = foregroundColor;
  }

  /**
   * @return Returns the largeIcon.
   */
  public String getLargeIcon() {
    return largeIcon;
  }

  /**
   * @param largeIcon
   *          The largeIcon to set.
   */
  void setLargeIcon(String largeIcon) {
    this.largeIcon = largeIcon;
  }

  /**
   * @return Returns the level.
   */
  public int getLevel() {
    return level;
  }

  /**
   * @param level
   *          The level to set.
   */
  void setLevel(int level) {
    this.level = level;
  }

  /**
   * @return Returns the name.
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          The name to set.
   */
  void setName(String name) {
    this.name = name;
  }

  /**
   * @return Returns the smallIcon.
   */
  public String getSmallIcon() {
    return smallIcon;
  }

  /**
   * @param smallIcon
   *          The smallIcon to set.
   */
  void setSmallIcon(String smallIcon) {
    this.smallIcon = smallIcon;
  }

  public Color getSelectionBackgroundColor() {
    return selectionBackgroundColor;
  }

  void setSelectionBackgroundColor(Color selectionBackgroundColor) {
    this.selectionBackgroundColor = selectionBackgroundColor;
  }

  public Color getSelectionForegroundColor() {
    return selectionForegroundColor;
  }

  void setSelectionForegroundColor(Color selectionForegroundColor) {
    this.selectionForegroundColor = selectionForegroundColor;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Severity other = (Severity) obj;
    if (level != other.level)
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + level;
    return result;
  }

  public int compareTo(Severity s) {
    return getLevel() - s.getLevel();
  }

  @Override
  public String toString() {
    return getName();
  }
}
