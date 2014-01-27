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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.Table;

public class SmileUtil
{
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper(new SmileFactory());
        SimpleModule customModule = new SimpleModule("CustomModule");
        customModule.addSerializer(Table.class, new TableSerializer());
        customModule.addDeserializer(Table.class, new TableDeserializer());
        JodaModule jodaModule = new JodaModule();
        mapper.registerModules(new GuavaModule(), jodaModule, customModule);
    }

    public static Blob serialize(Object object) throws JsonGenerationException, JsonMappingException, IOException, SerialException, SQLException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzOutStream = new GZIPOutputStream(out);
        mapper.writeValue(gzOutStream, object);
        gzOutStream.close();
        Blob blob = new SerialBlob(out.toByteArray());
        return blob;
    }

    public static <T> T deserialize(Blob blob, Class<T> objectType) throws JsonParseException, JsonMappingException, IOException, SQLException
    {
        return mapper.readValue(new GZIPInputStream(blob.getBinaryStream()), objectType);
    }
}
