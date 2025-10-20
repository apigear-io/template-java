//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

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
import tbSimple.tbSimple_android_service.SimpleInterfaceServiceAdapter;

//import message type and parcelabe types
import tbSimple.tbSimple_api.TbSimpleTestHelper;


import tbSimple.tbSimple_api.ISimpleInterfaceEventListener;
import tbSimple.tbSimple_android_service.ISimpleInterfaceServiceFactory;
import tbSimple.tbSimple_api.ISimpleInterface;
import tbSimple.tbSimple_api.AbstractSimpleInterface;
import tbSimple.tbSimple_android_messenger.SimpleInterfaceMessageType;


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

interface ISimpleInterfaceMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SimpleInterfaceServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private SimpleInterfaceServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private ISimpleInterfaceEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private ISimpleInterface backendServiceMock = mock(ISimpleInterface.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private ISimpleInterfaceServiceFactory serviceFactory = mock(ISimpleInterfaceServiceFactory.class);
    private ISimpleInterfaceMessageGetter clientMessagesStorage = mock(ISimpleInterfaceMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, SimpleInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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

    Handler createClientHandlerMock(ISimpleInterfaceMessageGetter messageGetterMock)
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
		boolean initpropBool = true;
        when(backendServiceMock.getPropBool()).thenReturn(initpropBool);
		int initpropInt = 1;
        when(backendServiceMock.getPropInt()).thenReturn(initpropInt);
		int initpropInt32 = 1;
        when(backendServiceMock.getPropInt32()).thenReturn(initpropInt32);
		long initpropInt64 = 1L;
        when(backendServiceMock.getPropInt64()).thenReturn(initpropInt64);
		float initpropFloat = 1.0f;
        when(backendServiceMock.getPropFloat()).thenReturn(initpropFloat);
		float initpropFloat32 = 1.0f;
        when(backendServiceMock.getPropFloat32()).thenReturn(initpropFloat32);
		double initpropFloat64 = 1.0;
        when(backendServiceMock.getPropFloat64()).thenReturn(initpropFloat64);
		String initpropString = new String("xyz");
        when(backendServiceMock.getPropString()).thenReturn(initpropString);


        Message registerMsg = Message.obtain(null, SimpleInterfaceMessageType.REGISTER_CLIENT.ordinal());
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
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropInt32();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropInt64();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropFloat();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropFloat32();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropFloat64();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropString();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        
			boolean receivedpropBool = data.getBoolean("propBool", false);
        
			int receivedpropInt = data.getInt("propInt", 0);
        
			int receivedpropInt32 = data.getInt("propInt32", 0);
        
			long receivedpropInt64 = data.getLong("propInt64", 0L);
        
			float receivedpropFloat = data.getFloat("propFloat", 0.0f);
        
			float receivedpropFloat32 = data.getFloat("propFloat32", 0.0f);
        
			double receivedpropFloat64 = data.getDouble("propFloat64", 0.0);
        
			String receivedpropString = data.getString("propString", new String());
        
        assertEquals(receivedpropBool, initpropBool);
        assertEquals(receivedpropInt, initpropInt);
        assertEquals(receivedpropInt32, initpropInt32);
        assertEquals(receivedpropInt64, initpropInt64);
        assertEquals(receivedpropFloat, initpropFloat,
        1e-6f);
        assertEquals(receivedpropFloat32, initpropFloat32,
        1e-6f);
        assertEquals(receivedpropFloat64, initpropFloat64,
        1e-6f);
        assertEquals(receivedpropString, initpropString);

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

        testedServiceAdapterIntent = new Intent(mMockContext, SimpleInterfaceServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(SimpleInterfaceServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(serviceFactory.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(serviceFactory);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<ISimpleInterfaceEventListener> eventListnerCaptor = ArgumentCaptor.forClass(ISimpleInterfaceEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
    @Test
    public void onReceivepropBoolPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.PROP_PropBool.getValue());
        Bundle data = new Bundle();
		boolean testpropBool = true;
		data.putBoolean("propBool", testpropBool);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropBool(testpropBool);
	    
    }

    @Test
     public void whenNotifiedpropBool()
    {
		boolean testpropBool = true;

        testedAdapterAsEventListener.onPropBoolChanged(testpropBool);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SET_PropBool.getValue(), response.what);
        Bundle data = response.getData();

        
			boolean receivedpropBool = data.getBoolean("propBool", false);

        assertEquals(receivedpropBool, testpropBool);
    }
    @Test
    public void onReceivepropIntPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.PROP_PropInt.getValue());
        Bundle data = new Bundle();
		int testpropInt = 1;
		data.putInt("propInt", testpropInt);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropInt(testpropInt);
	    
    }

    @Test
     public void whenNotifiedpropInt()
    {
		int testpropInt = 1;

        testedAdapterAsEventListener.onPropIntChanged(testpropInt);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SET_PropInt.getValue(), response.what);
        Bundle data = response.getData();

        
			int receivedpropInt = data.getInt("propInt", 0);

        assertEquals(receivedpropInt, testpropInt);
    }
    @Test
    public void onReceivepropInt32PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.PROP_PropInt32.getValue());
        Bundle data = new Bundle();
		int testpropInt32 = 1;
		data.putInt("propInt32", testpropInt32);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropInt32(testpropInt32);
	    
    }

    @Test
     public void whenNotifiedpropInt32()
    {
		int testpropInt32 = 1;

        testedAdapterAsEventListener.onPropInt32Changed(testpropInt32);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SET_PropInt32.getValue(), response.what);
        Bundle data = response.getData();

        
			int receivedpropInt32 = data.getInt("propInt32", 0);

        assertEquals(receivedpropInt32, testpropInt32);
    }
    @Test
    public void onReceivepropInt64PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.PROP_PropInt64.getValue());
        Bundle data = new Bundle();
		long testpropInt64 = 1L;
		data.putLong("propInt64", testpropInt64);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropInt64(testpropInt64);
	    
    }

    @Test
     public void whenNotifiedpropInt64()
    {
		long testpropInt64 = 1L;

        testedAdapterAsEventListener.onPropInt64Changed(testpropInt64);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SET_PropInt64.getValue(), response.what);
        Bundle data = response.getData();

        
			long receivedpropInt64 = data.getLong("propInt64", 0L);

        assertEquals(receivedpropInt64, testpropInt64);
    }
    @Test
    public void onReceivepropFloatPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.PROP_PropFloat.getValue());
        Bundle data = new Bundle();
		float testpropFloat = 1.0f;
		data.putFloat("propFloat", testpropFloat);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropFloat(testpropFloat);
	    
    }

    @Test
     public void whenNotifiedpropFloat()
    {
		float testpropFloat = 1.0f;

        testedAdapterAsEventListener.onPropFloatChanged(testpropFloat);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SET_PropFloat.getValue(), response.what);
        Bundle data = response.getData();

        
			float receivedpropFloat = data.getFloat("propFloat", 0.0f);

        assertEquals(receivedpropFloat, testpropFloat,
        1e-6f);
    }
    @Test
    public void onReceivepropFloat32PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.PROP_PropFloat32.getValue());
        Bundle data = new Bundle();
		float testpropFloat32 = 1.0f;
		data.putFloat("propFloat32", testpropFloat32);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropFloat32(testpropFloat32);
	    
    }

    @Test
     public void whenNotifiedpropFloat32()
    {
		float testpropFloat32 = 1.0f;

        testedAdapterAsEventListener.onPropFloat32Changed(testpropFloat32);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SET_PropFloat32.getValue(), response.what);
        Bundle data = response.getData();

        
			float receivedpropFloat32 = data.getFloat("propFloat32", 0.0f);

        assertEquals(receivedpropFloat32, testpropFloat32,
        1e-6f);
    }
    @Test
    public void onReceivepropFloat64PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.PROP_PropFloat64.getValue());
        Bundle data = new Bundle();
		double testpropFloat64 = 1.0;
		data.putDouble("propFloat64", testpropFloat64);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropFloat64(testpropFloat64);
	    
    }

    @Test
     public void whenNotifiedpropFloat64()
    {
		double testpropFloat64 = 1.0;

        testedAdapterAsEventListener.onPropFloat64Changed(testpropFloat64);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SET_PropFloat64.getValue(), response.what);
        Bundle data = response.getData();

        
			double receivedpropFloat64 = data.getDouble("propFloat64", 0.0);

        assertEquals(receivedpropFloat64, testpropFloat64,
        1e-6f);
    }
    @Test
    public void onReceivepropStringPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.PROP_PropString.getValue());
        Bundle data = new Bundle();
		String testpropString = new String("xyz");
		data.putString("propString", testpropString);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropString(testpropString);
	    
    }

    @Test
     public void whenNotifiedpropString()
    {
		String testpropString = new String("xyz");

        testedAdapterAsEventListener.onPropStringChanged(testpropString);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SET_PropString.getValue(), response.what);
        Bundle data = response.getData();

        
			String receivedpropString = data.getString("propString", new String());

        assertEquals(receivedpropString, testpropString);
    }
    @Test
    public void whenNotifiedsigBool()
    {
		boolean testparamBool = true;

        testedAdapterAsEventListener.onSigBool(testparamBool);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SIG_SigBool.getValue(), response.what);
        Bundle data = response.getData();
        
        
			boolean receivedparamBool = data.getBoolean("paramBool", false);
        assertEquals(receivedparamBool, testparamBool);
}
    @Test
    public void whenNotifiedsigInt()
    {
		int testparamInt = 1;

        testedAdapterAsEventListener.onSigInt(testparamInt);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SIG_SigInt.getValue(), response.what);
        Bundle data = response.getData();
        
        
			int receivedparamInt = data.getInt("paramInt", 0);
        assertEquals(receivedparamInt, testparamInt);
}
    @Test
    public void whenNotifiedsigInt32()
    {
		int testparamInt32 = 1;

        testedAdapterAsEventListener.onSigInt32(testparamInt32);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SIG_SigInt32.getValue(), response.what);
        Bundle data = response.getData();
        
        
			int receivedparamInt32 = data.getInt("paramInt32", 0);
        assertEquals(receivedparamInt32, testparamInt32);
}
    @Test
    public void whenNotifiedsigInt64()
    {
		long testparamInt64 = 1L;

        testedAdapterAsEventListener.onSigInt64(testparamInt64);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SIG_SigInt64.getValue(), response.what);
        Bundle data = response.getData();
        
        
			long receivedparamInt64 = data.getLong("paramInt64", 0L);
        assertEquals(receivedparamInt64, testparamInt64);
}
    @Test
    public void whenNotifiedsigFloat()
    {
		float testparamFloat = 1.0f;

        testedAdapterAsEventListener.onSigFloat(testparamFloat);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SIG_SigFloat.getValue(), response.what);
        Bundle data = response.getData();
        
        
			float receivedparamFloat = data.getFloat("paramFloat", 0.0f);
        assertEquals(receivedparamFloat, testparamFloat,
        1e-6f);
}
    @Test
    public void whenNotifiedsigFloat32()
    {
		float testparamFloat32 = 1.0f;

        testedAdapterAsEventListener.onSigFloat32(testparamFloat32);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SIG_SigFloat32.getValue(), response.what);
        Bundle data = response.getData();
        
        
			float receivedparamFloat32 = data.getFloat("paramFloat32", 0.0f);
        assertEquals(receivedparamFloat32, testparamFloat32,
        1e-6f);
}
    @Test
    public void whenNotifiedsigFloat64()
    {
		double testparamFloat64 = 1.0;

        testedAdapterAsEventListener.onSigFloat64(testparamFloat64);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SIG_SigFloat64.getValue(), response.what);
        Bundle data = response.getData();
        
        
			double receivedparamFloat64 = data.getDouble("paramFloat64", 0.0);
        assertEquals(receivedparamFloat64, testparamFloat64,
        1e-6f);
}
    @Test
    public void whenNotifiedsigString()
    {
		String testparamString = new String("xyz");

        testedAdapterAsEventListener.onSigString(testparamString);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.SIG_SigString.getValue(), response.what);
        Bundle data = response.getData();
        
        
			String receivedparamString = data.getString("paramString", new String());
        assertEquals(receivedparamString, testparamString);
}


    public void onfuncNoReturnValueRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncNoReturnValueReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		boolean testparamBool = true;
		data.putBoolean("paramBool", testparamBool);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcNoReturnValue(testparamBool);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.RPC_FuncNoReturnValueResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncBoolRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncBoolReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		boolean testparamBool = true;
		data.putBoolean("paramBool", testparamBool);
        boolean returnedValue = true;


        when(backendServiceMock.funcBool(testparamBool)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcBool(testparamBool);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.RPC_FuncBoolResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		boolean receivedByClient = resp_data.getBoolean("result", false);

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncIntRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncIntReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		int testparamInt = 1;
		data.putInt("paramInt", testparamInt);
        int returnedValue = 1;


        when(backendServiceMock.funcInt(testparamInt)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcInt(testparamInt);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.RPC_FuncIntResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		int receivedByClient = resp_data.getInt("result", 0);

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncInt32Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncInt32Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		int testparamInt32 = 1;
		data.putInt("paramInt32", testparamInt32);
        int returnedValue = 1;


        when(backendServiceMock.funcInt32(testparamInt32)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcInt32(testparamInt32);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.RPC_FuncInt32Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		int receivedByClient = resp_data.getInt("result", 0);

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncInt64Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncInt64Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		long testparamInt64 = 1L;
		data.putLong("paramInt64", testparamInt64);
        long returnedValue = 1L;


        when(backendServiceMock.funcInt64(testparamInt64)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcInt64(testparamInt64);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.RPC_FuncInt64Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		long receivedByClient = resp_data.getLong("result", 0L);

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncFloatRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncFloatReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		float testparamFloat = 1.0f;
		data.putFloat("paramFloat", testparamFloat);
        float returnedValue = 1.0f;


        when(backendServiceMock.funcFloat(testparamFloat)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcFloat(testparamFloat);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.RPC_FuncFloatResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		float receivedByClient = resp_data.getFloat("result", 0.0f);

        assertEquals(receivedByClient, returnedValue,
        1e-6f);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncFloat32Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncFloat32Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		float testparamFloat32 = 1.0f;
		data.putFloat("paramFloat32", testparamFloat32);
        float returnedValue = 1.0f;


        when(backendServiceMock.funcFloat32(testparamFloat32)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcFloat32(testparamFloat32);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.RPC_FuncFloat32Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		float receivedByClient = resp_data.getFloat("result", 0.0f);

        assertEquals(receivedByClient, returnedValue,
        1e-6f);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncFloat64Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncFloat64Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		double testparamFloat = 1.0;
		data.putDouble("paramFloat", testparamFloat);
        double returnedValue = 1.0;


        when(backendServiceMock.funcFloat64(testparamFloat)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcFloat64(testparamFloat);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.RPC_FuncFloat64Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		double receivedByClient = resp_data.getDouble("result", 0.0);

        assertEquals(receivedByClient, returnedValue,
        1e-6f);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncStringRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleInterfaceMessageType.RPC_FuncStringReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		String testparamString = new String("xyz");
		data.putString("paramString", testparamString);
        String returnedValue = new String("xyz");


        when(backendServiceMock.funcString(testparamString)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcString(testparamString);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleInterfaceMessageType.RPC_FuncStringResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		String receivedByClient = resp_data.getString("result", new String());

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
