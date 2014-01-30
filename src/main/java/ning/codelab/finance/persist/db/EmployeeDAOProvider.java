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

import org.skife.jdbi.v2.DBI;

import com.google.inject.Inject;
import com.google.inject.Provider;

import ning.codelab.finance.config.DBConfig;

public class EmployeeDAOProvider implements Provider<EmployeeDAO>
{

    private DBConfig dbConfig;
    
    @Inject
    public EmployeeDAOProvider(DBConfig dbConfig){
        this.dbConfig = dbConfig;
    }
    @Override
    public EmployeeDAO get()
    {
        DBI dbAccess = new DBI(dbConfig.getUrl(), dbConfig.getUser(), dbConfig.getPass());
        EmployeeDAO dao = dbAccess.open(EmployeeDAO.class);
        try {
            dao.checkCount();
        }
        catch (Exception e) {
            dao.create();
        }
       
        return dao;
    }

}
