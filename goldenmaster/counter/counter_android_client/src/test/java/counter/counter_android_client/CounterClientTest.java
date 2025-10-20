//TODO later// Copyright Epic Games, Inc. All Rights Reserved.
package counter.counter_android_client;

import counter.counter_android_client.CounterClient;

//import message type and parcelabe types
import counter.counter_api.CounterTestHelper;

import counter.counter_api.ICounterEventListener;
import counter.counter_api.ICounter;
import counter.counter_api.AbstractCounter;
import counter.counter_android_messenger.CounterMessageType;

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


interface ICounterClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class CounterClientTest
{

    @Mock
    private Context mMockContext;
   
    private CounterClient testedClient;
    private ICounterEventListener listenerMock = mock(ICounterEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private ICounterClientMessageGetter serviceMessagesStorage = mock(ICounterClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(CounterMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(ICounterClientMessageGetter messageGetterMock)
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


        testedClient = new CounterClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("counter.counter_android_service", "counter.counter_android_service.CounterServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(CounterMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, CounterMessageType.INIT.getValue());
        Bundle data = new Bundle();
        customTypes.customTypes_api.Vector3D testvector = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
		data.putParcelable("vector", new customTypes.customTypes_android_messenger.Vector3DParcelable(testvector));
		org.apache.commons.math3.geometry.euclidean.threed.Vector3D testextern_vector = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
		data.putParcelable("extern_vector", new externTypes.externTypes_android_messenger.MyVector3DParcelable(testextern_vector));
        customTypes.customTypes_api.Vector3D[] testvectorArray = new customTypes.customTypes_api.Vector3D[1];
        testvectorArray[0] = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
		data.putParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(testvectorArray));
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] testextern_vectorArray = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[1];
		testextern_vectorArray[0] = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
		data.putParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(testextern_vectorArray));

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onVectorChanged(any(customTypes.customTypes_api.Vector3D.class));
        
        // Make sure test data is properly filled and in case of extern serialization is in place.
        //inOrderEventListener.verify(listenerMock,times(1)).onExternVectorChanged(any(org.apache.commons.math3.geometry.euclidean.threed.Vector3D.class));
        inOrderEventListener.verify(listenerMock,times(1)).onVectorArrayChanged(any(customTypes.customTypes_api.Vector3D[].class));
        
        // Make sure test data is properly filled and in case of extern serialization is in place.
        //inOrderEventListener.verify(listenerMock,times(1)).onExternVectorArrayChanged(any(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[].class));
    }
    @Test
    public void onReceivevectorPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, CounterMessageType.SET_Vector.getValue());
        Bundle data = new Bundle();
        customTypes.customTypes_api.Vector3D testvector = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
		data.putParcelable("vector", new customTypes.customTypes_android_messenger.Vector3DParcelable(testvector));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onVectorChanged(any(customTypes.customTypes_api.Vector3D.class));	    
    }
    
    @Test
     public void setPropertyRequestvector()
    {
        customTypes.customTypes_api.Vector3D testvector = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();

        testedClient.setVector(testvector);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(CounterMessageType.PROP_Vector.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
        
			customTypes.customTypes_api.Vector3D receivedvector = data.getParcelable("vector", customTypes.customTypes_android_messenger.Vector3DParcelable.class).getVector3D();
        assertEquals(receivedvector, testvector);
    }
    
    @Test
    public void onReceiveextern_vectorPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, CounterMessageType.SET_ExternVector.getValue());
        Bundle data = new Bundle();
		org.apache.commons.math3.geometry.euclidean.threed.Vector3D testextern_vector = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
		data.putParcelable("extern_vector", new externTypes.externTypes_android_messenger.MyVector3DParcelable(testextern_vector));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        // Make sure test data is properly filled and in case of extern serialization is in place.
        //inOrderEventListener.verify(listenerMock,times(1)).onExternVectorChanged(any(org.apache.commons.math3.geometry.euclidean.threed.Vector3D.class));	    
    }
    
    /*
    @Test
     public void setPropertyRequestextern_vector()
    {
		org.apache.commons.math3.geometry.euclidean.threed.Vector3D testextern_vector = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);

        testedClient.setExternVector(testextern_vector);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(CounterMessageType.PROP_ExternVector.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());
        
			org.apache.commons.math3.geometry.euclidean.threed.Vector3D receivedextern_vector = data.getParcelable("extern_vector", externTypes.externTypes_android_messenger.MyVector3DParcelable.class).getMyVector3D();
        assertEquals(receivedextern_vector, testextern_vector);
    }
    
    */
    @Test
    public void onReceivevectorArrayPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, CounterMessageType.SET_VectorArray.getValue());
        Bundle data = new Bundle();
        customTypes.customTypes_api.Vector3D[] testvectorArray = new customTypes.customTypes_api.Vector3D[1];
        testvectorArray[0] = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
		data.putParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(testvectorArray));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderEventListener.verify(listenerMock,times(1)).onVectorArrayChanged(any(customTypes.customTypes_api.Vector3D[].class));	    
    }
    
    @Test
     public void setPropertyRequestvectorArray()
    {
        customTypes.customTypes_api.Vector3D[] testvectorArray = new customTypes.customTypes_api.Vector3D[1];
        testvectorArray[0] = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();

        testedClient.setVectorArray(testvectorArray);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(CounterMessageType.PROP_VectorArray.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
        
            customTypes.customTypes_api.Vector3D[] receivedvectorArray =  customTypes.customTypes_android_messenger.Vector3DParcelable.unwrapArray((customTypes.customTypes_android_messenger.Vector3DParcelable[])data.getParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.class));
        assertEquals(receivedvectorArray, testvectorArray);
    }
    
    @Test
    public void onReceiveextern_vectorArrayPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, CounterMessageType.SET_ExternVectorArray.getValue());
        Bundle data = new Bundle();
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] testextern_vectorArray = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[1];
		testextern_vectorArray[0] = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
		data.putParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(testextern_vectorArray));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        // Make sure test data is properly filled and in case of extern serialization is in place.
        //inOrderEventListener.verify(listenerMock,times(1)).onExternVectorArrayChanged(any(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[].class));	    
    }
    
    /*
    @Test
     public void setPropertyRequestextern_vectorArray()
    {
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] testextern_vectorArray = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[1];
		testextern_vectorArray[0] = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);

        testedClient.setExternVectorArray(testextern_vectorArray);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(CounterMessageType.PROP_ExternVectorArray.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());
        
            org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] receivedextern_vectorArray =  externTypes.externTypes_android_messenger.MyVector3DParcelable.unwrapArray((externTypes.externTypes_android_messenger.MyVector3DParcelable[])data.getParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.class));
        assertEquals(receivedextern_vectorArray, testextern_vectorArray);
    }
    
    */
    @Test
    public void whenNotifiedvalueChanged() throws RemoteException
    {

        Message msg = Message.obtain(null, CounterMessageType.SIG_ValueChanged.getValue());
        Bundle data = new Bundle();
        customTypes.customTypes_api.Vector3D testvector = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
		data.putParcelable("vector", new customTypes.customTypes_android_messenger.Vector3DParcelable(testvector));
		org.apache.commons.math3.geometry.euclidean.threed.Vector3D testextern_vector = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
		data.putParcelable("extern_vector", new externTypes.externTypes_android_messenger.MyVector3DParcelable(testextern_vector));
        customTypes.customTypes_api.Vector3D[] testvectorArray = new customTypes.customTypes_api.Vector3D[1];
        testvectorArray[0] = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
		data.putParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(testvectorArray));
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] testextern_vectorArray = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[1];
		testextern_vectorArray[0] = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
		data.putParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(testextern_vectorArray));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onValueChanged( any(customTypes.customTypes_api.Vector3D.class),  any(org.apache.commons.math3.geometry.euclidean.threed.Vector3D.class),  any(customTypes.customTypes_api.Vector3D[].class),  any(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[].class));

}


    public void onincrementRequest() throws RemoteException {

        // Execute method
		org.apache.commons.math3.geometry.euclidean.threed.Vector3D testvec = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
		org.apache.commons.math3.geometry.euclidean.threed.Vector3D expectedResult = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> resFuture = testedClient.incrementAsync(testvec);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(CounterMessageType.RPC_IncrementReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
    // all structs (even from other modules) are known at compile time (see gradle files) and share same PathClassLoader, any class loader provides access to it.
        data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());
        
			org.apache.commons.math3.geometry.euclidean.threed.Vector3D receivedvec = data.getParcelable("vec", externTypes.externTypes_android_messenger.MyVector3DParcelable.class).getMyVector3D();
        assertEquals(receivedvec, testvec);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, CounterMessageType.RPC_IncrementResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new externTypes.externTypes_android_messenger.MyVector3DParcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onincrementArrayRequest() throws RemoteException {

        // Execute method
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] testvec = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[1];
		testvec[0] = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] expectedResult = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[1];
		expectedResult[0] = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]> resFuture = testedClient.incrementArrayAsync(testvec);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(CounterMessageType.RPC_IncrementArrayReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
    // all structs (even from other modules) are known at compile time (see gradle files) and share same PathClassLoader, any class loader provides access to it.
        data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());
        
            org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] receivedvec =  externTypes.externTypes_android_messenger.MyVector3DParcelable.unwrapArray((externTypes.externTypes_android_messenger.MyVector3DParcelable[])data.getParcelableArray("vec", externTypes.externTypes_android_messenger.MyVector3DParcelable.class));
        assertEquals(receivedvec, testvec);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, CounterMessageType.RPC_IncrementArrayResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelableArray("result", externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void ondecrementRequest() throws RemoteException {

        // Execute method
        customTypes.customTypes_api.Vector3D testvec = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
        customTypes.customTypes_api.Vector3D expectedResult = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<customTypes.customTypes_api.Vector3D> resFuture = testedClient.decrementAsync(testvec);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(CounterMessageType.RPC_DecrementReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
    // all structs (even from other modules) are known at compile time (see gradle files) and share same PathClassLoader, any class loader provides access to it.
        data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
        
			customTypes.customTypes_api.Vector3D receivedvec = data.getParcelable("vec", customTypes.customTypes_android_messenger.Vector3DParcelable.class).getVector3D();
        assertEquals(receivedvec, testvec);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, CounterMessageType.RPC_DecrementResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new customTypes.customTypes_android_messenger.Vector3DParcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void ondecrementArrayRequest() throws RemoteException {

        // Execute method
        customTypes.customTypes_api.Vector3D[] testvec = new customTypes.customTypes_api.Vector3D[1];
        testvec[0] = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
        customTypes.customTypes_api.Vector3D[] expectedResult = new customTypes.customTypes_api.Vector3D[1];
        expectedResult[0] = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<customTypes.customTypes_api.Vector3D[]> resFuture = testedClient.decrementArrayAsync(testvec);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(CounterMessageType.RPC_DecrementArrayReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
    // all structs (even from other modules) are known at compile time (see gradle files) and share same PathClassLoader, any class loader provides access to it.
        data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
        
            customTypes.customTypes_api.Vector3D[] receivedvec =  customTypes.customTypes_android_messenger.Vector3DParcelable.unwrapArray((customTypes.customTypes_android_messenger.Vector3DParcelable[])data.getParcelableArray("vec", customTypes.customTypes_android_messenger.Vector3DParcelable.class));
        assertEquals(receivedvec, testvec);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, CounterMessageType.RPC_DecrementArrayResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelableArray("result", customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
