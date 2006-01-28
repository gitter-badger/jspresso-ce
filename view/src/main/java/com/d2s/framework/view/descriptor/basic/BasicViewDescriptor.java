/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Map;

import com.d2s.framework.action.ActionMap;
import com.d2s.framework.action.IDisplayableAction;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.util.descriptor.DefaultIconDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of a view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicViewDescriptor extends DefaultIconDescriptor
    implements IViewDescriptor {

  private int              borderType = NONE;
  private Color            foreground;
  private Color            background;
  private Font             font;
  private ActionMap        actionMap;
  private IModelDescriptor modelDescriptor;

  /**
   * {@inheritDoc}
   */
  public Color getForeground() {
    return foreground;
  }

  /**
   * {@inheritDoc}
   */
  public Color getBackground() {
    return background;
  }

  /**
   * {@inheritDoc}
   */
  public Font getFont() {
    return font;
  }

  /**
   * Sets the background.
   * 
   * @param background
   *          the background to set.
   */
  public void setBackground(Color background) {
    this.background = background;
  }

  /**
   * Sets the font.
   * 
   * @param font
   *          the font to set.
   */
  public void setFont(Font font) {
    this.font = font;
  }

  /**
   * Sets the foreground.
   * 
   * @param foreground
   *          the foreground to set.
   */
  public void setForeground(Color foreground) {
    this.foreground = foreground;
  }

  /**
   * {@inheritDoc}
   */
  public int getBorderType() {
    return borderType;
  }

  /**
   * Sets the borderType.
   * 
   * @param borderType
   *          the borderType to set.
   */
  public void setBorderType(int borderType) {
    this.borderType = borderType;
  }

  /**
   * Gets the actionMap.
   * 
   * @return the actionMap.
   */
  public Map<String, List<IDisplayableAction>> getActions() {
    if (actionMap != null) {
      return actionMap.getActionMap();
    }
    return null;
  }

  /**
   * Sets the actionMap.
   * 
   * @param actionMap
   *          the actionMap to set.
   */
  public void setActionMap(ActionMap actionMap) {
    this.actionMap = actionMap;
  }

  /**
   * Gets the modelDescriptor.
   * 
   * @return the modelDescriptor.
   */
  public IModelDescriptor getModelDescriptor() {
    return modelDescriptor;
  }

  /**
   * Sets the modelDescriptor.
   * 
   * @param modelDescriptor
   *          the modelDescriptor to set.
   */
  public void setModelDescriptor(IModelDescriptor modelDescriptor) {
    this.modelDescriptor = modelDescriptor;
  }
}
