package org.example.finance.analysis;

import org.example.finance.model.Transaction;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Логіка аналізу без створення екземпляра TransactionAnalyzer. */
public abstract class TransactionAnalyzer {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MM-uuuu");

    private TransactionAnalyzer() {
    }

    public static double calculateTotalBalance(List<Transaction> transactions) {
        double balance = 0;
        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }
        return balance;
    }

    public static int countTransactionsByMonth(List<Transaction> transactions, String monthYear) {
        int count = 0;
        for (Transaction transaction : transactions) {
            LocalDate date = parseDate(transaction.getDate());
            if (date.format(MONTH_FORMAT).equals(monthYear)) {
                count++;
            }
        }
        return count;
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparingDouble(Transaction::getAmount))
                .limit(10)
                .toList();
    }

    // Найбільша витрата = найбільший модуль від'ємної суми.
    public static Optional<Transaction> findLargestExpenseInPeriod(
            List<Transaction> transactions, String from, String to) {
        return expensesInPeriod(transactions, from, to).stream()
                .min(Comparator.comparingDouble(Transaction::getAmount));
    }

    // Найменша витрата = найменший модуль від'ємної суми.
    public static Optional<Transaction> findSmallestExpenseInPeriod(
            List<Transaction> transactions, String from, String to) {
        return expensesInPeriod(transactions, from, to).stream()
                .max(Comparator.comparingDouble(Transaction::getAmount));
    }

    public static Map<String, Double> calculateExpensesByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(Transaction::getDescription,
                        TreeMap::new,
                        Collectors.summingDouble(t -> -t.getAmount())));
    }

    public static Map<YearMonth, Double> calculateExpensesByMonth(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(parseDate(t.getDate())),
                        TreeMap::new,
                        Collectors.summingDouble(t -> -t.getAmount())));
    }

    private static List<Transaction> expensesInPeriod(List<Transaction> transactions, String from, String to) {
        LocalDate start = parseDate(from);
        LocalDate end = parseDate(to);
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Початок періоду пізніше за кінець");
        }
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> {
                    LocalDate date = parseDate(t.getDate());
                    return !date.isBefore(start) && !date.isAfter(end);
                })
                .toList();
    }

    private static LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_FORMAT);
    }
}
