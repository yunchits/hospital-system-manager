package com.solvd.hospital.common.input;

public class InputValidator {

    private InputValidator() {}

    public static boolean isValidName(String input) {
        return input.matches("[A-Z][a-zA-Z]*");
    }

    public static boolean isValidInt(int input, int min, int max) {
        return input >= min && input <= max;
    }

    public static boolean isValidDouble(double input, double min, double max) {
        return input >= min && input <= max;
    }
}
