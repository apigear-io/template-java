// Copyright Epic Games, Inc. All Rights Reserved.
package testbed2.testbed2_android_client;

import testbed2.testbed2_android_client.NestedStruct1InterfaceClient;

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

import testbed2.testbed2_api.INestedStruct1InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_api.AbstractNestedStruct1Interface;
import testbed2.testbed2_android_messenger.NestedStruct1InterfaceMessageType;

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


interface INestedStruct1InterfaceClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class NestedStruct1InterfaceClientTest
{

    @Mock
    private Context mMockContext;
   
    private NestedStruct1InterfaceClient testedClient;
    private INestedStruct1InterfaceEventListener listenerMock = mock(INestedStruct1InterfaceEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private INestedStruct1InterfaceClientMessageGetter serviceMessagesStorage = mock(INestedStruct1InterfaceClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(NestedStruct1InterfaceMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(INestedStruct1InterfaceClientMessageGetter messageGetterMock)
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


        testedClient = new NestedStruct1InterfaceClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("testbed2.testbed2_android_service", "testbed2.testbed2_android_service.NestedStruct1InterfaceServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(NestedStruct1InterfaceMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, NestedStruct1InterfaceMessageType.INIT.getValue());
        Bundle data = new Bundle();
        NestedStruct1 testprop1 = Testbed2TestHelper.makeTestNestedStruct1();
		data.putParcelable("prop1", new NestedStruct1Parcelable(testprop1));

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onProp1Changed(any(NestedStruct1.class));
    }
    @Test
    public void onReceiveprop1PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NestedStruct1InterfaceMessageType.SET_Prop1.getValue());
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

        assertEquals(NestedStruct1InterfaceMessageType.PROP_Prop1.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
        
			NestedStruct1 receivedprop1 = data.getParcelable("prop1", NestedStruct1Parcelable.class).getNestedStruct1();
        assertEquals(receivedprop1, testprop1);
    }
    
    @Test
    public void whenNotifiedsig1() throws RemoteException
    {

        Message msg = Message.obtain(null, NestedStruct1InterfaceMessageType.SIG_Sig1.getValue());
        Bundle data = new Bundle();
        NestedStruct1 testparam1 = Testbed2TestHelper.makeTestNestedStruct1();
		data.putParcelable("param1", new NestedStruct1Parcelable(testparam1));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSig1( any(NestedStruct1.class));

}

    @Test
    public void onfuncNoReturnValueRequest() throws RemoteException {

        // Execute method
        NestedStruct1 testparam1 = Testbed2TestHelper.makeTestNestedStruct1();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Void> resFuture = testedClient.funcNoReturnValueAsync(testparam1);

        resFuture.thenAccept(result -> {
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(NestedStruct1InterfaceMessageType.RPC_FuncNoReturnValueReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
        
			NestedStruct1 receivedparam1 = data.getParcelable("param1", NestedStruct1Parcelable.class).getNestedStruct1();
        assertEquals(receivedparam1, testparam1);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, NestedStruct1InterfaceMessageType.RPC_FuncNoReturnValueResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncNoParamsRequest() throws RemoteException {

        // Execute method
        NestedStruct1 expectedResult = Testbed2TestHelper.makeTestNestedStruct1();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<NestedStruct1> resFuture = testedClient.funcNoParamsAsync();

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(NestedStruct1InterfaceMessageType.RPC_FuncNoParamsReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, NestedStruct1InterfaceMessageType.RPC_FuncNoParamsResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new NestedStruct1Parcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
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
        assertEquals(NestedStruct1InterfaceMessageType.RPC_Func1Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
        
			NestedStruct1 receivedparam1 = data.getParcelable("param1", NestedStruct1Parcelable.class).getNestedStruct1();
        assertEquals(receivedparam1, testparam1);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, NestedStruct1InterfaceMessageType.RPC_Func1Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new NestedStruct1Parcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
