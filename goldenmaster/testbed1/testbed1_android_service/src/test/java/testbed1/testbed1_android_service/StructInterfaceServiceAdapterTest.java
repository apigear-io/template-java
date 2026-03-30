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
import testbed1.testbed1_android_service.StructInterfaceServiceAdapter;
import testbed1.testbed1_android_service.IStructInterfaceServiceProvider;

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
import testbed1.testbed1_android_service.IStructInterfaceServiceProvider;
import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_api.AbstractStructInterface;
import testbed1.testbed1_android_messenger.StructInterfaceMessageType;


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

interface IStructInterfaceMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class StructInterfaceServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private StructInterfaceServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private IStructInterfaceEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private IStructInterface backendServiceMock = mock(IStructInterface.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private IStructInterfaceServiceProvider ServiceProvider = mock(IStructInterfaceServiceProvider.class);
    private IStructInterfaceMessageGetter clientMessagesStorage = mock(IStructInterfaceMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, StructInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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

    Handler createClientHandlerMock(IStructInterfaceMessageGetter messageGetterMock)
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
        StructBool initpropBool = new StructBool();
        //TODO fill fields
        when(backendServiceMock.getPropBool()).thenReturn(initpropBool);
        StructInt initpropInt = new StructInt();
        //TODO fill fields
        when(backendServiceMock.getPropInt()).thenReturn(initpropInt);
        StructFloat initpropFloat = new StructFloat();
        //TODO fill fields
        when(backendServiceMock.getPropFloat()).thenReturn(initpropFloat);
        StructString initpropString = new StructString();
        //TODO fill fields
        when(backendServiceMock.getPropString()).thenReturn(initpropString);


        Message registerMsg = Message.obtain(null, StructInterfaceMessageType.REGISTER_CLIENT.ordinal());
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

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        
			StructBool receivedpropBool = data.getParcelable("propBool", StructBoolParcelable.class).getStructBool();
        
			StructInt receivedpropInt = data.getParcelable("propInt", StructIntParcelable.class).getStructInt();
        
			StructFloat receivedpropFloat = data.getParcelable("propFloat", StructFloatParcelable.class).getStructFloat();
        
			StructString receivedpropString = data.getParcelable("propString", StructStringParcelable.class).getStructString();
        
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(StructBoolParcelable.class.getClassLoader());
        // assertEquals(receivedpropBool, initpropBool);
        // assertEquals(receivedpropInt, initpropInt);
        // assertEquals(receivedpropFloat, initpropFloat);
        // assertEquals(receivedpropString, initpropString);

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

        testedServiceAdapterIntent = new Intent(mMockContext, StructInterfaceServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(StructInterfaceServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(ServiceProvider.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(ServiceProvider);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<IStructInterfaceEventListener> eventListnerCaptor = ArgumentCaptor.forClass(IStructInterfaceEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
    @Test
    public void onReceivepropBoolPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructInterfaceMessageType.PROP_PropBool.getValue());
        Bundle data = new Bundle();
        StructBool testpropBool = Testbed1TestHelper.makeTestStructBool();
		data.putParcelable("propBool", new StructBoolParcelable(testpropBool));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropBool( any(StructBool.class));
	    
    }

    @Test
     public void whenNotifiedpropBool()
    {
        StructBool testpropBool = Testbed1TestHelper.makeTestStructBool();

        testedAdapterAsEventListener.onPropBoolChanged(testpropBool);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.SET_PropBool.getValue(), response.what);
        Bundle data = response.getData();

        
			StructBool receivedpropBool = data.getParcelable("propBool", StructBoolParcelable.class).getStructBool();

        assertEquals(receivedpropBool, testpropBool);
    }
    @Test
    public void onReceivepropIntPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructInterfaceMessageType.PROP_PropInt.getValue());
        Bundle data = new Bundle();
        StructInt testpropInt = Testbed1TestHelper.makeTestStructInt();
		data.putParcelable("propInt", new StructIntParcelable(testpropInt));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropInt( any(StructInt.class));
	    
    }

    @Test
     public void whenNotifiedpropInt()
    {
        StructInt testpropInt = Testbed1TestHelper.makeTestStructInt();

        testedAdapterAsEventListener.onPropIntChanged(testpropInt);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.SET_PropInt.getValue(), response.what);
        Bundle data = response.getData();

        
			StructInt receivedpropInt = data.getParcelable("propInt", StructIntParcelable.class).getStructInt();

        assertEquals(receivedpropInt, testpropInt);
    }
    @Test
    public void onReceivepropFloatPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructInterfaceMessageType.PROP_PropFloat.getValue());
        Bundle data = new Bundle();
        StructFloat testpropFloat = Testbed1TestHelper.makeTestStructFloat();
		data.putParcelable("propFloat", new StructFloatParcelable(testpropFloat));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropFloat( any(StructFloat.class));
	    
    }

    @Test
     public void whenNotifiedpropFloat()
    {
        StructFloat testpropFloat = Testbed1TestHelper.makeTestStructFloat();

        testedAdapterAsEventListener.onPropFloatChanged(testpropFloat);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.SET_PropFloat.getValue(), response.what);
        Bundle data = response.getData();

        
			StructFloat receivedpropFloat = data.getParcelable("propFloat", StructFloatParcelable.class).getStructFloat();

        assertEquals(receivedpropFloat, testpropFloat);
    }
    @Test
    public void onReceivepropStringPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructInterfaceMessageType.PROP_PropString.getValue());
        Bundle data = new Bundle();
        StructString testpropString = Testbed1TestHelper.makeTestStructString();
		data.putParcelable("propString", new StructStringParcelable(testpropString));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropString( any(StructString.class));
	    
    }

    @Test
     public void whenNotifiedpropString()
    {
        StructString testpropString = Testbed1TestHelper.makeTestStructString();

        testedAdapterAsEventListener.onPropStringChanged(testpropString);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.SET_PropString.getValue(), response.what);
        Bundle data = response.getData();

        
			StructString receivedpropString = data.getParcelable("propString", StructStringParcelable.class).getStructString();

        assertEquals(receivedpropString, testpropString);
    }
    @Test
    public void whenNotifiedsigBool()
    {
        StructBool testparamBool = Testbed1TestHelper.makeTestStructBool();

        testedAdapterAsEventListener.onSigBool(testparamBool);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.SIG_SigBool.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructBoolParcelable.class.getClassLoader());
        
			StructBool receivedparamBool = data.getParcelable("paramBool", StructBoolParcelable.class).getStructBool();
        assertEquals(receivedparamBool, testparamBool);
}
    @Test
    public void whenNotifiedsigInt()
    {
        StructInt testparamInt = Testbed1TestHelper.makeTestStructInt();

        testedAdapterAsEventListener.onSigInt(testparamInt);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.SIG_SigInt.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructIntParcelable.class.getClassLoader());
        
			StructInt receivedparamInt = data.getParcelable("paramInt", StructIntParcelable.class).getStructInt();
        assertEquals(receivedparamInt, testparamInt);
}
    @Test
    public void whenNotifiedsigFloat()
    {
        StructFloat testparamFloat = Testbed1TestHelper.makeTestStructFloat();

        testedAdapterAsEventListener.onSigFloat(testparamFloat);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.SIG_SigFloat.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructFloatParcelable.class.getClassLoader());
        
			StructFloat receivedparamFloat = data.getParcelable("paramFloat", StructFloatParcelable.class).getStructFloat();
        assertEquals(receivedparamFloat, testparamFloat);
}
    @Test
    public void whenNotifiedsigString()
    {
        StructString testparamString = Testbed1TestHelper.makeTestStructString();

        testedAdapterAsEventListener.onSigString(testparamString);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.SIG_SigString.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(StructStringParcelable.class.getClassLoader());
        
			StructString receivedparamString = data.getParcelable("paramString", StructStringParcelable.class).getStructString();
        assertEquals(receivedparamString, testparamString);
}

    @Test
    public void onfuncBoolRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructInterfaceMessageType.RPC_FuncBoolReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        StructBool testparamBool = Testbed1TestHelper.makeTestStructBool();
		data.putParcelable("paramBool", new StructBoolParcelable(testparamBool));
        StructBool returnedValue = Testbed1TestHelper.makeTestStructBool();


        when(backendServiceMock.funcBool( any(StructBool.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcBool( any(StructBool.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.RPC_FuncBoolResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(StructBoolParcelable.class.getClassLoader());
		StructBool receivedByClient = resp_data.getParcelable("result", StructBoolParcelable.class).getStructBool();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onfuncIntRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructInterfaceMessageType.RPC_FuncIntReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        StructInt testparamInt = Testbed1TestHelper.makeTestStructInt();
		data.putParcelable("paramInt", new StructIntParcelable(testparamInt));
        StructInt returnedValue = Testbed1TestHelper.makeTestStructInt();


        when(backendServiceMock.funcInt( any(StructInt.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcInt( any(StructInt.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.RPC_FuncIntResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(StructIntParcelable.class.getClassLoader());
		StructInt receivedByClient = resp_data.getParcelable("result", StructIntParcelable.class).getStructInt();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onfuncFloatRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructInterfaceMessageType.RPC_FuncFloatReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        StructFloat testparamFloat = Testbed1TestHelper.makeTestStructFloat();
		data.putParcelable("paramFloat", new StructFloatParcelable(testparamFloat));
        StructFloat returnedValue = Testbed1TestHelper.makeTestStructFloat();


        when(backendServiceMock.funcFloat( any(StructFloat.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcFloat( any(StructFloat.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.RPC_FuncFloatResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(StructFloatParcelable.class.getClassLoader());
		StructFloat receivedByClient = resp_data.getParcelable("result", StructFloatParcelable.class).getStructFloat();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onfuncStringRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, StructInterfaceMessageType.RPC_FuncStringReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        StructString testparamString = Testbed1TestHelper.makeTestStructString();
		data.putParcelable("paramString", new StructStringParcelable(testparamString));
        StructString returnedValue = Testbed1TestHelper.makeTestStructString();


        when(backendServiceMock.funcString( any(StructString.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcString( any(StructString.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(StructInterfaceMessageType.RPC_FuncStringResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(StructStringParcelable.class.getClassLoader());
		StructString receivedByClient = resp_data.getParcelable("result", StructStringParcelable.class).getStructString();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
