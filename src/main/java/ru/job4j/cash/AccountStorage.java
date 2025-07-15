package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {
    @GuardedBy("this")
    private final Map<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    public synchronized boolean update(Account account) {
        if (!accounts.containsKey(account.id())) {
            return false;
        }
        accounts.put(account.id(), account);
        return true;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        Account from = accounts.get(fromId);
        Account to = accounts.get(toId);

        if (from == null || to == null || from.amount() < amount) {
            return false;
        }

        accounts.put(fromId, new Account(fromId, from.amount() - amount));
        accounts.put(toId, new Account(toId, to.amount() + amount));

        return true;
    }
}