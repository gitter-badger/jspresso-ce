/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.controller.remote;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.application.frontend.command.remote.RemoteClipboardCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteFlashDisplayCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RSplitContainer;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.descriptor.EOrientation;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This is is the default implementation of a &quot;remotable&quot; frontend
 * controller. It will implement a 3-tier architecture. The remote controller
 * lives on server-side and communicates with generic UI engines that are
 * deployed on client side. As of now, the remote frontend controller is used by
 * the <b>Flex</b> and <b>qooxdoo</b> frontends. Communication happens through
 * the use of generic UI commands that are produced/consumed on both sides of
 * the network.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class DefaultRemoteController extends AbstractRemoteController {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultRemoteController.class);

  private Set<String>            workspaceViews;

  /**
   * Constructs a new {@code DefaultRemoteController} instance.
   */
  public DefaultRemoteController() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayFlashObject(String swfUrl, Map<String, String> flashContext, List<RAction> actions, String title,
                                 RComponent sourceComponent, Map<String, Object> context, Dimension dimension,
                                 boolean reuseCurrent) {
    super.displayModalDialog(context, reuseCurrent);
    RemoteFlashDisplayCommand flashCommand = new RemoteFlashDisplayCommand();
    flashCommand.setSwfUrl(swfUrl);
    flashCommand.setTitle(title);
    flashCommand.setActions(actions.toArray(new RAction[actions.size()]));
    flashCommand.setUseCurrent(reuseCurrent);
    List<String> paramNames = new ArrayList<>();
    List<String> paramValues = new ArrayList<>();
    for (Map.Entry<String, String> flashVar : flashContext.entrySet()) {
      paramNames.add(flashVar.getKey());
      paramValues.add(flashVar.getValue());
    }
    flashCommand.setParamNames(paramNames.toArray(new String[paramNames.size()]));
    flashCommand.setParamValues(paramValues.toArray(new String[paramValues.size()]));
    flashCommand.setDimension(dimension);
    registerCommand(flashCommand);
  }

  /**
   * Sends a remote workspace display command.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected void displayWorkspace(String workspaceName, boolean bypassModuleBoundaryActions) {
    displayWorkspace(workspaceName, bypassModuleBoundaryActions, true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean stop() {
    if (workspaceViews != null) {
      workspaceViews.clear();
    }
    return super.stop();
  }

  /**
   * Sets the workspace as selected and optionally notifies the remote peer.
   *
   * @param workspaceName
   *     the selected workspace name.
   * @param bypassModuleBoundaryActions
   *     should we bypass module onEnter/Exit actions ?
   * @param notifyRemote
   *     if true, a remote notification will be sent to the remote peer.
   */
  protected void displayWorkspace(String workspaceName, boolean bypassModuleBoundaryActions, boolean notifyRemote) {
    if (!ObjectUtils.equals(workspaceName, getSelectedWorkspaceName())) {
      super.displayWorkspace(workspaceName, bypassModuleBoundaryActions);
      if (workspaceViews == null) {
        workspaceViews = new HashSet<>();
      }
      RSplitContainer workspaceView = null;
      if (!workspaceViews.contains(workspaceName)) {
        workspaceView = new RSplitContainer(workspaceName + "_split");
        workspaceView.setOrientation(EOrientation.HORIZONTAL.toString());
        IViewDescriptor workspaceNavigatorViewDescriptor = getWorkspace(workspaceName).getViewDescriptor();
        IValueConnector workspaceConnector = getBackendController().getWorkspaceConnector(workspaceName);
        IView<RComponent> workspaceNavigator = createWorkspaceNavigator(workspaceName,
            workspaceNavigatorViewDescriptor);
        IView<RComponent> moduleAreaView = createModuleAreaView(workspaceName);
        workspaceView.setLeftTop(workspaceNavigator.getPeer());
        workspaceView.setRightBottom(moduleAreaView.getPeer());
        workspaceViews.add(workspaceName);
        getMvcBinder().bind(workspaceNavigator.getConnector(), workspaceConnector);
      }
      if (notifyRemote) {
        RemoteWorkspaceDisplayCommand workspaceDisplayCommand = new RemoteWorkspaceDisplayCommand();
        if (workspaceView != null) {
          workspaceDisplayCommand.setWorkspaceView(workspaceView);
        }
        workspaceDisplayCommand.setWorkspaceName(workspaceName);
        registerCommand(workspaceDisplayCommand);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setClipboardContent(String plainContent, String htmlContent) {
    RemoteClipboardCommand clipboardCommand = new RemoteClipboardCommand();
    clipboardCommand.setPlainContent(plainContent);
    clipboardCommand.setHtmlContent(htmlContent);
    registerCommand(clipboardCommand);
  }

}
