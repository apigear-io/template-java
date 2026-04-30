// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2_android_service;

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
import testbed2.testbed2_android_service.ManyParamInterfaceServiceAdapter;
import testbed2.testbed2_android_service.IManyParamInterfaceServiceProvider;

//import message type and parcelabe types
import testbed2.testbed2_api.Struct1;
import testbed2.testbed2_android_messenger.Struct1Parcelable;
import testbed2.testbed2_api.Struct2;
import testbed2.testbed2_android_messenger.Struct2Parcelable;
import testbed2.testbed2_api.Struct3;
import testbed2.testbed2_android_messenger.Struct3Parcelable;
import testbed2.testbed2_api.Struct4;
import testbed2.testbed2_android_messenger.Struct4Parcelable;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_android_messenger.NestedStruct1Parcelable;
import testbed2.testbed2_api.NestedStruct2;
import testbed2.testbed2_android_messenger.NestedStruct2Parcelable;
import testbed2.testbed2_api.NestedStruct3;
import testbed2.testbed2_android_messenger.NestedStruct3Parcelable;
import testbed2.testbed2_api.Testbed2TestHelper;
import testbed2.testbed2_api.Enum1;
import testbed2.testbed2_android_messenger.Enum1Parcelable;
import testbed2.testbed2_api.Enum2;
import testbed2.testbed2_android_messenger.Enum2Parcelable;
import testbed2.testbed2_api.Enum3;
import testbed2.testbed2_android_messenger.Enum3Parcelable;
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_android_messenger.ManyParamInterfaceParcelable;
import testbed2.testbed2_impl.ManyParamInterfaceService;
import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_android_messenger.NestedStruct1InterfaceParcelable;
import testbed2.testbed2_impl.NestedStruct1InterfaceService;
import testbed2.testbed2_api.INestedStruct2Interface;
import testbed2.testbed2_android_messenger.NestedStruct2InterfaceParcelable;
import testbed2.testbed2_impl.NestedStruct2InterfaceService;
import testbed2.testbed2_api.INestedStruct3Interface;
import testbed2.testbed2_android_messenger.NestedStruct3InterfaceParcelable;
import testbed2.testbed2_impl.NestedStruct3InterfaceService;


import testbed2.testbed2_api.IManyParamInterfaceEventListener;
import testbed2.testbed2_android_service.IManyParamInterfaceServiceProvider;
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_api.AbstractManyParamInterface;
import testbed2.testbed2_android_messenger.ManyParamInterfaceMessageType;


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

