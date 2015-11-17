package de.number26.storage.impl;

import de.number26.storage.Transaction;
import de.number26.storage.TransactionNode;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.DoubleAdder;

/**
 * @author vasiliy
 */
public class TransactionNodeImpl implements TransactionNode {
    private final Transaction transaction;
    private final DoubleAdder doubleAdder = new DoubleAdder();

    public TransactionNodeImpl(@NotNull Transaction transaction, double sum) {
        this.transaction = transaction;
        this.doubleAdder.add(sum);
    }

    @Override
    @NotNull
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public double getSum() {
        return doubleAdder.doubleValue();
    }

    @Override
    public void addToSum(double delta) {
        doubleAdder.add(delta);
    }
}
