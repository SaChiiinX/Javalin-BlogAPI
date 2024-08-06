package Service;

import java.util.List;

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

    public boolean isUserValid(int posted_by) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isUserValid'");
    }


    


}   
