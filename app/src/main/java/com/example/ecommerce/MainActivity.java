package com.example.ecommerce;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    public static  final  int PAYPAL_REQUEST_CODE =7171;

    private  static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    Button btnPlayNow;
    EditText edtAmount;

    String Amount="";

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start PayPal Service
        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);


        btnPlayNow=(Button) findViewById(R.id.btnPayNow);
        edtAmount=(EditText) findViewById(R.id.edtAmount);

        btnPlayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }

    private void processPayment() {
        Amount=edtAmount.getText().toString();
        PayPalPayment payPalPayment= new PayPalPayment(new BigDecimal(String.valueOf(Amount)),"USD"
                                    ,"Donate for Raticate",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PAYPAL_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                PaymentConfirmation confirmation=data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation!=null){
                    try{
                        String paymentDetails= confirmation.toJSONObject().toString(4);

                        startActivity(new Intent(this, paymentDetails.class)
                                .putExtra("paymentDetails",paymentDetails)
                                .putExtra("PaymentAmount", Amount)
                        );
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }else if(resultCode==RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode==PaymentActivity.RESULT_EXTRAS_INVALID){
            {
                Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
            }
        }
    }
}