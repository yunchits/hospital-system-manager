package com.solvd.hospital.apps;

import com.solvd.hospital.menus.HospitalAdminMenu;
import com.solvd.hospital.menus.Menu;

public class HospitalAdminApp {

    public static void main(String[] args) {
        Menu menu = new HospitalAdminMenu();

        menu.display();
    }
}
