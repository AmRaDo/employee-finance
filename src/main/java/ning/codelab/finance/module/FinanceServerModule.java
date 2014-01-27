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

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import ning.codelab.finance.config.ConfigProvider;
import ning.codelab.finance.config.DBConfig;
import ning.codelab.finance.json.JacksonJsonProviderWrapper;
import ning.codelab.finance.json.JsonObjectMapperProvider;
import ning.codelab.finance.persist.FinancePersistance;
import ning.codelab.finance.persist.db.FinancePersistanceDBImpl;
import ning.codelab.finance.service.FinanceResource;

public class FinanceServerModule extends JerseyServletModule
{
    private static final String JERSEY_CONFIG_PROPERTY_PACKAGES = "com.sun.jersey.config.property.packages";
    private static final String FINANCE_RESOURCES_PACKAGE = "ning.codelab.finance";

    @Override
    protected void configureServlets()
    {
        bind(FinanceResource.class);
        bind(FinancePersistance.class).to(FinancePersistanceDBImpl.class).asEagerSingleton();
        bind(DBConfig.class).toProvider(ConfigProvider.class).asEagerSingleton();
        bind(JacksonJsonProvider.class).toProvider(JacksonJsonProviderWrapper.class).asEagerSingleton();

        bind(ObjectMapper.class).toProvider(new JsonObjectMapperProvider()).asEagerSingleton();

        final Map<String, String> params = new HashMap<String, String>();
        params.put(JERSEY_CONFIG_PROPERTY_PACKAGES, FINANCE_RESOURCES_PACKAGE);
        params.put("com.sun.jersey.spi.container.ContainerRequestFilters", GZIPContentEncodingFilter.class.getName());
        params.put("com.sun.jersey.spi.container.ContainerResponseFilters", GZIPContentEncodingFilter.class.getName());

        serve("/*").with(GuiceContainer.class, params);
    }
}
