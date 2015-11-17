package de.number26.storage;

import org.jetbrains.annotations.Nullable;

/**
 * @author vasiliy
 */
public interface Transaction {
    long getId();
    @Nullable Long getParentId();

    String getType();

    double getAmount();
}
