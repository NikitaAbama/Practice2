package org.example.finance;

import org.example.finance.analysis.TransactionAnalyzer;
import org.example.finance.io.TransactionCSVReader;
import org.example.finance.model.Transaction;
import org.example.finance.report.TransactionReportGenerator;

import java.io.IOException;
import java.time.DateTimeException;
import java.util.List;

public class Main {
    private static final String SAMPLE_FILE = "src/main/resources/sample.csv";
    // Посилання з презентації: https://informer.com.ua/dut/java/pr2.csv

    public static void main(String[] args) {
        String location = args.length > 0 ? args[0] : SAMPLE_FILE;
        String monthYear = args.length > 1 ? args[1] : "12-2023";
        String from = args.length > 2 ? args[2] : "01-12-2023";
        String to = args.length > 3 ? args[3] : "31-12-2023";

        try {
            List<Transaction> transactions = TransactionCSVReader.readTransactions(location);
            System.out.println("Прочитані транзакції: " + transactions.size());
            transactions.forEach(System.out::println);

            double balance = TransactionAnalyzer.calculateTotalBalance(transactions);
            TransactionReportGenerator.printBalanceReport(balance);

            int count = TransactionAnalyzer.countTransactionsByMonth(transactions, monthYear);
            TransactionReportGenerator.printTransactionsCountByMonth(monthYear, count);

            List<Transaction> top = TransactionAnalyzer.findTopExpenses(transactions);
            TransactionReportGenerator.printTopExpensesReport(top);

            TransactionReportGenerator.printExpenseExtremesReport(from, to,
                    TransactionAnalyzer.findLargestExpenseInPeriod(transactions, from, to),
                    TransactionAnalyzer.findSmallestExpenseInPeriod(transactions, from, to));

            TransactionReportGenerator.printCategoryExpensesReport(
                    TransactionAnalyzer.calculateExpensesByCategory(transactions));
            TransactionReportGenerator.printMonthlyExpensesReport(
                    TransactionAnalyzer.calculateExpensesByMonth(transactions));
        } catch (IOException | IllegalArgumentException | DateTimeException e) {
            System.err.println("Помилка читання/обробки транзакцій: " + e.getMessage());
        }
    }
}
