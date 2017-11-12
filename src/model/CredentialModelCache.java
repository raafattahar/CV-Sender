package model;

import model.authentification.Credential;

public class CredentialModelCache
{
    private Credential credential;

    public CredentialModelCache(Credential credential)
    {
        this.credential = credential;
    }

    public Credential getCredential()
    {
        return credential;
    }
}
