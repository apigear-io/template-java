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
        List<StructBool> testpropBool = new java.util.ArrayList<>();
        testpropBool.add(Testbed1TestHelper.makeTestStructBool());
		data.putParcelableArray("propBool", StructBoolParcelable.wrapArray(Conversions.toArray(testpropBool, new StructBool[0])));
        List<StructInt> testpropInt = new java.util.ArrayList<>();
        testpropInt.add(Testbed1TestHelper.makeTestStructInt());
		data.putParcelableArray("propInt", StructIntParcelable.wrapArray(Conversions.toArray(testpropInt, new StructInt[0])));
        List<StructFloat> testpropFloat = new java.util.ArrayList<>();
        testpropFloat.add(Testbed1TestHelper.makeTestStructFloat());
		data.putParcelableArray("propFloat", StructFloatParcelable.wrapArray(Conversions.toArray(testpropFloat, new StructFloat[0])));
        List<StructString> testpropString = new java.util.ArrayList<>();
        testpropString.add(Testbed1TestHelper.makeTestStructString());
		data.putParcelableArray("propString", StructStringParcelable.wrapArray(Conversions.toArray(testpropString, new StructString[0])));
        List<Enum0> testpropEnum = new java.util.ArrayList<>();
        testpropEnum.add(Enum0.Value1);
		data.putParcelableArray("propEnum", Enum0Parcelable.wrapArray(Conversions.toArray(testpropEnum, new Enum0[0])));

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropBoolChanged(any(List<StructBool>.class));
        inOrderEventListener.verify(listenerMock,times(1)).onPropIntChanged(any(List<StructInt>.class));
        inOrderEventListener.verify(listenerMock,times(1)).onPropFloatChanged(any(List<StructFloat>.class));
        inOrderEventListener.verify(listenerMock,times(1)).onPropStringChanged(any(List<StructString>.class));
		inOrderEventListener.verify(listenerMock,times(1)).onPropEnumChanged(testpropEnum);
    }
    @Test
    public void onReceivepropBoolPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SET_PropBool.getValue());
        Bundle data = new Bundle();
        List<StructBool> testpropBool = new java.util.ArrayList<>();
        testpropBool.add(Testbed1TestHelper.makeTestStructBool());
		data.putParcelableArray("propBool", StructBoolParcelable.wrapArray(Conversions.toArray(testpropBool, new StructBool[0])));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropBoolChanged(any(List<StructBool>.class));	    
    }
    
    @Test
     public void setPropertyRequestpropBool()
    {
        List<StructBool> testpropBool = new java.util.ArrayList<>();
        testpropBool.add(Testbed1TestHelper.makeTestStructBool());

        testedClient.setPropBool(testpropBool);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.PROP_PropBool.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructBoolParcelable.class.getClassLoader());
        
            List<StructBool> receivedpropBool = Conversions.toList(StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("propBool", StructBoolParcelable.class)));
        assertEquals(receivedpropBool, testpropBool);
    }
    
    @Test
    public void onReceivepropIntPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SET_PropInt.getValue());
        Bundle data = new Bundle();
        List<StructInt> testpropInt = new java.util.ArrayList<>();
        testpropInt.add(Testbed1TestHelper.makeTestStructInt());
		data.putParcelableArray("propInt", StructIntParcelable.wrapArray(Conversions.toArray(testpropInt, new StructInt[0])));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropIntChanged(any(List<StructInt>.class));	    
    }
    
    @Test
     public void setPropertyRequestpropInt()
    {
        List<StructInt> testpropInt = new java.util.ArrayList<>();
        testpropInt.add(Testbed1TestHelper.makeTestStructInt());

        testedClient.setPropInt(testpropInt);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.PROP_PropInt.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructIntParcelable.class.getClassLoader());
        
            List<StructInt> receivedpropInt = Conversions.toList(StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("propInt", StructIntParcelable.class)));
        assertEquals(receivedpropInt, testpropInt);
    }
    
    @Test
    public void onReceivepropFloatPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SET_PropFloat.getValue());
        Bundle data = new Bundle();
        List<StructFloat> testpropFloat = new java.util.ArrayList<>();
        testpropFloat.add(Testbed1TestHelper.makeTestStructFloat());
		data.putParcelableArray("propFloat", StructFloatParcelable.wrapArray(Conversions.toArray(testpropFloat, new StructFloat[0])));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropFloatChanged(any(List<StructFloat>.class));	    
    }
    
    @Test
     public void setPropertyRequestpropFloat()
    {
        List<StructFloat> testpropFloat = new java.util.ArrayList<>();
        testpropFloat.add(Testbed1TestHelper.makeTestStructFloat());

        testedClient.setPropFloat(testpropFloat);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.PROP_PropFloat.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructFloatParcelable.class.getClassLoader());
        
            List<StructFloat> receivedpropFloat = Conversions.toList(StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("propFloat", StructFloatParcelable.class)));
        assertEquals(receivedpropFloat, testpropFloat);
    }
    
    @Test
    public void onReceivepropStringPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SET_PropString.getValue());
        Bundle data = new Bundle();
        List<StructString> testpropString = new java.util.ArrayList<>();
        testpropString.add(Testbed1TestHelper.makeTestStructString());
		data.putParcelableArray("propString", StructStringParcelable.wrapArray(Conversions.toArray(testpropString, new StructString[0])));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropStringChanged(any(List<StructString>.class));	    
    }
    
    @Test
     public void setPropertyRequestpropString()
    {
        List<StructString> testpropString = new java.util.ArrayList<>();
        testpropString.add(Testbed1TestHelper.makeTestStructString());

        testedClient.setPropString(testpropString);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.PROP_PropString.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructStringParcelable.class.getClassLoader());
        
            List<StructString> receivedpropString = Conversions.toList(StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("propString", StructStringParcelable.class)));
        assertEquals(receivedpropString, testpropString);
    }
    
    @Test
    public void onReceivepropEnumPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SET_PropEnum.getValue());
        Bundle data = new Bundle();
        List<Enum0> testpropEnum = new java.util.ArrayList<>();
        testpropEnum.add(Enum0.Value1);
		data.putParcelableArray("propEnum", Enum0Parcelable.wrapArray(Conversions.toArray(testpropEnum, new Enum0[0])));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropEnumChanged(testpropEnum);	    
    }
    
    @Test
     public void setPropertyRequestpropEnum()
    {
        List<Enum0> testpropEnum = new java.util.ArrayList<>();
        testpropEnum.add(Enum0.Value1);

        testedClient.setPropEnum(testpropEnum);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.PROP_PropEnum.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(Enum0Parcelable.class.getClassLoader());
        
            List<Enum0> receivedpropEnum = Conversions.toList(Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("propEnum", Enum0Parcelable.class)));
        assertEquals(receivedpropEnum, testpropEnum);
    }
    
    @Test
    public void whenNotifiedsigBool() throws RemoteException
    {

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SIG_SigBool.getValue());
        Bundle data = new Bundle();
        List<StructBool> testparamBool = new java.util.ArrayList<>();
        testparamBool.add(Testbed1TestHelper.makeTestStructBool());
		data.putParcelableArray("paramBool", StructBoolParcelable.wrapArray(Conversions.toArray(testparamBool, new StructBool[0])));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigBool( any(List<StructBool>.class));

}
    @Test
    public void whenNotifiedsigInt() throws RemoteException
    {

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SIG_SigInt.getValue());
        Bundle data = new Bundle();
        List<StructInt> testparamInt = new java.util.ArrayList<>();
        testparamInt.add(Testbed1TestHelper.makeTestStructInt());
		data.putParcelableArray("paramInt", StructIntParcelable.wrapArray(Conversions.toArray(testparamInt, new StructInt[0])));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigInt( any(List<StructInt>.class));

}
    @Test
    public void whenNotifiedsigFloat() throws RemoteException
    {

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SIG_SigFloat.getValue());
        Bundle data = new Bundle();
        List<StructFloat> testparamFloat = new java.util.ArrayList<>();
        testparamFloat.add(Testbed1TestHelper.makeTestStructFloat());
		data.putParcelableArray("paramFloat", StructFloatParcelable.wrapArray(Conversions.toArray(testparamFloat, new StructFloat[0])));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigFloat( any(List<StructFloat>.class));

}
    @Test
    public void whenNotifiedsigString() throws RemoteException
    {

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SIG_SigString.getValue());
        Bundle data = new Bundle();
        List<StructString> testparamString = new java.util.ArrayList<>();
        testparamString.add(Testbed1TestHelper.makeTestStructString());
		data.putParcelableArray("paramString", StructStringParcelable.wrapArray(Conversions.toArray(testparamString, new StructString[0])));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigString( any(List<StructString>.class));

}
    @Test
    public void whenNotifiedsigEnum() throws RemoteException
    {

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.SIG_SigEnum.getValue());
        Bundle data = new Bundle();
        List<Enum0> testparamEnum = new java.util.ArrayList<>();
        testparamEnum.add(Enum0.Value1);
		data.putParcelableArray("paramEnum", Enum0Parcelable.wrapArray(Conversions.toArray(testparamEnum, new Enum0[0])));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigEnum(testparamEnum);

}

    @Test
    public void onfuncBoolRequest() throws RemoteException {

        // Execute method
        List<StructBool> testparamBool = new java.util.ArrayList<>();
        testparamBool.add(Testbed1TestHelper.makeTestStructBool());
        List<StructBool> expectedResult = new ArrayList<>();
        expectedResult.add(Testbed1TestHelper.makeTestStructBool());

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<List<StructBool>> resFuture = testedClient.funcBoolAsync(testparamBool);

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
        
            List<StructBool> receivedparamBool = Conversions.toList(StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("paramBool", StructBoolParcelable.class)));
        assertEquals(receivedparamBool, testparamBool);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncBoolResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelableArray("result", StructBoolParcelable.wrapArray(Conversions.toArray(expectedResult, new StructBool[0])));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncIntRequest() throws RemoteException {

        // Execute method
        List<StructInt> testparamInt = new java.util.ArrayList<>();
        testparamInt.add(Testbed1TestHelper.makeTestStructInt());
        List<StructInt> expectedResult = new ArrayList<>();
        expectedResult.add(Testbed1TestHelper.makeTestStructInt());

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<List<StructInt>> resFuture = testedClient.funcIntAsync(testparamInt);

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
        
            List<StructInt> receivedparamInt = Conversions.toList(StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("paramInt", StructIntParcelable.class)));
        assertEquals(receivedparamInt, testparamInt);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncIntResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelableArray("result", StructIntParcelable.wrapArray(Conversions.toArray(expectedResult, new StructInt[0])));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncFloatRequest() throws RemoteException {

        // Execute method
        List<StructFloat> testparamFloat = new java.util.ArrayList<>();
        testparamFloat.add(Testbed1TestHelper.makeTestStructFloat());
        List<StructFloat> expectedResult = new ArrayList<>();
        expectedResult.add(Testbed1TestHelper.makeTestStructFloat());

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<List<StructFloat>> resFuture = testedClient.funcFloatAsync(testparamFloat);

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
        
            List<StructFloat> receivedparamFloat = Conversions.toList(StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("paramFloat", StructFloatParcelable.class)));
        assertEquals(receivedparamFloat, testparamFloat);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncFloatResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelableArray("result", StructFloatParcelable.wrapArray(Conversions.toArray(expectedResult, new StructFloat[0])));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncStringRequest() throws RemoteException {

        // Execute method
        List<StructString> testparamString = new java.util.ArrayList<>();
        testparamString.add(Testbed1TestHelper.makeTestStructString());
        List<StructString> expectedResult = new ArrayList<>();
        expectedResult.add(Testbed1TestHelper.makeTestStructString());

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<List<StructString>> resFuture = testedClient.funcStringAsync(testparamString);

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
        
            List<StructString> receivedparamString = Conversions.toList(StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("paramString", StructStringParcelable.class)));
        assertEquals(receivedparamString, testparamString);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncStringResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelableArray("result", StructStringParcelable.wrapArray(Conversions.toArray(expectedResult, new StructString[0])));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncEnumRequest() throws RemoteException {

        // Execute method
        List<Enum0> testparamEnum = new java.util.ArrayList<>();
        testparamEnum.add(Enum0.Value1);
        List<Enum0> expectedResult = new ArrayList<>();
        expectedResult.add(Enum0.Value1);

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<List<Enum0>> resFuture = testedClient.funcEnumAsync(testparamEnum);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(StructArrayInterfaceMessageType.RPC_FuncEnumReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(Enum0Parcelable.class.getClassLoader());
        
            List<Enum0> receivedparamEnum = Conversions.toList(Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("paramEnum", Enum0Parcelable.class)));
        assertEquals(receivedparamEnum, testparamEnum);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncEnumResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelableArray("result", Enum0Parcelable.wrapArray(Conversions.toArray(expectedResult, new Enum0[0])));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
