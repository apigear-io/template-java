//TODO later// Copyright Epic Games, Inc. All Rights Reserved.
package testbed1.testbed1_android_client;

import testbed1.testbed1_android_client.StructInterfaceClient;

//import message type and parcelabe types
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_android_messenger.StructBoolParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;
import testbed1.testbed1_api.StructStruct;
import testbed1.testbed1_android_messenger.StructStructParcelable;
import testbed1.testbed1_api.StructEnum;
import testbed1.testbed1_android_messenger.StructEnumParcelable;
import testbed1.testbed1_api.StructBoolWithArray;
import testbed1.testbed1_android_messenger.StructBoolWithArrayParcelable;
import testbed1.testbed1_api.StructIntWithArray;
import testbed1.testbed1_android_messenger.StructIntWithArrayParcelable;
import testbed1.testbed1_api.StructFloatWithArray;
import testbed1.testbed1_android_messenger.StructFloatWithArrayParcelable;
import testbed1.testbed1_api.StructStringWithArray;
import testbed1.testbed1_android_messenger.StructStringWithArrayParcelable;
import testbed1.testbed1_api.StructStructWithArray;
import testbed1.testbed1_android_messenger.StructStructWithArrayParcelable;
import testbed1.testbed1_api.StructEnumWithArray;
import testbed1.testbed1_android_messenger.StructEnumWithArrayParcelable;
import testbed1.testbed1_api.Testbed1TestHelper;
import testbed1.testbed1_api.Enum0;
import testbed1.testbed1_android_messenger.Enum0Parcelable;
import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_android_messenger.StructInterfaceParcelable;
import testbed1.testbed1_impl.StructInterfaceService;
import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_android_messenger.StructArrayInterfaceParcelable;
import testbed1.testbed1_impl.StructArrayInterfaceService;
import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_android_messenger.StructArray2InterfaceParcelable;
import testbed1.testbed1_impl.StructArray2InterfaceService;

import testbed1.testbed1_api.IStructInterfaceEventListener;
import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_api.AbstractStructInterface;
import testbed1.testbed1_android_messenger.StructInterfaceMessageType;

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
import testbed1.testbed1_android_messenger.Conversions;
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


interface IStructInterfaceClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class StructInterfaceClientTest
{

    @Mock
    private Context mMockContext;
   
    private StructInterfaceClient testedClient;
    private IStructInterfaceEventListener listenerMock = mock(IStructInterfaceEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private IStructInterfaceClientMessageGetter serviceMessagesStorage = mock(IStructInterfaceClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(StructInterfaceMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(IStructInterfaceClientMessageGetter messageGetterMock)
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


        testedClient = new StructInterfaceClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("testbed1.testbed1_android_service", "testbed1.testbed1_android_service.StructInterfaceServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(StructInterfaceMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, StructInterfaceMessageType.INIT.getValue());
        Bundle data = new Bundle();
        StructBool testpropBool = Testbed1TestHelper.makeTestStructBool();
		data.putParcelable("propBool", new StructBoolParcelable(testpropBool));
        StructInt testpropInt = Testbed1TestHelper.makeTestStructInt();
		data.putParcelable("propInt", new StructIntParcelable(testpropInt));
        StructFloat testpropFloat = Testbed1TestHelper.makeTestStructFloat();
		data.putParcelable("propFloat", new StructFloatParcelable(testpropFloat));
        StructString testpropString = Testbed1TestHelper.makeTestStructString();
		data.putParcelable("propString", new StructStringParcelable(testpropString));

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropBoolChanged(any(StructBool.class));
        inOrderEventListener.verify(listenerMock,times(1)).onPropIntChanged(any(StructInt.class));
        inOrderEventListener.verify(listenerMock,times(1)).onPropFloatChanged(any(StructFloat.class));
        inOrderEventListener.verify(listenerMock,times(1)).onPropStringChanged(any(StructString.class));
    }
    @Test
    public void onReceivepropBoolPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructInterfaceMessageType.SET_PropBool.getValue());
        Bundle data = new Bundle();
        StructBool testpropBool = Testbed1TestHelper.makeTestStructBool();
		data.putParcelable("propBool", new StructBoolParcelable(testpropBool));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropBoolChanged(any(StructBool.class));	    
    }
    
    @Test
     public void setPropertyRequestpropBool()
    {
        StructBool testpropBool = Testbed1TestHelper.makeTestStructBool();

        testedClient.setPropBool(testpropBool);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.PROP_PropBool.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructBoolParcelable.class.getClassLoader());
        
			StructBool receivedpropBool = data.getParcelable("propBool", StructBoolParcelable.class).getStructBool();
        assertEquals(receivedpropBool, testpropBool);
    }
    
