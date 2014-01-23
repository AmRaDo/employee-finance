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
import static org.testng.Assert.assertNotNull;

import java.io.IOException;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ning.codelab.finance.Employee;
import ning.codelab.finance.Organization;

public class OrganizationTest
{
    private Organization testOrg;

    @BeforeTest
    void setup()
    {
        testOrg = new Organization(1, "FOO");
        Employee emp1 = new Employee(1, "john", "smith", "john.smith@foo.bar");
        testOrg.addEmployee(emp1);
    }

    @Test
    public void testAddEmployee()
    {
        Employee emp2 = new Employee(2, "smith", "john", "smith.john@foo.bar");
        testOrg.addEmployee(emp2);
    }

    @Test
    public void testGetPaySlipForMonth()
    {
        Employee employee = testOrg.getEmployee(1);
        assertNotNull(employee);
        assertEquals(employee.getId(), 1);
        assertEquals(employee.getFirstName(), "john");
        assertEquals(employee.getLastName(), "smith");
        assertEquals(employee.getEmailId(), "john.smith@foo.bar");
    }
    
    @Test
    public void testEmployeeSet(){
        Employee emp1 = new Employee(1, "john", "smith", "john.smith@foo.bar");
        Employee emp2 = new Employee(1, "first", "last", "first.last@foo.bar");
        Employee emp3 = new Employee(2, "john", "smith", "john.smith@foo.bar");

        testOrg.addEmployee(emp1);
        testOrg.addEmployee(emp2);
        testOrg.addEmployee(emp3);
        
        assertEquals( testOrg.getAllEmployees().size(), 2);
    }
    
    @Test(groups = { "jsontest"})
    public void testJsonSerialization() throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
       assertEquals(mapper.writeValueAsString(new Organization(1, "FOO")), "{\"id\":1,\"name\":\"FOO\",\"allEmployees\":[]}");
    }
    
    @Test(groups = { "jsontest"})
    public void testJsonDeserialization() throws JsonParseException, JsonMappingException, IOException{
        ObjectMapper mapper = new ObjectMapper();
        Organization org = mapper.readValue("{\"id\":1,\"name\":\"FOO\"}", Organization.class);
        assertNotNull(org);
        assertEquals(org.getId(), testOrg.getId());
        assertEquals(org.getName(), testOrg.getName());
    }
}
