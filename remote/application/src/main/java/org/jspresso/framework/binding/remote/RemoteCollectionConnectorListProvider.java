/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.binding.remote;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.basic.BasicCollectionConnectorListProvider;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.remote.IRemotePeer;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;

/**
 * The server peer of a remote collection connector list provider.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteCollectionConnectorListProvider extends
    BasicCollectionConnectorListProvider implements IRemotePeer,
    IRemoteStateOwner, IPermIdSource {

  private String                    permId;
  private RemoteConnectorFactory    connectorFactory;
  private String                    guid;
  private RemoteCompositeValueState state;

  /**
   * Constructs a new <code>RemoteCollectionConnectorListProvider</code>
   * instance.
   * 
   * @param id
   *          the connector id.
   * @param connectorFactory
   *          the remote connector factory.
   */
  public RemoteCollectionConnectorListProvider(String id,
      RemoteConnectorFactory connectorFactory) {
    super(id);
    this.guid = connectorFactory.generateGUID();
    this.connectorFactory = connectorFactory;
    connectorFactory.register(this);
  }

  /**
   * Returns the actual connector value.
   * <p>
   * {@inheritDoc}
   */
  public Object actualValue() {
    return getConnectorValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteCollectionConnectorListProvider clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteCollectionConnectorListProvider clone(String newConnectorId) {
    RemoteCollectionConnectorListProvider clonedConnector = (RemoteCollectionConnectorListProvider) super
        .clone(newConnectorId);
    clonedConnector.guid = connectorFactory.generateGUID();
    clonedConnector.state = null;
    connectorFactory.attachListeners(clonedConnector);
    connectorFactory.register(clonedConnector);
    return clonedConnector;
  }

  /**
   * Gets the permId.
   * 
   * @return the permId.
   */
  public String getPermId() {
    if (permId != null) {
      return permId;
    }
    return getId();
  }

  /**
   * Gets the guid.
   * 
   * @return the guid.
   */
  public String getGuid() {
    return guid;
  }

  /**
   * {@inheritDoc}
   */
  public RemoteCompositeValueState getState() {
    if (state == null) {
      state = createState();
      synchRemoteState();
    }
    return state;
  }

  /**
   * Sets the permId.
   * 
   * @param permId
   *          the permId to set.
   */
  public void setPermId(String permId) {
    this.permId = permId;
  }

  /**
   * {@inheritDoc}
   */
  public void synchRemoteState() {
    RemoteCompositeValueState currentState = getState();
    currentState.setValue(getDisplayValue());
    currentState.setReadable(isReadable());
    currentState.setWritable(isWritable());
    currentState.setDescription(getDisplayDescription());
    currentState.setIconImageUrl(ResourceProviderServlet
        .computeImageResourceDownloadUrl(getDisplayIconImageUrl(),
            RIcon.DEFAULT_DIM));
  }

  /**
   * Creates a new state instance rerpesenting this connector.
   * 
   * @return the newly created state.
   */
  protected RemoteCompositeValueState createState() {
    RemoteCompositeValueState createdState = connectorFactory
        .createRemoteCompositeValueState(getGuid(), getPermId());
    List<RemoteValueState> children = new ArrayList<RemoteValueState>();
    for (ICollectionConnector childConnector : getCollectionConnectors()) {
      if (childConnector instanceof IRemoteStateOwner) {
        children.add(((IRemoteStateOwner) childConnector).getState());
      }
    }
    createdState.setChildren(children);
    return createdState;
  }
}
