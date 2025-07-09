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
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.{{Camel .Name}};
import {{dot .Module.Name}}.{{dot .Module.Name}}_android_messenger.{{Camel .Name}}Parcelable;
{{- end }}
{{- range .Module.Enums }}
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.{{Camel .Name}};
import {{dot .Module.Name}}.{{dot .Module.Name}}_android_messenger.{{Camel .Name}}Parcelable;
{{- end }}


import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.I{{Camel .Interface.Name}}ServiceFactory;
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.Abstract{{Camel .Interface.Name}};
import {{dot .Module.Name}}.{{dot .Module.Name}}_android_messenger.{{Camel .Interface.Name}}MessageType;


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

import poc.poc_android_service.*;

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

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class {{Camel .Interface.Name }}ServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private {{Camel .Interface.Name }}ServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private I{{Camel .Interface.Name }}EventListener testedAdapterAsEventListener;
    //private ServiceController<{{Camel .Interface.Name }}ServiceAdapter> serviceController; TODO STARTED BY HAND
    private Messenger mServiceMessenger;
    private I{{Camel .Interface.Name }} backendServiceMock = mock(I{{Camel .Interface.Name }}.class);

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
        verify(backendServiceMock, times(1)).removeEventListener(testedAdapterAsEventListener);
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
        Message registerMsg = Message.obtain(null, {{Camel .Interface.Name}}MessageType.REGISTER_CLIENT.ordinal());
        registerMsg.getData().putString("connectionID", id);
        registerMsg.replyTo = messenger;
    
        try {
            mServiceMessenger.send(registerMsg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Robolectric.flushForegroundThreadScheduler();
    }

    @Before
    public void setUp() throws RemoteException
    {
        clientReplyHandler = createClientHandlerMock(clientMessagesStorage);
        clientReplyMessenger = new Messenger(clientReplyHandler);
	
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
        verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }

    {{- $InterfaceName := Camel .Interface.Name}}
{{- range .Interface.Properties }}
//TODO do not add when a property is readonly
    @Test
    public void onReceive{{.Name}}PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, {{$InterfaceName}}MessageType.PROP_{{Camel .Name}}.getValue());
        Bundle data = new Bundle();
        {{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
        {{javaReturn "" . }} newValue = {{javaTestValue "" . }};
		data.put{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", newValue);
		{{- else if (eq .KindType "bool")}}
        {{javaReturn "" . }} newValue = {{javaTestValue "" . }};
		data.putInt{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", newValue);
		{{- else }}
        {{javaReturn "" . }} newValue = {{javaDefault "" . }};
		data.putParcelable("{{.Name}}", new {{Camel .Type}}Parcelable(newValue));
		{{- end }}

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        verify(backendServiceMock,times(1)).set{{Camel .Name}}(newValue);
	    
    }

    @Test
     public void whenNotified{{.Name}}()
    {
        {{- if and .IsPrimitive}}
        {{javaReturn "" . }} newValue = {{javaTestValue "" . }};
		{{- else }}
        {{javaReturn "" . }} newValue = {{javaDefault "" . }};
		{{- end }}

        testedAdapterAsEventListener.on{{Camel .Name}}Changed(newValue);
        Robolectric.flushForegroundThreadScheduler();

        verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals({{$InterfaceName}}MessageType.SET_{{Camel .Name}}.getValue(), response.what);
        Bundle data = response.getData();
	{{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
		{{javaReturn "" . }} receivedByClient = data.get{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", -1);
	{{- else if (eq .KindType "bool")}}
		{{javaReturn "" . }} receivedByClient =  = data.getInt("{{.Name}}", -1);
	{{- else }}
		data.setClassLoader({{Camel .Type}}Parcelable.class.getClassLoader());
		{{javaReturn "" . }} receivedByClient = data.getParcelable("{{.Name}}", {{Camel .Type}}Parcelable.class).get{{Camel (javaReturn "" .)}}();
	{{- end }}
        assertEquals(receivedByClient, newValue);
    }
{{- end}}

{{- range .Interface.Signals }}
    @Test
    public void whenNotified{{.Name}}()
    {
        {{- range .Params }}
        {{- if and .IsPrimitive}}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
		{{- else }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaDefault "" . }};
		{{- end }}
        {{- end }}

        testedAdapterAsEventListener.on{{Camel .Name}}({{javaVars .Params}});
        Robolectric.flushForegroundThreadScheduler();

        verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals({{$InterfaceName}}MessageType.SIG_{{Camel .Name}}.getValue(), response.what);
        Bundle data = response.getData();
    {{- range .Params }}
	{{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
		{{javaReturn "" . }} receivedByClient{{javaVar .}} = data.get{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", -1);
	{{- else if (eq .KindType "bool")}}
		{{javaReturn "" . }} receivedByClient{{javaVar .}} =  = data.getInt("{{.Name}}", -1);
	{{- else }}
		data.setClassLoader({{Camel .Type}}Parcelable.class.getClassLoader());
		{{javaReturn "" . }} receivedByClient{{javaVar .}} = data.getParcelable("{{.Name}}", {{Camel .Type}}Parcelable.class).get{{Camel (javaReturn "" .)}}();
	{{- end }}
        assertEquals(receivedByClient{{javaVar .}}, {{javaVar .}});
    {{- end}}
}
{{- end}}


{{- range .Interface.Operations }}


    public void on{{.Name}}Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, {{$InterfaceName}}MessageType.RPC_{{Camel .Name}}Req.getValue());
        Bundle data = new Bundle();
    {{- range .Params }}
        {{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
		data.put{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", {{javaVar .}});
		{{- else if (eq .KindType "bool")}}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
		data.putInt{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", {{javaVar .}});
		{{- else }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaDefault "" . }};
		data.putParcelable("{{.Name}}", new {{Camel .Type}}Parcelable({{javaVar .}}));
		{{- end }}
	{{- end }}

        {{- if and .Return.IsPrimitive}}
        {{javaReturn "" .Return }} returnedValue = {{javaTestValue "" .Return }};
		{{- else }}
        {{javaReturn "" .Return }} returnedValue = {{javaDefault "" .Return }};
		{{- end }}
        when(backendServiceMock.{{camel .Name}}({{javaVars .Params}})).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        verify(backendServiceMock,times(1)).{{camel .Name}}({{javaVars .Params}});

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();

        verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals({{$InterfaceName}}MessageType.RPC_{{Camel .Name}}Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
	{{- if and (.Return.IsPrimitive) (not (eq .Return.KindType "bool")) }}
		{{javaReturn "" .Return }} receivedByClient = resp_data.get{{ ( Camel  (javaType "" .Return) ) }}("result", -1);
	{{- else if (eq .Return.KindType "bool")}}
		{{javaReturn "" .Return }} receivedByClient =  = resp_data.getInt("result", -1);
	{{- else }}
		resp_data.setClassLoader({{Camel .Return.Type}}Parcelable.class.getClassLoader());
		{{javaReturn "" .Return }} receivedByClient = resp_data.getParcelable("result", {{Camel .Return.Type}}Parcelable.class).get{{Camel (javaReturn "" .Return)}}();
	{{- end }}
        assertEquals(receivedByClient, returnedValue);
    }

{{- end}}

}
