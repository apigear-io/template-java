//TODO later// Copyright Epic Games, Inc. All Rights Reserved.
package testbed1.testbed1_android_client;

import testbed1.testbed1_android_client.StructArray2InterfaceClient;

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

import testbed1.testbed1_api.IStructArray2InterfaceEventListener;
import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_api.AbstractStructArray2Interface;
import testbed1.testbed1_android_messenger.StructArray2InterfaceMessageType;

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


interface IStructArray2InterfaceClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class StructArray2InterfaceClientTest
{

    @Mock
    private Context mMockContext;
   
    private StructArray2InterfaceClient testedClient;
    private IStructArray2InterfaceEventListener listenerMock = mock(IStructArray2InterfaceEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private IStructArray2InterfaceClientMessageGetter serviceMessagesStorage = mock(IStructArray2InterfaceClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(StructArray2InterfaceMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(IStructArray2InterfaceClientMessageGetter messageGetterMock)
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


        testedClient = new StructArray2InterfaceClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("testbed1.testbed1_android_service", "testbed1.testbed1_android_service.StructArray2InterfaceServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(StructArray2InterfaceMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.INIT.getValue());
        Bundle data = new Bundle();
        StructBoolWithArray testpropBool = Testbed1TestHelper.makeTestStructBoolWithArray();
		data.putParcelable("propBool", new StructBoolWithArrayParcelable(testpropBool));
        StructIntWithArray testpropInt = Testbed1TestHelper.makeTestStructIntWithArray();
		data.putParcelable("propInt", new StructIntWithArrayParcelable(testpropInt));
        StructFloatWithArray testpropFloat = Testbed1TestHelper.makeTestStructFloatWithArray();
		data.putParcelable("propFloat", new StructFloatWithArrayParcelable(testpropFloat));
        StructStringWithArray testpropString = Testbed1TestHelper.makeTestStructStringWithArray();
		data.putParcelable("propString", new StructStringWithArrayParcelable(testpropString));
        StructEnumWithArray testpropEnum = Testbed1TestHelper.makeTestStructEnumWithArray();
		data.putParcelable("propEnum", new StructEnumWithArrayParcelable(testpropEnum));

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropBoolChanged(any(StructBoolWithArray.class));
        inOrderEventListener.verify(listenerMock,times(1)).onPropIntChanged(any(StructIntWithArray.class));
        inOrderEventListener.verify(listenerMock,times(1)).onPropFloatChanged(any(StructFloatWithArray.class));
        inOrderEventListener.verify(listenerMock,times(1)).onPropStringChanged(any(StructStringWithArray.class));
        inOrderEventListener.verify(listenerMock,times(1)).onPropEnumChanged(any(StructEnumWithArray.class));
    }
    @Test
    public void onReceivepropBoolPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.SET_PropBool.getValue());
        Bundle data = new Bundle();
        StructBoolWithArray testpropBool = Testbed1TestHelper.makeTestStructBoolWithArray();
		data.putParcelable("propBool", new StructBoolWithArrayParcelable(testpropBool));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropBoolChanged(any(StructBoolWithArray.class));	    
    }
    
    @Test
     public void setPropertyRequestpropBool()
    {
        StructBoolWithArray testpropBool = Testbed1TestHelper.makeTestStructBoolWithArray();

        testedClient.setPropBool(testpropBool);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.PROP_PropBool.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructBoolWithArrayParcelable.class.getClassLoader());
        
			StructBoolWithArray receivedpropBool = data.getParcelable("propBool", StructBoolWithArrayParcelable.class).getStructBoolWithArray();
        assertEquals(receivedpropBool, testpropBool);
    }
    
