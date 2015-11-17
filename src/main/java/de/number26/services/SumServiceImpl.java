package de.number26.services;

import com.google.inject.Inject;
import de.number26.api.SumService;
import de.number26.api.dtos.TransactionDto;
import de.number26.api.dtos.TransactionParametersDto;
import de.number26.api.dtos.TransactionSumDto;
import de.number26.storage.Transaction;
import de.number26.storage.TransactionStorage;
import de.number26.storage.exception.StorageException;
import de.number26.storage.impl.TransactionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author vasiliy
 */
public class SumServiceImpl extends AbstractService implements SumService {
    private static final Logger LOG = LoggerFactory.getLogger(SumServiceImpl.class);

    private final TransactionStorage transactionStorage;

    @Inject
    public SumServiceImpl(TransactionStorage transactionStorage) {
        this.transactionStorage = transactionStorage;
    }

    @Override
    public void saveTransaction(long transactionId, TransactionParametersDto parameters) {
        LOG.info("saveTransaction(" + transactionId + ", " + parameters + ")");
        TransactionImpl transaction = new TransactionImpl(transactionId, parameters);
        try {
            transactionStorage.saveTransaction(transaction);
        } catch (StorageException e) {
            LOG.error("Storage exception with circular dependencies", e);
        }
    }

    @Override
    public TransactionDto getTransaction(long transactionId) {
        Transaction transaction = transactionStorage.getTransaction(transactionId);
        if (transaction != null) {
            return new TransactionDto(transaction);
        }
        return null;
    }

    @Override
    public long[] getIdsFromType(String type) {
        Set<Transaction> transactions = transactionStorage.getTransactions(type);
        long[] transactionIds = new long[transactions.size()];
        int i = 0;
        for (Transaction transaction : transactions) {
            transactionIds[i++] = transaction.getId();
        }
        return transactionIds;
    }

    @Override
    public TransactionSumDto getTransactionSum(long transactionId) {
        double transactionSum = transactionStorage.getTransactionSum(transactionId);
        return new TransactionSumDto(transactionSum);
    }
}
