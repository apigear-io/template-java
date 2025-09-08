//TODO later// Copyright Epic Games, Inc. All Rights Reserved.
package testbed1.testbed1_android_client;

import testbed1.testbed1_android_client.StructArrayInterfaceClient;

//import message type and parcelabe types
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_android_messenger.StructBoolParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;
import testbed1.testbed1_api.Testbed1TestHelper;

import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_api.AbstractStructArrayInterface;
import testbed1.testbed1_android_messenger.StructArrayInterfaceMessageType;

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


interface IStructArrayInterfaceClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class StructArrayInterfaceClientTest
{

    @Mock
    private Context mMockContext;
   
    private StructArrayInterfaceClient testedClient;
    private IStructArrayInterfaceEventListener listenerMock = mock(IStructArrayInterfaceEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private IStructArrayInterfaceClientMessageGetter serviceMessagesStorage = mock(IStructArrayInterfaceClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(StructArrayInterfaceMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(IStructArrayInterfaceClientMessageGetter messageGetterMock)
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


        testedClient = new StructArrayInterfaceClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("testbed1.testbed1_android_service", "testbed1.testbed1_android_service.StructArrayInterfaceServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(StructArrayInterfaceMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.INIT.getValue());
        Bundle data = new Bundle();
        StructBool[] testpropBool = new StructBool[1];
        testpropBool[0] = Testbed1TestHelper.makeTestStructBool();
		data.putParcelableArray("propBool", StructBoolParcelable.wrapArray(testpropBool));
        StructInt[] testpropInt = new StructInt[1];
        testpropInt[0] = Testbed1TestHelper.makeTestStructInt();
		data.putParcelableArray("propInt", StructIntParcelable.wrapArray(testpropInt));
        StructFloat[] testpropFloat = new StructFloat[1];
        testpropFloat[0] = Testbed1TestHelper.makeTestStructFloat();
		data.putParcelableArray("propFloat", StructFloatParcelable.wrapArray(testpropFloat));
        StructString[] testpropString = new StructString[1];
        testpropString[0] = Testbed1TestHelper.makeTestStructString();
		data.putParcelableArray("propString", StructStringParcelable.wrapArray(testpropString));

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropBoolChanged(any(StructBool[].class));
        inOrderEventListener.verify(listenerMock,times(1)).onPropIntChanged(any(StructInt[].class));
        inOrderEventListener.verify(listenerMock,times(1)).onPropFloatChanged(any(StructFloat[].class));
        inOrderEventListener.verify(listenerMock,times(1)).onPropStringChanged(any(StructString[].class));
    }
//TODO do not add when a property is readonly
    @Test
    public void onReceivepropBoolPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SET_PropBool.getValue());
        Bundle data = new Bundle();
        StructBool[] testpropBool = new StructBool[1];
        testpropBool[0] = Testbed1TestHelper.makeTestStructBool();
		data.putParcelableArray("propBool", StructBoolParcelable.wrapArray(testpropBool));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropBoolChanged(any(StructBool[].class));	    
    }

    @Test
     public void setPropertyRequestpropBool()
    {
        StructBool[] testpropBool = new StructBool[1];
        testpropBool[0] = Testbed1TestHelper.makeTestStructBool();

        testedClient.setPropBool(testpropBool);
        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.PROP_PropBool.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructBoolParcelable.class.getClassLoader());
        
            StructBool[] receivedpropBool =  StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("propBool", StructBoolParcelable.class));
        assertEquals(receivedpropBool, testpropBool);
    }
//TODO do not add when a property is readonly
    @Test
    public void onReceivepropIntPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SET_PropInt.getValue());
        Bundle data = new Bundle();
        StructInt[] testpropInt = new StructInt[1];
        testpropInt[0] = Testbed1TestHelper.makeTestStructInt();
		data.putParcelableArray("propInt", StructIntParcelable.wrapArray(testpropInt));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropIntChanged(any(StructInt[].class));	    
    }

    @Test
     public void setPropertyRequestpropInt()
    {
        StructInt[] testpropInt = new StructInt[1];
        testpropInt[0] = Testbed1TestHelper.makeTestStructInt();

        testedClient.setPropInt(testpropInt);
        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.PROP_PropInt.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructIntParcelable.class.getClassLoader());
        
            StructInt[] receivedpropInt =  StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("propInt", StructIntParcelable.class));
        assertEquals(receivedpropInt, testpropInt);
    }
//TODO do not add when a property is readonly
    @Test
    public void onReceivepropFloatPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SET_PropFloat.getValue());
        Bundle data = new Bundle();
        StructFloat[] testpropFloat = new StructFloat[1];
        testpropFloat[0] = Testbed1TestHelper.makeTestStructFloat();
		data.putParcelableArray("propFloat", StructFloatParcelable.wrapArray(testpropFloat));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropFloatChanged(any(StructFloat[].class));	    
    }

    @Test
     public void setPropertyRequestpropFloat()
    {
        StructFloat[] testpropFloat = new StructFloat[1];
        testpropFloat[0] = Testbed1TestHelper.makeTestStructFloat();

        testedClient.setPropFloat(testpropFloat);
        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.PROP_PropFloat.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructFloatParcelable.class.getClassLoader());
        
            StructFloat[] receivedpropFloat =  StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("propFloat", StructFloatParcelable.class));
        assertEquals(receivedpropFloat, testpropFloat);
    }
