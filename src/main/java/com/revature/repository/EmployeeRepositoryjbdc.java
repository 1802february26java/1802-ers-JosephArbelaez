package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.EmployeeToken;
import com.revature.util.ConnectionUtil;

import oracle.sql.DATE;

public class EmployeeRepositoryjbdc implements EmployeeRepository{

	private static EmployeeRepositoryjbdc repository = new EmployeeRepositoryjbdc();
	private static Logger logger = Logger.getLogger(EmployeeRepositoryjbdc.class);

	// Constructor
	private EmployeeRepositoryjbdc() {}

	public static EmployeeRepositoryjbdc getInstance(){
		return repository;
	}

	@Override
	public boolean insert(Employee employee) throws SQLException {
		logger.trace("Inserting new employee.");

		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql = "INSERT INTO USER_T(U_FIRSTNAME, U_LASTNAME, U_USERNAME, U_PASSWORD, U_EMAIL) VALUES (?,?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, employee.getFirstName());
			statement.setString(++parameterIndex, employee.getLastName());
			statement.setString(++parameterIndex, employee.getUsername());
			statement.setString(++parameterIndex, employee.getPassword());
			statement.setString(++parameterIndex, employee.getEmail());

			if (statement.executeUpdate() > 0) {
				logger.info("Insert success");
				return true;
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			logger.error("Exception thrown while inserting new User", e);}
		return false;

	}

	@Override
	public boolean update(Employee employee) {
		logger.trace("Updating new employee.");

		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql = "UPDATE USER_T SET(U_FIRSTNAME = ?, U_LASTNAME = ?, U_USERNAME = ?, U_PASSWORD = ? , U_EMAIL =?) WHERE U_ID =?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, employee.getFirstName());
			statement.setString(++parameterIndex, employee.getLastName());
			statement.setString(++parameterIndex, employee.getUsername());
			statement.setString(++parameterIndex, employee.getPassword());
			statement.setString(++parameterIndex, employee.getEmail());

			if (statement.executeUpdate() > 0) {
				logger.info("Update success");
				return true;
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			logger.error("Exception thrown while updating user", e);}
		return false;
	}

	@Override
	public Employee select(int employeeId) {
		logger.trace("Selecting employee.");
		String empID = Integer.toString(employeeId);
		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql = "SELECT * FROM USER_T WHERE U_ID =?";
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
						result.getString("U_EMAIL"));
		}
	} catch (SQLException e) {
		logger.error("Exception thrown while updating user", e);}



	@Override
	public Employee select(String username) {
		logger.trace("Selecting employee.");

		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql = "SELECT * FROM USER_T WHERE U_USERNAME =?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, username);
			ResultSet result = statement.executeQuery();

			if(result.next())
				return new Employee(
						result.getInt("U_ID"),
						result.getString("U_FIRSTNAME"),
						result.getString("U_LASTNAME"),
						result.getString("U_USERNAME"),
						result.getString("U_PASSWORD"),
						result.getString("U_EMAIL"));

		} catch (SQLException e) {
			logger.error("Exception thrown while updating user", e);}
	}


	@Override
	public Set<Employee> selectAll() {
		try(Connection connection = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM USER_T";
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
						result.getString("U_EMAIL"));
			}
		}catch (SQLException e){

		}
	}

	@Override
	public String getPasswordHash(Employee employee) {
		String password = employee.getPassword();
		password = Integer.toString(password.hashCode());
		
		return password;
	}

	@Override
	public boolean insertEmployeeToken(EmployeeToken employeeToken) {
		logger.trace("Inserting new employee.");

		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-DD HH:mm");
			LocalDateTime dateTime = employeeToken.getCreationDate();
			String formattedDateTime = dateTime.format(formatter);
			
			String sql = "INSERT INTO PASSWORD_RECOVERY(PR_ID, PR_TOKEN, PR_TIME, U_ID) VALUES (?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, Integer.toString(employeeToken.getId()));
			statement.setString(++parameterIndex, employeeToken.getToken());
			statement.setString(++parameterIndex, formattedDateTime);
			statement.setString(++parameterIndex, Integer.toString(employeeToken.getRequester().getId()));

			if (statement.executeUpdate() > 0) {
				return true;
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			logger.error("Exception thrown while inserting new User", e);}
		return false;
	}

	@Override
	public boolean deleteEmployeeToken(EmployeeToken employeeToken) {
		try(Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql ="DELETE FROM PASSWORD_RECOVERY WHERE PR_ID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setString(++parameterIndex, Integer.toString(employeeToken.getId()));
		}
		return false;
	}

	@Override
	public EmployeeToken selectEmployeeToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return null;
	}

}
