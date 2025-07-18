//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package {{camel .Module.Name}}.{{camel .Module.Name}}_android_client;

import {{camel .Module.Name}}.{{camel .Module.Name}}_android_client.{{Camel .Interface.Name }}Client;

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
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.Abstract{{Camel .Interface.Name}};
import {{dot .Module.Name}}.{{dot .Module.Name}}_android_messenger.{{Camel .Interface.Name}}MessageType;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import android.content.ComponentName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.InOrder;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.RuntimeEnvironment;

import androidx.annotation.NonNull;


interface I{{Camel .Interface.Name }}ClientMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class {{Camel .Interface.Name }}ClientTest
{

    @Mock
    private Context mMockContext;
   
    private {{Camel .Interface.Name }}Client testedClient;
    private I{{Camel .Interface.Name }}EventListener listenerMock = mock(I{{Camel .Interface.Name }}EventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private I{{Camel .Interface.Name }}ClientMessageGetter serviceMessagesStorage = mock(I{{Camel .Interface.Name }}ClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    {{- $InterfaceName := Camel .Interface.Name}}
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals({{$InterfaceName}}MessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(I{{Camel .Interface.Name }}ClientMessageGetter messageGetterMock)
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

    @Before
    public void setUp() throws RemoteException
    {
        inOrderServiceMessenger = inOrder(serviceMessagesStorage);
        inOrderEventListener = inOrder(listenerMock);
        mServiceHandler = createServiceHandlerMock(serviceMessagesStorage);
        mServiceMessenger = new Messenger(mServiceHandler);
        IBinder serviceBinder = mServiceMessenger.getBinder();
	
        mMockContext = RuntimeEnvironment.getApplication();


        testedClient = new {{Camel .Interface.Name }}Client(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("{{camel .Module.Name}}.{{camel .Module.Name}}_android_service", "{{camel .Module.Name}}.{{camel .Module.Name}}_android_service.{{Camel .Interface.Name }}ServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals({{$InterfaceName}}MessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, {{$InterfaceName}}MessageType.INIT.getValue());
        Bundle data = new Bundle();
    {{- range .Interface.Properties}}
        {{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
		data.put{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", {{javaVar .}});
		{{- else if (eq .KindType "bool")}}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
		data.putInt("{{.Name}}", {{javaVar .}});
		{{- else }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaDefault "" . }};
		data.putParcelable("{{.Name}}", new {{Camel .Type}}Parcelable({{javaVar .}}));
		{{- end }}
    {{- end }}

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
    {{- range .Interface.Properties}}
        inOrderEventListener.verify(listenerMock,times(1)).on{{Camel .Name}}Changed({{javaVar .}});
    {{- end }}
    }

{{- range .Interface.Properties }}
//TODO do not add when a property is readonly
    @Test
    public void onReceive{{.Name}}PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, {{$InterfaceName}}MessageType.SET_{{Camel .Name}}.getValue());
        Bundle data = new Bundle();
        {{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
        {{javaReturn "" . }} newValue = {{javaTestValue "" . }};
		data.put{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", newValue);
		{{- else if (eq .KindType "bool")}}
        {{javaReturn "" . }} newValue = {{javaTestValue "" . }};
		data.putInt("{{.Name}}", newValue);
		{{- else }}
        {{javaReturn "" . }} newValue = {{javaDefault "" . }};
		data.putParcelable("{{.Name}}", new {{Camel .Type}}Parcelable(newValue));
		{{- end }}

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).on{{Camel .Name}}Changed(newValue);
	    
    }

    @Test
     public void setPropertyRequest{{.Name}}()
    {
        {{- if and .IsPrimitive}}
        {{javaReturn "" . }} newValue = {{javaTestValue "" . }};
		{{- else }}
        {{javaReturn "" . }} newValue = {{javaDefault "" . }};
		{{- end }}

        testedClient.set{{Camel .Name}}(newValue);
        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals({{$InterfaceName}}MessageType.PROP_{{Camel .Name}}.getValue(), response.what);
        Bundle data = response.getData();
	{{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
		{{javaReturn "" . }} receivedByService = data.get{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", -1);
	{{- else if (eq .KindType "bool")}}
		{{javaReturn "" . }} receivedByService =  = data.getInt("{{.Name}}", -1);
	{{- else }}
		data.setClassLoader({{Camel .Type}}Parcelable.class.getClassLoader());
		{{javaReturn "" . }} receivedByService = data.getParcelable("{{.Name}}", {{Camel .Type}}Parcelable.class).get{{Camel (javaReturn "" .)}}();
	{{- end }}
        assertEquals(receivedByService, newValue);
    }
{{- end}}

{{- range .Interface.Signals }}
    @Test
    public void whenNotified{{.Name}}() throws RemoteException
    {

        Message msg = Message.obtain(null, {{$InterfaceName}}MessageType.SIG_{{Camel .Name}}.getValue());
        Bundle data = new Bundle();
    {{- range .Params }}
        {{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
		data.put{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", {{javaVar .}});
		{{- else if (eq .KindType "bool")}}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
		data.putInt("{{.Name}}", {{javaVar .}});
		{{- else }}
        {{javaReturn "" . }} {{javaVar . }} = {{javaDefault "" . }};
		data.putParcelable("{{.Name}}", new {{Camel .Type}}Parcelable({{javaVar .}}));
		{{- end }}
    {{- end }}

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).on{{Camel .Name}}({{javaVars .Params}});

}
{{- end}}


{{- range .Interface.Operations }}


    public void on{{.Name}}Request() throws RemoteException {

        // Execute method
    {{- range .Params }}
    {{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
	{{- else if (eq .KindType "bool")}}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
	{{- else }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaDefault "" . }};
	{{- end }}
	{{- end }}

        {{- if and .Return.IsPrimitive}}
        {{javaReturn "" .Return }} expectedResult = {{javaTestValue "" .Return }};
		{{- else }}
        {{javaReturn "" .Return }} expectedResult = {{javaDefault "" .Return }};
		{{- end }}

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        {{javaAsyncReturn "" .Return}} resFuture = testedClient.{{camel .Name}}Async({{javaVars .Params}});

        resFuture.thenAccept(result -> {
        {{- if and (.Return.IsPrimitive) (not (eq .Return.KindType "string")) }}
            assertEquals(expectedResult, result.{{camel (javaType "" .Return)}}Value());
        {{- else }}
            assertEquals(expectedResult, result);
		{{- end }}
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());

        Message method_request = messageCaptor.getValue();
        assertEquals({{$InterfaceName}}MessageType.RPC_{{Camel .Name}}Req.getValue(), method_request.what);

        Bundle req_data = method_request.getData();
    {{- range .Params }}
	{{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
		{{javaReturn "" . }} received{{javaVar .}} = req_data.get{{ ( Camel  (javaType "" .) ) }}("result", -1);
	{{- else if (eq .KindType "bool")}}
		{{javaReturn "" . }} received{{javaVar .}} =  = req_data.getInt("{{javaVar .}}", -1);
	{{- else }}
		req_data.setClassLoader({{Camel .Type}}Parcelable.class.getClassLoader());
		{{javaReturn "" . }} received{{javaVar .}} = req_data.getParcelable("result", {{Camel .Type}}Parcelable.class).get{{Camel (javaReturn "" .)}}();
	{{- end }}
        assertEquals(received{{javaVar .}}, {{javaVar .}});
    	{{- end }}
        int returnedCallId = req_data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, {{$InterfaceName}}MessageType.RPC_{{Camel .Name}}Resp.getValue());

        Bundle data = new Bundle();
		data.putInt("callId", returnedCallId);
    {{- if not .Return.IsVoid }}
    {{- if .Return.IsPrimitive }}
		data.put{{ ( Camel  (javaType "" .Return) ) }}("result",expectedResult);
	{{- else }}
		data.putParcelable("result", new {{Camel .Return.Type}}Parcelable(expectedResult));
	{{- end }}
	{{- end }}

        msg.setData(data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

{{- end}}

}
