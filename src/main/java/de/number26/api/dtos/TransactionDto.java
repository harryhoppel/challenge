package de.number26.api.dtos;

import de.number26.storage.Transaction;
import org.jetbrains.annotations.NotNull;

/**
 * @author vasiliy
 */
public class TransactionDto {
    private long id;
    private long parentId;
    private String type;
    private double amount;

    public TransactionDto(@NotNull Transaction transaction) {
        this.id = transaction.getId();
        this.parentId = transaction.getParentId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
    }

    public long getId() {
        return id;
    }

    public long getParentId() {
        return parentId;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}