interface IManyParamInterfaceMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ManyParamInterfaceServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private ManyParamInterfaceServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private IManyParamInterfaceEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private IManyParamInterface backendServiceMock = mock(IManyParamInterface.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private IManyParamInterfaceServiceProvider ServiceProvider = mock(IManyParamInterfaceServiceProvider.class);
    private IManyParamInterfaceMessageGetter clientMessagesStorage = mock(IManyParamInterfaceMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, ManyParamInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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

    Handler createClientHandlerMock(IManyParamInterfaceMessageGetter messageGetterMock)
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
		int initprop1 = 1;
        when(backendServiceMock.getProp1()).thenReturn(initprop1);
		int initprop2 = 1;
        when(backendServiceMock.getProp2()).thenReturn(initprop2);
		int initprop3 = 1;
        when(backendServiceMock.getProp3()).thenReturn(initprop3);
		int initprop4 = 1;
        when(backendServiceMock.getProp4()).thenReturn(initprop4);


        Message registerMsg = Message.obtain(null, ManyParamInterfaceMessageType.REGISTER_CLIENT.ordinal());
        registerMsg.getData().putString("connectionID", id);
        registerMsg.replyTo = messenger;
    
        try {
            mServiceMessenger.send(registerMsg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock, times(1)).getProp1();
        inOrderBackendService.verify(backendServiceMock, times(1)).getProp2();
        inOrderBackendService.verify(backendServiceMock, times(1)).getProp3();
        inOrderBackendService.verify(backendServiceMock, times(1)).getProp4();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        
			int receivedprop1 = data.getInt("prop1", 0);
        
			int receivedprop2 = data.getInt("prop2", 0);
        
			int receivedprop3 = data.getInt("prop3", 0);
        
			int receivedprop4 = data.getInt("prop4", 0);
        
        assertEquals(receivedprop1, initprop1);
        assertEquals(receivedprop2, initprop2);
        assertEquals(receivedprop3, initprop3);
        assertEquals(receivedprop4, initprop4);

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

        testedServiceAdapterIntent = new Intent(mMockContext, ManyParamInterfaceServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(ManyParamInterfaceServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(ServiceProvider.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(ServiceProvider);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<IManyParamInterfaceEventListener> eventListnerCaptor = ArgumentCaptor.forClass(IManyParamInterfaceEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
    @Test
    public void onReceiveprop1PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.PROP_Prop1.getValue());
        Bundle data = new Bundle();
		int testprop1 = 1;
		data.putInt("prop1", testprop1);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setProp1(testprop1);
	    
    }

    @Test
     public void whenNotifiedprop1()
    {
		int testprop1 = 1;

        testedAdapterAsEventListener.onProp1Changed(testprop1);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.SET_Prop1.getValue(), response.what);
        Bundle data = response.getData();

        
			int receivedprop1 = data.getInt("prop1", 0);

        assertEquals(receivedprop1, testprop1);
    }
    @Test
    public void onReceiveprop2PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.PROP_Prop2.getValue());
        Bundle data = new Bundle();
		int testprop2 = 1;
		data.putInt("prop2", testprop2);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setProp2(testprop2);
	    
    }

    @Test
     public void whenNotifiedprop2()
    {
		int testprop2 = 1;

        testedAdapterAsEventListener.onProp2Changed(testprop2);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.SET_Prop2.getValue(), response.what);
        Bundle data = response.getData();

        
			int receivedprop2 = data.getInt("prop2", 0);

        assertEquals(receivedprop2, testprop2);
    }
    @Test
    public void onReceiveprop3PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.PROP_Prop3.getValue());
        Bundle data = new Bundle();
		int testprop3 = 1;
		data.putInt("prop3", testprop3);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setProp3(testprop3);
	    
    }

    @Test
     public void whenNotifiedprop3()
    {
		int testprop3 = 1;

        testedAdapterAsEventListener.onProp3Changed(testprop3);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.SET_Prop3.getValue(), response.what);
        Bundle data = response.getData();

        
			int receivedprop3 = data.getInt("prop3", 0);

        assertEquals(receivedprop3, testprop3);
    }
    @Test
    public void onReceiveprop4PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.PROP_Prop4.getValue());
        Bundle data = new Bundle();
		int testprop4 = 1;
		data.putInt("prop4", testprop4);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setProp4(testprop4);
	    
    }

    @Test
     public void whenNotifiedprop4()
    {
		int testprop4 = 1;

        testedAdapterAsEventListener.onProp4Changed(testprop4);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.SET_Prop4.getValue(), response.what);
        Bundle data = response.getData();

        
			int receivedprop4 = data.getInt("prop4", 0);

        assertEquals(receivedprop4, testprop4);
    }
    @Test
    public void whenNotifiedsig1()
    {
		int testparam1 = 1;

        testedAdapterAsEventListener.onSig1(testparam1);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.SIG_Sig1.getValue(), response.what);
        Bundle data = response.getData();
        
        
			int receivedparam1 = data.getInt("param1", 0);
        assertEquals(receivedparam1, testparam1);
}
    @Test
    public void whenNotifiedsig2()
    {
		int testparam1 = 1;
		int testparam2 = 1;

        testedAdapterAsEventListener.onSig2(testparam1, testparam2);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.SIG_Sig2.getValue(), response.what);
        Bundle data = response.getData();
        
        
			int receivedparam1 = data.getInt("param1", 0);
        assertEquals(receivedparam1, testparam1);
        
			int receivedparam2 = data.getInt("param2", 0);
        assertEquals(receivedparam2, testparam2);
}
    @Test
    public void whenNotifiedsig3()
    {
		int testparam1 = 1;
		int testparam2 = 1;
		int testparam3 = 1;

        testedAdapterAsEventListener.onSig3(testparam1, testparam2, testparam3);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.SIG_Sig3.getValue(), response.what);
        Bundle data = response.getData();
        
        
			int receivedparam1 = data.getInt("param1", 0);
        assertEquals(receivedparam1, testparam1);
        
			int receivedparam2 = data.getInt("param2", 0);
        assertEquals(receivedparam2, testparam2);
        
			int receivedparam3 = data.getInt("param3", 0);
        assertEquals(receivedparam3, testparam3);
}
    @Test
    public void whenNotifiedsig4()
    {
		int testparam1 = 1;
		int testparam2 = 1;
		int testparam3 = 1;
		int testparam4 = 1;

        testedAdapterAsEventListener.onSig4(testparam1, testparam2, testparam3, testparam4);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.SIG_Sig4.getValue(), response.what);
        Bundle data = response.getData();
        
        
			int receivedparam1 = data.getInt("param1", 0);
        assertEquals(receivedparam1, testparam1);
        
			int receivedparam2 = data.getInt("param2", 0);
        assertEquals(receivedparam2, testparam2);
        
			int receivedparam3 = data.getInt("param3", 0);
        assertEquals(receivedparam3, testparam3);
        
			int receivedparam4 = data.getInt("param4", 0);
        assertEquals(receivedparam4, testparam4);
}

    @Test
    public void onfunc1Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.RPC_Func1Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		int testparam1 = 1;
		data.putInt("param1", testparam1);
        int returnedValue = 1;


        when(backendServiceMock.func1(testparam1)).thenReturn(returnedValue);

        msg.setData(data);
        msg.replyTo = clientReplyMessenger;
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).func1(testparam1);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.RPC_Func1Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		int receivedByClient = resp_data.getInt("result", 0);

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onfunc2Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.RPC_Func2Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		int testparam1 = 1;
		data.putInt("param1", testparam1);
		int testparam2 = 1;
		data.putInt("param2", testparam2);
        int returnedValue = 1;


        when(backendServiceMock.func2(testparam1, testparam2)).thenReturn(returnedValue);

        msg.setData(data);
        msg.replyTo = clientReplyMessenger;
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).func2(testparam1, testparam2);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.RPC_Func2Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		int receivedByClient = resp_data.getInt("result", 0);

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onfunc3Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.RPC_Func3Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		int testparam1 = 1;
		data.putInt("param1", testparam1);
		int testparam2 = 1;
		data.putInt("param2", testparam2);
		int testparam3 = 1;
		data.putInt("param3", testparam3);
        int returnedValue = 1;


        when(backendServiceMock.func3(testparam1, testparam2, testparam3)).thenReturn(returnedValue);

        msg.setData(data);
        msg.replyTo = clientReplyMessenger;
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).func3(testparam1, testparam2, testparam3);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.RPC_Func3Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		int receivedByClient = resp_data.getInt("result", 0);

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onfunc4Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.RPC_Func4Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		int testparam1 = 1;
		data.putInt("param1", testparam1);
		int testparam2 = 1;
		data.putInt("param2", testparam2);
		int testparam3 = 1;
		data.putInt("param3", testparam3);
		int testparam4 = 1;
		data.putInt("param4", testparam4);
        int returnedValue = 1;


        when(backendServiceMock.func4(testparam1, testparam2, testparam3, testparam4)).thenReturn(returnedValue);

        msg.setData(data);
        msg.replyTo = clientReplyMessenger;
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).func4(testparam1, testparam2, testparam3, testparam4);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.RPC_Func4Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		int receivedByClient = resp_data.getInt("result", 0);

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
