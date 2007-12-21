/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util;

/**
 * Implementations of this interface are designed to provide image urls based on
 * an object.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IIconImageURLProvider {

  /**
   * Gets the image url for the user object passed as parameter.
   * 
   * @param userObject
   *            the user object to get the image url for.
   * @return the looked up image url or null if none.
   */
  String getIconImageURLForObject(Object userObject);
}
