package com.medas.mi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.medas.mi.listener.Cobas6000;
import com.medas.mi.listener.CobasB221;
import com.medas.mi.listener.CobasC111;
import com.medas.mi.listener.CobasC311;
import com.medas.mi.listener.CobasE411;
import com.medas.mi.listener.CobasP612;
import com.medas.mi.listener.Urisys1100;
//import com.medas.mi.listener.CobasC111;
//import com.medas.mi.listener.CobasC311;
//import com.medas.mi.listener.CobasE411;
//import com.medas.mi.listener.CobasP612;
//import com.medas.mi.listener.Minividas;
//import com.medas.mi.listener.Urisys1100;
import com.medas.mi.utils.AlertUtil;
import com.medas.mi.utils.XmlUtil;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

//import gnu.io.NoSuchPortException;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

/**
 * It is used to create Serial port Controls. It Contains Menu, MunuItem etc.
 *
 * @author Munquasim Alam
 */
public class SerialPortControls {

    private static final Logger logger = Logger.getLogger(SerialPortControls.class);
    private static int controlsCount = -1;
    //private  TextArea textArea = null;
    private boolean isPermanentSetting = false;
    List<Integer> controlsIndexList = new ArrayList<Integer>();

    public Map<String, String> portSettingMap = null;
    private PortSettingComponent portSettingComponent = new PortSettingComponent();
    public Map<Integer, Map<String, String>> settings = new HashMap<Integer, Map<String, String>>();
    public Map<String, String> portNames = new HashMap<String, String>();

    boolean isConnected = false;
    private Map<Integer, Map<String, Button>> controlsNames = new HashMap<Integer, Map<String, Button>>();
    Cobas6000 cobas6000 = null;
    CobasB221 cobasB221 = null;
    CobasC311 cobasC311 = null;
    private CobasC111 cobasC111 = null;
    private CobasE411 cobasE411 = null;
    private Urisys1100 urisys1100 = null;
    private CobasP612 cobasP612 = null;

    /**
     * It is used to add Serial Port Controls in Flow pane.
     *
     * @param flow is the Object of Flow Pane
     * @return it return object of FlowPane.
     */
    public FlowPane addControlsToFlowPane(FlowPane flowPaneMainControls, Map<String, String> permanentSettings, List<String> portNames) {
        try {
            if (permanentSettings != null) {
                isPermanentSetting = true;
                // portNames.put(permanentSettings.get("comPort"), permanentSettings.get("comPort"));
            } else {
                isPermanentSetting = false;
            }
            FlowPane flowPaneSerialControls = new FlowPane();
            flowPaneSerialControls.setPadding(new Insets(10, 10, 10, 10));
            flowPaneSerialControls.setStyle("-fx-background-color: #87CEFA;");
            flowPaneSerialControls.setHgap(5);
            ImageView imgViewStatus = new ImageView("com/medas/mi/graphics/Red_X_close.gif");
            setImageDimention(imgViewStatus);
            Label instrumentName = new Label("");
            Button settingBtn = new Button();
            ImageView imgViewSetting = new ImageView("com/medas/mi/graphics/Setting.png");
            setImageDimention(imgViewSetting);
            settingBtn.setGraphic(imgViewSetting);

            Button connectBtn = new Button();
            connectBtn.setId("C");
            ImageView imgViewConnect = new ImageView("com/medas/mi/graphics/Connect.png");
            setImageDimention(imgViewConnect);
            connectBtn.setGraphic(imgViewConnect);

            Button removeBtn = new Button();// it should be local variable
            ImageView imgViewRemove = new ImageView("com/medas/mi/graphics/Delete.png");
            setImageDimention(imgViewRemove);
            removeBtn.setGraphic(imgViewRemove);

            flowPaneSerialControls.getChildren().addAll(imgViewStatus, instrumentName, settingBtn, connectBtn, removeBtn);

            if ("C".equalsIgnoreCase(connectBtn.getId())) {
                connectBtn.setDisable(true);
            }

            controlsCount++;
            controlsIndexList.add(controlsCount);
            System.out.println("*************controlsIndexList add panel:" + controlsIndexList);
            removeBtn.setId(String.valueOf(controlsCount));
            settings.put(controlsCount, null);
            System.out.println("machineNames add controls:" + settings);
            addControlsButtons(settingBtn, removeBtn, connectBtn);
            System.out.println("controlsNames:" + controlsNames);
            //textArea = new TextArea(String.valueOf(controlsCount));
            System.out.println("count set in textArea:" + controlsCount);

            AnchorPane anchorpane = new AnchorPane();
            TextArea textArea = new TextArea();

            textArea.setMaxWidth(270);// height and width of AnchorPane and flowPane
            // are only decided by TextArea and
            // BorderPane.
            textArea.setMaxHeight(100);

            textArea.appendText("");

            anchorpane.setStyle("-fx-background-color: #A9A9A9;");

            anchorpane.getChildren().addAll(textArea);

            BorderPane pane = new BorderPane();
            pane.setMaxWidth(270);
            pane.setMaxHeight(100);
            pane.setTop(flowPaneSerialControls);
            pane.setCenter(anchorpane);

            flowPaneMainControls.getChildren().add(pane);
            if (isPermanentSetting) {
                connectOnStartUp(settingBtn, connectBtn, removeBtn, instrumentName, imgViewStatus, permanentSettings, textArea);
            }

            settingBtn.setOnAction((event) -> {

                portSettingComponent.createPortSettingForm(isPermanentSetting, removeBtn, settings, portNames);
                connectBtn.setDisable(false);

            });

            connectBtn.setOnAction((event) -> {

                connectOnClick(settingBtn, connectBtn, removeBtn, instrumentName, imgViewStatus, textArea);

            });

            removeBtn.setOnAction((event) -> {
                System.out.println(" removeBtn Id..  :" + removeBtn.getId());
                removeFlowPaneControls(flowPaneMainControls, controlsIndexList, removeBtn, instrumentName);
            });

        } catch (Exception ex) {
            AlertUtil.exceptionAlert(ex);
            logger.error("addControlsToFlowPane Exception", ex);
            System.out.println("addControlsToFlowPane Exception" + ex.getMessage());
        }
        return flowPaneMainControls;

    }

