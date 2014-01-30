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

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ning.codelab.finance.Employee;
import ning.codelab.finance.Organization;
import ning.codelab.finance.persist.PersistanceManager;
import ning.codelab.finance.service.FinanceResource;

import static org.easymock.EasyMock.*;
import static org.testng.Assert.*;

public class FinanceResourceTest
{
    private FinanceResource financeResource;
    private PersistanceManager manager;

    @BeforeTest
    private void setup()
    {
        manager = createNiceMock(PersistanceManager.class);
        financeResource = new FinanceResource(manager);
    }

    @BeforeMethod
    private void resetMock()
    {
        resetToNice(manager);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddOrganizationInvalidId()
    {
        Organization org = new Organization(-1, "invalidOrg");
        financeResource.addOrganization(org);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddOrganizationInvalidNameNull()
    {
        Organization org = new Organization(1, null);
        financeResource.addOrganization(org);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddOrganizationInvalidNameEmpty()
    {
        Organization org = new Organization(1, "");
        financeResource.addOrganization(org);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddOrganizationInvalidNameBlank()
    {
        Organization org = new Organization(1, "     \t    ");
        financeResource.addOrganization(org);
    }

    @Test
    public void testAddOrganizationNew()
    {
        expect(manager.getOrganization(1)).andReturn(null);
        replay(manager);
        Organization org = new Organization(1, "testOrganization");
        Response response = financeResource.addOrganization(org);
        assertNotNull(response);
        assertEquals(response.getStatus(), Status.CREATED.getStatusCode());
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddOrganizationExisting()
    {
        expect(manager.getOrganization(1)).andReturn(new Organization(1, "testName"));
        replay(manager);
        Organization org = new Organization(1, "testOrganization");
        financeResource.addOrganization(org);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeInvalidId()
    {
        Employee employee = new Employee(-1, "first", "last", "first.last@test.org");
        financeResource.addEmployee(1, employee);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeInvalidFirstNameNull()
    {
        Employee employee = new Employee(1, null, "last", "first.last@test.org");
        financeResource.addEmployee(1, employee);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeInvalidFirstNameEmpty()
    {
        Employee employee = new Employee(1, "", "last", "first.last@test.org");
        financeResource.addEmployee(1, employee);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeInvalidFirstNameBlank()
    {
        Employee employee = new Employee(1, "   \t   ", "last", "first.last@test.org");
        financeResource.addEmployee(1, employee);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeInvalidLastNameNull()
    {
        Employee employee = new Employee(1, "first", null, "first.last@test.org");
        financeResource.addEmployee(1, employee);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeInvalidLastNameEmpty()
    {
        Employee employee = new Employee(1, "first", "", "first.last@test.org");
        financeResource.addEmployee(1, employee);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeInvalidLastNameBlank()
    {
        Employee employee = new Employee(1, "first", "  \t\t   ", "first.last@test.org");
        financeResource.addEmployee(1, employee);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeInvalidEmailIdNull()
    {
        Employee employee = new Employee(1, "first", "last", null);
        financeResource.addEmployee(1, employee);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeInvalidEmailIdEmpty()
    {
        Employee employee = new Employee(1, "first", "last", "");
        financeResource.addEmployee(1, employee);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeInvalidEmailIdBlank()
    {
        Employee employee = new Employee(1, "first", "last", "  \t  \t   \t");
        financeResource.addEmployee(1, employee);
    }
}
