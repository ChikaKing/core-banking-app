package com.chikason.corebankapp.controller;

import com.chikason.corebankapp.models.AccountHolder;
import com.chikason.corebankapp.models.TransactionDetail;
import com.chikason.corebankapp.service.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

@RestController
@RequestMapping("api/banking-app")
public class AppController {
    @Autowired
    private BankingService bankingService;
    private ExecutorService depositExecutor = Executors.newFixedThreadPool(2);

    @GetMapping("generate-accounts")
    public List<AccountHolder>  createNewAccounts() {
            bankingService.generatesBankAccounts("John Doe", "08055801677");
            bankingService.generatesBankAccounts("Chika King", "08044801687");
            bankingService.generatesBankAccounts("Smith Paul", "08010801670");
            bankingService.generatesBankAccounts("Micheal Tailor", "08054801661");
            bankingService.generatesBankAccounts("Uche Mathew", "08035801907");
            bankingService.generatesBankAccounts("Emmeka Eze", "09010801611");
            bankingService.generatesBankAccounts("Blessing Joy", "08010201604");
            bankingService.generatesBankAccounts("Ngozi Ofor", "07020441610");
            bankingService.generatesBankAccounts("Victor Osu", "08098803332");
            bankingService.generatesBankAccounts("Ify Ogbodu", "08014063399");

        return bankingService.getAllAccountHolder();
    }

    @GetMapping("withdraw")
    public AccountHolder withdraw(@RequestParam("account") String account,
                                       @RequestParam("amount") int amount) throws InterruptedException,
            ExecutionException, TimeoutException {

        Future<AccountHolder>  withdrawFuture = Executors.newSingleThreadExecutor().submit(() -> {
            AccountHolder holder = new AccountHolder();
            //generates different amount to deposit on account
            int[] amounts = generatesDepositAmounts(amount, 25);
            for(int amt : amounts) {
                holder = bankingService.withdraw(account, amt);
            }
            return holder;
        });

        while (!withdrawFuture.isDone()) {
            Thread.sleep(500);
        }

        return withdrawFuture.get(3, TimeUnit.MINUTES);
    }

    @GetMapping("deposit")
    public AccountHolder deposit(@RequestParam("account") String account,
                                       @RequestParam("amount") int amount) throws
            InterruptedException, ExecutionException, TimeoutException {

        Future<AccountHolder>  holderFuture = depositExecutor.submit(() -> {
            AccountHolder holder = new AccountHolder();
            //generates different amount to deposit on account
            int[] amounts = generatesDepositAmounts(amount, 20);
            for(int amt : amounts) {
                holder = bankingService.deposit(account, amt);
            }
            return holder;
        });

        while (!holderFuture.isDone()) {
            Thread.sleep(500);
        }

        return holderFuture.get(3, TimeUnit.MINUTES);
    }

    @GetMapping("transaction")
    public List<TransactionDetail> listTransactions(@RequestParam("account") String account) {

        return bankingService.getAccountTransactions(account);
    }

    private int[] generatesDepositAmounts(int initial, int size) {
        Random random = new Random();
        return random.ints(size, initial, 500).toArray();
    }

}
