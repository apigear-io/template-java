package tbSame2.tbSame2serviceexample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;


//TODO for each interface there coudl be a tab? now only first one is added

import tbSame2.tbSame2_android_service.SameStruct1InterfaceServiceAdapter;
import tbSame2.tbSame2_android_service.SameStruct1InterfaceServiceFactory;
import tbSame2.tbSame2_android_service.SameStruct1InterfaceServiceStarter;

//import message type and parcelabe types
import tbSame2.tbSame2_api.Struct1;
import tbSame2.tbSame2_android_messenger.Struct1Parcelable;
import tbSame2.tbSame2_api.Struct2;
import tbSame2.tbSame2_android_messenger.Struct2Parcelable;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_android_messenger.Enum1Parcelable;
import tbSame2.tbSame2_api.Enum2;
import tbSame2.tbSame2_android_messenger.Enum2Parcelable;

import tbSame2.tbSame2_api.ISameStruct1InterfaceEventListener;
import tbSame2.tbSame2_api.ISameStruct1Interface;
import tbSame2.tbSame2_impl.SameStruct1InterfaceService;
import java.util.concurrent.CompletableFuture;



public class TbSame2TestServiceApp extends Activity implements ISameStruct1InterfaceEventListener
{

    private static final String TAG = "TbSame2TestServiceApp";
    static Intent stub_service = null;


    private ISameStruct1Interface mBackend = null;

    private TextView outputTextViewProp;
    private TextView outputTextViewSig;
    private TextView outputTextVieMethodRes;
    private TextView outputTextViewBIND;
    private String lastServicePackage ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        View spacer = new View(this);
        LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                200 // height in pixels, adjust as needed
        );
        layout.addView(spacer, spacerParams);

        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        layout.setBackgroundColor(Color.YELLOW);

        LinearLayout propertyButtonsLine = new LinearLayout(this);
        propertyButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        Button bProp1 = new Button(this);
        bProp1.setText("Set prop1");
        bProp1.setBackgroundColor(Color.GREEN);

        bProp1.setOnClickListener(v -> {
            Struct1 newProp1 = mBackend.getProp1();
            //TODO increment
            Log.i(TAG, "SET prop1" + newProp1);
            mBackend.setProp1(newProp1);
        });
        propertyButtonsLine.addView(bProp1);

        layout.addView(propertyButtonsLine);


        LinearLayout methodButtonsLine = new LinearLayout(this);
        methodButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        Button bSig1 = new Button(this);
        bSig1.setText("sig1");

        bSig1.setOnClickListener(v -> {
            Log.w(TAG, "broadcasting singal  sig1 ");
            Struct1 param1 =  new Struct1();
            mBackend.fireSig1(param1);
        });
        bSig1.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bSig1);

        layout.addView(methodButtonsLine);

        Button testButton5 = new Button(this);
        testButton5.setText("start service");
        testButton5.setOnClickListener(v -> {
            startMyService();
        });
        testButton5.setBackgroundColor(Color.GREEN);
        layout.addView(testButton5);

        Button testButton6 = new Button(this);
        testButton6.setText("stop service");
        testButton6.setOnClickListener(v -> {
            stopMyService();
        });
        testButton6.setBackgroundColor(Color.GREEN);
        layout.addView(testButton6);

        outputTextViewProp = new TextView(this);
        outputTextViewProp.setText("Property received from service will appear here");
        outputTextViewProp.setTextSize(16);
        outputTextViewProp.setPadding(16, 16, 16, 16); // optional for spacing
        layout.addView(outputTextViewProp);


        outputTextViewSig = new TextView(this);
        outputTextViewSig.setText("Singals will apper here");
        outputTextViewSig.setTextSize(16);
        outputTextViewSig.setPadding(16, 16, 16, 16); // optional for spacing
        layout.addView(outputTextViewSig);


        outputTextVieMethodRes = new TextView(this);
        outputTextVieMethodRes.setText("Method result will appear here");
        outputTextVieMethodRes.setTextSize(16);
        outputTextVieMethodRes.setPadding(16, 16, 16, 16); // optional for spacing
        layout.addView(outputTextVieMethodRes);

        setContentView(layout);
    }

    @Override
    protected void onStart()
    {
        Log.v(TAG, "My app: onStart, binding if not bound");
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        Log.v(TAG, "My app: onStop, NOT UNBINDING");
        super.onStop();
    }
    
    private void startMyService(){
        stub_service = new Intent(this, SameStruct1InterfaceServiceAdapter.class);
        this.startService(stub_service);
        Log.w(TAG, "Service started with stub backend");
        mBackend = SameStruct1InterfaceServiceAdapter.setService(SameStruct1InterfaceServiceFactory.get());
        mBackend.addEventListener(this);
    }

    public void stopMyService() {
        mBackend.removeEventListener(this);
        if (stub_service!= null)
        {
            this.stopService(stub_service);
        }
        stub_service = null;
        mBackend = null;
    }

    @Override
    protected void onDestroy()
    {
        Log.v(TAG, "My app: onDestroy, stop service");
        stopMyService();
        super.onDestroy();
    }
    @Override
    public void onProp1Changed(Struct1 newValue)
    {
        outputTextViewProp.setText("Property from service: prop1 " + newValue);
        Log.w(TAG, "Property from service: prop1 " + newValue);
     }
    @Override
    public void onSig1(Struct1 param1)
    {
        String text = "Signal sig1 "+ " " + param1;
        outputTextViewSig.setText(text);
        Log.w(TAG, text);
    }
    @Override
    public void on_readyStatusChanged(boolean isReady)
    { 
         if (isReady)
         {
             Log.w(TAG, "Connected to service ");
         }
         else
         {
             Log.w(TAG, "Disconnected from service ");
         }
    }


}
