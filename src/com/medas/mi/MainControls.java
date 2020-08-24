
package com.medas.mi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.medas.mi.utils.AlertUtil;
import com.medas.mi.utils.DateUtil;
import com.medas.mi.utils.XmlUtil;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import jssc.SerialPortList;

/**
 * This class is used to open main Controls. which contains Menu,
 * MenuItems,FlowPane etc. Other Controls are added in FlowPane.
 * 
 * @author Munquasim Alam
 */

public class MainControls extends Application {
	static final Logger logger = Logger.getLogger(MainControls.class);
	private  FlowPane flowPaneMainControls = null;
	private SerialPortControls serialPortControls = null;
	private LoginComponent loginComponent = null;
	 Button loginBtn = null;
	 Button SerialPortBtn = null;
	 Button serverBtn = null;
	 Button clientBtn = null;
	 Button saveBtn = null;
	 List<String> portNames = null;

	/**
	 * main method to launch the app.
	 * 
	 * @param args
	 *            is array of String.
	 */
	public static void main(String[] args) {
		try {

			PropertyConfigurator.configure("log4j.properties");
			logger.info("MainControls Launched :");
			launch(MainControls.class, args);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

	}

	@Override
	public void init() {
		flowPaneMainControls = centerFlowPane();
		serialPortControls = new SerialPortControls();
		loginComponent = new LoginComponent();
		 portNames = getPortNames();
	}

	@Override
	public void start(Stage mainStage) {
		try {
			System.out.println("start:");
			// Using border pane as the root for scene
			BorderPane borderPane = new BorderPane();
			HBox hbox = addHBox();
			borderPane.setTop(hbox);
			borderPane.setCenter(centerFlowPane());
			borderPane.setBottom(bottomFlowPane());
			Scene scene = new Scene(borderPane, 600, 400);
			//Image customIcon = new Image("com/medas/mi/graphics/Setting.png");
			Image customIcon = new Image("com/medas/mi/graphics/MedasLog.png");
			
			mainStage.getIcons().add(customIcon);
			mainStage.setScene(scene);
				
			mainStage.setTitle("Laboratory Interface System Ver 1.0.0.0");
			//mainStage.initStyle(StageStyle.UTILITY);
			mainStage.show();
			
			
			Map<Integer, Map<String, String>> permanentSettings = XmlUtil.readXmlFile("settings/permanentSetting.xml");

			permanentSettings.forEach((k, v) -> {
				System.out.println("key: " + k + " Value :" + v);
				if (v != null) {
					serialPortControls.addControlsToFlowPane(flowPaneMainControls, v, portNames);
				}
			});

			mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		            public void handle(WindowEvent we) {
		                System.out.println("Stage is closing");
		                
		                Map<Integer, Map<String, String>> settings = serialPortControls.getSettings();

		                settings.forEach((k, v) -> {
		    				System.out.println("key: " + k + " Value :" + v);
		    				if (v != null) {
		    					serialPortControls.disConnect(v);
		    				}
		    			});
		    			
		            }
		        }); 

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * Creates an HBox with MenuBar,Menu,MunuItems for the top region
	 */
	private HBox addHBox() {
		HBox hbox = new HBox();	
	 loginBtn = new Button();
		loginBtn.setId("Login");
		ImageView imgViewLogin = new ImageView("com/medas/mi/graphics/login_icon.png");
		 setImageDimention(imgViewLogin);
		 loginBtn.setGraphic(imgViewLogin);
                 
		 SerialPortBtn = new Button();
			ImageView imgViewSerialPort = new ImageView("com/medas/mi/graphics/AddSerialPort.png");
			 setImageDimention(imgViewSerialPort);
			 SerialPortBtn.setGraphic(imgViewSerialPort);
		
			 SerialPortBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				
				serialPortControls.addControlsToFlowPane(flowPaneMainControls, null, portNames);
			}
		});
		 saveBtn = new Button();
			 ImageView imgViewSave = new ImageView("com/medas/mi/graphics/save_all.png");
				 setImageDimention(imgViewSave);
				 saveBtn.setGraphic(imgViewSave);
				 
				 saveBtn.setOnAction((event) -> {
			try {
				XmlUtil.writeXmlFile(serialPortControls.getSettings());
				System.out.println("created xml");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				logger.error("addHBox() exception :", e);

			}
		});
		
				 serverBtn = new Button();
				 ImageView imgViewServer = new ImageView("com/medas/mi/graphics/add-server-icon.png");
					 setImageDimention(imgViewServer);
					 serverBtn.setGraphic(imgViewServer);
		
					 serverBtn.setOnAction((event) -> {
			try {
				System.out.println(" TCP Ip Server");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				logger.error("addHBox() exception :", e);

			}
		});
				  clientBtn = new Button();
					 ImageView imgViewClient = new ImageView("com/medas/mi/graphics/ConnectClient.png");
						 setImageDimention(imgViewClient);
						 clientBtn.setGraphic(imgViewClient);
		
						 clientBtn.setOnAction((event) -> {
			try {
				System.out.println(" TCP Ip Client");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				logger.error("addHBox() exception :", e);

			}
		});
		FlowPane flowPaneMenuBar = new FlowPane();
	   loginBtn.setOnAction((event) -> {
			if("Logout".equals(loginBtn.getId())){
				setLogoutVisibility(flowPaneMenuBar);
				loginBtn.setId("Login");
				imgViewLogin.setImage( new Image("com/medas/mi/graphics/login_icon.png"));
				
			} else {
				AlertUtil.loginAlert(flowPaneMenuBar,imgViewLogin, serialPortControls, loginBtn, SerialPortBtn, serverBtn, clientBtn, saveBtn);
				//loginComponent.showLoginScreen(flowPaneMenuBar,imgViewLogin, serialPortControls, loginBtn, SerialPortBtn, serverBtn, clientBtn, saveBtn);
					
			}
		});

		setLoginVisibility();
		flowPaneMenuBar.getChildren().add(0,loginBtn);
		hbox.getChildren().addAll(flowPaneMenuBar);
		hbox.setStyle("-fx-background-color: #87CEFA;");
		return hbox;
	}

	private  FlowPane centerFlowPane() {
		flowPaneMainControls = new FlowPane();
		flowPaneMainControls.setPadding(new Insets(5, 0, 5, 0));
		flowPaneMainControls.setVgap(4);
		flowPaneMainControls.setHgap(4);
		flowPaneMainControls.setPrefWrapLength(270); // preferred width allows for two
											// columns
		flowPaneMainControls.setStyle("-fx-background-color: DAE6F3;");
		return flowPaneMainControls;
	}
	
	private  FlowPane bottomFlowPane() {
		FlowPane bottomFlowPane = new FlowPane();
		bottomFlowPane.setPadding(new Insets(1, 0, 1, 0));
		bottomFlowPane.setVgap(0);
		bottomFlowPane.setHgap(0);
		bottomFlowPane.setStyle("-fx-background-color: #87CEFA;");
		 ImageView imgViewLogo = new ImageView("com/medas/mi/graphics/Medas_emp.png");
			 imgViewLogo.setFitHeight(50);
		 imgViewLogo.setFitWidth(130);
		bottomFlowPane.getChildren().add(imgViewLogo);
	System.out.println("GuiUtil.getCurrentTimeStamp():"+DateUtil.getCurrentDateAndTime());
		
		bottomFlowPane.getChildren().add(new Label(DateUtil.getCurrentDateAndTime()));
		
		return bottomFlowPane;
	}
	
	private void setLoginVisibility() {
		SerialPortBtn.setDisable(true);
		serverBtn.setDisable(true);
		clientBtn.setDisable(true);
		saveBtn.setDisable(true);
		
	}
	
	private void setLogoutVisibility(FlowPane flowPaneMenuBar) {
		flowPaneMenuBar.getChildren().remove(SerialPortBtn);
		flowPaneMenuBar.getChildren().remove(serverBtn);
		flowPaneMenuBar.getChildren().remove(clientBtn);
		flowPaneMenuBar.getChildren().remove(saveBtn);
		Map<Integer, Map<String, Button>> controlsNames = serialPortControls.getButtons();
	    controlsNames.forEach((k, v) -> {
			System.out.println("key: " + k + " Value :" + v);
			if (v != null) {
					 v.get("settingBtn").setDisable(true);
					 v.get("removeBtn").setDisable(true);
					 v.get("connectBtn").setDisable(true); 
			}
		});
	}
	
	private void setImageDimention(ImageView imageView) {
		imageView.setFitHeight(30);
		imageView.setFitWidth(30);
	}
	
	private  List<String> getPortNames() {
		List<String> portNames = new ArrayList<String>();
		String[] machinePortNames = SerialPortList.getPortNames();
		for(int i = 0; i < machinePortNames.length; i++ ) {
			portNames.add(machinePortNames[i]);	
		}
		
		return portNames;
	}
	
	
}
