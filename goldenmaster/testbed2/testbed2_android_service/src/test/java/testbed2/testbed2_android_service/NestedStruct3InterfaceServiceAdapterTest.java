//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

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
import testbed2.testbed2_android_service.NestedStruct3InterfaceServiceAdapter;

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


import testbed2.testbed2_api.INestedStruct3InterfaceEventListener;
import testbed2.testbed2_android_service.INestedStruct3InterfaceServiceFactory;
import testbed2.testbed2_api.INestedStruct3Interface;
import testbed2.testbed2_api.AbstractNestedStruct3Interface;
import testbed2.testbed2_android_messenger.NestedStruct3InterfaceMessageType;


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

interface INestedStruct3InterfaceMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class NestedStruct3InterfaceServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private NestedStruct3InterfaceServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private INestedStruct3InterfaceEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private INestedStruct3Interface backendServiceMock = mock(INestedStruct3Interface.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private INestedStruct3InterfaceServiceFactory serviceFactory = mock(INestedStruct3InterfaceServiceFactory.class);
    private INestedStruct3InterfaceMessageGetter clientMessagesStorage = mock(INestedStruct3InterfaceMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, NestedStruct3InterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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

    Handler createClientHandlerMock(INestedStruct3InterfaceMessageGetter messageGetterMock)
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
        NestedStruct1 initprop1 = new NestedStruct1();
        //TODO fill fields
        when(backendServiceMock.getProp1()).thenReturn(initprop1);
        NestedStruct2 initprop2 = new NestedStruct2();
        //TODO fill fields
        when(backendServiceMock.getProp2()).thenReturn(initprop2);
        NestedStruct3 initprop3 = new NestedStruct3();
        //TODO fill fields
        when(backendServiceMock.getProp3()).thenReturn(initprop3);


        Message registerMsg = Message.obtain(null, NestedStruct3InterfaceMessageType.REGISTER_CLIENT.ordinal());
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

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NestedStruct3InterfaceMessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        
			NestedStruct1 receivedprop1 = data.getParcelable("prop1", NestedStruct1Parcelable.class).getNestedStruct1();
        
			NestedStruct2 receivedprop2 = data.getParcelable("prop2", NestedStruct2Parcelable.class).getNestedStruct2();
        
			NestedStruct3 receivedprop3 = data.getParcelable("prop3", NestedStruct3Parcelable.class).getNestedStruct3();
        
        data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
        // assertEquals(receivedprop1, initprop1);
        // assertEquals(receivedprop2, initprop2);
        // assertEquals(receivedprop3, initprop3);

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

        testedServiceAdapterIntent = new Intent(mMockContext, NestedStruct3InterfaceServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(NestedStruct3InterfaceServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(serviceFactory.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(serviceFactory);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<INestedStruct3InterfaceEventListener> eventListnerCaptor = ArgumentCaptor.forClass(INestedStruct3InterfaceEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
    @Test
    public void onReceiveprop1PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.PROP_Prop1.getValue());
        Bundle data = new Bundle();
        NestedStruct1 testprop1 = Testbed2TestHelper.makeTestNestedStruct1();
		data.putParcelable("prop1", new NestedStruct1Parcelable(testprop1));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setProp1( any(NestedStruct1.class));
	    
    }

    @Test
     public void whenNotifiedprop1()
    {
        NestedStruct1 testprop1 = Testbed2TestHelper.makeTestNestedStruct1();

        testedAdapterAsEventListener.onProp1Changed(testprop1);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NestedStruct3InterfaceMessageType.SET_Prop1.getValue(), response.what);
        Bundle data = response.getData();

        
			NestedStruct1 receivedprop1 = data.getParcelable("prop1", NestedStruct1Parcelable.class).getNestedStruct1();

        assertEquals(receivedprop1, testprop1);
    }
    @Test
    public void onReceiveprop2PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.PROP_Prop2.getValue());
        Bundle data = new Bundle();
        NestedStruct2 testprop2 = Testbed2TestHelper.makeTestNestedStruct2();
		data.putParcelable("prop2", new NestedStruct2Parcelable(testprop2));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setProp2( any(NestedStruct2.class));
	    
    }

    @Test
     public void whenNotifiedprop2()
    {
        NestedStruct2 testprop2 = Testbed2TestHelper.makeTestNestedStruct2();

        testedAdapterAsEventListener.onProp2Changed(testprop2);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NestedStruct3InterfaceMessageType.SET_Prop2.getValue(), response.what);
        Bundle data = response.getData();

        
			NestedStruct2 receivedprop2 = data.getParcelable("prop2", NestedStruct2Parcelable.class).getNestedStruct2();

        assertEquals(receivedprop2, testprop2);
    }
    @Test
    public void onReceiveprop3PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.PROP_Prop3.getValue());
        Bundle data = new Bundle();
        NestedStruct3 testprop3 = Testbed2TestHelper.makeTestNestedStruct3();
		data.putParcelable("prop3", new NestedStruct3Parcelable(testprop3));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setProp3( any(NestedStruct3.class));
	    
    }

    @Test
     public void whenNotifiedprop3()
    {
        NestedStruct3 testprop3 = Testbed2TestHelper.makeTestNestedStruct3();

        testedAdapterAsEventListener.onProp3Changed(testprop3);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NestedStruct3InterfaceMessageType.SET_Prop3.getValue(), response.what);
        Bundle data = response.getData();

        
			NestedStruct3 receivedprop3 = data.getParcelable("prop3", NestedStruct3Parcelable.class).getNestedStruct3();

        assertEquals(receivedprop3, testprop3);
    }
    @Test
    public void whenNotifiedsig1()
    {
        NestedStruct1 testparam1 = Testbed2TestHelper.makeTestNestedStruct1();

        testedAdapterAsEventListener.onSig1(testparam1);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NestedStruct3InterfaceMessageType.SIG_Sig1.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
        
			NestedStruct1 receivedparam1 = data.getParcelable("param1", NestedStruct1Parcelable.class).getNestedStruct1();
        assertEquals(receivedparam1, testparam1);
}
    @Test
    public void whenNotifiedsig2()
    {
        NestedStruct1 testparam1 = Testbed2TestHelper.makeTestNestedStruct1();
        NestedStruct2 testparam2 = Testbed2TestHelper.makeTestNestedStruct2();

        testedAdapterAsEventListener.onSig2(testparam1, testparam2);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NestedStruct3InterfaceMessageType.SIG_Sig2.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
        
			NestedStruct1 receivedparam1 = data.getParcelable("param1", NestedStruct1Parcelable.class).getNestedStruct1();
        assertEquals(receivedparam1, testparam1);
        
			NestedStruct2 receivedparam2 = data.getParcelable("param2", NestedStruct2Parcelable.class).getNestedStruct2();
        assertEquals(receivedparam2, testparam2);
}
    @Test
    public void whenNotifiedsig3()
    {
        NestedStruct1 testparam1 = Testbed2TestHelper.makeTestNestedStruct1();
        NestedStruct2 testparam2 = Testbed2TestHelper.makeTestNestedStruct2();
        NestedStruct3 testparam3 = Testbed2TestHelper.makeTestNestedStruct3();

        testedAdapterAsEventListener.onSig3(testparam1, testparam2, testparam3);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NestedStruct3InterfaceMessageType.SIG_Sig3.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
        
			NestedStruct1 receivedparam1 = data.getParcelable("param1", NestedStruct1Parcelable.class).getNestedStruct1();
        assertEquals(receivedparam1, testparam1);
        
			NestedStruct2 receivedparam2 = data.getParcelable("param2", NestedStruct2Parcelable.class).getNestedStruct2();
        assertEquals(receivedparam2, testparam2);
        
			NestedStruct3 receivedparam3 = data.getParcelable("param3", NestedStruct3Parcelable.class).getNestedStruct3();
        assertEquals(receivedparam3, testparam3);
}


    public void onfunc1Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.RPC_Func1Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        NestedStruct1 testparam1 = Testbed2TestHelper.makeTestNestedStruct1();
		data.putParcelable("param1", new NestedStruct1Parcelable(testparam1));
        NestedStruct1 returnedValue = Testbed2TestHelper.makeTestNestedStruct1();


        when(backendServiceMock.func1( any(NestedStruct1.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).func1( any(NestedStruct1.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NestedStruct3InterfaceMessageType.RPC_Func1Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
		NestedStruct1 receivedByClient = resp_data.getParcelable("result", NestedStruct1Parcelable.class).getNestedStruct1();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfunc2Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.RPC_Func2Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        NestedStruct1 testparam1 = Testbed2TestHelper.makeTestNestedStruct1();
		data.putParcelable("param1", new NestedStruct1Parcelable(testparam1));
        NestedStruct2 testparam2 = Testbed2TestHelper.makeTestNestedStruct2();
		data.putParcelable("param2", new NestedStruct2Parcelable(testparam2));
        NestedStruct1 returnedValue = Testbed2TestHelper.makeTestNestedStruct1();


        when(backendServiceMock.func2( any(NestedStruct1.class),  any(NestedStruct2.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).func2( any(NestedStruct1.class),  any(NestedStruct2.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NestedStruct3InterfaceMessageType.RPC_Func2Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
		NestedStruct1 receivedByClient = resp_data.getParcelable("result", NestedStruct1Parcelable.class).getNestedStruct1();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfunc3Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.RPC_Func3Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        NestedStruct1 testparam1 = Testbed2TestHelper.makeTestNestedStruct1();
		data.putParcelable("param1", new NestedStruct1Parcelable(testparam1));
        NestedStruct2 testparam2 = Testbed2TestHelper.makeTestNestedStruct2();
		data.putParcelable("param2", new NestedStruct2Parcelable(testparam2));
        NestedStruct3 testparam3 = Testbed2TestHelper.makeTestNestedStruct3();
		data.putParcelable("param3", new NestedStruct3Parcelable(testparam3));
        NestedStruct1 returnedValue = Testbed2TestHelper.makeTestNestedStruct1();


        when(backendServiceMock.func3( any(NestedStruct1.class),  any(NestedStruct2.class),  any(NestedStruct3.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).func3( any(NestedStruct1.class),  any(NestedStruct2.class),  any(NestedStruct3.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NestedStruct3InterfaceMessageType.RPC_Func3Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
		NestedStruct1 receivedByClient = resp_data.getParcelable("result", NestedStruct1Parcelable.class).getNestedStruct1();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
