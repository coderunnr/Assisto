package com.kani.project.assisto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;

import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by root on 1/10/16.
 */
public class PaymentAPI extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paid);
        pay();
    }

    public void pay()
    {
        //Getting the Service Instance. PaytmPGService.getStagingService()  will return the Service pointing to Staging Environment and PaytmPGService.getProductionService() will return the Service pointing to Production Environment.
        PaytmPGService Service = null;
        Service = PaytmPGService.getStagingService();
//or
      //  Service = PaytmPGService.getProductionService();
//Create new order Object having all order information.
        Random r = new Random(System.currentTimeMillis());
        String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("MID", "WorldP64425807474247");
        paramMap.put("CUST_ID","CUST11410") ;
        paramMap.put("CHANNEL_ID", "WEB");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
      //  paramMap.put("Key","kbzk1DSbJiV_O3p5");
        paramMap.put("WEBSITE", "worldpressplg");
        paramMap.put("TXN_AMOUNT", "30.0");
        paramMap.put("THEME ", "merchant");
        paramMap.put("MOBILE_NO","7777777777");
     //   paramMap.put("CALLBACK_URL","https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");

        PaytmOrder Order = new PaytmOrder(paramMap);
//Create new Merchant Object having all merchant configuration.
        PaytmMerchant Merchant = new PaytmMerchant( "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp", "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");

//Set PaytmOrder and PaytmMerchant objects. Call this method and set both objects before starting transaction.
        Service.initialize(Order, Merchant, null);

//Start the Payment Transaction. Before starting the transaction ensure that initialize method is called.
        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback()
        {
            @Override
            public void someUIErrorOccurred(String inErrorMessage)
            {
                // Some UI Error Occurred in Payment Gateway Activity.
                // This may be due to initialization of views in Payment Gateway Activity or may be due to initialization of webview.
                // Error Message details the error occurred.
            }

            @Override
            public void onTransactionSuccess(Bundle  inResponse)
            {
                // After successful transaction this method gets called.
                // Response bundle contains the merchant response parameters.
                Log.v(getClass().getSimpleName(),inResponse.toString());
            }

            @Override
            public void onTransactionFailure(String inErrorMessage, Bundle  inResponse)
            {
                // This method gets called if transaction failed.
                // Here in this case transaction is completed, but with a failure.
                // Error Message describes the reason for failure.
                // Response bundle contains the merchant response parameters.
            }

            @Override
            public void networkNotAvailable()
            {
                // If network is not available, then this method gets called.
            }

            @Override
            public void clientAuthenticationFailed(String  inErrorMessage)
            {
                // This method gets called if client authentication failed.
                // Failure may be due to following reasons
                //      1. Server error or downtime.
                //      2. Server unable to generate checksum or checksum response is
                //         not in proper format.
                //      3. Server failed to authenticate that client. That is value of
                //         payt_STATUS is 2.
                // Error Message describes the reason for failure.
            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingURL)
            {
                // This page gets called if some error occurred while loading some URL in Webview.
                // Error Code and Error Message describes the error.
                // Failing URL is the URL that failed to load.
            }

            @Override
            public void onBackPressedCancelTransaction() {

            }
        });
    }
}
