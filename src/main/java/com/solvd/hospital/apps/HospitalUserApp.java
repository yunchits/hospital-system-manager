package com.solvd.hospital.apps;

import com.solvd.hospital.menus.Menu;
import com.solvd.hospital.menus.usermenus.UserMenu;

public class HospitalUserApp {

    public static void main(String[] args) {
        Menu menu = new UserMenu();
        menu.display();
    }
}
