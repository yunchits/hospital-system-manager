package com.solvd.hospital.apps;

import com.solvd.hospital.menus.DoctorMenu;
import com.solvd.hospital.menus.Menu;

public class DoctorApp {

    public static void main(String[] args) {
        Menu menu = new DoctorMenu();

        menu.display();
    }
}
