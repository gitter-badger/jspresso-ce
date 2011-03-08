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
package org.jspresso.framework.security;


/**
 * This interface is implemented by Jspresso security handlers.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ISecurityHandler {

  /**
   * Checks authorization for secured access. It shoud throw a SecurityException
   * whenever access should not be granted.
   * 
   * @param securable
   *          the id of the secured access to check.
   */
  void checkAccess(ISecurable securable);

  /**
   * Checks authorization for secured access.
   * 
   * @param securable
   *          the id of the secured access to check.
   * @return true if access is granted.
   */
  boolean isAccessGranted(ISecurable securable);

}
