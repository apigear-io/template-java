//TODO later// Copyright Epic Games, Inc. All Rights Reserved.
package tbSame1.tbSame1_android_client;

import tbSame1.tbSame1_android_client.SameEnum2InterfaceClient;

//import message type and parcelabe types
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_android_messenger.Struct1Parcelable;
import tbSame1.tbSame1_api.Struct2;
import tbSame1.tbSame1_android_messenger.Struct2Parcelable;
import tbSame1.tbSame1_api.TbSame1TestHelper;
import tbSame1.tbSame1_api.Enum1;
import tbSame1.tbSame1_android_messenger.Enum1Parcelable;
import tbSame1.tbSame1_api.Enum2;
import tbSame1.tbSame1_android_messenger.Enum2Parcelable;

import tbSame1.tbSame1_api.ISameEnum2InterfaceEventListener;
import tbSame1.tbSame1_api.ISameEnum2Interface;
import tbSame1.tbSame1_api.AbstractSameEnum2Interface;
import tbSame1.tbSame1_android_messenger.SameEnum2InterfaceMessageType;

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


interface ISameEnum2InterfaceClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SameEnum2InterfaceClientTest
{

    @Mock
    private Context mMockContext;
   
    private SameEnum2InterfaceClient testedClient;
    private ISameEnum2InterfaceEventListener listenerMock = mock(ISameEnum2InterfaceEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private ISameEnum2InterfaceClientMessageGetter serviceMessagesStorage = mock(ISameEnum2InterfaceClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(SameEnum2InterfaceMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(ISameEnum2InterfaceClientMessageGetter messageGetterMock)
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


        testedClient = new SameEnum2InterfaceClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("tbSame1.tbSame1_android_service", "tbSame1.tbSame1_android_service.SameEnum2InterfaceServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(SameEnum2InterfaceMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, SameEnum2InterfaceMessageType.INIT.getValue());
        Bundle data = new Bundle();
		Enum1 testprop1 = Enum1.Value2;
		data.putParcelable("prop1", new Enum1Parcelable(testprop1));
		Enum2 testprop2 = Enum2.Value2;
		data.putParcelable("prop2", new Enum2Parcelable(testprop2));

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onProp1Changed(testprop1);
		inOrderEventListener.verify(listenerMock,times(1)).onProp2Changed(testprop2);
    }
    @Test
    public void onReceiveprop1PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SameEnum2InterfaceMessageType.SET_Prop1.getValue());
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

        assertEquals(SameEnum2InterfaceMessageType.PROP_Prop1.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(Enum1Parcelable.class.getClassLoader());
        
			Enum1 receivedprop1 = data.getParcelable("prop1", Enum1Parcelable.class).getEnum1();
        assertEquals(receivedprop1, testprop1);
    }
    
    @Test
    public void onReceiveprop2PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SameEnum2InterfaceMessageType.SET_Prop2.getValue());
        Bundle data = new Bundle();
		Enum2 testprop2 = Enum2.Value2;
		data.putParcelable("prop2", new Enum2Parcelable(testprop2));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onProp2Changed(testprop2);	    
    }
    
    @Test
     public void setPropertyRequestprop2()
    {
		Enum2 testprop2 = Enum2.Value2;

        testedClient.setProp2(testprop2);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SameEnum2InterfaceMessageType.PROP_Prop2.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(Enum2Parcelable.class.getClassLoader());
        
			Enum2 receivedprop2 = data.getParcelable("prop2", Enum2Parcelable.class).getEnum2();
        assertEquals(receivedprop2, testprop2);
    }
    
    @Test
    public void whenNotifiedsig1() throws RemoteException
    {

        Message msg = Message.obtain(null, SameEnum2InterfaceMessageType.SIG_Sig1.getValue());
        Bundle data = new Bundle();
		Enum1 testparam1 = Enum1.Value2;
		data.putParcelable("param1", new Enum1Parcelable(testparam1));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSig1(testparam1);

}
    @Test
    public void whenNotifiedsig2() throws RemoteException
    {

        Message msg = Message.obtain(null, SameEnum2InterfaceMessageType.SIG_Sig2.getValue());
        Bundle data = new Bundle();
		Enum1 testparam1 = Enum1.Value2;
		data.putParcelable("param1", new Enum1Parcelable(testparam1));
		Enum2 testparam2 = Enum2.Value2;
		data.putParcelable("param2", new Enum2Parcelable(testparam2));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSig2(testparam1, testparam2);

}


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
        assertEquals(SameEnum2InterfaceMessageType.RPC_Func1Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(Enum1Parcelable.class.getClassLoader());
        
			Enum1 receivedparam1 = data.getParcelable("param1", Enum1Parcelable.class).getEnum1();
        assertEquals(receivedparam1, testparam1);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SameEnum2InterfaceMessageType.RPC_Func1Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new Enum1Parcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfunc2Request() throws RemoteException {

        // Execute method
		Enum1 testparam1 = Enum1.Value2;
		Enum2 testparam2 = Enum2.Value2;
        Enum1 expectedResult = Enum1.Value2;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Enum1> resFuture = testedClient.func2Async(testparam1, testparam2);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SameEnum2InterfaceMessageType.RPC_Func2Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(Enum1Parcelable.class.getClassLoader());
        
			Enum1 receivedparam1 = data.getParcelable("param1", Enum1Parcelable.class).getEnum1();
        assertEquals(receivedparam1, testparam1);
        
			Enum2 receivedparam2 = data.getParcelable("param2", Enum2Parcelable.class).getEnum2();
        assertEquals(receivedparam2, testparam2);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SameEnum2InterfaceMessageType.RPC_Func2Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new Enum1Parcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
