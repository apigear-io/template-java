package {{camel .Module.Name}}.{{camel .Module.Name}}serviceexample;

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
{{- $Interface := (index .Module.Interfaces 0) }}

import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.{{Camel $Interface.Name }}ServiceAdapter;
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.{{Camel $Interface.Name}}ServiceFactory;
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.{{Camel $Interface.Name }}ServiceStarter;

//import message type and parcelabe types
{{- range .Module.Structs }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger.{{Camel .Name}}Parcelable;
{{- end }}
{{- range .Module.Enums }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger.{{Camel .Name}}Parcelable;
{{- end }}

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel $Interface.Name }}EventListener;
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel $Interface.Name}};
import {{camel .Module.Name}}.{{camel .Module.Name}}_impl.{{Camel $Interface.Name}}Service;
import java.util.concurrent.CompletableFuture;



public class {{Camel .Module.Name}}TestServiceApp extends Activity implements I{{Camel $Interface.Name }}EventListener
{

    private static final String TAG = "{{Camel .Module.Name}}TestServiceApp";
    static Intent stub_service = null;


    private I{{Camel $Interface.Name}} mBackend = null;

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
            {{javaReturn "" . }} new{{Camel .Name}} = mBackend.get{{Camel .Name}}();
            //TODO increment
            Log.i(TAG, "SET {{.Name}}" + new{{Camel .Name}});
            mBackend.set{{Camel .Name}}(new{{Camel .Name}});
        });
        propertyButtonsLine.addView(b{{Camel .Name}});
     {{- end }}

        layout.addView(propertyButtonsLine);


        LinearLayout methodButtonsLine = new LinearLayout(this);
        methodButtonsLine.setOrientation(LinearLayout.HORIZONTAL);
        
        {{- range $Interface.Signals }}
        Button b{{Camel .Name}} = new Button(this);
        b{{Camel .Name}}.setText("{{.Name}}");

        b{{Camel .Name}}.setOnClickListener(v -> {
            Log.w(TAG, "broadcasting singal  {{.Name}} ");
            {{- range .Params}}
            {{javaType "" . }} {{javaVar .}} =  {{javaTestValue "" .}};
            {{- end}}
            mBackend.fire{{Camel .Name}}({{javaVars  .Params}});
        });
        b{{Camel .Name}}.setBackgroundColor(Color.GREEN);
        methodButtonsLine.addView(b{{Camel .Name}});
        {{- end }}

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
        stub_service = new Intent(this, {{Camel $Interface.Name }}ServiceAdapter.class);
        this.startService(stub_service);
        Log.w(TAG, "Service started with stub backend");
        mBackend = {{Camel $Interface.Name }}ServiceAdapter.setService({{Camel $Interface.Name }}ServiceFactory.get());
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
        String text = "Signal {{.Name}} "{{- range .Params -}} + " " + {{javaVar .}}{{ end}};
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
