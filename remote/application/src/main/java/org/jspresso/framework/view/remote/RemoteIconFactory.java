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
package org.jspresso.framework.view.remote;

import java.awt.Dimension;

import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.util.uid.IGUIDGenerator;
import org.jspresso.framework.view.AbstractIconFactory;

/**
 * A factory to create (and cache) remote icons.
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
public class RemoteIconFactory extends AbstractIconFactory<RIcon> {

  private IGUIDGenerator       uidGenerator;

  /**
   * {@inheritDoc}
   */
  @Override
  protected RIcon createIcon(String urlSpec, Dimension iconSize) {
    if (urlSpec != null) {
      RIcon imageIcon = new RIcon(uidGenerator.generateGUID());
      imageIcon.setImageUrlSpec(urlSpec);
      imageIcon.setWidth((int) iconSize.getWidth());
      imageIcon.setHeight((int) iconSize.getHeight());
      return imageIcon;
    }
    return null;
  }

  
  /**
   * Sets the uidGenerator.
   * 
   * @param uidGenerator the uidGenerator to set.
   */
  public void setUidGenerator(IGUIDGenerator uidGenerator) {
    this.uidGenerator = uidGenerator;
  }
}
