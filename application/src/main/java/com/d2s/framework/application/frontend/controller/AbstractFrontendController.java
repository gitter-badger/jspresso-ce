/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IAction;
import com.d2s.framework.application.AbstractController;
import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.application.frontend.IFrontendController;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.application.model.SubModule;
import com.d2s.framework.application.view.descriptor.IModuleViewDescriptorFactory;
import com.d2s.framework.application.view.descriptor.basic.ModuleCardViewDescriptor;
import com.d2s.framework.binding.ConnectorSelectionEvent;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IConnectorSelectionListener;
import com.d2s.framework.binding.IConnectorSelector;
import com.d2s.framework.binding.IMvcBinder;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.security.SecurityHelper;
import com.d2s.framework.security.UserPrincipal;
import com.d2s.framework.util.descriptor.DefaultIconDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.ICompositeView;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IMapView;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.IViewFactory;
import com.d2s.framework.view.action.ActionMap;
import com.d2s.framework.view.descriptor.ISplitViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicSplitViewDescriptor;

/**
 * This class serves as base class for frontend (view) controllers.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the actual gui component type used.
 * @param <F>
 *            the actual icon type used.
 * @param <G>
 *            the actual action type used.
 */
public abstract class AbstractFrontendController<E, F, G> extends
    AbstractController implements IFrontendController<E, F, G> {

  /**
   * <code>MAX_LOGIN_RETRIES</code>.
   */
  protected static final int                    MAX_LOGIN_RETRIES = 3;

  private ActionMap                             actionMap;
  private ActionMap                             helpActionMap;

  private IBackendController                    backendController;
  private DefaultIconDescriptor                 controllerDescriptor;
  private CallbackHandler                       loginCallbackHandler;
  private String                                loginContextName;
  private Map<String, Module>                   modules;

  private String                                modulesMenuIconImageUrl;

  private IMvcBinder                            mvcBinder;

  private Map<String, ICompositeValueConnector> selectedModuleConnectors;
  private String                                selectedModuleName;

  private IAction                               startupAction;

  private IViewFactory<E, F, G>                 viewFactory;
  private IModuleViewDescriptorFactory          moduleViewDescriptorFactory;

  /**
   * Constructs a new <code>AbstractFrontendController</code> instance.
   */
  public AbstractFrontendController() {
    controllerDescriptor = new DefaultIconDescriptor();
    selectedModuleConnectors = new HashMap<String, ICompositeValueConnector>();
  }

  /**
   * Executes frontend actions and delegates backend actions execution to its
   * peer backend controller.
   * <p>
   * {@inheritDoc}
   */
  public boolean execute(IAction action, Map<String, Object> context) {
    if (action == null) {
      return true;
    }
    Map<String, Object> actionContext = getInitialActionContext();
    context.putAll(actionContext);
    try {
      SecurityHelper.checkAccess(getBackendController().getApplicationSession()
          .getSubject(), action, getTranslationProvider(), getLocale());
      if (action.isBackend()) {
        return executeBackend(action, context);
      }
      return executeFrontend(action, context);
    } catch (Throwable ex) {
      handleException(ex, context);
      return false;
    }
  }

  /**
   * Gets the actions.
   * 
   * @return the actions.
   */
  public ActionMap getActions() {
    return actionMap;
  }

  /**
   * Gets the help actions.
   * 
   * @return the help actions.
   */
  public ActionMap getHelpActions() {
    return helpActionMap;
  }

  /**
   * {@inheritDoc}
   */
  public IApplicationSession getApplicationSession() {
    return getBackendController().getApplicationSession();
  }

  /**
   * Gets the peer model controller.
   * 
   * @return the backend controller this frontend controller is attached to.
   */
  public IBackendController getBackendController() {
    return backendController;
  }

  /**
   * {@inheritDoc}
   */
  public String getDescription() {
    return controllerDescriptor.getDescription();
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    return controllerDescriptor.getI18nDescription(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    return controllerDescriptor.getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getIconImageURL() {
    return controllerDescriptor.getIconImageURL();
  }

  /**
   * Contains nothing.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> getInitialActionContext() {
    Map<String, Object> initialActionContext = new HashMap<String, Object>();
    initialActionContext.put(ActionContextConstants.FRONT_CONTROLLER, this);
    ICompositeValueConnector selectedModuleViewConnector = selectedModuleConnectors
        .get(getSelectedModuleName());
    if (selectedModuleViewConnector != null) {
      initialActionContext.put(ActionContextConstants.MODULE_VIEW_CONNECTOR,
          selectedModuleViewConnector);
    }
    return initialActionContext;
  }

  /**
   * Gets the locale.
   * 
   * @return the locale.
   */
  public Locale getLocale() {
    return getBackendController().getApplicationSession().getLocale();
  }

  /**
   * Gets the mvcBinder.
   * 
   * @return the mvcBinder.
   */
  public IMvcBinder getMvcBinder() {
    return mvcBinder;
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return controllerDescriptor.getName();
  }

  /**
   * {@inheritDoc}
   */
  public IAction getStartupAction() {
    return startupAction;
  }

  /**
   * Gets the viewFactory.
   * 
   * @return the viewFactory.
   */
  public IViewFactory<E, F, G> getViewFactory() {
    return viewFactory;
  }

  /**
   * {@inheritDoc}
   */
  public IEntity merge(IEntity entity, MergeMode mergeMode) {
    return getBackendController().merge(entity, mergeMode);
  }

  /**
   * {@inheritDoc}
   */
  public List<IEntity> merge(List<IEntity> entities, MergeMode mergeMode) {
    return getBackendController().merge(entities, mergeMode);
  }

  /**
   * Sets the actionMap.
   * 
   * @param actionMap
   *            the actionMap to set.
   */
  public void setActionMap(ActionMap actionMap) {
    this.actionMap = actionMap;
  }

  /**
   * Sets the helpActionMap.
   * 
   * @param helpActionMap
   *            the helpActionMap to set.
   */
  public void setHelpActionMap(ActionMap helpActionMap) {
    this.helpActionMap = helpActionMap;
  }

  /**
   * Sets the description.
   * 
   * @param description
   *            the description to set.
   */
  public void setDescription(String description) {
    controllerDescriptor.setDescription(description);
  }

  /**
   * Sets the iconImageURL.
   * 
   * @param iconImageURL
   *            the iconImageURL to set.
   */
  public void setIconImageURL(String iconImageURL) {
    controllerDescriptor.setIconImageURL(iconImageURL);
  }

  /**
   * Sets the loginContextName.
   * 
   * @param loginContextName
   *            the loginContextName to set.
   */
  public void setLoginContextName(String loginContextName) {
    this.loginContextName = loginContextName;
  }

  /**
   * Sets the modules. Modules are used by the frontend controller to give a
   * user access on the domain window.
   * 
   * @param modules
   *            the modules to set.
   */
  public void setModules(List<Module> modules) {
    this.modules = new LinkedHashMap<String, Module>();
    for (Module module : modules) {
      this.modules.put(module.getName(), module);
    }
  }

  /**
   * Sets the modulesMenuIconImageUrl.
   * 
   * @param modulesMenuIconImageUrl
   *            the modulesMenuIconImageUrl to set.
   */
  public void setModulesMenuIconImageUrl(String modulesMenuIconImageUrl) {
    this.modulesMenuIconImageUrl = modulesMenuIconImageUrl;
  }

  /**
   * Sets the mvcBinder.
   * 
   * @param mvcBinder
   *            the mvcBinder to set.
   */
  public void setMvcBinder(IMvcBinder mvcBinder) {
    this.mvcBinder = mvcBinder;
  }

  /**
   * Sets the name.
   * 
   * @param name
   *            the name to set.
   */
  public void setName(String name) {
    controllerDescriptor.setName(name);
  }

  /**
   * Sets the startupAction.
   * 
   * @param startupAction
   *            the startupAction to set.
   */
  public void setStartupAction(IAction startupAction) {
    this.startupAction = startupAction;
  }

  /**
   * Sets the viewFactory.
   * 
   * @param viewFactory
   *            the viewFactory to set.
   */
  public void setViewFactory(IViewFactory<E, F, G> viewFactory) {
    this.viewFactory = viewFactory;
  }

  /**
   * Binds to the backend controller and ask it to start.
   * <p>
   * {@inheritDoc}
   */
  public boolean start(IBackendController peerController, Locale startingLocale) {
    setBackendController(peerController);
    return peerController.start(startingLocale);
  }

  /**
   * {@inheritDoc}
   */
  public boolean stop() {
    return getBackendController().stop();
  }

  /**
   * Creates a new login callback handler.
   * 
   * @return a new login callback handler
   */
  protected abstract CallbackHandler createLoginCallbackHandler();

  /**
   * Creates a root module view.
   * 
   * @param moduleName
   *            the identifier of the module to create the view for.
   * @param moduleViewDescriptor
   *            the view descriptor of the module to render.
   * @param module
   *            the module to create the view for.
   * @return a view rendering the module.
   */
  protected IView<E> createModuleView(final String moduleName,
      IViewDescriptor moduleViewDescriptor, Module module) {
    BasicSplitViewDescriptor splitViewDescriptor = new BasicSplitViewDescriptor();
    splitViewDescriptor.setOrientation(ISplitViewDescriptor.HORIZONTAL);
    splitViewDescriptor.setName(moduleViewDescriptor.getName());
    //splitViewDescriptor.setDescription(moduleViewDescriptor.getDescription());
    splitViewDescriptor.setIconImageURL(moduleViewDescriptor.getIconImageURL());
    splitViewDescriptor.setMasterDetail(true);

    ModuleCardViewDescriptor modulePaneDescriptor = new ModuleCardViewDescriptor(
        module, moduleViewDescriptorFactory);

    splitViewDescriptor.setLeftTopViewDescriptor(moduleViewDescriptor);
    splitViewDescriptor.setRightBottomViewDescriptor(modulePaneDescriptor);

    ICompositeView<E> moduleView = (ICompositeView<E>) viewFactory.createView(
        splitViewDescriptor, this, getLocale());
    ((IConnectorSelector) moduleView.getConnector())
        .addConnectorSelectionListener(new IConnectorSelectionListener() {

          public void selectedConnectorChange(ConnectorSelectionEvent event) {
            selectedModuleChanged(moduleName, (ICompositeValueConnector) event
                .getSelectedConnector());
          }

        });
    for (IView<E> childView : moduleView.getChildren()) {
      if (childView instanceof IMapView) {
        for (Map.Entry<String, IView<E>> grandChildView : ((IMapView<E>) childView)
            .getChildrenMap().entrySet()) {
          mvcBinder.bind(grandChildView.getValue().getConnector(),
              getBackendController().createModelConnector(
                  moduleName + "_" + grandChildView.getKey(),
                  grandChildView.getValue().getDescriptor()
                      .getModelDescriptor()));
        }
      }
    }
    return moduleView;
  }

  private void selectedModuleChanged(String moduleName,
      ICompositeValueConnector selectedConnector) {
    selectedModuleConnectors.put(moduleName, selectedConnector);
    if (selectedConnector != null
        && selectedConnector.getConnectorValue() instanceof SubModule) {
      SubModule selectedModule = (SubModule) selectedConnector
          .getConnectorValue();
      if (!selectedModule.isStarted()
          && selectedModule.getStartupAction() != null) {
        execute(selectedModule.getStartupAction(), createEmptyContext());
        selectedModule.setStarted(true);
      }
    }
  }

  /**
   * Displays a module.
   * 
   * @param moduleName
   *            the module identifier.
   */
  protected abstract void displayModule(String moduleName);

  /**
   * Executes a backend action.
   * 
   * @param action
   *            the backend action to execute.
   * @param context
   *            the action execution context.
   * @return true if the action was succesfully executed.
   */
  protected boolean executeBackend(IAction action, Map<String, Object> context) {
    return getBackendController().execute(action, context);
  }

  /**
   * Executes a frontend action.
   * 
   * @param action
   *            the frontend action to execute.
   * @param context
   *            the action execution context.
   * @return true if the action was succesfully executed.
   */
  protected boolean executeFrontend(IAction action, Map<String, Object> context) {
    return action.execute(this, context);
  }

  /**
   * Gets the iconFactory.
   * 
   * @return the iconFactory.
   */
  protected IIconFactory<F> getIconFactory() {
    return viewFactory.getIconFactory();
  }

  /**
   * Gets the loginCallbackHandler.
   * 
   * @return the loginCallbackHandler.
   */
  protected CallbackHandler getLoginCallbackHandler() {
    if (loginCallbackHandler == null) {
      loginCallbackHandler = createLoginCallbackHandler();
    }
    return loginCallbackHandler;
  }

  /**
   * Gets the loginContextName.
   * 
   * @return the loginContextName.
   */
  protected String getLoginContextName() {
    return loginContextName;
  }

  /**
   * Whenever the loginContextName is not configured, creates a default subject.
   * 
   * @return the default Subject in case the login configuration is not set.
   */
  protected Subject getAnonymousSubject() {
    return SecurityHelper.createAnonymousSubject();
  }

  /**
   * Given a module name, this method returns the associated module.
   * 
   * @param moduleName
   *            the name of the module.
   * @return the selected module.
   */
  protected Module getModule(String moduleName) {
    if (modules != null) {
      return modules.get(moduleName);
    }
    return null;
  }

  /**
   * Returns the list of module names. This list defines the set of modules the
   * user have access to.
   * 
   * @return the list of module names.
   */
  protected List<String> getModuleNames() {
    if (modules != null) {
      return new ArrayList<String>(modules.keySet());
    }
    return Collections.<String> emptyList();
  }

  /**
   * Gets the modulesMenuIconImageUrl.
   * 
   * @return the modulesMenuIconImageUrl.
   */
  protected String getModulesMenuIconImageUrl() {
    return modulesMenuIconImageUrl;
  }

  /**
   * Gets the selectedModuleConnectors.
   * 
   * @return the selectedModuleConnectors.
   */
  protected Map<String, ICompositeValueConnector> getSelectedModuleConnectors() {
    return selectedModuleConnectors;
  }

  /**
   * Gets the selectedModuleName.
   * 
   * @return the selectedModuleName.
   */
  protected String getSelectedModuleName() {
    return selectedModuleName;
  }

  /**
   * This method installs the security subject into the application session.
   * 
   * @param subject
   *            the authenticated user subject.
   */
  protected void loginSuccess(Subject subject) {
    getBackendController().getApplicationSession().setSubject(subject);
    String userPreferredLanguageCode = (String) getBackendController()
        .getApplicationSession().getPrincipal().getCustomProperty(
            UserPrincipal.LANGUAGE_PROPERTY);
    if (userPreferredLanguageCode != null) {
      getBackendController().getApplicationSession().setLocale(
          new Locale(userPreferredLanguageCode));
    }
    if (modules != null) {
      for (Module module : modules.values()) {
        translateModule(module);
      }
      getBackendController().installModules(modules);
    }
  }

  /**
   * Sets the backend controller this controller is attached to.
   * 
   * @param backendController
   *            the backend controller to set.
   */
  protected void setBackendController(IBackendController backendController) {
    this.backendController = backendController;
  }

  /**
   * Sets the selectedModuleName.
   * 
   * @param selectedModuleName
   *            the selectedModuleName to set.
   */
  protected void setSelectedModuleName(String selectedModuleName) {
    this.selectedModuleName = selectedModuleName;
  }

  private void translateModule(Module module) {
    module.setI18nName(getTranslationProvider().getTranslation(
        module.getName(), getLocale()));
    module.setI18nDescription(getTranslationProvider().getTranslation(
        module.getDescription(), getLocale()));
    if (module.getSubModules() != null) {
      for (Module subModule : module.getSubModules()) {
        translateModule(subModule);
      }
    }
  }

  /**
   * Sets the moduleViewDescriptorFactory.
   * 
   * @param moduleViewDescriptorFactory
   *            the moduleViewDescriptorFactory to set.
   */
  public void setModuleViewDescriptorFactory(
      IModuleViewDescriptorFactory moduleViewDescriptorFactory) {
    this.moduleViewDescriptorFactory = moduleViewDescriptorFactory;
  }
}
