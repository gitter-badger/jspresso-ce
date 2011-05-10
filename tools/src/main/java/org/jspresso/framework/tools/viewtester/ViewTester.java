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
package org.jspresso.framework.tools.viewtester;

import java.awt.BorderLayout;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.context.ApplicationContext;

/**
 * Generates Jspresso powered component java code based on its descriptor.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ViewTester {

  private static final String APPLICATION_CONTEXT_KEY = "applicationContextKey";
  private static final String LANGUAGE                = "language";
  private static final String VIEW_ID                 = "viewId";

  private String              applicationContextKey;
  private String              language;
  private String              viewId;

  /**
   * Starts Code generation for an component.
   * 
   * @param args
   *          the command line arguments.
   */
  @SuppressWarnings("static-access")
  public static void main(String[] args) {
    Options options = new Options();
    options
        .addOption(OptionBuilder
            .withArgName(APPLICATION_CONTEXT_KEY)
            .isRequired()
            .hasArg()
            .withDescription(
                "use given applicationContextKey as registered in the spring BeanFactoryLocator.")
            .create(APPLICATION_CONTEXT_KEY));
    options.addOption(OptionBuilder
        .withArgName(VIEW_ID)
        .isRequired()
        .hasArg()
        .withDescription(
            "use given view identifier to instanciate and display the view.")
        .create(VIEW_ID));
    options.addOption(OptionBuilder
        .withArgName(LANGUAGE)
        .hasArg()
        .withDescription(
            "use given locale to instanciate and display the view.")
        .create(LANGUAGE));
    CommandLineParser parser = new BasicParser();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException ex) {
      System.err.println(ex.getLocalizedMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(ViewTester.class.getSimpleName(), options);
      return;
    }

    ViewTester tester = new ViewTester();
    tester
        .setApplicationContextKey(cmd.getOptionValue(APPLICATION_CONTEXT_KEY));
    tester.setViewId(cmd.getOptionValue(VIEW_ID));
    tester.setLanguage(cmd.getOptionValue(LANGUAGE));
    tester.displayView();
  }

  /**
   * Generates the component java source files.
   */
  @SuppressWarnings("unchecked")
  public void displayView() {
    Locale locale;
    if (language != null) {
      locale = new Locale(language);
    } else {
      locale = Locale.getDefault();
    }

    ApplicationContext appContext = getApplicationContext();
    IViewDescriptor viewDescriptor = (IViewDescriptor) appContext
        .getBean(viewId);

    IFrontendController<JComponent, Icon, Action> mockFrontController = (IFrontendController<JComponent, Icon, Action>) appContext
        .getBean("applicationFrontController");
    IBackendController mockBackController = (IBackendController) appContext
        .getBean("applicationBackController");

    mockFrontController.start(mockBackController, locale, TimeZone.getDefault());

    IView<JComponent> view = mockFrontController.getViewFactory().createView(
        viewDescriptor, mockFrontController, locale);

    IValueConnector modelConnector = mockBackController.createModelConnector(
        "modelConnector", viewDescriptor.getModelDescriptor());

    IEntityFactory entityFactory = mockBackController.getEntityFactory();

    modelConnector.setConnectorValue(entityFactory
        .createEntityInstance(((IComponentDescriptor<IEntity>) viewDescriptor
            .getModelDescriptor()).getComponentContract()));

    mockFrontController.getMvcBinder()
        .bind(view.getConnector(), modelConnector);

    JFrame testFrame = new JFrame("View tester");
    testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    testFrame.getContentPane().setLayout(new BorderLayout());
    testFrame.getContentPane().add(view.getPeer(), BorderLayout.CENTER);

    testFrame.pack();
    testFrame.setSize(450, 300);
    System.setProperty("sun.awt.exception.handler",
        TesterExceptionHandler.class.getName());
    SwingUtil.centerOnScreen(testFrame);
    testFrame.setVisible(true);
  }

  /**
   * Sets the applicationContextKey.
   * 
   * @param applicationContextKey
   *          the applicationContextKey to set.
   */
  public void setApplicationContextKey(String applicationContextKey) {
    this.applicationContextKey = applicationContextKey;
  }

  /**
   * Sets the language.
   * 
   * @param language
   *          the language to set.
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Sets the viewId.
   * 
   * @param viewId
   *          the viewId to set.
   */
  public void setViewId(String viewId) {
    this.viewId = viewId;
  }

  private ApplicationContext getApplicationContext() {
    BeanFactoryLocator bfl = SingletonBeanFactoryLocator.getInstance();
    BeanFactoryReference bf = bfl.useBeanFactory(applicationContextKey);
    return (ApplicationContext) bf.getFactory();
  }

  /**
   * Specialized exception handler for the tester event dispatch thread.
   * 
   * @version $LastChangedRevision$
   * @author Vincent Vandenschrick
   */
  public static class TesterExceptionHandler {

    /**
     * Handles a uncaught exception.
     * 
     * @param t
     *          the uncaught exception.
     */
    public void handle(Throwable t) {
      t.printStackTrace();
      JOptionPane.showMessageDialog(null, t.getMessage(), "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
