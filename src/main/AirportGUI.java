package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Airport;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class AirportGUI extends Application {

    private TextArea consoleTextArea;
    private GateUI[] gates = new GateUI[4]; 
    private Map<String, Integer> flightGateMap = new HashMap<>(); 

    private Map<String, Integer> serviceCounts = new HashMap<>();
    private Map<String, Label> serviceLabels = new HashMap<>();
    
    private int flightsServedCount = 0;
    private Label servedLabel;
    private Label costLabel;
    private Label waitTimeLabel;

    private final String[] ALL_SERVICES = {
        "Ambulance", "FuelTruck", "CleaningCrew", "BaggageHandler", 
        "CateringTruck", "Stairs", "PassengerBus", "PushbackTug", 
        "FireTruck", "MaintenanceService", "CustomsService", "SpecialAssistance"
    };

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #121212;"); 

        Label title = new Label("LIVE AIRPORT CONTROL DASHBOARD");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #00d2ff;");
        title.setPadding(new Insets(15));
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        VBox leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(15));
        leftPanel.setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #333; -fx-border-width: 0 2 0 0;");
        leftPanel.setPrefWidth(260);

        Label statsTitle = new Label("📊 Live Statistics");
        statsTitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        
        servedLabel = createStatLabel("Flights Served: 0");
        costLabel = createStatLabel("Total Cost: Calculating...");
        waitTimeLabel = createStatLabel("Wait Time: Calculating...");
        
        VBox statsBox = new VBox(8, statsTitle, servedLabel, costLabel, waitTimeLabel);
        statsBox.setPadding(new Insets(0, 0, 15, 0));
        statsBox.setStyle("-fx-border-color: #444; -fx-border-width: 0 0 1 0;");

        Label servicesTitle = new Label("🚚 Service Fleet Status");
        servicesTitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        VBox fleetBox = new VBox(8);
        fleetBox.getChildren().add(servicesTitle);

        for (String s : ALL_SERVICES) {
            serviceCounts.put(s, 2); 
            Label sLabel = new Label(s + ": 2");
            sLabel.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 14px; -fx-font-weight: bold;");
            serviceLabels.put(s, sLabel);
            fleetBox.getChildren().add(sLabel);
        }

        leftPanel.getChildren().addAll(statsBox, fleetBox);
        root.setLeft(leftPanel);

        VBox gatesContainer = new VBox(12);
        gatesContainer.setAlignment(Pos.TOP_CENTER);
        gatesContainer.setPadding(new Insets(15));

        for (int i = 0; i < 4; i++) {
            gates[i] = new GateUI(i);
            gatesContainer.getChildren().add(gates[i]);
        }
        
        ScrollPane scrollGates = new ScrollPane(gatesContainer);
        scrollGates.setFitToWidth(true);
        scrollGates.setStyle("-fx-background: #121212; -fx-border-color: #121212;");
        root.setCenter(scrollGates);

        consoleTextArea = new TextArea();
        consoleTextArea.setEditable(false);
        consoleTextArea.setPrefHeight(160);
        consoleTextArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px; -fx-control-inner-background: #000000; -fx-text-fill: #55ff55;");
        root.setBottom(consoleTextArea);

        Scene scene = new Scene(root, 1250, 900);
        primaryStage.setTitle("Airport Management System - Live Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();

        redirectSystemOutAndParse();

        Thread simulationThread = new Thread(() -> {
            Airport kingAbdulaziz = new Airport(4);
            kingAbdulaziz.run();
        });
        simulationThread.start();
    }

    private Label createStatLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: #ffaa00; -fx-font-size: 14px; -fx-font-weight: bold;");
        return lbl;
    }

    private void updateServiceLabel(String serviceName) {
        if (serviceCounts.containsKey(serviceName)) {
            int count = serviceCounts.get(serviceName);
            Label lbl = serviceLabels.get(serviceName);
            lbl.setText(serviceName + ": " + count);
            if (count > 0) {
                lbl.setStyle("-fx-text-fill: #00ff00; -fx-font-size: 14px; -fx-font-weight: bold;"); 
            } else {
                lbl.setStyle("-fx-text-fill: #ff3333; -fx-font-size: 14px; -fx-font-weight: bold;"); 
            }
        }
    }

    // ==========================================
    // دالة التقاط النصوص وتحليلها (محدثة لدعم الأيقونات المظلمة)
    // ==========================================
    private void redirectSystemOutAndParse() {
        OutputStream out = new OutputStream() {
            StringBuilder lineBuilder = new StringBuilder();

            @Override
            public void write(int b) {
                char c = (char) b;
                lineBuilder.append(c);
                if (c == '\n') {
                    String line = lineBuilder.toString();
                    lineBuilder.setLength(0); 

                    Platform.runLater(() -> {
                        consoleTextArea.appendText(line);
                        processSimulationEvent(line);
                    });
                }
            }
        };
        System.setOut(new PrintStream(out, true));
    }

    private void processSimulationEvent(String line) {
        try {
            // 1. الطيارة دخلت البوابة
            if (line.contains("to the gate:")) {
                String flightId = line.substring(line.indexOf("Flight: ") + 8, line.indexOf(" to the gate:")).trim();
                int gateId = Integer.parseInt(line.substring(line.indexOf("gate: ") + 6).trim());
                flightGateMap.put(flightId, gateId);
                gates[gateId].parkFlight(flightId);
            } 
            // 2. حدث جديد: الخدمة مطلوبة ولكنها مشغولة (ننتظرها) -> أيقونة مظلمة
            else if (line.contains("is BUSY!")) {
                int firstBracket = line.indexOf("[");
                int secondBracket = line.indexOf("]");
                String serviceName = line.substring(firstBracket + 1, secondBracket).trim();

                int flightStart = line.indexOf("Flight [") + 8;
                int flightEnd = line.indexOf("]", flightStart);
                String flightId = line.substring(flightStart, flightEnd).trim();

                if (flightGateMap.containsKey(flightId)) {
                    int gateId = flightGateMap.get(flightId);
                    gates[gateId].setServiceStatus(serviceName, false); // false = dark image
                }
            }
            // 3. الخدمة توفرت وانطلقت للطيارة -> أيقونة ملونة
            else if (line.contains("Found available:")) {
                String servicePart = line.substring(line.indexOf("Found available: ") + 17);
                String serviceName = servicePart.substring(0, servicePart.indexOf("!")).trim();
                String flightId = line.substring(line.indexOf("for flight: ") + 12).trim();

                if (flightGateMap.containsKey(flightId)) {
                    int gateId = flightGateMap.get(flightId);
                    gates[gateId].setServiceStatus(serviceName, true); // true = original image
                }
                
                if (serviceCounts.containsKey(serviceName)) {
                    serviceCounts.put(serviceName, serviceCounts.get(serviceName) - 1);
                    updateServiceLabel(serviceName);
                }
            } 
            // 4. الخدمة انتهت ورجعت للمطار
            else if (line.contains("is now FREE.")) {
                String serviceName = line.substring(line.indexOf("[") + 1, line.indexOf(" ID:")).trim();
                if (serviceCounts.containsKey(serviceName)) {
                    serviceCounts.put(serviceName, serviceCounts.get(serviceName) + 1);
                    updateServiceLabel(serviceName);
                }
            }
            // 5. الطيارة طارت وفضت البوابة
            else if (line.contains("has departed from the airport")) {
                String flightId = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                if (flightGateMap.containsKey(flightId)) {
                    int gateId = flightGateMap.get(flightId);
                    gates[gateId].clearGate();
                    flightGateMap.remove(flightId);
                    
                    flightsServedCount++;
                    servedLabel.setText("Flights Served: " + flightsServedCount);
                }
            }
            else if (line.contains("Total Cost of Services: $")) {
                String cost = line.substring(line.indexOf("$") + 1).trim();
                costLabel.setText("Total Cost: $" + cost);
            }
            else if (line.contains("Estimated total waiting time =")) {
                String wait = line.substring(line.indexOf("=") + 1, line.indexOf("mins")).trim();
                waitTimeLabel.setText("Wait Time: " + wait + " mins");
            }

        } catch (Exception e) { }
    }

    // ==========================================
    // تصميم البوابة (تم تحديثه ليدعم تبديل الصور ديناميكياً)
    // ==========================================
    class GateUI extends HBox {
        private int gateId;
        Label gateTitle;
        ImageView planeImage;
        FlowPane servicesPane; 
        
        // خريطة لتتبع أيقونات الخدمات المرتبطة بهذه البوابة لتحديثها لاحقاً
        private Map<String, ImageView> serviceIconsMap = new HashMap<>();

        public GateUI(int gateId) {
            this.gateId = gateId;
            this.setAlignment(Pos.CENTER_LEFT);
            this.setSpacing(20);
            this.setPadding(new Insets(10, 15, 10, 15));
            this.setStyle("-fx-border-color: #444; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: #1e1e1e;");
            this.setPrefHeight(120);

            VBox infoBox = new VBox();
            infoBox.setAlignment(Pos.CENTER_LEFT);
            infoBox.setPrefWidth(160); 
            
            gateTitle = new Label("Gate " + gateId + " : EMPTY");
            gateTitle.setStyle("-fx-text-fill: #888; -fx-font-weight: bold; -fx-font-size: 15px;");
            infoBox.getChildren().add(gateTitle);

            planeImage = new ImageView();
            planeImage.setFitWidth(90);
            planeImage.setFitHeight(90);
            planeImage.setPreserveRatio(true);

            servicesPane = new FlowPane();
            servicesPane.setHgap(10);
            servicesPane.setVgap(10);
            servicesPane.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(servicesPane, Priority.ALWAYS); 

            this.getChildren().addAll(infoBox, planeImage, servicesPane);
        }

        public void parkFlight(String flightId) {
            gateTitle.setText("Gate " + gateId + " : [" + flightId + "]");
            gateTitle.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold; -fx-font-size: 15px;");
            
            boolean isCargo = flightId.equals("XY222") || flightId.equals("QR444") || flightId.equals("MS777") || flightId.equals("LH100");
            Image img = loadImage("original-img", isCargo ? "CargoPlane" : "PassengerPlane");
            if (img != null) planeImage.setImage(img);
        }

        // دالة جديدة وذكية: تضيف الصورة المظلمة، أو تحدثها للملونة إذا توفرت!
        public void setServiceStatus(String serviceName, boolean isAvailable) {
            String folder = isAvailable ? "original-img" : "dark-img";
            Image img = loadImage(folder, serviceName);

            if (img != null) {
                if (serviceIconsMap.containsKey(serviceName)) {
                    // إذا الأيقونة موجودة مسبقاً (مظلمة)، بدلها بالملونة في نفس المكان!
                    serviceIconsMap.get(serviceName).setImage(img);
                } else {
                    // إذا مو موجودة، أنشئ أيقونة جديدة
                    ImageView serviceIcon = new ImageView(img);
                    serviceIcon.setFitWidth(45);
                    serviceIcon.setFitHeight(45);
                    serviceIcon.setPreserveRatio(true);
                    
                    serviceIconsMap.put(serviceName, serviceIcon);
                    servicesPane.getChildren().add(serviceIcon);
                }
            }
        }

        public void clearGate() {
            gateTitle.setText("Gate " + gateId + " : EMPTY");
            gateTitle.setStyle("-fx-text-fill: #888; -fx-font-weight: bold; -fx-font-size: 15px;");
            planeImage.setImage(null);
            servicesPane.getChildren().clear(); 
            serviceIconsMap.clear(); // تفريغ الذاكرة للطيارة الجاية
        }
    }

    // ==========================================
    // دالة جلب الصور معدلة لدعم المجلدات الفرعية بشكل آمن
    // ==========================================
    private Image loadImage(String folderName, String imageName) {
        try {
            // المحاولة الأولى: من داخل المجلد الفرعي (dark-img أو original-img)
            File file = new File("src/resources/" + folderName + "/" + imageName + ".png");
            if (file.exists()) return new Image(file.toURI().toString());
            
            // في حال تم تشغيل المشروع من بيئة مختلفة
            File file2 = new File("resources/" + folderName + "/" + imageName + ".png");
            if (file2.exists()) return new Image(file2.toURI().toString());

            // المحاولة كـ Resource من الـ Classpath
            var stream = getClass().getResourceAsStream("/resources/" + folderName + "/" + imageName + ".png");
            if (stream != null) return new Image(stream);

            // ⚠️ حركة أمان إضافية: لو نسيت تحط صورة الطيارة داخل المجلد الفرعي وخليتها برا
            File fallback = new File("src/resources/" + imageName + ".png");
            if (fallback.exists()) return new Image(fallback.toURI().toString());

        } catch (Exception e) {}
        return null;
    }
}