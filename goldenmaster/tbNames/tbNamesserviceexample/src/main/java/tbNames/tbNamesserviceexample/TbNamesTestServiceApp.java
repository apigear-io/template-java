package tbNames.tbNamesserviceexample;

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

import tbNames.tbNames_android_service.NamEsServiceAdapter;
import tbNames.tbNames_android_service.NamEsServiceProvider;
import tbNames.tbNames_android_service.NamEsServiceStarter;

//import message type and parcelabe types
import tbNames.tbNames_api.EnumWithUnderScores;
import tbNames.tbNames_android_messenger.EnumWithUnderScoresParcelable;

import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_impl.NamEsService;
import java.util.concurrent.CompletableFuture;



public class TbNamesTestServiceApp extends Activity implements INamEsEventListener
{

    private static final String TAG = "TbNamesTestServiceApp";
    static Intent stub_service = null;


    private INamEs mBackend = null;

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
            boolean newSwitch = mBackend.getSwitch();
            //TODO increment
            Log.i(TAG, "SET Switch" + newSwitch);
            mBackend.setSwitch(newSwitch);
        });
        propertyButtonsLine.addView(bSwitch);
        Button bSomeProperty = new Button(this);
        bSomeProperty.setText("Set SOME_PROPERTY");
        bSomeProperty.setBackgroundColor(Color.GREEN);

        bSomeProperty.setOnClickListener(v -> {
            int newSomeProperty = mBackend.getSomeProperty();
            //TODO increment
            Log.i(TAG, "SET SOME_PROPERTY" + newSomeProperty);
            mBackend.setSomeProperty(newSomeProperty);
        });
        propertyButtonsLine.addView(bSomeProperty);
        Button bSomePoperty2 = new Button(this);
        bSomePoperty2.setText("Set Some_Poperty2");
        bSomePoperty2.setBackgroundColor(Color.GREEN);

        bSomePoperty2.setOnClickListener(v -> {
            int newSomePoperty2 = mBackend.getSomePoperty2();
            //TODO increment
            Log.i(TAG, "SET Some_Poperty2" + newSomePoperty2);
            mBackend.setSomePoperty2(newSomePoperty2);
        });
        propertyButtonsLine.addView(bSomePoperty2);
        Button bEnumProperty = new Button(this);
        bEnumProperty.setText("Set enum_property");
        bEnumProperty.setBackgroundColor(Color.GREEN);

        bEnumProperty.setOnClickListener(v -> {
            EnumWithUnderScores newEnumProperty = mBackend.getEnumProperty();
            //TODO increment
            Log.i(TAG, "SET enum_property" + newEnumProperty);
            mBackend.setEnumProperty(newEnumProperty);
        });
        propertyButtonsLine.addView(bEnumProperty);

        layout.addView(propertyButtonsLine);


        LinearLayout methodButtonsLine = new LinearLayout(this);
        methodButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        Button bSomeSignal = new Button(this);
        bSomeSignal.setText("SOME_SIGNAL");

        bSomeSignal.setOnClickListener(v -> {
            Log.i(TAG, "broadcasting singal  SOME_SIGNAL ");
            boolean SOME_PARAM =  true;
            mBackend.fireSomeSignal(SOME_PARAM);
        });
        bSomeSignal.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bSomeSignal);
        Button bSomeSignal2 = new Button(this);
        bSomeSignal2.setText("Some_Signal2");

        bSomeSignal2.setOnClickListener(v -> {
            Log.i(TAG, "broadcasting singal  Some_Signal2 ");
            boolean Some_Param =  true;
            mBackend.fireSomeSignal2(Some_Param);
        });
        bSomeSignal2.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bSomeSignal2);

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
        stub_service = new Intent(this, NamEsServiceAdapter.class);
        this.startService(stub_service);
        Log.i(TAG, "Service started with stub backend");
        mBackend = NamEsServiceAdapter.setService(NamEsServiceProvider.get());
        mBackend.addEventListener(this);
    }

    public void stopMyService() {
        if (mBackend != null) {
            mBackend.removeEventListener(this);
        }
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
