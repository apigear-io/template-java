package counter.counterserviceexample;

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

import counter.counter_android_service.CounterServiceAdapter;
import counter.counter_android_service.CounterServiceProvider;
import counter.counter_android_service.CounterServiceStarter;

//import message type and parcelabe types

import counter.counter_api.ICounterEventListener;
import counter.counter_api.ICounter;
import counter.counter_impl.CounterService;
import java.util.concurrent.CompletableFuture;



public class CounterTestServiceApp extends Activity implements ICounterEventListener
{

    private static final String TAG = "CounterTestServiceApp";
    static Intent stub_service = null;


    private ICounter mBackend = null;

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
        Button bVector = new Button(this);
        bVector.setText("Set vector");
        bVector.setBackgroundColor(Color.GREEN);

        bVector.setOnClickListener(v -> {
            customTypes.customTypes_api.Vector3D newVector = mBackend.getVector();
            //TODO increment
            Log.i(TAG, "SET vector" + newVector);
            mBackend.setVector(newVector);
        });
        propertyButtonsLine.addView(bVector);
        Button bExternVector = new Button(this);
        bExternVector.setText("Set extern_vector");
        bExternVector.setBackgroundColor(Color.GREEN);

        bExternVector.setOnClickListener(v -> {
            org.apache.commons.math3.geometry.euclidean.threed.Vector3D newExternVector = mBackend.getExternVector();
            //TODO increment
            Log.i(TAG, "SET extern_vector" + newExternVector);
            mBackend.setExternVector(newExternVector);
        });
        propertyButtonsLine.addView(bExternVector);
        Button bVectorArray = new Button(this);
        bVectorArray.setText("Set vectorArray");
        bVectorArray.setBackgroundColor(Color.GREEN);

        bVectorArray.setOnClickListener(v -> {
            customTypes.customTypes_api.Vector3D[] newVectorArray = mBackend.getVectorArray();
            //TODO increment
            Log.i(TAG, "SET vectorArray" + newVectorArray);
            mBackend.setVectorArray(newVectorArray);
        });
        propertyButtonsLine.addView(bVectorArray);
        Button bExternVectorArray = new Button(this);
        bExternVectorArray.setText("Set extern_vectorArray");
        bExternVectorArray.setBackgroundColor(Color.GREEN);

        bExternVectorArray.setOnClickListener(v -> {
            org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] newExternVectorArray = mBackend.getExternVectorArray();
            //TODO increment
            Log.i(TAG, "SET extern_vectorArray" + newExternVectorArray);
            mBackend.setExternVectorArray(newExternVectorArray);
        });
        propertyButtonsLine.addView(bExternVectorArray);

        layout.addView(propertyButtonsLine);


        LinearLayout methodButtonsLine = new LinearLayout(this);
        methodButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        Button bValueChanged = new Button(this);
        bValueChanged.setText("valueChanged");

        bValueChanged.setOnClickListener(v -> {
            Log.i(TAG, "broadcasting singal  valueChanged ");
            customTypes.customTypes_api.Vector3D vector =  new customTypes.customTypes_api.Vector3D();
            org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector =  new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
            customTypes.customTypes_api.Vector3D[] vectorArray =  new customTypes.customTypes_api.Vector3D();
            org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray =  new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
            mBackend.fireValueChanged(vector, extern_vector, vectorArray, extern_vectorArray);
        });
        bValueChanged.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bValueChanged);

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
        stub_service = new Intent(this, CounterServiceAdapter.class);
        this.startService(stub_service);
        Log.i(TAG, "Service started with stub backend");
        mBackend = CounterServiceAdapter.setService(CounterServiceProvider.get());
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
    public void onVectorChanged(customTypes.customTypes_api.Vector3D newValue)
    {
        outputTextViewProp.setText("Property from service: vector " + newValue);
        Log.i(TAG, "Property from service: vector " + newValue);
     }
    @Override
    public void onExternVectorChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D newValue)
    {
        outputTextViewProp.setText("Property from service: extern_vector " + newValue);
        Log.i(TAG, "Property from service: extern_vector " + newValue);
     }
    @Override
    public void onVectorArrayChanged(customTypes.customTypes_api.Vector3D[] newValue)
    {
        outputTextViewProp.setText("Property from service: vectorArray " + newValue);
        Log.i(TAG, "Property from service: vectorArray " + newValue);
     }
    @Override
    public void onExternVectorArrayChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] newValue)
    {
        outputTextViewProp.setText("Property from service: extern_vectorArray " + newValue);
        Log.i(TAG, "Property from service: extern_vectorArray " + newValue);
     }
    @Override
    public void onValueChanged(customTypes.customTypes_api.Vector3D vector, org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector, customTypes.customTypes_api.Vector3D[] vectorArray, org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray)
    {
        String text = "Signal valueChanged "+ " " + vector+ " " + extern_vector+ " " + vectorArray+ " " + extern_vectorArray;
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
