package testbed1.testbed1_client_example;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


//TODO for each interface there coudl be a tab? now only first one is added

import testbed1.testbed1_android_client.StructInterfaceClient;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_android_messenger.StructBoolParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;
import testbed1.testbed1_api.IStructInterfaceEventListener;
import java.util.concurrent.CompletableFuture;



public class Testbed1TestClientApp extends Activity implements IStructInterfaceEventListener
{

    private static final String TAG = "Testbed1TestClientApp";

    private StructInterfaceClient mClient = null;

    //TODO ALIGN TO YOUR APP 
    private static String mModuleNameUnreal = "com.example.TestAndroid";
    private static String mModuleNameStub = "testbed1.testbed1serviceexample.Testbed1TestServiceApp";
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
            StructBool newPropBool = new StructBool(mClient.getPropBool(););
            //TODO increment
            Log.i(TAG, "SET propBool" + newPropBool);
            mClient.setPropBool(newPropBool);
        });
        propertyButtonsLine.addView(bPropBool);
        Button bPropInt = new Button(this);
        bPropInt.setText("Set propInt");
        bPropInt.setBackgroundColor(Color.GREEN);

        bPropInt.setOnClickListener(v -> {
            StructInt newPropInt = new StructInt(mClient.getPropInt(););
            //TODO increment
            Log.i(TAG, "SET propInt" + newPropInt);
            mClient.setPropInt(newPropInt);
        });
        propertyButtonsLine.addView(bPropInt);
        Button bPropFloat = new Button(this);
        bPropFloat.setText("Set propFloat");
        bPropFloat.setBackgroundColor(Color.GREEN);

        bPropFloat.setOnClickListener(v -> {
            StructFloat newPropFloat = new StructFloat(mClient.getPropFloat(););
            //TODO increment
            Log.i(TAG, "SET propFloat" + newPropFloat);
            mClient.setPropFloat(newPropFloat);
        });
        propertyButtonsLine.addView(bPropFloat);
        Button bPropString = new Button(this);
        bPropString.setText("Set propString");
        bPropString.setBackgroundColor(Color.GREEN);

        bPropString.setOnClickListener(v -> {
            StructString newPropString = new StructString(mClient.getPropString(););
            //TODO increment
            Log.i(TAG, "SET propString" + newPropString);
            mClient.setPropString(newPropString);
        });
        propertyButtonsLine.addView(bPropString);

        layout.addView(propertyButtonsLine);


        LinearLayout methodButtonsLine = new LinearLayout(this);
        methodButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        Button bFuncBool = new Button(this);
        bFuncBool.setText("funcBool");

        bFuncBool.setOnClickListener(v -> {
            Log.i(TAG, "CALLING METHOD  funcBool ");
            StructBool paramBool =  new StructBool();
            CompletableFuture<StructBool> method_res
                    = mClient.funcBoolAsync(paramBool).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got funcBool result "+  i);
                        return i;
                    });
        });
        bFuncBool.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bFuncBool);
        Button bFuncInt = new Button(this);
        bFuncInt.setText("funcInt");

        bFuncInt.setOnClickListener(v -> {
            Log.i(TAG, "CALLING METHOD  funcInt ");
            StructInt paramInt =  new StructInt();
            CompletableFuture<StructInt> method_res
                    = mClient.funcIntAsync(paramInt).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got funcInt result "+  i);
                        return i;
                    });
        });
        bFuncInt.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bFuncInt);
        Button bFuncFloat = new Button(this);
        bFuncFloat.setText("funcFloat");

        bFuncFloat.setOnClickListener(v -> {
            Log.i(TAG, "CALLING METHOD  funcFloat ");
            StructFloat paramFloat =  new StructFloat();
            CompletableFuture<StructFloat> method_res
                    = mClient.funcFloatAsync(paramFloat).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got funcFloat result "+  i);
                        return i;
                    });
        });
        bFuncFloat.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bFuncFloat);
        Button bFuncString = new Button(this);
        bFuncString.setText("funcString");

        bFuncString.setOnClickListener(v -> {
            Log.i(TAG, "CALLING METHOD  funcString ");
            StructString paramString =  new StructString();
            CompletableFuture<StructString> method_res
                    = mClient.funcStringAsync(paramString).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got funcString result "+  i);
                        return i;
                    });
        });
        bFuncString.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bFuncString);

        layout.addView(methodButtonsLine);

        Button testButton5 = new Button(this);
        testButton5.setText("Bind to Unreal");
        testButton5.setOnClickListener(v -> {
            initServiceConnection(mModuleNameUnreal);
        });
        testButton5.setBackgroundColor(Color.GREEN);
        layout.addView(testButton5);

        Button testButton6 = new Button(this);
        testButton6.setText("Bind to Stub app");
        testButton6.setOnClickListener(v -> {
            initServiceConnection(mModuleNameStub);
        });
        testButton6.setBackgroundColor(Color.GREEN);
        layout.addView(testButton6);

        Button testButton4 = new Button(this);
        testButton4.setText("Unbind service");
        testButton4.setOnClickListener(v -> {
            if (mClient != null) {
                mClient.unbindFromService();
            }
        });
        testButton4.setBackgroundColor(Color.GREEN);
        layout.addView(testButton4);

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


        outputTextViewBIND = new TextView(this);
        outputTextViewBIND.setText("NOT BINDED");
        outputTextViewBIND.setTextSize(16);
        outputTextViewBIND.setPadding(16, 16, 16, 16); // optional for spacing
        layout.addView(outputTextViewBIND);

        setContentView(layout);
    }

    @Override
    protected void onStart()
    {
        Log.v(TAG, "My app: onStart, binding if not bound");
        super.onStart();

        if (mClient != null && !mClient.isBoundToService())
        {
            boolean res = mClient.bindToService(lastServicePackage);
            Log.v(TAG, "My app: bind " + res);
            outputTextViewBIND.setText(res+": to "+lastServicePackage);
            //unbindFromService
        }
    }

    @Override
    protected void onStop()
    {
        Log.v(TAG, "My app: onStop, NOT UNBINDING");
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        Log.v(TAG, "My app: onDestroy, unbinding");
        if (mClient != null) {
            mClient.unbindFromService();
        }
        mClient.removeEventListener(this);
        super.onDestroy();
    }

    private void initServiceConnection( String servicePackage)
    {
        lastServicePackage = servicePackage;
        Log.i(TAG, "init service connection the client ");

        if (mClient == null)
        {
            mClient = new StructInterfaceClient(this.getApplicationContext(), "");
            Log.i(TAG, "client created ");
            mClient.addEventListener(this);
        }

        boolean res = mClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bindding result " + res);
        outputTextViewBIND.setText(res+": to "+lastServicePackage);
    }
    @Override
    public void onPropBoolChanged(StructBool newValue)
    {
        outputTextViewProp.setText("Property from service: propBool " + newValue);
        Log.i(TAG, "Property from service: propBool " + newValue);
     }
    @Override
    public void onPropIntChanged(StructInt newValue)
    {
        outputTextViewProp.setText("Property from service: propInt " + newValue);
        Log.i(TAG, "Property from service: propInt " + newValue);
     }
    @Override
    public void onPropFloatChanged(StructFloat newValue)
    {
        outputTextViewProp.setText("Property from service: propFloat " + newValue);
        Log.i(TAG, "Property from service: propFloat " + newValue);
     }
    @Override
    public void onPropStringChanged(StructString newValue)
    {
        outputTextViewProp.setText("Property from service: propString " + newValue);
        Log.i(TAG, "Property from service: propString " + newValue);
     }
    @Override
    public void onSigBool(StructBool paramBool)
    {
        String text = "Signal sigBool "+ " " + paramBool;
        outputTextViewSig.setText(text);
        Log.i(TAG, text);
    }
    @Override
    public void onSigInt(StructInt paramInt)
    {
        String text = "Signal sigInt "+ " " + paramInt;
        outputTextViewSig.setText(text);
        Log.i(TAG, text);
    }
    @Override
    public void onSigFloat(StructFloat paramFloat)
    {
        String text = "Signal sigFloat "+ " " + paramFloat;
        outputTextViewSig.setText(text);
        Log.i(TAG, text);
    }
    @Override
    public void onSigString(StructString paramString)
    {
        String text = "Signal sigString "+ " " + paramString;
        outputTextViewSig.setText(text);
        Log.i(TAG, text);
    }
    @Override
    public void on_readyStatusChanged(boolean isReady)
    { 
         if (isReady)
         {
             Log.i(TAG, "Connected to service ");
         }
         else
         {
             Log.i(TAG, "Disconnected from service ");
         }
    }

}