//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame2.tbSame2_android_service;

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
import tbSame2.tbSame2_android_service.SameEnum1InterfaceServiceAdapter;

//import message type and parcelabe types
import tbSame2.tbSame2_api.Struct1;
import tbSame2.tbSame2_android_messenger.Struct1Parcelable;
import tbSame2.tbSame2_api.Struct2;
import tbSame2.tbSame2_android_messenger.Struct2Parcelable;
import tbSame2.tbSame2_api.TbSame2TestHelper;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_android_messenger.Enum1Parcelable;
import tbSame2.tbSame2_api.Enum2;
import tbSame2.tbSame2_android_messenger.Enum2Parcelable;


import tbSame2.tbSame2_api.ISameEnum1InterfaceEventListener;
import tbSame2.tbSame2_android_service.ISameEnum1InterfaceServiceFactory;
import tbSame2.tbSame2_api.ISameEnum1Interface;
import tbSame2.tbSame2_api.AbstractSameEnum1Interface;
import tbSame2.tbSame2_android_messenger.SameEnum1InterfaceMessageType;


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

interface ISameEnum1InterfaceMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SameEnum1InterfaceServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private SameEnum1InterfaceServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private ISameEnum1InterfaceEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private ISameEnum1Interface backendServiceMock = mock(ISameEnum1Interface.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private ISameEnum1InterfaceServiceFactory serviceFactory = mock(ISameEnum1InterfaceServiceFactory.class);
    private ISameEnum1InterfaceMessageGetter clientMessagesStorage = mock(ISameEnum1InterfaceMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, SameEnum1InterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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

    Handler createClientHandlerMock(ISameEnum1InterfaceMessageGetter messageGetterMock)
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
        Enum1 initprop1 = Enum1.Value2;
        when(backendServiceMock.getProp1()).thenReturn(initprop1);


        Message registerMsg = Message.obtain(null, SameEnum1InterfaceMessageType.REGISTER_CLIENT.ordinal());
        registerMsg.getData().putString("connectionID", id);
        registerMsg.replyTo = messenger;
    
        try {
            mServiceMessenger.send(registerMsg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock, times(1)).getProp1();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SameEnum1InterfaceMessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        
			Enum1 receivedprop1 = data.getParcelable("prop1", Enum1Parcelable.class).getEnum1();
		data.setClassLoader(Enum1Parcelable.class.getClassLoader());
        assertEquals(receivedprop1, initprop1);

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

        testedServiceAdapterIntent = new Intent(mMockContext, SameEnum1InterfaceServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(SameEnum1InterfaceServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(serviceFactory.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(serviceFactory);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<ISameEnum1InterfaceEventListener> eventListnerCaptor = ArgumentCaptor.forClass(ISameEnum1InterfaceEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
//TODO do not add when a property is readonly
    @Test
    public void onReceiveprop1PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SameEnum1InterfaceMessageType.PROP_Prop1.getValue());
        Bundle data = new Bundle();
		Enum1 testprop1 = Enum1.Value2;
		data.putParcelable("prop1", new Enum1Parcelable(testprop1));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setProp1(testprop1);
	    
    }

    @Test
     public void whenNotifiedprop1()
    {
		Enum1 testprop1 = Enum1.Value2;

        testedAdapterAsEventListener.onProp1Changed(testprop1);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SameEnum1InterfaceMessageType.SET_Prop1.getValue(), response.what);
        Bundle data = response.getData();

        
			Enum1 receivedprop1 = data.getParcelable("prop1", Enum1Parcelable.class).getEnum1();

        assertEquals(receivedprop1, testprop1);
    }
    @Test
    public void whenNotifiedsig1()
    {
		Enum1 testparam1 = Enum1.Value2;

        testedAdapterAsEventListener.onSig1(testparam1);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SameEnum1InterfaceMessageType.SIG_Sig1.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(Enum1Parcelable.class.getClassLoader());
        
			Enum1 receivedparam1 = data.getParcelable("param1", Enum1Parcelable.class).getEnum1();
        assertEquals(receivedparam1, testparam1);
}


    public void onfunc1Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SameEnum1InterfaceMessageType.RPC_Func1Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		Enum1 testparam1 = Enum1.Value2;
		data.putParcelable("param1", new Enum1Parcelable(testparam1));
        Enum1 returnedValue = Enum1.Value2;


        when(backendServiceMock.func1(testparam1)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).func1(testparam1);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SameEnum1InterfaceMessageType.RPC_Func1Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(Enum1Parcelable.class.getClassLoader());
		Enum1 receivedByClient = resp_data.getParcelable("result", Enum1Parcelable.class).getEnum1();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
