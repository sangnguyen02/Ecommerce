package com.example.ecommerce.User.Paypal;

import android.app.Application;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

import utils.LocalDataManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PayPalCheckout.setConfig(new CheckoutConfig(
                this,
                "AQLjybR6ovCpMh9MsXqlUVOjgfj8ZqUHfyE0xW6RHMdwFBhpRVwziolihVSBWjiZWNmKeg-fwrxGUSuI",
                Environment.SANDBOX,
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                "com.example.ecommerce://paypalpay"
        ));

        LocalDataManager.init(getApplicationContext());
    }
}
