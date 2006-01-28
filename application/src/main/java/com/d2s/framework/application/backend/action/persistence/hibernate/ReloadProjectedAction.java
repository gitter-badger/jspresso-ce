/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.util.Collection;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.projection.BeanProjection;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.model.entity.IEntity;

/**
 * Reloads the projected object(s) in a transaction.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ReloadProjectedAction extends AbstractHibernateAction {

  /**
   * Reloads the projected object(s) in a transaction.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> execute(@SuppressWarnings("unused")
  IActionHandler actionHandler) {
    getTransactionTemplate().execute(new TransactionCallback() {

      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        ICompositeValueConnector projectionConnector = getProjectionConnector();
        BeanProjection projection = (BeanProjection) projectionConnector
            .getConnectorValue();
        if (projection.getProjectedObjects() != null) {
          Collection projectedCollection = projection.getProjectedObjects();
          for (Object entity : projectedCollection) {
            reloadEntity((IEntity) entity);
          }
        } else if (projection.getProjectedObject() != null) {
          reloadEntity((IEntity) projection.getProjectedObject());
        }
        return null;
      }
    });
    return null;
  }

  private void reloadEntity(IEntity entity) {
    if (entity.isPersistent()) {
      getApplicationSession().merge(
          (IEntity) getHibernateTemplate().load(entity.getContract().getName(),
              entity.getId()), MergeMode.MERGE_CLEAN_EAGER);
    }
  }
}
