package com.solvd.hospital.menus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MenuMessages {
    private static final Logger LOGGER = LogManager.getLogger(MenuMessages.class);

    public static final String CREATE = "1 - Create";
    public static final String DISPLAY_ALL = "2 - Display all";
    public static final String UPDATE = "3 - Update";
    public static final String DELETE = "4 - Delete";
    public static final String RETURN_TO_MAIN_MENU = "0 - Return to Main Menu";

    private MenuMessages() {
    }

    public static void printCrudMenuOptions() {
        LOGGER.info(CREATE);
        LOGGER.info(DISPLAY_ALL);
        LOGGER.info(UPDATE);
        LOGGER.info(DELETE);
        LOGGER.info(RETURN_TO_MAIN_MENU);
    }
}
