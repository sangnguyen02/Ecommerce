package com.example.ecommerce.User.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PaymentFragmentUser extends Fragment {

    PaymentButtonContainer paymentButtonContainer;
    Button VNPayBtt;
    private static final String TAG = "MyTag";
    private DatabaseReference databaseReference;

    private float currencyRate = 0.04f;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_payment, container, false);
        paymentButtonContainer = rootView.findViewById(R.id.payment_button_container);

        // Initialize Firebase
        FirebaseApp.initializeApp(requireContext());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Order").child("-Njp-kTCxUGK_nj6O2dO").child("price");

       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               Float priceVND =snapshot.getValue(Float.class);
               priceVND = (float)Math.round(priceVND/1000);
               if(priceVND!=null) {
                   Float price=convertVndToUsd(priceVND);
                   setupPayPal(price);
               }


           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Log.e(TAG, "onCancelled: "+ error.getMessage());
           }
       });



        //VNPay
//        VNPayBtt=rootView.findViewById(R.id.btnOpenSdk);
//        VNPayBtt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openSdk();
//            }
//        });
        return rootView;
    }

//    public void openSdk() {
//        Intent intent = new Intent(requireActivity(), VNP_AuthenticationActivity.class);
//        intent.putExtra("url", "https://sandbox.vnpayment.vn/testsdk/"); //bắt buộc, VNPAY cung cấp
//        intent.putExtra("tmn_code", "FAHASA03"); //bắt buộc, VNPAY cung cấp
//        intent.putExtra("scheme", "resultactivity"); //bắt buộc, scheme để mở lại app khi có kết quả thanh toán từ mobile banking
//        intent.putExtra("is_sandbox", false); //bắt buộc, true <=> môi trường test, true <=> môi trường live
//        VNP_AuthenticationActivity.setSdkCompletedCallback(new VNP_SdkCompletedCallback() {
//            @Override
//            public void sdkAction(String action) {
//                Log.wtf("SplashActivity", "action: " + action);
//                //action == AppBackAction
//                //Người dùng nhấn back từ sdk để quay lại
//
//                //action == CallMobileBankingApp
//                //Người dùng nhấn chọn thanh toán qua app thanh toán (Mobile Banking, Ví...)
//                //lúc này app tích hợp sẽ cần lưu lại cái PNR, khi nào người dùng mở lại app tích hợp thì sẽ gọi kiểm tra trạng thái thanh toán của PNR Đó xem đã thanh toán hay chưa.
//
//                //action == WebBackAction
//                //Người dùng nhấn back từ trang thanh toán thành công khi thanh toán qua thẻ khi url có chứa: cancel.sdk.merchantbackapp
//
//                //action == FaildBackAction
//                //giao dịch thanh toán bị failed
//
//                //action == SuccessBackAction
//                //thanh toán thành công trên webview
//            }
//        });
//        startActivity(intent);
//    }

    //Paypal method
    private float convertVndToUsd(float priceVnd) {

        return priceVnd * currencyRate;
    }


    private  void setupPayPal(Float price){
        paymentButtonContainer.setup(

                new CreateOrder() {

                    @Override
                    public void create(@NonNull CreateOrderActions createOrderActions) {
                        Log.d(TAG, "create: ");
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.USD)
                                                        .value(Float.toString(price))
                                                        .build()
                                        )
                                        .build()
                        );
                        OrderRequest order = new OrderRequest(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NonNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                Log.d(TAG, String.format("CaptureOrderResult: %s", result));
                                Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        );

    }


}