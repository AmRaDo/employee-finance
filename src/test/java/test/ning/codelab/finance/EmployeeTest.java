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

import java.util.Map;

import org.joda.time.YearMonth;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import ning.codelab.finance.Employee;

public class EmployeeTest
{
    private Employee testEmployee;

    @BeforeTest
    void setup()
    {
        testEmployee = new Employee(1, "john", "smith", "john.smith@foo.bar");
        Table<YearMonth, String, Integer> payslipDetails = HashBasedTable.create();
        YearMonth month = new YearMonth(2013, 12);
        payslipDetails.put(month, "Basic", 20000);
        payslipDetails.put(month, "HRA", 16000);
        payslipDetails.put(month, "Professional Tax", -200);
        payslipDetails.put(month, "Total", 35800);

        testEmployee.addPayslipDetails(payslipDetails);
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
}
