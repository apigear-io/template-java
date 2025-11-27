package tbEnum.tbEnum_client_example;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


//TODO for each interface there coudl be a tab? now only first one is added

import tbEnum.tbEnum_android_client.EnumInterfaceClient;
import tbEnum.tbEnum_api.Enum0;
import tbEnum.tbEnum_android_messenger.Enum0Parcelable;
import tbEnum.tbEnum_api.Enum1;
import tbEnum.tbEnum_android_messenger.Enum1Parcelable;
import tbEnum.tbEnum_api.Enum2;
import tbEnum.tbEnum_android_messenger.Enum2Parcelable;
import tbEnum.tbEnum_api.Enum3;
import tbEnum.tbEnum_android_messenger.Enum3Parcelable;
import tbEnum.tbEnum_api.IEnumInterfaceEventListener;
import java.util.concurrent.CompletableFuture;



public class TbEnumTestClientApp extends Activity implements IEnumInterfaceEventListener
{

    private static final String TAG = "TbEnumTestClientApp";

    private EnumInterfaceClient mClient = null;

    //TODO ALIGN TO YOUR APP 
    private static String mModuleNameUnreal = "com.example.TestAndroid";
    private static String mModuleNameStub = "tbEnum.tbEnumserviceexample.TbEnumTestServiceApp";
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
        Button bProp0 = new Button(this);
        bProp0.setText("Set prop0");
        bProp0.setBackgroundColor(Color.GREEN);

        bProp0.setOnClickListener(v -> {
            Enum0 newProp0 = mClient.getProp0();
            
            //TODO increment
            Log.i(TAG, "SET prop0" + newProp0);
            mClient.setProp0(newProp0);
        });
        propertyButtonsLine.addView(bProp0);
        Button bProp1 = new Button(this);
        bProp1.setText("Set prop1");
        bProp1.setBackgroundColor(Color.GREEN);

        bProp1.setOnClickListener(v -> {
            Enum1 newProp1 = mClient.getProp1();
            
            //TODO increment
            Log.i(TAG, "SET prop1" + newProp1);
            mClient.setProp1(newProp1);
        });
        propertyButtonsLine.addView(bProp1);
        Button bProp2 = new Button(this);
        bProp2.setText("Set prop2");
        bProp2.setBackgroundColor(Color.GREEN);

        bProp2.setOnClickListener(v -> {
            Enum2 newProp2 = mClient.getProp2();
            
            //TODO increment
            Log.i(TAG, "SET prop2" + newProp2);
            mClient.setProp2(newProp2);
        });
        propertyButtonsLine.addView(bProp2);
        Button bProp3 = new Button(this);
        bProp3.setText("Set prop3");
        bProp3.setBackgroundColor(Color.GREEN);

        bProp3.setOnClickListener(v -> {
            Enum3 newProp3 = mClient.getProp3();
            
            //TODO increment
            Log.i(TAG, "SET prop3" + newProp3);
            mClient.setProp3(newProp3);
        });
        propertyButtonsLine.addView(bProp3);

        layout.addView(propertyButtonsLine);


        LinearLayout methodButtonsLine = new LinearLayout(this);
        methodButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        Button bFunc0 = new Button(this);
        bFunc0.setText("func0");

        bFunc0.setOnClickListener(v -> {
            Log.i(TAG, "CALLING METHOD  func0 ");
            Enum0 param0 =  Enum0.Value1;
            CompletableFuture<Enum0> method_res
                    = mClient.func0Async(param0).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got func0 result "+  i);
                        return i;
                    });
        });
        bFunc0.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bFunc0);
        Button bFunc1 = new Button(this);
        bFunc1.setText("func1");

        bFunc1.setOnClickListener(v -> {
            Log.i(TAG, "CALLING METHOD  func1 ");
            Enum1 param1 =  Enum1.Value2;
            CompletableFuture<Enum1> method_res
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
            Log.i(TAG, "CALLING METHOD  func2 ");
            Enum2 param2 =  Enum2.Value1;
            CompletableFuture<Enum2> method_res
                    = mClient.func2Async(param2).thenApply(
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
            Log.i(TAG, "CALLING METHOD  func3 ");
            Enum3 param3 =  Enum3.Value2;
            CompletableFuture<Enum3> method_res
                    = mClient.func3Async(param3).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got func3 result "+  i);
                        return i;
                    });
        });
        bFunc3.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bFunc3);

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
            mClient = new EnumInterfaceClient(this.getApplicationContext(), "");
            Log.i(TAG, "client created ");
            mClient.addEventListener(this);
        }

        boolean res = mClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bindding result " + res);
        outputTextViewBIND.setText(res+": to "+lastServicePackage);
    }
    @Override
    public void onProp0Changed(Enum0 newValue)
    {
        outputTextViewProp.setText("Property from service: prop0 " + newValue);
        Log.i(TAG, "Property from service: prop0 " + newValue);
     }
    @Override
    public void onProp1Changed(Enum1 newValue)
    {
        outputTextViewProp.setText("Property from service: prop1 " + newValue);
        Log.i(TAG, "Property from service: prop1 " + newValue);
     }
    @Override
    public void onProp2Changed(Enum2 newValue)
    {
        outputTextViewProp.setText("Property from service: prop2 " + newValue);
        Log.i(TAG, "Property from service: prop2 " + newValue);
     }
    @Override
    public void onProp3Changed(Enum3 newValue)
    {
        outputTextViewProp.setText("Property from service: prop3 " + newValue);
        Log.i(TAG, "Property from service: prop3 " + newValue);
     }
    @Override
    public void onSig0(Enum0 param0)
    {
        String text = "Signal sig0 "+ " " + param0;
        outputTextViewSig.setText(text);
        Log.i(TAG, text);
    }
    @Override
    public void onSig1(Enum1 param1)
    {
        String text = "Signal sig1 "+ " " + param1;
        outputTextViewSig.setText(text);
        Log.i(TAG, text);
    }
    @Override
    public void onSig2(Enum2 param2)
    {
        String text = "Signal sig2 "+ " " + param2;
        outputTextViewSig.setText(text);
        Log.i(TAG, text);
    }
    @Override
    public void onSig3(Enum3 param3)
    {
        String text = "Signal sig3 "+ " " + param3;
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