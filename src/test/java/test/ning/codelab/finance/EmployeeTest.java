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

import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

import ning.codelab.finance.Employee;

import static org.testng.Assert.*;

public class EmployeeTest
{
    private Employee testEmployee;

    @BeforeTest
    void setup()
    {
        testEmployee = new Employee(1, "john", "smith", "john.smith@foo.bar");
        ImmutableMap<String, Integer> payslipDetails = ImmutableMap.of("Basic", 20000, "HRA", 16000, "Professional Tax", -200, "Total", 35800);
        testEmployee.addPayslipForMonth(2013, 12, payslipDetails);
    }

    @Test
    public void testAddPayslip()
    {
        ImmutableMap<String, Integer> payslipDetails = ImmutableMap.of("Basic", 20000, "HRA", 16000, "Professional Tax", -200, "Total", 35800);

        testEmployee.addPayslipForMonth(2014, 1, payslipDetails);
    }
    
    @Test
    public void testGetPaySlipForMonth(){
        Map<String, Integer> payslipForMonth = testEmployee.getPayslipForMonth(2013, 12);
        assertNotNull(payslipForMonth);
        assertEquals(payslipForMonth.size(), 4);
        assertEquals(payslipForMonth.get("Basic"), Integer.valueOf(20000));
        assertEquals(payslipForMonth.get("HRA"), Integer.valueOf(16000));
        assertEquals(payslipForMonth.get("Professional Tax"), Integer.valueOf(-200));
    }

}
