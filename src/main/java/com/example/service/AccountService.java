package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository = null;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    
    /**
     * This is used to create new account for the users.
     * @param account Takes in a {@code Account} object to process the account creation request.
     * @return Account with a ID attach to the account if the process is sucessful or null if the process 
     * failed.
     */
    public Account register(Account account){
        if(account.getUsername().equals("") ||
            account.getPassword().length() < 4)
        {
            return null;
        }

        return accountRepository.save(account);
    }

    /**
     * This is used for the sole purpose of logging into the system by using a {@code Account} object.
     * @param account Take in a {@code Account} object to grab username and password to process request.
     * @return A {@code Account} object if the operation was successful or a {@code null} if there was an 
     * issue.
     */
    public Account login(Account account){
        Optional<Account> returnAccount = accountRepository.findByUsername(account.getUsername());
        Account newAccount = null;

        if(returnAccount.isPresent()){
            newAccount = returnAccount.get();
        } else {
            return null;
        }

        if(!account.getPassword().equals(newAccount.getPassword())){
            return null;
        }

        return newAccount;
    }

    /**
     * This will check whether or not the username is currently exist.
     * @param username Takes in a {@code String} of the username to find if the account exist.
     * @return It will return a {@code Boolean} of {@code True} if the account exist or 
     * {@code Fales} if the account doesn't exist. 
     */
    public boolean checkIfUsernameExist(String username){
        Optional<Account> account = accountRepository.findByUsername(username);
        if(account.isPresent()){
            return true;
        }
        return false;
    }

    /**
     * This will check whether or not the username is currently exist.
     * @param id Takes in a {@code Integer} of the username id to find if the account exist.
     * @return It will return a {@code Boolean} of {@code True} if the account exist or 
     * {@code Fales} if the account doesn't exist. 
     */
    public boolean checkIfUserExistByID(Integer id){
        if(id == null) {
            return false;
        }

        return accountRepository.existsById(id);
    }
}
