package counter.counter_client_example;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


//TODO for each interface there coudl be a tab? now only first one is added

import counter.counter_android_client.CounterClient;
import counter.counter_api.ICounterEventListener;
import java.util.concurrent.CompletableFuture;



public class CounterTestClientApp extends Activity implements ICounterEventListener
{

    private static final String TAG = "CounterTestClientApp";

    private CounterClient mClient = null;

    //TODO ALIGN TO YOUR APP 
    private static String mModuleNameUnreal = "com.example.TestAndroid";
    private static String mModuleNameStub = "counter.counterserviceexample.CounterTestServiceApp";
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
            customTypes.customTypes_api.Vector3D newVector = new customTypes.customTypes_api.Vector3D(mClient.getVector());
            //TODO increment
            Log.i(TAG, "SET vector" + newVector);
            mClient.setVector(newVector);
        });
        propertyButtonsLine.addView(bVector);
        Button bExternVector = new Button(this);
        bExternVector.setText("Set extern_vector");
        bExternVector.setBackgroundColor(Color.GREEN);

        bExternVector.setOnClickListener(v -> {
            org.apache.commons.math3.geometry.euclidean.threed.Vector3D newExternVector = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(mClient.getExternVector());
            //TODO increment
            Log.i(TAG, "SET extern_vector" + newExternVector);
            mClient.setExternVector(newExternVector);
        });
        propertyButtonsLine.addView(bExternVector);
        Button bVectorArray = new Button(this);
        bVectorArray.setText("Set vectorArray");
        bVectorArray.setBackgroundColor(Color.GREEN);

        bVectorArray.setOnClickListener(v -> {
            customTypes.customTypes_api.Vector3D[] newVectorArray = new customTypes.customTypes_api.Vector3D[](mClient.getVectorArray());
            //TODO increment
            Log.i(TAG, "SET vectorArray" + newVectorArray);
            mClient.setVectorArray(newVectorArray);
        });
        propertyButtonsLine.addView(bVectorArray);
        Button bExternVectorArray = new Button(this);
        bExternVectorArray.setText("Set extern_vectorArray");
        bExternVectorArray.setBackgroundColor(Color.GREEN);

        bExternVectorArray.setOnClickListener(v -> {
            org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] newExternVectorArray = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[](mClient.getExternVectorArray());
            //TODO increment
            Log.i(TAG, "SET extern_vectorArray" + newExternVectorArray);
            mClient.setExternVectorArray(newExternVectorArray);
        });
        propertyButtonsLine.addView(bExternVectorArray);

        layout.addView(propertyButtonsLine);


        LinearLayout methodButtonsLine = new LinearLayout(this);
        methodButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        Button bIncrement = new Button(this);
        bIncrement.setText("increment");

        bIncrement.setOnClickListener(v -> {
            Log.i(TAG, "CALLING METHOD  increment ");
            org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec =  new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
            CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> method_res
                    = mClient.incrementAsync(vec).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got increment result "+  i);
                        return i;
                    });
        });
        bIncrement.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bIncrement);
        Button bIncrementArray = new Button(this);
        bIncrementArray.setText("incrementArray");

        bIncrementArray.setOnClickListener(v -> {
            Log.i(TAG, "CALLING METHOD  incrementArray ");
            org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec =  new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
            CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]> method_res
                    = mClient.incrementArrayAsync(vec).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got incrementArray result "+  i);
                        return i;
                    });
        });
        bIncrementArray.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bIncrementArray);
        Button bDecrement = new Button(this);
        bDecrement.setText("decrement");

        bDecrement.setOnClickListener(v -> {
            Log.i(TAG, "CALLING METHOD  decrement ");
            customTypes.customTypes_api.Vector3D vec =  new customTypes.customTypes_api.Vector3D();
            CompletableFuture<customTypes.customTypes_api.Vector3D> method_res
                    = mClient.decrementAsync(vec).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got decrement result "+  i);
                        return i;
                    });
        });
        bDecrement.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bDecrement);
        Button bDecrementArray = new Button(this);
        bDecrementArray.setText("decrementArray");

        bDecrementArray.setOnClickListener(v -> {
            Log.i(TAG, "CALLING METHOD  decrementArray ");
            customTypes.customTypes_api.Vector3D[] vec =  new customTypes.customTypes_api.Vector3D();
            CompletableFuture<customTypes.customTypes_api.Vector3D[]> method_res
                    = mClient.decrementArrayAsync(vec).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got decrementArray result "+  i);
                        return i;
                    });
        });
        bDecrementArray.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bDecrementArray);

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
            mClient.removeEventListener(this);
        }
        super.onDestroy();
    }

    private void initServiceConnection( String servicePackage)
    {
        lastServicePackage = servicePackage;
        Log.i(TAG, "init service connection the client ");

        if (mClient == null)
        {
            mClient = new CounterClient(this.getApplicationContext(), "");
            Log.i(TAG, "client created ");
            mClient.addEventListener(this);
        }

        boolean res = mClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bindding result " + res);
        outputTextViewBIND.setText(res+": to "+lastServicePackage);
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