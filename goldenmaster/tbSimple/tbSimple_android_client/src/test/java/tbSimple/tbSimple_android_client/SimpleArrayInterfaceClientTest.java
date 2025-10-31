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
        boolean[] testpropBool = new boolean[1];
        testpropBool[0] = true;
		data.putBooleanArray("propBool", testpropBool);
        int[] testpropInt = new int[1];
        testpropInt[0] = 1;
		data.putIntArray("propInt", testpropInt);
        int[] testpropInt32 = new int[1];
        testpropInt32[0] = 1;
		data.putIntArray("propInt32", testpropInt32);
        long[] testpropInt64 = new long[1];
        testpropInt64[0] = 1L;
		data.putLongArray("propInt64", testpropInt64);
        float[] testpropFloat = new float[1];
        testpropFloat[0] = 1.0f;
		data.putFloatArray("propFloat", testpropFloat);
        float[] testpropFloat32 = new float[1];
        testpropFloat32[0] = 1.0f;
		data.putFloatArray("propFloat32", testpropFloat32);
        double[] testpropFloat64 = new double[1];
        testpropFloat64[0] = 1.0;
		data.putDoubleArray("propFloat64", testpropFloat64);
        String[] testpropString = new String[1];
        testpropString[0] = new String("xyz");
		data.putStringArray("propString", testpropString);
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
        boolean[] testpropBool = new boolean[1];
        testpropBool[0] = true;
		data.putBooleanArray("propBool", testpropBool);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropBoolChanged(testpropBool);	    
    }
    
    @Test
     public void setPropertyRequestpropBool()
    {
        boolean[] testpropBool = new boolean[1];
        testpropBool[0] = true;

        testedClient.setPropBool(testpropBool);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropBool.getValue(), response.what);
        Bundle data = response.getData();
        
			boolean[] receivedpropBool = data.getBooleanArray("propBool");
        assertEquals(receivedpropBool, testpropBool);
    }
    
    @Test
    public void onReceivepropIntPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropInt.getValue());
        Bundle data = new Bundle();
        int[] testpropInt = new int[1];
        testpropInt[0] = 1;
		data.putIntArray("propInt", testpropInt);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropIntChanged(testpropInt);	    
    }
    
    @Test
     public void setPropertyRequestpropInt()
    {
        int[] testpropInt = new int[1];
        testpropInt[0] = 1;

        testedClient.setPropInt(testpropInt);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropInt.getValue(), response.what);
        Bundle data = response.getData();
        
			int[] receivedpropInt = data.getIntArray("propInt");
        assertEquals(receivedpropInt, testpropInt);
    }
    
    @Test
    public void onReceivepropInt32PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropInt32.getValue());
        Bundle data = new Bundle();
        int[] testpropInt32 = new int[1];
        testpropInt32[0] = 1;
		data.putIntArray("propInt32", testpropInt32);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropInt32Changed(testpropInt32);	    
    }
    
    @Test
     public void setPropertyRequestpropInt32()
    {
        int[] testpropInt32 = new int[1];
        testpropInt32[0] = 1;

        testedClient.setPropInt32(testpropInt32);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropInt32.getValue(), response.what);
        Bundle data = response.getData();
        
			int[] receivedpropInt32 = data.getIntArray("propInt32");
        assertEquals(receivedpropInt32, testpropInt32);
    }
    
    @Test
    public void onReceivepropInt64PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropInt64.getValue());
        Bundle data = new Bundle();
        long[] testpropInt64 = new long[1];
        testpropInt64[0] = 1L;
		data.putLongArray("propInt64", testpropInt64);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropInt64Changed(testpropInt64);	    
    }
    
    @Test
     public void setPropertyRequestpropInt64()
    {
        long[] testpropInt64 = new long[1];
        testpropInt64[0] = 1L;

        testedClient.setPropInt64(testpropInt64);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropInt64.getValue(), response.what);
        Bundle data = response.getData();
        
			long[] receivedpropInt64 = data.getLongArray("propInt64");
        assertEquals(receivedpropInt64, testpropInt64);
    }
    
    @Test
    public void onReceivepropFloatPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropFloat.getValue());
        Bundle data = new Bundle();
        float[] testpropFloat = new float[1];
        testpropFloat[0] = 1.0f;
		data.putFloatArray("propFloat", testpropFloat);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloatChanged(testpropFloat);	    
    }
    
    @Test
     public void setPropertyRequestpropFloat()
    {
        float[] testpropFloat = new float[1];
        testpropFloat[0] = 1.0f;

        testedClient.setPropFloat(testpropFloat);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropFloat.getValue(), response.what);
        Bundle data = response.getData();
        
			float[] receivedpropFloat = data.getFloatArray("propFloat");
        assertEquals(receivedpropFloat, testpropFloat);
    }
    
    @Test
    public void onReceivepropFloat32PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropFloat32.getValue());
        Bundle data = new Bundle();
        float[] testpropFloat32 = new float[1];
        testpropFloat32[0] = 1.0f;
		data.putFloatArray("propFloat32", testpropFloat32);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloat32Changed(testpropFloat32);	    
    }
    
    @Test
     public void setPropertyRequestpropFloat32()
    {
        float[] testpropFloat32 = new float[1];
        testpropFloat32[0] = 1.0f;

        testedClient.setPropFloat32(testpropFloat32);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropFloat32.getValue(), response.what);
        Bundle data = response.getData();
        
			float[] receivedpropFloat32 = data.getFloatArray("propFloat32");
        assertEquals(receivedpropFloat32, testpropFloat32);
    }
    
    @Test
    public void onReceivepropFloat64PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropFloat64.getValue());
        Bundle data = new Bundle();
        double[] testpropFloat64 = new double[1];
        testpropFloat64[0] = 1.0;
		data.putDoubleArray("propFloat64", testpropFloat64);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropFloat64Changed(testpropFloat64);	    
    }
    
    @Test
     public void setPropertyRequestpropFloat64()
    {
        double[] testpropFloat64 = new double[1];
        testpropFloat64[0] = 1.0;

        testedClient.setPropFloat64(testpropFloat64);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropFloat64.getValue(), response.what);
        Bundle data = response.getData();
        
			double[] receivedpropFloat64 = data.getDoubleArray("propFloat64");
        assertEquals(receivedpropFloat64, testpropFloat64);
    }
    
    @Test
    public void onReceivepropStringPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.SET_PropString.getValue());
        Bundle data = new Bundle();
        String[] testpropString = new String[1];
        testpropString[0] = new String("xyz");
		data.putStringArray("propString", testpropString);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
		inOrderEventListener.verify(listenerMock,times(1)).onPropStringChanged(testpropString);	    
    }
    
    @Test
     public void setPropertyRequestpropString()
    {
        String[] testpropString = new String[1];
        testpropString[0] = new String("xyz");

        testedClient.setPropString(testpropString);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.PROP_PropString.getValue(), response.what);
        Bundle data = response.getData();
        
			String[] receivedpropString = data.getStringArray("propString");
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
        boolean[] testparamBool = new boolean[1];
        testparamBool[0] = true;
		data.putBooleanArray("paramBool", testparamBool);

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
        int[] testparamInt = new int[1];
        testparamInt[0] = 1;
		data.putIntArray("paramInt", testparamInt);

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
        int[] testparamInt32 = new int[1];
        testparamInt32[0] = 1;
		data.putIntArray("paramInt32", testparamInt32);

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
        long[] testparamInt64 = new long[1];
        testparamInt64[0] = 1L;
		data.putLongArray("paramInt64", testparamInt64);

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
        float[] testparamFloat = new float[1];
        testparamFloat[0] = 1.0f;
		data.putFloatArray("paramFloat", testparamFloat);

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
        float[] testparamFloa32 = new float[1];
        testparamFloa32[0] = 1.0f;
		data.putFloatArray("paramFloa32", testparamFloa32);

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
        double[] testparamFloat64 = new double[1];
        testparamFloat64[0] = 1.0;
		data.putDoubleArray("paramFloat64", testparamFloat64);

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
        String[] testparamString = new String[1];
        testparamString[0] = new String("xyz");
		data.putStringArray("paramString", testparamString);

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onSigString(testparamString);

}


    public void onfuncBoolRequest() throws RemoteException {

        // Execute method
        boolean[] testparamBool = new boolean[1];
        testparamBool[0] = true;
        boolean[] expectedResult = new boolean[1];
        expectedResult[0] = true;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<boolean[]> resFuture = testedClient.funcBoolAsync(testparamBool);

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
        
        
			boolean[] receivedparamBool = data.getBooleanArray("paramBool");
        assertEquals(receivedparamBool, testparamBool);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncBoolResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putBooleanArray("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncIntRequest() throws RemoteException {

        // Execute method
        int[] testparamInt = new int[1];
        testparamInt[0] = 1;
        int[] expectedResult = new int[1];
        expectedResult[0] = 1;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<int[]> resFuture = testedClient.funcIntAsync(testparamInt);

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
        
        
			int[] receivedparamInt = data.getIntArray("paramInt");
        assertEquals(receivedparamInt, testparamInt);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncIntResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putIntArray("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncInt32Request() throws RemoteException {

        // Execute method
        int[] testparamInt32 = new int[1];
        testparamInt32[0] = 1;
        int[] expectedResult = new int[1];
        expectedResult[0] = 1;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<int[]> resFuture = testedClient.funcInt32Async(testparamInt32);

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
        
        
			int[] receivedparamInt32 = data.getIntArray("paramInt32");
        assertEquals(receivedparamInt32, testparamInt32);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncInt32Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putIntArray("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncInt64Request() throws RemoteException {

        // Execute method
        long[] testparamInt64 = new long[1];
        testparamInt64[0] = 1L;
        long[] expectedResult = new long[1];
        expectedResult[0] = 1L;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<long[]> resFuture = testedClient.funcInt64Async(testparamInt64);

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
        
        
			long[] receivedparamInt64 = data.getLongArray("paramInt64");
        assertEquals(receivedparamInt64, testparamInt64);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncInt64Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putLongArray("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncFloatRequest() throws RemoteException {

        // Execute method
        float[] testparamFloat = new float[1];
        testparamFloat[0] = 1.0f;
        float[] expectedResult = new float[1];
        expectedResult[0] = 1.0f;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<float[]> resFuture = testedClient.funcFloatAsync(testparamFloat);

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
        
        
			float[] receivedparamFloat = data.getFloatArray("paramFloat");
        assertEquals(receivedparamFloat, testparamFloat);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncFloatResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putFloatArray("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncFloat32Request() throws RemoteException {

        // Execute method
        float[] testparamFloat32 = new float[1];
        testparamFloat32[0] = 1.0f;
        float[] expectedResult = new float[1];
        expectedResult[0] = 1.0f;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<float[]> resFuture = testedClient.funcFloat32Async(testparamFloat32);

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
        
        
			float[] receivedparamFloat32 = data.getFloatArray("paramFloat32");
        assertEquals(receivedparamFloat32, testparamFloat32);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncFloat32Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putFloatArray("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncFloat64Request() throws RemoteException {

        // Execute method
        double[] testparamFloat = new double[1];
        testparamFloat[0] = 1.0;
        double[] expectedResult = new double[1];
        expectedResult[0] = 1.0;

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<double[]> resFuture = testedClient.funcFloat64Async(testparamFloat);

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
        
        
			double[] receivedparamFloat = data.getDoubleArray("paramFloat");
        assertEquals(receivedparamFloat, testparamFloat);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncFloat64Resp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putDoubleArray("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onfuncStringRequest() throws RemoteException {

        // Execute method
        String[] testparamString = new String[1];
        testparamString[0] = new String("xyz");
        String[] expectedResult = new String[1];
        expectedResult[0] = new String("xyz");

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<String[]> resFuture = testedClient.funcStringAsync(testparamString);

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
        
        
			String[] receivedparamString = data.getStringArray("paramString");
        assertEquals(receivedparamString, testparamString);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncStringResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putStringArray("result", expectedResult);

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
