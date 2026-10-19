package org.example.finance.io;

import org.example.finance.model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Перетворює CSV-рядки на об'єкти Transaction; не знає про URL або файли. */
public final class TransactionCSVParser {
    private TransactionCSVParser() {
    }

    public static List<Transaction> parse(List<String> lines) {
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).replace("﻿", "").trim();
            if (line.isEmpty()) {
                continue;
            }

            // У файлі викладача є заголовок: Дата,Сума,Категорія.
            String lower = line.toLowerCase(Locale.ROOT);
            if (i == 0 && (lower.startsWith("дата,") || lower.startsWith("date,"))) {
                continue;
            }

            // split з лімітом 3: назва категорії може містити кому.
            String[] fields = line.split(",", 3);
            if (fields.length != 3) {
                throw new IllegalArgumentException("Некоректний CSV, рядок " + (i + 1) + ": " + line);
            }
            try {
                double amount = Double.parseDouble(fields[1].trim());
                if (!Double.isFinite(amount)) {
                    throw new NumberFormatException("Сума має бути скінченним числом");
                }
                transactions.add(new Transaction(
                        fields[0].trim(),
                        amount,
                        fields[2].trim()
                ));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Некоректна сума, рядок " + (i + 1) + ": " + line, e);
            }
        }
        return transactions;
    }
}
