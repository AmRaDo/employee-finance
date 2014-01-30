/*
 * Copyright 2010-2013 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package ning.codelab.finance.persist;

import java.util.List;

import ning.codelab.finance.Employee;
import ning.codelab.finance.Organization;

public interface FinancePersistance
{
    public static final int NONE = 0;
    public static final int IN_MEMORY = 1;
    public static final int DATABASE = 2;
    
    public void addOrganization(Organization org);

    public Organization getOrganization(int orgId);

    public Employee getEmployee(int orgId, int EmpId);
    
    public void addEmployee(int orgId, Employee emp) throws PersistanceException;
    
    public void updateEmployee(int orgId, Employee emp) throws PersistanceException;

    public List<Employee> getEmployees(int orgId, int empId, int size);
    
    public int getType();
}
