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


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        StructBool init_elementpropBool = new StructBool();
        // todo fill if is struct
        StructBool[] initpropBool = new StructBool[]{ init_elementpropBool } ;
        when(backendServiceMock.getPropBool()).thenReturn(initpropBool);
        StructInt init_elementpropInt = new StructInt();
        // todo fill if is struct
        StructInt[] initpropInt = new StructInt[]{ init_elementpropInt } ;
        when(backendServiceMock.getPropInt()).thenReturn(initpropInt);
        StructFloat init_elementpropFloat = new StructFloat();
        // todo fill if is struct
        StructFloat[] initpropFloat = new StructFloat[]{ init_elementpropFloat } ;
        when(backendServiceMock.getPropFloat()).thenReturn(initpropFloat);
        StructString init_elementpropString = new StructString();
        // todo fill if is struct
        StructString[] initpropString = new StructString[]{ init_elementpropString } ;
        when(backendServiceMock.getPropString()).thenReturn(initpropString);
        Enum0 init_elementpropEnum = Enum0.Value1;
        // todo fill if is struct
        Enum0[] initpropEnum = new Enum0[]{ init_elementpropEnum } ;
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
        
            StructBool[] receivedpropBool =  StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("propBool", StructBoolParcelable.class));
        
            StructInt[] receivedpropInt =  StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("propInt", StructIntParcelable.class));
        
            StructFloat[] receivedpropFloat =  StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("propFloat", StructFloatParcelable.class));
        
            StructString[] receivedpropString =  StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("propString", StructStringParcelable.class));
        
            Enum0[] receivedpropEnum =  Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("propEnum", Enum0Parcelable.class));
        
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
        StructBool[] testpropBool = new StructBool[1];
        testpropBool[0] = Testbed1TestHelper.makeTestStructBool();
		data.putParcelableArray("propBool", StructBoolParcelable.wrapArray(testpropBool));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropBool( any(StructBool[].class));
	    
    }

    @Test
     public void whenNotifiedpropBool()
    {
        StructBool[] testpropBool = new StructBool[1];
        testpropBool[0] = Testbed1TestHelper.makeTestStructBool();

        testedAdapterAsEventListener.onPropBoolChanged(testpropBool);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SET_PropBool.getValue(), response.what);
        Bundle data = response.getData();

        
            StructBool[] receivedpropBool =  StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("propBool", StructBoolParcelable.class));

        assertEquals(receivedpropBool, testpropBool);
    }
    @Test
    public void onReceivepropIntPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.PROP_PropInt.getValue());
        Bundle data = new Bundle();
        StructInt[] testpropInt = new StructInt[1];
        testpropInt[0] = Testbed1TestHelper.makeTestStructInt();
		data.putParcelableArray("propInt", StructIntParcelable.wrapArray(testpropInt));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropInt( any(StructInt[].class));
	    
    }

    @Test
     public void whenNotifiedpropInt()
    {
        StructInt[] testpropInt = new StructInt[1];
        testpropInt[0] = Testbed1TestHelper.makeTestStructInt();

        testedAdapterAsEventListener.onPropIntChanged(testpropInt);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SET_PropInt.getValue(), response.what);
        Bundle data = response.getData();

        
            StructInt[] receivedpropInt =  StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("propInt", StructIntParcelable.class));

        assertEquals(receivedpropInt, testpropInt);
    }
    @Test
    public void onReceivepropFloatPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.PROP_PropFloat.getValue());
        Bundle data = new Bundle();
        StructFloat[] testpropFloat = new StructFloat[1];
        testpropFloat[0] = Testbed1TestHelper.makeTestStructFloat();
		data.putParcelableArray("propFloat", StructFloatParcelable.wrapArray(testpropFloat));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropFloat( any(StructFloat[].class));
	    
    }

    @Test
     public void whenNotifiedpropFloat()
    {
        StructFloat[] testpropFloat = new StructFloat[1];
        testpropFloat[0] = Testbed1TestHelper.makeTestStructFloat();

        testedAdapterAsEventListener.onPropFloatChanged(testpropFloat);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SET_PropFloat.getValue(), response.what);
        Bundle data = response.getData();

        
            StructFloat[] receivedpropFloat =  StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("propFloat", StructFloatParcelable.class));

        assertEquals(receivedpropFloat, testpropFloat);
    }
    @Test
    public void onReceivepropStringPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.PROP_PropString.getValue());
        Bundle data = new Bundle();
        StructString[] testpropString = new StructString[1];
        testpropString[0] = Testbed1TestHelper.makeTestStructString();
		data.putParcelableArray("propString", StructStringParcelable.wrapArray(testpropString));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropString( any(StructString[].class));
	    
    }

    @Test
     public void whenNotifiedpropString()
    {
        StructString[] testpropString = new StructString[1];
        testpropString[0] = Testbed1TestHelper.makeTestStructString();

        testedAdapterAsEventListener.onPropStringChanged(testpropString);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SET_PropString.getValue(), response.what);
        Bundle data = response.getData();

        
            StructString[] receivedpropString =  StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("propString", StructStringParcelable.class));

        assertEquals(receivedpropString, testpropString);
    }
    @Test
    public void onReceivepropEnumPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.PROP_PropEnum.getValue());
        Bundle data = new Bundle();
        Enum0[] testpropEnum = new Enum0[1];
        testpropEnum[0] = Enum0.Value1;
		data.putParcelableArray("propEnum", Enum0Parcelable.wrapArray(testpropEnum));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropEnum(testpropEnum);
	    
    }

    @Test
     public void whenNotifiedpropEnum()
    {
        Enum0[] testpropEnum = new Enum0[1];
        testpropEnum[0] = Enum0.Value1;

        testedAdapterAsEventListener.onPropEnumChanged(testpropEnum);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SET_PropEnum.getValue(), response.what);
        Bundle data = response.getData();

        
            Enum0[] receivedpropEnum =  Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("propEnum", Enum0Parcelable.class));

        assertEquals(receivedpropEnum, testpropEnum);
    }
    @Test
    public void whenNotifiedsigBool()
    {
        StructBool[] testparamBool = new StructBool[1];
        testparamBool[0] = Testbed1TestHelper.makeTestStructBool();

        testedAdapterAsEventListener.onSigBool(testparamBool);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SIG_SigBool.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructBoolParcelable.class.getClassLoader());
        
            StructBool[] receivedparamBool =  StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("paramBool", StructBoolParcelable.class));
        assertEquals(receivedparamBool, testparamBool);
}
    @Test
    public void whenNotifiedsigInt()
    {
        StructInt[] testparamInt = new StructInt[1];
        testparamInt[0] = Testbed1TestHelper.makeTestStructInt();

        testedAdapterAsEventListener.onSigInt(testparamInt);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SIG_SigInt.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructIntParcelable.class.getClassLoader());
        
            StructInt[] receivedparamInt =  StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("paramInt", StructIntParcelable.class));
        assertEquals(receivedparamInt, testparamInt);
}
    @Test
    public void whenNotifiedsigFloat()
    {
        StructFloat[] testparamFloat = new StructFloat[1];
        testparamFloat[0] = Testbed1TestHelper.makeTestStructFloat();

        testedAdapterAsEventListener.onSigFloat(testparamFloat);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SIG_SigFloat.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructFloatParcelable.class.getClassLoader());
        
            StructFloat[] receivedparamFloat =  StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("paramFloat", StructFloatParcelable.class));
        assertEquals(receivedparamFloat, testparamFloat);
}
    @Test
    public void whenNotifiedsigString()
    {
        StructString[] testparamString = new StructString[1];
        testparamString[0] = Testbed1TestHelper.makeTestStructString();

        testedAdapterAsEventListener.onSigString(testparamString);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SIG_SigString.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructStringParcelable.class.getClassLoader());
        
            StructString[] receivedparamString =  StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("paramString", StructStringParcelable.class));
        assertEquals(receivedparamString, testparamString);
}
    @Test
    public void whenNotifiedsigEnum()
    {
        Enum0[] testparamEnum = new Enum0[1];
        testparamEnum[0] = Enum0.Value1;

        testedAdapterAsEventListener.onSigEnum(testparamEnum);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.SIG_SigEnum.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(Enum0Parcelable.class.getClassLoader());
        
            Enum0[] receivedparamEnum =  Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("paramEnum", Enum0Parcelable.class));
        assertEquals(receivedparamEnum, testparamEnum);
}


    public void onfuncBoolRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncBoolReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        StructBool[] testparamBool = new StructBool[1];
        testparamBool[0] = Testbed1TestHelper.makeTestStructBool();
		data.putParcelableArray("paramBool", StructBoolParcelable.wrapArray(testparamBool));
        StructBool[] returnedValue = new StructBool[1];
        returnedValue[0] = Testbed1TestHelper.makeTestStructBool();


        when(backendServiceMock.funcBool( any(StructBool[].class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcBool( any(StructBool[].class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.RPC_FuncBoolResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(StructBoolParcelable.class.getClassLoader());
        StructBool[] receivedByClient =  StructBoolParcelable.unwrapArray((StructBoolParcelable[])resp_data.getParcelableArray("result", StructBoolParcelable.class));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncIntRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncIntReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        StructInt[] testparamInt = new StructInt[1];
        testparamInt[0] = Testbed1TestHelper.makeTestStructInt();
		data.putParcelableArray("paramInt", StructIntParcelable.wrapArray(testparamInt));
        StructInt[] returnedValue = new StructInt[1];
        returnedValue[0] = Testbed1TestHelper.makeTestStructInt();


        when(backendServiceMock.funcInt( any(StructInt[].class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcInt( any(StructInt[].class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.RPC_FuncIntResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(StructIntParcelable.class.getClassLoader());
        StructInt[] receivedByClient =  StructIntParcelable.unwrapArray((StructIntParcelable[])resp_data.getParcelableArray("result", StructIntParcelable.class));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncFloatRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncFloatReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        StructFloat[] testparamFloat = new StructFloat[1];
        testparamFloat[0] = Testbed1TestHelper.makeTestStructFloat();
		data.putParcelableArray("paramFloat", StructFloatParcelable.wrapArray(testparamFloat));
        StructFloat[] returnedValue = new StructFloat[1];
        returnedValue[0] = Testbed1TestHelper.makeTestStructFloat();


        when(backendServiceMock.funcFloat( any(StructFloat[].class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcFloat( any(StructFloat[].class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.RPC_FuncFloatResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(StructFloatParcelable.class.getClassLoader());
        StructFloat[] receivedByClient =  StructFloatParcelable.unwrapArray((StructFloatParcelable[])resp_data.getParcelableArray("result", StructFloatParcelable.class));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncStringRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncStringReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        StructString[] testparamString = new StructString[1];
        testparamString[0] = Testbed1TestHelper.makeTestStructString();
		data.putParcelableArray("paramString", StructStringParcelable.wrapArray(testparamString));
        StructString[] returnedValue = new StructString[1];
        returnedValue[0] = Testbed1TestHelper.makeTestStructString();


        when(backendServiceMock.funcString( any(StructString[].class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcString( any(StructString[].class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructArrayInterfaceMessageType.RPC_FuncStringResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(StructStringParcelable.class.getClassLoader());
        StructString[] receivedByClient =  StructStringParcelable.unwrapArray((StructStringParcelable[])resp_data.getParcelableArray("result", StructStringParcelable.class));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncEnumRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructArrayInterfaceMessageType.RPC_FuncEnumReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        Enum0[] testparamEnum = new Enum0[1];
        testparamEnum[0] = Enum0.Value1;
		data.putParcelableArray("paramEnum", Enum0Parcelable.wrapArray(testparamEnum));
        Enum0[] returnedValue = new Enum0[1];
        returnedValue[0] = Enum0.Value1;


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
        Enum0[] receivedByClient =  Enum0Parcelable.unwrapArray((Enum0Parcelable[])resp_data.getParcelableArray("result", Enum0Parcelable.class));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
