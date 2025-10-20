//TODO later// Copyright Epic Games, Inc. All Rights Reserved.
package tbSimple.tbSimple_android_client;

import tbSimple.tbSimple_android_client.SimpleInterfaceClient;

//import message type and parcelabe types
import tbSimple.tbSimple_api.TbSimpleTestHelper;

import tbSimple.tbSimple_api.ISimpleInterfaceEventListener;
import tbSimple.tbSimple_api.ISimpleInterface;
import tbSimple.tbSimple_api.AbstractSimpleInterface;
import tbSimple.tbSimple_android_messenger.SimpleInterfaceMessageType;

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


interface ISimpleInterfaceClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SimpleInterfaceClientTest
{

    @Mock
    private Context mMockContext;
   
    private SimpleInterfaceClient testedClient;
    private ISimpleInterfaceEventListener listenerMock = mock(ISimpleInterfaceEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private ISimpleInterfaceClientMessageGetter serviceMessagesStorage = mock(ISimpleInterfaceClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(SimpleInterfaceMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(ISimpleInterfaceClientMessageGetter messageGetterMock)
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


        testedClient = new SimpleInterfaceClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("tbSimple.tbSimple_android_service", "tbSimple.tbSimple_android_service.SimpleInterfaceServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(SimpleInterfaceMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.INIT.getValue());
        Bundle data = new Bundle();
		boolean testpropBool = true;
		data.putBoolean("propBool", testpropBool);
		int testpropInt = 1;
		data.putInt("propInt", testpropInt);
		int testpropInt32 = 1;
		data.putInt("propInt32", testpropInt32);
		long testpropInt64 = 1L;
		data.putLong("propInt64", testpropInt64);
		float testpropFloat = 1.0f;
		data.putFloat("propFloat", testpropFloat);
		float testpropFloat32 = 1.0f;
		data.putFloat("propFloat32", testpropFloat32);
		double testpropFloat64 = 1.0;
		data.putDouble("propFloat64", testpropFloat64);
		String testpropString = new String("xyz");
		data.putString("propString", testpropString);

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropBoolChanged(testpropBool);
		inOrderEventListener.verify(listenerMock,times(1)).onPropIntChanged(testpropInt);
		inOrderEventListener.verify(listenerMock,times(1)).onPropInt32Changed(testpropInt32);
		inOrderEventListener.verify(listenerMock,times(1)).onPropInt64Changed(testpropInt64);
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloatChanged(testpropFloat);
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloat32Changed(testpropFloat32);
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloat64Changed(testpropFloat64);
		inOrderEventListener.verify(listenerMock,times(1)).onPropStringChanged(testpropString);
    }
    @Test
    public void onReceivepropBoolPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SET_PropBool.getValue());
        Bundle data = new Bundle();
		boolean testpropBool = true;
		data.putBoolean("propBool", testpropBool);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropBoolChanged(testpropBool);	    
    }
    
    @Test
     public void setPropertyRequestpropBool()
    {
		boolean testpropBool = true;

        testedClient.setPropBool(testpropBool);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.PROP_PropBool.getValue(), response.what);
        Bundle data = response.getData();
        
			boolean receivedpropBool = data.getBoolean("propBool", false);
        assertEquals(receivedpropBool, testpropBool);
    }
    
