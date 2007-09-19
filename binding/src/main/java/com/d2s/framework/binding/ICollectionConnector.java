/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import com.d2s.framework.util.event.ISelectable;
import com.d2s.framework.util.event.ISelectionChangeListener;

/**
 * This is the interface implemented by connectors on collections.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICollectionConnector extends ICompositeValueConnector,
    ISelectable, ISelectionChangeListener, ICollectionConnectorProvider {

  /**
   * Clones this connector.
   * 
   * @return the connector's clone.
   */
  ICollectionConnector clone();

  /**
   * Clones this connector.
   * 
   * @param newConnectorId
   *            the identifier of the clone connector
   * @return the connector's clone.
   */
  ICollectionConnector clone(String newConnectorId);

  /**
   * Creates a new collection element connector.
   * 
   * @param connectorId
   *            the identifier of the new created connector.
   * @return the created connector
   */
  IValueConnector createChildConnector(String connectorId);

  /**
   * Returns the connector at the given index in the collection.
   * 
   * @param index
   *            the index of the searched connector.
   * @return the searched connector or null if the collection is not large
   *         enough.
   */
  IValueConnector getChildConnector(int index);
}
