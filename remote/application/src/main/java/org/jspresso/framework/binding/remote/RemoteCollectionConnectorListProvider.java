/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.binding.basic.BasicCollectionConnectorListProvider;
import org.jspresso.framework.util.remote.IRemoteServerPeer;
import org.jspresso.framework.util.uid.IGUIDGenerator;

/**
 * The server peer of a remote collection connector list provider.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteCollectionConnectorListProvider extends
    BasicCollectionConnectorListProvider implements IRemoteServerPeer {

  private IGUIDGenerator uidGenerator;
  private String                     uid;

  /**
   * Constructs a new <code>RemoteCollectionConnectorListProvider</code>
   * instance.
   * 
   * @param id
   *          the connector id.
   * @param uidGenerator
   *          the uid generator.
   */
  public RemoteCollectionConnectorListProvider(String id, IGUIDGenerator uidGenerator) {
    super(id);
    this.uid = uidGenerator.generateGUID();
    this.uidGenerator = uidGenerator;
  }

  /**
   * Gets the uid.
   * 
   * @return the uid.
   */
  public String getUid() {
    return uid;
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
    clonedConnector.uid = uidGenerator.generateGUID();
    return clonedConnector;
  }
}
