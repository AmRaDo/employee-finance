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

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import ning.codelab.finance.Employee;
import ning.codelab.finance.Organization;
import ning.codelab.finance.persist.FinancePersistance;

@Path("finance")
public class FinanceResource
{
    private final FinancePersistance persistance;

    @Inject
    public FinanceResource(FinancePersistance persistance)
    {
        this.persistance = persistance;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOrganization(Organization org)
    {
        if (persistance.getOrganization(org.getId()) != null) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Organization already exists").build());
        }
        persistance.addOrganization(org);
        return Response.status(Status.CREATED).build();
    }

    @GET
    @Path("/{orgid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Organization getOrganization(@PathParam("orgid") int orgId)
    {
        Organization org = persistance.getOrganization(orgId);
        if (org == null) {
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).build());
        }

        return org;
    }

    @POST
    @Path("/{orgid}/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEmployee(@PathParam("orgid") int orgId, Employee employee)
    {
        if (persistance.getOrganization(orgId) == null) {
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Organization with given id not found.").build());
        }

        Organization organization = persistance.getOrganization(orgId);

        if (organization.getEmployee(employee.getId()) != null) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Employee already exists.").build());
        }

        organization.addEmployee(employee);
        return Response.status(Status.CREATED).build();
    }

    @GET
    @Path("/{orgid}/{empid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Employee getEmployee(@PathParam("orgid") int orgId, @PathParam("empid") int empId)
    {
        Organization organization = getOrganization(orgId);
        Employee employee = organization.getEmployee(empId);
        if (employee == null) {
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).build());
        }

        return employee;
    }
    
    @POST
    @Path("/{orgid}/{empid}/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPayslip(@PathParam("orgid") int orgId, @PathParam("empid") int empId, @PathParam("year") int year, @PathParam("month") int month, ImmutableMap<String, Integer> payslipDetails){
        Employee employee = getEmployee(orgId, empId);
        employee.addPayslipForMonth(year, month, payslipDetails);
        return Response.ok("Payslip added successfully.").build();
    }
    
    @GET
    @Path("/{orgid}/{empid}/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Integer> getPayslip(@PathParam("orgid") int orgId, @PathParam("empid") int empId, @PathParam("year") int year, @PathParam("month") int month){
        Employee employee = getEmployee(orgId, empId);
        Map<String, Integer> payslipForMonth = employee.getPayslipForMonth(year, month);
        if(payslipForMonth == null){
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).build());
        }
        return payslipForMonth;
    }
    
}
