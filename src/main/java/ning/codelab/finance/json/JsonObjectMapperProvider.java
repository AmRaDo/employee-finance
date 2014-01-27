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
package ning.codelab.finance.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.Table;
import com.google.inject.Provider;

public class JsonObjectMapperProvider implements Provider<ObjectMapper>
{

    @Override
    public ObjectMapper get()
    {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule customModule = new SimpleModule("CustomModule");
        customModule.addSerializer(Table.class, new TableSerializer());
        customModule.addDeserializer(Table.class, new TableDeserializer());
        JodaModule jodaModule = new JodaModule();
        mapper.registerModules(new GuavaModule(), jodaModule, customModule);
        return mapper;
    }

}