    private FlowPane removeFlowPaneControls(FlowPane flowPaneMainControls, List<Integer> controlsIndexList,
            Button removeBtn, Label instrumentName) {
        try {

            int controlsIndex = controlsIndexList.indexOf(Integer.parseInt(removeBtn.getId()));
            System.out.println("controlsIndex:" + controlsIndex);

            switch (controlsIndex) {
                case 0:
                    flowPaneMainControls.getChildren().remove(0);
                    break;
                case 1:
                    flowPaneMainControls.getChildren().remove(1);
                    break;
                case 2:
                    flowPaneMainControls.getChildren().remove(controlsIndex);
                    break;
                case 3:
                    flowPaneMainControls.getChildren().remove(controlsIndex);
                    break;
                case 4:
                    flowPaneMainControls.getChildren().remove(controlsIndex);
                    break;
                case 5:
                    flowPaneMainControls.getChildren().remove(controlsIndex);
                    break;
                case 6:
                    flowPaneMainControls.getChildren().remove(controlsIndex);
                    break;
                case 7:
                    flowPaneMainControls.getChildren().remove(controlsIndex);
                    break;
                case 8:
                    flowPaneMainControls.getChildren().remove(controlsIndex);
                    break;
                case 9:
                    flowPaneMainControls.getChildren().remove(controlsIndex);
                    break;
                default:
                    flowPaneMainControls.getChildren().remove(controlsIndex);
                    break;

            }

            controlsIndexList.remove(controlsIndex);
            System.out.println("*************controlsIndexList remove:" + controlsIndexList);
            System.out.println("machineNames before remove:" + settings);
            settings.remove(Integer.parseInt(removeBtn.getId()));
            System.out.println("machineNames after remove:" + settings);

            System.out.println("controlsNames before remove:" + controlsNames);
            controlsNames.remove(Integer.parseInt(removeBtn.getId()));
            System.out.println("controlsNames after remove:" + controlsNames);

        } catch (Exception ex) {
            AlertUtil.exceptionAlert(ex);
            logger.error("removeFlowPaneControls Exception", ex);
            System.out.println("removeFlowPaneControls Exception" + ex.getMessage());
        }

        return flowPaneMainControls;
    }

    public static void showReceivedData(TextArea textArea, String strReceived) {
        textArea.appendText(strReceived + "\n");
    }

