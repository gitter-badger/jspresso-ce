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
package org.jspresso.framework.model.component;

/**
 * This interface is implemented by component property translations.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("EmptyMethod")
public interface IPropertyTranslation extends IComponent {


  /**
   * Constant value for propertyName.
   */
  String PROPERTY_NAME = "propertyName";

  /**
   * Gets the propertyName.
   *
   * @return the propertyName.
   */
  java.lang.String getPropertyName();

  /**
   * Sets the propertyName.
   *
   * @param propertyName
   *          the propertyName to set.
   */
  void setPropertyName(java.lang.String propertyName);



  /**
   * Constant value for language.
   */
  String LANGUAGE = "language";

  /**
   * Gets the language.

   * @return the language.
   */
  java.lang.String getLanguage();

  /**
   * Sets the language.
   *
   * @param language
   *          the language to set.
   */
  void setLanguage(java.lang.String language);



  /**
   * Constant value for translatedValue.
   */
  String TRANSLATED_VALUE = "translatedValue";

  /**
   * Gets the translatedValue.
   *
   * @return the translatedValue.
   */
  java.lang.String getTranslatedValue();

  /**
   * Sets the translatedValue.
   *
   * @param translatedValue
   *          the translatedValue to set.
   */
  void setTranslatedValue(java.lang.String translatedValue);
}