package ru.job4j.cash;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AccountStorageTest {

    @Test
    void whenAdd() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(100);
    }

    @Test
    void whenAddNewAccountThenReturnsTrueAndAccountExists() {
        AccountStorage storage = new AccountStorage();
        Account account = new Account(1, 100);

        boolean result = storage.add(account);

        assertThat(result).isTrue();
        Optional<Account> saved = storage.getById(1);
        assertThat(saved).isPresent();
        assertThat(saved.get().amount()).isEqualTo(100);
    }

    @Test
    void whenUpdate() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.update(new Account(1, 200));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(200);
    }

    @Test
    void whenUpdateExistingAccountThenReturnsTrueAndUpdated() {
        AccountStorage storage = new AccountStorage();
        Account initial = new Account(1, 100);
        storage.add(initial);

        Account updated = new Account(1, 200);
        boolean result = storage.update(updated);

        assertThat(result).isTrue();
        Optional<Account> account = storage.getById(1);
        assertThat(account).isPresent();
        assertThat(account.get().amount()).isEqualTo(200);
    }

    @Test
    void whenUpdateNonExistingAccountThenReturnsFalse() {
        AccountStorage storage = new AccountStorage();

        Account account = new Account(1, 100);
        boolean result = storage.update(account);

        assertThat(result).isFalse();
    }

    @Test
    void whenDelete() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.delete(1);
        assertThat(storage.getById(1)).isEmpty();
    }

    @Test
    void whenTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        storage.transfer(1, 2, 100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(0);
        assertThat(secondAccount.amount()).isEqualTo(200);
    }

    @Test
    void whenTransferFromOneToAnotherAccountThenSuccess() {
        AccountStorage storage = new AccountStorage();
        Account account1 = new Account(1, 100);
        Account account2 = new Account(2, 50);

        storage.add(account1);
        storage.add(account2);

        boolean result = storage.transfer(1, 2, 30);

        assertThat(result).isTrue();
        Optional<Account> optionalAccount1 = storage.getById(1);
        if (optionalAccount1.isPresent()) {
            assertThat(optionalAccount1.get().amount()).isEqualTo(70);
        } else {
            throw new AssertionError("Account with ID 1 not found");
        }
        Optional<Account> optionalAccount2 = storage.getById(2);
        if (optionalAccount2.isPresent()) {
            assertThat(optionalAccount2.get().amount()).isEqualTo(80);
        } else {
            throw new AssertionError("Account with ID 2 not found");
        }
    }

    @Test
    void whenFromAccountNotExistThenTransferFalse() {
        AccountStorage storage = new AccountStorage();
        storage.add(new Account(2, 50));

        boolean result = storage.transfer(1, 2, 30);

        assertThat(result).isFalse();
    }

    @Test
    void whenToAccountNotExistThenTransferFalse() {
        AccountStorage storage = new AccountStorage();
        storage.add(new Account(1, 100));

        boolean result = storage.transfer(1, 2, 30);

        assertThat(result).isFalse();
    }

    @Test
    void whenNotEnoughMoneyThenTransferFalse() {
        AccountStorage storage = new AccountStorage();
        storage.add(new Account(1, 50));
        storage.add(new Account(2, 30));

        boolean result = storage.transfer(1, 2, 60);

        assertThat(result).isFalse();
        Account account1 = storage.getById(1).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        assertThat(account1.amount()).isEqualTo(50);
        Account account2 = storage.getById(2).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        assertThat(account2.amount()).isEqualTo(30);
    }
}