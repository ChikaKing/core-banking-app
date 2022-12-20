package com.chikason.corebankapp.service;

import com.chikason.corebankapp.models.AccountHolder;
import com.chikason.corebankapp.models.TransactionDetail;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BankingServiceImpl implements BankingService {
    private List<AccountHolder> bankAccounts = new ArrayList<>();
    private List<TransactionDetail> bankTransactions = new ArrayList<>();

    @Override
    public void generatesBankAccounts(String name, String phone) {
        AccountHolder user = new AccountHolder();
        user.setName(name);
        user.setPhone(phone);
        user.setBalance(0.0);
        int totalUser = bankAccounts.size();
        String acct_no;
        String ACCOUNT_NO_PREFIX = "001030";
        if(totalUser < 10) {
           acct_no = ACCOUNT_NO_PREFIX + "00" + totalUser + 1;
        }
        else {
            acct_no = ACCOUNT_NO_PREFIX + "0" + totalUser;
        }
        user.setNumber(acct_no);
        bankAccounts.add(user);
    }

    @Override
    public AccountHolder withdraw(String account, double amount) {
        return processTransaction(account, amount, "withdrawal");
    }

    @Override
    public AccountHolder deposit(String account, double amount) {

        return processTransaction(account, amount, "deposit");
    }

    @Override
    public List<AccountHolder> getAllAccountHolder() {
        return bankAccounts;
    }

    @Override
    public List<TransactionDetail> getAccountTransactions(String account) {
        List<TransactionDetail> ownerTransactions = new ArrayList<>();
        for(TransactionDetail transaction : bankTransactions) {
            if(transaction.getNumber().equals(account)) {
                ownerTransactions.add(transaction);
            }
        }

        if(ownerTransactions.isEmpty()){
            return bankTransactions;
        }
        else {
            return ownerTransactions;
        }
    }

    private AccountHolder processTransaction(String account, double amount, String type) {
        for(int i= 0; i < bankAccounts.size(); i++) {
            if(bankAccounts.get(i).getNumber().equals(account)) {
                AccountHolder accountHolder = bankAccounts.get(i);
                double balance;
                if(type.equals("withdrawal")){
                    balance = accountHolder.getBalance() - amount;
                }
                else {
                    balance = accountHolder.getBalance() + amount;
                }
                accountHolder.setBalance(balance);
                bankAccounts.set(i, accountHolder);
                //create new transaction detail
                newAccountTransaction(account, type, amount);

                return accountHolder;
            }
        }

        return null;
    }

    private void newAccountTransaction(String account, String type, double amount) {
        for(TransactionDetail transaction : bankTransactions) {
            if(transaction.getNumber() == account) {
                TransactionDetail detail = new TransactionDetail();
                detail.setAmount(amount);
                detail.setDateTime(LocalDateTime.now().toString());
                detail.setType(type);
                bankTransactions.add(detail);
            }
        }
    }


}
