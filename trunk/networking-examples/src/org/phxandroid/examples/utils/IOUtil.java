package org.phxandroid.examples.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import android.widget.TextView;

public class IOUtil {
    private static final int BUFSIZE = 4096;

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

    /**
     * Copy an input stream to the textview of choice.
     * 
     * @param input
     *            the stream to copy from
     * @param txt
     *            the {@link TextView} to write to
     * @return the number of bytes read.
     * @throws IOException
     */
    public static long copy(InputStream input, TextView txt) throws IOException {
        byte buf[] = new byte[BUFSIZE];
        long count = 0;
        int n = 0;
        while ((n = input.read(buf)) != (-1)) {
            txt.append(new String(buf, 0, n));
            count += n;
        }
        return count;
    }

    /**
     * Copy a reader to a String and return it.
     * 
     * @param reader
     *            the reader to read from
     * @return the string representing the text from the reader.
     * @throws IOException
     */
    public static String readAsString(Reader reader) throws IOException {
        StringBuilder str = new StringBuilder();
        char buf[] = new char[BUFSIZE];
        int n = 0;
        while ((n = reader.read(buf)) != (-1)) {
            str.append(buf, 0, n);
        }
        return str.toString();
    }

}