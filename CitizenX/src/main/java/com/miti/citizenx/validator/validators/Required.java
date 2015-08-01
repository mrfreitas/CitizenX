package com.miti.citizenx.validator.validators;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.TextView;
import com.miti.citizenx.validator.AbstractValidator;


/**
 * mrfreitas
 * Date: 20/07/2015
 * Time: 18:25
 */
public class Required extends AbstractValidator
{

    public Required(Context c, int messageRes)
    {
        super(c, messageRes);
    }

    public Required(String message)
    {
        super(message);
    }


    @Override
    public boolean validate(TextView textView)
    {
        String text = textView.getText().toString();
        boolean asError = TextUtils.isEmpty(text);
        textView.setError(asError ? errorMessage : null);
        return asError;
    }
}
