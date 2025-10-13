//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package {{camel .Module.Name}}.{{camel .Module.Name}}_android_service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.{{Camel .Interface.Name }}ServiceAdapter;

//import message type and parcelabe types

{{- range .Module.Structs }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger.{{Camel .Name}}Parcelable;
{{- end }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Module.Name}}TestHelper;
{{- range .Module.Enums }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger.{{Camel .Name}}Parcelable;
{{- end }}


import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.I{{Camel .Interface.Name}}ServiceFactory;
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.Abstract{{Camel .Interface.Name}};
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger.{{Camel .Interface.Name}}MessageType;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import androidx.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.InOrder;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ServiceController;
import org.robolectric.annotation.Config;
import org.robolectric.RuntimeEnvironment;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

interface I{{Camel .Interface.Name }}MessageGetter
{
    public void getMessage(Message msg);
}
{{- $InterfaceName := Camel .Interface.Name}}

{{- define "prepareInitValue"}}
        {{- if .IsArray }}
        {{javaElementType "" .}} init_element{{ javaVar .}} = {{javaTestValue "" . }};
        // todo fill if is struct
        {{javaReturn "" . }} init{{ javaVar .}} = new {{javaReturn "" . }}{ init_element{{ javaVar .}} } ;
		{{- else if (.IsPrimitive) }}
		{{javaReturn "" . }} init{{ javaVar .}} = {{javaTestValue "" . }};
        {{- else if eq .KindType "enum"}}
        {{javaReturn "" . }} init{{ javaVar .}} = {{javaTestValue "" . }};
		{{- else }}
        {{javaReturn "" . }} init{{ javaVar .}} = {{javaTestValue "" . }};
        //TODO fill fields
		{{- end }}
{{- end }}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class {{Camel .Interface.Name }}ServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private {{Camel .Interface.Name }}ServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private I{{Camel .Interface.Name }}EventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private I{{Camel .Interface.Name }} backendServiceMock = mock(I{{Camel .Interface.Name }}.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private I{{Camel .Interface.Name}}ServiceFactory serviceFactory = mock(I{{Camel .Interface.Name}}ServiceFactory.class);
    private I{{Camel .Interface.Name }}MessageGetter clientMessagesStorage = mock(I{{Camel .Interface.Name }}MessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, {{Camel .Interface.Name}}MessageType.UNREGISTER_CLIENT.ordinal());
        unregisterMsg.getData().putString("connectionID", mTestConnectionID1);
	   
        try {
            mServiceMessenger.send(unregisterMsg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        if (mMockContext != null) {
            mMockContext.stopService(testedServiceAdapterIntent);  
        }
        testedServiceAdapter.onDestroy();
        inOrderBackendService.verify(backendServiceMock, times(1)).removeEventListener(testedAdapterAsEventListener);
    }

    Handler createClientHandlerMock(I{{Camel .Interface.Name }}MessageGetter messageGetterMock)
    {
        return new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                Message copy = Message.obtain();
                copy.copyFrom(msg);
                messageGetterMock.getMessage(copy);
            }
        };
    }

    void registerFakeActivityClient(Messenger messenger, String id)
    {
        {{- range .Interface.Properties}}
        {{- template "prepareInitValue" .}}
        when(backendServiceMock.get{{Camel .Name}}()).thenReturn(init{{javaVar .}});
        {{- end }}


        Message registerMsg = Message.obtain(null, {{Camel .Interface.Name}}MessageType.REGISTER_CLIENT.ordinal());
        registerMsg.getData().putString("connectionID", id);
        registerMsg.replyTo = messenger;
    
        try {
            mServiceMessenger.send(registerMsg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Robolectric.flushForegroundThreadScheduler();

        {{- range .Interface.Properties}}
        inOrderBackendService.verify(backendServiceMock, times(1)).get{{Camel .Name}}();
        {{- end }}

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals({{$InterfaceName}}MessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        {{- range .Interface.Properties}}
        {{ template "getReceivedFromBundle" .}}
        {{- end }}
        {{template "setClassLoaderIfNeeded" .Interface.Properties}}
    {{- range .Interface.Properties}}
        {{ if not (or (.IsPrimitive) (eq .KindType "enum")) }}// {{ end -}}
        assertEquals(received{{javaVar .}}, init{{ javaVar .}}
        {{- if and (not .IsArray) (or (or (eq .KindType "float") (eq .KindType "float32") ) (eq .KindType "float64")) -}},
        1e-6f{{end -}}
        );
	{{- end }}

    }

    @Before
    public void setUp() throws RemoteException
    {
        clientReplyHandler = createClientHandlerMock(clientMessagesStorage);
        clientReplyMessenger = new Messenger(clientReplyHandler);
	    inOrderClientMessagesHandler = inOrder(clientMessagesStorage);
	    inOrderBackendService = inOrder(backendServiceMock);
        mMockContext = RuntimeEnvironment.getApplication();

        when(backendServiceMock._isReady()).thenReturn(true);

        testedServiceAdapterIntent = new Intent(mMockContext, {{Camel .Interface.Name }}ServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService({{Camel .Interface.Name }}ServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(serviceFactory.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(serviceFactory);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<I{{Camel .Interface.Name }}EventListener> eventListnerCaptor = ArgumentCaptor.forClass(I{{Camel .Interface.Name }}EventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }


    {{- if not ( or (len (.Interface.Signals)) ( or ( len (.Interface.Operations)) ( len (.Interface.Properties)))) }}
    @Test
    public void onlySetupAndTeardown()
    {

    }
    {{- end }}

{{- range .Interface.Properties }}
    {{- if not .IsReadOnly }}
    @Test
    public void onReceive{{.Name}}PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, {{$InterfaceName}}MessageType.PROP_{{Camel .Name}}.getValue());
        Bundle data = new Bundle();

        {{- template "prepareTestValue" .}}
        {{- template "putTestDataIntoBundle" .}}

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).set{{Camel .Name}}({{- if or (.IsPrimitive) (eq .KindType "enum") }}test{{javaVar .}}{{- else }} any({{javaReturn "" . }}.class) {{- end -}});
	    
    }
    {{- end }}

    @Test
     public void whenNotified{{.Name}}()
    {
        {{- template "prepareTestValue" .}}

        testedAdapterAsEventListener.on{{Camel .Name}}Changed(test{{javaVar .}});
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals({{$InterfaceName}}MessageType.SET_{{Camel .Name}}.getValue(), response.what);
        Bundle data = response.getData();

        {{template "getReceivedFromBundle" . }}

        assertEquals(received{{javaVar .}}, test{{javaVar .}}
        {{- if and (not .IsArray) (or (or (eq .KindType "float") (eq .KindType "float32") ) (eq .KindType "float64")) -}},
        1e-6f{{end -}}
        );
    }
{{- end}}

{{- range .Interface.Signals }}
    @Test
    public void whenNotified{{.Name}}()
    {
        {{- range .Params }}
        {{- template "prepareTestValue" .}}
        {{- end }}

        testedAdapterAsEventListener.on{{Camel .Name}}({{- range $idx, $p :=.Params }}{{- if $idx}}, {{ end -}}test{{javaVar $p}}{{- end }});
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals({{$InterfaceName}}MessageType.SIG_{{Camel .Name}}.getValue(), response.what);
        Bundle data = response.getData();
        {{template "setClassLoaderIfNeeded" .Params}}
    {{- range .Params }}
        {{template "getReceivedFromBundle" . }}
        assertEquals(received{{javaVar .}}, test{{ javaVar .}}
        {{- if and (not .IsArray) (or (or (eq .KindType "float") (eq .KindType "float32") ) (eq .KindType "float64")) -}},
        1e-6f{{end -}}
        );
    {{- end}}
}
{{- end}}


{{- range .Interface.Operations }}


    public void on{{.Name}}Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, {{$InterfaceName}}MessageType.RPC_{{Camel .Name}}Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        {{- range .Params }}
        {{- template "prepareTestValue" .}}
        {{- template "putTestDataIntoBundle" .}}
        {{- end}}

        {{- if not .Return.IsVoid }}
        {{- if .Return.IsArray }}
            {{- if or  (.Return.IsPrimitive) (eq .Return.KindType "enum")}}
        {{javaType "" .Return }} returnedValue = new {{javaElementType "" .Return }}[1];
        returnedValue[0] = {{javaTestValue "" .Return }};
            {{- else }}
        {{javaElementType "" .Return }}[] returnedValue = new {{javaElementType "" .Return }}[1];
                {{- if (eq .Return.KindType "extern") }}
		returnedValue[0] = {{javaTestValue "" .Return}};
                {{- else }}
        returnedValue[0] = {{template "getMakeTestHelper" .Return }}({{-  if (eq .Return.KindType "interface")}}{{javaDefault "" .Return}}{{end}});
                {{- end }}
            {{- end}}
		{{- else if or  ( .Return.IsPrimitive) (eq .Return.KindType "enum") }}
        {{javaReturn "" .Return }} returnedValue = {{javaTestValue "" .Return }};
        {{- else if (eq .Return.KindType "extern") }}
		{{javaReturn "" .Return }} returnedValue = {{javaTestValue "" .Return}};
        {{- else }}
        {{javaReturn "" .Return }} returnedValue = {{template "getMakeTestHelper" .Return }}({{-  if (eq .Return.KindType "interface")}}{{javaDefault "" .Return}}{{end}});
		{{- end }}


        when(backendServiceMock.{{camel .Name}}({{- range $idx, $p :=.Params }}{{- if $idx}}, {{ end -}}
        {{- if or (.IsPrimitive) (eq .KindType "enum") }}test{{javaVar $p}}{{- else }} any({{javaReturn "" . }}.class) {{- end -}}{{- end -}}
        )).thenReturn(returnedValue);
        {{- end}}

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).{{camel .Name}}({{- range $idx, $p :=.Params }}{{- if $idx}}, {{ end -}}
        {{- if or (.IsPrimitive) (eq .KindType "enum") }}test{{javaVar $p}}{{- else }} any({{javaReturn "" . }}.class) {{- end -}}{{- end -}}
        );

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals({{$InterfaceName}}MessageType.RPC_{{Camel .Name}}Resp.getValue(), response.what);
        Bundle resp_data = response.getData();

    {{- if not .Return.IsVoid }}
	{{- if .Return.IsPrimitive }}
		{{javaReturn "" .Return }} receivedByClient = resp_data.get{{ ( Camel  (javaElementType "" .Return) ) }}{{if .Return.IsArray}}Array{{end}}("result"{{if not .Return.IsArray}}, {{javaDefault "" .Return}}{{end}});
	{{- else if .Return.IsArray }}
        resp_data.setClassLoader({{template "getParcelable" .Return }}.class.getClassLoader());
        {{javaReturn "" .Return }} receivedByClient =  {{template "getParcelable" .Return }}.unwrapArray(({{template "getParcelable" .Return }}[])resp_data.getParcelableArray("result", {{template "getParcelable" .Return }}.class));
    {{- else }}
		resp_data.setClassLoader({{template "getParcelable" .Return }}.class.getClassLoader());
		{{javaReturn "" .Return }} receivedByClient = resp_data.getParcelable("result", {{template "getParcelable" .Return }}.class).get{{Camel .Return.Type }}();
	{{- end }}

        assertEquals(receivedByClient, returnedValue
        {{- if and (not .Return.IsArray) (or (or (eq .Return.KindType "float") (eq .Return.KindType "float32") ) (eq .Return.KindType "float64")) -}},
        1e-6f{{end -}}
        );
    {{- end }}
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

{{- end}}

}
