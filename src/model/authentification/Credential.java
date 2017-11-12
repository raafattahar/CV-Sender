package model.authentification;

public class Credential
{
    private String login;
    private String password;

    public Credential(String login, String password)
    {
        this.login = login;
        this.password = password;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getLogin()
    {
        return login;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void reset()
    {
        login = null;
        password = null;
    }

    public String getPassword()
    {
        return password;
    }

    public boolean isValid()
    {
        return !(login == null || login.isEmpty() || password == null || password.isEmpty());
    }
}