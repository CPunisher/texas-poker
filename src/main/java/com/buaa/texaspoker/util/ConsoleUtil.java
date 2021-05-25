package com.buaa.texaspoker.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleUtil {
    private static Scanner scanner = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger();

    public static String nextLine() {
        return scanner.nextLine();
    }

    public static int nextInt() {
        while (true) {
            try {
                int res = scanner.nextInt();
                return res;
            } catch (InputMismatchException e) {
                logger.info("Invalid integer");
            }
        }
    }
}