    public synchronized void connectOnStartUp(Button settingBtn, Button connectBtn, Button removeBtn, Label instrumentName,
            ImageView imageView, Map<String, String> permanentSettings, TextArea textArea) {
        try {
            Map<String, String> portSettingMap = permanentSettings;
            if ("C".equalsIgnoreCase(connectBtn.getId()) && settings != null) {
                instrumentName.setText(portSettingMap.get("machineName"));
                System.out.println("machineName connectBtn:" + portSettingMap.get("machineName"));

                System.out.println(" ************c portSettingMap:" + portSettingMap);

                Map<String, String> setting = new HashMap<String, String>();
                portSettingMap.forEach((k, v) -> {
                    setting.put(k, v);
                });

                settings.put(Integer.parseInt(removeBtn.getId()), setting);
                router(portSettingMap, imageView, textArea);
                setVisibilityOnStartUp(settingBtn, connectBtn, removeBtn, isConnected);

            }
        } catch (Exception ex) {
            AlertUtil.exceptionAlert(ex);
            logger.error("connectOnStartUp Exception", ex);
            System.out.println("connectOnStartUp Exception" + ex.getMessage());

        }

    }

    public synchronized void connectOnClick(Button settingBtn, Button connectBtn, Button removeBtn, Label instrumentName,
            ImageView imageView, TextArea textArea) {
        try {
            portSettingMap = settings.get(Integer.parseInt(removeBtn.getId()));
            if (portSettingMap.size() == 0) {
                String instName = instrumentName.getText();
                System.out.println("insName:" + instName);
            }

            System.out.println("portSettingMap:" + portSettingMap);
            if ("C".equalsIgnoreCase(connectBtn.getId())) {
                System.out.println("machineNames connect:" + settings);
                instrumentName.setText(portSettingMap.get("machineName"));
                System.out.println("instrumentName.getText()connectBtn:" + instrumentName.getText());
                System.out.println("machineName connectBtn:" + portSettingMap.get("machineName"));

                System.out.println(" ************c portSettingMap:" + portSettingMap);
                router(portSettingMap, imageView, textArea);

                setVisibilityOnClick(settingBtn, connectBtn, removeBtn, isConnected);

            } else if ("D".equalsIgnoreCase(connectBtn.getId())) {
                String buttonType = AlertUtil.confirmationAlert(portSettingMap.get("comPort"));
                if ("OK".equals(buttonType)) {
                    disConnect(portSettingMap);
                    imageView.setImage(new Image("com/medas/mi/graphics/Red_X_close.gif"));
                    settingBtn.setDisable(false);
                    removeBtn.setDisable(false);
                    connectBtn.setId("C");
                    ImageView imgViewConnect = new ImageView("com/medas/mi/graphics/Connect.png");
                    setImageDimention(imgViewConnect);
                    connectBtn.setGraphic(imgViewConnect);

                }
            }
        } catch (Exception ex) {
            AlertUtil.exceptionAlert(ex);
            logger.error("connectOnClick Exception", ex);
            System.out.println("connectOnClick Exception" + ex.getMessage());

        }

    }

