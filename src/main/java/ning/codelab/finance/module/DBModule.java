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
package ning.codelab.finance.module;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import ning.codelab.finance.config.ConfigProvider;
import ning.codelab.finance.config.DBConfig;
import ning.codelab.finance.persist.FinancePersistance;
import ning.codelab.finance.persist.db.EmployeeDAO;
import ning.codelab.finance.persist.db.EmployeeDAOProvider;
import ning.codelab.finance.persist.db.FinancePersistanceDBImpl;

public class DBModule extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind(DBConfig.class).toProvider(ConfigProvider.class).asEagerSingleton();
        bind(EmployeeDAO.class).toProvider(EmployeeDAOProvider.class).asEagerSingleton();
        Multibinder<FinancePersistance> multibinder = Multibinder.newSetBinder(binder(), FinancePersistance.class);
        multibinder.addBinding().to(FinancePersistanceDBImpl.class);
    }

}
