package org.phxandroid.examples.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorUtil {
    public static String getStackTraceAsString(Throwable t) {
        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
