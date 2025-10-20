package tbRefIfaces.tbRefIfacesserviceexample;

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

import tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfServiceAdapter;
import tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfServiceFactory;
import tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfServiceStarter;

//import message type and parcelabe types

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_impl.SimpleLocalIfService;
import java.util.concurrent.CompletableFuture;



public class TbRefIfacesTestServiceApp extends Activity implements ISimpleLocalIfEventListener
{

    private static final String TAG = "TbRefIfacesTestServiceApp";
    static Intent stub_service = null;


    private ISimpleLocalIf mBackend = null;

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
        Button bIntProperty = new Button(this);
        bIntProperty.setText("Set intProperty");
        bIntProperty.setBackgroundColor(Color.GREEN);

        bIntProperty.setOnClickListener(v -> {
            int newIntProperty = mBackend.getIntProperty();
            //TODO increment
            Log.i(TAG, "SET intProperty" + newIntProperty);
            mBackend.setIntProperty(newIntProperty);
        });
        propertyButtonsLine.addView(bIntProperty);

        layout.addView(propertyButtonsLine);


        LinearLayout methodButtonsLine = new LinearLayout(this);
        methodButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        Button bIntSignal = new Button(this);
        bIntSignal.setText("intSignal");

        bIntSignal.setOnClickListener(v -> {
            Log.w(TAG, "broadcasting singal  intSignal ");
            int param =  1;
            mBackend.fireIntSignal(param);
        });
        bIntSignal.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bIntSignal);

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
        stub_service = new Intent(this, SimpleLocalIfServiceAdapter.class);
        this.startService(stub_service);
        Log.w(TAG, "Service started with stub backend");
        mBackend = SimpleLocalIfServiceAdapter.setService(SimpleLocalIfServiceFactory.get());
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
    public void onIntPropertyChanged(int newValue)
    {
        outputTextViewProp.setText("Property from service: intProperty " + newValue);
        Log.w(TAG, "Property from service: intProperty " + newValue);
     }
    @Override
    public void onIntSignal(int param)
    {
        String text = "Signal intSignal "+ " " + param;
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
