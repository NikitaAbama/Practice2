package org.example.finance.io;

import org.example.finance.model.Transaction;
import org.example.finance.source.FileTransactionDataSource;
import org.example.finance.source.TransactionDataSource;
import org.example.finance.source.UrlTransactionDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** За вимогою завдання: абстрактний клас лише зі статичними методами. */
public abstract class TransactionCSVReader {
    private TransactionCSVReader() {
    }

    // Частина 1 самостійної роботи: читання тексту НЕ залежить від джерела.
    public static List<String> readLines(TransactionDataSource source) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(source.openReader())) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    // Частина 2 самостійної роботи: обробку виконує окремий parser.
    public static List<Transaction> readTransactions(TransactionDataSource source) throws IOException {
        return TransactionCSVParser.parse(readLines(source));
    }

    // Перевантажений метод: можна передати одразу URL або шлях до файлу.
    public static List<Transaction> readTransactions(String location) throws IOException {
        TransactionDataSource source = location.startsWith("https://") || location.startsWith("http://")
                ? new UrlTransactionDataSource(location)
                : new FileTransactionDataSource(Path.of(location));
        return readTransactions(source);
    }
}
