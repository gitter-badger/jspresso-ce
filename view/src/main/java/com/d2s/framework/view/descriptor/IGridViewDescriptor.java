/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

import java.util.List;

/**
 * This public interface is implemented by any composite view descriptor
 * organizing their contained descriptors in a grid way.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IGridViewDescriptor extends ICompositeViewDescriptor {

  /**
   * Gets the list of view descriptors contained in this grid composite view.
   * 
   * @return the list of contained view descriptors.
   */
  List<IViewDescriptor> getChildViewDescriptors();

}
