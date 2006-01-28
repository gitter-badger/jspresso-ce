/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.List;

import com.d2s.framework.view.descriptor.IGridViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of a grid view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicGridViewDescriptor extends BasicCompositeViewDescriptor
    implements IGridViewDescriptor {

  private List<IViewDescriptor> viewDescriptors;

  /**
   * {@inheritDoc}
   */
  public List<IViewDescriptor> getChildViewDescriptors() {
    return viewDescriptors;
  }

  /**
   * Sets the viewDescriptors.
   * 
   * @param viewDescriptors
   *          the viewDescriptors to set.
   */
  public void setViewDescriptors(List<IViewDescriptor> viewDescriptors) {
    this.viewDescriptors = viewDescriptors;
  }

}
