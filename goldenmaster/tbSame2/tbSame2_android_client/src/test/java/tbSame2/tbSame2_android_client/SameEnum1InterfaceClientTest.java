// Copyright Epic Games, Inc. All Rights Reserved.
package tbSame2.tbSame2_android_client;

import tbSame2.tbSame2_android_client.SameEnum1InterfaceClient;

//import message type and parcelabe types
import tbSame2.tbSame2_api.Struct1;
import tbSame2.tbSame2_android_messenger.Struct1Parcelable;
import tbSame2.tbSame2_api.Struct2;
import tbSame2.tbSame2_android_messenger.Struct2Parcelable;
import tbSame2.tbSame2_api.TbSame2TestHelper;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_android_messenger.Enum1Parcelable;
import tbSame2.tbSame2_api.Enum2;
import tbSame2.tbSame2_android_messenger.Enum2Parcelable;
import tbSame2.tbSame2_api.ISameStruct1Interface;
import tbSame2.tbSame2_android_messenger.SameStruct1InterfaceParcelable;
import tbSame2.tbSame2_impl.SameStruct1InterfaceService;
import tbSame2.tbSame2_api.ISameStruct2Interface;
import tbSame2.tbSame2_android_messenger.SameStruct2InterfaceParcelable;
import tbSame2.tbSame2_impl.SameStruct2InterfaceService;
import tbSame2.tbSame2_api.ISameEnum1Interface;
import tbSame2.tbSame2_android_messenger.SameEnum1InterfaceParcelable;
import tbSame2.tbSame2_impl.SameEnum1InterfaceService;
import tbSame2.tbSame2_api.ISameEnum2Interface;
import tbSame2.tbSame2_android_messenger.SameEnum2InterfaceParcelable;
import tbSame2.tbSame2_impl.SameEnum2InterfaceService;

import tbSame2.tbSame2_api.ISameEnum1InterfaceEventListener;
import tbSame2.tbSame2_api.ISameEnum1Interface;
import tbSame2.tbSame2_api.AbstractSameEnum1Interface;
import tbSame2.tbSame2_android_messenger.SameEnum1InterfaceMessageType;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
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


interface ISameEnum1InterfaceClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SameEnum1InterfaceClientTest
{

    @Mock
    private Context mMockContext;
   
    private SameEnum1InterfaceClient testedClient;
    private ISameEnum1InterfaceEventListener listenerMock = mock(ISameEnum1InterfaceEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private ISameEnum1InterfaceClientMessageGetter serviceMessagesStorage = mock(ISameEnum1InterfaceClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(SameEnum1InterfaceMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(ISameEnum1InterfaceClientMessageGetter messageGetterMock)
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


        testedClient = new SameEnum1InterfaceClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("tbSame2.tbSame2_android_service", "tbSame2.tbSame2_android_service.SameEnum1InterfaceServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(SameEnum1InterfaceMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, SameEnum1InterfaceMessageType.INIT.getValue());
        Bundle data = new Bundle();
		Enum1 testprop1 = Enum1.Value2;
		data.putParcelable("prop1", new Enum1Parcelable(testprop1));

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onProp1Changed(testprop1);
    }
    @Test
    public void onReceiveprop1PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SameEnum1InterfaceMessageType.SET_Prop1.getValue());
        Bundle data = new Bundle();
		Enum1 testprop1 = Enum1.Value2;
		data.putParcelable("prop1", new Enum1Parcelable(testprop1));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onProp1Changed(testprop1);	    
    }
    
    @Test
     public void setPropertyRequestprop1()
    {
		Enum1 testprop1 = Enum1.Value2;

        testedClient.setProp1(testprop1);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SameEnum1InterfaceMessageType.PROP_Prop1.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(Enum1Parcelable.class.getClassLoader());
        
			Enum1 receivedprop1 = data.getParcelable("prop1", Enum1Parcelable.class).getEnum1();
        assertEquals(receivedprop1, testprop1);
    }
    
    @Test
    public void whenNotifiedsig1() throws RemoteException
    {

        Message msg = Message.obtain(null, SameEnum1InterfaceMessageType.SIG_Sig1.getValue());
        Bundle data = new Bundle();
		Enum1 testparam1 = Enum1.Value2;
		data.putParcelable("param1", new Enum1Parcelable(testparam1));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSig1(testparam1);

}

    @Test
    public void onfunc1Request() throws RemoteException {

        // Execute method
		Enum1 testparam1 = Enum1.Value2;
        Enum1 expectedResult = Enum1.Value2;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Enum1> resFuture = testedClient.func1Async(testparam1);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SameEnum1InterfaceMessageType.RPC_Func1Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(Enum1Parcelable.class.getClassLoader());
        
			Enum1 receivedparam1 = data.getParcelable("param1", Enum1Parcelable.class).getEnum1();
        assertEquals(receivedparam1, testparam1);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SameEnum1InterfaceMessageType.RPC_Func1Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new Enum1Parcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
