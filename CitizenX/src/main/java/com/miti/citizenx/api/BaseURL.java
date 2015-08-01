package com.miti.citizenx.api;

/**
 * mrfreitas
 * Date: 27/07/2015
 * Time: 15:15
 */
public enum BaseURL
{
    BASE_URL("http://citizenx-mrfreitas.rhcloud.com/webapi"),
    PHOTO_URL("http://citizenx-mrfreitas.rhcloud.com/uploads/");

    private String baseUrl;

    private BaseURL(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }


    @Override
    public String toString()
    {
        return this.baseUrl;
    }
}
