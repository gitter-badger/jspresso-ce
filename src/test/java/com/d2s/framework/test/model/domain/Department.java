/*
 * Generated by Design2see. All rights reserved.
 */
package com.d2s.framework.test.model.domain;

/**
 * Department entity.
 * <p>
 * Generated by Design2see. All rights reserved.
 * <p>
 * 
 * @hibernate.mapping default-access =
 *                    "com.d2s.framework.model.persistence.hibernate.property.EntityPropertyAccessor"
 *                    package = "com.d2s.framework.test.model.domain"
 * @hibernate.class table = "DEPARTMENT" dynamic-insert = "true" dynamic-update =
 *                  "true" persister =
 *                  "com.d2s.framework.model.persistence.hibernate.entity.persister.EntityProxySingleTableEntityPersister"
 * @author Generated by Design2see
 */
public interface Department extends
    com.d2s.framework.test.model.domain.Nameable,
    com.d2s.framework.test.model.domain.Describeable,
    com.d2s.framework.model.entity.IEntity,
    com.d2s.framework.test.model.domain.Traceable {

  /**
   * Gets the manager.
   * 
   * @hibernate.many-to-one cascade = "lock"
   * @hibernate.column name = "MANAGER_ID"
   * @return the manager.
   */
  com.d2s.framework.test.model.domain.Employee getManager();

  /**
   * Sets the manager.
   * 
   * @param manager
   *          the manager to set.
   */
  void setManager(com.d2s.framework.test.model.domain.Employee manager);

  /**
   * Gets the departmentEmployees.
   * 
   * @hibernate.set cascade =
   *                "persist,merge,save-update,lock,refresh,evict,replicate"
   *                inverse = "true" order-by="NAME"
   * @hibernate.key column = "DEPARTMENT_ID"
   * @hibernate.one-to-many class =
   *                        "com.d2s.framework.test.model.domain.Employee"
   * @return the departmentEmployees.
   */
  java.util.Set<com.d2s.framework.test.model.domain.Employee> getDepartmentEmployees();

  /**
   * Sets the departmentEmployees.
   * 
   * @param departmentEmployees
   *          the departmentEmployees to set.
   */
  void setDepartmentEmployees(
      java.util.Set<com.d2s.framework.test.model.domain.Employee> departmentEmployees);

  /**
   * Adds an element to the departmentEmployees.
   * 
   * @param departmentEmployeesElement
   *          the departmentEmployees element to add.
   */
  void addToDepartmentEmployees(
      com.d2s.framework.test.model.domain.Employee departmentEmployeesElement);

  /**
   * Removes an element from the departmentEmployees.
   * 
   * @param departmentEmployeesElement
   *          the departmentEmployees element to remove.
   */
  void removeFromDepartmentEmployees(
      com.d2s.framework.test.model.domain.Employee departmentEmployeesElement);

  /**
   * Gets the departmentProjects.
   * 
   * @hibernate.set cascade =
   *                "persist,merge,save-update,lock,refresh,evict,replicate"
   *                inverse = "true" order-by="NAME"
   * @hibernate.key column = "DEPARTMENT_ID"
   * @hibernate.one-to-many class =
   *                        "com.d2s.framework.test.model.domain.Project"
   * @return the departmentProjects.
   */
  java.util.Set<com.d2s.framework.test.model.domain.Project> getDepartmentProjects();

  /**
   * Sets the departmentProjects.
   * 
   * @param departmentProjects
   *          the departmentProjects to set.
   */
  void setDepartmentProjects(
      java.util.Set<com.d2s.framework.test.model.domain.Project> departmentProjects);

  /**
   * Adds an element to the departmentProjects.
   * 
   * @param departmentProjectsElement
   *          the departmentProjects element to add.
   */
  void addToDepartmentProjects(
      com.d2s.framework.test.model.domain.Project departmentProjectsElement);

  /**
   * Removes an element from the departmentProjects.
   * 
   * @param departmentProjectsElement
   *          the departmentProjects element to remove.
   */
  void removeFromDepartmentProjects(
      com.d2s.framework.test.model.domain.Project departmentProjectsElement);

}
