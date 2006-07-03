/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.printing.model.basic;

import java.util.HashMap;
import java.util.Locale;

import com.d2s.framework.application.printing.model.IReport;
import com.d2s.framework.application.printing.model.IReportFactory;
import com.d2s.framework.application.printing.model.descriptor.IReportDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * Basic implementation of report factory returning instances of
 * <code>BasicReport</code>.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicReportFactory implements IReportFactory {

  /**
   * {@inheritDoc}
   */
  public IReport createReportInstance(IReportDescriptor reportDescriptor,
      ITranslationProvider translationProvider, Locale locale) {
    BasicReport report = new BasicReport();
    report.setName(reportDescriptor.getI18nName(translationProvider, locale));
    report.setDescription(reportDescriptor.getI18nDescription(
        translationProvider, locale));
    report.setReportDescriptor(reportDescriptor);
    report.setContext(new HashMap<String, Object>());
    return report;
  }

}
