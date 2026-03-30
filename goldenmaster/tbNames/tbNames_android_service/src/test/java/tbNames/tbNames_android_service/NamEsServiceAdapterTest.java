//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbNames.tbNames_android_service;

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
import tbNames.tbNames_android_service.NamEsServiceAdapter;
import tbNames.tbNames_android_service.INamEsServiceProvider;

//import message type and parcelabe types
import tbNames.tbNames_api.TbNamesTestHelper;
import tbNames.tbNames_api.EnumWithUnderScores;
import tbNames.tbNames_android_messenger.EnumWithUnderScoresParcelable;
import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_android_messenger.NamEsParcelable;
import tbNames.tbNames_impl.NamEsService;


import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_android_service.INamEsServiceProvider;
import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_api.AbstractNamEs;
import tbNames.tbNames_android_messenger.NamEsMessageType;


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
import tbNames.tbNames_android_messenger.Conversions;

interface INamEsMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class NamEsServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private NamEsServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private INamEsEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private INamEs backendServiceMock = mock(INamEs.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private INamEsServiceProvider ServiceProvider = mock(INamEsServiceProvider.class);
    private INamEsMessageGetter clientMessagesStorage = mock(INamEsMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, NamEsMessageType.UNREGISTER_CLIENT.ordinal());
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

    Handler createClientHandlerMock(INamEsMessageGetter messageGetterMock)
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
		boolean initSwitch = true;
        when(backendServiceMock.getSwitch()).thenReturn(initSwitch);
		int initSOME_PROPERTY = 1;
        when(backendServiceMock.getSomeProperty()).thenReturn(initSOME_PROPERTY);
		int initSome_Poperty2 = 1;
        when(backendServiceMock.getSomePoperty2()).thenReturn(initSome_Poperty2);
        EnumWithUnderScores initenum_property = EnumWithUnderScores.SecondValue;
        when(backendServiceMock.getEnumProperty()).thenReturn(initenum_property);


        Message registerMsg = Message.obtain(null, NamEsMessageType.REGISTER_CLIENT.ordinal());
        registerMsg.getData().putString("connectionID", id);
        registerMsg.replyTo = messenger;
    
        try {
            mServiceMessenger.send(registerMsg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock, times(1)).getSwitch();
        inOrderBackendService.verify(backendServiceMock, times(1)).getSomeProperty();
        inOrderBackendService.verify(backendServiceMock, times(1)).getSomePoperty2();
        inOrderBackendService.verify(backendServiceMock, times(1)).getEnumProperty();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NamEsMessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        
			boolean receivedSwitch = data.getBoolean("Switch", false);
        
			int receivedSOME_PROPERTY = data.getInt("SOME_PROPERTY", 0);
        
			int receivedSome_Poperty2 = data.getInt("Some_Poperty2", 0);
        
			EnumWithUnderScores receivedenum_property = data.getParcelable("enum_property", EnumWithUnderScoresParcelable.class).getEnumWithUnderScores();
        
        data.setClassLoader(EnumWithUnderScoresParcelable.class.getClassLoader());
        assertEquals(receivedSwitch, initSwitch);
        assertEquals(receivedSOME_PROPERTY, initSOME_PROPERTY);
        assertEquals(receivedSome_Poperty2, initSome_Poperty2);
        assertEquals(receivedenum_property, initenum_property);

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

        testedServiceAdapterIntent = new Intent(mMockContext, NamEsServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(NamEsServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(ServiceProvider.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(ServiceProvider);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<INamEsEventListener> eventListnerCaptor = ArgumentCaptor.forClass(INamEsEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
    @Test
    public void onReceiveSwitchPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NamEsMessageType.PROP_Switch.getValue());
        Bundle data = new Bundle();
		boolean testSwitch = true;
		data.putBoolean("Switch", testSwitch);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setSwitch(testSwitch);
	    
    }

    @Test
     public void whenNotifiedSwitch()
    {
		boolean testSwitch = true;

        testedAdapterAsEventListener.onSwitchChanged(testSwitch);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NamEsMessageType.SET_Switch.getValue(), response.what);
        Bundle data = response.getData();

        
			boolean receivedSwitch = data.getBoolean("Switch", false);

        assertEquals(receivedSwitch, testSwitch);
    }
    @Test
    public void onReceiveSOME_PROPERTYPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NamEsMessageType.PROP_SomeProperty.getValue());
        Bundle data = new Bundle();
		int testSOME_PROPERTY = 1;
		data.putInt("SOME_PROPERTY", testSOME_PROPERTY);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setSomeProperty(testSOME_PROPERTY);
	    
    }

    @Test
     public void whenNotifiedSOME_PROPERTY()
    {
		int testSOME_PROPERTY = 1;

        testedAdapterAsEventListener.onSomePropertyChanged(testSOME_PROPERTY);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NamEsMessageType.SET_SomeProperty.getValue(), response.what);
        Bundle data = response.getData();

        
			int receivedSOME_PROPERTY = data.getInt("SOME_PROPERTY", 0);

        assertEquals(receivedSOME_PROPERTY, testSOME_PROPERTY);
    }
    @Test
    public void onReceiveSome_Poperty2PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NamEsMessageType.PROP_SomePoperty2.getValue());
        Bundle data = new Bundle();
		int testSome_Poperty2 = 1;
		data.putInt("Some_Poperty2", testSome_Poperty2);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setSomePoperty2(testSome_Poperty2);
	    
    }

    @Test
     public void whenNotifiedSome_Poperty2()
    {
		int testSome_Poperty2 = 1;

        testedAdapterAsEventListener.onSomePoperty2Changed(testSome_Poperty2);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NamEsMessageType.SET_SomePoperty2.getValue(), response.what);
        Bundle data = response.getData();

        
			int receivedSome_Poperty2 = data.getInt("Some_Poperty2", 0);

        assertEquals(receivedSome_Poperty2, testSome_Poperty2);
    }
    @Test
    public void onReceiveenum_propertyPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NamEsMessageType.PROP_EnumProperty.getValue());
        Bundle data = new Bundle();
		EnumWithUnderScores testenum_property = EnumWithUnderScores.SecondValue;
		data.putParcelable("enum_property", new EnumWithUnderScoresParcelable(testenum_property));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setEnumProperty(testenum_property);
	    
    }

    @Test
     public void whenNotifiedenum_property()
    {
		EnumWithUnderScores testenum_property = EnumWithUnderScores.SecondValue;

        testedAdapterAsEventListener.onEnumPropertyChanged(testenum_property);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NamEsMessageType.SET_EnumProperty.getValue(), response.what);
        Bundle data = response.getData();

        
			EnumWithUnderScores receivedenum_property = data.getParcelable("enum_property", EnumWithUnderScoresParcelable.class).getEnumWithUnderScores();

        assertEquals(receivedenum_property, testenum_property);
    }
    @Test
    public void whenNotifiedSOME_SIGNAL()
    {
		boolean testSOME_PARAM = true;

        testedAdapterAsEventListener.onSomeSignal(testSOME_PARAM);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NamEsMessageType.SIG_SomeSignal.getValue(), response.what);
        Bundle data = response.getData();
        
        
			boolean receivedSOME_PARAM = data.getBoolean("SOME_PARAM", false);
        assertEquals(receivedSOME_PARAM, testSOME_PARAM);
}
    @Test
    public void whenNotifiedSome_Signal2()
    {
		boolean testSome_Param = true;

        testedAdapterAsEventListener.onSomeSignal2(testSome_Param);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NamEsMessageType.SIG_SomeSignal2.getValue(), response.what);
        Bundle data = response.getData();
        
        
			boolean receivedSome_Param = data.getBoolean("Some_Param", false);
        assertEquals(receivedSome_Param, testSome_Param);
}

    @Test
    public void onSOME_FUNCTIONRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NamEsMessageType.RPC_SomeFunctionReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		boolean testSOME_PARAM = true;
		data.putBoolean("SOME_PARAM", testSOME_PARAM);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).someFunction(testSOME_PARAM);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NamEsMessageType.RPC_SomeFunctionResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onSome_Function2Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NamEsMessageType.RPC_SomeFunction2Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		boolean testSome_Param = true;
		data.putBoolean("Some_Param", testSome_Param);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).someFunction2(testSome_Param);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NamEsMessageType.RPC_SomeFunction2Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
