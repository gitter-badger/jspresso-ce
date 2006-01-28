/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.lov;

import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.IController;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.application.frontend.action.ulc.std.DialogOkAction;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.bean.BeanRefPropertyConnector;
import com.d2s.framework.model.entity.IEntity;

/**
 * Sets the selected entity as the value of the source view connector (which
 * will propagate to the backend).
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class OkLovAction extends DialogOkAction {

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> execute(IActionHandler actionHandler) {
    Map<String, Object> result = new HashMap<String, Object>();
    ICollectionConnector resultConnector = ((ICollectionConnectorProvider) ((ICompositeValueConnector) getViewConnector())
        .getChildConnector(BeanRefPropertyConnector.THIS_PROPERTY))
        .getCollectionConnector();
    int[] resultSelectedIndices = resultConnector.getSelectedIndices();
    if (resultSelectedIndices != null && resultSelectedIndices.length > 0) {
      IValueConnector sourceViewConnector = (IValueConnector) getContext().get(
          ActionContextConstants.SOURCE_VIEW_CONNECTOR);
      // the following will force a connector value change event.
      sourceViewConnector.setConnectorValue(null);
      IEntity selectedEntity = (IEntity) resultConnector.getChildConnector(
          resultSelectedIndices[0]).getConnectorValue();
      if (selectedEntity != null && actionHandler instanceof IController) {
        selectedEntity = ((IController) actionHandler).merge(selectedEntity,
            MergeMode.MERGE_KEEP);
      }
      sourceViewConnector.setConnectorValue(selectedEntity);
    }
    setContext(result);
    return super.execute(actionHandler);
  }
}
