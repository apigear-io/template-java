//TODO later// Copyright Epic Games, Inc. All Rights Reserved.
package testbed2.testbed2_android_client;

import testbed2.testbed2_android_client.ManyParamInterfaceClient;

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
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_api.AbstractManyParamInterface;
import testbed2.testbed2_android_messenger.ManyParamInterfaceMessageType;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import testbed2.testbed2_android_messenger.Conversions;
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


interface IManyParamInterfaceClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ManyParamInterfaceClientTest
{

    @Mock
    private Context mMockContext;
   
    private ManyParamInterfaceClient testedClient;
    private IManyParamInterfaceEventListener listenerMock = mock(IManyParamInterfaceEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private IManyParamInterfaceClientMessageGetter serviceMessagesStorage = mock(IManyParamInterfaceClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(ManyParamInterfaceMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(IManyParamInterfaceClientMessageGetter messageGetterMock)
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


        testedClient = new ManyParamInterfaceClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("testbed2.testbed2_android_service", "testbed2.testbed2_android_service.ManyParamInterfaceServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(ManyParamInterfaceMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.INIT.getValue());
        Bundle data = new Bundle();
		int testprop1 = 1;
		data.putInt("prop1", testprop1);
		int testprop2 = 1;
		data.putInt("prop2", testprop2);
		int testprop3 = 1;
		data.putInt("prop3", testprop3);
		int testprop4 = 1;
		data.putInt("prop4", testprop4);

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onProp1Changed(testprop1);
		inOrderEventListener.verify(listenerMock,times(1)).onProp2Changed(testprop2);
		inOrderEventListener.verify(listenerMock,times(1)).onProp3Changed(testprop3);
		inOrderEventListener.verify(listenerMock,times(1)).onProp4Changed(testprop4);
    }
    @Test
    public void onReceiveprop1PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.SET_Prop1.getValue());
        Bundle data = new Bundle();
		int testprop1 = 1;
		data.putInt("prop1", testprop1);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onProp1Changed(testprop1);	    
    }
    
    @Test
     public void setPropertyRequestprop1()
    {
		int testprop1 = 1;

        testedClient.setProp1(testprop1);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.PROP_Prop1.getValue(), response.what);
        Bundle data = response.getData();
        
			int receivedprop1 = data.getInt("prop1", 0);
        assertEquals(receivedprop1, testprop1);
    }
    
