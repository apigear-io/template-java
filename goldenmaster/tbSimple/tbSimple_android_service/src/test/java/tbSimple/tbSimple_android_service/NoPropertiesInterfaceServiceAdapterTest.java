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
import tbSimple.tbSimple_android_service.NoPropertiesInterfaceServiceAdapter;
import tbSimple.tbSimple_android_service.INoPropertiesInterfaceServiceProvider;

//import message type and parcelabe types
import tbSimple.tbSimple_api.TbSimpleTestHelper;
import tbSimple.tbSimple_api.IVoidInterface;
import tbSimple.tbSimple_android_messenger.VoidInterfaceParcelable;
import tbSimple.tbSimple_impl.VoidInterfaceService;
import tbSimple.tbSimple_api.ISimpleInterface;
import tbSimple.tbSimple_android_messenger.SimpleInterfaceParcelable;
import tbSimple.tbSimple_impl.SimpleInterfaceService;
import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_android_messenger.SimpleArrayInterfaceParcelable;
import tbSimple.tbSimple_impl.SimpleArrayInterfaceService;
import tbSimple.tbSimple_api.INoPropertiesInterface;
import tbSimple.tbSimple_android_messenger.NoPropertiesInterfaceParcelable;
import tbSimple.tbSimple_impl.NoPropertiesInterfaceService;
import tbSimple.tbSimple_api.INoOperationsInterface;
import tbSimple.tbSimple_android_messenger.NoOperationsInterfaceParcelable;
import tbSimple.tbSimple_impl.NoOperationsInterfaceService;
import tbSimple.tbSimple_api.INoSignalsInterface;
import tbSimple.tbSimple_android_messenger.NoSignalsInterfaceParcelable;
import tbSimple.tbSimple_impl.NoSignalsInterfaceService;
import tbSimple.tbSimple_api.IEmptyInterface;
import tbSimple.tbSimple_android_messenger.EmptyInterfaceParcelable;
import tbSimple.tbSimple_impl.EmptyInterfaceService;


import tbSimple.tbSimple_api.INoPropertiesInterfaceEventListener;
import tbSimple.tbSimple_android_service.INoPropertiesInterfaceServiceProvider;
import tbSimple.tbSimple_api.INoPropertiesInterface;
import tbSimple.tbSimple_api.AbstractNoPropertiesInterface;
import tbSimple.tbSimple_android_messenger.NoPropertiesInterfaceMessageType;


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

interface INoPropertiesInterfaceMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class NoPropertiesInterfaceServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private NoPropertiesInterfaceServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private INoPropertiesInterfaceEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private INoPropertiesInterface backendServiceMock = mock(INoPropertiesInterface.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private INoPropertiesInterfaceServiceProvider ServiceProvider = mock(INoPropertiesInterfaceServiceProvider.class);
    private INoPropertiesInterfaceMessageGetter clientMessagesStorage = mock(INoPropertiesInterfaceMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, NoPropertiesInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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

    Handler createClientHandlerMock(INoPropertiesInterfaceMessageGetter messageGetterMock)
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


        Message registerMsg = Message.obtain(null, NoPropertiesInterfaceMessageType.REGISTER_CLIENT.ordinal());
        registerMsg.getData().putString("connectionID", id);
        registerMsg.replyTo = messenger;
    
        try {
            mServiceMessenger.send(registerMsg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NoPropertiesInterfaceMessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        

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

        testedServiceAdapterIntent = new Intent(mMockContext, NoPropertiesInterfaceServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(NoPropertiesInterfaceServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(ServiceProvider.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(ServiceProvider);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<INoPropertiesInterfaceEventListener> eventListnerCaptor = ArgumentCaptor.forClass(INoPropertiesInterfaceEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
    @Test
    public void whenNotifiedsigVoid()
    {

        testedAdapterAsEventListener.onSigVoid();
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NoPropertiesInterfaceMessageType.SIG_SigVoid.getValue(), response.what);
        Bundle data = response.getData();
        
}
    @Test
    public void whenNotifiedsigBool()
    {
		boolean testparamBool = true;

        testedAdapterAsEventListener.onSigBool(testparamBool);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NoPropertiesInterfaceMessageType.SIG_SigBool.getValue(), response.what);
        Bundle data = response.getData();
        
        
			boolean receivedparamBool = data.getBoolean("paramBool", false);
        assertEquals(receivedparamBool, testparamBool);
}

    @Test
    public void onfuncVoidRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NoPropertiesInterfaceMessageType.RPC_FuncVoidReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcVoid();

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(NoPropertiesInterfaceMessageType.RPC_FuncVoidResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onfuncBoolRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, NoPropertiesInterfaceMessageType.RPC_FuncBoolReq.getValue());
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

        assertEquals(NoPropertiesInterfaceMessageType.RPC_FuncBoolResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		boolean receivedByClient = resp_data.getBoolean("result", false);

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
