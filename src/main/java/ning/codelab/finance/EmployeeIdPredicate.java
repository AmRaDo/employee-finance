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
package ning.codelab.finance;

import com.google.common.base.Predicate;

public class EmployeeIdPredicate implements Predicate<Employee>
{
    private int employeeId;
    
    EmployeeIdPredicate(int employeeId){
        this.employeeId = employeeId;
    }

    public boolean apply(Employee employee)
    {
        if(employee != null){
              return employeeId == employee.getId();
        }
        return false;
    }
    
    
    
}
