package com.example.perpusapi.service;

import com.example.perpusapi.model.Account;
import com.example.perpusapi.repository.AccountRepository;
import com.example.perpusapi.util.HashUtil;

import java.util.Optional;

public class AuthService {
    private final AccountRepository accountRepository;

    public AuthService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean login(String email, String password) {
        password = HashUtil.sha256(password);

        Optional<Account> accountOpt = accountRepository.findByEmail(email);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            return account.getPassword().equals(password);
        }

        return false;
    }

    public Optional<Account> register(String email, String password) {
        if (accountRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email sudah terdaftar");
        }
        password = HashUtil.sha256(password);


        Account account = new Account();
        account.setEmail(email);
        account.setPassword(password);

        if (accountRepository.insert(account)) {
            if (accountRepository.findByEmail(email).isPresent()) {
                return accountRepository.findByEmail(email);
            }else{
                return Optional.empty();
            }
        }

        return Optional.empty();

    }
}
