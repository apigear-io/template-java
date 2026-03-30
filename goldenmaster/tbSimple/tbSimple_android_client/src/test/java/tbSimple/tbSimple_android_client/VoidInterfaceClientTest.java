//TODO later// Copyright Epic Games, Inc. All Rights Reserved.
package tbSimple.tbSimple_android_client;

import tbSimple.tbSimple_android_client.VoidInterfaceClient;

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

import tbSimple.tbSimple_api.IVoidInterfaceEventListener;
import tbSimple.tbSimple_api.IVoidInterface;
import tbSimple.tbSimple_api.AbstractVoidInterface;
import tbSimple.tbSimple_android_messenger.VoidInterfaceMessageType;

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
import tbSimple.tbSimple_android_messenger.Conversions;
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


interface IVoidInterfaceClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class VoidInterfaceClientTest
{

    @Mock
    private Context mMockContext;
   
    private VoidInterfaceClient testedClient;
    private IVoidInterfaceEventListener listenerMock = mock(IVoidInterfaceEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private IVoidInterfaceClientMessageGetter serviceMessagesStorage = mock(IVoidInterfaceClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(VoidInterfaceMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(IVoidInterfaceClientMessageGetter messageGetterMock)
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


        testedClient = new VoidInterfaceClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("tbSimple.tbSimple_android_service", "tbSimple.tbSimple_android_service.VoidInterfaceServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(VoidInterfaceMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, VoidInterfaceMessageType.INIT.getValue());
        Bundle data = new Bundle();

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
    }
    @Test
    public void whenNotifiedsigVoid() throws RemoteException
    {

        Message msg = Message.obtain(null, VoidInterfaceMessageType.SIG_SigVoid.getValue());
        Bundle data = new Bundle();

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigVoid();

}

    @Test
    public void onfuncVoidRequest() throws RemoteException {

        // Execute method

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Void> resFuture = testedClient.funcVoidAsync();

        resFuture.thenAccept(result -> {
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(VoidInterfaceMessageType.RPC_FuncVoidReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, VoidInterfaceMessageType.RPC_FuncVoidResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