    @Test
    public void onReceiveprop2PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.SET_Prop2.getValue());
        Bundle data = new Bundle();
		int testprop2 = 1;
		data.putInt("prop2", testprop2);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onProp2Changed(testprop2);	    
    }
    
    @Test
     public void setPropertyRequestprop2()
    {
		int testprop2 = 1;

        testedClient.setProp2(testprop2);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.PROP_Prop2.getValue(), response.what);
        Bundle data = response.getData();
        
			int receivedprop2 = data.getInt("prop2", 0);
        assertEquals(receivedprop2, testprop2);
    }
    
    @Test
    public void onReceiveprop3PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.SET_Prop3.getValue());
        Bundle data = new Bundle();
		int testprop3 = 1;
		data.putInt("prop3", testprop3);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onProp3Changed(testprop3);	    
    }
    
    @Test
     public void setPropertyRequestprop3()
    {
		int testprop3 = 1;

        testedClient.setProp3(testprop3);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.PROP_Prop3.getValue(), response.what);
        Bundle data = response.getData();
        
			int receivedprop3 = data.getInt("prop3", 0);
        assertEquals(receivedprop3, testprop3);
    }
    
    @Test
    public void onReceiveprop4PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.SET_Prop4.getValue());
        Bundle data = new Bundle();
		int testprop4 = 1;
		data.putInt("prop4", testprop4);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onProp4Changed(testprop4);	    
    }
    
    @Test
     public void setPropertyRequestprop4()
    {
		int testprop4 = 1;

        testedClient.setProp4(testprop4);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ManyParamInterfaceMessageType.PROP_Prop4.getValue(), response.what);
        Bundle data = response.getData();
        
			int receivedprop4 = data.getInt("prop4", 0);
        assertEquals(receivedprop4, testprop4);
    }
    
    @Test
    public void whenNotifiedsig1() throws RemoteException
    {

        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.SIG_Sig1.getValue());
        Bundle data = new Bundle();
		int testparam1 = 1;
		data.putInt("param1", testparam1);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSig1(testparam1);

}
    @Test
    public void whenNotifiedsig2() throws RemoteException
    {

        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.SIG_Sig2.getValue());
        Bundle data = new Bundle();
		int testparam1 = 1;
		data.putInt("param1", testparam1);
		int testparam2 = 1;
		data.putInt("param2", testparam2);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSig2(testparam1, testparam2);

}
    @Test
    public void whenNotifiedsig3() throws RemoteException
    {

        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.SIG_Sig3.getValue());
        Bundle data = new Bundle();
		int testparam1 = 1;
		data.putInt("param1", testparam1);
		int testparam2 = 1;
		data.putInt("param2", testparam2);
		int testparam3 = 1;
		data.putInt("param3", testparam3);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSig3(testparam1, testparam2, testparam3);

}
    @Test
    public void whenNotifiedsig4() throws RemoteException
    {

        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.SIG_Sig4.getValue());
        Bundle data = new Bundle();
		int testparam1 = 1;
		data.putInt("param1", testparam1);
		int testparam2 = 1;
		data.putInt("param2", testparam2);
		int testparam3 = 1;
		data.putInt("param3", testparam3);
		int testparam4 = 1;
		data.putInt("param4", testparam4);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSig4(testparam1, testparam2, testparam3, testparam4);

}

    @Test
    public void onfunc1Request() throws RemoteException {

        // Execute method
		int testparam1 = 1;
        int expectedResult = 1;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Integer> resFuture = testedClient.func1Async(testparam1);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result.intValue());
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(ManyParamInterfaceMessageType.RPC_Func1Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			int receivedparam1 = data.getInt("param1", 0);
        assertEquals(receivedparam1, testparam1);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.RPC_Func1Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putInt("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfunc2Request() throws RemoteException {

        // Execute method
		int testparam1 = 1;
		int testparam2 = 1;
        int expectedResult = 1;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Integer> resFuture = testedClient.func2Async(testparam1, testparam2);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result.intValue());
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(ManyParamInterfaceMessageType.RPC_Func2Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			int receivedparam1 = data.getInt("param1", 0);
        assertEquals(receivedparam1, testparam1);
        
			int receivedparam2 = data.getInt("param2", 0);
        assertEquals(receivedparam2, testparam2);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.RPC_Func2Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putInt("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfunc3Request() throws RemoteException {

        // Execute method
		int testparam1 = 1;
		int testparam2 = 1;
		int testparam3 = 1;
        int expectedResult = 1;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Integer> resFuture = testedClient.func3Async(testparam1, testparam2, testparam3);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result.intValue());
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(ManyParamInterfaceMessageType.RPC_Func3Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			int receivedparam1 = data.getInt("param1", 0);
        assertEquals(receivedparam1, testparam1);
        
			int receivedparam2 = data.getInt("param2", 0);
        assertEquals(receivedparam2, testparam2);
        
			int receivedparam3 = data.getInt("param3", 0);
        assertEquals(receivedparam3, testparam3);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.RPC_Func3Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putInt("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfunc4Request() throws RemoteException {

        // Execute method
		int testparam1 = 1;
		int testparam2 = 1;
		int testparam3 = 1;
		int testparam4 = 1;
        int expectedResult = 1;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Integer> resFuture = testedClient.func4Async(testparam1, testparam2, testparam3, testparam4);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result.intValue());
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(ManyParamInterfaceMessageType.RPC_Func4Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			int receivedparam1 = data.getInt("param1", 0);
        assertEquals(receivedparam1, testparam1);
        
			int receivedparam2 = data.getInt("param2", 0);
        assertEquals(receivedparam2, testparam2);
        
			int receivedparam3 = data.getInt("param3", 0);
        assertEquals(receivedparam3, testparam3);
        
			int receivedparam4 = data.getInt("param4", 0);
        assertEquals(receivedparam4, testparam4);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, ManyParamInterfaceMessageType.RPC_Func4Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putInt("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