    @Test
    public void onReceivepropIntPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructInterfaceMessageType.SET_PropInt.getValue());
        Bundle data = new Bundle();
        StructInt testpropInt = Testbed1TestHelper.makeTestStructInt();
		data.putParcelable("propInt", new StructIntParcelable(testpropInt));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropIntChanged(any(StructInt.class));	    
    }
    
    @Test
     public void setPropertyRequestpropInt()
    {
        StructInt testpropInt = Testbed1TestHelper.makeTestStructInt();

        testedClient.setPropInt(testpropInt);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.PROP_PropInt.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructIntParcelable.class.getClassLoader());
        
			StructInt receivedpropInt = data.getParcelable("propInt", StructIntParcelable.class).getStructInt();
        assertEquals(receivedpropInt, testpropInt);
    }
    
    @Test
    public void onReceivepropFloatPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructInterfaceMessageType.SET_PropFloat.getValue());
        Bundle data = new Bundle();
        StructFloat testpropFloat = Testbed1TestHelper.makeTestStructFloat();
		data.putParcelable("propFloat", new StructFloatParcelable(testpropFloat));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropFloatChanged(any(StructFloat.class));	    
    }
    
    @Test
     public void setPropertyRequestpropFloat()
    {
        StructFloat testpropFloat = Testbed1TestHelper.makeTestStructFloat();

        testedClient.setPropFloat(testpropFloat);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.PROP_PropFloat.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructFloatParcelable.class.getClassLoader());
        
			StructFloat receivedpropFloat = data.getParcelable("propFloat", StructFloatParcelable.class).getStructFloat();
        assertEquals(receivedpropFloat, testpropFloat);
    }
    
    @Test
    public void onReceivepropStringPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructInterfaceMessageType.SET_PropString.getValue());
        Bundle data = new Bundle();
        StructString testpropString = Testbed1TestHelper.makeTestStructString();
		data.putParcelable("propString", new StructStringParcelable(testpropString));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropStringChanged(any(StructString.class));	    
    }
    
    @Test
     public void setPropertyRequestpropString()
    {
        StructString testpropString = Testbed1TestHelper.makeTestStructString();

        testedClient.setPropString(testpropString);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.PROP_PropString.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructStringParcelable.class.getClassLoader());
        
			StructString receivedpropString = data.getParcelable("propString", StructStringParcelable.class).getStructString();
        assertEquals(receivedpropString, testpropString);
    }
    
    @Test
    public void whenNotifiedsigBool() throws RemoteException
    {

        Message msg = Message.obtain(null, StructInterfaceMessageType.SIG_SigBool.getValue());
        Bundle data = new Bundle();
        StructBool testparamBool = Testbed1TestHelper.makeTestStructBool();
		data.putParcelable("paramBool", new StructBoolParcelable(testparamBool));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigBool( any(StructBool.class));

}
    @Test
    public void whenNotifiedsigInt() throws RemoteException
    {

        Message msg = Message.obtain(null, StructInterfaceMessageType.SIG_SigInt.getValue());
        Bundle data = new Bundle();
        StructInt testparamInt = Testbed1TestHelper.makeTestStructInt();
		data.putParcelable("paramInt", new StructIntParcelable(testparamInt));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigInt( any(StructInt.class));

}
    @Test
    public void whenNotifiedsigFloat() throws RemoteException
    {

        Message msg = Message.obtain(null, StructInterfaceMessageType.SIG_SigFloat.getValue());
        Bundle data = new Bundle();
        StructFloat testparamFloat = Testbed1TestHelper.makeTestStructFloat();
		data.putParcelable("paramFloat", new StructFloatParcelable(testparamFloat));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigFloat( any(StructFloat.class));

}
    @Test
    public void whenNotifiedsigString() throws RemoteException
    {

        Message msg = Message.obtain(null, StructInterfaceMessageType.SIG_SigString.getValue());
        Bundle data = new Bundle();
        StructString testparamString = Testbed1TestHelper.makeTestStructString();
		data.putParcelable("paramString", new StructStringParcelable(testparamString));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigString( any(StructString.class));

}

    @Test
    public void onfuncBoolRequest() throws RemoteException {

        // Execute method
        StructBool testparamBool = Testbed1TestHelper.makeTestStructBool();
        StructBool expectedResult = Testbed1TestHelper.makeTestStructBool();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<StructBool> resFuture = testedClient.funcBoolAsync(testparamBool);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(StructInterfaceMessageType.RPC_FuncBoolReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(StructBoolParcelable.class.getClassLoader());
        
			StructBool receivedparamBool = data.getParcelable("paramBool", StructBoolParcelable.class).getStructBool();
        assertEquals(receivedparamBool, testparamBool);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructInterfaceMessageType.RPC_FuncBoolResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new StructBoolParcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncIntRequest() throws RemoteException {

        // Execute method
        StructInt testparamInt = Testbed1TestHelper.makeTestStructInt();
        StructInt expectedResult = Testbed1TestHelper.makeTestStructInt();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<StructInt> resFuture = testedClient.funcIntAsync(testparamInt);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(StructInterfaceMessageType.RPC_FuncIntReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(StructIntParcelable.class.getClassLoader());
        
			StructInt receivedparamInt = data.getParcelable("paramInt", StructIntParcelable.class).getStructInt();
        assertEquals(receivedparamInt, testparamInt);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructInterfaceMessageType.RPC_FuncIntResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new StructIntParcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncFloatRequest() throws RemoteException {

        // Execute method
        StructFloat testparamFloat = Testbed1TestHelper.makeTestStructFloat();
        StructFloat expectedResult = Testbed1TestHelper.makeTestStructFloat();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<StructFloat> resFuture = testedClient.funcFloatAsync(testparamFloat);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(StructInterfaceMessageType.RPC_FuncFloatReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(StructFloatParcelable.class.getClassLoader());
        
			StructFloat receivedparamFloat = data.getParcelable("paramFloat", StructFloatParcelable.class).getStructFloat();
        assertEquals(receivedparamFloat, testparamFloat);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructInterfaceMessageType.RPC_FuncFloatResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new StructFloatParcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncStringRequest() throws RemoteException {

        // Execute method
        StructString testparamString = Testbed1TestHelper.makeTestStructString();
        StructString expectedResult = Testbed1TestHelper.makeTestStructString();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<StructString> resFuture = testedClient.funcStringAsync(testparamString);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(StructInterfaceMessageType.RPC_FuncStringReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(StructStringParcelable.class.getClassLoader());
        
			StructString receivedparamString = data.getParcelable("paramString", StructStringParcelable.class).getStructString();
        assertEquals(receivedparamString, testparamString);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructInterfaceMessageType.RPC_FuncStringResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new StructStringParcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
