package com.medas.mi.utils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.medas.mi.model.PatientInfo;  

/**
 * @author Munquasim Alam
 *
 */
public class DbUtil {
	private static final Logger logger = Logger.getLogger(DbUtil.class);
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/elab_ghi";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "medteam2013";

	public static void insertRecordInEquipResults(int machineId,String barcodeno,String equip_param,String results) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String insertTableSQL = "INSERT INTO equip_results (equip_id, barcodeno, equip_param,results,read_status, created_dttm) VALUES(?,?,?,?,?,?)";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(insertTableSQL);

			preparedStatement.setInt(1, machineId);
			
			preparedStatement.setString(2, barcodeno);
			preparedStatement.setString(3, equip_param);
			preparedStatement.setString(4, results);
			preparedStatement.setInt(5, 0);
			preparedStatement.setTimestamp(6, getCurrentTimeStamp());
			// execute insert SQL stetement
			preparedStatement.executeUpdate();

			System.out.println("Record is inserted into equip_results table!");
		
		} catch (SQLException ex) {
			System.out.println("insertRecordInEquipResults SQLException:"+ ex.getMessage());
			logger.error("insertRecordInEquipResults SQLException:", ex);
		} finally {
			
			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}
	
	public static List<PatientInfo> getPatientInfo(int machineId,String barcode) throws SQLException {
		List<PatientInfo> patienList = new ArrayList<PatientInfo>();

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String selectSQL = "SELECT * FROM patient_info where machine_id = ? AND barcode = ?";

		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, machineId);
			preparedStatement.setString(2, barcode);

			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PatientInfo patientInfo = new PatientInfo();
				patientInfo.setPatientId(rs.getInt("patient_id"));
				patientInfo.setName(rs.getString("name"));
				patientInfo.setAge(rs.getString("age"));
				patientInfo.setSex(rs.getString("sex"));
				patientInfo.setDob(rs.getString("dob"));
				patientInfo.setBarcode(rs.getString("barcode"));
				patientInfo.setMachineId(rs.getInt("machine_id"));
			    patienList.add(patientInfo);

			}

			} catch (SQLException ex) {
			System.out.println("getPatientInfo SQLException:"+ ex.getMessage());
			logger.error("getPatientInfo SQLException:", ex);
	
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
	}
		return patienList;

	}

	private static Connection getDBConnection() {

		Connection dbConnection = null;
		try {
			Class.forName(DB_DRIVER);
			dbConnection = DriverManager.getConnection( DB_CONNECTION, DB_USER,DB_PASSWORD);
		} catch (ClassNotFoundException ex) {
			System.out.println("getDBConnection ClassNotFoundException:"+ ex.getMessage());
			logger.error("getDBConnection ClassNotFoundException:", ex);
		} catch (SQLException ex) {
			System.out.println("getDBConnection SQLException:"+ ex.getMessage());
			logger.error("getDBConnection SQLException:", ex);
		}
		return dbConnection;
	}

	private static java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}


}
