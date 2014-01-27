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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.skife.jdbi.v2.DBI;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import ning.codelab.finance.Employee;
import ning.codelab.finance.Organization;
import ning.codelab.finance.config.DBConfig;
import ning.codelab.finance.json.SmileUtil;
import ning.codelab.finance.persist.FinancePersistance;
import ning.codelab.finance.persist.PersistanceException;

public class FinancePersistanceDBImpl implements FinancePersistance
{
    private Map<Integer, Organization> orgStorage = Maps.newConcurrentMap();

    private Set<Integer> employeeIdSet;

    private LoadingCache<Integer, Employee> employeeCache;

    private final DBConfig db;
    private EmployeeDAO dao;

    @Inject
    public FinancePersistanceDBImpl(DBConfig dbase)
    {
        this.db = dbase;
        makeConnection();
        createTable();
        employeeIdSet = new ConcurrentSkipListSet<Integer>(dao.getAllEmployeeIds());
        employeeCache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(10, TimeUnit.MINUTES).build(new CacheLoader<Integer, Employee>() {
            public Employee load(Integer key)
            {
                return dao.findById(key);
            }
        });
    }

    private void makeConnection()
    {
        DBI dbAccess = new DBI(db.getUrl(), db.getUser(), db.getPass());
        this.dao = dbAccess.open(EmployeeDAO.class);
    }

    private void createTable()
    {
        try {
            dao.checkCount();
        }
        catch (Exception e) {
            dao.create();
        }
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
        if (!employeeIdSet.contains(empId)) {
            return null;
        }
        try {
            return employeeCache.get(empId);
        }
        catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addEmployee(int orgId, Employee emp) throws PersistanceException
    {
        try {
            if (employeeIdSet.contains(emp.getId())) {
                dao.update(emp.getId(), SmileUtil.serialize(emp));
                employeeCache.invalidate(emp.getId());
            }
            else {
                dao.insert(emp.getId(), SmileUtil.serialize(emp));
                employeeIdSet.add(emp.getId());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new PersistanceException(e);
        }
    }

    @Override
    public void updateEmployee(int orgId, Employee emp) throws PersistanceException
    {
        try {
            if (employeeIdSet.contains(emp.getId())) {
                dao.update(emp.getId(), SmileUtil.serialize(emp));
                employeeCache.invalidate(emp.getId());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new PersistanceException(e);
        }
    }
}
