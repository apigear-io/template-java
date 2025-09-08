package testbed1.testbed1serviceexample;

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

import testbed1.testbed1_android_service.StructInterfaceServiceAdapter;
import testbed1.testbed1_android_service.StructInterfaceServiceFactory;
import testbed1.testbed1_android_service.StructInterfaceServiceStarter;

//import message type and parcelabe types
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_android_messenger.StructBoolParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;

import testbed1.testbed1_api.IStructInterfaceEventListener;
import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_impl.StructInterfaceService;
import java.util.concurrent.CompletableFuture;



public class Testbed1TestServiceApp extends Activity implements IStructInterfaceEventListener
{

    private static final String TAG = "Testbed1TestServiceApp";
    static Intent stub_service = null;


    private IStructInterface mBackend = null;

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
        Button bPropBool = new Button(this);
        bPropBool.setText("Set propBool");
        bPropBool.setBackgroundColor(Color.GREEN);

        bPropBool.setOnClickListener(v -> {
            StructBool newPropBool = mBackend.getPropBool();
            //TODO increment
            Log.i(TAG, "SET propBool" + newPropBool);
            mBackend.setPropBool(newPropBool);
        });
        propertyButtonsLine.addView(bPropBool);
        Button bPropInt = new Button(this);
        bPropInt.setText("Set propInt");
        bPropInt.setBackgroundColor(Color.GREEN);

        bPropInt.setOnClickListener(v -> {
            StructInt newPropInt = mBackend.getPropInt();
            //TODO increment
            Log.i(TAG, "SET propInt" + newPropInt);
            mBackend.setPropInt(newPropInt);
        });
        propertyButtonsLine.addView(bPropInt);
        Button bPropFloat = new Button(this);
        bPropFloat.setText("Set propFloat");
        bPropFloat.setBackgroundColor(Color.GREEN);

        bPropFloat.setOnClickListener(v -> {
            StructFloat newPropFloat = mBackend.getPropFloat();
            //TODO increment
            Log.i(TAG, "SET propFloat" + newPropFloat);
            mBackend.setPropFloat(newPropFloat);
        });
        propertyButtonsLine.addView(bPropFloat);
        Button bPropString = new Button(this);
        bPropString.setText("Set propString");
        bPropString.setBackgroundColor(Color.GREEN);

        bPropString.setOnClickListener(v -> {
            StructString newPropString = mBackend.getPropString();
            //TODO increment
            Log.i(TAG, "SET propString" + newPropString);
            mBackend.setPropString(newPropString);
        });
        propertyButtonsLine.addView(bPropString);

        layout.addView(propertyButtonsLine);


        LinearLayout methodButtonsLine = new LinearLayout(this);
        methodButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        Button bSigBool = new Button(this);
        bSigBool.setText("sigBool");

        bSigBool.setOnClickListener(v -> {
            Log.w(TAG, "broadcasting singal  sigBool ");
            StructBool paramBool =  new StructBool();
            mBackend.fireSigBool(paramBool);
        });
        bSigBool.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bSigBool);
        Button bSigInt = new Button(this);
        bSigInt.setText("sigInt");

        bSigInt.setOnClickListener(v -> {
            Log.w(TAG, "broadcasting singal  sigInt ");
            StructInt paramInt =  new StructInt();
            mBackend.fireSigInt(paramInt);
        });
        bSigInt.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bSigInt);
        Button bSigFloat = new Button(this);
        bSigFloat.setText("sigFloat");

        bSigFloat.setOnClickListener(v -> {
            Log.w(TAG, "broadcasting singal  sigFloat ");
            StructFloat paramFloat =  new StructFloat();
            mBackend.fireSigFloat(paramFloat);
        });
        bSigFloat.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bSigFloat);
        Button bSigString = new Button(this);
        bSigString.setText("sigString");

        bSigString.setOnClickListener(v -> {
            Log.w(TAG, "broadcasting singal  sigString ");
            StructString paramString =  new StructString();
            mBackend.fireSigString(paramString);
        });
        bSigString.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bSigString);

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
        stub_service = new Intent(this, StructInterfaceServiceAdapter.class);
        this.startService(stub_service);
        Log.w(TAG, "Service started with stub backend");
        mBackend = StructInterfaceServiceAdapter.setService(StructInterfaceServiceFactory.get());
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
    public void onPropBoolChanged(StructBool newValue)
    {
        outputTextViewProp.setText("Property from service: propBool " + newValue);
        Log.w(TAG, "Property from service: propBool " + newValue);
     }
    @Override
    public void onPropIntChanged(StructInt newValue)
    {
        outputTextViewProp.setText("Property from service: propInt " + newValue);
        Log.w(TAG, "Property from service: propInt " + newValue);
     }
    @Override
    public void onPropFloatChanged(StructFloat newValue)
    {
        outputTextViewProp.setText("Property from service: propFloat " + newValue);
        Log.w(TAG, "Property from service: propFloat " + newValue);
     }
    @Override
    public void onPropStringChanged(StructString newValue)
    {
        outputTextViewProp.setText("Property from service: propString " + newValue);
        Log.w(TAG, "Property from service: propString " + newValue);
     }
    @Override
    public void onSigBool(StructBool paramBool)
    {
        String text = "Signal sigBool "+ " " + paramBool;
        outputTextViewSig.setText(text);
        Log.w(TAG, text);
    }
    @Override
    public void onSigInt(StructInt paramInt)
    {
        String text = "Signal sigInt "+ " " + paramInt;
        outputTextViewSig.setText(text);
        Log.w(TAG, text);
    }
    @Override
    public void onSigFloat(StructFloat paramFloat)
    {
        String text = "Signal sigFloat "+ " " + paramFloat;
        outputTextViewSig.setText(text);
        Log.w(TAG, text);
    }
    @Override
    public void onSigString(StructString paramString)
    {
        String text = "Signal sigString "+ " " + paramString;
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
