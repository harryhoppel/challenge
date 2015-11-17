package de.number26.storage.impl;

import com.google.common.collect.Sets;
import com.google.inject.Singleton;
import de.number26.storage.Transaction;
import de.number26.storage.TransactionNode;
import de.number26.storage.TransactionStorage;
import de.number26.storage.exception.StorageException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vasiliy
 */
@Singleton
public class HashMapBasedTransactionStorage implements TransactionStorage {
    private final Map<String, Set<Transaction>> transactionsByType = new ConcurrentHashMap<>();
    private final Map<Long, TransactionNode> transactionNodeById = new ConcurrentHashMap<>();

    @Override
    public @Nullable Transaction getTransaction(long transactionId) {
        TransactionNode transactionNode = transactionNodeById.get(transactionId);
        if (transactionNode != null) {
            return transactionNode.getTransaction();
        }
        return null;
    }

    @Override
    @NotNull
    public Set<Transaction> getTransactions(@NotNull String transactionType) {
        Set<Transaction> transactions = transactionsByType.get(transactionType);
        if (transactions != null) {
            return transactions;
        }
        return Collections.emptySet();
    }

    @Override
    public double getTransactionSum(long transactionId) {
        TransactionNode transactionNode = transactionNodeById.get(transactionId);
        if (transactionNode != null) {
            return transactionNode.getSum();
        }
        return Double.NaN;
    }

    @Override
    public void saveTransaction(Transaction transaction) throws StorageException {
        // no synchronization between two maps – see comment in TransactionStorage
        transactionsByType.putIfAbsent(transaction.getType(), Sets.newConcurrentHashSet());
        transactionsByType.get(transaction.getType()).add(transaction);

        transactionNodeById.put(transaction.getId(), new TransactionNodeImpl(transaction, transaction.getAmount()));

        Long parentId = transaction.getParentId();
        if (parentId != null) {
            if (parentId == transaction.getId()) {
                revertTransaction(transaction);
                throw new StorageException("Parent id is in itself in transaction: " + transaction);
            }
            while (parentId != null) {
                TransactionNode parentTransactionNode = transactionNodeById.get(parentId);
                if (parentTransactionNode == null) {
                    revertTransaction(transaction);
                    throw new StorageException("Illegal parent node in transaction: " + transaction);
                }
                parentTransactionNode.addToSum(transaction.getAmount());
                parentId = parentTransactionNode.getTransaction().getParentId();
            }
        }
    }

    @Override
    public void saveTransactions(Transaction... transactions) throws StorageException {
        for (Transaction transaction : transactions) {
            saveTransaction(transaction);
        }
    }

    private void revertTransaction(Transaction transaction) {
        transactionsByType.get(transaction.getType()).remove(transaction);
        transactionNodeById.remove(transaction.getId());
    }
}