    private synchronized void router(Map<String, String> portSettingMap, ImageView imageView, TextArea textArea) {

        try {
            switch (portSettingMap.get("machineName")) {
                case "Cobas6000":
                    cobas6000 = new Cobas6000(portSettingMap.get("machineId"),
                            portSettingMap.get("comPort"), portSettingMap.get("bitsPerSecond"),
                            portSettingMap.get("dataBits"), portSettingMap.get("parity"), portSettingMap.get("stopBit"),
                            portSettingMap.get("isPrintLog"), textArea);
                    cobas6000.initialize();
                    System.out.println("isConnected:" + cobas6000.isConnected());
                    isConnected = cobas6000.isConnected();
                    setImages(isConnected, imageView, portSettingMap);
                    break;
                case "CobasB221":
                    cobasB221 = new CobasB221(portSettingMap.get("machineId"),
                            portSettingMap.get("comPort"), portSettingMap.get("bitsPerSecond"),
                            portSettingMap.get("dataBits"), portSettingMap.get("parity"), portSettingMap.get("stopBit"),
                            portSettingMap.get("isPrintLog"), textArea);
                    cobasB221.initialize();
                    isConnected = cobasB221.isConnected();
                    System.out.println("isConnected:" + cobasB221.isConnected());
                    setImages(isConnected, imageView, portSettingMap);
                    break;
                case "CobasC311":
                    cobasC311 = new CobasC311(portSettingMap.get("machineId"),
                            portSettingMap.get("comPort"), portSettingMap.get("bitsPerSecond"),
                            portSettingMap.get("dataBits"), portSettingMap.get("parity"), portSettingMap.get("stopBit"),
                            portSettingMap.get("isPrintLog"), textArea);
                    cobasC311.initialize();
                    isConnected = cobasC311.isConnected();
                    System.out.println("isConnected:" + cobasC311.isConnected());
                    setImages(isConnected, imageView, portSettingMap);

                    break;

                case "CobasC111":
                    cobasC111 = new CobasC111(portSettingMap.get("machineId"),
                            portSettingMap.get("comPort"), portSettingMap.get("bitsPerSecond"),
                            portSettingMap.get("dataBits"), portSettingMap.get("parity"), portSettingMap.get("stopBit"),
                            portSettingMap.get("isPrintLog"), textArea);
                    cobasC111.initialize();
                    System.out.println("isConnected:" + cobasC111.isConnected());
                    isConnected = cobasC111.isConnected();
                    setImages(isConnected, imageView, portSettingMap);
                    break;
                case "CobasE411":
                    cobasE411 = new CobasE411(portSettingMap.get("machineId"),
                            portSettingMap.get("comPort"), portSettingMap.get("bitsPerSecond"),
                            portSettingMap.get("dataBits"), portSettingMap.get("parity"), portSettingMap.get("stopBit"),
                            portSettingMap.get("isPrintLog"), textArea);
                    cobasE411.initialize();
                    System.out.println("isConnected:" + cobasE411.isConnected());
                    isConnected = cobasE411.isConnected();
                    setImages(isConnected, imageView, portSettingMap);
                    break;

                case "Urisys1100":
                    urisys1100 = new Urisys1100(portSettingMap.get("machineId"),
                            portSettingMap.get("comPort"), portSettingMap.get("bitsPerSecond"),
                            portSettingMap.get("dataBits"), portSettingMap.get("parity"), portSettingMap.get("stopBit"),
                            portSettingMap.get("isPrintLog"), textArea);
                    urisys1100.initialize();
                    System.out.println("isConnected:" + urisys1100.isConnected());
                    isConnected = urisys1100.isConnected();
                    setImages(isConnected, imageView, portSettingMap);
                    break;
                case "CobasP612":
                    cobasP612 = new CobasP612(portSettingMap.get("machineId"),
                            portSettingMap.get("comPort"), portSettingMap.get("bitsPerSecond"),
                            portSettingMap.get("dataBits"), portSettingMap.get("parity"), portSettingMap.get("stopBit"),
                            portSettingMap.get("isPrintLog"), textArea);
                    cobasP612.initialize();
                    System.out.println("isConnected:" + cobasP612.isConnected());
                    isConnected = cobasP612.isConnected();
                    setImages(isConnected, imageView, portSettingMap);

                    break;

//		case "Minividas":
//			final Minividas minividas = new Minividas(portSettingMap.get("machineId"),
//					portSettingMap.get("comPort"), portSettingMap.get("bitsPerSecond"),
//					portSettingMap.get("dataBits"), portSettingMap.get("parity"), portSettingMap.get("stopBit"),
//					portSettingMap.get("isPrintLog"), textArea);
//			minividas.initialize();
//			System.out.println("isConnected:" + minividas.isConnected());
//			if (minividas.isConnected()) {
//				imageView.setImage(new Image("com/medas/mi/graphics/tick.png"));
//			} else {
//				imageView.setImage(new Image("com/medas/mi/graphics/cross.png"));
//			}
//			System.out.println("****com port" + portSettingMap.get("comPort"));
//			System.out.println("****minividas");
                // case "clinitek":
                // SerialPortListenerClinitek serialPortListenerClinitek = new
                // SerialPortListenerClinitek(portSettingMap);
                // System.out.println("isConnected:"+serialPortListenerClinitek.isConnected());
                // if(serialPortListenerClinitek.isConnected()) {
                // AlertUtil.connectionSuccessAlert(portSettingMap.get("comPort").toString());
                // }
                // break;
                default:
                    System.out.println("No Correct Machine Name Selected.");
                    logger.error("No Correct Machine Name Selected.");

            }
        } catch (Exception ex) {
            AlertUtil.exceptionAlert(ex);
            logger.error("router Exception", ex);
            System.out.println("router Exception" + ex.getMessage());

        }
    }

    private void setImages(boolean isConnected, ImageView imageView, Map<String, String> portSettingMap) {
        if (isConnected) {
            imageView.setImage(new Image("com/medas/mi/graphics/Tick.gif"));
            if (!isPermanentSetting) {
                AlertUtil.connectionSuccessAlert(portSettingMap.get("comPort").toString());
            }

        } else {
            imageView.setImage(new Image("com/medas/mi/graphics/Red_X_close.gif"));
        }
    }

