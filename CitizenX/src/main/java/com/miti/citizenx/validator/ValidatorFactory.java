package com.miti.citizenx.validator;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.miti.citizenx.R;
import com.miti.citizenx.validator.validators.*;

/**
 * mrfreitas
 * Date: 27/07/2015
 * Time: 13:54
 */
public class ValidatorFactory
{
    private FormValidator formValidator;

    public ValidatorFactory()
    {
        formValidator = new FormValidator();
    }

    public void nameValidate(Context c, TextView v, boolean addToForm)
    {
        // Name Validate
        Validator nameValidator = new Validator(v);
        nameValidator.addValidator(new Required(c.getResources().getString(R.string.empty_validation)));
        nameValidator.addValidator(new NameValidator(c.getResources().getString(R.string.name_validation)));
        v.setOnFocusChangeListener(nameValidator);
        if(addToForm)
        formValidator.addValidates(nameValidator);
    }

    public void emailValidate(Context c, TextView v , boolean addToForm)
    {
        Validator emailValidator = new Validator(v);
        emailValidator.addValidator(new Required(c.getResources().getString(R.string.empty_validation)));
        emailValidator.addValidator(new EmailValidator(c.getResources().getString(R.string.email_validation)));
        v.setOnFocusChangeListener(emailValidator);
        if(addToForm)
        formValidator.addValidates(emailValidator);
    }

    public void cEmailValidate(Context c, TextView v ,TextView v1, boolean addToForm)
    {
        Validator cEmailValidator = new Validator(v);
        cEmailValidator.addValidator(new ConfirmValidator(c.getResources().getString(R.string.conf_email_validation), v1));;
        v.setOnFocusChangeListener(cEmailValidator);
        formValidator.addValidates(cEmailValidator);
        if(addToForm)
            formValidator.addValidates(cEmailValidator);
    }

    public void passwordValidate(Context c, TextView v , boolean addToForm)
    {
        Validator passwordValidator = new Validator(v);
        passwordValidator.addValidator(new PasswordValidator(c.getResources().getString(R.string.password_validation), "", false));
        v.setOnFocusChangeListener(passwordValidator);
        formValidator.addValidates(passwordValidator);
        if(addToForm)
            formValidator.addValidates(passwordValidator);
    }

    public void cPasswordValidate(Context c, TextView v ,TextView v1, boolean addToForm)
    {
        Validator cPasswordValidator = new Validator(v);
        cPasswordValidator.addValidator(new ConfirmValidator(c.getResources().getString(R.string.conf_password_validation), v1));
        v.setOnFocusChangeListener(cPasswordValidator);
        formValidator.addValidates(cPasswordValidator);
        if(addToForm)
            formValidator.addValidates(cPasswordValidator);
    }

    public boolean formaValidate()
    {
        return formValidator.validate();
    }

}
