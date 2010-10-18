package org.phxandroid.net.servers;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
    public static String asString(Throwable t) {
        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
