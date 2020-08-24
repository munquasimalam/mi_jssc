/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medas.mi.utils;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.medas.mi.SerialPortControls;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Munquasim Alam
 */
public class AlertUtil {
    
   public static void loginSuccessAlert(String user) {
       
	   Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Login Successfully");
		alert.setHeaderText(null);
		 String s = user +" , user  Login Successfully";
         alert.setContentText(s);
         alert.initStyle(StageStyle.UTILITY);
     	alert.showAndWait();
        
    } 
   
 public void informationAlert() {
 	Alert alert = new Alert(Alert.AlertType.INFORMATION);
 	alert.setTitle("Information Dialog");
 	alert.setHeaderText("Look, an Information Dialog");
 	alert.setContentText("I have a great message for you!");
 	alert.showAndWait();
     }
 
 public static void connectionSuccessAlert(String Port) {
 	Alert alert = new Alert(Alert.AlertType.INFORMATION);
 	alert.setTitle("Connected Successfully");
 	alert.setHeaderText(null);
 	 String s = "Connected with Port: " +Port;
     alert.setContentText(s);
     alert.initStyle(StageStyle.UNDECORATED);
 	alert.showAndWait();
     }
 
 public void warningAlert() {
 	Alert alert = new Alert(Alert.AlertType.WARNING);
 	alert.setTitle("Warning Dialog");
 	alert.setHeaderText("Look, a Warning Dialog");
 	alert.setContentText("Careful with the next step!");
         alert.initStyle(StageStyle.UTILITY);
 	alert.showAndWait();
     }

 public static void errorAlert(String user) {
 	Alert alert = new Alert(Alert.AlertType.ERROR);
 	alert.setTitle("Error");
 	alert.setHeaderText(null);
 	alert.setContentText(user +" , User Name OR password Wrong!");	
         alert.initStyle(StageStyle.UNDECORATED);
 	alert.showAndWait();
     }
 
 public static void exceptionAlert(Exception ex) {
 	Alert alert = new Alert(Alert.AlertType.ERROR);
 	alert.setTitle("Exception Dialog");
 	alert.setHeaderText("Look, an Exception Dialog");
 	alert.setContentText("Following exception has occurs");
 	alert.setContentText(ex.getMessage());
 	

 // mm 	Exception ex = new FileNotFoundException("Could not find file blabla.txt");

 	// Create expandable Exception.
 	StringWriter sw = new StringWriter();
 	PrintWriter pw = new PrintWriter(sw);
 	ex.printStackTrace(pw);
 	String exceptionText = sw.toString();

 	Label label = new Label("The exception stacktrace was:");

 	TextArea textArea = new TextArea(exceptionText);
 	textArea.setEditable(false);
 	textArea.setWrapText(true);

 	textArea.setMaxWidth(Double.MAX_VALUE);
 	textArea.setMaxHeight(Double.MAX_VALUE);
 	GridPane.setVgrow(textArea, Priority.ALWAYS);
 	GridPane.setHgrow(textArea, Priority.ALWAYS);

 	GridPane expContent = new GridPane();
 	expContent.setMaxWidth(Double.MAX_VALUE);
 	expContent.add(label, 0, 0);
 	expContent.add(textArea, 0, 1);

 	// Set expandable Exception into the dialog pane.
 	alert.getDialogPane().setExpandableContent(expContent);
         alert.initStyle(StageStyle.UNDECORATED);

 	alert.showAndWait();
     }

 public static String confirmationAlert(String portName) {
 	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
 	alert.setTitle("Confirmation");
 	alert.setHeaderText("Look, A Confirmation");
 	alert.setContentText("Do you want to disconnect com Port:" + portName + "?");
        alert.initStyle(StageStyle.UNDECORATED);// to remove java Icon
 	Optional<ButtonType> result = alert.showAndWait();
 	  return result.get().getText();


    }

 public void confirmationAlert2() {
 	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
 	alert.setTitle("Confirmation Dialog with Custom Actions");
 	alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
 	alert.setContentText("Choose your option.");

 	ButtonType buttonTypeOne = new ButtonType("One");
 	ButtonType buttonTypeTwo = new ButtonType("Two");
 	ButtonType buttonTypeThree = new ButtonType("Three");
 	ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

 	alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

 	Optional<ButtonType> result = alert.showAndWait();
 	if (result.get() == buttonTypeOne){
 	    // ... user chose "One"
 	} else if (result.get() == buttonTypeTwo) {
 	    // ... user chose "Two"
 	} else if (result.get() == buttonTypeThree) {
 	    // ... user chose "Three"
 	} else {
 	    // ... user chose CANCEL or closed the dialog
 	}
    }
 
 public static String textInputAlert() {
 	TextInputDialog dialog = new TextInputDialog("");
 	dialog.setTitle(null);
 	dialog.setHeaderText("");
 	dialog.setContentText("Please enter Machine ID:");

 	// Traditional way to get the response value.
         dialog.initStyle(StageStyle.UNDECORATED);
 	Optional<String> result = dialog.showAndWait();
 	String machineId = "";
 	if (result.isPresent()){
     	System.out.println("Your name: " + result.get());
     	machineId = result.get();
         }
	

 	// The Java 8 way to get the response value (with lambda expression).
 	//result.ifPresent(name -> { System.out.println("Your name: " + name);
 	
 		//});
 	return machineId;
     }

