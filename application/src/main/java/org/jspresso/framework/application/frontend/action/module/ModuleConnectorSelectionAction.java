/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.module;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.Module;

/**
 * This action takes an indices array (
 * <code>ActionContextConstants.SELECTED_INDICES</code>) out of the action
 * context, and selects the corresponding child connectors of the currently
 * select one.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ModuleConnectorSelectionAction<E, F, G> extends
    FrontendAction<E, F, G> {

  /**
   * Selects indices on the module view collection connector based on the
   * <code>ActionContextConstants.SELECTED_INDICES</code> context value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    Module module = getModule(context);
    int[] selectedIndices = getSelectedIndices(context);
    if (selectedIndices.length > 0 && module != null) {
      int selectedModuleIndex = selectedIndices[0];
      if (module.getSubModules() != null
          && module.getSubModules().size() > selectedModuleIndex) {
        getController(context).displayModule(
            module.getSubModules().get(selectedModuleIndex));
      }
    }
    return super.execute(actionHandler, context);
  }
}