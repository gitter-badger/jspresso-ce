/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of collections.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICollectionDescriptor extends IModelDescriptor,
    ICollectionDescriptorProvider {

  /**
   * Gets the <code>Collection</code> sub-interface implemented by the
   * described collection property (i.e <code>java.util.Set</code>,
   * <code>java.util.List</code>, ...).
   * 
   * @return the collection interface.
   */
  Class getCollectionInterface();

  /**
   * Gets the component descriptor of the elements contained in this collection.
   * 
   * @return The collection's component descriptor.
   */
  IComponentDescriptor getElementDescriptor();
}
