//TODO later// Copyright Epic Games, Inc. All Rights Reserved.
package tbEnum.tbEnum_android_client;

import tbEnum.tbEnum_android_client.EnumInterfaceClient;

//import message type and parcelabe types
import tbEnum.tbEnum_api.TbEnumTestHelper;
import tbEnum.tbEnum_api.Enum0;
import tbEnum.tbEnum_android_messenger.Enum0Parcelable;
import tbEnum.tbEnum_api.Enum1;
import tbEnum.tbEnum_android_messenger.Enum1Parcelable;
import tbEnum.tbEnum_api.Enum2;
import tbEnum.tbEnum_android_messenger.Enum2Parcelable;
import tbEnum.tbEnum_api.Enum3;
import tbEnum.tbEnum_android_messenger.Enum3Parcelable;
import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_android_messenger.EnumInterfaceParcelable;
import tbEnum.tbEnum_impl.EnumInterfaceService;

import tbEnum.tbEnum_api.IEnumInterfaceEventListener;
import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_api.AbstractEnumInterface;
import tbEnum.tbEnum_android_messenger.EnumInterfaceMessageType;

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


interface IEnumInterfaceClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class EnumInterfaceClientTest
{

    @Mock
    private Context mMockContext;
   
    private EnumInterfaceClient testedClient;
    private IEnumInterfaceEventListener listenerMock = mock(IEnumInterfaceEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private IEnumInterfaceClientMessageGetter serviceMessagesStorage = mock(IEnumInterfaceClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(EnumInterfaceMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(IEnumInterfaceClientMessageGetter messageGetterMock)
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


        testedClient = new EnumInterfaceClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("tbEnum.tbEnum_android_service", "tbEnum.tbEnum_android_service.EnumInterfaceServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(EnumInterfaceMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, EnumInterfaceMessageType.INIT.getValue());
        Bundle data = new Bundle();
		Enum0 testprop0 = Enum0.Value1;
		data.putParcelable("prop0", new Enum0Parcelable(testprop0));
		Enum1 testprop1 = Enum1.Value2;
		data.putParcelable("prop1", new Enum1Parcelable(testprop1));
		Enum2 testprop2 = Enum2.Value1;
		data.putParcelable("prop2", new Enum2Parcelable(testprop2));
		Enum3 testprop3 = Enum3.Value2;
		data.putParcelable("prop3", new Enum3Parcelable(testprop3));

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onProp0Changed(testprop0);
		inOrderEventListener.verify(listenerMock,times(1)).onProp1Changed(testprop1);
		inOrderEventListener.verify(listenerMock,times(1)).onProp2Changed(testprop2);
		inOrderEventListener.verify(listenerMock,times(1)).onProp3Changed(testprop3);
    }
    @Test
    public void onReceiveprop0PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, EnumInterfaceMessageType.SET_Prop0.getValue());
        Bundle data = new Bundle();
		Enum0 testprop0 = Enum0.Value1;
		data.putParcelable("prop0", new Enum0Parcelable(testprop0));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onProp0Changed(testprop0);	    
    }
    
    @Test
     public void setPropertyRequestprop0()
    {
		Enum0 testprop0 = Enum0.Value1;

        testedClient.setProp0(testprop0);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.PROP_Prop0.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(Enum0Parcelable.class.getClassLoader());
        
			Enum0 receivedprop0 = data.getParcelable("prop0", Enum0Parcelable.class).getEnum0();
        assertEquals(receivedprop0, testprop0);
    }
    
    @Test
    public void onReceiveprop1PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, EnumInterfaceMessageType.SET_Prop1.getValue());
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

        assertEquals(EnumInterfaceMessageType.PROP_Prop1.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(Enum1Parcelable.class.getClassLoader());
        
			Enum1 receivedprop1 = data.getParcelable("prop1", Enum1Parcelable.class).getEnum1();
        assertEquals(receivedprop1, testprop1);
    }
    
