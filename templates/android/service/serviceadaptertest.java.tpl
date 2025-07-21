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
        {{- if or .IsPrimitive (eq .KindType "enum" ) }}
        when(backendServiceMock.get{{Camel .Name}}()).thenReturn({{javaTestValue "" .}});
        {{- else }}
        when(backendServiceMock.get{{Camel .Name}}()).thenReturn({{javaDefault "" .}});
		{{- end }}
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
    {{- if not .IsPrimitive }}
		data.setClassLoader({{Camel .Type}}Parcelable.class.getClassLoader());
	{{- end }}
	{{- end }}
    {{- range .Interface.Properties}}
        assertTrue(data.containsKey("{{.Name}}"));
	{{- if .IsPrimitive }}
		assertEquals(data.get{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", -1), {{javaTestValue "" .}});
	{{- else if eq .KindType "enum" }}
        // assertEquals(data.getParcelable("{{.Name}}", {{Camel .Type}}Parcelable.class).get{{Camel (javaReturn "" .)}}(), {{javaTestValue "" .}});
    {{- else }}
        //  TODO uncomment after adding comparision operator
        // assertEquals(data.getParcelable("{{.Name}}", {{Camel .Type}}Parcelable.class).get{{Camel (javaReturn "" .)}}(), {{javaDefault "" .}});
	{{- end }}
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

{{- range .Interface.Properties }}
//TODO do not add when a property is readonly
    @Test
    public void onReceive{{.Name}}PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, {{$InterfaceName}}MessageType.PROP_{{Camel .Name}}.getValue());
        Bundle data = new Bundle();

        {{- if .IsPrimitive }}
        {{javaReturn "" . }} newValue = {{javaTestValue "" . }};
		data.put{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", newValue);
		data.putInt("{{.Name}}", newValue);
		{{- else if (eq .KindType "enum") }}
        {{javaReturn "" . }} newValue = {{javaTestValue "" . }};
		data.putParcelable("{{.Name}}", new {{Camel .Type}}Parcelable(newValue));
        {{- else }}
        {{javaReturn "" . }} newValue = {{javaDefault "" . }};
		data.putParcelable("{{.Name}}", new {{Camel .Type}}Parcelable(newValue));
		{{- end }}

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).set{{Camel .Name}}(newValue);
	    
    }

    @Test
     public void whenNotified{{.Name}}()
    {
        {{- if or .IsPrimitive (eq .KindType "enum" ) }}
        {{javaReturn "" . }} newValue = {{javaTestValue "" . }};
		{{- else }}
        {{javaReturn "" . }} newValue = {{javaDefault "" . }};
		{{- end }}

        testedAdapterAsEventListener.on{{Camel .Name}}Changed(newValue);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals({{$InterfaceName}}MessageType.SET_{{Camel .Name}}.getValue(), response.what);
        Bundle data = response.getData();
	{{- if .IsPrimitive }}
		{{javaReturn "" . }} receivedByClient = data.get{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", -1);
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
        {{- if or .IsPrimitive (eq .KindType "enum" ) }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
		{{- else }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaDefault "" . }};
		{{- end }}
        {{- end }}

        testedAdapterAsEventListener.on{{Camel .Name}}({{javaVars .Params}});
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals({{$InterfaceName}}MessageType.SIG_{{Camel .Name}}.getValue(), response.what);
        Bundle data = response.getData();
    {{- range .Params}}
    {{- if not .IsPrimitive }}
		data.setClassLoader({{Camel .Type}}Parcelable.class.getClassLoader());
	{{- end }}
	{{- end }}
    {{- range .Params }}
	{{- if .IsPrimitive }}
		{{javaReturn "" . }} receivedByClient{{javaVar .}} = data.get{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", -1);
	{{- else }}
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
        {{- if .IsPrimitive }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
		data.put{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", {{javaVar .}});
		{{- else if eq .KindType "enum" }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
		data.putParcelable("{{.Name}}", new {{Camel .Type}}Parcelable({{javaVar .}}));
        {{- else }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaDefault "" . }};
		data.putParcelable("{{.Name}}", new {{Camel .Type}}Parcelable({{javaVar .}}));
		{{- end }}
	{{- end }}

        {{- if not .Return.IsVoid }}
        {{- if and .Return.IsPrimitive}}
        {{javaReturn "" .Return }} returnedValue = {{javaTestValue "" .Return }};
		{{- else }}
        {{javaReturn "" .Return }} returnedValue = {{javaDefault "" .Return }};
		{{- end }}
        when(backendServiceMock.{{camel .Name}}({{javaVars .Params}})).thenReturn(returnedValue);
        {{- end}}

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).{{camel .Name}}({{javaVars .Params}});

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals({{$InterfaceName}}MessageType.RPC_{{Camel .Name}}Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
    {{- if not .Return.IsVoid }}
	{{- if .Return.IsPrimitive }}
		{{javaReturn "" .Return }} receivedByClient = resp_data.get{{ ( Camel  (javaType "" .Return) ) }}("result", -1);
	{{- else }}
		resp_data.setClassLoader({{Camel .Return.Type}}Parcelable.class.getClassLoader());
		{{javaReturn "" .Return }} receivedByClient = resp_data.getParcelable("result", {{Camel .Return.Type}}Parcelable.class).get{{Camel (javaReturn "" .Return)}}();
	{{- end }}

        assertEquals(receivedByClient, returnedValue);
    {{- end}}
    }

{{- end}}

}
