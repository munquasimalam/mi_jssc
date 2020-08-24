
package com.medas.mi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.medas.mi.utils.AlertUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * This class is used to set all the properties to Serial port.
 * 
 * @author Munquasim Alam
 */
public class PortSettingComponent {

	static final Logger logger = Logger.getLogger(PortSettingComponent.class);
	public  Map<String, String> portSetting = new HashMap<String, String>();
	private String machineName = "";
        private String machineId = "";
	private String comPort = "";
	private String bitsPerSecond = "";
	private String dataBits = "";
	private String parity = "";
	private String stopBit = "";
	private String isPrintLog = "";
	public static String instrumentName = "";
	
	
	
	/**
	 * This method is used to create a window where we set properties of serial
	 * port.
	 * 
	 * @throws PortNotFoundException
	 *             if no any port are available in the machine then it gives
	 *             PortNotFoundException.
	 * @throws NoSuchPortException 
	 */
	public  void createPortSettingForm(boolean isPermanentSetting, Button removeBtn,  Map<Integer, Map<String, String>> machineNames, List<String> portNames) {

		final Button apply = new Button("Apply");
                final Button cancel = new Button("Cancel");
                
		final Label machineNameALert = new Label();
                final Label machineIdALert = new Label();
		final Label comPortALert = new Label();
		final Label bitsPerSecondAlert = new Label();
		final Label dataBitsAlert = new Label();
		final Label parityALert = new Label();
		final Label stopBitALert = new Label();
		final Label isPrintLogALert = new Label();

		Stage serialPortSettingStage = new Stage();
		serialPortSettingStage.setTitle("Port Settings");
                

		//Scene scene = new Scene(new Group(), 293, 430);
                Scene scene = new Scene(new Group(), 295, 440);
                 scene.getStylesheets().add(getClass().getClassLoader().getResource("com/medas/mi/css/login.css").toExternalForm());
                 apply.setId("btnLogin");
                // apply.getStyleClass().add("button-style-no");
                  //cancel.getStyleClass().add("btnLogin");
                   cancel.setId("btnLogin");

		
		TextField machineIdTextField = new TextField();
		try { 
	
		final ComboBox machineNameComboBox = new ComboBox();
		machineNameComboBox.getItems().addAll("Cobas6000", "CobasB221", "CobasC311","CobasC111",
				"CobasE411", "CobasP612", "Urisys1100","Minividas");

		machineNameComboBox.setPromptText("select machine name");
		machineNameComboBox.setEditable(true);
		machineNameComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String oldValue, String currentValue) {
				machineName = currentValue;
			    instrumentName = currentValue;
				System.out.println("***********currentValue:" + currentValue);
				System.out.println("***********ov:" + ov);
				System.out.println("***********oldValue:" + oldValue);

			}
		});

		//List<String> machinePorts = new CommPortIdentifierFinder().findMachinePorts(isPermanentSetting, portNames);
		//System.out.print("machinePorts:" + machinePorts);
	   ComboBox comPortComboBox = new ComboBox();
	   //comPortComboBox.getItems().addAll("COM8","COM9");
	   comPortComboBox.getItems().addAll(portNames);
           System.out.println("***********portNames122222:" + portNames);
