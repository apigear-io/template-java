//TODO later// Copyright Epic Games, Inc. All Rights Reserved.
package tbNames.tbNames_android_client;

import tbNames.tbNames_android_client.NamEsClient;

//import message type and parcelabe types
import tbNames.tbNames_api.TbNamesTestHelper;

import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_api.AbstractNamEs;
import tbNames.tbNames_android_messenger.NamEsMessageType;

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


interface INamEsClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class NamEsClientTest
{

    @Mock
    private Context mMockContext;
   
    private NamEsClient testedClient;
    private INamEsEventListener listenerMock = mock(INamEsEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private INamEsClientMessageGetter serviceMessagesStorage = mock(INamEsClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(NamEsMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(INamEsClientMessageGetter messageGetterMock)
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


        testedClient = new NamEsClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("tbNames.tbNames_android_service", "tbNames.tbNames_android_service.NamEsServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(NamEsMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, NamEsMessageType.INIT.getValue());
        Bundle data = new Bundle();
		boolean testSwitch = true;
		data.putBoolean("Switch", testSwitch);
		int testSOME_PROPERTY = 1;
		data.putInt("SOME_PROPERTY", testSOME_PROPERTY);
		int testSome_Poperty2 = 1;
		data.putInt("Some_Poperty2", testSome_Poperty2);

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onSwitchChanged(testSwitch);
		inOrderEventListener.verify(listenerMock,times(1)).onSomePropertyChanged(testSOME_PROPERTY);
		inOrderEventListener.verify(listenerMock,times(1)).onSomePoperty2Changed(testSome_Poperty2);
    }
//TODO do not add when a property is readonly
    @Test
    public void onReceiveSwitchPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NamEsMessageType.SET_Switch.getValue());
        Bundle data = new Bundle();
		boolean testSwitch = true;
		data.putBoolean("Switch", testSwitch);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onSwitchChanged(testSwitch);	    
    }

    @Test
     public void setPropertyRequestSwitch()
    {
		boolean testSwitch = true;

        testedClient.setSwitch(testSwitch);
        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NamEsMessageType.PROP_Switch.getValue(), response.what);
        Bundle data = response.getData();
        
			boolean receivedSwitch = data.getBoolean("Switch", false);
        assertEquals(receivedSwitch, testSwitch);
    }
//TODO do not add when a property is readonly
    @Test
    public void onReceiveSOME_PROPERTYPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NamEsMessageType.SET_SomeProperty.getValue());
        Bundle data = new Bundle();
		int testSOME_PROPERTY = 1;
		data.putInt("SOME_PROPERTY", testSOME_PROPERTY);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onSomePropertyChanged(testSOME_PROPERTY);	    
    }

    @Test
     public void setPropertyRequestSOME_PROPERTY()
    {
		int testSOME_PROPERTY = 1;

        testedClient.setSomeProperty(testSOME_PROPERTY);
        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NamEsMessageType.PROP_SomeProperty.getValue(), response.what);
        Bundle data = response.getData();
        
			int receivedSOME_PROPERTY = data.getInt("SOME_PROPERTY", 0);
        assertEquals(receivedSOME_PROPERTY, testSOME_PROPERTY);
    }
//TODO do not add when a property is readonly
    @Test
    public void onReceiveSome_Poperty2PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NamEsMessageType.SET_SomePoperty2.getValue());
        Bundle data = new Bundle();
		int testSome_Poperty2 = 1;
		data.putInt("Some_Poperty2", testSome_Poperty2);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onSomePoperty2Changed(testSome_Poperty2);	    
    }

    @Test
     public void setPropertyRequestSome_Poperty2()
    {
		int testSome_Poperty2 = 1;

        testedClient.setSomePoperty2(testSome_Poperty2);
        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NamEsMessageType.PROP_SomePoperty2.getValue(), response.what);
        Bundle data = response.getData();
        
			int receivedSome_Poperty2 = data.getInt("Some_Poperty2", 0);
        assertEquals(receivedSome_Poperty2, testSome_Poperty2);
    }
    @Test
    public void whenNotifiedSOME_SIGNAL() throws RemoteException
    {

        Message msg = Message.obtain(null, NamEsMessageType.SIG_SomeSignal.getValue());
        Bundle data = new Bundle();
		boolean testSOME_PARAM = true;
		data.putBoolean("SOME_PARAM", testSOME_PARAM);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSomeSignal(testSOME_PARAM);

}
    @Test
    public void whenNotifiedSome_Signal2() throws RemoteException
    {

        Message msg = Message.obtain(null, NamEsMessageType.SIG_SomeSignal2.getValue());
        Bundle data = new Bundle();
		boolean testSome_Param = true;
		data.putBoolean("Some_Param", testSome_Param);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSomeSignal2(testSome_Param);

}


    public void onSOME_FUNCTIONRequest() throws RemoteException {

        // Execute method
		boolean testSOME_PARAM = true;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Void> resFuture = testedClient.someFunctionAsync(testSOME_PARAM);

        resFuture.thenAccept(result -> {
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(NamEsMessageType.RPC_SomeFunctionReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
			boolean receivedSOME_PARAM = data.getBoolean("SOME_PARAM", false);
        assertEquals(receivedSOME_PARAM, testSOME_PARAM);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, NamEsMessageType.RPC_SomeFunctionResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onSome_Function2Request() throws RemoteException {

        // Execute method
		boolean testSome_Param = true;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Void> resFuture = testedClient.someFunction2Async(testSome_Param);

        resFuture.thenAccept(result -> {
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(NamEsMessageType.RPC_SomeFunction2Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
			boolean receivedSome_Param = data.getBoolean("Some_Param", false);
        assertEquals(receivedSome_Param, testSome_Param);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, NamEsMessageType.RPC_SomeFunction2Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
