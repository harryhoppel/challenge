package de.number26.api.dtos;

import de.number26.storage.Transaction;

import java.util.List;

/**
 * @author vasiliy
 */
public class IdsListDto {
    private long[] transactionIds;

    public IdsListDto() {}

    public IdsListDto(List<Transaction> transactions) {
    }

    public long[] getTransactionIds() {
        return transactionIds;
    }

    public void setTransactionIds(long[] transactionIds) {
        this.transactionIds = transactionIds;
    }
}
