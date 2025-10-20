//TODO later// Copyright Epic Games, Inc. All Rights Reserved.
package tbRefIfaces.tbRefIfaces_android_client;

import tbRefIfaces.tbRefIfaces_android_client.SimpleLocalIfClient;

//import message type and parcelabe types
import tbRefIfaces.tbRefIfaces_api.TbRefIfacesTestHelper;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.AbstractSimpleLocalIf;
import tbRefIfaces.tbRefIfaces_android_messenger.SimpleLocalIfMessageType;

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


interface ISimpleLocalIfClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SimpleLocalIfClientTest
{

    @Mock
    private Context mMockContext;
   
    private SimpleLocalIfClient testedClient;
    private ISimpleLocalIfEventListener listenerMock = mock(ISimpleLocalIfEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private ISimpleLocalIfClientMessageGetter serviceMessagesStorage = mock(ISimpleLocalIfClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(SimpleLocalIfMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(ISimpleLocalIfClientMessageGetter messageGetterMock)
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


        testedClient = new SimpleLocalIfClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("tbRefIfaces.tbRefIfaces_android_service", "tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(SimpleLocalIfMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, SimpleLocalIfMessageType.INIT.getValue());
        Bundle data = new Bundle();
		int testintProperty = 1;
		data.putInt("intProperty", testintProperty);

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onIntPropertyChanged(testintProperty);
    }
    @Test
    public void onReceiveintPropertyPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleLocalIfMessageType.SET_IntProperty.getValue());
        Bundle data = new Bundle();
		int testintProperty = 1;
		data.putInt("intProperty", testintProperty);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onIntPropertyChanged(testintProperty);	    
    }
    
    @Test
     public void setPropertyRequestintProperty()
    {
		int testintProperty = 1;

        testedClient.setIntProperty(testintProperty);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleLocalIfMessageType.PROP_IntProperty.getValue(), response.what);
        Bundle data = response.getData();
        
			int receivedintProperty = data.getInt("intProperty", 0);
        assertEquals(receivedintProperty, testintProperty);
    }
    
    @Test
    public void whenNotifiedintSignal() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleLocalIfMessageType.SIG_IntSignal.getValue());
        Bundle data = new Bundle();
		int testparam = 1;
		data.putInt("param", testparam);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onIntSignal(testparam);

}


    public void onintMethodRequest() throws RemoteException {

        // Execute method
		int testparam = 1;
        int expectedResult = 1;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Integer> resFuture = testedClient.intMethodAsync(testparam);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result.intValue());
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleLocalIfMessageType.RPC_IntMethodReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			int receivedparam = data.getInt("param", 0);
        assertEquals(receivedparam, testparam);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleLocalIfMessageType.RPC_IntMethodResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putInt("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
