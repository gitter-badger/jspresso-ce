/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.resources;

/**
 * Base implementation class for web resources.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractResource implements IResource {

  private String mimeType;

  /**
   * Constructs a new <code>AbstractResource</code> instance.
   * 
   * @param mimeType
   *            the mime type of the resource.
   */
  public AbstractResource(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * {@inheritDoc}
   */
  public String getMimeType() {
    return mimeType;
  }
}
