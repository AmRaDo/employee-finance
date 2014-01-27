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
package test.ning.codelab.finance;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.joda.time.YearMonth;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.io.Files;

import ning.codelab.finance.Employee;
import ning.codelab.finance.json.TableDeserializer;
import ning.codelab.finance.json.TableSerializer;

public class EmployeeTest
{
    private static final String EMPLOYEE_JSON =
        "{\"id\":1,\"firstName\":\"john\"," + "\"lastName\":\"smith\",\"emailId\":\"john.smith@foo.bar\"," + "\"paySlipInfo\":{\"2013-12\":{\"HRA\":16000,\"Basic\":20000," + "\"Professional Tax\":-200,\"Total\":35800}}}";
    private Employee testEmployee;

    @BeforeTest
    void setup()
    {
        testEmployee = createTestEmployee();
    }

    private Employee createTestEmployee()
    {
        Employee employee = new Employee(1, "john", "smith", "john.smith@foo.bar");
        Table<YearMonth, String, Integer> payslipDetails = HashBasedTable.create();
        YearMonth month = new YearMonth(2013, 12);
        payslipDetails.put(month, "Basic", 20000);
        payslipDetails.put(month, "HRA", 16000);
        payslipDetails.put(month, "Professional Tax", -200);
        payslipDetails.put(month, "Total", 35800);

        employee.addPayslipDetails(payslipDetails);
        return employee;
    }

    private ObjectMapper getObjectMapper(JsonFactory jsonFactory)
    {
        ObjectMapper mapper = (jsonFactory != null) ? new ObjectMapper(jsonFactory) : new ObjectMapper();
        SimpleModule customModule = new SimpleModule("CustomModule");
        customModule.addSerializer(Table.class, new TableSerializer());
        customModule.addDeserializer(Table.class, new TableDeserializer());
        JodaModule jodaModule = new JodaModule();
        mapper.registerModules(new GuavaModule(), jodaModule, customModule);
        return mapper;
    }

    @Test
    public void testAddPayslip()
    {
        Table<YearMonth, String, Integer> payslipDetails = HashBasedTable.create();
        YearMonth month = new YearMonth(2014, 2);
        payslipDetails.put(month, "Basic", 20000);
        payslipDetails.put(month, "HRA", 16000);
        payslipDetails.put(month, "Professional Tax", -200);
        payslipDetails.put(month, "Total", 35800);

        testEmployee.addPayslipDetails(payslipDetails);
    }

    @Test
    public void testGetPaySlipForMonth()
    {
        Map<String, Integer> payslipForMonth = testEmployee.getPayslipForMonth(new YearMonth(2013, 12));
        assertNotNull(payslipForMonth);
        assertEquals(payslipForMonth.size(), 4);
        assertEquals(payslipForMonth.get("Basic"), Integer.valueOf(20000));
        assertEquals(payslipForMonth.get("HRA"), Integer.valueOf(16000));
        assertEquals(payslipForMonth.get("Professional Tax"), Integer.valueOf(-200));
        assertEquals(payslipForMonth.get("Total"), Integer.valueOf(35800));

    }

    @Test
    public void testEmployeeEquality()
    {
        Employee emp1 = new Employee(1, "john", "smith", "john.smith@foo.bar");
        Employee emp2 = new Employee(1, "first", "last", "first.last@foo.bar");
        Employee emp3 = new Employee(2, "john", "smith", "john.smith@foo.bar");

        assertEquals(emp1, emp2);
        assertEquals(emp2, emp1);
        assertNotEquals(emp2, emp3);
        assertNotEquals(emp1, emp3);
    }

    @Test(groups = { "jsontest" })
    public void testJsonSerialization() throws JsonProcessingException
    {
        ObjectMapper mapper = getObjectMapper(null);
        assertEquals(mapper.writeValueAsString(createTestEmployee()), EMPLOYEE_JSON);
    }

    @Test(groups = { "jsontest" })
    public void testJsonDeserialization() throws JsonParseException, JsonMappingException, IOException
    {
        ObjectMapper mapper = getObjectMapper(null);
        Employee emp = mapper.readValue(EMPLOYEE_JSON, Employee.class);
        assertNotNull(emp);
        assertEquals(emp.getId(), 1);
        assertEquals(emp.getFirstName(), "john");
        assertEquals(emp.getLastName(), "smith");
        assertEquals(emp.getEmailId(), "john.smith@foo.bar");
        Map<String, Integer> actualPayslip = emp.getPayslipForMonth(new YearMonth(2013, 12));
        assertNotNull(actualPayslip);
        assertEquals(actualPayslip.size(), 4);
        assertEquals(actualPayslip.get("Basic"), Integer.valueOf(20000));
        assertEquals(actualPayslip.get("HRA"), Integer.valueOf(16000));
        assertEquals(actualPayslip.get("Professional Tax"), Integer.valueOf(-200));
        assertEquals(actualPayslip.get("Total"), Integer.valueOf(35800));

    }

    @Test(groups = { "jsontest" })
    public void testJsonSerializationSmile() throws IOException
    {
        ObjectMapper mapper = getObjectMapper(new SmileFactory());
        assertEquals(mapper.writeValueAsBytes(createTestEmployee()), Files.toByteArray(new File("employee.txt")));
    }

    @Test(groups = { "jsontest" })
    public void testJsonDeserializationSmile() throws JsonParseException, JsonMappingException, IOException
    {
        ObjectMapper mapper = getObjectMapper(new SmileFactory());
        Employee emp = mapper.readValue(new File("employee.txt"), Employee.class);
        assertNotNull(emp);
        assertEquals(emp.getId(), 1);
        assertEquals(emp.getFirstName(), "john");
        assertEquals(emp.getLastName(), "smith");
        assertEquals(emp.getEmailId(), "john.smith@foo.bar");
        Map<String, Integer> actualPayslip = emp.getPayslipForMonth(new YearMonth(2013, 12));
        assertNotNull(actualPayslip);
        assertEquals(actualPayslip.size(), 4);
        assertEquals(actualPayslip.get("Basic"), Integer.valueOf(20000));
        assertEquals(actualPayslip.get("HRA"), Integer.valueOf(16000));
        assertEquals(actualPayslip.get("Professional Tax"), Integer.valueOf(-200));
        assertEquals(actualPayslip.get("Total"), Integer.valueOf(35800));
    }
}
