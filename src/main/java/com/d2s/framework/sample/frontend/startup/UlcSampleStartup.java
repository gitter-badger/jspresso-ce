/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.sample.frontend.startup;

import com.d2s.framework.application.frontend.startup.ulc.UlcStartup;
import com.d2s.framework.application.model.BeanCollectionModule;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.sample.data.AppDataProducer;

/**
 * ULC sample startup class.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UlcSampleStartup extends UlcStartup {

  /**
   * Sets up some test data.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void start() {
    super.start();
    BeanCollectionModule companyModule = (BeanCollectionModule) ((Module) getBackendController()
        .getModuleConnector("company").getConnectorValue()).getSubModules()
        .get(0);
    companyModule.setModuleObjects(new AppDataProducer(getApplicationContext())
        .createTestData());
  }

  /**
   * Returns the "sample-ulc-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "sample-ulc-context";
  }
}
