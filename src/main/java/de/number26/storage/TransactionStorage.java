package de.number26.storage;

import de.number26.storage.exception.StorageException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * OK, so we need to implement in-memory storage that not only returns all transactions by type,
 * but also modifies and manages some tree-based structure that manages sums of transactions.
 *
 * It looks like the most tricky part is to manage these sums, because after we get it right,
 * all we need to do is to atomically add a new transaction to a list for it's type.
 *
 * We can manage these sums in several ways; most of them, I think, will boil down to one of
 * a two choices:
 * – we either update the tree with calculated sum when we save a transaction;
 * – or we calculate the sum of transaction and all of its parents when we need to read it.
 *
 * The first approach looks better for a read-intensive application, because we will be able to
 * retrieve values with O(1), and save them (in the most brutal and worst case) in O(n) (n here
 * is the number of all values in the storage), but in the probable case in O(k), where k is the
 * height of a tree (i.e. the number of parents of this transaction or the number of nodes in the
 * tree that we need to update with a new value). And the k will be really small if we'll have
 * a large number of transaction types and/or a small number of every transaction grandparents.
 *
 * By the way, if the tree height becomes too large we might make some optimizations like
 * calculating partial sums and storing them in intermediate nodes in our tree structure.
 *
 * Now, about synchronization: if we have several structures – one to hold sums and transactions and
 * the other to hold types and transactions – in general we need to update them atomically, so that
 * when we add transactions in parallel in several threads, we still have a consistent storage. But with our API
 * we might not do that and still have a consistent API behavior (except for one case – when a malicious attacker
 * tries to create circular references in our storage, but that seems to be a very strange case. And we can
 * handle that quite easily anyway). Still, in general case we might make some entity that could provide us
 * with synchronization locks based on transactions we are modifying. In that case we still could achieve a
 * high write and read performance (without threads unnecessary blocking each other).
 *
 * @author vasiliy
 */
public interface TransactionStorage {
    @Nullable
    Transaction getTransaction(long transactionId);

    @NotNull
    Set<Transaction> getTransactions(@NotNull String transactionType);

    double getTransactionSum(long transactionId);

    void saveTransaction(Transaction transaction) throws StorageException;

    void saveTransactions(Transaction... transactions) throws StorageException;
}
