package tbNames.tbNames_client_example;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


//TODO for each interface there coudl be a tab? now only first one is added

import tbNames.tbNames_android_client.NamEsClient;
import tbNames.tbNames_api.EnumWithUnderScores;
import tbNames.tbNames_android_messenger.EnumWithUnderScoresParcelable;
import tbNames.tbNames_api.INamEsEventListener;
import java.util.concurrent.CompletableFuture;



public class TbNamesTestClientApp extends Activity implements INamEsEventListener
{

    private static final String TAG = "TbNamesTestClientApp";

    private NamEsClient mClient = null;

    //TODO ALIGN TO YOUR APP 
    private static String mModuleNameUnreal = "com.example.TestAndroid";
    private static String mModuleNameStub = "tbNames.tbNamesserviceexample.TbNamesTestServiceApp";
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
        Button bSwitch = new Button(this);
        bSwitch.setText("Set Switch");
        bSwitch.setBackgroundColor(Color.GREEN);

        bSwitch.setOnClickListener(v -> {
            boolean newSwitch = mClient.getSwitch();
            
            //TODO increment
            Log.i(TAG, "SET Switch" + newSwitch);
            mClient.setSwitch(newSwitch);
        });
        propertyButtonsLine.addView(bSwitch);
        Button bSomeProperty = new Button(this);
        bSomeProperty.setText("Set SOME_PROPERTY");
        bSomeProperty.setBackgroundColor(Color.GREEN);

        bSomeProperty.setOnClickListener(v -> {
            int newSomeProperty = mClient.getSomeProperty();
            
            //TODO increment
            Log.i(TAG, "SET SOME_PROPERTY" + newSomeProperty);
            mClient.setSomeProperty(newSomeProperty);
        });
        propertyButtonsLine.addView(bSomeProperty);
        Button bSomePoperty2 = new Button(this);
        bSomePoperty2.setText("Set Some_Poperty2");
        bSomePoperty2.setBackgroundColor(Color.GREEN);

        bSomePoperty2.setOnClickListener(v -> {
            int newSomePoperty2 = mClient.getSomePoperty2();
            
            //TODO increment
            Log.i(TAG, "SET Some_Poperty2" + newSomePoperty2);
            mClient.setSomePoperty2(newSomePoperty2);
        });
        propertyButtonsLine.addView(bSomePoperty2);
        Button bEnumProperty = new Button(this);
        bEnumProperty.setText("Set enum_property");
        bEnumProperty.setBackgroundColor(Color.GREEN);

        bEnumProperty.setOnClickListener(v -> {
            EnumWithUnderScores newEnumProperty = mClient.getEnumProperty();
            
            //TODO increment
            Log.i(TAG, "SET enum_property" + newEnumProperty);
            mClient.setEnumProperty(newEnumProperty);
        });
        propertyButtonsLine.addView(bEnumProperty);

        layout.addView(propertyButtonsLine);


        LinearLayout methodButtonsLine = new LinearLayout(this);
        methodButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        Button bSomeFunction = new Button(this);
        bSomeFunction.setText("SOME_FUNCTION");

        bSomeFunction.setOnClickListener(v -> {
            Log.i(TAG, "CALLING METHOD  SOME_FUNCTION ");
            boolean SOME_PARAM =  true;
            CompletableFuture<Void> method_res
                    = mClient.someFunctionAsync(SOME_PARAM).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got SOME_FUNCTION result "+  i);
                        return i;
                    });
        });
        bSomeFunction.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bSomeFunction);
        Button bSomeFunction2 = new Button(this);
        bSomeFunction2.setText("Some_Function2");

        bSomeFunction2.setOnClickListener(v -> {
            Log.i(TAG, "CALLING METHOD  Some_Function2 ");
            boolean Some_Param =  true;
            CompletableFuture<Void> method_res
                    = mClient.someFunction2Async(Some_Param).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got Some_Function2 result "+  i);
                        return i;
                    });
        });
        bSomeFunction2.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bSomeFunction2);

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
            mClient = new NamEsClient(this.getApplicationContext(), "");
            Log.i(TAG, "client created ");
            mClient.addEventListener(this);
        }

        boolean res = mClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bindding result " + res);
        outputTextViewBIND.setText(res+": to "+lastServicePackage);
    }
    @Override
    public void onSwitchChanged(boolean newValue)
    {
        outputTextViewProp.setText("Property from service: Switch " + newValue);
        Log.i(TAG, "Property from service: Switch " + newValue);
     }
    @Override
    public void onSomePropertyChanged(int newValue)
    {
        outputTextViewProp.setText("Property from service: SOME_PROPERTY " + newValue);
        Log.i(TAG, "Property from service: SOME_PROPERTY " + newValue);
     }
    @Override
    public void onSomePoperty2Changed(int newValue)
    {
        outputTextViewProp.setText("Property from service: Some_Poperty2 " + newValue);
        Log.i(TAG, "Property from service: Some_Poperty2 " + newValue);
     }
    @Override
    public void onEnumPropertyChanged(EnumWithUnderScores newValue)
    {
        outputTextViewProp.setText("Property from service: enum_property " + newValue);
        Log.i(TAG, "Property from service: enum_property " + newValue);
     }
    @Override
    public void onSomeSignal(boolean SOME_PARAM)
    {
        String text = "Signal SOME_SIGNAL "+ " " + SOME_PARAM;
        outputTextViewSig.setText(text);
        Log.i(TAG, text);
    }
    @Override
    public void onSomeSignal2(boolean Some_Param)
    {
        String text = "Signal Some_Signal2 "+ " " + Some_Param;
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