package com.medas.mi.listener;


import org.apache.log4j.Logger;

import com.medas.mi.SerialPortControls;
import com.medas.mi.utils.AlertUtil;
import com.medas.mi.utils.DateUtil;
import com.medas.mi.utils.DbUtil;

import javafx.scene.control.TextArea;
//import javafx.scene.control.TextArea;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
 
public class Clinitek implements SerialPortEventListener {
	private static final Logger logger = Logger.getLogger(Clinitek.class);
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

public Clinitek(String machineId, String portName, String bitsPerSecond, String dataBits, String parity,
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
		SerialPortControls.showReceivedData(textArea, strReceived);
		for (int i = 0; i < strReceived.length(); i++) {
			char  charReceived =strReceived.charAt(i);
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
			default:
				strDataReceived = charReceived == (char) 13 ? strDataReceived.trim() + "," + charReceived
						: strDataReceived.trim() + charReceived;
				break;
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

public synchronized void saveData(String str)
{
     String[] fields_of_o = null;
    
     String[] fields = null;
     String[] records =null;
     String[] components = null;
    String[] rarray = new String[10];
    String  LIMT_TestID;
    String  LIMO_Results = null;
    String  LIMO_Alarm;
    String  LIMO_Units;
    String  equip_param = null;  
    try
    {
    	records = str.split(String.valueOf((char)13)); // 13>  CR  (carriage return) 
         for (int j = 0; j <= records.length - 1; j++)
        {
        	System.out.println("**********records:" + records[j]);
            if (records[j].length() > 1)
            {
                String recChr = records[j].substring(0, 1);
                switch (recChr)
                {
                    case "H":
                        LI_InterfaceStatus = 1;
                          break;
                    case "R":
                      	fields = records[j].split("\\|");
                    	System.out.println("fields:"+fields);
                    	components = fields[2].split("\\^");
                    	  LIMO_Results = fields[3].trim();
                          System.out.println("LIMO_Results:"+LIMO_Results);
                       
                    	equip_param = components[3].trim();
                        System.out.println("equip_param:"+equip_param);
                              LIMO_Alarm = "";
                            LIMO_Units = fields[4].toString().trim();
                          
                        LIMO_Alarm = LIMO_Results.trim();
                            System.out.println("LIMO_LabID in R:"+LIMO_LabID);
                        if(LIMO_LabID!=null && !"".equals(LIMO_LabID)){
                        	DbUtil.insertRecordInEquipResults(machineId,LIMO_LabID.trim(), equip_param, LIMO_Results);
                        	SerialPortControls.showReceivedData(textArea, DateUtil.getCurrentDateAndTime() + " :Barcode: " + LIMO_LabID + " :Test Name : " +equip_param + " :Test Result: " +LIMO_Results);
                        	logger.info("machineId:"+ machineId +" :Barcode: " + LIMO_LabID + " :Test Name : " +equip_param + " :Test Result: " +LIMO_Results);
         		             equip_param = null;
                        	LIMO_Results = null;
                        }
                        
					   break;
                    case "O":
                        LI_InterfaceStatus = 3;
                        fields_of_o = records[j].split("\\|");
                         LIMO_LabID = fields_of_o[2].trim();
                     
                        break;
                    case "P":
                        break;
                    case "L":
                        LI_InterfaceStatus = 6;
                        break;
                    default:
                        break;
                }; 
            }
        }
    }
    catch (Exception ex)
    {
    	 logger.error("Data saving error", ex);
      System.out.println("Data saving error:" + ex.getMessage()); 
    }
}
	public boolean isConnected() {
 		return isConnected;
 	}
    }
