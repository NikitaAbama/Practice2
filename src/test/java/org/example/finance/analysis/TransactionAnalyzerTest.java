package org.example.finance.analysis;

import org.example.finance.model.Transaction;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionAnalyzerTest {
    @Test
    void testCalculateTotalBalance() {
        List<Transaction> transactions = List.of(
                new Transaction("01-02-2023", 100, "Дохід"),
                new Transaction("02-02-2023", -50, "Витрата"),
                new Transaction("03-02-2023", 150, "Дохід"));
        assertEquals(200, TransactionAnalyzer.calculateTotalBalance(transactions), 0.0001);
    }

    @Test
    void testCountTransactionsByMonth() {
        List<Transaction> transactions = List.of(
                new Transaction("01-02-2023", 50, "Дохід"),
                new Transaction("15-02-2023", -20, "Витрата"),
                new Transaction("05-03-2023", 100, "Дохід"));
        assertEquals(2, TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2023"));
        assertEquals(1, TransactionAnalyzer.countTransactionsByMonth(transactions, "03-2023"));
        assertEquals(0, TransactionAnalyzer.countTransactionsByMonth(transactions, "04-2023"));
    }

    @Test
    void testFindTopExpenses() {
        List<Transaction> transactions = java.util.stream.IntStream.rangeClosed(1, 12)
                .mapToObj(i -> new Transaction("01-01-2024", -i * 100, "Витрата " + i))
                .collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new));
        transactions.add(new Transaction("01-01-2024", 100000, "Дохід"));
        List<Transaction> top = TransactionAnalyzer.findTopExpenses(transactions);
        assertEquals(10, top.size());
        assertEquals(-1200, top.get(0).getAmount());
        assertEquals(-300, top.get(9).getAmount());
    }

    @Test
    void testLargestAndSmallestExpensesInPeriod() {
        List<Transaction> transactions = List.of(
                new Transaction("01-03-2024", -50, "Кава"),
                new Transaction("04-03-2024", -300, "Їжа"),
                new Transaction("07-03-2024", -1000, "Ноутбук"),
                new Transaction("08-04-2024", -2000, "Поза періодом"));
        assertEquals(-1000, TransactionAnalyzer.findLargestExpenseInPeriod(
                transactions, "01-03-2024", "31-03-2024").orElseThrow().getAmount());
        assertEquals(-50, TransactionAnalyzer.findSmallestExpenseInPeriod(
                transactions, "01-03-2024", "31-03-2024").orElseThrow().getAmount());
    }

    @Test
    void testExpenseReportsData() {
        List<Transaction> transactions = List.of(
                new Transaction("01-01-2024", -200, "Їжа"),
                new Transaction("02-01-2024", -300, "Їжа"),
                new Transaction("03-02-2024", -800, "Транспорт"),
                new Transaction("03-02-2024", 1000, "Зарплата"));
        assertEquals(500, TransactionAnalyzer.calculateExpensesByCategory(transactions).get("Їжа"), 0.0001);
        assertEquals(500, TransactionAnalyzer.calculateExpensesByMonth(transactions)
                .get(YearMonth.of(2024, 1)), 0.0001);
        assertEquals(800, TransactionAnalyzer.calculateExpensesByMonth(transactions)
                .get(YearMonth.of(2024, 2)), 0.0001);
    }

    @Test
    void testNoExpensesInPeriod() {
        var transactions = List.of(new Transaction("01-01-2024", 100, "Дохід"));
        assertTrue(TransactionAnalyzer.findLargestExpenseInPeriod(
                transactions, "01-01-2024", "31-01-2024").isEmpty());
    }
}
