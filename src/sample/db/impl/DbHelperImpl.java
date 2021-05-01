package sample.db.impl;

import sample.db.DbHelper;
import sample.models.Account;
import sample.models.User;
import sun.java2d.opengl.OGLRenderQueue;

import java.sql.*;

public class DbHelperImpl implements DbHelper {

    @Override
    public Account findUserByLogin(String login) {
        Connection connection = null;
        try {
            connection = getConnection();
            String findByLoginQuery = "SELECT  u.id,u.name,a.login,a.password,a.active FROM accounts as a JOIN users as u on\n" +
                    "a.user_id = u.id WHERE login =?";
            PreparedStatement preparedStatement = connection.prepareStatement(findByLoginQuery);
            preparedStatement.setString(1,login);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Account account = new Account();
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                account.setUser(user);
                account.setLogin(resultSet.getString(3));
                account.setPassword(resultSet.getString(4));
                account.setActive(resultSet.getInt(5)==1?true:false);
                return account;
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public boolean isLoginUnique(String login) {
        Connection connection = null;
        try {
            connection = getConnection();
            String isUniqueLoginQuery ="SELECT count(login) FROM accounts WHERE login=?";
            PreparedStatement preparedStatement = connection.prepareStatement(isUniqueLoginQuery);
            preparedStatement.setString(1,login);
            ResultSet resultSet = preparedStatement.executeQuery();
                int result = resultSet.getInt(1);
                if (result >=1) {
                    return false;
                }
                return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public User saveUser(User user) {
        Connection connection = null;
        try {
            connection = getConnection();
            String insertUserQuery = "INSERT INTO users (name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,user.getName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()){
                user.setId(resultSet.getLong(1));
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public boolean saveAccount(Account account) {
        Connection connection = null;
        try {
            connection = getConnection();
            String accountInsertQuery = "INSERT INTO accounts (user_id,login,password,active) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(accountInsertQuery);
            preparedStatement.setLong(1,account.getUser().getId());
            preparedStatement.setString(2,account.getLogin());
            preparedStatement.setString(3,account.getPassword());
            preparedStatement.setInt(4,account.isActive()?1:0);
            int resultOfAffectedRows = preparedStatement.executeUpdate();
            return resultOfAffectedRows==1?true:false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return false;
    }

    private Connection getConnection() throws SQLException {

           Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\DBsql\\store.db");
            return connection;

    }
}
