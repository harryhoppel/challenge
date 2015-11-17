package de.number26.storage;

import org.jetbrains.annotations.NotNull;

/**
 * @author vasiliy
 */
public interface TransactionNode {
    @NotNull Transaction getTransaction();

    double getSum();
    void addToSum(double delta);
}
