package org.phxandroid.net.servers;

import java.io.Closeable;
import java.io.IOException;

public class IOUtil {
    /**
     * Close a potentially active {@link Closeable} (Usually Streams)
     * 
     * @param closeable
     *            the closeable to close.
     */
    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException ignore) {
            /* ignore */
        }
    }

}
