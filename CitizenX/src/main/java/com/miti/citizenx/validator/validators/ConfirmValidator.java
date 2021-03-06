package com.miti.citizenx.validator.validators;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;
import com.miti.citizenx.validator.AbstractValidator;

/**
 * mrfreitas
 * Date: 20/07/2015
 * Time: 20:58
 */
public class ConfirmValidator extends AbstractValidator
{
    final TextView TO_COMPARE;
    public ConfirmValidator(Context c, int messageRes, TextView toCompare)
    {
        super(c, messageRes);
        this.TO_COMPARE = toCompare;
    }

    public ConfirmValidator(String message, TextView toCompare)
    {
        super(message);
        this.TO_COMPARE = toCompare;
    }


    @Override
    public boolean validate(TextView textView)
    {
        String toCompareStr = TO_COMPARE.getText().toString();
        String sourceStr = textView.getText().toString();
        boolean asError = (!TextUtils.isEmpty(toCompareStr) && !sourceStr.equals(toCompareStr));
        textView.setError(asError ? errorMessage : null);
        return asError;
    }
}