    private void setImageDimention(ImageView imageView) {
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
    }

    public Map<Integer, Map<String, String>> getSettings() {
        return settings;
    }

    private void setVisibilityOnClick(Button settingBtn, Button connectBtn, Button removeBtn, boolean isConnected) {

        if (isConnected) {
            connectBtn.setId("D");
            ImageView imgViewConnect = new ImageView("com/medas/mi/graphics/Disconnect.png");
            setImageDimention(imgViewConnect);
            connectBtn.setGraphic(imgViewConnect);
            connectBtn.setDisable(false);
            settingBtn.setDisable(isConnected);
            removeBtn.setDisable(isConnected);

        }

        addControlsButtons(settingBtn, removeBtn, connectBtn);
    }

    private void setVisibilityOnStartUp(Button settingBtn, Button connectBtn, Button removeBtn, boolean isConnected) {
        settingBtn.setDisable(true);
        removeBtn.setDisable(true);
        connectBtn.setDisable(true);
        if (isConnected) {
            connectBtn.setId("D");
            ImageView imgViewConnect = new ImageView("com/medas/mi/graphics/Disconnect.png");
            setImageDimention(imgViewConnect);
            connectBtn.setGraphic(imgViewConnect);

        }
        addControlsButtons(settingBtn, removeBtn, connectBtn);

    }

    public void setVisibilityOnLogin() {
        controlsNames.forEach((k, v) -> {
            System.out.println("key: " + k + " Value :" + v);
            if (v != null) {
                if (v.get("connectBtn") != null && "D".equalsIgnoreCase(v.get("connectBtn").getId())) {
                    v.get("settingBtn").setDisable(true);
                    v.get("removeBtn").setDisable(true);
                    v.get("connectBtn").setDisable(false);
                } else if (v.get("connectBtn") != null && "C".equalsIgnoreCase(v.get("connectBtn").getId())) {
                    v.get("settingBtn").setDisable(false);
                    v.get("removeBtn").setDisable(false);
                    v.get("connectBtn").setDisable(true);
                }
            }
        });

    }

    public Map<Integer, Map<String, Button>> getButtons() {
        return controlsNames;
    }

    private void addControlsButtons(Button settingBtn, Button removeBtn, Button connectBtn) {
        Map<String, Button> controlsbuttons = new HashMap<String, Button>();
        controlsbuttons.put("settingBtn", settingBtn);
        controlsbuttons.put("removeBtn", removeBtn);
        controlsbuttons.put("connectBtn", connectBtn);
        controlsNames.put(Integer.parseInt(removeBtn.getId()), controlsbuttons);
    }

    public void disConnect(Map<String, String> portSettingMap) {
        try {
            if (portSettingMap != null) {
                switch (portSettingMap.get("machineName")) {
                    case "Cobas6000":
                        if (cobas6000 != null) {
                            cobas6000.closeSerialPort();
                            cobas6000 = null;
                        }
                        break;
                    case "CobasB221":
                        if (cobasB221 != null) {
                            cobasB221.closeSerialPort();
                            cobasB221 = null;
                        }
                        break;
                    case "CobasC311":
                        if (cobasC311 != null) {
                            cobasC311.closeSerialPort();
                            cobasC311 = null;
                        }
                        break;
                    case "CobasC111":
                        if (cobasC111 != null) {
                            cobasC111.closeSerialPort();
                            cobasC111 = null;
                        }
                        break;
                    case "CobasP612":
                        if (cobasP612 != null) {
                            cobasP612.closeSerialPort();
                            cobasP612 = null;
                        }
                        break;
                    case "CobasE411":
                        if (cobasE411 != null) {
                            cobasE411.closeSerialPort();
                            cobasE411 = null;
                        }
                        break;
                    case "Urisys1100":
                        if (urisys1100 != null) {
                            urisys1100.closeSerialPort();
                            urisys1100 = null;
                        }
                        break;
                    // case "clinitek":
                    // new
                    // SerialPortListenerCobas6000(portSettingMap.get("comPort")).close();
                    // break;

                }
            }

        } catch (Exception ex) {
            AlertUtil.exceptionAlert(ex);
            logger.error("disConnect Exception", ex);
            System.out.println("disConnect Exception" + ex.getMessage());
        }

    }

}
