//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package testbed1.testbed1_android_service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import testbed1.testbed1_android_service.StructArray2InterfaceServiceAdapter;

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


import testbed1.testbed1_api.IStructArray2InterfaceEventListener;
import testbed1.testbed1_android_service.IStructArray2InterfaceServiceFactory;
import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_api.AbstractStructArray2Interface;
import testbed1.testbed1_android_messenger.StructArray2InterfaceMessageType;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import androidx.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.InOrder;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ServiceController;
import org.robolectric.annotation.Config;
import org.robolectric.RuntimeEnvironment;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

interface IStructArray2InterfaceMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class StructArray2InterfaceServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private StructArray2InterfaceServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private IStructArray2InterfaceEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private IStructArray2Interface backendServiceMock = mock(IStructArray2Interface.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private IStructArray2InterfaceServiceFactory serviceFactory = mock(IStructArray2InterfaceServiceFactory.class);
    private IStructArray2InterfaceMessageGetter clientMessagesStorage = mock(IStructArray2InterfaceMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, StructArray2InterfaceMessageType.UNREGISTER_CLIENT.ordinal());
        unregisterMsg.getData().putString("connectionID", mTestConnectionID1);
	   
        try {
            mServiceMessenger.send(unregisterMsg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        if (mMockContext != null) {
            mMockContext.stopService(testedServiceAdapterIntent);  
        }
        testedServiceAdapter.onDestroy();
        inOrderBackendService.verify(backendServiceMock, times(1)).removeEventListener(testedAdapterAsEventListener);
    }

    Handler createClientHandlerMock(IStructArray2InterfaceMessageGetter messageGetterMock)
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

    void registerFakeActivityClient(Messenger messenger, String id)
    {
        StructBoolWithArray initpropBool = new StructBoolWithArray();
        //TODO fill fields
        when(backendServiceMock.getPropBool()).thenReturn(initpropBool);
        StructIntWithArray initpropInt = new StructIntWithArray();
        //TODO fill fields
        when(backendServiceMock.getPropInt()).thenReturn(initpropInt);
        StructFloatWithArray initpropFloat = new StructFloatWithArray();
        //TODO fill fields
        when(backendServiceMock.getPropFloat()).thenReturn(initpropFloat);
        StructStringWithArray initpropString = new StructStringWithArray();
        //TODO fill fields
        when(backendServiceMock.getPropString()).thenReturn(initpropString);
        StructEnumWithArray initpropEnum = new StructEnumWithArray();
        //TODO fill fields
        when(backendServiceMock.getPropEnum()).thenReturn(initpropEnum);


        Message registerMsg = Message.obtain(null, StructArray2InterfaceMessageType.REGISTER_CLIENT.ordinal());
        registerMsg.getData().putString("connectionID", id);
        registerMsg.replyTo = messenger;
    
        try {
            mServiceMessenger.send(registerMsg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropBool();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropInt();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropFloat();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropString();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropEnum();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        
			StructBoolWithArray receivedpropBool = data.getParcelable("propBool", StructBoolWithArrayParcelable.class).getStructBoolWithArray();
        
			StructIntWithArray receivedpropInt = data.getParcelable("propInt", StructIntWithArrayParcelable.class).getStructIntWithArray();
        
			StructFloatWithArray receivedpropFloat = data.getParcelable("propFloat", StructFloatWithArrayParcelable.class).getStructFloatWithArray();
        
			StructStringWithArray receivedpropString = data.getParcelable("propString", StructStringWithArrayParcelable.class).getStructStringWithArray();
        
			StructEnumWithArray receivedpropEnum = data.getParcelable("propEnum", StructEnumWithArrayParcelable.class).getStructEnumWithArray();
        
        data.setClassLoader(StructBoolWithArrayParcelable.class.getClassLoader());
        // assertEquals(receivedpropBool, initpropBool);
        // assertEquals(receivedpropInt, initpropInt);
        // assertEquals(receivedpropFloat, initpropFloat);
        // assertEquals(receivedpropString, initpropString);
        // assertEquals(receivedpropEnum, initpropEnum);

    }

    @Before
    public void setUp() throws RemoteException
    {
        clientReplyHandler = createClientHandlerMock(clientMessagesStorage);
        clientReplyMessenger = new Messenger(clientReplyHandler);
	    inOrderClientMessagesHandler = inOrder(clientMessagesStorage);
	    inOrderBackendService = inOrder(backendServiceMock);
        mMockContext = RuntimeEnvironment.getApplication();

        when(backendServiceMock._isReady()).thenReturn(true);

        testedServiceAdapterIntent = new Intent(mMockContext, StructArray2InterfaceServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(StructArray2InterfaceServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(serviceFactory.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(serviceFactory);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<IStructArray2InterfaceEventListener> eventListnerCaptor = ArgumentCaptor.forClass(IStructArray2InterfaceEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
    @Test
    public void onReceivepropBoolPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.PROP_PropBool.getValue());
        Bundle data = new Bundle();
        StructBoolWithArray testpropBool = Testbed1TestHelper.makeTestStructBoolWithArray();
		data.putParcelable("propBool", new StructBoolWithArrayParcelable(testpropBool));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropBool( any(StructBoolWithArray.class));
	    
    }

    @Test
     public void whenNotifiedpropBool()
    {
        StructBoolWithArray testpropBool = Testbed1TestHelper.makeTestStructBoolWithArray();

        testedAdapterAsEventListener.onPropBoolChanged(testpropBool);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.SET_PropBool.getValue(), response.what);
        Bundle data = response.getData();

        
			StructBoolWithArray receivedpropBool = data.getParcelable("propBool", StructBoolWithArrayParcelable.class).getStructBoolWithArray();

        assertEquals(receivedpropBool, testpropBool);
    }
    @Test
    public void onReceivepropIntPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.PROP_PropInt.getValue());
        Bundle data = new Bundle();
        StructIntWithArray testpropInt = Testbed1TestHelper.makeTestStructIntWithArray();
		data.putParcelable("propInt", new StructIntWithArrayParcelable(testpropInt));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropInt( any(StructIntWithArray.class));
	    
    }

    @Test
     public void whenNotifiedpropInt()
    {
        StructIntWithArray testpropInt = Testbed1TestHelper.makeTestStructIntWithArray();

        testedAdapterAsEventListener.onPropIntChanged(testpropInt);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.SET_PropInt.getValue(), response.what);
        Bundle data = response.getData();

        
			StructIntWithArray receivedpropInt = data.getParcelable("propInt", StructIntWithArrayParcelable.class).getStructIntWithArray();

        assertEquals(receivedpropInt, testpropInt);
    }
    @Test
    public void onReceivepropFloatPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.PROP_PropFloat.getValue());
        Bundle data = new Bundle();
        StructFloatWithArray testpropFloat = Testbed1TestHelper.makeTestStructFloatWithArray();
		data.putParcelable("propFloat", new StructFloatWithArrayParcelable(testpropFloat));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropFloat( any(StructFloatWithArray.class));
	    
    }

    @Test
     public void whenNotifiedpropFloat()
    {
        StructFloatWithArray testpropFloat = Testbed1TestHelper.makeTestStructFloatWithArray();

        testedAdapterAsEventListener.onPropFloatChanged(testpropFloat);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.SET_PropFloat.getValue(), response.what);
        Bundle data = response.getData();

        
			StructFloatWithArray receivedpropFloat = data.getParcelable("propFloat", StructFloatWithArrayParcelable.class).getStructFloatWithArray();

        assertEquals(receivedpropFloat, testpropFloat);
    }
    @Test
    public void onReceivepropStringPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.PROP_PropString.getValue());
        Bundle data = new Bundle();
        StructStringWithArray testpropString = Testbed1TestHelper.makeTestStructStringWithArray();
		data.putParcelable("propString", new StructStringWithArrayParcelable(testpropString));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropString( any(StructStringWithArray.class));
	    
    }

    @Test
     public void whenNotifiedpropString()
    {
        StructStringWithArray testpropString = Testbed1TestHelper.makeTestStructStringWithArray();

        testedAdapterAsEventListener.onPropStringChanged(testpropString);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.SET_PropString.getValue(), response.what);
        Bundle data = response.getData();

        
			StructStringWithArray receivedpropString = data.getParcelable("propString", StructStringWithArrayParcelable.class).getStructStringWithArray();

        assertEquals(receivedpropString, testpropString);
    }
    @Test
    public void onReceivepropEnumPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.PROP_PropEnum.getValue());
        Bundle data = new Bundle();
        StructEnumWithArray testpropEnum = Testbed1TestHelper.makeTestStructEnumWithArray();
		data.putParcelable("propEnum", new StructEnumWithArrayParcelable(testpropEnum));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropEnum( any(StructEnumWithArray.class));
	    
    }

    @Test
     public void whenNotifiedpropEnum()
    {
        StructEnumWithArray testpropEnum = Testbed1TestHelper.makeTestStructEnumWithArray();

        testedAdapterAsEventListener.onPropEnumChanged(testpropEnum);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.SET_PropEnum.getValue(), response.what);
        Bundle data = response.getData();

        
			StructEnumWithArray receivedpropEnum = data.getParcelable("propEnum", StructEnumWithArrayParcelable.class).getStructEnumWithArray();

        assertEquals(receivedpropEnum, testpropEnum);
    }
    @Test
    public void whenNotifiedsigBool()
    {
        StructBoolWithArray testparamBool = Testbed1TestHelper.makeTestStructBoolWithArray();

        testedAdapterAsEventListener.onSigBool(testparamBool);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.SIG_SigBool.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructBoolWithArrayParcelable.class.getClassLoader());
        
			StructBoolWithArray receivedparamBool = data.getParcelable("paramBool", StructBoolWithArrayParcelable.class).getStructBoolWithArray();
        assertEquals(receivedparamBool, testparamBool);
}
    @Test
    public void whenNotifiedsigInt()
    {
        StructIntWithArray testparamInt = Testbed1TestHelper.makeTestStructIntWithArray();

        testedAdapterAsEventListener.onSigInt(testparamInt);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.SIG_SigInt.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructIntWithArrayParcelable.class.getClassLoader());
        
			StructIntWithArray receivedparamInt = data.getParcelable("paramInt", StructIntWithArrayParcelable.class).getStructIntWithArray();
        assertEquals(receivedparamInt, testparamInt);
}
    @Test
    public void whenNotifiedsigFloat()
    {
        StructFloatWithArray testparamFloat = Testbed1TestHelper.makeTestStructFloatWithArray();

        testedAdapterAsEventListener.onSigFloat(testparamFloat);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.SIG_SigFloat.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructFloatWithArrayParcelable.class.getClassLoader());
        
			StructFloatWithArray receivedparamFloat = data.getParcelable("paramFloat", StructFloatWithArrayParcelable.class).getStructFloatWithArray();
        assertEquals(receivedparamFloat, testparamFloat);
}
    @Test
    public void whenNotifiedsigString()
    {
        StructStringWithArray testparamString = Testbed1TestHelper.makeTestStructStringWithArray();

        testedAdapterAsEventListener.onSigString(testparamString);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.SIG_SigString.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructStringWithArrayParcelable.class.getClassLoader());
        
			StructStringWithArray receivedparamString = data.getParcelable("paramString", StructStringWithArrayParcelable.class).getStructStringWithArray();
        assertEquals(receivedparamString, testparamString);
}


    public void onfuncBoolRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.RPC_FuncBoolReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        StructBoolWithArray testparamBool = Testbed1TestHelper.makeTestStructBoolWithArray();
		data.putParcelable("paramBool", new StructBoolWithArrayParcelable(testparamBool));
        StructBool[] returnedValue = new StructBool[1];
        returnedValue[0] = Testbed1TestHelper.makeTestStructBool();


        when(backendServiceMock.funcBool( any(StructBoolWithArray.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcBool( any(StructBoolWithArray.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.RPC_FuncBoolResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(StructBoolParcelable.class.getClassLoader());
        StructBool[] receivedByClient =  StructBoolParcelable.unwrapArray((StructBoolParcelable[])resp_data.getParcelableArray("result", StructBoolParcelable.class));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncIntRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.RPC_FuncIntReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        StructIntWithArray testparamInt = Testbed1TestHelper.makeTestStructIntWithArray();
		data.putParcelable("paramInt", new StructIntWithArrayParcelable(testparamInt));
        StructInt[] returnedValue = new StructInt[1];
        returnedValue[0] = Testbed1TestHelper.makeTestStructInt();


        when(backendServiceMock.funcInt( any(StructIntWithArray.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcInt( any(StructIntWithArray.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.RPC_FuncIntResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(StructIntParcelable.class.getClassLoader());
        StructInt[] receivedByClient =  StructIntParcelable.unwrapArray((StructIntParcelable[])resp_data.getParcelableArray("result", StructIntParcelable.class));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncFloatRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.RPC_FuncFloatReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        StructFloatWithArray testparamFloat = Testbed1TestHelper.makeTestStructFloatWithArray();
		data.putParcelable("paramFloat", new StructFloatWithArrayParcelable(testparamFloat));
        StructFloat[] returnedValue = new StructFloat[1];
        returnedValue[0] = Testbed1TestHelper.makeTestStructFloat();


        when(backendServiceMock.funcFloat( any(StructFloatWithArray.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcFloat( any(StructFloatWithArray.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.RPC_FuncFloatResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(StructFloatParcelable.class.getClassLoader());
        StructFloat[] receivedByClient =  StructFloatParcelable.unwrapArray((StructFloatParcelable[])resp_data.getParcelableArray("result", StructFloatParcelable.class));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncStringRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.RPC_FuncStringReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        StructStringWithArray testparamString = Testbed1TestHelper.makeTestStructStringWithArray();
		data.putParcelable("paramString", new StructStringWithArrayParcelable(testparamString));
        StructString[] returnedValue = new StructString[1];
        returnedValue[0] = Testbed1TestHelper.makeTestStructString();


        when(backendServiceMock.funcString( any(StructStringWithArray.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcString( any(StructStringWithArray.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.RPC_FuncStringResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(StructStringParcelable.class.getClassLoader());
        StructString[] receivedByClient =  StructStringParcelable.unwrapArray((StructStringParcelable[])resp_data.getParcelableArray("result", StructStringParcelable.class));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncEnumRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArray2InterfaceMessageType.RPC_FuncEnumReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        StructEnumWithArray testparamEnum = Testbed1TestHelper.makeTestStructEnumWithArray();
		data.putParcelable("paramEnum", new StructEnumWithArrayParcelable(testparamEnum));
        Enum0[] returnedValue = new Enum0[1];
        returnedValue[0] = Enum0.Value1;


        when(backendServiceMock.funcEnum( any(StructEnumWithArray.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcEnum( any(StructEnumWithArray.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArray2InterfaceMessageType.RPC_FuncEnumResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(Enum0Parcelable.class.getClassLoader());
        Enum0[] receivedByClient =  Enum0Parcelable.unwrapArray((Enum0Parcelable[])resp_data.getParcelableArray("result", Enum0Parcelable.class));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