 public static  String  choiceAlertMachineName() {
 	List<String> choices = new ArrayList<>();

 	choices.add("Cobas6000");
 	choices.add("CobasB221");
 	choices.add("CobasC311");
 	choices.add("CobasC111");
 	choices.add("CobasE411");
 	choices.add("CobasP612");
 	choices.add("Urisys1100");
 	choices.add("Minividas");
  	ChoiceDialog<String> dialog = new ChoiceDialog<>("", choices);
 	dialog.setTitle(null);
 	dialog.setHeaderText(null);
 	dialog.setContentText("Plz Choose your Machine Name:");
 	String machineName = null;
 	// Traditional way to get the response value.
        dialog.initStyle(StageStyle.UNDECORATED);
 	Optional<String> result = dialog.showAndWait();
 		if (result.isPresent()) {
    		 	System.out.println("Your choice: " + result.get());
    		 	machineName =  result.get();	
 		}

 	// The Java 8 way to get the response value (with lambda expression).
 	//result.ifPresent(letter -> System.out.println("Your choice: " + letter));
 		return machineName;
     }
 
 public static  String  choiceAlertPortName(List<String> portNames) {
	 	//List<String> choices = new ArrayList<>();

	 	//choices.add("COM8");
	 	//choices.add("COM9");
	 
	 	ChoiceDialog<String> dialog = new ChoiceDialog<>("", portNames);
	 	dialog.setTitle(null);
	 	dialog.setHeaderText(null);
	 	dialog.setContentText("Plz Choose your Port Name:");
	 	String PortName = null;
	 	// Traditional way to get the response value.
                dialog.initStyle(StageStyle.UNDECORATED);
	 	Optional<String> result = dialog.showAndWait();
	 		if (result.isPresent()) {
	    		 	System.out.println("Your choice: " + result.get());
	    		 	PortName =  result.get();	
	 		}

	 	// The Java 8 way to get the response value (with lambda expression).
	 	//result.ifPresent(letter -> System.out.println("Your choice: " + letter));
	 		return PortName;
	     }

// modified from original code to use ArrayList rather than Pair
 public static void loginAlert(FlowPane flowPaneMenuBar, ImageView imgViewLogin, SerialPortControls serialPortControls, Button loginBtn, Button SerialPortBtn, Button serverBtn, Button clientBtn, Button saveBtn) {
	 // Create the custom dialog
	Dialog<ArrayList<String>> dialog = new Dialog<>();
	dialog.setTitle("Login");
	//dialog.setHeaderText("Look, a Custom Login Dialog");

	// Set the icon (must be included in the project).
	//dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

	// Set the button types.
	ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
	dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

	// Create the username and password labels and fields.
	GridPane grid = new GridPane();
        
	grid.setHgap(10);
	grid.setVgap(10);
	grid.setPadding(new Insets(20, 150, 10, 10));

	TextField username = new TextField();
	username.setPromptText("Username");
	PasswordField password = new PasswordField();
	password.setPromptText("Password");
	 Label lblMessage = new Label();

	grid.add(new Label("Username:"), 0, 0);
	grid.add(username, 1, 0);
	grid.add(new Label("Password:"), 0, 1);
	grid.add(password, 1, 1);
	grid.add(lblMessage, 1, 2);
	
	// Enable/Disable login button depending on whether a username was entered.
	Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
	loginButton.setDisable(true);

	// Do some validation (using the Java 8 lambda syntax).
	username.textProperty().addListener((observable, oldValue, newValue) -> {
  	loginButton.setDisable(newValue.trim().isEmpty());
	});

        grid.setStyle("-fx-background-color: #f5deb3;");
	dialog.getDialogPane().setContent(grid);

	// Request focus on the username field by default.
	Platform.runLater(() -> username.requestFocus());
	dialog.initStyle(StageStyle.UNDECORATED);
        

	// Convert the result to a username-password-pair when the login button is clicked.
	dialog.setResultConverter(dialogButton -> {
  	if (dialogButton == loginButtonType) {
      	ArrayList<String> list = new ArrayList<String>();
		list.add(username.getText());
		list.add(password.getText());
		return list;
  	}
  	return null;
	});


	Optional<ArrayList<String>> result = dialog.showAndWait();

	result.ifPresent(usernamePassword -> {
  	System.out.println("Username= " + usernamePassword.get(0) + ", Password= " + usernamePassword.get(1));
  	
    if(usernamePassword.get(0).equals("mm") && usernamePassword.get(1).equals("mm")) {
        lblMessage.setText("Congratulations!");
        lblMessage.setTextFill(Color.GREEN);
        AlertUtil.loginSuccessAlert(usernamePassword.get(0));
     // loginStage.close(); // return to main window
      serialPortControls.setVisibilityOnLogin();
    
     SerialPortBtn.setDisable(false);
	    serverBtn.setDisable(false);
     clientBtn.setDisable(false);
		saveBtn.setDisable(false);
		loginBtn.setId("Logout");
		flowPaneMenuBar.getChildren().add(1, SerialPortBtn);
		flowPaneMenuBar.getChildren().add(2, serverBtn);
		flowPaneMenuBar.getChildren().add(3, clientBtn);
		flowPaneMenuBar.getChildren().add(4, saveBtn);
	imgViewLogin.setImage(new Image("com/medas/mi/graphics/icon-logout-big.png"));

    } else {
    	errorAlert(usernamePassword.get(0));
}
  	
	});
}
   
   
   
}
