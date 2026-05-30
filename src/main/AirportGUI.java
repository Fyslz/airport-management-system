package main; // تأكد إنهم في نفس الباكيج أو استدعيه صح

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class AirportGUI extends Application {

    // -- متغيرات وهمية بس عشان نعرض الشكل --
    private double simTimeline = 0.0;
    private double simCost = 0.0;
    private int simQueueSize = 0;

    private Map<Integer, GateUI> gatesMap = new HashMap<>();

// =========================================================================
    // 1. GATES VISUAL CONTROL OBJECT (بعد التجميل والـ CSS)
    // =========================================================================
    class GateUI extends VBox {
        private ImageView planeView;
        private Label gateLabel;
        private HBox serviceIconsBox;

        public GateUI(int gateId) {
            this.setSpacing(10);
            this.setAlignment(Pos.CENTER);
            this.setPadding(new Insets(10));

            // 1. تجميل البوابة: رسم موقف طيارة بخطوط متقطعة صفراء
            this.setStyle("-fx-background-color: #4A4A4A; " +
                          "-fx-border-color: #FFCC00; " +
                          "-fx-border-width: 2; " +
                          "-fx-border-style: dashed; " +
                          "-fx-border-radius: 10; " +
                          "-fx-background-radius: 10;");
            this.setPrefWidth(160);
            this.setPrefHeight(200);

            // 2. تجميل لوحة رقم البوابة
            gateLabel = new Label("GATE " + gateId);
            gateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            gateLabel.setTextFill(Color.BLACK);
            gateLabel.setStyle("-fx-background-color: white; -fx-padding: 3px 10px; -fx-background-radius: 5px;");

            // 3. تكبير الطيارة وتعديل ميلانها
            planeView = new ImageView();
            planeView.setFitWidth(120); 
            planeView.setFitHeight(120);
            planeView.setPreserveRatio(true);
            planeView.setOpacity(0); 
            
            // لأن طيارة الركاب حقتك لافة يمين، بنلفها 45- درجة عشان تصير سيدة قدر الإمكان
            planeView.setRotate(-35); 

            // 4. صندوق الخدمات
            serviceIconsBox = new HBox(8); // مسافة بين السيارات
            serviceIconsBox.setAlignment(Pos.CENTER);
            serviceIconsBox.setPrefHeight(50);

            this.getChildren().addAll(gateLabel, planeView, serviceIconsBox);
        }

        public void setTestOccupied(String planeImageName, String[] testServices) {
            Image pImg = loadImage("/original-img/" + planeImageName + ".png");
            if (pImg != null) {
                planeView.setImage(pImg);
                planeView.setOpacity(1); 
            }

            serviceIconsBox.getChildren().clear();
            for (String service : testServices) {
                Image sImg = loadImage("/original-img/" + service + ".png");
                if (sImg != null) {
                    ImageView iconView = new ImageView(sImg);
                    
                    // تكبير حجم السيارات
                    iconView.setFitWidth(40);
                    iconView.setFitHeight(40);
                    iconView.setPreserveRatio(true);
                    
                    // خدعة بصرية: نحط خلفية بيضاء خفيفة (Glow) تحت كل سيارة عشان لو لونها غامق زي الإسعاف تبان فوراً
                    StackPane iconBg = new StackPane(iconView);
                    iconBg.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 5; -fx-padding: 3;");
                    
                    serviceIconsBox.getChildren().add(iconBg);
                }
            }
        }
    }

    // =========================================================================
    // 2. MAIN JavaFX START METHOD (تركيب الشاشة)
    // =========================================================================
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        root.setTop(createTopControlBar());
        root.setCenter(createAirportMap());
        root.setRight(createRightStatsPanel());

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Smart Airport Design Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // =========================================================================
    // 3. Helper Methods to Create Layout Components
    // =========================================================================
    private HBox createTopControlBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(15));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 0 0 2 0;");

        Label title = new Label("AIRPORT SIMULATION");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setPadding(new Insets(0, 50, 0, 0)); 

        Button startBtn = new Button("Start Sim");
        Button resetBtn = new Button("Print Final Report");
        resetBtn.setStyle("-fx-base: #f44336;"); 
        Button slowDownBtn = new Button("<< Slow");
        Button speedUpBtn = new Button("Fast >>");
        Label statusLabel = new Label("Status: TEST MODE");

        topBar.getChildren().addAll(title, startBtn, resetBtn, new Label("| Speed:"), slowDownBtn, speedUpBtn, statusLabel);
        return topBar;
    }

    private StackPane createAirportMap() {
        StackPane mapPane = new StackPane();
        mapPane.setPadding(new Insets(20));

        Pane tarmacBg = new Pane();
        tarmacBg.setStyle("-fx-background-color: #333333; -fx-background-radius: 10;");

        GridPane gatesGrid = new GridPane();
        gatesGrid.setHgap(50);
        gatesGrid.setVgap(10);
        gatesGrid.setAlignment(Pos.CENTER);

        for (int i = 0; i < 4; i++) {
            int leftGateId = i + 5; 
            GateUI leftUI = new GateUI(leftGateId);
            gatesMap.put(leftGateId, leftUI);
            gatesGrid.add(leftUI, 0, i); 
            
            int rightGateId = i + 1; 
            GateUI rightUI = new GateUI(rightGateId);
            gatesMap.put(rightGateId, rightUI);
            gatesGrid.add(rightUI, 2, i); 
        }

        Pane yellowLine = new Pane();
        yellowLine.setStyle("-fx-background-color: #FFCC00;");
        yellowLine.setPrefWidth(12);
        gatesGrid.add(yellowLine, 1, 0, 1, 4); 

        // ---------------------------------------------------------
        // محاكاة بصرية للاختبار فقط
        // ---------------------------------------------------------
        gatesMap.get(1).setTestOccupied("PassengerPlane", new String[]{"stairs", "Ambulance"});
        gatesMap.get(6).setTestOccupied("CargoPlane", new String[]{"FuelTruck", "BaggageHandler"});
        // ---------------------------------------------------------

        mapPane.getChildren().addAll(tarmacBg, gatesGrid);
        return mapPane;
    }

    private VBox createRightStatsPanel() {
        VBox statsPanel = new VBox(20);
        statsPanel.setPadding(new Insets(20));
        statsPanel.setPrefWidth(250);
        statsPanel.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 10; -fx-border-color: #bbb;");

        Label titleLabel = new Label("SIMULATION STATS");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.DARKBLUE);

        Label timelineLabel = new Label("Time Line : " + simTimeline + " min");
        timelineLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label costLabel = new Label("Total Cost : $" + simCost);
        costLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        costLabel.setTextFill(Color.DARKGREEN);

        Label queueLabel = new Label("Planes Waiting in Queue : " + simQueueSize);
        queueLabel.setWrapText(true);
        queueLabel.setFont(Font.font("Arial", 16));

        statsPanel.getChildren().addAll(titleLabel, timelineLabel, costLabel, queueLabel);
        return statsPanel;
    }

// =========================================================================
    // 4. Image Loading Helper (Direct File System Access)
    // =========================================================================
    private Image loadImage(String path) {
        try {
            // بناء المسار المباشر: بنضيف كلمة "resources" قبل المسار اللي يجينا
            // عشان يصير كذا: resources/original-img/PassengerPlane.png
            java.io.File imgFile = new java.io.File("resources" + path);
            
            if (!imgFile.exists()) {
                System.out.println("ERROR: Image not found at -> " + imgFile.getAbsolutePath());
                return null;
            }
            
            // تحويل مسار الملف إلى صيغة URI اللي يقبلها الـ JavaFX
            return new Image(imgFile.toURI().toString());
            
        } catch (Exception e) {
            System.out.println("EXCEPTION loading image: " + path + " -> " + e.getMessage());
            return null;
        }
    }
}