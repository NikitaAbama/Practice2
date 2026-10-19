package org.example.finance.io;

import org.example.finance.model.Transaction;
import org.example.finance.source.TransactionDataSource;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCSVReaderTest {
    @Test
    void testReadTransactionsFromCsv() throws Exception {
        String csv = "Дата,Сума,Категорія\n01-02-2024,100,Зарплата\n02-02-2024,-25,Кава\n";
        TransactionDataSource source = () -> new StringReader(csv);

        List<Transaction> result = TransactionCSVReader.readTransactions(source);
        assertEquals(2, result.size());
        assertEquals("01-02-2024", result.get(0).getDate());
        assertEquals(100, result.get(0).getAmount(), 0.0001);
        assertEquals("Кава", result.get(1).getDescription());
    }

    @Test
    void testReadLinesIndependentlyOfParsing() throws Exception {
        TransactionDataSource source = () -> new StringReader("header\nvalue\n");
        assertEquals(List.of("header", "value"), TransactionCSVReader.readLines(source));
    }

    @Test
    void testMalformedCsvFailsWithExplanation() {
        assertThrows(IllegalArgumentException.class,
                () -> TransactionCSVParser.parse(List.of("Дата,Сума,Категорія", "01-02-2024,abc,Кава")));
    }

    @Test
    void testCsvRejectsNonFiniteAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> TransactionCSVParser.parse(List.of("Дата,Сума,Категорія", "01-02-2024,NaN,Кава")));
    }
}
