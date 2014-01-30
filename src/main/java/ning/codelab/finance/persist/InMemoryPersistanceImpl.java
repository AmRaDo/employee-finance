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
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import ning.codelab.finance.Employee;
import ning.codelab.finance.Organization;

public class InMemoryPersistanceImpl implements FinancePersistance
{

    private Map<Integer, Organization> orgStorage = Maps.newConcurrentMap();

    @Override
    public void addOrganization(Organization org)
    {
        orgStorage.put(org.getId(), org);
    }

    @Override
    public Organization getOrganization(int orgId)
    {
        return orgStorage.get(orgId);
    }

    @Override
    public Employee getEmployee(int orgId, int empId)
    {
        Organization organization = getOrganization(orgId);
        if (organization != null) {
            return organization.getEmployee(empId);
        }

        return null;
    }

    @Override
    public void addEmployee(int orgId, Employee employee)
    {
        Organization organization = getOrganization(orgId);
        if (organization != null) {
            organization.addEmployee(employee);
        }
    }

    @Override
    public void updateEmployee(int orgId, Employee emp) throws PersistanceException
    {
        addEmployee(orgId, emp);
    }

    @Override
    public List<Employee> getEmployees(int orgId, int empId, int size)
    {
        Organization organization = getOrganization(orgId);
        if (organization != null) {
            return ImmutableList.copyOf(organization.getAllEmployees());
        }
        return null;
    }

    @Override
    public int getType()
    {
        return IN_MEMORY;
    }

}
