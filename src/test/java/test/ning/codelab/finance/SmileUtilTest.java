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

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.joda.time.YearMonth;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.io.Files;

import static org.testng.Assert.*;

import ning.codelab.finance.Employee;
import ning.codelab.finance.json.SmileUtil;

public class SmileUtilTest
{
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
    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, SerialException, IOException, SQLException{
        Employee emp = createTestEmployee();
        Blob serialize = SmileUtil.serialize(emp);
        assertEquals(serialize.getBytes(1, (int) serialize.length()), Files.toByteArray(new File("SmileTest.txt")));
    }
    
    @Test
    public void testDeserialize() throws JsonParseException, JsonMappingException, IOException, SQLException{
        Blob blob = new SerialBlob(Files.toByteArray(new File("SmileTest.txt")));
        Employee emp = SmileUtil.deserialize( blob, Employee.class);
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
