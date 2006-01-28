/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.Collection;

/**
 * This is a simple connector implementation whic allows the management of child
 * connectors. It can be used for straight view connector implementation.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */

public abstract class AbstractCompositeValueConnector extends
    AbstractValueConnector implements IRenderableCompositeValueConnector,
    IConnectorMapProvider {

  private IConnectorMap             childConnectors;
  private ChildConnectorSupport     childConnectorSupport;
  private ConnectorSelectionSupport connectorSelectionSupport;
  private boolean                   tracksChildrenSelection;
  private String                    renderingChildConnectorId;

  /**
   * Constructs a new <code>AbstractCompositeValueConnector</code>.
   * 
   * @param id
   *          the connector identifier
   */
  public AbstractCompositeValueConnector(String id) {
    super(id);
    childConnectorSupport = new ChildConnectorSupport(this);
    connectorSelectionSupport = new ConnectorSelectionSupport();
    tracksChildrenSelection = false;
    childConnectors = new ConnectorMap(this);
  }

  /**
   * Returns the connector map of the connectors contained in this connector.
   * This method should return null if no connector are child in this connector.
   * 
   * @return the connector map of the child connectors
   */
  public IConnectorMap getConnectorMap() {
    return childConnectors;
  }

  /**
   * Adds a new child connector.
   * 
   * @param connector
   *          the connector to be added as composite.
   */
  public void addChildConnector(IValueConnector connector) {
    addChildConnector(connector.getId(), connector);
  }

  /**
   * Adds a new child connector using a specified storage key.
   * 
   * @param storageKey
   *          the key to use to store the child connector. It may be different
   *          from its id.
   * @param connector
   *          the connector to be added as composite.
   */
  protected void addChildConnector(String storageKey, IValueConnector connector) {
    childConnectors.addConnector(storageKey, connector);
  }

  /**
   * Removes a child connector.
   * 
   * @param connector
   *          the connector to be removed.
   */
  protected void removeChildConnector(IValueConnector connector) {
    childConnectors.removeConnector(connector.getId());
    connector.setParentConnector(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractCompositeValueConnector clone(String newConnectorId) {
    AbstractCompositeValueConnector clonedConnector = (AbstractCompositeValueConnector) super
        .clone(newConnectorId);
    clonedConnector.childConnectors = new ConnectorMap(clonedConnector);
    clonedConnector.childConnectorSupport = new ChildConnectorSupport(
        clonedConnector);
    clonedConnector.connectorSelectionSupport = new ConnectorSelectionSupport();
    clonedConnector.tracksChildrenSelection = tracksChildrenSelection;
    for (String connectorKey : getChildConnectorKeys()) {
      clonedConnector
          .addChildConnector(getChildConnector(connectorKey).clone());
    }
    clonedConnector.renderingChildConnectorId = renderingChildConnectorId;
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractCompositeValueConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getChildConnector(String connectorKey) {
    return childConnectorSupport.getChildConnector(connectorKey);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getChildConnectorKeys() {
    return childConnectorSupport.getChildConnectorKeys();
  }

  /**
   * {@inheritDoc}
   */
  public int getChildConnectorCount() {
    return getChildConnectorKeys().size();
  }

  /**
   * Uses the value to compute the string representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    IValueConnector renderingConnector = getRenderingConnector();
    if (renderingConnector != null) {
      if (renderingConnector.getConnectorValue() != null) {
        return renderingConnector.getConnectorValue().toString();
      }
      return "";
    }
    Object value = getConnectorValue();
    if (value != null) {
      return value.toString();
    }
    return "";
  }

  /**
   * Sets the renderingChildConnectorId.
   * 
   * @param renderingChildConnectorId
   *          the renderingChildConnectorId to set.
   */
  public void setRenderingChildConnectorId(String renderingChildConnectorId) {
    this.renderingChildConnectorId = renderingChildConnectorId;
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getRenderingConnector() {
    if (renderingChildConnectorId != null) {
      return getChildConnector(renderingChildConnectorId);
    }
    return null;
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement <code>IConnectorSelector</code>.
   * 
   * @param listener
   *          the listener to add.
   */
  protected void implAddConnectorSelectionListener(
      IConnectorSelectionListener listener) {
    connectorSelectionSupport.addConnectorSelectionListener(listener);
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement <code>IConnectorSelector</code>.
   * 
   * @param listener
   *          the listener to remove.
   */
  protected void implRemoveConnectorSelectionListener(
      IConnectorSelectionListener listener) {
    connectorSelectionSupport.removeConnectorSelectionListener(listener);
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement <code>IConnectorSelector</code>.
   * 
   * @param selectedConnector
   *          the newly selected connector or null.
   */
  protected void implFireSelectedConnectorChange(
      IValueConnector selectedConnector) {
    implFireSelectedConnectorChange(new ConnectorSelectionEvent(this,
        selectedConnector));
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement <code>IConnectorSelector</code>.
   * 
   * @param evt
   *          the connector selection event to propagate.
   */
  protected void implFireSelectedConnectorChange(ConnectorSelectionEvent evt) {
    if (evt.getSource() == this || tracksChildrenSelection) {
      connectorSelectionSupport.fireSelectedConnectorChange(evt);
    }
    IValueConnector parentConnector = getParentConnector();
    while (parentConnector != null
        && !(parentConnector instanceof IConnectorSelector)) {
      parentConnector = parentConnector.getParentConnector();
    }
    if (parentConnector != null) {
      ((IConnectorSelector) parentConnector).fireSelectedConnectorChange(evt);
    }
  }

  /**
   * Utility implementation to factorize method support. This should only be
   * used by subclasses which implement <code>IConnectorSelector</code>.
   * 
   * @param tracksChildren
   *          the tracksChildrenSelection to set.
   */
  protected void implSetTracksChildrenSelection(boolean tracksChildren) {
    this.tracksChildrenSelection = tracksChildren;
  }

  /**
   * Gets the tracksChildrenSelection.
   * 
   * @return the tracksChildrenSelection.
   */
  protected boolean isTracksChildrenSelection() {
    return tracksChildrenSelection;
  }
}
