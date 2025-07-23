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

{{- define "getReceivedFromBundle"}}
		{{- if .IsPrimitive }}
			{{javaReturn "" .}} received{{javaVar .}} = data.get{{ ( Camel  (javaElementType "" .) ) }}{{if .IsArray}}Array{{end}}("{{.Name}}"{{if not .IsArray}}, {{javaDefault "" .}}{{end}});
		{{- else if .IsArray }}
            {{javaReturn "" .}} received{{javaVar .}} =  {{Camel .Type}}Parcelable.unwrapArray(({{Camel .Type}}Parcelable[])data.getParcelableArray("{{.Name}}", {{Camel .Type}}Parcelable.class));
        {{- else }}
			{{javaReturn "" .}} received{{javaVar .}} = data.getParcelable("{{.Name}}", {{Camel .Type}}Parcelable.class).get{{Camel (javaReturn "" .)}}();
		{{- end }}
{{- end }}

{{- define "putTestDataIntoBundle"}}
		{{- if .IsPrimitive }}
		data.put{{ ( Camel  (javaElementType "" .) ) }}{{if .IsArray}}Array{{end}}("{{.Name}}", test{{ javaVar .}});
		{{- else if .IsArray }}
		data.putParcelableArray("{{.Name}}", {{Camel (javaElementType "" .) }}Parcelable.wrapArray(test{{javaVar .}}));
        {{- else }}
		data.putParcelable("{{.Name}}", new {{Camel (javaElementType "" .) }}Parcelable(test{{javaVar .}}));
		{{- end }}
{{- end }}

{{- define "prepareTestValue"}}
        {{- if .IsArray }}
        {{javaElementType "" .}} element{{ javaVar .}} = {{javaTestValue "" . }};
        // todo fill if is struct
        {{javaReturn "" . }} test{{ javaVar .}} = new {{javaReturn "" . }}{element{{ javaVar .}}} ;
		{{- else if (.IsPrimitive) }}
		{{javaReturn "" . }} test{{ javaVar .}} = {{javaTestValue "" . }};
        {{- else if eq .KindType "enum"}}
        {{javaReturn "" . }} test{{ javaVar .}} = {{javaTestValue "" . }};
		{{- else }}
        {{javaReturn "" . }} test{{ javaVar .}} = {{javaTestValue "" . }};
        //TODO fill fields
		{{- end }}
{{- end }}


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
        {{- template "prepareTestValue" .}}
        {{- template "putTestDataIntoBundle" .}}
    {{- end }}

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
    {{- range .Interface.Properties}}
		{{- if or (.IsPrimitive) (eq .KindType "enum") }}
		inOrderEventListener.verify(listenerMock,times(1)).on{{Camel .Name}}Changed(test{{javaVar .}});
		{{- else }}
        inOrderEventListener.verify(listenerMock,times(1)).on{{Camel .Name}}Changed(any({{javaReturn "" . }}.class));
		{{- end }}
    {{- end }}
    }

{{- range .Interface.Properties }}
//TODO do not add when a property is readonly
    @Test
    public void onReceive{{.Name}}PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, {{$InterfaceName}}MessageType.SET_{{Camel .Name}}.getValue());
        Bundle data = new Bundle();
        {{- template "prepareTestValue" .}}
        {{- template "putTestDataIntoBundle" .}}

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        {{- if or (.IsPrimitive) (eq .KindType "enum") }}
		inOrderEventListener.verify(listenerMock,times(1)).on{{Camel .Name}}Changed(test{{javaVar .}});
		{{- else }}
        inOrderEventListener.verify(listenerMock,times(1)).on{{Camel .Name}}Changed(any({{javaReturn "" . }}.class));
		{{- end }}	    
    }

    @Test
     public void setPropertyRequest{{.Name}}()
    {
        {{- template "prepareTestValue" .}}

        testedClient.set{{Camel .Name}}(test{{ javaVar .}});
        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals({{$InterfaceName}}MessageType.PROP_{{Camel .Name}}.getValue(), response.what);
        Bundle data = response.getData();
	{{- if not (.IsPrimitive) }}
		data.setClassLoader({{Camel .Type}}Parcelable.class.getClassLoader());
	{{- end }}
        {{template "getReceivedFromBundle" . }}
        {{ if not (or (.IsPrimitive) (eq .KindType "enum")) }}// {{ end -}}
        assertEquals(received{{javaVar .}}, test{{ javaVar .}});
    }
{{- end}}

