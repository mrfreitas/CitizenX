package com.miti.citizenx.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * mrfreitas
 * Date: 22/07/2015
 * Time: 14:47
 */
public class AccountEntity extends RealmObject
{
    @PrimaryKey
    private int idAccount;
    private String type;
    private String cDate;
    private String status;

    public int getIdAccount()
    {
        return idAccount;
    }

    public void setIdAccount(int idAccount)
    {
        this.idAccount = idAccount;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getcDate()
    {
        return cDate;
    }

    public void setcDate(String cDate)
    {
        this.cDate = cDate;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

}
