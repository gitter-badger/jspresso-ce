/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import com.d2s.framework.view.descriptor.IListViewDescriptor;
import com.d2s.framework.view.descriptor.ITreeLevelDescriptor;

/**
 * Default implementation of a tree level descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicTreeLevelDescriptor implements ITreeLevelDescriptor {

  private IListViewDescriptor nodeGroupDescriptor;

  /**
   * {@inheritDoc}
   */
  public IListViewDescriptor getNodeGroupDescriptor() {
    return nodeGroupDescriptor;
  }

  /**
   * Sets the nodeGroupDescriptor.
   * 
   * @param nodeGroupDescriptor
   *            the nodeGroupDescriptor to set.
   */
  public void setNodeGroupDescriptor(IListViewDescriptor nodeGroupDescriptor) {
    this.nodeGroupDescriptor = nodeGroupDescriptor;
  }
}
