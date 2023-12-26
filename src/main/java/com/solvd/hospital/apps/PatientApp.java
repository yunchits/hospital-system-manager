package com.solvd.hospital.apps;

import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.PatientMenu;

public class PatientApp {

    public static void main(String[] args) {
        Menu menu = new PatientMenu();

        menu.display();
    }
}
