package tbRefIfaces.tbRefIfaces_client_example;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


//TODO for each interface there coudl be a tab? now only first one is added

import tbRefIfaces.tbRefIfaces_android_client.SimpleLocalIfClient;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;
import java.util.concurrent.CompletableFuture;



public class TbRefIfacesTestClientApp extends Activity implements ISimpleLocalIfEventListener
{

    private static final String TAG = "TbRefIfacesTestClientApp";

    private SimpleLocalIfClient mClient = null;

    //TODO ALIGN TO YOUR APP 
    private static String mModuleNameUnreal = "com.example.TestAndroid";
    private static String mModuleNameStub = "tbRefIfaces.tbRefIfacesserviceexample.TbRefIfacesTestServiceApp";
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
            int newIntProperty = mClient.getIntProperty();
            
            //TODO increment
            Log.i(TAG, "SET intProperty" + newIntProperty);
            mClient.setIntProperty(newIntProperty);
        });
        propertyButtonsLine.addView(bIntProperty);

        layout.addView(propertyButtonsLine);


        LinearLayout methodButtonsLine = new LinearLayout(this);
        methodButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        Button bIntMethod = new Button(this);
        bIntMethod.setText("intMethod");

        bIntMethod.setOnClickListener(v -> {
            Log.i(TAG, "CALLING METHOD  intMethod ");
            int param =  1;
            CompletableFuture<Integer> method_res
                    = mClient.intMethodAsync(param).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got intMethod result "+  i);
                        return i;
                    });
        });
        bIntMethod.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(bIntMethod);

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
            mClient = new SimpleLocalIfClient(this.getApplicationContext(), "");
            Log.i(TAG, "client created ");
            mClient.addEventListener(this);
        }

        boolean res = mClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bindding result " + res);
        outputTextViewBIND.setText(res+": to "+lastServicePackage);
    }
    @Override
    public void onIntPropertyChanged(int newValue)
    {
        outputTextViewProp.setText("Property from service: intProperty " + newValue);
        Log.i(TAG, "Property from service: intProperty " + newValue);
     }
    @Override
    public void onIntSignal(int param)
    {
        String text = "Signal intSignal "+ " " + param;
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