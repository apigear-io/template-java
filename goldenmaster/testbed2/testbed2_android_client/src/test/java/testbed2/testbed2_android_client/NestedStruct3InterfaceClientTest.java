//TODO later// Copyright Epic Games, Inc. All Rights Reserved.
package testbed2.testbed2_android_client;

import testbed2.testbed2_android_client.NestedStruct3InterfaceClient;

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

import testbed2.testbed2_api.INestedStruct3InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct3Interface;
import testbed2.testbed2_api.AbstractNestedStruct3Interface;
import testbed2.testbed2_android_messenger.NestedStruct3InterfaceMessageType;

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


interface INestedStruct3InterfaceClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class NestedStruct3InterfaceClientTest
{

    @Mock
    private Context mMockContext;
   
    private NestedStruct3InterfaceClient testedClient;
    private INestedStruct3InterfaceEventListener listenerMock = mock(INestedStruct3InterfaceEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private INestedStruct3InterfaceClientMessageGetter serviceMessagesStorage = mock(INestedStruct3InterfaceClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(NestedStruct3InterfaceMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(INestedStruct3InterfaceClientMessageGetter messageGetterMock)
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


        testedClient = new NestedStruct3InterfaceClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("testbed2.testbed2_android_service", "testbed2.testbed2_android_service.NestedStruct3InterfaceServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(NestedStruct3InterfaceMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.INIT.getValue());
        Bundle data = new Bundle();
        NestedStruct1 testprop1 = Testbed2TestHelper.makeTestNestedStruct1();
		data.putParcelable("prop1", new NestedStruct1Parcelable(testprop1));
        NestedStruct2 testprop2 = Testbed2TestHelper.makeTestNestedStruct2();
		data.putParcelable("prop2", new NestedStruct2Parcelable(testprop2));
        NestedStruct3 testprop3 = Testbed2TestHelper.makeTestNestedStruct3();
		data.putParcelable("prop3", new NestedStruct3Parcelable(testprop3));

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onProp1Changed(any(NestedStruct1.class));
        inOrderEventListener.verify(listenerMock,times(1)).onProp2Changed(any(NestedStruct2.class));
        inOrderEventListener.verify(listenerMock,times(1)).onProp3Changed(any(NestedStruct3.class));
    }
    @Test
    public void onReceiveprop1PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.SET_Prop1.getValue());
        Bundle data = new Bundle();
        NestedStruct1 testprop1 = Testbed2TestHelper.makeTestNestedStruct1();
		data.putParcelable("prop1", new NestedStruct1Parcelable(testprop1));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onProp1Changed(any(NestedStruct1.class));	    
    }
    
    @Test
     public void setPropertyRequestprop1()
    {
        NestedStruct1 testprop1 = Testbed2TestHelper.makeTestNestedStruct1();

        testedClient.setProp1(testprop1);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NestedStruct3InterfaceMessageType.PROP_Prop1.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
        
			NestedStruct1 receivedprop1 = data.getParcelable("prop1", NestedStruct1Parcelable.class).getNestedStruct1();
        assertEquals(receivedprop1, testprop1);
    }
    
    @Test
    public void onReceiveprop2PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.SET_Prop2.getValue());
        Bundle data = new Bundle();
        NestedStruct2 testprop2 = Testbed2TestHelper.makeTestNestedStruct2();
		data.putParcelable("prop2", new NestedStruct2Parcelable(testprop2));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onProp2Changed(any(NestedStruct2.class));	    
    }
    
    @Test
     public void setPropertyRequestprop2()
    {
        NestedStruct2 testprop2 = Testbed2TestHelper.makeTestNestedStruct2();

        testedClient.setProp2(testprop2);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NestedStruct3InterfaceMessageType.PROP_Prop2.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(NestedStruct2Parcelable.class.getClassLoader());
        
			NestedStruct2 receivedprop2 = data.getParcelable("prop2", NestedStruct2Parcelable.class).getNestedStruct2();
        assertEquals(receivedprop2, testprop2);
    }
    
    @Test
    public void onReceiveprop3PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.SET_Prop3.getValue());
        Bundle data = new Bundle();
        NestedStruct3 testprop3 = Testbed2TestHelper.makeTestNestedStruct3();
		data.putParcelable("prop3", new NestedStruct3Parcelable(testprop3));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onProp3Changed(any(NestedStruct3.class));	    
    }
    
    @Test
     public void setPropertyRequestprop3()
    {
        NestedStruct3 testprop3 = Testbed2TestHelper.makeTestNestedStruct3();

        testedClient.setProp3(testprop3);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NestedStruct3InterfaceMessageType.PROP_Prop3.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(NestedStruct3Parcelable.class.getClassLoader());
        
			NestedStruct3 receivedprop3 = data.getParcelable("prop3", NestedStruct3Parcelable.class).getNestedStruct3();
        assertEquals(receivedprop3, testprop3);
    }
    
    @Test
    public void whenNotifiedsig1() throws RemoteException
    {

        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.SIG_Sig1.getValue());
        Bundle data = new Bundle();
        NestedStruct1 testparam1 = Testbed2TestHelper.makeTestNestedStruct1();
		data.putParcelable("param1", new NestedStruct1Parcelable(testparam1));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSig1( any(NestedStruct1.class));

}
    @Test
    public void whenNotifiedsig2() throws RemoteException
    {

        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.SIG_Sig2.getValue());
        Bundle data = new Bundle();
        NestedStruct1 testparam1 = Testbed2TestHelper.makeTestNestedStruct1();
		data.putParcelable("param1", new NestedStruct1Parcelable(testparam1));
        NestedStruct2 testparam2 = Testbed2TestHelper.makeTestNestedStruct2();
		data.putParcelable("param2", new NestedStruct2Parcelable(testparam2));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSig2( any(NestedStruct1.class),  any(NestedStruct2.class));

}
    @Test
    public void whenNotifiedsig3() throws RemoteException
    {

        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.SIG_Sig3.getValue());
        Bundle data = new Bundle();
        NestedStruct1 testparam1 = Testbed2TestHelper.makeTestNestedStruct1();
		data.putParcelable("param1", new NestedStruct1Parcelable(testparam1));
        NestedStruct2 testparam2 = Testbed2TestHelper.makeTestNestedStruct2();
		data.putParcelable("param2", new NestedStruct2Parcelable(testparam2));
        NestedStruct3 testparam3 = Testbed2TestHelper.makeTestNestedStruct3();
		data.putParcelable("param3", new NestedStruct3Parcelable(testparam3));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSig3( any(NestedStruct1.class),  any(NestedStruct2.class),  any(NestedStruct3.class));

}


    public void onfunc1Request() throws RemoteException {

        // Execute method
        NestedStruct1 testparam1 = Testbed2TestHelper.makeTestNestedStruct1();
        NestedStruct1 expectedResult = Testbed2TestHelper.makeTestNestedStruct1();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<NestedStruct1> resFuture = testedClient.func1Async(testparam1);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(NestedStruct3InterfaceMessageType.RPC_Func1Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
        
			NestedStruct1 receivedparam1 = data.getParcelable("param1", NestedStruct1Parcelable.class).getNestedStruct1();
        assertEquals(receivedparam1, testparam1);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.RPC_Func1Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new NestedStruct1Parcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfunc2Request() throws RemoteException {

        // Execute method
        NestedStruct1 testparam1 = Testbed2TestHelper.makeTestNestedStruct1();
        NestedStruct2 testparam2 = Testbed2TestHelper.makeTestNestedStruct2();
        NestedStruct1 expectedResult = Testbed2TestHelper.makeTestNestedStruct1();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<NestedStruct1> resFuture = testedClient.func2Async(testparam1, testparam2);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(NestedStruct3InterfaceMessageType.RPC_Func2Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
        
			NestedStruct1 receivedparam1 = data.getParcelable("param1", NestedStruct1Parcelable.class).getNestedStruct1();
        assertEquals(receivedparam1, testparam1);
        
			NestedStruct2 receivedparam2 = data.getParcelable("param2", NestedStruct2Parcelable.class).getNestedStruct2();
        assertEquals(receivedparam2, testparam2);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.RPC_Func2Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new NestedStruct1Parcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfunc3Request() throws RemoteException {

        // Execute method
        NestedStruct1 testparam1 = Testbed2TestHelper.makeTestNestedStruct1();
        NestedStruct2 testparam2 = Testbed2TestHelper.makeTestNestedStruct2();
        NestedStruct3 testparam3 = Testbed2TestHelper.makeTestNestedStruct3();
        NestedStruct1 expectedResult = Testbed2TestHelper.makeTestNestedStruct1();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<NestedStruct1> resFuture = testedClient.func3Async(testparam1, testparam2, testparam3);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(NestedStruct3InterfaceMessageType.RPC_Func3Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
        
			NestedStruct1 receivedparam1 = data.getParcelable("param1", NestedStruct1Parcelable.class).getNestedStruct1();
        assertEquals(receivedparam1, testparam1);
        
			NestedStruct2 receivedparam2 = data.getParcelable("param2", NestedStruct2Parcelable.class).getNestedStruct2();
        assertEquals(receivedparam2, testparam2);
        
			NestedStruct3 receivedparam3 = data.getParcelable("param3", NestedStruct3Parcelable.class).getNestedStruct3();
        assertEquals(receivedparam3, testparam3);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, NestedStruct3InterfaceMessageType.RPC_Func3Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new NestedStruct1Parcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
