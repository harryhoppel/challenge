package de.number26.storage.impl;

import de.number26.api.dtos.TransactionParametersDto;
import de.number26.storage.Transaction;
import org.jetbrains.annotations.NotNull;

/**
 * @author vasiliy
 */
public class TransactionImpl implements Transaction {
    private long id;
    private Long parentId;
    private double amount;
    private String type;

    public TransactionImpl(long id, Long parentId, String type, double amount) {
        this.id = id;
        this.parentId = parentId;
        this.amount = amount;
        this.type = type;
    }

    public TransactionImpl(long id, @NotNull TransactionParametersDto parameters) {
        this.id = id;
        this.parentId = parameters.getParentId();
        this.amount = parameters.getAmount();
        this.type = parameters.getType();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Long getParentId() {
        return parentId;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TransactionImpl{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionImpl that = (TransactionImpl) o;

        if (id != that.id) return false;
        if (Double.compare(that.amount, amount) != 0) return false;
        //noinspection SimplifiableIfStatement
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null) return false;
        return type.equals(that.type);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + type.hashCode();
        return result;
    }
}