{{- range .Interface.Signals }}
    @Test
    public void whenNotified{{.Name}}() throws RemoteException
    {

        Message msg = Message.obtain(null, {{$InterfaceName}}MessageType.SIG_{{Camel .Name}}.getValue());
        Bundle data = new Bundle();
    {{- range .Params }}
        {{- template "prepareTestValue" .}}
        {{- template "putTestDataIntoBundle" .}}
    {{- end }}

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).on{{Camel .Name}}(
        {{- range $idx, $p :=.Params }}{{- if $idx}}, {{ end -}}
        {{- if or (.IsPrimitive) (eq .KindType "enum") }}test{{javaVar $p}}{{- else }} any({{javaReturn "" . }}.class) {{- end -}}{{- end -}}
        );

}
{{- end}}


{{- range .Interface.Operations }}


    public void on{{.Name}}Request() throws RemoteException {

        // Execute method
    {{- range .Params }}
        {{- template "prepareTestValue" .}}
	{{- end }}
    {{- if not .Return.IsVoid }}

        {{- if .Return.IsArray }}
        {{javaElementType "" .Return}} elementForResult = {{javaTestValue "" .Return }};
        // todo fill if is struct
        {{javaReturn "" .Return }} expectedResult = new {{javaReturn "" .Return }}{elementForResult} ;
		{{- else if (.Return.IsPrimitive) }}
		{{javaReturn "" .Return }} expectedResult = {{javaTestValue "" .Return }};
        {{- else if eq .Return.KindType "enum"}}
        {{javaReturn "" .Return }} expectedResult = {{javaTestValue "" .Return }};
		{{- else }}
        {{javaReturn "" .Return }} expectedResult = {{javaTestValue "" .Return }};
        //TODO fill fields
		{{- end }}
        {{- end }}

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        {{javaAsyncReturn "" .Return}} resFuture = testedClient.{{camel .Name}}Async({{- range $idx, $p :=.Params }}{{- if $idx}}, {{ end -}}test{{javaVar $p}}{{- end }});

        resFuture.thenAccept(result -> {
        {{- if not .Return.IsVoid }}
        {{- if .Return.IsArray }}
            assertEquals(expectedResult, result);
        {{- else if and (.Return.IsPrimitive) (not (eq .Return.KindType "string")) }}
            assertEquals(expectedResult, result.{{camel (javaType "" .Return)}}Value());
        {{- else }}
            assertEquals(expectedResult, result);
		{{- end }}
        {{- end }}
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals({{$InterfaceName}}MessageType.RPC_{{Camel .Name}}Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        {{- range .Params }}
	    {{- if not (.IsPrimitive) }}
		data.setClassLoader({{Camel .Type}}Parcelable.class.getClassLoader());
        {{- end }}
        {{- end }}
        {{- range .Params }}
        {{template "getReceivedFromBundle" . }}
        assertEquals(received{{javaVar .}}, test{{javaVar .}});
    	{{- end }}
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, {{$InterfaceName}}MessageType.RPC_{{Camel .Name}}Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
        {{- if not .Return.IsVoid }}
		{{- if .Return.IsPrimitive }}
		result_data.put{{ ( Camel  (javaElementType "" .Return) ) }}{{if .Return.IsArray}}Array{{end}}("result", expectedResult);
		{{- else if .Return.IsArray }}
		result_data.putParcelableArray("result", {{Camel (javaElementType "" .Return) }}Parcelable.wrapArray(expectedResult));
        {{- else }}
		result_data.putParcelable("result", new {{Camel (javaElementType "" .Return) }}Parcelable(expectedResult));
		{{- end }}
        {{- end }}

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

{{- end}}

}