//TODO do not add when a property is readonly
    @Test
    public void onReceivepropStringPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SET_PropString.getValue());
        Bundle data = new Bundle();
        StructString[] testpropString = new StructString[1];
        testpropString[0] = Testbed1TestHelper.makeTestStructString();
		data.putParcelableArray("propString", StructStringParcelable.wrapArray(testpropString));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropStringChanged(any(StructString[].class));	    
    }

    @Test
     public void setPropertyRequestpropString()
    {
        StructString[] testpropString = new StructString[1];
        testpropString[0] = Testbed1TestHelper.makeTestStructString();

        testedClient.setPropString(testpropString);
        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.PROP_PropString.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructStringParcelable.class.getClassLoader());
        
            StructString[] receivedpropString =  StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("propString", StructStringParcelable.class));
        assertEquals(receivedpropString, testpropString);
    }
    @Test
    public void whenNotifiedsigBool() throws RemoteException
    {

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SIG_SigBool.getValue());
        Bundle data = new Bundle();
        StructBool[] testparamBool = new StructBool[1];
        testparamBool[0] = Testbed1TestHelper.makeTestStructBool();
		data.putParcelableArray("paramBool", StructBoolParcelable.wrapArray(testparamBool));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigBool( any(StructBool[].class));

}
    @Test
    public void whenNotifiedsigInt() throws RemoteException
    {

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SIG_SigInt.getValue());
        Bundle data = new Bundle();
        StructInt[] testparamInt = new StructInt[1];
        testparamInt[0] = Testbed1TestHelper.makeTestStructInt();
		data.putParcelableArray("paramInt", StructIntParcelable.wrapArray(testparamInt));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigInt( any(StructInt[].class));

}
    @Test
    public void whenNotifiedsigFloat() throws RemoteException
    {

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SIG_SigFloat.getValue());
        Bundle data = new Bundle();
        StructFloat[] testparamFloat = new StructFloat[1];
        testparamFloat[0] = Testbed1TestHelper.makeTestStructFloat();
		data.putParcelableArray("paramFloat", StructFloatParcelable.wrapArray(testparamFloat));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigFloat( any(StructFloat[].class));

}
    @Test
    public void whenNotifiedsigString() throws RemoteException
    {

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SIG_SigString.getValue());
        Bundle data = new Bundle();
        StructString[] testparamString = new StructString[1];
        testparamString[0] = Testbed1TestHelper.makeTestStructString();
		data.putParcelableArray("paramString", StructStringParcelable.wrapArray(testparamString));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigString( any(StructString[].class));

}


    public void onfuncBoolRequest() throws RemoteException {

        // Execute method
        StructBool[] testparamBool = new StructBool[1];
        testparamBool[0] = Testbed1TestHelper.makeTestStructBool();
        StructBool[] expectedResult = new StructBool[1];
        expectedResult[0] = Testbed1TestHelper.makeTestStructBool();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<StructBool[]> resFuture = testedClient.funcBoolAsync(testparamBool);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(StructArrayInterfaceMessageType.RPC_FuncBoolReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
		data.setClassLoader(StructBoolParcelable.class.getClassLoader());
        
            StructBool[] receivedparamBool =  StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("paramBool", StructBoolParcelable.class));
        assertEquals(receivedparamBool, testparamBool);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncBoolResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelableArray("result", StructBoolParcelable.wrapArray(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncIntRequest() throws RemoteException {

        // Execute method
        StructInt[] testparamInt = new StructInt[1];
        testparamInt[0] = Testbed1TestHelper.makeTestStructInt();
        StructInt[] expectedResult = new StructInt[1];
        expectedResult[0] = Testbed1TestHelper.makeTestStructInt();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<StructInt[]> resFuture = testedClient.funcIntAsync(testparamInt);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(StructArrayInterfaceMessageType.RPC_FuncIntReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
		data.setClassLoader(StructIntParcelable.class.getClassLoader());
        
            StructInt[] receivedparamInt =  StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("paramInt", StructIntParcelable.class));
        assertEquals(receivedparamInt, testparamInt);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncIntResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelableArray("result", StructIntParcelable.wrapArray(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncFloatRequest() throws RemoteException {

        // Execute method
        StructFloat[] testparamFloat = new StructFloat[1];
        testparamFloat[0] = Testbed1TestHelper.makeTestStructFloat();
        StructFloat[] expectedResult = new StructFloat[1];
        expectedResult[0] = Testbed1TestHelper.makeTestStructFloat();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<StructFloat[]> resFuture = testedClient.funcFloatAsync(testparamFloat);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(StructArrayInterfaceMessageType.RPC_FuncFloatReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
		data.setClassLoader(StructFloatParcelable.class.getClassLoader());
        
            StructFloat[] receivedparamFloat =  StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("paramFloat", StructFloatParcelable.class));
        assertEquals(receivedparamFloat, testparamFloat);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncFloatResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelableArray("result", StructFloatParcelable.wrapArray(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncStringRequest() throws RemoteException {

        // Execute method
        StructString[] testparamString = new StructString[1];
        testparamString[0] = Testbed1TestHelper.makeTestStructString();
        StructString[] expectedResult = new StructString[1];
        expectedResult[0] = Testbed1TestHelper.makeTestStructString();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<StructString[]> resFuture = testedClient.funcStringAsync(testparamString);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(StructArrayInterfaceMessageType.RPC_FuncStringReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
		data.setClassLoader(StructStringParcelable.class.getClassLoader());
        
            StructString[] receivedparamString =  StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("paramString", StructStringParcelable.class));
        assertEquals(receivedparamString, testparamString);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncStringResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelableArray("result", StructStringParcelable.wrapArray(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
