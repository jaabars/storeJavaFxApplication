package sample.db;

import sample.db.impl.DbHelperImpl;
import sample.models.Account;
import sample.models.User;

public interface DbHelper {

    DbHelper INSTANCE = new DbHelperImpl();

    Account findUserByLogin(String login);

    boolean isLoginUnique(String login);

    User saveUser(User user);

    boolean saveAccount(Account account);
}
