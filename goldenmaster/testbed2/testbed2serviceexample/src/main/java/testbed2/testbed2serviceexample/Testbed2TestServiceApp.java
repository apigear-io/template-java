package testbed2.testbed2serviceexample;

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

import testbed2.testbed2_android_service.ManyParamInterfaceServiceAdapter;
import testbed2.testbed2_android_service.ManyParamInterfaceServiceProvider;
import testbed2.testbed2_android_service.ManyParamInterfaceServiceStarter;

//import message type and parcelabe types

import testbed2.testbed2_api.IManyParamInterfaceEventListener;
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_impl.ManyParamInterfaceService;
import java.util.concurrent.CompletableFuture;



public class Testbed2TestServiceApp extends Activity implements IManyParamInterfaceEventListener
{

    private static final String TAG = "Testbed2TestServiceApp";
    static Intent stub_service = null;


    private IManyParamInterface mBackend = null;

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
            int newProp1 = mBackend.getProp1();
            //TODO increment
            Log.i(TAG, "SET prop1" + newProp1);
            mBackend.setProp1(newProp1);
        });
        propertyButtonsLine.addView(bProp1);
        Button bProp2 = new Button(this);
        bProp2.setText("Set prop2");
        bProp2.setBackgroundColor(Color.GREEN);

        bProp2.setOnClickListener(v -> {
            int newProp2 = mBackend.getProp2();
            //TODO increment
            Log.i(TAG, "SET prop2" + newProp2);
            mBackend.setProp2(newProp2);
        });
        propertyButtonsLine.addView(bProp2);
        Button bProp3 = new Button(this);
        bProp3.setText("Set prop3");
        bProp3.setBackgroundColor(Color.GREEN);

        bProp3.setOnClickListener(v -> {
            int newProp3 = mBackend.getProp3();
            //TODO increment
            Log.i(TAG, "SET prop3" + newProp3);
            mBackend.setProp3(newProp3);
        });
        propertyButtonsLine.addView(bProp3);
        Button bProp4 = new Button(this);
        bProp4.setText("Set prop4");
        bProp4.setBackgroundColor(Color.GREEN);

        bProp4.setOnClickListener(v -> {
            int newProp4 = mBackend.getProp4();
            //TODO increment
            Log.i(TAG, "SET prop4" + newProp4);
            mBackend.setProp4(newProp4);
        });
        propertyButtonsLine.addView(bProp4);

        layout.addView(propertyButtonsLine);


        LinearLayout methodButtonsLine = new LinearLayout(this);
        methodButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        Button bSig1 = new Button(this);
        bSig1.setText("sig1");

        bSig1.setOnClickListener(v -> {
            Log.i(TAG, "broadcasting singal  sig1 ");
            int param1 =  1;
            mBackend.fireSig1(param1);
        });
        bSig1.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bSig1);
        Button bSig2 = new Button(this);
        bSig2.setText("sig2");

        bSig2.setOnClickListener(v -> {
            Log.i(TAG, "broadcasting singal  sig2 ");
            int param1 =  1;
            int param2 =  1;
            mBackend.fireSig2(param1, param2);
        });
        bSig2.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bSig2);
        Button bSig3 = new Button(this);
        bSig3.setText("sig3");

        bSig3.setOnClickListener(v -> {
            Log.i(TAG, "broadcasting singal  sig3 ");
            int param1 =  1;
            int param2 =  1;
            int param3 =  1;
            mBackend.fireSig3(param1, param2, param3);
        });
        bSig3.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bSig3);
        Button bSig4 = new Button(this);
        bSig4.setText("sig4");

        bSig4.setOnClickListener(v -> {
            Log.i(TAG, "broadcasting singal  sig4 ");
            int param1 =  1;
            int param2 =  1;
            int param3 =  1;
            int param4 =  1;
            mBackend.fireSig4(param1, param2, param3, param4);
        });
        bSig4.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bSig4);

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
        stub_service = new Intent(this, ManyParamInterfaceServiceAdapter.class);
        this.startService(stub_service);
        Log.i(TAG, "Service started with stub backend");
        mBackend = ManyParamInterfaceServiceAdapter.setService(ManyParamInterfaceServiceProvider.get());
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
    public void onProp1Changed(int newValue)
    {
        outputTextViewProp.setText("Property from service: prop1 " + newValue);
        Log.i(TAG, "Property from service: prop1 " + newValue);
     }
    @Override
    public void onProp2Changed(int newValue)
    {
        outputTextViewProp.setText("Property from service: prop2 " + newValue);
        Log.i(TAG, "Property from service: prop2 " + newValue);
     }
    @Override
    public void onProp3Changed(int newValue)
    {
        outputTextViewProp.setText("Property from service: prop3 " + newValue);
        Log.i(TAG, "Property from service: prop3 " + newValue);
     }
    @Override
    public void onProp4Changed(int newValue)
    {
        outputTextViewProp.setText("Property from service: prop4 " + newValue);
        Log.i(TAG, "Property from service: prop4 " + newValue);
     }
    @Override
    public void onSig1(int param1)
    {
        String text = "Signal sig1 "+ " " + param1;
        outputTextViewSig.setText(text);
        Log.i(TAG, text);
    }
    @Override
    public void onSig2(int param1, int param2)
    {
        String text = "Signal sig2 "+ " " + param1+ " " + param2;
        outputTextViewSig.setText(text);
        Log.i(TAG, text);
    }
    @Override
    public void onSig3(int param1, int param2, int param3)
    {
        String text = "Signal sig3 "+ " " + param1+ " " + param2+ " " + param3;
        outputTextViewSig.setText(text);
        Log.i(TAG, text);
    }
    @Override
    public void onSig4(int param1, int param2, int param3, int param4)
    {
        String text = "Signal sig4 "+ " " + param1+ " " + param2+ " " + param3+ " " + param4;
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
