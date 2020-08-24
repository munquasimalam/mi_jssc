package com.medas.mi.listener;

import java.util.List;

import org.apache.log4j.Logger;

import com.medas.mi.SerialPortControls;
import com.medas.mi.model.PatientInfo;
import com.medas.mi.utils.AlertUtil;
import com.medas.mi.utils.DateUtil;
import com.medas.mi.utils.DbUtil;

import javafx.scene.control.TextArea;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class CobasC111 implements SerialPortEventListener {
	private static final Logger logger = Logger.getLogger(CobasC111.class);
		// bits per second for port OR Baud rate
	private int portDataRate;
	// port name
	private String portName;
	private int machineId = 0;
	private int dataBits;
	private int parity;
	private int stopBit;
	private String isPrintLog;
	private boolean isConnected = false;
	// text area to display data comming from machine.
	private TextArea textArea = null;
	static String strDataReceived = "";
	static String strETBString = "";
	static String QPatientID = "";
	static String RPatientID = "";
	static String strSaveString = "";
	static int eot = 0;
	char sendChar = '0';
	int CheckSumValue;
	int intStatus = 0;
	String strDispPatientid = "";

	public static String LI_QPatientID = "";
	public int LI_InterfaceStatus = 0;
	public String LI_DispPatientID = "";
	public static String LI_DataReceived = "";
	public static String LI_ETBString = "";
	public long LI_CheckSumValue;
	public static String LI_SaveString = "";
	public static String LI_strDataReceived = "";
	public static String LI_ResultPatientID = "";
	public static int LI_Eot;
	public char LI_SendChar;
	String[] componentOfQ = null;
	String patientTestList = "";
	String LIMO_LabID = "";
	SerialPort serialPort = null;
	private boolean  barCodeFlag = false;

	public CobasC111(String machineId, String portName, String bitsPerSecond, String dataBits, String parity,
			String stopBit, String isPrintLog, TextArea textArea) {
		logger.info("machineId:" + machineId + "portName:" + portName + "bitsPerSecond:" + bitsPerSecond + "dataBits:"
				+ dataBits + "parity:" + parity + "stopBit:" + stopBit + "isPrintLog:" + isPrintLog);
		this.portDataRate = Integer.parseInt(bitsPerSecond);
		this.machineId = Integer.parseInt(machineId);
		this.portName = portName;
		this.dataBits = Integer.parseInt(dataBits);
		this.parity = Integer.parseInt(parity);
		this.stopBit = Integer.parseInt(stopBit);
		this.isPrintLog = isPrintLog;
		this.textArea = textArea;
	}

	public synchronized void initialize() {
		logger.info("initialize method:");
		isConnected = false;
		serialPort = new SerialPort(portName);
		try {
			serialPort.openPort();
			// serialPort.setParams(9600, 8, 1, 0);
			serialPort.setParams(portDataRate, dataBits, stopBit, parity);
			// Add an interface through which we will receive information about
			// events
			serialPort.addEventListener(this);
			isConnected = true;
		} catch (SerialPortException ex) {
			AlertUtil.exceptionAlert(ex);
			logger.error("initialize SerialPortException", ex);
			System.out.println("initialize SerialPortException" + ex.getMessage());
		} catch (Exception ex) {
			AlertUtil.exceptionAlert(ex);
			logger.error("initialize Exception", ex);
			System.out.println("initialize Exception" + ex.getMessage());
		}
	}

	public void closeSerialPort() {
		try {
			if (serialPort != null && serialPort.isOpened()) {
				serialPort.removeEventListener();
				serialPort.closePort();
			}
		} catch (SerialPortException ex) {
			AlertUtil.exceptionAlert(ex);
			logger.error("closeSerialPort SerialPortException", ex);
			System.out.println("closeSerialPort SerialPortException" + ex.getMessage());
		} catch (Exception ex) {
			AlertUtil.exceptionAlert(ex);
			logger.error("closeSerialPort Exception", ex);
			System.out.println("closeSerialPort Exception" + ex.getMessage());
		}
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		try {
			String strReceived = serialPort.readString();
			if(strReceived != null) {
			//SerialPortControls.showReceivedData(textArea, strReceived);
			for (int i = 0; i < strReceived.length(); i++) {
				char charReceived = strReceived.charAt(i);
				switch (charReceived) {
				case (char) 2: // STX (start of text)
					strDataReceived = "";
					break;
				case (char) 5: // ENQ (enquiry)
					serialPort.writeInt(6);
					break;
				case (char) 4: // EOT (end of transmission)
					if (LI_QPatientID != null && LI_QPatientID != "") {
						eot = 1;
						serialPort.writeInt(5);
					}
					break;
				case (char) 23: // ETB (end of trans. block > end of text of
								// end entermediate frame)
					LI_ETBString = strDataReceived.substring(1, strDataReceived.length());
					LI_SaveString = LI_SaveString + LI_ETBString;
					LI_ETBString = "";
					strDataReceived = "";
					serialPort.writeInt(6);
					break;
				case (char) 3: // ETX (end of text > of end frame)
					serialPort.writeInt(6);
					LI_ETBString = strDataReceived.substring(1, strDataReceived.length());
					LI_SaveString = LI_SaveString + LI_ETBString;
					strDataReceived = "";
					LI_ETBString = "";
					saveData(LI_SaveString);
					LI_SaveString = "";
					break;
				case (char) 6:
					if (eot == 1 && LI_QPatientID != null) {
						SendTest(LI_QPatientID, serialPort);
						}
					break;
				default:
					strDataReceived = charReceived == (char) 13 ? strDataReceived.trim() + "," + charReceived
							: strDataReceived.trim() + charReceived;
					break;
				}
			}
			}
		} catch (SerialPortException ex) {
			logger.error("serialEvent SerialPortException", ex);
			System.out.println("serialEvent SerialPortException" + ex);

		} catch (Exception ex) {
			logger.error("serialEvent Exception", ex);
			System.out.println("serialEvent Exception" + ex.getMessage());
		}

	}

	/**
	 * used to save record in db in case of test Result used to take barcode
	 * from Q record in case of test Request
	 * 
	 * @param str
	 *            represents records
	 * 
	 */
	public synchronized void saveData(String str) {
		String[] fields_of_o;
		String[] fields = null;
		String[] records;
		String[] components;
		String LIMO_Results = "";
		String equip_param = "";
		String[] fieldsOfQ = null;
		try {
			records = str.split(","); // 13> CR (carriage return)
			System.out.println("records:"+records);
			for (int j = 0; j <= records.length - 1; j++) {
				System.out.println("(records[j]:"+records[j]);
				if (records[j].length() > 1) {
					String recChr = records[j].substring(0, 1);
					switch (recChr) {
					case "H":
						LI_InterfaceStatus = 1;
						break;
					case "R":
						fields = records[j].split("\\|");
						System.out.println("fields:" + fields);
						components = fields[2].split("\\^");
						LIMO_Results = fields[3].trim();
						System.out.println("LIMO_Results:" + LIMO_Results);

						equip_param = components[3].trim().split("\\/")[0];
						System.out.println("equip_param:" + equip_param);

						System.out.println("LIMO_LabID in R:" + LIMO_LabID);
						if (LIMO_LabID != null && !"".equals(LIMO_LabID)) {
							DbUtil.insertRecordInEquipResults(machineId, LIMO_LabID.trim(), equip_param, LIMO_Results);
							SerialPortControls.showReceivedData(textArea, DateUtil.getCurrentDateAndTime() + " :Barcode: " + LIMO_LabID + " :Test Name : " +equip_param + " :Test Result: " +LIMO_Results);
							logger.info("machineId:"+ machineId +" :Barcode: " + LIMO_LabID + " :Test Name : " +equip_param + " :Test Result: " +LIMO_Results);
					        equip_param = null;
							LIMO_Results = null;
						}

						break;
					case "O":
						LI_InterfaceStatus = 3;
						fields_of_o = records[j].split("\\|");
						LIMO_LabID = fields_of_o[3].trim();

						break;
					case "P":
						break;
					case "Q":
						// fieldsOfQ = str.split("\\|");
						fieldsOfQ = records[0].split("\\|");
						// componentOfQ = fieldsOfQ[12].split("\\^");
						componentOfQ = fieldsOfQ[2].split("\\^");

						if (fieldsOfQ[12].trim() != "O") {
							// LI_QPatientID = componentOfQ[1];
							LI_QPatientID = componentOfQ[1];
						} else {
							LI_QPatientID = null;
						}
						System.out.println("Query  :" + LI_QPatientID);
						break;
					case "L":
						LI_InterfaceStatus = 6;
						break;
					default:
						break;
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Data saving error", ex);
			System.out.println("Data saving error:" + ex.getMessage());
		}
	}

	/**
	 * used to send test to instrument
	 * 
	 * @param qPatientID
	 *            represent barcode of Q
	 * 
	 * @param outputStream
	 *            to write data
	 * 
	 * 
	 */
	public void SendTest(String qPatientID, SerialPort serialPort) {
		try {
			List<PatientInfo> patienList = DbUtil.getPatientInfo(machineId, LI_QPatientID);
			if(patienList.size() > 0 && patienList.get(0).getBarcode() != null && !barCodeFlag) {
				SerialPortControls.showReceivedData(textArea, DateUtil.getCurrentDateAndTime()+ ":Barcode of Query: " + patienList.get(0).getBarcode());
				barCodeFlag = true;
				logger.info("barcode of Q :" + patienList.get(0).getBarcode());
				}
		
			if (patienList.size() > 0) {
				patientInfo(patienList);
				LI_InterfaceStatus = 4;
				LI_DispPatientID = qPatientID;
				String str = "";
				int frameNumber = 0;

				frameNumber = (frameNumber + 1) % 8;
				str = String.valueOf(frameNumber) + "H|\\^&||||||||||P||" + String.valueOf(((char) 13))
						+ String.valueOf(((char) 3));
				str = String.valueOf((char) 2) + str + CheckSum(str) + String.valueOf((char) 13)
						+ String.valueOf((char) 10);
				System.out.println("strOut h:" + str);
				writeString(str, serialPort);

				frameNumber = (frameNumber + 1) % 8;
				str = "";
				str = String.valueOf(frameNumber) + "P|1" + String.valueOf(((char) 13)) + String.valueOf(((char) 3));
				str = String.valueOf((char) 2) + str + CheckSum(str) + String.valueOf((char) 13)
						+ String.valueOf((char) 10);
				System.out.println("strOut p:" + str);
				writeString(str, serialPort);
				frameNumber = (frameNumber + 1) % 8;
				str = "";
				str = String.valueOf(frameNumber) + "O|1|" + qPatientID + "|" + componentOfQ[1] + "^"+ "|";
				str = str + patientTestList;
				str = str + "|R||||||N||||||||||||||O" + String.valueOf(((char) 13)) + String.valueOf(((char) 3));
				str = String.valueOf((char) 2) + str + CheckSum(str) + String.valueOf((char) 13)
						+ String.valueOf((char) 10);
				System.out.println("strOut o:" + str);
				writeString(str, serialPort);

				frameNumber = (frameNumber + 1) % 8;
				str = "";
				str = String.valueOf(frameNumber) + "L|1|N" + String.valueOf(((char) 13)) + String.valueOf(((char) 3));
				str = String.valueOf((char) 2) + str + CheckSum(str) + String.valueOf((char) 13)
						+ String.valueOf((char) 10);
				System.out.println("strOut l:" + str);
				writeString(str, serialPort);
				Thread.sleep(100);
				serialPort.writeInt(4);

				eot = 0;
				str = "";
				LI_QPatientID = null;
			}
		} catch (Exception ex) {
			System.out.println(": SendTest : " + ex.getMessage());

		}
	}

	/**
	 * @param patienList
	 *            contains patient details
	 */
	public void patientInfo(List<PatientInfo> patienList) {
		patientTestList = "";
		try {
			String patientName = patienList.get(0).getName();
			String patientAge = patienList.get(0).getAge();
			String patientSex = patienList.get(0).getSex();
			patientName = patientName.replace(".", " ");
			if (patientName.trim().length() > 20) {
				patientName = patientName.substring(0, 20);
			}

			for (int i = 0; i < patienList.size(); i++) {
				patientTestList = patientTestList + "^^^" + patienList.get(i).getPatientId() + "^0\\";
			}
			if (patientTestList.length() > 0) {
				patientTestList = patientTestList.substring(0, patientTestList.length() - 1);
			}

		} catch (Exception ex) {
			logger.error("PatientInfo error:", ex);
			System.out.println(": PatientInfo : " + ex.getMessage());

		}
	}

	public String CheckSum(String str) {
		int x = 0;
		String hex = "";
		try {
			for (int i = 0; i < str.length(); i++) {
				int st = str.charAt(i);
				x = x + st;
			}
			hex = Integer.toHexString(x);
		} catch (Exception ex) {
			logger.error("CheckSum Exception:", ex);
			System.out.println("CheckSum Exception:" + ex.getMessage());

		}

		return hex.substring(hex.length() - 2).toUpperCase();
	}

	
	private void writeString(String str, SerialPort serialPort) {
		byte[] bytes = str.getBytes();
		try {
			for (int k = 0; k < bytes.length; k++) {
				serialPort.writeByte(bytes[k]);
				Thread.sleep(50);
			}
		} catch (InterruptedException ex) {
			logger.error("writeString InterruptedException:", ex);
			System.out.println("writeString InterruptedException:" + ex.getMessage());
		} catch (SerialPortException ex) {
			logger.error("writeString SerialPortException:", ex);
			System.out.println("writeString SerialPortException:" + ex.getMessage());
		} catch (Exception ex) {
			logger.error("writeString Exception:", ex);
			System.out.println("writeString Exception:" + ex.getMessage());
		}
	}

	public boolean isConnected() {
		return isConnected;
	}
}
