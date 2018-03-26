package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.model.EmployeeToken;
import com.revature.service.EmployeeServiceAlpha;
import com.revature.util.ConnectionUtil;

public class EmployeeRepositoryjbdc implements EmployeeRepository{

	private static EmployeeRepositoryjbdc repository = new EmployeeRepositoryjbdc();
	private static Logger logger = Logger.getLogger(EmployeeRepositoryjbdc.class);

	// Constructor
	private EmployeeRepositoryjbdc() {}

	// Make it Singleton
	public static EmployeeRepositoryjbdc getInstance(){
		return repository;
	}

	@Override
	public boolean insert(Employee employee) throws SQLException {
		logger.trace("Inserting new employee.");

		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql = "INSERT INTO USER_T VALUES (null,?,?,?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, employee.getFirstName());
			statement.setString(++parameterIndex, employee.getLastName());
			statement.setString(++parameterIndex, employee.getUsername());
			statement.setString(++parameterIndex, employee.getPassword());
			statement.setString(++parameterIndex, employee.getEmail());
			statement.setInt(++parameterIndex, employee.getEmployeeRole().getId());
			if (statement.executeUpdate() > 0) {
				logger.info("Insert successful!");
				return true;
			}
		} catch (SQLException e) {
			logger.error("Exception at EmployeeRepositoryjbdc.insert", e);
		}
		return false;
	}

	@Override
	public boolean update(Employee employee) {
		logger.trace("Updating new employee.");

		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql = "UPDATE USER_T SET U_FIRSTNAME = ?, U_LASTNAME = ?, U_PASSWORD = ? , U_EMAIL =? WHERE U_ID =?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, employee.getFirstName());
			statement.setString(++parameterIndex, employee.getLastName());
			statement.setString(++parameterIndex, getPasswordHash(employee));
			statement.setString(++parameterIndex, employee.getEmail());
			statement.setInt(++parameterIndex, employee.getId());
			int num = statement.executeUpdate();
			if ( num > 0) {
				logger.info("Update success!");
				return true;
			}
		} catch (SQLException e) {
			logger.error("Exception at EmployeeRepository.update.", e);}
		return false;
	}

	@Override
	public Employee select(int employeeId) {
		logger.trace("Selecting employee.");
		String empID = Integer.toString(employeeId);
		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql = "SELECT * FROM USER_T,USER_ROLE WHERE U_ID = ? AND USER_T.UR_ID = USER_ROLE.UR_ID";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, empID);
			ResultSet result = statement.executeQuery();

			if(result.next())
				return new Employee(
						result.getInt("U_ID"),
						result.getString("U_FIRSTNAME"),
						result.getString("U_LASTNAME"),
						result.getString("U_USERNAME"),
						result.getString("U_PASSWORD"),
						result.getString("U_EMAIL"),
						new EmployeeRole(
								result.getInt("UR_ID"),
								result.getString("UR_TYPE")
								)
						);
			logger.error("Employee selection successful!");
		} catch (SQLException e) {
			logger.error("Exception at EmployeeRepositoryjbdc.select", e);
		}
		return null;
	}

	@Override
	public Employee select(String username) {
		logger.trace("Selecting employee.");
		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql = "SELECT * FROM USER_T,USER_ROLE WHERE U_USERNAME = ? AND USER_T.UR_ID = USER_ROLE.UR_ID";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, username);
			ResultSet result = statement.executeQuery();

			if(result.next()) {
				return new Employee(
						result.getInt("U_ID"),
						result.getString("U_FIRSTNAME"),
						result.getString("U_LASTNAME"),
						result.getString("U_USERNAME"),
						result.getString("U_PASSWORD"),
						result.getString("U_EMAIL"),
						new EmployeeRole(
								result.getInt("UR_ID"),
								result.getString("UR_TYPE")
								)
						);
			}
			logger.trace("Employee selection successful!");
		} catch (SQLException e) {
			logger.error("Exception thrown while selecting employee by username", e);
		}
		return new Employee();
	}


	@Override
	public Set<Employee> selectAll() {
		try(Connection connection = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM USER_T INNER JOIN USER_ROLE ON USER_T.UR_ID = USER_ROLE.UR_ID";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			Set<Employee> set = new HashSet<>();
			while(result.next()){
				set.add(new Employee(
						result.getInt("U_ID"),
						result.getString("U_FIRSTNAME"),
						result.getString("U_LASTNAME"),
						result.getString("U_USERNAME"),
						result.getString("U_PASSWORD"),
						result.getString("U_EMAIL"),
						new EmployeeRole(
								result.getInt("UR_ID"),
								result.getString("UR_TYPE")
								)
						)
						);
			}
			logger.error("SelectAll successful!");
			return set;
		}catch (SQLException e){
			logger.error("Exception at EmployeeRepositoryjbdc.selectAll", e);
		}
		return null;
	}

	@Override
	public String getPasswordHash(Employee employee) {
		logger.trace("Gathering password.");
		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql = "SELECT GET_HASH(?) AS HASH FROM DUAL";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, employee.getPassword());

			ResultSet result = statement.executeQuery();
			if(result.next()){
				return result.getString("HASH");
			}
			logger.info("Password hash acquired!");
		} catch (SQLException e) {
			logger.error("Exception at EmployeeRepositoryjdbc.getPasswordHash", e);
		}
		return new String();
	}

	@Override
	public boolean insertEmployeeToken(EmployeeToken employeeToken) {
		logger.trace("Inserting new employee token.");

		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			LocalDateTime dateTime = employeeToken.getCreationDate();

			// The first null is for the primary key, it has a trigger.
			// The second null is for the token hash that is generated from the current timestamp.
			String sql = "INSERT INTO PASSWORD_RECOVERY(PR_ID, PR_TOKEN, PR_TIME, U_ID) VALUES (NULL, NULL,?,?)";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setTimestamp(++parameterIndex, Timestamp.valueOf(employeeToken.getCreationDate()));
			statement.setInt(++parameterIndex, employeeToken.getRequester().getId());

			if (statement.executeUpdate() > 0) {
				logger.error("EmployeeToken insert successful!");
				return true;
			}
		} catch (SQLException e) {
			logger.error("Exception at EmployeeRepositoryjdbc.inserEmployeeToken", e);}
		return false;
	}

	@Override
	public boolean deleteEmployeeToken(EmployeeToken employeeToken) throws SQLException {
		try(Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql ="DELETE FROM PASSWORD_RECOVERY WHERE PR_ID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(++parameterIndex, Integer.toString(employeeToken.getId()));
			if (statement.executeUpdate() > 0){
				logger.info("Delete successful!");
				return true;
			} else {
				throw new SQLException("There weren't any tokens within the database with those parameters.");
			}

		} catch (SQLException e){
			logger.error("Exception at EmployeeRepository.deleteEmployeeToken");
			return false;
		}
	}

	@Override
	public EmployeeToken selectEmployeeToken(EmployeeToken employeeToken) {
		logger.trace("Selecting employee token");
		try(Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;

			String sql ="SELECT USER_T.U_ID, USER_T.U_FIRSTNAME, USER_T.U_LASTNAME, USER_T.U_USERNAME, USER_T.U_PASSWORD, USER_T.U_EMAIL, USER_T.UR_ID, PASSWORD_RECOVERY.PR_ID, PASSWORD_RECOVERY.PR_TOKEN, PASSWORD_RECOVERY.PR_TIME, PASSWORD_RECOVERY.U_ID FROM USER_T INNER JOIN PASSWORD_RECOVERY ON USER_T.U_ID = PASSWORD_RECOVERY.U_ID WHERE PR_ID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(++parameterIndex, employeeToken.getId());
			ResultSet result = statement.executeQuery();
			logger.trace("SQL Executed correctly.");
			if (result.next()) {
				return new EmployeeToken(
						result.getInt("PR_ID"),
						result.getString("PR_TOKEN"),
						result.getTimestamp("PR_TIME").toLocalDateTime(),
						new Employee(
								result.getInt("U_ID"),
								result.getString("U_FIRSTNAME"),
								result.getString("U_LASTNAME"),
								result.getString("U_USERNAME"),
								result.getString("U_PASSWORD"),
								result.getString("U_EMAIL")
								)
						);
			}
			logger.trace("Employee token returned successfully.");
		} catch (SQLException e){
			logger.error("Exception at EmployeeRepositoryjbdc.selectEmployeeToken", e);
		}
		return null;
	}

	public static void main(String[] args) {
		EmployeeRepositoryjbdc er = new EmployeeRepositoryjbdc();
		Employee emp = new Employee(41, "anthony", "pena", "a", "1", "penaa@gmail.com",new EmployeeRole(2,"MANAGER"));
		int num =43;
		//System.out.println(er.getInstance().select(num));
		//Set<Employee> s = er.selectAll();
		//System.out.println(s);

		//Insert Employee Token Testing
		//er.getInstance().insertEmployeeToken(new EmployeeToken(1, "I Love Anthony", LocalDateTime.now(), new Employee(41, "anthony", "pena", "a", "1", "penaa@gmail.com",new EmployeeRole(2,"MANAGER"))));

		// Select Employee Token TEST
		//System.out.println(er.getInstance().selectEmployeeToken(new EmployeeToken(1, "I Love Anthony", LocalDateTime.now(), new Employee(41, "anthony", "pena", "antman", "1", "penaa@gmail.com",new EmployeeRole(2,"MANAGER")))));

		// Delete Employee Token Test
//		/try {
//			er.getInstance().deleteEmployeeToken(new EmployeeToken(1, "I Love Anthony", LocalDateTime.now(), new Employee(41, "anthony", "pena", "a", "1", "penaa@gmail.com",new EmployeeRole(2,"MANAGER"))));
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