    @Test
    public void onReceivepropIntPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SET_PropInt.getValue());
        Bundle data = new Bundle();
		int testpropInt = 1;
		data.putInt("propInt", testpropInt);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropIntChanged(testpropInt);	    
    }
    
    @Test
     public void setPropertyRequestpropInt()
    {
		int testpropInt = 1;

        testedClient.setPropInt(testpropInt);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.PROP_PropInt.getValue(), response.what);
        Bundle data = response.getData();
        
			int receivedpropInt = data.getInt("propInt", 0);
        assertEquals(receivedpropInt, testpropInt);
    }
    
    @Test
    public void onReceivepropInt32PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SET_PropInt32.getValue());
        Bundle data = new Bundle();
		int testpropInt32 = 1;
		data.putInt("propInt32", testpropInt32);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropInt32Changed(testpropInt32);	    
    }
    
    @Test
     public void setPropertyRequestpropInt32()
    {
		int testpropInt32 = 1;

        testedClient.setPropInt32(testpropInt32);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.PROP_PropInt32.getValue(), response.what);
        Bundle data = response.getData();
        
			int receivedpropInt32 = data.getInt("propInt32", 0);
        assertEquals(receivedpropInt32, testpropInt32);
    }
    
    @Test
    public void onReceivepropInt64PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SET_PropInt64.getValue());
        Bundle data = new Bundle();
		long testpropInt64 = 1L;
		data.putLong("propInt64", testpropInt64);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropInt64Changed(testpropInt64);	    
    }
    
    @Test
     public void setPropertyRequestpropInt64()
    {
		long testpropInt64 = 1L;

        testedClient.setPropInt64(testpropInt64);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.PROP_PropInt64.getValue(), response.what);
        Bundle data = response.getData();
        
			long receivedpropInt64 = data.getLong("propInt64", 0L);
        assertEquals(receivedpropInt64, testpropInt64);
    }
    
    @Test
    public void onReceivepropFloatPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SET_PropFloat.getValue());
        Bundle data = new Bundle();
		float testpropFloat = 1.0f;
		data.putFloat("propFloat", testpropFloat);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloatChanged(testpropFloat);	    
    }
    
    @Test
     public void setPropertyRequestpropFloat()
    {
		float testpropFloat = 1.0f;

        testedClient.setPropFloat(testpropFloat);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.PROP_PropFloat.getValue(), response.what);
        Bundle data = response.getData();
        
			float receivedpropFloat = data.getFloat("propFloat", 0.0f);
        assertEquals(receivedpropFloat, testpropFloat, 1e-6f);
    }
    
    @Test
    public void onReceivepropFloat32PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SET_PropFloat32.getValue());
        Bundle data = new Bundle();
		float testpropFloat32 = 1.0f;
		data.putFloat("propFloat32", testpropFloat32);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloat32Changed(testpropFloat32);	    
    }
    
    @Test
     public void setPropertyRequestpropFloat32()
    {
		float testpropFloat32 = 1.0f;

        testedClient.setPropFloat32(testpropFloat32);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.PROP_PropFloat32.getValue(), response.what);
        Bundle data = response.getData();
        
			float receivedpropFloat32 = data.getFloat("propFloat32", 0.0f);
        assertEquals(receivedpropFloat32, testpropFloat32, 1e-6f);
    }
    
    @Test
    public void onReceivepropFloat64PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SET_PropFloat64.getValue());
        Bundle data = new Bundle();
		double testpropFloat64 = 1.0;
		data.putDouble("propFloat64", testpropFloat64);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloat64Changed(testpropFloat64);	    
    }
    
    @Test
     public void setPropertyRequestpropFloat64()
    {
		double testpropFloat64 = 1.0;

        testedClient.setPropFloat64(testpropFloat64);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.PROP_PropFloat64.getValue(), response.what);
        Bundle data = response.getData();
        
			double receivedpropFloat64 = data.getDouble("propFloat64", 0.0);
        assertEquals(receivedpropFloat64, testpropFloat64, 1e-6f);
    }
    
    @Test
    public void onReceivepropStringPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SET_PropString.getValue());
        Bundle data = new Bundle();
		String testpropString = new String("xyz");
		data.putString("propString", testpropString);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropStringChanged(testpropString);	    
    }
    
    @Test
     public void setPropertyRequestpropString()
    {
		String testpropString = new String("xyz");

        testedClient.setPropString(testpropString);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.PROP_PropString.getValue(), response.what);
        Bundle data = response.getData();
        
			String receivedpropString = data.getString("propString", new String());
        assertEquals(receivedpropString, testpropString);
    }
    
    @Test
    public void whenNotifiedsigBool() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SIG_SigBool.getValue());
        Bundle data = new Bundle();
		boolean testparamBool = true;
		data.putBoolean("paramBool", testparamBool);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigBool(testparamBool);

}
    @Test
    public void whenNotifiedsigInt() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SIG_SigInt.getValue());
        Bundle data = new Bundle();
		int testparamInt = 1;
		data.putInt("paramInt", testparamInt);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigInt(testparamInt);

}
    @Test
    public void whenNotifiedsigInt32() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SIG_SigInt32.getValue());
        Bundle data = new Bundle();
		int testparamInt32 = 1;
		data.putInt("paramInt32", testparamInt32);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigInt32(testparamInt32);

}
    @Test
    public void whenNotifiedsigInt64() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SIG_SigInt64.getValue());
        Bundle data = new Bundle();
		long testparamInt64 = 1L;
		data.putLong("paramInt64", testparamInt64);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigInt64(testparamInt64);

}
    @Test
    public void whenNotifiedsigFloat() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SIG_SigFloat.getValue());
        Bundle data = new Bundle();
		float testparamFloat = 1.0f;
		data.putFloat("paramFloat", testparamFloat);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigFloat(testparamFloat);

}
    @Test
    public void whenNotifiedsigFloat32() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SIG_SigFloat32.getValue());
        Bundle data = new Bundle();
		float testparamFloat32 = 1.0f;
		data.putFloat("paramFloat32", testparamFloat32);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigFloat32(testparamFloat32);

}
    @Test
    public void whenNotifiedsigFloat64() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SIG_SigFloat64.getValue());
        Bundle data = new Bundle();
		double testparamFloat64 = 1.0;
		data.putDouble("paramFloat64", testparamFloat64);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigFloat64(testparamFloat64);

}
    @Test
    public void whenNotifiedsigString() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.SIG_SigString.getValue());
        Bundle data = new Bundle();
		String testparamString = new String("xyz");
		data.putString("paramString", testparamString);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigString(testparamString);

}


    public void onfuncNoReturnValueRequest() throws RemoteException {

        // Execute method
		boolean testparamBool = true;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Void> resFuture = testedClient.funcNoReturnValueAsync(testparamBool);

        resFuture.thenAccept(result -> {
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleInterfaceMessageType.RPC_FuncNoReturnValueReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			boolean receivedparamBool = data.getBoolean("paramBool", false);
        assertEquals(receivedparamBool, testparamBool);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncNoReturnValueResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncBoolRequest() throws RemoteException {

        // Execute method
		boolean testparamBool = true;
        boolean expectedResult = true;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Boolean> resFuture = testedClient.funcBoolAsync(testparamBool);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result.booleanValue());
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleInterfaceMessageType.RPC_FuncBoolReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			boolean receivedparamBool = data.getBoolean("paramBool", false);
        assertEquals(receivedparamBool, testparamBool);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncBoolResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putBoolean("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncIntRequest() throws RemoteException {

        // Execute method
		int testparamInt = 1;
        int expectedResult = 1;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Integer> resFuture = testedClient.funcIntAsync(testparamInt);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result.intValue());
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleInterfaceMessageType.RPC_FuncIntReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			int receivedparamInt = data.getInt("paramInt", 0);
        assertEquals(receivedparamInt, testparamInt);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncIntResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putInt("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncInt32Request() throws RemoteException {

        // Execute method
		int testparamInt32 = 1;
        int expectedResult = 1;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Integer> resFuture = testedClient.funcInt32Async(testparamInt32);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result.intValue());
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleInterfaceMessageType.RPC_FuncInt32Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			int receivedparamInt32 = data.getInt("paramInt32", 0);
        assertEquals(receivedparamInt32, testparamInt32);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncInt32Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putInt("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncInt64Request() throws RemoteException {

        // Execute method
		long testparamInt64 = 1L;
        long expectedResult = 1L;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Long> resFuture = testedClient.funcInt64Async(testparamInt64);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result.longValue());
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleInterfaceMessageType.RPC_FuncInt64Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			long receivedparamInt64 = data.getLong("paramInt64", 0L);
        assertEquals(receivedparamInt64, testparamInt64);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncInt64Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putLong("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncFloatRequest() throws RemoteException {

        // Execute method
		float testparamFloat = 1.0f;
        float expectedResult = 1.0f;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Float> resFuture = testedClient.funcFloatAsync(testparamFloat);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result.floatValue(), 1e-6f);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleInterfaceMessageType.RPC_FuncFloatReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			float receivedparamFloat = data.getFloat("paramFloat", 0.0f);
        assertEquals(receivedparamFloat, testparamFloat, 1e-6f);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncFloatResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putFloat("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncFloat32Request() throws RemoteException {

        // Execute method
		float testparamFloat32 = 1.0f;
        float expectedResult = 1.0f;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Float> resFuture = testedClient.funcFloat32Async(testparamFloat32);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result.floatValue(), 1e-6f);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleInterfaceMessageType.RPC_FuncFloat32Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			float receivedparamFloat32 = data.getFloat("paramFloat32", 0.0f);
        assertEquals(receivedparamFloat32, testparamFloat32, 1e-6f);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncFloat32Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putFloat("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncFloat64Request() throws RemoteException {

        // Execute method
		double testparamFloat = 1.0;
        double expectedResult = 1.0;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Double> resFuture = testedClient.funcFloat64Async(testparamFloat);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result.doubleValue(), 1e-6f);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleInterfaceMessageType.RPC_FuncFloat64Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			double receivedparamFloat = data.getDouble("paramFloat", 0.0);
        assertEquals(receivedparamFloat, testparamFloat, 1e-6f);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncFloat64Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putDouble("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncStringRequest() throws RemoteException {

        // Execute method
		String testparamString = new String("xyz");
        String expectedResult = new String("xyz");

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<String> resFuture = testedClient.funcStringAsync(testparamString);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleInterfaceMessageType.RPC_FuncStringReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			String receivedparamString = data.getString("paramString", new String());
        assertEquals(receivedparamString, testparamString);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncStringResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putString("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
