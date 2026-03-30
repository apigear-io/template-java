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
import testbed1.testbed1_android_service.StructArrayInterfaceServiceAdapter;
import testbed1.testbed1_android_service.IStructArrayInterfaceServiceProvider;

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
import testbed1.testbed1_android_service.IStructArrayInterfaceServiceProvider;
import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_api.AbstractStructArrayInterface;
import testbed1.testbed1_android_messenger.StructArrayInterfaceMessageType;


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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import testbed1.testbed1_android_messenger.Conversions;

interface IStructArrayInterfaceMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class StructArrayInterfaceServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private StructArrayInterfaceServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private IStructArrayInterfaceEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private IStructArrayInterface backendServiceMock = mock(IStructArrayInterface.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private IStructArrayInterfaceServiceProvider ServiceProvider = mock(IStructArrayInterfaceServiceProvider.class);
    private IStructArrayInterfaceMessageGetter clientMessagesStorage = mock(IStructArrayInterfaceMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, StructArrayInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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

    Handler createClientHandlerMock(IStructArrayInterfaceMessageGetter messageGetterMock)
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
        List<StructBool> initpropBool = new ArrayList<>();
        initpropBool.add(new StructBool());
        when(backendServiceMock.getPropBool()).thenReturn(initpropBool);
        List<StructInt> initpropInt = new ArrayList<>();
        initpropInt.add(new StructInt());
        when(backendServiceMock.getPropInt()).thenReturn(initpropInt);
        List<StructFloat> initpropFloat = new ArrayList<>();
        initpropFloat.add(new StructFloat());
        when(backendServiceMock.getPropFloat()).thenReturn(initpropFloat);
        List<StructString> initpropString = new ArrayList<>();
        initpropString.add(new StructString());
        when(backendServiceMock.getPropString()).thenReturn(initpropString);
        List<Enum0> initpropEnum = new ArrayList<>();
        initpropEnum.add(Enum0.Value1);
        when(backendServiceMock.getPropEnum()).thenReturn(initpropEnum);


        Message registerMsg = Message.obtain(null, StructArrayInterfaceMessageType.REGISTER_CLIENT.ordinal());
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

        assertEquals(StructArrayInterfaceMessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        
            List<StructBool> receivedpropBool = Conversions.toList(StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("propBool", StructBoolParcelable.class)));
        
            List<StructInt> receivedpropInt = Conversions.toList(StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("propInt", StructIntParcelable.class)));
        
            List<StructFloat> receivedpropFloat = Conversions.toList(StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("propFloat", StructFloatParcelable.class)));
        
            List<StructString> receivedpropString = Conversions.toList(StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("propString", StructStringParcelable.class)));
        
            List<Enum0> receivedpropEnum = Conversions.toList(Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("propEnum", Enum0Parcelable.class)));
        
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(StructBoolParcelable.class.getClassLoader());
        // assertEquals(receivedpropBool, initpropBool);
        // assertEquals(receivedpropInt, initpropInt);
        // assertEquals(receivedpropFloat, initpropFloat);
        // assertEquals(receivedpropString, initpropString);
        assertEquals(receivedpropEnum, initpropEnum);

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

        testedServiceAdapterIntent = new Intent(mMockContext, StructArrayInterfaceServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(StructArrayInterfaceServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(ServiceProvider.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(ServiceProvider);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<IStructArrayInterfaceEventListener> eventListnerCaptor = ArgumentCaptor.forClass(IStructArrayInterfaceEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
    @Test
    public void onReceivepropBoolPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.PROP_PropBool.getValue());
        Bundle data = new Bundle();
        List<StructBool> testpropBool = new java.util.ArrayList<>();
        testpropBool.add(Testbed1TestHelper.makeTestStructBool());
		data.putParcelableArray("propBool", StructBoolParcelable.wrapArray(Conversions.toArray(testpropBool, new StructBool[0])));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropBool( any(List<StructBool>.class));
	    
    }

    @Test
     public void whenNotifiedpropBool()
    {
        List<StructBool> testpropBool = new java.util.ArrayList<>();
        testpropBool.add(Testbed1TestHelper.makeTestStructBool());

        testedAdapterAsEventListener.onPropBoolChanged(testpropBool);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SET_PropBool.getValue(), response.what);
        Bundle data = response.getData();

        
            List<StructBool> receivedpropBool = Conversions.toList(StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("propBool", StructBoolParcelable.class)));

        assertEquals(receivedpropBool, testpropBool);
    }
    @Test
    public void onReceivepropIntPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.PROP_PropInt.getValue());
        Bundle data = new Bundle();
        List<StructInt> testpropInt = new java.util.ArrayList<>();
        testpropInt.add(Testbed1TestHelper.makeTestStructInt());
		data.putParcelableArray("propInt", StructIntParcelable.wrapArray(Conversions.toArray(testpropInt, new StructInt[0])));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropInt( any(List<StructInt>.class));
	    
    }

    @Test
     public void whenNotifiedpropInt()
    {
        List<StructInt> testpropInt = new java.util.ArrayList<>();
        testpropInt.add(Testbed1TestHelper.makeTestStructInt());

        testedAdapterAsEventListener.onPropIntChanged(testpropInt);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SET_PropInt.getValue(), response.what);
        Bundle data = response.getData();

        
            List<StructInt> receivedpropInt = Conversions.toList(StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("propInt", StructIntParcelable.class)));

        assertEquals(receivedpropInt, testpropInt);
    }
    @Test
    public void onReceivepropFloatPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.PROP_PropFloat.getValue());
        Bundle data = new Bundle();
        List<StructFloat> testpropFloat = new java.util.ArrayList<>();
        testpropFloat.add(Testbed1TestHelper.makeTestStructFloat());
		data.putParcelableArray("propFloat", StructFloatParcelable.wrapArray(Conversions.toArray(testpropFloat, new StructFloat[0])));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropFloat( any(List<StructFloat>.class));
	    
    }

    @Test
     public void whenNotifiedpropFloat()
    {
        List<StructFloat> testpropFloat = new java.util.ArrayList<>();
        testpropFloat.add(Testbed1TestHelper.makeTestStructFloat());

        testedAdapterAsEventListener.onPropFloatChanged(testpropFloat);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SET_PropFloat.getValue(), response.what);
        Bundle data = response.getData();

        
            List<StructFloat> receivedpropFloat = Conversions.toList(StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("propFloat", StructFloatParcelable.class)));

        assertEquals(receivedpropFloat, testpropFloat);
    }
    @Test
    public void onReceivepropStringPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.PROP_PropString.getValue());
        Bundle data = new Bundle();
        List<StructString> testpropString = new java.util.ArrayList<>();
        testpropString.add(Testbed1TestHelper.makeTestStructString());
		data.putParcelableArray("propString", StructStringParcelable.wrapArray(Conversions.toArray(testpropString, new StructString[0])));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropString( any(List<StructString>.class));
	    
    }

    @Test
     public void whenNotifiedpropString()
    {
        List<StructString> testpropString = new java.util.ArrayList<>();
        testpropString.add(Testbed1TestHelper.makeTestStructString());

        testedAdapterAsEventListener.onPropStringChanged(testpropString);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SET_PropString.getValue(), response.what);
        Bundle data = response.getData();

        
            List<StructString> receivedpropString = Conversions.toList(StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("propString", StructStringParcelable.class)));

        assertEquals(receivedpropString, testpropString);
    }
    @Test
    public void onReceivepropEnumPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.PROP_PropEnum.getValue());
        Bundle data = new Bundle();
        List<Enum0> testpropEnum = new java.util.ArrayList<>();
        testpropEnum.add(Enum0.Value1);
		data.putParcelableArray("propEnum", Enum0Parcelable.wrapArray(Conversions.toArray(testpropEnum, new Enum0[0])));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropEnum(testpropEnum);
	    
    }

    @Test
     public void whenNotifiedpropEnum()
    {
        List<Enum0> testpropEnum = new java.util.ArrayList<>();
        testpropEnum.add(Enum0.Value1);

        testedAdapterAsEventListener.onPropEnumChanged(testpropEnum);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SET_PropEnum.getValue(), response.what);
        Bundle data = response.getData();

        
            List<Enum0> receivedpropEnum = Conversions.toList(Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("propEnum", Enum0Parcelable.class)));

        assertEquals(receivedpropEnum, testpropEnum);
    }
    @Test
    public void whenNotifiedsigBool()
    {
        List<StructBool> testparamBool = new java.util.ArrayList<>();
        testparamBool.add(Testbed1TestHelper.makeTestStructBool());

        testedAdapterAsEventListener.onSigBool(testparamBool);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SIG_SigBool.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructBoolParcelable.class.getClassLoader());
        
            List<StructBool> receivedparamBool = Conversions.toList(StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("paramBool", StructBoolParcelable.class)));
        assertEquals(receivedparamBool, testparamBool);
}
    @Test
    public void whenNotifiedsigInt()
    {
        List<StructInt> testparamInt = new java.util.ArrayList<>();
        testparamInt.add(Testbed1TestHelper.makeTestStructInt());

        testedAdapterAsEventListener.onSigInt(testparamInt);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SIG_SigInt.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructIntParcelable.class.getClassLoader());
        
            List<StructInt> receivedparamInt = Conversions.toList(StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("paramInt", StructIntParcelable.class)));
        assertEquals(receivedparamInt, testparamInt);
}
    @Test
    public void whenNotifiedsigFloat()
    {
        List<StructFloat> testparamFloat = new java.util.ArrayList<>();
        testparamFloat.add(Testbed1TestHelper.makeTestStructFloat());

        testedAdapterAsEventListener.onSigFloat(testparamFloat);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SIG_SigFloat.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructFloatParcelable.class.getClassLoader());
        
            List<StructFloat> receivedparamFloat = Conversions.toList(StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("paramFloat", StructFloatParcelable.class)));
        assertEquals(receivedparamFloat, testparamFloat);
}
    @Test
    public void whenNotifiedsigString()
    {
        List<StructString> testparamString = new java.util.ArrayList<>();
        testparamString.add(Testbed1TestHelper.makeTestStructString());

        testedAdapterAsEventListener.onSigString(testparamString);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SIG_SigString.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructStringParcelable.class.getClassLoader());
        
            List<StructString> receivedparamString = Conversions.toList(StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("paramString", StructStringParcelable.class)));
        assertEquals(receivedparamString, testparamString);
}
    @Test
    public void whenNotifiedsigEnum()
    {
        List<Enum0> testparamEnum = new java.util.ArrayList<>();
        testparamEnum.add(Enum0.Value1);

        testedAdapterAsEventListener.onSigEnum(testparamEnum);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SIG_SigEnum.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(Enum0Parcelable.class.getClassLoader());
        
            List<Enum0> receivedparamEnum = Conversions.toList(Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("paramEnum", Enum0Parcelable.class)));
        assertEquals(receivedparamEnum, testparamEnum);
}

    @Test
    public void onfuncBoolRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncBoolReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        List<StructBool> testparamBool = new java.util.ArrayList<>();
        testparamBool.add(Testbed1TestHelper.makeTestStructBool());
		data.putParcelableArray("paramBool", StructBoolParcelable.wrapArray(Conversions.toArray(testparamBool, new StructBool[0])));
        List<StructBool> returnedValue = new ArrayList<>();
        returnedValue.add(Testbed1TestHelper.makeTestStructBool());


        when(backendServiceMock.funcBool( any(List<StructBool>.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcBool( any(List<StructBool>.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.RPC_FuncBoolResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(StructBoolParcelable.class.getClassLoader());
        List<StructBool> receivedByClient = Conversions.toList(StructBoolParcelable.unwrapArray((StructBoolParcelable[])resp_data.getParcelableArray("result", StructBoolParcelable.class)));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onfuncIntRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncIntReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        List<StructInt> testparamInt = new java.util.ArrayList<>();
        testparamInt.add(Testbed1TestHelper.makeTestStructInt());
		data.putParcelableArray("paramInt", StructIntParcelable.wrapArray(Conversions.toArray(testparamInt, new StructInt[0])));
        List<StructInt> returnedValue = new ArrayList<>();
        returnedValue.add(Testbed1TestHelper.makeTestStructInt());


        when(backendServiceMock.funcInt( any(List<StructInt>.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcInt( any(List<StructInt>.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.RPC_FuncIntResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(StructIntParcelable.class.getClassLoader());
        List<StructInt> receivedByClient = Conversions.toList(StructIntParcelable.unwrapArray((StructIntParcelable[])resp_data.getParcelableArray("result", StructIntParcelable.class)));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onfuncFloatRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncFloatReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        List<StructFloat> testparamFloat = new java.util.ArrayList<>();
        testparamFloat.add(Testbed1TestHelper.makeTestStructFloat());
		data.putParcelableArray("paramFloat", StructFloatParcelable.wrapArray(Conversions.toArray(testparamFloat, new StructFloat[0])));
        List<StructFloat> returnedValue = new ArrayList<>();
        returnedValue.add(Testbed1TestHelper.makeTestStructFloat());


        when(backendServiceMock.funcFloat( any(List<StructFloat>.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcFloat( any(List<StructFloat>.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.RPC_FuncFloatResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(StructFloatParcelable.class.getClassLoader());
        List<StructFloat> receivedByClient = Conversions.toList(StructFloatParcelable.unwrapArray((StructFloatParcelable[])resp_data.getParcelableArray("result", StructFloatParcelable.class)));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onfuncStringRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncStringReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        List<StructString> testparamString = new java.util.ArrayList<>();
        testparamString.add(Testbed1TestHelper.makeTestStructString());
		data.putParcelableArray("paramString", StructStringParcelable.wrapArray(Conversions.toArray(testparamString, new StructString[0])));
        List<StructString> returnedValue = new ArrayList<>();
        returnedValue.add(Testbed1TestHelper.makeTestStructString());


        when(backendServiceMock.funcString( any(List<StructString>.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcString( any(List<StructString>.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.RPC_FuncStringResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(StructStringParcelable.class.getClassLoader());
        List<StructString> receivedByClient = Conversions.toList(StructStringParcelable.unwrapArray((StructStringParcelable[])resp_data.getParcelableArray("result", StructStringParcelable.class)));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onfuncEnumRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncEnumReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        List<Enum0> testparamEnum = new java.util.ArrayList<>();
        testparamEnum.add(Enum0.Value1);
		data.putParcelableArray("paramEnum", Enum0Parcelable.wrapArray(Conversions.toArray(testparamEnum, new Enum0[0])));
        List<Enum0> returnedValue = new ArrayList<>();
        returnedValue.add(Enum0.Value1);


        when(backendServiceMock.funcEnum(testparamEnum)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcEnum(testparamEnum);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.RPC_FuncEnumResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(Enum0Parcelable.class.getClassLoader());
        List<Enum0> receivedByClient = Conversions.toList(Enum0Parcelable.unwrapArray((Enum0Parcelable[])resp_data.getParcelableArray("result", Enum0Parcelable.class)));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
