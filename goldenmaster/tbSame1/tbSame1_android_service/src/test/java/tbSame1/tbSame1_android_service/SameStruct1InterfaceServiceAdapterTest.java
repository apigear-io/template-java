//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame1.tbSame1_android_service;

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
import tbSame1.tbSame1_android_service.SameStruct1InterfaceServiceAdapter;

//import message type and parcelabe types
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_android_messenger.Struct1Parcelable;
import tbSame1.tbSame1_api.Struct2;
import tbSame1.tbSame1_android_messenger.Struct2Parcelable;
import tbSame1.tbSame1_api.TbSame1TestHelper;
import tbSame1.tbSame1_api.Enum1;
import tbSame1.tbSame1_android_messenger.Enum1Parcelable;
import tbSame1.tbSame1_api.Enum2;
import tbSame1.tbSame1_android_messenger.Enum2Parcelable;
import tbSame1.tbSame1_api.ISameStruct1Interface;
import tbSame1.tbSame1_android_messenger.SameStruct1InterfaceParcelable;
import tbSame1.tbSame1_impl.SameStruct1InterfaceService;
import tbSame1.tbSame1_api.ISameStruct2Interface;
import tbSame1.tbSame1_android_messenger.SameStruct2InterfaceParcelable;
import tbSame1.tbSame1_impl.SameStruct2InterfaceService;
import tbSame1.tbSame1_api.ISameEnum1Interface;
import tbSame1.tbSame1_android_messenger.SameEnum1InterfaceParcelable;
import tbSame1.tbSame1_impl.SameEnum1InterfaceService;
import tbSame1.tbSame1_api.ISameEnum2Interface;
import tbSame1.tbSame1_android_messenger.SameEnum2InterfaceParcelable;
import tbSame1.tbSame1_impl.SameEnum2InterfaceService;


import tbSame1.tbSame1_api.ISameStruct1InterfaceEventListener;
import tbSame1.tbSame1_android_service.ISameStruct1InterfaceServiceProvider;
import tbSame1.tbSame1_api.ISameStruct1Interface;
import tbSame1.tbSame1_api.AbstractSameStruct1Interface;
import tbSame1.tbSame1_android_messenger.SameStruct1InterfaceMessageType;


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

interface ISameStruct1InterfaceMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SameStruct1InterfaceServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private SameStruct1InterfaceServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private ISameStruct1InterfaceEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private ISameStruct1Interface backendServiceMock = mock(ISameStruct1Interface.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private ISameStruct1InterfaceServiceProvider ServiceProvider = mock(ISameStruct1InterfaceServiceProvider.class);
    private ISameStruct1InterfaceMessageGetter clientMessagesStorage = mock(ISameStruct1InterfaceMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, SameStruct1InterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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

    Handler createClientHandlerMock(ISameStruct1InterfaceMessageGetter messageGetterMock)
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
        Struct1 initprop1 = new Struct1();
        //TODO fill fields
        when(backendServiceMock.getProp1()).thenReturn(initprop1);


        Message registerMsg = Message.obtain(null, SameStruct1InterfaceMessageType.REGISTER_CLIENT.ordinal());
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

        assertEquals(SameStruct1InterfaceMessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        
			Struct1 receivedprop1 = data.getParcelable("prop1", Struct1Parcelable.class).getStruct1();
        
        data.setClassLoader(Struct1Parcelable.class.getClassLoader());
        // assertEquals(receivedprop1, initprop1);

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

        testedServiceAdapterIntent = new Intent(mMockContext, SameStruct1InterfaceServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(SameStruct1InterfaceServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(ServiceProvider.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(ServiceProvider);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<ISameStruct1InterfaceEventListener> eventListnerCaptor = ArgumentCaptor.forClass(ISameStruct1InterfaceEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
    @Test
    public void onReceiveprop1PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SameStruct1InterfaceMessageType.PROP_Prop1.getValue());
        Bundle data = new Bundle();
        Struct1 testprop1 = TbSame1TestHelper.makeTestStruct1();
		data.putParcelable("prop1", new Struct1Parcelable(testprop1));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setProp1( any(Struct1.class));
	    
    }

    @Test
     public void whenNotifiedprop1()
    {
        Struct1 testprop1 = TbSame1TestHelper.makeTestStruct1();

        testedAdapterAsEventListener.onProp1Changed(testprop1);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SameStruct1InterfaceMessageType.SET_Prop1.getValue(), response.what);
        Bundle data = response.getData();

        
			Struct1 receivedprop1 = data.getParcelable("prop1", Struct1Parcelable.class).getStruct1();

        assertEquals(receivedprop1, testprop1);
    }
    @Test
    public void whenNotifiedsig1()
    {
        Struct1 testparam1 = TbSame1TestHelper.makeTestStruct1();

        testedAdapterAsEventListener.onSig1(testparam1);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SameStruct1InterfaceMessageType.SIG_Sig1.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(Struct1Parcelable.class.getClassLoader());
        
			Struct1 receivedparam1 = data.getParcelable("param1", Struct1Parcelable.class).getStruct1();
        assertEquals(receivedparam1, testparam1);
}


    public void onfunc1Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SameStruct1InterfaceMessageType.RPC_Func1Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        Struct1 testparam1 = TbSame1TestHelper.makeTestStruct1();
		data.putParcelable("param1", new Struct1Parcelable(testparam1));
        Struct1 returnedValue = TbSame1TestHelper.makeTestStruct1();


        when(backendServiceMock.func1( any(Struct1.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).func1( any(Struct1.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SameStruct1InterfaceMessageType.RPC_Func1Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(Struct1Parcelable.class.getClassLoader());
		Struct1 receivedByClient = resp_data.getParcelable("result", Struct1Parcelable.class).getStruct1();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
