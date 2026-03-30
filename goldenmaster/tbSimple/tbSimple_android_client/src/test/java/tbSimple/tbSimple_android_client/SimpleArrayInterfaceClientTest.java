//TODO later// Copyright Epic Games, Inc. All Rights Reserved.
package tbSimple.tbSimple_android_client;

import tbSimple.tbSimple_android_client.SimpleArrayInterfaceClient;

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

import tbSimple.tbSimple_api.ISimpleArrayInterfaceEventListener;
import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_api.AbstractSimpleArrayInterface;
import tbSimple.tbSimple_android_messenger.SimpleArrayInterfaceMessageType;

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


interface ISimpleArrayInterfaceClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SimpleArrayInterfaceClientTest
{

    @Mock
    private Context mMockContext;
   
    private SimpleArrayInterfaceClient testedClient;
    private ISimpleArrayInterfaceEventListener listenerMock = mock(ISimpleArrayInterfaceEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private ISimpleArrayInterfaceClientMessageGetter serviceMessagesStorage = mock(ISimpleArrayInterfaceClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(SimpleArrayInterfaceMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(ISimpleArrayInterfaceClientMessageGetter messageGetterMock)
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


        testedClient = new SimpleArrayInterfaceClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("tbSimple.tbSimple_android_service", "tbSimple.tbSimple_android_service.SimpleArrayInterfaceServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(SimpleArrayInterfaceMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.INIT.getValue());
        Bundle data = new Bundle();
        List<Boolean> testpropBool = new java.util.ArrayList<>();
        testpropBool.add(true);
		data.putBooleanArray("propBool", Conversions.toArray(testpropBool, new boolean[0]));
        List<Integer> testpropInt = new java.util.ArrayList<>();
        testpropInt.add(1);
		data.putIntArray("propInt", Conversions.toArray(testpropInt, new int[0]));
        List<Integer> testpropInt32 = new java.util.ArrayList<>();
        testpropInt32.add(1);
		data.putIntArray("propInt32", Conversions.toArray(testpropInt32, new int[0]));
        List<Long> testpropInt64 = new java.util.ArrayList<>();
        testpropInt64.add(1L);
		data.putLongArray("propInt64", Conversions.toArray(testpropInt64, new long[0]));
        List<Float> testpropFloat = new java.util.ArrayList<>();
        testpropFloat.add(1.0f);
		data.putFloatArray("propFloat", Conversions.toArray(testpropFloat, new float[0]));
        List<Float> testpropFloat32 = new java.util.ArrayList<>();
        testpropFloat32.add(1.0f);
		data.putFloatArray("propFloat32", Conversions.toArray(testpropFloat32, new float[0]));
        List<Double> testpropFloat64 = new java.util.ArrayList<>();
        testpropFloat64.add(1.0);
		data.putDoubleArray("propFloat64", Conversions.toArray(testpropFloat64, new double[0]));
        List<String> testpropString = new java.util.ArrayList<>();
        testpropString.add(new String("xyz"));
		data.putStringArray("propString", Conversions.toArray(testpropString, new String[0]));
		String testpropReadOnlyString = new String("xyz");
		data.putString("propReadOnlyString", testpropReadOnlyString);

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropBoolChanged(testpropBool);
		inOrderEventListener.verify(listenerMock,times(1)).onPropIntChanged(testpropInt);
		inOrderEventListener.verify(listenerMock,times(1)).onPropInt32Changed(testpropInt32);
		inOrderEventListener.verify(listenerMock,times(1)).onPropInt64Changed(testpropInt64);
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloatChanged(testpropFloat);
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloat32Changed(testpropFloat32);
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloat64Changed(testpropFloat64);
		inOrderEventListener.verify(listenerMock,times(1)).onPropStringChanged(testpropString);
		inOrderEventListener.verify(listenerMock,times(1)).onPropReadOnlyStringChanged(testpropReadOnlyString);
    }
    @Test
    public void onReceivepropBoolPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropBool.getValue());
        Bundle data = new Bundle();
        List<Boolean> testpropBool = new java.util.ArrayList<>();
        testpropBool.add(true);
		data.putBooleanArray("propBool", Conversions.toArray(testpropBool, new boolean[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropBoolChanged(testpropBool);	    
    }
    
    @Test
     public void setPropertyRequestpropBool()
    {
        List<Boolean> testpropBool = new java.util.ArrayList<>();
        testpropBool.add(true);

        testedClient.setPropBool(testpropBool);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropBool.getValue(), response.what);
        Bundle data = response.getData();
        
			List<Boolean> receivedpropBool = Conversions.toList(data.getBooleanArray("propBool"));
        assertEquals(receivedpropBool, testpropBool);
    }
    
    @Test
    public void onReceivepropIntPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropInt.getValue());
        Bundle data = new Bundle();
        List<Integer> testpropInt = new java.util.ArrayList<>();
        testpropInt.add(1);
		data.putIntArray("propInt", Conversions.toArray(testpropInt, new int[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropIntChanged(testpropInt);	    
    }
    
    @Test
     public void setPropertyRequestpropInt()
    {
        List<Integer> testpropInt = new java.util.ArrayList<>();
        testpropInt.add(1);

        testedClient.setPropInt(testpropInt);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropInt.getValue(), response.what);
        Bundle data = response.getData();
        
			List<Integer> receivedpropInt = Conversions.toList(data.getIntArray("propInt"));
        assertEquals(receivedpropInt, testpropInt);
    }
    
    @Test
    public void onReceivepropInt32PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropInt32.getValue());
        Bundle data = new Bundle();
        List<Integer> testpropInt32 = new java.util.ArrayList<>();
        testpropInt32.add(1);
		data.putIntArray("propInt32", Conversions.toArray(testpropInt32, new int[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropInt32Changed(testpropInt32);	    
    }
    
    @Test
     public void setPropertyRequestpropInt32()
    {
        List<Integer> testpropInt32 = new java.util.ArrayList<>();
        testpropInt32.add(1);

        testedClient.setPropInt32(testpropInt32);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropInt32.getValue(), response.what);
        Bundle data = response.getData();
        
			List<Integer> receivedpropInt32 = Conversions.toList(data.getIntArray("propInt32"));
        assertEquals(receivedpropInt32, testpropInt32);
    }
    
    @Test
    public void onReceivepropInt64PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropInt64.getValue());
        Bundle data = new Bundle();
        List<Long> testpropInt64 = new java.util.ArrayList<>();
        testpropInt64.add(1L);
		data.putLongArray("propInt64", Conversions.toArray(testpropInt64, new long[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropInt64Changed(testpropInt64);	    
    }
    
    @Test
     public void setPropertyRequestpropInt64()
    {
        List<Long> testpropInt64 = new java.util.ArrayList<>();
        testpropInt64.add(1L);

        testedClient.setPropInt64(testpropInt64);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropInt64.getValue(), response.what);
        Bundle data = response.getData();
        
			List<Long> receivedpropInt64 = Conversions.toList(data.getLongArray("propInt64"));
        assertEquals(receivedpropInt64, testpropInt64);
    }
    
    @Test
    public void onReceivepropFloatPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropFloat.getValue());
        Bundle data = new Bundle();
        List<Float> testpropFloat = new java.util.ArrayList<>();
        testpropFloat.add(1.0f);
		data.putFloatArray("propFloat", Conversions.toArray(testpropFloat, new float[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloatChanged(testpropFloat);	    
    }
    
    @Test
     public void setPropertyRequestpropFloat()
    {
        List<Float> testpropFloat = new java.util.ArrayList<>();
        testpropFloat.add(1.0f);

        testedClient.setPropFloat(testpropFloat);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropFloat.getValue(), response.what);
        Bundle data = response.getData();
        
			List<Float> receivedpropFloat = Conversions.toList(data.getFloatArray("propFloat"));
        assertEquals(receivedpropFloat, testpropFloat);
    }
    
    @Test
    public void onReceivepropFloat32PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropFloat32.getValue());
        Bundle data = new Bundle();
        List<Float> testpropFloat32 = new java.util.ArrayList<>();
        testpropFloat32.add(1.0f);
		data.putFloatArray("propFloat32", Conversions.toArray(testpropFloat32, new float[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloat32Changed(testpropFloat32);	    
    }
    
    @Test
     public void setPropertyRequestpropFloat32()
    {
        List<Float> testpropFloat32 = new java.util.ArrayList<>();
        testpropFloat32.add(1.0f);

        testedClient.setPropFloat32(testpropFloat32);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropFloat32.getValue(), response.what);
        Bundle data = response.getData();
        
			List<Float> receivedpropFloat32 = Conversions.toList(data.getFloatArray("propFloat32"));
        assertEquals(receivedpropFloat32, testpropFloat32);
    }
    
    @Test
    public void onReceivepropFloat64PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropFloat64.getValue());
        Bundle data = new Bundle();
        List<Double> testpropFloat64 = new java.util.ArrayList<>();
        testpropFloat64.add(1.0);
		data.putDoubleArray("propFloat64", Conversions.toArray(testpropFloat64, new double[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloat64Changed(testpropFloat64);	    
    }
    
    @Test
     public void setPropertyRequestpropFloat64()
    {
        List<Double> testpropFloat64 = new java.util.ArrayList<>();
        testpropFloat64.add(1.0);

        testedClient.setPropFloat64(testpropFloat64);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropFloat64.getValue(), response.what);
        Bundle data = response.getData();
        
			List<Double> receivedpropFloat64 = Conversions.toList(data.getDoubleArray("propFloat64"));
        assertEquals(receivedpropFloat64, testpropFloat64);
    }
    
    @Test
    public void onReceivepropStringPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropString.getValue());
        Bundle data = new Bundle();
        List<String> testpropString = new java.util.ArrayList<>();
        testpropString.add(new String("xyz"));
		data.putStringArray("propString", Conversions.toArray(testpropString, new String[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropStringChanged(testpropString);	    
    }
    
    @Test
     public void setPropertyRequestpropString()
    {
        List<String> testpropString = new java.util.ArrayList<>();
        testpropString.add(new String("xyz"));

        testedClient.setPropString(testpropString);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropString.getValue(), response.what);
        Bundle data = response.getData();
        
			List<String> receivedpropString = Conversions.toList(data.getStringArray("propString"));
        assertEquals(receivedpropString, testpropString);
    }
    
    @Test
    public void onReceivepropReadOnlyStringPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropReadOnlyString.getValue());
        Bundle data = new Bundle();
		String testpropReadOnlyString = new String("xyz");
		data.putString("propReadOnlyString", testpropReadOnlyString);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropReadOnlyStringChanged(testpropReadOnlyString);	    
    }
    @Test
    public void whenNotifiedsigBool() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SIG_SigBool.getValue());
        Bundle data = new Bundle();
        List<Boolean> testparamBool = new java.util.ArrayList<>();
        testparamBool.add(true);
		data.putBooleanArray("paramBool", Conversions.toArray(testparamBool, new boolean[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigBool(testparamBool);

}
    @Test
    public void whenNotifiedsigInt() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SIG_SigInt.getValue());
        Bundle data = new Bundle();
        List<Integer> testparamInt = new java.util.ArrayList<>();
        testparamInt.add(1);
		data.putIntArray("paramInt", Conversions.toArray(testparamInt, new int[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigInt(testparamInt);

}
    @Test
    public void whenNotifiedsigInt32() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SIG_SigInt32.getValue());
        Bundle data = new Bundle();
        List<Integer> testparamInt32 = new java.util.ArrayList<>();
        testparamInt32.add(1);
		data.putIntArray("paramInt32", Conversions.toArray(testparamInt32, new int[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigInt32(testparamInt32);

}
    @Test
    public void whenNotifiedsigInt64() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SIG_SigInt64.getValue());
        Bundle data = new Bundle();
        List<Long> testparamInt64 = new java.util.ArrayList<>();
        testparamInt64.add(1L);
		data.putLongArray("paramInt64", Conversions.toArray(testparamInt64, new long[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigInt64(testparamInt64);

}
    @Test
    public void whenNotifiedsigFloat() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SIG_SigFloat.getValue());
        Bundle data = new Bundle();
        List<Float> testparamFloat = new java.util.ArrayList<>();
        testparamFloat.add(1.0f);
		data.putFloatArray("paramFloat", Conversions.toArray(testparamFloat, new float[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigFloat(testparamFloat);

}
    @Test
    public void whenNotifiedsigFloat32() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SIG_SigFloat32.getValue());
        Bundle data = new Bundle();
        List<Float> testparamFloa32 = new java.util.ArrayList<>();
        testparamFloa32.add(1.0f);
		data.putFloatArray("paramFloa32", Conversions.toArray(testparamFloa32, new float[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigFloat32(testparamFloa32);

}
    @Test
    public void whenNotifiedsigFloat64() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SIG_SigFloat64.getValue());
        Bundle data = new Bundle();
        List<Double> testparamFloat64 = new java.util.ArrayList<>();
        testparamFloat64.add(1.0);
		data.putDoubleArray("paramFloat64", Conversions.toArray(testparamFloat64, new double[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigFloat64(testparamFloat64);

}
    @Test
    public void whenNotifiedsigString() throws RemoteException
    {

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SIG_SigString.getValue());
        Bundle data = new Bundle();
        List<String> testparamString = new java.util.ArrayList<>();
        testparamString.add(new String("xyz"));
		data.putStringArray("paramString", Conversions.toArray(testparamString, new String[0]));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigString(testparamString);

}

    @Test
    public void onfuncBoolRequest() throws RemoteException {

        // Execute method
        List<Boolean> testparamBool = new java.util.ArrayList<>();
        testparamBool.add(true);
        List<Boolean> expectedResult = new ArrayList<>();
        expectedResult.add(true);

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<List<Boolean>> resFuture = testedClient.funcBoolAsync(testparamBool);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncBoolReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			List<Boolean> receivedparamBool = Conversions.toList(data.getBooleanArray("paramBool"));
        assertEquals(receivedparamBool, testparamBool);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncBoolResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putBooleanArray("result", Conversions.toArray(expectedResult, new boolean[0]));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncIntRequest() throws RemoteException {

        // Execute method
        List<Integer> testparamInt = new java.util.ArrayList<>();
        testparamInt.add(1);
        List<Integer> expectedResult = new ArrayList<>();
        expectedResult.add(1);

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<List<Integer>> resFuture = testedClient.funcIntAsync(testparamInt);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncIntReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			List<Integer> receivedparamInt = Conversions.toList(data.getIntArray("paramInt"));
        assertEquals(receivedparamInt, testparamInt);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncIntResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putIntArray("result", Conversions.toArray(expectedResult, new int[0]));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncInt32Request() throws RemoteException {

        // Execute method
        List<Integer> testparamInt32 = new java.util.ArrayList<>();
        testparamInt32.add(1);
        List<Integer> expectedResult = new ArrayList<>();
        expectedResult.add(1);

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<List<Integer>> resFuture = testedClient.funcInt32Async(testparamInt32);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncInt32Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			List<Integer> receivedparamInt32 = Conversions.toList(data.getIntArray("paramInt32"));
        assertEquals(receivedparamInt32, testparamInt32);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncInt32Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putIntArray("result", Conversions.toArray(expectedResult, new int[0]));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncInt64Request() throws RemoteException {

        // Execute method
        List<Long> testparamInt64 = new java.util.ArrayList<>();
        testparamInt64.add(1L);
        List<Long> expectedResult = new ArrayList<>();
        expectedResult.add(1L);

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<List<Long>> resFuture = testedClient.funcInt64Async(testparamInt64);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncInt64Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			List<Long> receivedparamInt64 = Conversions.toList(data.getLongArray("paramInt64"));
        assertEquals(receivedparamInt64, testparamInt64);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncInt64Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putLongArray("result", Conversions.toArray(expectedResult, new long[0]));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncFloatRequest() throws RemoteException {

        // Execute method
        List<Float> testparamFloat = new java.util.ArrayList<>();
        testparamFloat.add(1.0f);
        List<Float> expectedResult = new ArrayList<>();
        expectedResult.add(1.0f);

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<List<Float>> resFuture = testedClient.funcFloatAsync(testparamFloat);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncFloatReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			List<Float> receivedparamFloat = Conversions.toList(data.getFloatArray("paramFloat"));
        assertEquals(receivedparamFloat, testparamFloat);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncFloatResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putFloatArray("result", Conversions.toArray(expectedResult, new float[0]));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncFloat32Request() throws RemoteException {

        // Execute method
        List<Float> testparamFloat32 = new java.util.ArrayList<>();
        testparamFloat32.add(1.0f);
        List<Float> expectedResult = new ArrayList<>();
        expectedResult.add(1.0f);

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<List<Float>> resFuture = testedClient.funcFloat32Async(testparamFloat32);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncFloat32Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			List<Float> receivedparamFloat32 = Conversions.toList(data.getFloatArray("paramFloat32"));
        assertEquals(receivedparamFloat32, testparamFloat32);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncFloat32Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putFloatArray("result", Conversions.toArray(expectedResult, new float[0]));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncFloat64Request() throws RemoteException {

        // Execute method
        List<Double> testparamFloat = new java.util.ArrayList<>();
        testparamFloat.add(1.0);
        List<Double> expectedResult = new ArrayList<>();
        expectedResult.add(1.0);

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<List<Double>> resFuture = testedClient.funcFloat64Async(testparamFloat);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncFloat64Req.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			List<Double> receivedparamFloat = Conversions.toList(data.getDoubleArray("paramFloat"));
        assertEquals(receivedparamFloat, testparamFloat);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncFloat64Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putDoubleArray("result", Conversions.toArray(expectedResult, new double[0]));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

    @Test
    public void onfuncStringRequest() throws RemoteException {

        // Execute method
        List<String> testparamString = new java.util.ArrayList<>();
        testparamString.add(new String("xyz"));
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add(new String("xyz"));

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<List<String>> resFuture = testedClient.funcStringAsync(testparamString);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncStringReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        
			List<String> receivedparamString = Conversions.toList(data.getStringArray("paramString"));
        assertEquals(receivedparamString, testparamString);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncStringResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putStringArray("result", Conversions.toArray(expectedResult, new String[0]));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
