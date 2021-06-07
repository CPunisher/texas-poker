package com.buaa.texaspoker.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 调用控制台输入的工具类
 * @deprecated 后期已经全部使用Gui进行输入输出了，所以不需要控制台了
 * @author CPunisher
 * @see java.util.Scanner
 */
@Deprecated
public class ConsoleUtil {
    private static Scanner scanner = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger();

    /**
     * 请求用户在控制台中输入一行字符串
     * @return 用户输入的字符串
     */
    public static String nextLine() {
        return scanner.nextLine();
    }

    /**
     * 请求用户在控制台中输入一个整数
     * @return 用户输入的整数
     * @see Integer
     */
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
