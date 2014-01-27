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
package ning.codelab.finance.persist.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import ning.codelab.finance.Employee;
import ning.codelab.finance.json.SmileUtil;

public class EmployeeMapper implements ResultSetMapper<Employee>
{

    @Override
    public Employee map(int paramInt, ResultSet paramResultSet, StatementContext paramStatementContext) throws SQLException
    {
        try {
            return SmileUtil.deserialize(paramResultSet.getBlob("c_data"), Employee.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            if (e instanceof SQLException) {
                throw (SQLException)e;
            }
            throw new SQLException(e);
        }
    }

}
