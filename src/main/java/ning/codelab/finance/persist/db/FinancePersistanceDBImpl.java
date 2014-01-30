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
package ning.codelab.finance.persist.db;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

import ning.codelab.finance.Employee;
import ning.codelab.finance.Organization;
import ning.codelab.finance.json.SmileUtil;
import ning.codelab.finance.persist.FinancePersistance;
import ning.codelab.finance.persist.PersistanceException;

public class FinancePersistanceDBImpl implements FinancePersistance
{
    private Map<Integer, Organization> orgStorage = Maps.newConcurrentMap();

    private final EmployeeDAO dao;

    @Inject
    public FinancePersistanceDBImpl(EmployeeDAO dao)
    {
        this.dao = dao;
    }

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
        return dao.findById(empId);
    }

    @Override
    public void addEmployee(int orgId, Employee emp) throws PersistanceException
    {
        try {
            dao.insert(emp.getId(), SmileUtil.serialize(emp));
        }
        catch (Exception e) {
            throw new PersistanceException(e);
        }
    }

    @Override
    public void updateEmployee(int orgId, Employee emp) throws PersistanceException
    {
        try {
            dao.update(emp.getId(), SmileUtil.serialize(emp));
        }
        catch (Exception e) {
            throw new PersistanceException(e);
        }
    }

    @Override
    public List<Employee> getEmployees(int orgId, int empId, int size)
    {
        return dao.loadNextPage(empId, size);
    }

    @Override
    public int getType()
    {
        return DATABASE;
    }
}