//		for(String portName : portNames) {
//			comPortComboBox.getItems().add(portName);
//		}
		
		//comPortComboBox.getItems().addAll(portNames);
		comPortComboBox.setPromptText(" select com port");
		comPortComboBox.setEditable(true);
		//bitsPerSecond = bitsPerSecondComboBox.getValue().toString();
		comPortComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String oldValue, String currentValue) {
				comPort = currentValue;
				System.out.println("***********currentValue:" + currentValue);
				System.out.println("***********ov:" + ov);
				System.out.println("***********oldValue:" + oldValue);

			}
		});

	   ComboBox bitsPerSecondComboBox = new ComboBox();
		bitsPerSecondComboBox.getItems().addAll("1200", "2400", "4800", "9600", "19200", "38400");	
		bitsPerSecondComboBox.setPromptText("Select bit per second");
		bitsPerSecondComboBox.setEditable(true);
		bitsPerSecondComboBox.setValue("9600");
		bitsPerSecond = bitsPerSecondComboBox.getValue().toString();
		bitsPerSecondComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String oldValue, String currentValue) {
				bitsPerSecond = currentValue;
				System.out.println("***********currentValue:" + currentValue);
				System.out.println("***********ov:" + ov);
				System.out.println("***********oldValue:" + oldValue);

			}
		});

		 ComboBox dataBitsComboBox = new ComboBox();
		dataBitsComboBox.getItems().addAll("8", "7");
		dataBitsComboBox.setPromptText(" select data bits");
		dataBitsComboBox.setEditable(true);
		dataBitsComboBox.setValue("8");
		dataBits = dataBitsComboBox.getValue().toString();
		dataBitsComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String oldValue, String currentValue) {
				dataBits = currentValue;
				System.out.println("***********currentValue:" + currentValue);
				System.out.println("***********ov:" + ov);
				System.out.println("***********oldValue:" + oldValue);
			}
		});

		final ComboBox parityComboBox = new ComboBox();
		parityComboBox.getItems().addAll("None", "Odd", "Even");
		parityComboBox.setPromptText("select parity");
		parityComboBox.setEditable(true);
		parityComboBox.setValue("None");
		parity = parityComboBox.getValue().toString();
		
		parityComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String oldValue, String currentValue) {

				parity = currentValue;
				System.out.println("***********parity currentValue1234:" + currentValue);
				System.out.println("***********ov:" + ov);
				System.out.println("***********oldValue:" + oldValue);
			}
		});

		final ComboBox stopBitComboBox = new ComboBox();
		stopBitComboBox.getItems().addAll("One", "Two");
		stopBitComboBox.setPromptText("select stop bit");
		stopBitComboBox.setEditable(true);
		stopBitComboBox.setValue("One");
		stopBit = stopBitComboBox.getValue().toString();
		stopBitComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String oldValue, String currentValue) {
				stopBit = currentValue;
				System.out.println("***********currentValue:" + currentValue);
				System.out.println("***********ov:" + ov);
				System.out.println("***********oldValue:" + oldValue);
			}
		});

		final ComboBox logComboBox = new ComboBox();
		logComboBox.getItems().addAll("Yes", "No");
		logComboBox.setPromptText("select log");
		logComboBox.setEditable(true);
		logComboBox.setValue("Yes");
		isPrintLog = logComboBox.getValue().toString();
		logComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue ov, String oldValue, String currentValue) {
				isPrintLog = currentValue;
				System.out.println("***********currentValue:" + currentValue);
				System.out.println("***********ov:" + ov);
				System.out.println("***********oldValue:" + oldValue);
			}
		});
		     apply.setOnAction((event) -> {
			
				if (machineName != null && !machineName.isEmpty()) {
					machineNameALert.setText("Machine: " + machineName + " set Successfully");
					portSetting.put("machineName", machineName);
					// machineNames.add(machineName);
	                 System.out.println("machineNames Set connect:" +machineNames);
					System.out.println("***********machineName:" + machineName);
					machineName = null;
				} else {
					//machineNameALert.setText("Please select Machine Name.");
                                        machineName = AlertUtil.choiceAlertMachineName();
					portSetting.put("machineName",machineName);
                                         machineNameComboBox.setValue(machineName);
					System.out.println("***********machineName:" + machineName);

				}
				if (machineIdTextField.getText() != null && !machineIdTextField.getText().isEmpty()) {
                                    machineIdALert.setText("Machine Id: " + machineIdTextField.getText() + " set Successfully");
					
						portSetting.put("machineId", machineIdTextField.getText());
					System.out.println("***********machineId:" + machineIdTextField.getText());
					
				} else {
					machineId = AlertUtil.textInputAlert();
					portSetting.put("machineId",machineId);
                                          machineIdTextField.setText(machineId);
					System.out.println("***********machineId:" + machineIdTextField.getText());
				}
				if (comPort != null && !comPort.isEmpty()) {
					comPortALert.setText("Com Port: " + comPort + " set Successfully");
					portSetting.put("comPort", comPort);
					System.out.println("***********comPort:" + comPort);
					comPort = null;
				} else {
                                    comPort = AlertUtil.choiceAlertPortName(portNames);
					portSetting.put("comPort", comPort);
                                        comPortComboBox.setValue(comPort);
					
					//comPortALert.setText("Please  select  com Port.");
					System.out.println("***********comPort:" + comPort);
				}

				if (bitsPerSecond != null && !bitsPerSecond.isEmpty()) {
					bitsPerSecondAlert.setText("Bits Per Sec: " + bitsPerSecond + " set Successfully");
					portSetting.put("bitsPerSecond", bitsPerSecond);
					System.out.println("***********bitsPerSecond:" + bitsPerSecond);
					bitsPerSecond = "";
				} else {
                                    
					bitsPerSecondAlert.setText("Please  select  bits Per Second!");
					System.out.println("***********bitsPerSecond:" + bitsPerSecond);
				}
				if (dataBits != null && !dataBits.isEmpty()) {
					dataBitsAlert.setText("Data Bits : " + dataBits + " set Successfully");
					portSetting.put("dataBits", dataBits);
					System.out.println("***********dataBits:" + dataBits);
					dataBits = "";
				} else {
					dataBitsAlert.setText("Please  select  dataBits!");
					System.out.println("***********dataBits:" + dataBits);
				}
				if (parity != null && !parity.isEmpty()) {
					parityALert.setText("Parity: " + parity + " set Successfully");
					String parity_str = "Odd".equalsIgnoreCase(parity) ? "1" : "Even".equalsIgnoreCase(parity) ? "2" : "0";
					portSetting.put("parity", parity_str);
					System.out.println("***********parity:" + parity);
					parity = null;
				} else {
					parityALert.setText("Please  select parity!");
					System.out.println("***********parity:" + parity);
				}
				if (stopBit != null && !stopBit.isEmpty()) {
					stopBitALert.setText("Stop Bit : " + stopBit + " set Successfully");
					String stopBit_str = "Two".equalsIgnoreCase(stopBit) ? "2" : "1";
					portSetting.put("stopBit", stopBit_str);
					System.out.println("***********stopBit:" + stopBit);
					stopBit = null;
				} else {
					stopBitALert.setText("Please  select stopBit!");
					System.out.println("***********isPrintLog:" + stopBit);
				}

				if (isPrintLog != null && !isPrintLog.isEmpty()) {
					isPrintLogALert.setText("Print Log: " + isPrintLog + " set Successfully");
					portSetting.put("isPrintLog", isPrintLog);
					System.out.println("***********isPrintLog:" + isPrintLog);
					isPrintLog = null;
				} else {
					isPrintLogALert.setText("Please  select Print Log!");
					System.out.println("***********isPrintLog:" + isPrintLog);
				}
				
				//machineNames.put(Integer.parseInt(removeBtn.getId()), portSetting);
				Map<String,String> setting = new HashMap<String,String>();
				portSetting.forEach((k,v) -> {
					setting.put(k, v);
				});
				machineNames.put(Integer.parseInt(removeBtn.getId()), setting);
				
				
				System.out.println("machineNames in apply:" +machineNames);
				//XmlUtil.writeXmlFile(portSetting);

		
		});
                     
                     cancel.setOnAction((event) -> {
                        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();      
 
                     });
                             
                     

		GridPane grid = new GridPane();
		//grid.setStyle("-fx-background-color: #87CEFA;");
		//grid.setStyle("-fx-background-color: #D8BFD8;");
		grid.setStyle("-fx-background-color: #87CEFA;");
               // grid.setStyle("-fx-background-color:  transparent;");
               
		//grid.setStyle("-fx-background-color: #CD5C5C;");
		grid.setVgap(4);
		grid.setHgap(10);
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.add(new Label("Machine Name"), 0, 0);
		grid.add(machineNameComboBox, 1, 0);
		grid.add(new Label("Machine Id"), 0, 1);
		grid.add(machineIdTextField, 1, 1);
		grid.add(new Label("Port Name"), 0, 2);
		grid.add(comPortComboBox, 1, 2);
		grid.add(new Label("Bit per second"), 0, 3);
		grid.add(bitsPerSecondComboBox, 1, 3);
		grid.add(new Label("Data bit"), 0, 4);
		grid.add(dataBitsComboBox, 1, 4);
		grid.add(new Label("Parity: "), 0, 5);
		grid.add(parityComboBox, 1, 5);
		grid.add(new Label("Stop bit: "), 0, 6);
		grid.add(stopBitComboBox, 1, 6);
		grid.add(new Label("Log: "), 0, 7);
		grid.add(logComboBox, 1, 7);
		grid.add(apply, 0, 8);
                grid.add(cancel, 1, 8);
               
		grid.add(machineNameALert, 1, 9, 3, 1);
                grid.add(machineIdALert, 1, 10, 3, 1);
		grid.add(comPortALert, 1, 11, 3, 1);
		grid.add(bitsPerSecondAlert, 1, 12, 3, 1);
		grid.add(dataBitsAlert, 1, 13, 3, 1);
		grid.add(parityALert, 1, 14, 3, 1);
		grid.add(stopBitALert, 1, 15, 3, 1);
		grid.add(isPrintLogALert, 1, 16, 3, 1);
		Group root = (Group) scene.getRoot();
		root.getChildren().add(grid);
                
		//root.setId("root");
	   
	    
		serialPortSettingStage.setScene(scene);
		serialPortSettingStage.initStyle(StageStyle.UTILITY);
		    
		serialPortSettingStage.show();
		
     } catch (Exception ex) {
    	 AlertUtil.exceptionAlert(ex);
			logger.error("createPortSettingForm Exception", ex);
			System.out.println("createPortSettingForm Exception" + ex.getMessage());	
		}

	}

	public Map<String, String> getPortSetting() {
		return portSetting;
	}


	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public void setComPort(String comPort) {
		this.comPort = comPort;
	}

	public void setBitsPerSecond(String bitsPerSecond) {
		this.bitsPerSecond = bitsPerSecond;
	}

	public void setDataBits(String dataBits) {
		this.dataBits = dataBits;
	}

	public void setParity(String parity) {
		this.parity = parity;
	}

	public void setStopBit(String stopBit) {
		this.stopBit = stopBit;
	}

	public void setIsPrintLog(String isPrintLog) {
		this.isPrintLog = isPrintLog;
	}

	public static void setInstrumentName(String instrumentName) {
		PortSettingComponent.instrumentName = instrumentName;
	}
	
	

	public void setPortSetting(Map<String, String> portSetting) {
		this.portSetting = portSetting;
	}

	
}