    @Test
    public void onReceivepropIntPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.SET_PropInt.getValue());
        Bundle data = new Bundle();
        StructIntWithArray testpropInt = Testbed1TestHelper.makeTestStructIntWithArray();
		data.putParcelable("propInt", new StructIntWithArrayParcelable(testpropInt));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropIntChanged(any(StructIntWithArray.class));	    
    }
    
    @Test
     public void setPropertyRequestpropInt()
    {
        StructIntWithArray testpropInt = Testbed1TestHelper.makeTestStructIntWithArray();

        testedClient.setPropInt(testpropInt);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.PROP_PropInt.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructIntWithArrayParcelable.class.getClassLoader());
        
			StructIntWithArray receivedpropInt = data.getParcelable("propInt", StructIntWithArrayParcelable.class).getStructIntWithArray();
        assertEquals(receivedpropInt, testpropInt);
    }
    
    @Test
    public void onReceivepropFloatPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.SET_PropFloat.getValue());
        Bundle data = new Bundle();
        StructFloatWithArray testpropFloat = Testbed1TestHelper.makeTestStructFloatWithArray();
		data.putParcelable("propFloat", new StructFloatWithArrayParcelable(testpropFloat));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropFloatChanged(any(StructFloatWithArray.class));	    
    }
    
    @Test
     public void setPropertyRequestpropFloat()
    {
        StructFloatWithArray testpropFloat = Testbed1TestHelper.makeTestStructFloatWithArray();

        testedClient.setPropFloat(testpropFloat);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.PROP_PropFloat.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructFloatWithArrayParcelable.class.getClassLoader());
        
			StructFloatWithArray receivedpropFloat = data.getParcelable("propFloat", StructFloatWithArrayParcelable.class).getStructFloatWithArray();
        assertEquals(receivedpropFloat, testpropFloat);
    }
    
    @Test
    public void onReceivepropStringPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.SET_PropString.getValue());
        Bundle data = new Bundle();
        StructStringWithArray testpropString = Testbed1TestHelper.makeTestStructStringWithArray();
		data.putParcelable("propString", new StructStringWithArrayParcelable(testpropString));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropStringChanged(any(StructStringWithArray.class));	    
    }
    
    @Test
     public void setPropertyRequestpropString()
    {
        StructStringWithArray testpropString = Testbed1TestHelper.makeTestStructStringWithArray();

        testedClient.setPropString(testpropString);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.PROP_PropString.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructStringWithArrayParcelable.class.getClassLoader());
        
			StructStringWithArray receivedpropString = data.getParcelable("propString", StructStringWithArrayParcelable.class).getStructStringWithArray();
        assertEquals(receivedpropString, testpropString);
    }
    
    @Test
    public void onReceivepropEnumPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.SET_PropEnum.getValue());
        Bundle data = new Bundle();
        StructEnumWithArray testpropEnum = Testbed1TestHelper.makeTestStructEnumWithArray();
		data.putParcelable("propEnum", new StructEnumWithArrayParcelable(testpropEnum));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onPropEnumChanged(any(StructEnumWithArray.class));	    
    }
    
    @Test
     public void setPropertyRequestpropEnum()
    {
        StructEnumWithArray testpropEnum = Testbed1TestHelper.makeTestStructEnumWithArray();

        testedClient.setPropEnum(testpropEnum);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.PROP_PropEnum.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(StructEnumWithArrayParcelable.class.getClassLoader());
        
			StructEnumWithArray receivedpropEnum = data.getParcelable("propEnum", StructEnumWithArrayParcelable.class).getStructEnumWithArray();
        assertEquals(receivedpropEnum, testpropEnum);
    }
    
    @Test
    public void whenNotifiedsigBool() throws RemoteException
    {

        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.SIG_SigBool.getValue());
        Bundle data = new Bundle();
        StructBoolWithArray testparamBool = Testbed1TestHelper.makeTestStructBoolWithArray();
		data.putParcelable("paramBool", new StructBoolWithArrayParcelable(testparamBool));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigBool( any(StructBoolWithArray.class));

}
    @Test
    public void whenNotifiedsigInt() throws RemoteException
    {

        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.SIG_SigInt.getValue());
        Bundle data = new Bundle();
        StructIntWithArray testparamInt = Testbed1TestHelper.makeTestStructIntWithArray();
		data.putParcelable("paramInt", new StructIntWithArrayParcelable(testparamInt));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigInt( any(StructIntWithArray.class));

}
    @Test
    public void whenNotifiedsigFloat() throws RemoteException
    {

        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.SIG_SigFloat.getValue());
        Bundle data = new Bundle();
        StructFloatWithArray testparamFloat = Testbed1TestHelper.makeTestStructFloatWithArray();
		data.putParcelable("paramFloat", new StructFloatWithArrayParcelable(testparamFloat));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigFloat( any(StructFloatWithArray.class));

}
    @Test
    public void whenNotifiedsigString() throws RemoteException
    {

        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.SIG_SigString.getValue());
        Bundle data = new Bundle();
        StructStringWithArray testparamString = Testbed1TestHelper.makeTestStructStringWithArray();
		data.putParcelable("paramString", new StructStringWithArrayParcelable(testparamString));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigString( any(StructStringWithArray.class));

}

    @Test
    public void onfuncBoolRequest() throws RemoteException {

        // Execute method
        StructBoolWithArray testparamBool = Testbed1TestHelper.makeTestStructBoolWithArray();
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
        assertEquals(StructArray2InterfaceMessageType.RPC_FuncBoolReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(StructBoolWithArrayParcelable.class.getClassLoader());
        
			StructBoolWithArray receivedparamBool = data.getParcelable("paramBool", StructBoolWithArrayParcelable.class).getStructBoolWithArray();
        assertEquals(receivedparamBool, testparamBool);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.RPC_FuncBoolResp.getValue());

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
        StructIntWithArray testparamInt = Testbed1TestHelper.makeTestStructIntWithArray();
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
        assertEquals(StructArray2InterfaceMessageType.RPC_FuncIntReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(StructIntWithArrayParcelable.class.getClassLoader());
        
			StructIntWithArray receivedparamInt = data.getParcelable("paramInt", StructIntWithArrayParcelable.class).getStructIntWithArray();
        assertEquals(receivedparamInt, testparamInt);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.RPC_FuncIntResp.getValue());

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
        StructFloatWithArray testparamFloat = Testbed1TestHelper.makeTestStructFloatWithArray();
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
        assertEquals(StructArray2InterfaceMessageType.RPC_FuncFloatReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(StructFloatWithArrayParcelable.class.getClassLoader());
        
			StructFloatWithArray receivedparamFloat = data.getParcelable("paramFloat", StructFloatWithArrayParcelable.class).getStructFloatWithArray();
        assertEquals(receivedparamFloat, testparamFloat);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.RPC_FuncFloatResp.getValue());

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
        StructStringWithArray testparamString = Testbed1TestHelper.makeTestStructStringWithArray();
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
        assertEquals(StructArray2InterfaceMessageType.RPC_FuncStringReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(StructStringWithArrayParcelable.class.getClassLoader());
        
			StructStringWithArray receivedparamString = data.getParcelable("paramString", StructStringWithArrayParcelable.class).getStructStringWithArray();
        assertEquals(receivedparamString, testparamString);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.RPC_FuncStringResp.getValue());

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
        StructEnumWithArray testparamEnum = Testbed1TestHelper.makeTestStructEnumWithArray();
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
        assertEquals(StructArray2InterfaceMessageType.RPC_FuncEnumReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(StructEnumWithArrayParcelable.class.getClassLoader());
        
			StructEnumWithArray receivedparamEnum = data.getParcelable("paramEnum", StructEnumWithArrayParcelable.class).getStructEnumWithArray();
        assertEquals(receivedparamEnum, testparamEnum);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.RPC_FuncEnumResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelableArray("result", Enum0Parcelable.wrapArray(Conversions.toArray(expectedResult, new Enum0[0])));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
