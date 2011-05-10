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
package org.jspresso.framework.application.frontend.controller.wings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.swing.Action;
import javax.swing.JOptionPane;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.wings.components.SErrorDialog;
import org.jspresso.framework.util.exception.BusinessException;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.html.HtmlHelper;
import org.jspresso.framework.util.http.CookiePreferencesStore;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.util.preferences.IPreferencesStore;
import org.jspresso.framework.util.security.LoginUtils;
import org.jspresso.framework.util.wings.WingsUtil;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.wings.SBorderLayout;
import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SCardLayout;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.SDialog;
import org.wings.SDimension;
import org.wings.SFrame;
import org.wings.SGridBagLayout;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.SMenu;
import org.wings.SMenuBar;
import org.wings.SMenuItem;
import org.wings.SOptionPane;
import org.wings.SPanel;
import org.wings.SSpacer;
import org.wings.border.SEmptyBorder;
import org.wings.border.SLineBorder;
import org.wings.script.JavaScriptListener;
import org.wings.script.ScriptListener;
import org.wings.session.SessionManager;

/**
 * This is is the default implementation of the <b>WingS</b> frontend
 * controller. It will implement a 3-tier architecture through the WingS
 * internal architecture.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultWingsController extends
    AbstractFrontendController<SComponent, SIcon, Action> {

  private static final SDimension DIALOG_DIMENSION = new SDimension("800px",
                                                       "600px");

  private SPanel                  cardPanel;
  private SFrame                  controllerFrame;
  private SLabel                  statusBar;

  private Set<String>             workspaceViews;

  /**
   * {@inheritDoc}
   */
  public void displayFlashObject(String swfUrl,
      Map<String, String> flashContext, List<Action> actions, String title,
      SComponent sourceComponent, Map<String, Object> context,
      Dimension dimension, boolean reuseCurrent) {

    StringBuffer flashVars = new StringBuffer();
    for (Map.Entry<String, String> flashVar : flashContext.entrySet()) {
      flashVars.append("&").append(flashVar.getKey()).append("=")
          .append(flashVar.getValue());
    }
    int width = 600;
    int height = 600;
    if (dimension != null) {
      width = dimension.getWidth();
      height = dimension.getHeight();
    }
    String htmlText = "<html>"
        + "<body bgcolor=\"#ffffff\">"
        + "<OBJECT classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" "
        + "codebase=http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\" width=\""
        + width
        + "\" height=\""
        + height
        + "\" id=\"Column3D\" >"
        + "<param name=\"movie\" value=\""
        + swfUrl
        + "\" />"
        + "<param name=\"FlashVars\" value=\""
        + flashVars.toString()
        + "\">"
        + "<embed src=\""
        + swfUrl
        + "\" flashVars=\""
        + flashVars.toString()
        + "\" quality=\"high\" width=\""
        + width
        + "\" height=\""
        + height
        + "\" name=\"Column3D\" type=\"application/x-shockwave-flash\" "
        + "pluginspage=\"http://www.macromedia.com/go/getflashplayer\" />"
        + "</object>" + "</body>" + "</html>";
    SLabel embedder = new SLabel();
    embedder.setText(htmlText);
    displayModalDialog(embedder, actions, title, sourceComponent, context,
        dimension, reuseCurrent);
  }

  /**
   * {@inheritDoc}
   */
  public void displayModalDialog(SComponent mainView, List<Action> actions,
      String title, SComponent sourceComponent, Map<String, Object> context,
      Dimension dimension, boolean reuseCurrent) {
    super.displayModalDialog(context, reuseCurrent);
    final SDialog dialog;
    SContainer actionWindow = WingsUtil.getVisibleWindow(sourceComponent);
    if (reuseCurrent && actionWindow instanceof SDialog) {
      dialog = ((SDialog) actionWindow);
      dialog.removeAll();
    } else {
      SFrame window;
      if (sourceComponent != null) {
        window = sourceComponent.getParentFrame();
      } else {
        window = controllerFrame;
      }
      dialog = new SDialog(window, title, true);
      dialog.setDraggable(true);
    }

    SPanel buttonBox = new SPanel();
    buttonBox.setLayout(new SBoxLayout(dialog, SBoxLayout.X_AXIS));
    buttonBox.setBorder(new SEmptyBorder(new java.awt.Insets(5, 10, 5, 10)));

    SButton defaultButton = null;
    for (Action action : actions) {
      SButton actionButton = new SButton();
      actionButton.setAction(action);
      actionButton.setDisabledIcon(actionButton.getIcon());
      buttonBox.add(actionButton);
      buttonBox.add(new SSpacer(10, 10));
      if (defaultButton == null) {
        defaultButton = actionButton;
      }
    }
    SPanel actionPanel = new SPanel(new SBorderLayout());
    actionPanel.add(buttonBox, SBorderLayout.EAST);

    SPanel mainPanel = new SPanel(new SBorderLayout());
    if (dimension != null) {
      mainView.setPreferredSize(new SDimension(dimension.getWidth() + "px",
          dimension.getHeight() + "px"));
    } else {
      mainView.setPreferredSize(DIALOG_DIMENSION);
    }
    mainPanel.add(mainView, SBorderLayout.CENTER);
    mainPanel.add(actionPanel, SBorderLayout.SOUTH);
    dialog.add(mainPanel);
    if (defaultButton != null) {
      dialog.setDefaultButton(defaultButton);
    }
    dialog.setVisible(true);
  }

  /**
   * {@inheritDoc}
   */
  public void displayUrl(String urlSpec) {
    ScriptListener listener = new JavaScriptListener(null, null,
        "wingS.util.openLink('download','" + urlSpec + "',null);");
    SessionManager.getSession().getScriptManager().addScriptListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayWorkspace(String workspaceName) {
    if (!ObjectUtils.equals(workspaceName, getSelectedWorkspaceName())) {
      super.displayWorkspace(workspaceName);
      if (workspaceViews == null) {
        workspaceViews = new HashSet<String>();
      }
      if (!workspaceViews.contains(workspaceName)) {
        IViewDescriptor workspaceNavigatorViewDescriptor = getWorkspace(
            workspaceName).getViewDescriptor();
        IValueConnector workspaceConnector = getBackendController()
            .getWorkspaceConnector(workspaceName);
        IView<SComponent> workspaceNavigator = createWorkspaceNavigator(
            workspaceName, workspaceNavigatorViewDescriptor);
        IView<SComponent> moduleAreaView = createModuleAreaView(workspaceName);
        moduleAreaView.getPeer().setPreferredSize(SDimension.FULLAREA);

        // Split pane definition !!!
        SPanel workspaceView = new SPanel(new SGridBagLayout());
        workspaceView.setPreferredSize(SDimension.FULLAREA);

        Insets insets = new Insets(0, 0, 0, 0);

        workspaceNavigator.getPeer().setHorizontalAlignment(
            SConstants.LEFT_ALIGN);
        workspaceNavigator.getPeer().setVerticalAlignment(SConstants.TOP_ALIGN);
        double weightx = 0.0d;
        workspaceView.add(workspaceNavigator.getPeer(), new GridBagConstraints(
            0, 0, 1, 1, weightx, 1.0d, GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH, insets, 0, 0));

        moduleAreaView.getPeer().setHorizontalAlignment(SConstants.LEFT_ALIGN);
        moduleAreaView.getPeer().setVerticalAlignment(SConstants.TOP_ALIGN);
        int gridx = 1;
        int gridy = 0;
        workspaceView.add(moduleAreaView.getPeer(),
            new GridBagConstraints(gridx, gridy, GridBagConstraints.REMAINDER,
                GridBagConstraints.REMAINDER, 1.0d, 1.0d,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets,
                0, 0));
        // End split pane definition !!!

        workspaceViews.add(workspaceName);
        cardPanel.add(workspaceView, workspaceName);
        getMvcBinder().bind(workspaceNavigator.getConnector(),
            workspaceConnector);
      }
      ((SCardLayout) cardPanel.getLayout()).show(workspaceName);
      updateFrameTitle();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disposeModalDialog(SComponent sourceWidget,
      Map<String, Object> context) {
    super.disposeModalDialog(sourceWidget, context);
    SContainer actionWindow = WingsUtil.getVisibleWindow(sourceWidget);
    if (actionWindow instanceof SDialog) {
      ((SDialog) actionWindow).dispose();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean handleException(Throwable ex, Map<String, Object> context) {
    if (super.handleException(ex, context)) {
      return true;
    }
    SComponent sourceComponent = controllerFrame;
    if (ex instanceof SecurityException) {
      SOptionPane.showMessageDialog(sourceComponent,
          HtmlHelper.toHtml(HtmlHelper.emphasis(HtmlHelper.escapeForHTML(ex
              .getMessage()))), getTranslation("error", getLocale()),
          SOptionPane.ERROR_MESSAGE);
    } else if (ex instanceof BusinessException) {
      SOptionPane.showMessageDialog(sourceComponent, HtmlHelper
          .toHtml(HtmlHelper.emphasis(HtmlHelper
              .escapeForHTML(((BusinessException) ex).getI18nMessage(this,
                  getLocale())))), getTranslation("error", getLocale()),
          SOptionPane.ERROR_MESSAGE);
    } else if (ex instanceof DataIntegrityViolationException) {
      SOptionPane
          .showMessageDialog(
              sourceComponent,
              HtmlHelper.toHtml(HtmlHelper.emphasis(HtmlHelper.escapeForHTML(this
                  .getTranslation(
                      refineIntegrityViolationTranslationKey((DataIntegrityViolationException) ex),
                      getLocale())))), this
                  .getTranslation("error", getLocale()),
              SOptionPane.ERROR_MESSAGE);
    } else if (ex instanceof ConcurrencyFailureException) {
      SOptionPane.showMessageDialog(sourceComponent, HtmlHelper
          .toHtml(HtmlHelper.emphasis(HtmlHelper.escapeForHTML(getTranslation(
              "concurrency.error.description", getLocale())))),
          getTranslation("error", getLocale()), SOptionPane.ERROR_MESSAGE);
    } else {
      ex.printStackTrace();
      SOptionPane.showMessageDialog(sourceComponent,
          String.valueOf(ex.getMessage()),
          getTranslation("error", getLocale()), SOptionPane.ERROR_MESSAGE);
      SErrorDialog dialog = SErrorDialog.createInstance(sourceComponent, this,
          getLocale());
      dialog.setMessageIcon(getIconFactory().getErrorIcon(
          getIconFactory().getMediumIconSize()));
      dialog.setTitle(getTranslation("error", getLocale()));
      dialog.setMessage(HtmlHelper.toHtml(HtmlHelper.emphasis(HtmlHelper
          .escapeForHTML(ex.getLocalizedMessage()))));
      dialog.setDetails(ex);
      dialog.setVisible(true);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public void popupInfo(SComponent sourceComponent, String title,
      @SuppressWarnings("unused") String iconImageUrl, String message) {
    SOptionPane.showMessageDialog(sourceComponent, message, title,
        SOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * {@inheritDoc}
   */
  public void popupOkCancel(SComponent sourceComponent, String title,
      @SuppressWarnings("unused") String iconImageUrl, String message,
      final IAction okAction, final IAction cancelAction,
      final Map<String, Object> context) {
    SOptionPane.showConfirmDialog(sourceComponent, message, title,
        JOptionPane.WARNING_MESSAGE, new ActionListener() {

          public void actionPerformed(ActionEvent e) {
            IAction nextAction = null;
            if (SOptionPane.OK_ACTION.equals(e.getActionCommand())) {
              nextAction = okAction;
            } else if (SOptionPane.CANCEL_ACTION.equals(e.getActionCommand())) {
              nextAction = cancelAction;
            }
            if (nextAction != null) {
              execute(nextAction, context);
            }
          }
        });
  }

  /**
   * {@inheritDoc}
   */
  public void popupYesNo(SComponent sourceComponent, String title,
      @SuppressWarnings("unused") String iconImageUrl, String message,
      final IAction yesAction, final IAction noAction,
      final Map<String, Object> context) {
    SOptionPane.showYesNoDialog(sourceComponent, message, title,
        new ActionListener() {

          public void actionPerformed(ActionEvent e) {
            IAction nextAction = null;
            if (SOptionPane.YES_ACTION.equals(e.getActionCommand())) {
              nextAction = yesAction;
            } else if (SOptionPane.NO_ACTION.equals(e.getActionCommand())) {
              nextAction = noAction;
            }
            if (nextAction != null) {
              execute(nextAction, context);
            }
          }

        });
  }

  /**
   * {@inheritDoc}
   */
  public void popupYesNoCancel(SComponent sourceComponent, String title,
      @SuppressWarnings("unused") String iconImageUrl, String message,
      final IAction yesAction, final IAction noAction,
      final IAction cancelAction, final Map<String, Object> context) {
    SOptionPane.showConfirmDialog(sourceComponent, message, title,
        SOptionPane.YES_NO_CANCEL_OPTION, new ActionListener() {

          public void actionPerformed(ActionEvent e) {
            IAction nextAction = null;
            if (SOptionPane.YES_ACTION.equals(e.getActionCommand())) {
              nextAction = yesAction;
            } else if (SOptionPane.NO_ACTION.equals(e.getActionCommand())) {
              nextAction = noAction;
            } else if (SOptionPane.CANCEL_ACTION.equals(e.getActionCommand())) {
              nextAction = cancelAction;
            }
            if (nextAction != null) {
              execute(nextAction, context);
            }
          }

        }, null);
  }

  /**
   * Creates the initial view from the root view descriptor, then a SFrame
   * containing this view and presents it to the user.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean start(IBackendController backendController,
      Locale clientLocale, TimeZone clientTimezone) {
    if (super.start(backendController, clientLocale, clientTimezone)) {
      initLoginProcess();
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean stop() {
    if (super.stop()) {
      controllerFrame.getSession().exit();
      return true;
    }
    return false;
  }

  private SMenuBar createApplicationMenuBar() {
    SMenuBar applicationMenuBar = new SMenuBar();
    applicationMenuBar.setBorder(new SLineBorder(Color.LIGHT_GRAY));
    List<SMenu> workspaceMenus = createMenus(createWorkspaceActionMap(), true);
    if (workspaceMenus != null) {
      for (SMenu workspaceMenu : workspaceMenus) {
        applicationMenuBar.add(workspaceMenu);
      }
    }
    List<SMenu> navigationMenus = createMenus(getNavigationActions(), false);
    if (navigationMenus != null) {
      for (SMenu navigationMenu : navigationMenus) {
        applicationMenuBar.add(navigationMenu);
      }
    }
    List<SMenu> actionMenus = createMenus(getActionMap(), false);
    if (actionMenus != null) {
      for (SMenu actionMenu : actionMenus) {
        applicationMenuBar.add(actionMenu);
      }
    }
    List<SMenu> helpActionMenus = createMenus(getHelpActions(), true);
    if (helpActionMenus != null) {
      for (SMenu helpActionMenu : helpActionMenus) {
        helpActionMenu.setHorizontalAlignment(SConstants.RIGHT_ALIGN);
        applicationMenuBar.add(helpActionMenu);
      }
    }
    return applicationMenuBar;
  }

  private void createControllerFrame() {
    controllerFrame = new SFrame();
    String w = "95%";
    String h = "768px";
    if (getFrameWidth() != null) {
      w = getFrameWidth().intValue() + "px";
    }
    if (getFrameHeight() != null) {
      h = getFrameHeight().intValue() + "px";
    }
    controllerFrame.setPreferredSize(new SDimension(w, h));
    controllerFrame.getContentPane().setPreferredSize(SDimension.FULLAREA);
    cardPanel = new SPanel(new SCardLayout());
    cardPanel.setPreferredSize(SDimension.FULLAREA);
    controllerFrame.getContentPane().add(cardPanel, SBorderLayout.CENTER);

    statusBar = new SLabel();
    statusBar.setBorder(new SLineBorder(1));
    statusBar.setVisible(false);
    controllerFrame.getContentPane().add(statusBar, BorderLayout.SOUTH);

    updateFrameTitle();
    controllerFrame.setVisible(true);
  }

  private SMenu createMenu(ActionList actionList) {
    SMenu menu = new SMenu(actionList.getI18nName(this, getLocale()));
    if (actionList.getDescription() != null) {
      menu.setToolTipText(actionList.getI18nDescription(this, getLocale())
          + IActionFactory.TOOLTIP_ELLIPSIS);
    }
    menu.setIcon(getIconFactory().getIcon(actionList.getIconImageURL(),
        getIconFactory().getSmallIconSize()));
    for (SMenuItem menuItem : createMenuItems(actionList)) {
      menu.add(menuItem);
    }
    return menu;
  }

  private SMenuItem createMenuItem(IDisplayableAction action) {
    return new SMenuItem(getViewFactory().getActionFactory().createAction(
        action, this, null, getLocale()));
  }

  private List<SMenuItem> createMenuItems(ActionList actionList) {
    List<SMenuItem> menuItems = new ArrayList<SMenuItem>();
    for (IDisplayableAction action : actionList.getActions()) {
      if (isAccessGranted(action)) {
        try {
          pushToSecurityContext(action);
          menuItems.add(createMenuItem(action));
        } finally {
          restoreLastSecurityContextSnapshot();
        }
      }
    }
    return menuItems;
  }

  @SuppressWarnings("null")
  private List<SMenu> createMenus(ActionMap actionMap, boolean useSeparator) {
    List<SMenu> menus = new ArrayList<SMenu>();
    if (actionMap != null && isAccessGranted(actionMap)) {
      try {
        pushToSecurityContext(actionMap);
        SMenu menu = null;
        for (ActionList actionList : actionMap.getActionLists(this)) {
          if (isAccessGranted(actionList)) {
            try {
              pushToSecurityContext(actionList);
              if (!useSeparator || menus.isEmpty()) {
                menu = createMenu(actionList);
                menus.add(menu);
              } else {
                menu.addSeparator();
                // SMenuItem separator = new SMenuItem("---------");
                // separator.setBorder(new SLineBorder(1));
                // menu.add(separator);

                for (SMenuItem menuItem : createMenuItems(actionList)) {
                  menu.add(menuItem);
                }
              }
            } finally {
              restoreLastSecurityContextSnapshot();
            }
          }
        }
      } finally {
        restoreLastSecurityContextSnapshot();
      }
    }
    return menus;
  }

  private void initLoginProcess() {
    createControllerFrame();
    if (getLoginContextName() == null) {
      performLogin();
      updateControllerFrame();
      execute(getStartupAction(), getInitialActionContext());
      return;
    }

    IView<SComponent> loginView = createLoginView();

    // Login dialog
    final SDialog dialog = new SDialog(controllerFrame,
        getLoginViewDescriptor().getI18nName(this, getLocale()), true);
    dialog.setDraggable(true);

    SPanel buttonBox = new SPanel(new SBoxLayout(dialog, SBoxLayout.X_AXIS));
    buttonBox.setBorder(new SEmptyBorder(new java.awt.Insets(5, 10, 5, 10)));

    SButton loginButton = new SButton(getTranslation("ok", getLocale()));
    loginButton.setIcon(getIconFactory().getOkYesIcon(
        getIconFactory().getSmallIconSize()));
    loginButton.addActionListener(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
        if (performLogin()) {
          dialog.dispose();
          updateControllerFrame();
          execute(getStartupAction(), getInitialActionContext());
        } else {
          SOptionPane.showMessageDialog(dialog,
              getTranslation(LoginUtils.LOGIN_FAILED, getLocale()),
              getTranslation("error", getLocale()), SOptionPane.ERROR_MESSAGE);
        }
      }
    });
    buttonBox.add(loginButton);
    dialog.setDefaultButton(loginButton);

    SPanel actionPanel = new SPanel(new SBorderLayout());
    actionPanel.add(buttonBox, SBorderLayout.EAST);

    SPanel mainPanel = new SPanel(new SBorderLayout());
    mainPanel.add(
        new SLabel(getTranslation(LoginUtils.CRED_MESSAGE, getLocale())),
        SBorderLayout.NORTH);
    mainPanel.add(loginView.getPeer(), SBorderLayout.CENTER);
    mainPanel.add(actionPanel, SBorderLayout.SOUTH);
    dialog.add(mainPanel);

    dialog.setVisible(true);
  }

  private void updateControllerFrame() {
    controllerFrame.getContentPane().add(createApplicationMenuBar(),
        SBorderLayout.NORTH);
    updateFrameTitle();
  }

  private void updateFrameTitle() {
    String workspaceName = getSelectedWorkspaceName();
    if (workspaceName != null) {
      controllerFrame.setTitle(getWorkspace(getSelectedWorkspaceName())
          .getViewDescriptor().getI18nDescription(this, getLocale())
          + " - "
          + getI18nName(this, getLocale()));
    } else {
      controllerFrame.setTitle(getI18nName(this, getLocale()));
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setStatusInfo(String statusInfo) {
    if (statusInfo != null && statusInfo.length() > 0) {
      statusBar.setText(statusInfo);
      statusBar.setVisible(true);
    } else {
      statusBar.setVisible(false);
    }
  }

  /**
   * Returns a preference store based pon Java preferences API.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IPreferencesStore createClientPreferencesStore() {
    return new CookiePreferencesStore();
  }
}
