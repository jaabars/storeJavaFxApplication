package sample.models;

public class Account {
    private User user;
    private String login;
    private String password;
    private boolean active;

    public Account(User user, String login, String password, boolean active) {
        this.user = user;
        this.login = login;
        this.password = password;
        this.active = active;
    }

    public Account() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
