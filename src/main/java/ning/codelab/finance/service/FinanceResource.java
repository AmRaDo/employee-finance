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
package ning.codelab.finance.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;

import com.google.common.collect.Table;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ning.codelab.finance.Employee;
import ning.codelab.finance.Organization;
import ning.codelab.finance.persist.PersistanceException;
import ning.codelab.finance.persist.PersistanceManager;

@Path("finance")
@Singleton
public class FinanceResource
{
    private final PersistanceManager manager;

    @Inject
    public FinanceResource(PersistanceManager manager)
    {
        this.manager = manager;
    }

    private void validateOrganization(Organization org)
    {
        if (org.getId() <= 0) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Organization id.").build());
        }
        if (org.getName() == null || org.getName().isEmpty() || org.getName().trim().isEmpty()) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Organization name.").build());
        }
    }

    private void validateEmployee(Employee employee)
    {
        if (employee.getId() <= 0) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee id.").build());
        }
        if (employee.getFirstName() == null || employee.getFirstName().isEmpty() || employee.getFirstName().trim().isEmpty()) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee first Name.").build());
        }

        if (employee.getLastName() == null || employee.getLastName().isEmpty() || employee.getLastName().trim().isEmpty()) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee last Name.").build());
        }

        if (employee.getEmailId() == null || employee.getEmailId().isEmpty() || employee.getEmailId().trim().isEmpty()) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee email address.").build());
        }
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response addOrganization(Organization org)
    {
        validateOrganization(org);
        if (manager.getOrganization(org.getId()) != null) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Organization already exists").build());
        }
        manager.addOrganization(org);
        return Response.status(Status.CREATED).build();
    }

    private Organization getOrganization(int orgId)
    {
        Organization org = manager.getOrganization(orgId);
        if (org == null) {
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).build());
        }

        return org;
    }

    @POST
    @Path("/{orgid}/")
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response addEmployee(@PathParam("orgid") int orgId, Employee employee)
    {
        validateEmployee(employee);

        Organization organization = getOrganization(orgId);

        if (organization.getEmployee(employee.getId()) != null) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Employee already exists.").build());
        }

        try {
            manager.addEmployee(orgId, employee);
        }
        catch (PersistanceException e) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build());
        }

        return Response.status(Status.CREATED).build();
    }

    @GET
    @Path("/{orgid}/{empid}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Employee getEmployee(@PathParam("orgid") int orgId, @PathParam("empid") int empId)
    {
        getOrganization(orgId);
        Employee employee = manager.getEmployee(orgId, empId);
        if (employee == null) {
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).build());
        }

        return employee;
    }

    @GET
    @Path("/{orgid}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Set<Employee> getAllEmployees(@PathParam("orgid") int orgId)
    {
        Organization organization = getOrganization(orgId);
        return organization.getAllEmployees();
    }

    @POST
    @Path("/{orgid}/{empid}")
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response addPayslip(@PathParam("orgid") int orgId, @PathParam("empid") int empId, Table<YearMonth, String, Integer> payslipDetails)
    {
        Employee employee = getEmployee(orgId, empId);
        employee.addPayslipDetails(payslipDetails);

        try {
            manager.updateEmployee(orgId, employee);
        }
        catch (PersistanceException e) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build());
        }
        return Response.ok("Payslip added successfully.").build();
    }

    @GET
    @Path("/{orgid}/{empid}/payslip")
    @Produces({ MediaType.APPLICATION_JSON })
    public Map<String, Integer> getPayslip(@PathParam("orgid") int orgId, @PathParam("empid") int empId, @QueryParam("month") String month)
    {
        Employee employee = getEmployee(orgId, empId);
        Map<String, Integer> payslipForMonth = employee.getPayslipForMonth(YearMonth.parse(month, DateTimeFormat.forPattern("yyyy-MM")));
        if (payslipForMonth == null || payslipForMonth.isEmpty()) {
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).build());
        }
        return payslipForMonth;
    }

    @GET
    @Path("/{orgid}/getAll")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Employee> getEmployees(@PathParam("orgid") int orgId, @QueryParam("last_empId") @DefaultValue("0") int empId, @QueryParam("size") @DefaultValue("50") int size)
    {
        getOrganization(orgId);
        return manager.getEmployees(orgId, empId, size);
    }

/*    @GET
    @Path("/{orgid}/prepareData")
    public void prepareData(@PathParam("orgid") int orgId)
    {
        System.out.println("Preparing records for " + getOrganization(orgId).getName());
        String fname = "firstName_", lname = "lastName_";
        int empId = 1;
        while (empId < 10 * 1000000) {
            String firstName = fname + empId;
            String lastName = lname + empId;
            String emailId = firstName + "." + lastName + "@foo.bar";
            try {
                persistance.addEmployee(orgId, new Employee(empId, firstName, lastName, emailId));
            }
            catch (PersistanceException e) {
                throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build());
            }
            if (empId % 10000 == 0) {
                System.out.println("Completed records #" + empId);
            }
            empId++;
        }
    }*/
}
