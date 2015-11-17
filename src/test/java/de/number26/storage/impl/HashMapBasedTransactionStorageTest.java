package de.number26.storage.impl;

import de.number26.storage.Transaction;
import de.number26.storage.TransactionStorage;
import de.number26.storage.exception.StorageException;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

/**
 * @author vasiliy
 *
 * todo multithreaded saving/getting; plus measure multithreaded performance
 */
public class HashMapBasedTransactionStorageTest extends TestCase {
    public void testBasicFunctionality() throws Exception {
        TransactionStorage transactionStorage = new HashMapBasedTransactionStorage();
        Transaction transaction = new TransactionImpl(1, null, "type1", 10);
        transactionStorage.saveTransaction(transaction);
        Set<Transaction> transactionsByType = new HashSet<>();
        transactionsByType.add(transaction);

        assertEquals(transaction, transactionStorage.getTransaction(1));
        assertEquals(transactionsByType, transactionStorage.getTransactions("type1"));
        assertEquals(10.0, transactionStorage.getTransactionSum(1));
    }

    public void testBasicFunctionalityWithSeveralValues() throws Exception {
        TransactionStorage transactionStorage = new HashMapBasedTransactionStorage();
        Transaction transaction1 = new TransactionImpl(1, null, "type1", 10);
        Transaction transaction2 = new TransactionImpl(2, 1L, "type1", 25);
        Transaction transaction3 = new TransactionImpl(3, 2L, "type1", 50);
        Transaction transaction4 = new TransactionImpl(4, null, "type2", 100);
        Transaction transaction5 = new TransactionImpl(5, 4L, "type2", 200);
        transactionStorage.saveTransactions(transaction1, transaction2, transaction3, transaction4, transaction5);

        Set<Transaction> firstTypeTransactions = new HashSet<>();
        firstTypeTransactions.add(transaction1);
        firstTypeTransactions.add(transaction2);
        firstTypeTransactions.add(transaction3);
        Set<Transaction> secondTypeTransactions = new HashSet<>();
        secondTypeTransactions.add(transaction4);
        secondTypeTransactions.add(transaction5);

        assertEquals(firstTypeTransactions, transactionStorage.getTransactions("type1"));
        assertEquals(secondTypeTransactions, transactionStorage.getTransactions("type2"));

        assertEquals(85.0, transactionStorage.getTransactionSum(1));
        assertEquals(75.0, transactionStorage.getTransactionSum(2));
        assertEquals(50.0, transactionStorage.getTransactionSum(3));
        assertEquals(300.0, transactionStorage.getTransactionSum(4));
        assertEquals(200.0, transactionStorage.getTransactionSum(5));
    }

    public void testViaOriginalExample() throws Exception {
        TransactionStorage transactionStorage = new HashMapBasedTransactionStorage();
        TransactionImpl carsTransaction = new TransactionImpl(10, null, "cars", 5000);
        transactionStorage.saveTransaction(carsTransaction);
        TransactionImpl shoppingTransaction = new TransactionImpl(11, 10L, "shopping", 10000);
        transactionStorage.saveTransaction(shoppingTransaction);

        Set<Transaction> transactions = new HashSet<>();
        transactions.add(carsTransaction);
        assertEquals(transactions, transactionStorage.getTransactions("cars"));

        assertEquals(15000.0, transactionStorage.getTransactionSum(10));
        assertEquals(10000.0, transactionStorage.getTransactionSum(11));
    }

    public void testCornerCases() throws Exception {
        TransactionStorage transactionStorage = new HashMapBasedTransactionStorage();
        assertNull(transactionStorage.getTransaction(0));
        assertEquals(0, transactionStorage.getTransactions("fake_type").size());
        assertEquals(Double.NaN, transactionStorage.getTransactionSum(-1));
    }

    public void testIllegalReferences() throws Exception {
        TransactionStorage transactionStorage = new HashMapBasedTransactionStorage();
        Transaction noSuchParentTransaction = new TransactionImpl(1, 2L, "type1", 10);
        Transaction parentToItselfTransaction = new TransactionImpl(3, 3L, "type1", 10);
        try {
            transactionStorage.saveTransaction(noSuchParentTransaction);
            fail();
        } catch (StorageException ignore) {}

        try {
            transactionStorage.saveTransaction(parentToItselfTransaction);
            fail();
        } catch (StorageException ignore) {}

        assertNull(transactionStorage.getTransaction(1));
        assertNull(transactionStorage.getTransaction(3));
    }

    public void testIllegalReferences_RevertingAllParents() throws Exception {
        TransactionStorage transactionStorage = new HashMapBasedTransactionStorage();
        Transaction transaction1 = new TransactionImpl(1, null, "type1", 10);
        Transaction transaction2 = new TransactionImpl(2, 1L, "type1", 25);
        transactionStorage.saveTransactions(transaction1, transaction2);

        assertNull(transactionStorage.getTransaction(3));
    }
}