package com.chikason.corebankapp.service;


import com.chikason.corebankapp.models.AccountHolder;
import com.chikason.corebankapp.models.TransactionDetail;

import java.util.List;

public interface BankingService {

    public void generatesBankAccounts(String name, String phone);

    public AccountHolder withdraw(String account, double amount);

    public AccountHolder deposit(String account, double amount);

    public List<AccountHolder> getAllAccountHolder();

    public List<TransactionDetail> getAccountTransactions(String account);

}
