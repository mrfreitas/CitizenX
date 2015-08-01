package com.miti.citizenx.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * mrfreitas
 * Date: 22/07/2015
 * Time: 14:45
 */
public class UserEntity extends RealmObject
{
    @PrimaryKey
    private int idUser;
    private String userType;
    private String password;
    private String picture;
    private String name;
    private String email;
    private String address;
    private String pCode;
    private String city;
    private String sex;
    private String bDate;

    // User account (relation Many to One)
    private AccountEntity account;

    public AccountEntity getAccount()
    {
        return account;
    }
    public void setAccount(AccountEntity account)
    {
        this.account = account;
    }

    public int getIdUser()
    {
        return idUser;
    }
    public void setIdUser(int idUser)
    {
        this.idUser = idUser;
    }

    public String getPicture()
    {
        return picture;
    }
    public void setPicture(String picture)
    {
        this.picture = "users/"+name+"/"+picture;
    }

    public String getUserType()
    {
        return userType;
    }
    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAddress()
    {
        return address;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getpCode()
    {
        return pCode;
    }
    public void setpCode(String pCode)
    {
        this.pCode = pCode;
    }

    public String getCity()
    {
        return city;
    }
    public void setCity(String city)
    {
        this.city = city;
    }

    public String getSex()
    {
        return sex;
    }
    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getbDate()
    {
        return bDate;
    }
    public void setbDate(String bDate)
    {
        this.bDate = bDate;
    }
}
