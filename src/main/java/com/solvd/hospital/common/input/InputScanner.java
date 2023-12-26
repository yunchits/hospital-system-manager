package com.solvd.hospital.common.input;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputScanner implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger(InputScanner.class);

    private final Scanner scanner;

    public InputScanner() {
        this.scanner = new Scanner(System.in);
    }

    public String scanString() {
        return scanner.nextLine();
    }

    public String scanName() {
        String input;
        do {
            input = scanner.nextLine();
            if (!InputValidator.isValidName(input)) {
                LOGGER.info("Invalid name. Please enter a valid name (starts with a capital letter, " +
                    "no special characters or digits): ");
            }
        } while (!InputValidator.isValidName(input));
        return input;
    }

    public int scanInt(int min, int max) {
        int input;
        do {
            while (!scanner.hasNextInt()) {
                LOGGER.info("Please enter a valid integer: ");
                scanner.next();
            }

            input = scanner.nextInt();

            if (!InputValidator.isValidInt(input, min, max)) {
                LOGGER.info("Please enter an integer in the range from " + min + " to " + max);
            }
        } while (!InputValidator.isValidInt(input, min, max));
        scanner.nextLine();
        return input;
    }

    public int scanPositiveInt() {
        int input;
        do {
            while (!scanner.hasNextInt()) {
                LOGGER.info("Please enter a valid integer: ");
                scanner.next();
            }

            input = scanner.nextInt();

            if (input < 0) {
                LOGGER.info("Please enter a positive number.");
            }
        } while (input < 0);
        scanner.nextLine();
        return input;
    }

    public double scanDouble(double min, double max) {
        double input;
        do {
            while (!scanner.hasNextDouble()) {
                LOGGER.info("Please enter a valid number: to enter a decimal number, use the comma symbol - \",\"");
                scanner.next();
            }

            input = scanner.nextDouble();

            if (!InputValidator.isValidDouble(input, min, max)) {
                LOGGER.info("Please enter a number in the range from " + min + " to " + max);
            }
        } while (!InputValidator.isValidDouble(input, min, max));
        scanner.nextLine();
        return input;
    }

    public double scanPositiveDouble() {
        double input;
        do {
            while (!scanner.hasNextDouble()) {
                LOGGER.info("Please enter a valid number: to enter a decimal number, use the comma symbol - \",\"");
                scanner.next();
            }

            input = scanner.nextDouble();

            if (input < 0) {
                LOGGER.info("Please enter a positive number.");
            }
        } while (input < 0);
        scanner.nextLine();
        return input;
    }

    public boolean scanBoolean() {
        String input;
        do {
            input = scanner.nextLine().toLowerCase();
            if (input.equals("true")) {
                return true;
            } else if (input.equals("false")) {
                return false;
            } else {
                LOGGER.info("Invalid input. Please enter 'true' or 'false':");
            }
        } while (true);
    }

    public LocalDate scanLocalDate() {
        String input;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        do {
            input = scanner.nextLine();
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                LOGGER.info("Invalid date format. Please enter a valid date (dd.MM.yyyy):");
            }
        } while (true);
    }

    public LocalDateTime scanLocalDateTime() {
        String input;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        do {
            input = scanner.nextLine();
            try {
                return LocalDateTime.parse(input, formatter);
            } catch (DateTimeParseException e) {
                LOGGER.info("Invalid date and time format. " +
                    "Please enter a valid date and time (dd.MM.yyyy HH:mm):");
            }
        } while (true);
    }

    @Override
    public void close() {
        scanner.close();
    }
}
