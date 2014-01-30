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
import java.util.Set;

import com.google.inject.Inject;

import ning.codelab.finance.Employee;
import ning.codelab.finance.Organization;

public class PersistanceManager
{   
    Set<FinancePersistance> persistanceProviders;
    
    @Inject
    public PersistanceManager(Set<FinancePersistance> persistanceProviders)
    {
        this.persistanceProviders = persistanceProviders;
    }
    
    public void addEmployee(int orgId, Employee emp) throws PersistanceException{
        
        for( FinancePersistance impl : persistanceProviders){
            impl.addEmployee(orgId, emp);
        }
    }
    
    public Employee getEmployee(int orgId, int empId){
        for( FinancePersistance impl : persistanceProviders){
            if(impl.getType() == FinancePersistance.IN_MEMORY){
                return impl.getEmployee(orgId, empId);
            }
        }
        return null;
    }

    public Organization getOrganization(int orgId)
    {
        for( FinancePersistance impl : persistanceProviders){
            if(impl.getType() == FinancePersistance.IN_MEMORY){
                return impl.getOrganization(orgId);
            }
        }
        return null;
    }

    public void addOrganization(Organization org)
    {
        for( FinancePersistance impl : persistanceProviders){
            impl.addOrganization(org);
        }
        
    }

    public void updateEmployee(int orgId, Employee employee) throws PersistanceException
    {
        for( FinancePersistance impl : persistanceProviders){
            impl.updateEmployee(orgId, employee);
        }
        
    }

    public List<Employee> getEmployees(int orgId, int empId, int size)
    {
        
        for( FinancePersistance impl : persistanceProviders){
            if(impl.getType() == FinancePersistance.DATABASE){
                return impl.getEmployees(orgId, empId, size);
            }
        }
        return null;
    }

}
