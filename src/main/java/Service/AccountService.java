package Service;

import DAO.AccountDao;
import Model.Account;

public class AccountService {
    private AccountDao accountDao;

    public AccountService(){
        this.accountDao = new AccountDao();
    }

    public AccountService(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    public Account registerUser(Account account){
        if(account.username.isBlank() || account.password.length() < 4 || this.accountDao.isUser(account.username)){
            return null;
        }
        return this.accountDao.registerUser(account);
    }

    public Account verifyAccountDetails(Account account){
        return this.accountDao.verifyAccountDetails(account);
    }

    public boolean isUser(String username){
        return this.accountDao.isUser(username);
    }

    public boolean isUser(int posted_by) {
        return this.accountDao.isUser(posted_by);
    }
}   
