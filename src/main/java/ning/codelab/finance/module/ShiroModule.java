/*
  * Copyright 2010-2014 Ning, Inc.
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

import ning.codelab.finance.realm.FinAuthorizingRealm;

import com.google.inject.name.Names;

import org.apache.shiro.guice.web.ShiroWebModule;

import javax.servlet.ServletContext;

public class ShiroModule
 extends ShiroWebModule
{

    public ShiroModule(ServletContext servletContext)
    {
        super(servletContext);
    }

    @Override
    protected void configureShiroWeb()
    {
        bindConstant().annotatedWith(Names.named("shiro.globalSessionTimeout")).to(30000L);

        bindRealm().to(FinAuthorizingRealm.class).asEagerSingleton();

        addFilterChain("/login.jsp", AUTHC);
        addFilterChain("/finance/*", AUTHC);
    }

}
