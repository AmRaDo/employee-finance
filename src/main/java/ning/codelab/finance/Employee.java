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
package ning.codelab.finance;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.joda.time.YearMonth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class Employee
{

    final private int id;
    private String firstName;
    private String lastName;
    private String emailId;

    ConcurrentMap<YearMonth, ImmutableMap<String, Integer>> paySlipInfo;

    @JsonCreator
    public Employee(@JsonProperty("id") int id, @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("emailId") String emailId)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.paySlipInfo = Maps.newConcurrentMap();
    }

    public int getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getEmailId()
    {
        return emailId;
    }

    public Map<String, Integer> getPayslipForMonth(int year, int month)
    {
        return paySlipInfo.get(new YearMonth(year, month));
    }

    public void addPayslipForMonth(int year, int month, ImmutableMap<String, Integer> payslipDetails)
    {
        paySlipInfo.putIfAbsent(new YearMonth(year, month), payslipDetails);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

}
