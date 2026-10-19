package org.example.finance.report;

import org.example.finance.model.Transaction;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Клас лише виводить дані, обчислень транзакцій тут немає. */
public abstract class TransactionReportGenerator {
    // 1 символ * = до 1000 грн витрат (кількість округлюємо вгору).
    private static final double MONEY_PER_STAR = 1000.0;

    private TransactionReportGenerator() {
    }

    public static void printBalanceReport(double totalBalance) {
        System.out.printf("Загальний баланс: %.2f грн%n", totalBalance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.printf("Кількість транзакцій за %s: %d%n", monthYear, count);
    }

    public static void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println("\n10 найбільших витрат:");
        for (Transaction expense : topExpenses) {
            System.out.printf("  %s | %s: %.2f грн%n",
                    expense.getDate(), expense.getDescription(), expense.getAmount());
        }
    }

    public static void printExpenseExtremesReport(String from, String to,
            Optional<Transaction> largest, Optional<Transaction> smallest) {
        System.out.printf("\nВитрати за період %s — %s:%n", from, to);
        System.out.println("Найбільша: " + largest.map(Transaction::toString).orElse("немає витрат"));
        System.out.println("Найменша: " + smallest.map(Transaction::toString).orElse("немає витрат"));
    }

    public static void printCategoryExpensesReport(Map<String, Double> expenses) {
        System.out.println("\nВитрати за категоріями (* = до 1000 грн):");
        expenses.forEach((category, total) -> printBar(category, total));
    }

    public static void printMonthlyExpensesReport(Map<YearMonth, Double> expenses) {
        System.out.println("\nВитрати за місяцями (* = до 1000 грн):");
        expenses.forEach((month, total) -> printBar(month.toString(), total));
    }

    private static void printBar(String label, double amount) {
        int stars = (int) Math.ceil(amount / MONEY_PER_STAR);
        System.out.printf("  %-23s %s (%.2f грн)%n", label, "*".repeat(stars), amount);
    }
}
