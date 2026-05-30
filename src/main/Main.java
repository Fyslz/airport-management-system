package main; // تأكد من اسم الباكيج حقك

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        // هذا السطر يروح يفتح كلاس الواجهة ويشغل دالة start() اللي فيه
        Application.launch(AirportGUI.class, args);
    }
}