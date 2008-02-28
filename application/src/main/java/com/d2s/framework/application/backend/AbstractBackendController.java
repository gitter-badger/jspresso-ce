/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IAction;
import com.d2s.framework.application.AbstractController;
import com.d2s.framework.application.backend.entity.ApplicationSessionAwareProxyEntityFactory;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.application.backend.session.basic.BasicApplicationSession;
import com.d2s.framework.application.model.Workspace;
import com.d2s.framework.application.model.descriptor.WorkspaceDescriptor;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.model.IModelConnectorFactory;
import com.d2s.framework.model.component.IComponent;
import com.d2s.framework.model.datatransfer.ComponentTransferStructure;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.security.ISecurable;
import com.d2s.framework.security.SecurityHelper;
import com.d2s.framework.util.accessor.IAccessorFactory;

/**
 * Base class for backend application controllers. It provides the implementor
 * with commonly used accessors as well as a reference to the root model
 * descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractBackendController extends AbstractController
    implements IBackendController {

  private IApplicationSession                              applicationSession;
  private IModelConnectorFactory                           modelConnectorFactory;
  private IEntityFactory                                   entityFactory;
  private ComponentTransferStructure<? extends IComponent> transferStructure;

  private Map<String, IValueConnector>                     workspaceConnectors;

  /**
   * {@inheritDoc}
   */
  public void checkWorkspaceAccess(String workspaceName) {
    checkAccess((ISecurable) getWorkspaceConnector(workspaceName)
        .getConnectorValue());
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector createModelConnector(String id,
      IModelDescriptor modelDescriptor) {
    return modelConnectorFactory.createModelConnector(id, modelDescriptor);
  }

  /**
   * Directly delegates execution to the action after having completed its
   * execution context with the controller's initial context.
   * <p>
   * {@inheritDoc}
   */
  public boolean execute(IAction action, Map<String, Object> context) {
    if (action == null) {
      return true;
    }
    SecurityHelper.checkAccess(getApplicationSession().getSubject(), action,
        getTranslationProvider(), getLocale());
    Map<String, Object> actionContext = getInitialActionContext();
    if (context != null) {
      context.putAll(actionContext);
    }
    return action.execute(this, context);
  }

  /**
   * {@inheritDoc}
   */
  public IApplicationSession getApplicationSession() {
    return applicationSession;
  }

  /**
   * {@inheritDoc}
   */
  public IAccessorFactory getAccessorFactory() {
    return modelConnectorFactory.getAccessorFactory();
  }

  /**
   * {@inheritDoc}
   */
  public IEntityFactory getEntityFactory() {
    return entityFactory;
  }

  /**
   * Contains the current application session.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> getInitialActionContext() {
    Map<String, Object> initialActionContext = new HashMap<String, Object>();
    initialActionContext.put(ActionContextConstants.BACK_CONTROLLER, this);
    return initialActionContext;
  }

  /**
   * Gets the locale used by this controller. The locale is actually held by the
   * session.
   * 
   * @return locale used by this controller.
   */
  public Locale getLocale() {
    return applicationSession.getLocale();
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getWorkspaceConnector(String workspaceName) {
    return workspaceConnectors.get(workspaceName);
  }

  /**
   * Sets the model controller workspaces. These workspaces are not kept as-is.
   * Their connectors are.
   * 
   * @param workspaces
   *            A map containing the workspaces indexed by a well-known key used
   *            to bind them with their views.
   */
  public void installWorkspaces(Map<String, Workspace> workspaces) {
    workspaceConnectors = new HashMap<String, IValueConnector>();
    for (Map.Entry<String, Workspace> workspaceEntry : workspaces.entrySet()) {
      IModelDescriptor workspaceDescriptor;
      workspaceDescriptor = WorkspaceDescriptor.WORKSPACE_DESCRIPTOR;
      IValueConnector nextWorkspaceConnector = modelConnectorFactory
          .createModelConnector(workspaceEntry.getKey(), workspaceDescriptor);
      nextWorkspaceConnector.setConnectorValue(workspaceEntry.getValue());
      workspaceConnectors.put(workspaceEntry.getKey(), nextWorkspaceConnector);
    }
  }

  /**
   * {@inheritDoc}
   */
  public IEntity merge(IEntity entity, MergeMode mergeMode) {
    return getApplicationSession().merge(entity, mergeMode);
  }

  /**
   * {@inheritDoc}
   */
  public List<IEntity> merge(List<IEntity> entities, MergeMode mergeMode) {
    return getApplicationSession().merge(entities, mergeMode);
  }

  /**
   * {@inheritDoc}
   */
  public ComponentTransferStructure<? extends IComponent> retrieveComponents() {
    return transferStructure;
  }

  /**
   * Sets the applicationSession.
   * 
   * @param applicationSession
   *            the applicationSession to set.
   */
  public void setApplicationSession(IApplicationSession applicationSession) {
    if (!(applicationSession instanceof BasicApplicationSession)) {
      throw new IllegalArgumentException(
          "applicationSession must be a BasicApplicationSession.");
    }
    this.applicationSession = applicationSession;
    linkSessionArtifacts();
  }

  /**
   * Sets the modelConnectorFactory.
   * 
   * @param modelConnectorFactory
   *            the modelConnectorFactory to set.
   */
  public void setModelConnectorFactory(
      IModelConnectorFactory modelConnectorFactory) {
    this.modelConnectorFactory = modelConnectorFactory;
  }

  /**
   * Sets the entityFactory.
   * 
   * @param entityFactory
   *            the entityFactory to set.
   */
  public void setEntityFactory(IEntityFactory entityFactory) {
    if (!(entityFactory instanceof ApplicationSessionAwareProxyEntityFactory)) {
      throw new IllegalArgumentException(
          "entityFactory must be an ApplicationSessionAwareProxyEntityFactory.");
    }
    this.entityFactory = entityFactory;
    linkSessionArtifacts();
  }

  /**
   * {@inheritDoc}
   */
  public boolean start(Locale locale) {
    applicationSession.setLocale(locale);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean stop() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public void storeComponents(
      ComponentTransferStructure<? extends IComponent> components) {
    this.transferStructure = components;
  }

  private void linkSessionArtifacts() {
    if (getApplicationSession() != null && getEntityFactory() != null) {
      ((ApplicationSessionAwareProxyEntityFactory) getEntityFactory())
          .setApplicationSession(getApplicationSession());
      ((BasicApplicationSession) getApplicationSession())
          .setEntityFactory(getEntityFactory());
    }
  }
}
