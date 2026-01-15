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
import tbSimple.tbSimple_android_service.EmptyInterfaceServiceAdapter;

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


import tbSimple.tbSimple_api.IEmptyInterfaceEventListener;
import tbSimple.tbSimple_android_service.IEmptyInterfaceServiceProvider;
import tbSimple.tbSimple_api.IEmptyInterface;
import tbSimple.tbSimple_api.AbstractEmptyInterface;
import tbSimple.tbSimple_android_messenger.EmptyInterfaceMessageType;


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

interface IEmptyInterfaceMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class EmptyInterfaceServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private EmptyInterfaceServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private IEmptyInterfaceEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private IEmptyInterface backendServiceMock = mock(IEmptyInterface.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private IEmptyInterfaceServiceProvider ServiceProvider = mock(IEmptyInterfaceServiceProvider.class);
    private IEmptyInterfaceMessageGetter clientMessagesStorage = mock(IEmptyInterfaceMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, EmptyInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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

    Handler createClientHandlerMock(IEmptyInterfaceMessageGetter messageGetterMock)
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


        Message registerMsg = Message.obtain(null, EmptyInterfaceMessageType.REGISTER_CLIENT.ordinal());
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

        assertEquals(EmptyInterfaceMessageType.INIT.getValue(), response.what);
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

        testedServiceAdapterIntent = new Intent(mMockContext, EmptyInterfaceServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(EmptyInterfaceServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(ServiceProvider.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(ServiceProvider);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<IEmptyInterfaceEventListener> eventListnerCaptor = ArgumentCaptor.forClass(IEmptyInterfaceEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
    @Test
    public void onlySetupAndTeardown()
    {

    }

}
