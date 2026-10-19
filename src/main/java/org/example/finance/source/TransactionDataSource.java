package org.example.finance.source;

import java.io.IOException;
import java.io.Reader;

/** Джерело текстових даних: файл, URL або інше джерело. */
@FunctionalInterface
public interface TransactionDataSource {
    Reader openReader() throws IOException;
}
