package com.taylored_it.tools;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Common utilities used by INTERV.
 *
 * @author Hendry Taylor
 * @version 1.0
 */
public class commonUtils {

    //private commonUtils commUtils = new commonUtils();

    /**
     * Determines if system executing on is Windows.
     *
     * @return TRUE if Windows
     */
    public static boolean isWindows() {

        String os = System.getProperty("os.name").toLowerCase();
        //windows
        return (os.contains("win"));

    }

    /**
     * Determines if system executing on is Mac OSX.
     *
     * @return TRUE if Mac
     */
    public static boolean isMac() {

        String os = System.getProperty("os.name").toLowerCase();
        //Mac
        return (os.contains("mac"));

    }

    /**
     * Determines if system executing on is Unix.
     *
     * @return TRUE if Unix
     */
    public static boolean isUnix() {

        String os = System.getProperty("os.name").toLowerCase();
        //linux or unix
        return (os.contains("nix") || os.contains("nux"));

    }

    /**
     * Gets the exception stack trace as a string.
     *
     * @param exception The execption to be returned as a string.
     * @return exception as a String
     */
    public String getStackTraceAsString(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.print(" [ ");
        pw.print(exception.getClass().getName());
        pw.print(" ] ");
        pw.print(exception.getMessage());
        exception.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Checks if process has completed.
     *
     * @param p Process reference.
     * @return True if process has completed and false if not.
     */
    private static boolean procDone(Process p) {
        try {
            int v = p.exitValue();
            return true;
        } catch (IllegalThreadStateException e) {
            return false;
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String substringupto(String str, String seperator) {
        if (isEmpty(str)) {
            return str;
        }
        if (seperator == null) {
            return "";
        }
        int pos = str.indexOf(seperator);
        if (pos == -1) {
            return "";
        }
        return str.substring(0, pos);
    }
}
