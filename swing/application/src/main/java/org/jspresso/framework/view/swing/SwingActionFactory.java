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
package org.jspresso.framework.view.swing;

import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.view.ControllerAwareActionFactory;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * A swing action factory.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SwingActionFactory extends
    ControllerAwareActionFactory<Action, JComponent, Icon> {

  /**
   * {@inheritDoc}
   */
  @Override
  public Action createAction(IAction action, Dimension dimension,
      IActionHandler actionHandler, IView<JComponent> view, Locale locale) {
    Dimension d = dimension;
    if (d == null) {
      d = getIconFactory().getTinyIconSize();
    }
    Action swingAction = new ActionAdapter(action, d, actionHandler, view,
        locale);
    if (action instanceof IDisplayableAction) {
      attachActionGates(((IDisplayableAction) action), actionHandler, view,
          swingAction);
    }
    return swingAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActionEnabled(Action action, boolean enabled) {
    action.setEnabled(enabled);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActionName(Action action, String name) {
    action.putValue(Action.NAME, name);
  }

  private final class ActionAdapter extends AbstractAction {

    private static final long serialVersionUID = 5819377672533326496L;

    private IAction           action;
    private IActionHandler    actionHandler;
    private IView<JComponent> view;

    /**
     * Constructs a new <code>ActionAdapter</code> instance.
     * 
     * @param action
     * @param dimension
     * @param actionHandler
     * @param view
     * @param locale
     */
    public ActionAdapter(IAction action, Dimension dimension,
        IActionHandler actionHandler, IView<JComponent> view, Locale locale) {
      this.action = action;
      this.actionHandler = actionHandler;
      this.view = view;
      if (action instanceof IDisplayableAction) {
        putValue(Action.NAME,
            ((IDisplayableAction) action).getI18nName(actionHandler, locale));
        putValue(Action.ACTION_COMMAND_KEY, "");
        String i18nDescription = ((IDisplayableAction) action)
            .getI18nDescription(actionHandler, locale);
        if (i18nDescription != null && i18nDescription.length() > 1) {
          putValue(Action.SHORT_DESCRIPTION, i18nDescription + TOOLTIP_ELLIPSIS);
        }
        putValue(
            Action.SMALL_ICON,
            getIconFactory().getIcon(
                ((IDisplayableAction) action).getIconImageURL(), dimension));
        if (((IDisplayableAction) action).getMnemonicAsString() != null) {
          putValue(
              Action.MNEMONIC_KEY,
              new Integer(KeyStroke.getKeyStroke(
                  ((IDisplayableAction) action).getMnemonicAsString())
                  .getKeyCode()));
        }
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      if (actionHandler != null) {
        IValueConnector viewConnector = null;
        if (view != null) {
          viewConnector = view.getConnector();
        }
        Map<String, Object> actionContext = createActionContext(actionHandler,
            view, viewConnector, e.getActionCommand(),
            (JComponent) e.getSource());
        actionHandler.execute(action, actionContext);
      }
    }
  }

}
