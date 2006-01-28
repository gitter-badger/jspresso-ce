/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

/**
 * Simple holder of view constraints.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ViewConstraints {

  private int     row;

  private int     column;

  private int     width;

  private int     height;

  private boolean widthResizable;

  private boolean heightResizable;

  /**
   * Constructs a new <code>ViewConstraints</code> instance.
   */
  public ViewConstraints() {
    super();
  }

  /**
   * Constructs a new <code>ViewConstraints</code> instance.
   * 
   * @param row
   *          the row.
   * @param column
   *          the column.
   * @param width
   *          the width.
   * @param height
   *          the height.
   * @param widthResizable
   *          is resizable in width ?
   * @param heightResizable
   *          is resizable in height ?
   */
  public ViewConstraints(int row, int column, int width, int height,
      boolean widthResizable, boolean heightResizable) {
    super();
    this.row = row;
    this.column = column;
    this.width = width;
    this.height = height;
    this.widthResizable = widthResizable;
    this.heightResizable = heightResizable;
  }

  /**
   * Gets the column.
   * 
   * @return the column.
   */
  public int getColumn() {
    return column;
  }

  /**
   * Gets the height.
   * 
   * @return the height.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Gets the heightResizable.
   * 
   * @return the heightResizable.
   */
  public boolean isHeightResizable() {
    return heightResizable;
  }

  /**
   * Gets the row.
   * 
   * @return the row.
   */
  public int getRow() {
    return row;
  }

  /**
   * Gets the width.
   * 
   * @return the width.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Gets the widthResizable.
   * 
   * @return the widthResizable.
   */
  public boolean isWidthResizable() {
    return widthResizable;
  }

  /**
   * Sets the column.
   * 
   * @param column
   *          the column to set.
   */
  public void setColumn(int column) {
    this.column = column;
  }

  /**
   * Sets the height.
   * 
   * @param height
   *          the height to set.
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Sets the heightResizable.
   * 
   * @param heightResizable
   *          the heightResizable to set.
   */
  public void setHeightResizable(boolean heightResizable) {
    this.heightResizable = heightResizable;
  }

  /**
   * Sets the row.
   * 
   * @param row
   *          the row to set.
   */
  public void setRow(int row) {
    this.row = row;
  }

  /**
   * Sets the width.
   * 
   * @param width
   *          the width to set.
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * Sets the widthResizable.
   * 
   * @param widthResizable
   *          the widthResizable to set.
   */
  public void setWidthResizable(boolean widthResizable) {
    this.widthResizable = widthResizable;
  }
}
