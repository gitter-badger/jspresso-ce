/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view;

import java.awt.Dimension;

/**
 * A factory for icons.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual icon class created.
 */
public interface IIconFactory<E> {

  /**
   * <code>SMALL_ICON_SIZE</code> is 16x16 dimension.
   */
  Dimension TINY_ICON_SIZE   = new Dimension(12, 12);
  /**
   * <code>SMALL_ICON_SIZE</code> is 16x16 dimension.
   */
  Dimension SMALL_ICON_SIZE  = new Dimension(16, 16);
  /**
   * <code>MEDIUM_ICON_SIZE</code> is 32x32 dimension.
   */
  Dimension MEDIUM_ICON_SIZE = new Dimension(32, 32);
  /**
   * <code>LARGE_ICON_SIZE</code> is 48x48 dimension.
   */
  Dimension LARGE_ICON_SIZE  = new Dimension(48, 48);

  /**
   * Creates an icon from an image url or get it from a local cache.
   * 
   * @param urlSpec
   *          the url of the image to be used on the icon.
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          nacessary to match the requested size.
   * @return the constructed icon.
   */
  E getIcon(String urlSpec, Dimension iconSize);
}
