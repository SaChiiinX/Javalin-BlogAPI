package Service;

import DAO.AccountDao;
import Model.Account;

/**
 * Service class for managing user accounts.
 */
public class AccountService {
    private AccountDao accountDao;

    /**
     * Default constructor that initializes the AccountDao.
     */
    public AccountService(){
        this.accountDao = new AccountDao();
    }

    /**
     * Constructor with a custom AccountDao.
     * @param accountDao The AccountDao to be used by this service.
     */
    public AccountService(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    /**
     * Registers a new user account if the username is not blank, the password length is at least 4 characters, 
     * and the username does not already exist.
     * @param account The account information to be registered.
     * @return The registered Account object if successful, or null if the registration failed.
     */
    public Account registerUser(Account account){
        if(account.username.isBlank() || account.password.length() < 4 || this.accountDao.isUser(account.username)){
            return null;
        }
        return this.accountDao.registerUser(account);
    }

    /**
     * Verifies the account details by checking the provided account information against the data store.
     * @param account The account details to verify.
     * @return The verified Account object if the details are correct, or null if they are incorrect.
     */
    public Account verifyAccountDetails(Account account){
        return this.accountDao.verifyAccountDetails(account);
    }

    /**
     * Checks if a user with the given username exists in the data store.
     * @param username The username to check.
     * @return true if the user exists, false otherwise.
     */
    public boolean isUser(String username){
        return this.accountDao.isUser(username);
    }

    /**
     * Checks if a user with the given ID exists in the data store.
     * @param posted_by The ID of the user to check.
     * @return true if the user exists, false otherwise.
     */
    public boolean isUser(int posted_by) {
        return this.accountDao.isUser(posted_by);
    }
} 
