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
			String sql = "INSERT INTO USER_T(U_FIRSTNAME, U_LASTNAME, U_USERNAME, U_PASSWORD, U_EMAIL) VALUES (?,?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, employee.getFirstName());
			statement.setString(++parameterIndex, employee.getLastName());
			statement.setString(++parameterIndex, employee.getUsername());
			statement.setString(++parameterIndex, employee.getPassword());
			statement.setString(++parameterIndex, employee.getEmail());

			if (statement.executeUpdate() > 0) {
				logger.info("Insert successful!");
				return true;
			} else {
				throw new SQLException();
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
			String sql = "UPDATE USER_T SET(U_FIRSTNAME = ?, U_LASTNAME = ?, U_USERNAME = ?, U_PASSWORD = ? , U_EMAIL =?) WHERE U_ID =?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, employee.getFirstName());
			statement.setString(++parameterIndex, employee.getLastName());
			statement.setString(++parameterIndex, employee.getUsername());
			statement.setString(++parameterIndex, employee.getPassword());
			statement.setString(++parameterIndex, employee.getEmail());

			if (statement.executeUpdate() > 0) {
				logger.info("Update success!");
				return true;
			} else {
				throw new SQLException();
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
			String sql = "SELECT * FROM USER_T INNER JOIN USER_ROLE ON USER_T.UR_ID = USER_ROLE.UR_ID WHERE U_ID = ?";
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
			String sql = "SELECT * FROM USER_T INNER JOIN USER_ROLE ON USER_T.UR_ID = USER_ROLE.UR_ID WHERE USER_T.U_USERNAME = ?";
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
						result.getString("U_EMAIL"),
						new EmployeeRole(
								result.getInt("UR_ID"),
								result.getString("UR_TYPE")
								)
						);
			logger.error("Employee selection successful!");
		} catch (SQLException e) {
			logger.error("Exception thrown while updating user", e);
		}
		return null;
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
		String num = Integer.toBinaryString(employee.getId());
		String hash = "";
		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql = "SELECT U_PASSWORD FROM USER_T WHERE U_ID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, num);

			ResultSet result = statement.executeQuery();
			while(result.next()){
				hash = result.getString("U_PASSWORD");
			}
			logger.info("Password hash acquired!");
			return hash;
		} catch (SQLException e) {
			logger.error("Exception at EmployeeRepositoryjdbc.getPasswordHash", e);
		}
		return hash;
	}

	@Override
	public boolean insertEmployeeToken(EmployeeToken employeeToken) {
		logger.trace("Inserting new employee.");

		try (Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			LocalDateTime dateTime = employeeToken.getCreationDate();

			String sql = "INSERT INTO PASSWORD_RECOVERY(PR_ID, PR_TOKEN, PR_TIME, U_ID) VALUES (?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, Integer.toString(employeeToken.getId()));
			statement.setString(++parameterIndex, employeeToken.getToken());
			statement.setTimestamp(++parameterIndex, Timestamp.valueOf(employeeToken.getCreationDate()));
			statement.setString(++parameterIndex, Integer.toString(employeeToken.getRequester().getId()));

			if (statement.executeUpdate() > 0) {
				logger.error("EmployeeToken insert successful!");
				return true;
			} else {
				throw new SQLException();
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
			statement.executeUpdate();
			logger.error("Delete successful!");
			return true;
		} catch (SQLException e){
			logger.error("Exception at EmployeeRepository.deleteEmployeeToken");
			return false;
		}
	}

	@Override
	public EmployeeToken selectEmployeeToken(EmployeeToken employeeToken) {
		try(Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;

			String sql ="SELECT FROM PASSWORD_RECOVERY WHERE PR_ID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(++parameterIndex, Integer.toString(employeeToken.getId()));
			ResultSet result = statement.executeQuery();
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
								result.getString("U_EMAIL"),
								new EmployeeRole(
										result.getInt("UR_ID"),
										result.getString("UR_TYPE")
										)
								)
						);
			}
			logger.error("Employee token return successful.");
		} catch (SQLException e){
			logger.error("Exception at EmployeeRepositoryjbdc.selectEmployeeToken", e);
		}
		return null;
	}
}