    @Test
    public void onReceiveprop2PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, EnumInterfaceMessageType.SET_Prop2.getValue());
        Bundle data = new Bundle();
		Enum2 testprop2 = Enum2.Value1;
		data.putParcelable("prop2", new Enum2Parcelable(testprop2));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onProp2Changed(testprop2);	    
    }
    
    @Test
     public void setPropertyRequestprop2()
    {
		Enum2 testprop2 = Enum2.Value1;

        testedClient.setProp2(testprop2);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.PROP_Prop2.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(Enum2Parcelable.class.getClassLoader());
        
			Enum2 receivedprop2 = data.getParcelable("prop2", Enum2Parcelable.class).getEnum2();
        assertEquals(receivedprop2, testprop2);
    }
    
    @Test
    public void onReceiveprop3PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, EnumInterfaceMessageType.SET_Prop3.getValue());
        Bundle data = new Bundle();
		Enum3 testprop3 = Enum3.Value2;
		data.putParcelable("prop3", new Enum3Parcelable(testprop3));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onProp3Changed(testprop3);	    
    }
    
    @Test
     public void setPropertyRequestprop3()
    {
		Enum3 testprop3 = Enum3.Value2;

        testedClient.setProp3(testprop3);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.PROP_Prop3.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(Enum3Parcelable.class.getClassLoader());
        
			Enum3 receivedprop3 = data.getParcelable("prop3", Enum3Parcelable.class).getEnum3();
        assertEquals(receivedprop3, testprop3);
    }
    
    @Test
    public void whenNotifiedsig0() throws RemoteException
    {

        Message msg = Message.obtain(null, EnumInterfaceMessageType.SIG_Sig0.getValue());
        Bundle data = new Bundle();
		Enum0 testparam0 = Enum0.Value1;
		data.putParcelable("param0", new Enum0Parcelable(testparam0));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSig0(testparam0);

}
    @Test
    public void whenNotifiedsig1() throws RemoteException
    {

        Message msg = Message.obtain(null, EnumInterfaceMessageType.SIG_Sig1.getValue());
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

        Message msg = Message.obtain(null, EnumInterfaceMessageType.SIG_Sig2.getValue());
        Bundle data = new Bundle();
		Enum2 testparam2 = Enum2.Value1;
		data.putParcelable("param2", new Enum2Parcelable(testparam2));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSig2(testparam2);

}
    @Test
    public void whenNotifiedsig3() throws RemoteException
    {

        Message msg = Message.obtain(null, EnumInterfaceMessageType.SIG_Sig3.getValue());
        Bundle data = new Bundle();
		Enum3 testparam3 = Enum3.Value2;
		data.putParcelable("param3", new Enum3Parcelable(testparam3));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSig3(testparam3);

}


    public void onfunc0Request() throws RemoteException {

        // Execute method
		Enum0 testparam0 = Enum0.Value1;
        Enum0 expectedResult = Enum0.Value1;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Enum0> resFuture = testedClient.func0Async(testparam0);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(EnumInterfaceMessageType.RPC_Func0Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(Enum0Parcelable.class.getClassLoader());
        
			Enum0 receivedparam0 = data.getParcelable("param0", Enum0Parcelable.class).getEnum0();
        assertEquals(receivedparam0, testparam0);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, EnumInterfaceMessageType.RPC_Func0Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new Enum0Parcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

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
        assertEquals(EnumInterfaceMessageType.RPC_Func1Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(Enum1Parcelable.class.getClassLoader());
        
			Enum1 receivedparam1 = data.getParcelable("param1", Enum1Parcelable.class).getEnum1();
        assertEquals(receivedparam1, testparam1);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, EnumInterfaceMessageType.RPC_Func1Resp.getValue());

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
		Enum2 testparam2 = Enum2.Value1;
        Enum2 expectedResult = Enum2.Value1;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Enum2> resFuture = testedClient.func2Async(testparam2);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(EnumInterfaceMessageType.RPC_Func2Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(Enum2Parcelable.class.getClassLoader());
        
			Enum2 receivedparam2 = data.getParcelable("param2", Enum2Parcelable.class).getEnum2();
        assertEquals(receivedparam2, testparam2);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, EnumInterfaceMessageType.RPC_Func2Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new Enum2Parcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfunc3Request() throws RemoteException {

        // Execute method
		Enum3 testparam3 = Enum3.Value2;
        Enum3 expectedResult = Enum3.Value2;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<Enum3> resFuture = testedClient.func3Async(testparam3);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(EnumInterfaceMessageType.RPC_Func3Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(Enum3Parcelable.class.getClassLoader());
        
			Enum3 receivedparam3 = data.getParcelable("param3", Enum3Parcelable.class).getEnum3();
        assertEquals(receivedparam3, testparam3);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, EnumInterfaceMessageType.RPC_Func3Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new Enum3Parcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
