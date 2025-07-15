package {{dot .Module.Name}}.{{dot .Module.Name}}_client_example;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


//TODO for each interface there coudl be a tab? now only first one is added
{{- $Interface := (index .Module.Interfaces 0) }}

import {{camel .Module.Name}}.{{camel .Module.Name}}_android_client.{{Camel $Interface.Name }}Client;

//import message type and parcelabe types
{{- range .Module.Structs }}
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.{{Camel .Name}};
import {{dot .Module.Name}}.{{dot .Module.Name}}_android_messenger.{{Camel .Name}}Parcelable;
{{- end }}
{{- range .Module.Enums }}
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.{{Camel .Name}};
import {{dot .Module.Name}}.{{dot .Module.Name}}_android_messenger.{{Camel .Name}}Parcelable;
{{- end }}

import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel $Interface.Name }}EventListener;
import java.util.concurrent.CompletableFuture;



public class {{Camel .Module.Name}}TestClientApp extends Activity implements I{{Camel $Interface.Name }}EventListener
{

    private static final String TAG = "{{Camel .Module.Name}}TestClientApp";

    private {{Camel $Interface.Name }}Client mClient = null;

    //TODO ALIGN TO YOUR APP 
    private static String mModuleNameUnreal = "com.example.TestAndroid";
    private static String mModuleNameStub = "{{dot .Module.Name}}.{{dot .Module.Name}}_service_example.{{Camel .Module.Name}}TestServiceApp";
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
        
    {{- range $Interface.Properties }}
        Button b{{Camel .Name}} = new Button(this);
        b{{Camel .Name}}.setText("Set {{.Name}}");
        b{{Camel .Name}}.setBackgroundColor(Color.GREEN);

        b{{Camel .Name}}.setOnClickListener(v -> {
            {{javaReturn "" . }} new{{Camel .Name}} = mClient.get{{Camel .Name}}();
            //TODO increment
            Log.i(TAG, "SET {{.Name}}" + new{{Camel .Name}});
            mClient.set{{Camel .Name}}(new{{Camel .Name}});
        });
        propertyButtonsLine.addView(b{{Camel .Name}});
     {{- end }}

        layout.addView(propertyButtonsLine);


        LinearLayout methodButtonsLine = new LinearLayout(this);
        methodButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        
        {{- range $Interface.Operations }}
        Button b{{Camel .Name}} = new Button(this);
        b{{Camel .Name}}.setText("Execute Struct Method");

        b{{Camel .Name}}.setOnClickListener(v -> {
            Log.w(TAG, "CALLING METHOD  {{.Name}} ");
            {{- range .Params}}
            {{javaType "" . }} {{javaVar .}} =  {{- if not .IsPrimitive}} new {{ end -}} {{javaTestValue "" .}};
            {{- end}}
            {{javaAsyncReturn "" .Return}} method_res
                    = mClient.{{camel .Name}}Async({{javaVars .Params }}).thenApply(
                    i -> {
                        outputTextVieMethodRes.setText("Got {{.Name}} result "+  i);
                        return i;
                    });
        });
        b{{Camel .Name}}.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(b{{Camel .Name}});
        {{- end }}

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
            mClient = new {{Camel $Interface.Name }}Client(this.getApplicationContext(), "");
            Log.w(TAG, "client created ");
            mClient.addEventListener(this);
        }

        boolean res = mClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bindding result " + res);
        outputTextViewBIND.setText(res+": to "+lastServicePackage);
    }

    {{- range $Interface.Properties }}
    @Override
    public void on{{Camel .Name}}Changed({{javaType "" .}} newValue)
    {
        outputTextViewProp.setText("Property from service: {{.Name}} " + newValue);
        Log.w(TAG, "Property from service: {{.Name}} " + newValue);
     }
    {{- end }}
    {{- range $Interface.Signals }}
    @Override
    public void on{{Camel .Name}}({{javaParams "" .Params}})
    {
        String text = "Signal {{.Name}} {{- range .Params -}} + " " + {{javaVar .}}{{ end}};
        outputTextViewSig.setText(text);
        Log.w(TAG, text);
    }
    {{- end }}
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
