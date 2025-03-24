package com.example.perpusapi.service;

import com.example.perpusapi.model.Account;
import com.example.perpusapi.model.Member;
import com.example.perpusapi.repository.AccountRepository;
import com.example.perpusapi.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

public class AccountService {
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    public AccountService(AccountRepository accountRepository, MemberRepository memberRepository) {
        this.accountRepository = accountRepository;
        this.memberRepository = memberRepository;
    }

    public AccountService(MemberRepository memberRepo) {
        this(null, memberRepo);
    }

    public AccountService(AccountRepository accountRepo) {
        this(accountRepo, null);
    }

    public Member getProfileByEmail(String email){
        Optional<Account> accountOpt = this.accountRepository.findByEmail(email);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Email tidak ditemukan");
        }

        Account account = accountOpt.get();
        Optional<Member> memberOpt = this.memberRepository.findByUserId(account.getUser_id());
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            member.setEmail(email);
            return (member);
        }
        return null;
    }

    public Member getProfileByUserId(int id){
        Optional<Member> memberOpt = memberRepository.findByUserId(id);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            Optional<Account> acc = accountRepository.findById(id);
            member.setEmail(acc.get().getEmail());
            return member;
        }
        return null;
    }

    public List<Account> getAllAccount(){
        return this.accountRepository.getAll();
    }
}

