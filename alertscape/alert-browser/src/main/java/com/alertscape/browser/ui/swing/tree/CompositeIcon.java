/**
 * 
 */
package com.alertscape.browser.ui.swing.tree;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * @author josh
 *
 */
public class CompositeIcon implements Icon {
  private Icon[] icons;
  private int height;
  private int width;
  
  public CompositeIcon(Icon[] icons) {
    setIcons(icons);
  }

  public int getIconHeight() {
    return height;
  }

  public int getIconWidth() {
    return width;
  }

  public void paintIcon(Component c, Graphics g, int x, int y) {
    int xoffset = 0;
    for (Icon icon : icons) {
      icon.paintIcon(c, g, xoffset, y);
      xoffset += icon.getIconWidth();
    }
  }

  /**
   * @return the icons
   */
  public Icon[] getIcons() {
    return icons;
  }

  /**
   * @param icons the icons to set
   */
  public void setIcons(Icon[] icons) {
    this.icons = icons;
    for (Icon icon : icons) {
      if(icon.getIconHeight() > height) {
        height = icon.getIconHeight();
      }
      width += icon.getIconWidth();
    }
  }

}
