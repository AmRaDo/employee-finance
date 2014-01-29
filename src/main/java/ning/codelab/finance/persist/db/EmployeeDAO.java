package ning.codelab.finance.persist.db;

import java.sql.Blob;
import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import ning.codelab.finance.Employee;

public interface EmployeeDAO {

	@SqlUpdate("create table employee (c_id int primary key, c_data blob)")
	void create();

	@SqlQuery("select count(*) from employee")
	int checkCount();

	@SqlUpdate("insert into employee (c_id, c_data) values (:c_id, :c_data)")
	int insert(@Bind("c_id") int id, @Bind("c_data") Blob blob);
	
	@SqlUpdate("update employee set c_data=:c_data where c_id=:c_id")
    int update(@Bind("c_id") int id, @Bind("c_data") Blob blob);

	@Mapper(EmployeeMapper.class)
	@SqlQuery("select * from employee where c_id = :c_id")
	Employee findById(@Bind("c_id") int id);

	@Mapper(EmployeeMapper.class)
	@SqlQuery("select * from employee")
	List<Employee> showAll();

	@SqlQuery("select c_id from employee")
	List<Integer> getAllEmployeeIds();
	
	@SqlUpdate("delete from employee")
	void clearAllRecords();
	
	@Mapper(EmployeeMapper.class)
	@SqlQuery("select c_id, c_data from employee where c_id > :end_of_last_page order by c_id limit :size")
    public List<Employee> loadNextPage(@Bind("end_of_last_page") int last, @Bind("size") int size);

	@Mapper(EmployeeMapper.class)
    @SqlQuery("select c_id, c_data from employee where c_id < :start_of_next_page order by c_id desc limit :size")
    public List<Employee> loadPreviousPage(@Bind("start_of_next_page") int start, @Bind("size") int size);

	void close();
}
