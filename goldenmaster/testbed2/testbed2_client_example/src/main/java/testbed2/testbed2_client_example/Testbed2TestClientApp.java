package testbed2.testbed2_client_example;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


//TODO for each interface there coudl be a tab? now only first one is added

import testbed2.testbed2_android_client.ManyParamInterfaceClient;

//import message type and parcelabe types
import testbed2.testbed2_api.Struct1;
import testbed2.testbed2_android_messenger.Struct1Parcelable;
import testbed2.testbed2_api.Struct2;
import testbed2.testbed2_android_messenger.Struct2Parcelable;
import testbed2.testbed2_api.Struct3;
import testbed2.testbed2_android_messenger.Struct3Parcelable;
import testbed2.testbed2_api.Struct4;
import testbed2.testbed2_android_messenger.Struct4Parcelable;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_android_messenger.NestedStruct1Parcelable;
import testbed2.testbed2_api.NestedStruct2;
import testbed2.testbed2_android_messenger.NestedStruct2Parcelable;
import testbed2.testbed2_api.NestedStruct3;
import testbed2.testbed2_android_messenger.NestedStruct3Parcelable;
import testbed2.testbed2_api.Enum1;
import testbed2.testbed2_android_messenger.Enum1Parcelable;
import testbed2.testbed2_api.Enum2;
import testbed2.testbed2_android_messenger.Enum2Parcelable;
import testbed2.testbed2_api.Enum3;
import testbed2.testbed2_android_messenger.Enum3Parcelable;

import testbed2.testbed2_api.IManyParamInterfaceEventListener;
import java.util.concurrent.CompletableFuture;



public class Testbed2TestClientApp extends Activity implements IManyParamInterfaceEventListener
{

    private static final String TAG = "Testbed2TestClientApp";

    private ManyParamInterfaceClient mClient = null;

    //TODO ALIGN TO YOUR APP 
    private static String mModuleNameUnreal = "com.example.TestAndroid";
    private static String mModuleNameStub = "testbed2.testbed2serviceexample.Testbed2TestServiceApp";
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
            int newProp1 = mClient.getProp1();
            
            //TODO increment
            Log.i(TAG, "SET prop1" + newProp1);
            mClient.setProp1(newProp1);
        });
        propertyButtonsLine.addView(bProp1);
        Button bProp2 = new Button(this);
        bProp2.setText("Set prop2");
        bProp2.setBackgroundColor(Color.GREEN);

        bProp2.setOnClickListener(v -> {
            int newProp2 = mClient.getProp2();
            
            //TODO increment
            Log.i(TAG, "SET prop2" + newProp2);
            mClient.setProp2(newProp2);
        });
        propertyButtonsLine.addView(bProp2);
        Button bProp3 = new Button(this);
        bProp3.setText("Set prop3");
        bProp3.setBackgroundColor(Color.GREEN);

        bProp3.setOnClickListener(v -> {
            int newProp3 = mClient.getProp3();
            
            //TODO increment
            Log.i(TAG, "SET prop3" + newProp3);
            mClient.setProp3(newProp3);
        });
        propertyButtonsLine.addView(bProp3);
        Button bProp4 = new Button(this);
        bProp4.setText("Set prop4");
        bProp4.setBackgroundColor(Color.GREEN);

        bProp4.setOnClickListener(v -> {
            int newProp4 = mClient.getProp4();
            
            //TODO increment
            Log.i(TAG, "SET prop4" + newProp4);
            mClient.setProp4(newProp4);
        });
        propertyButtonsLine.addView(bProp4);

        layout.addView(propertyButtonsLine);


        LinearLayout methodButtonsLine = new LinearLayout(this);
        methodButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        Button bFunc1 = new Button(this);
        bFunc1.setText("func1");

        bFunc1.setOnClickListener(v -> {
            Log.w(TAG, "CALLING METHOD  func1 ");
            int param1 =  1;
            CompletableFuture<Integer> method_res
                    = mClient.func1Async(param1).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got func1 result "+  i);
                        return i;
                    });
        });
        bFunc1.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bFunc1);
        Button bFunc2 = new Button(this);
        bFunc2.setText("func2");

        bFunc2.setOnClickListener(v -> {
            Log.w(TAG, "CALLING METHOD  func2 ");
            int param1 =  1;
            int param2 =  1;
            CompletableFuture<Integer> method_res
                    = mClient.func2Async(param1, param2).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got func2 result "+  i);
                        return i;
                    });
        });
        bFunc2.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bFunc2);
        Button bFunc3 = new Button(this);
        bFunc3.setText("func3");

        bFunc3.setOnClickListener(v -> {
            Log.w(TAG, "CALLING METHOD  func3 ");
            int param1 =  1;
            int param2 =  1;
            int param3 =  1;
            CompletableFuture<Integer> method_res
                    = mClient.func3Async(param1, param2, param3).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got func3 result "+  i);
                        return i;
                    });
        });
        bFunc3.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bFunc3);
        Button bFunc4 = new Button(this);
        bFunc4.setText("func4");

        bFunc4.setOnClickListener(v -> {
            Log.w(TAG, "CALLING METHOD  func4 ");
            int param1 =  1;
            int param2 =  1;
            int param3 =  1;
            int param4 =  1;
            CompletableFuture<Integer> method_res
                    = mClient.func4Async(param1, param2, param3, param4).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got func4 result "+  i);
                        return i;
                    });
        });
        bFunc4.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bFunc4);

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
        Log.w(TAG, "init service connection the client ");

        if (mClient == null)
        {
            mClient = new ManyParamInterfaceClient(this.getApplicationContext(), "");
            Log.w(TAG, "client created ");
            mClient.addEventListener(this);
        }

        boolean res = mClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bindding result " + res);
        outputTextViewBIND.setText(res+": to "+lastServicePackage);
    }
    @Override
    public void onProp1Changed(int newValue)
    {
        outputTextViewProp.setText("Property from service: prop1 " + newValue);
        Log.w(TAG, "Property from service: prop1 " + newValue);
     }
    @Override
    public void onProp2Changed(int newValue)
    {
        outputTextViewProp.setText("Property from service: prop2 " + newValue);
        Log.w(TAG, "Property from service: prop2 " + newValue);
     }
    @Override
    public void onProp3Changed(int newValue)
    {
        outputTextViewProp.setText("Property from service: prop3 " + newValue);
        Log.w(TAG, "Property from service: prop3 " + newValue);
     }
    @Override
    public void onProp4Changed(int newValue)
    {
        outputTextViewProp.setText("Property from service: prop4 " + newValue);
        Log.w(TAG, "Property from service: prop4 " + newValue);
     }
    @Override
    public void onSig1(int param1)
    {
        String text = "Signal sig1 "+ " " + param1;
        outputTextViewSig.setText(text);
        Log.w(TAG, text);
    }
    @Override
    public void onSig2(int param1, int param2)
    {
        String text = "Signal sig2 "+ " " + param1+ " " + param2;
        outputTextViewSig.setText(text);
        Log.w(TAG, text);
    }
    @Override
    public void onSig3(int param1, int param2, int param3)
    {
        String text = "Signal sig3 "+ " " + param1+ " " + param2+ " " + param3;
        outputTextViewSig.setText(text);
        Log.w(TAG, text);
    }
    @Override
    public void onSig4(int param1, int param2, int param3, int param4)
    {
        String text = "Signal sig4 "+ " " + param1+ " " + param2+ " " + param3+ " " + param4;
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
