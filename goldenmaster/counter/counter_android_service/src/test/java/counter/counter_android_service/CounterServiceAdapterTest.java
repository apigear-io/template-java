//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package counter.counter_android_service;

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
import counter.counter_android_service.CounterServiceAdapter;
import counter.counter_android_service.ICounterServiceProvider;

//import message type and parcelabe types
import counter.counter_api.CounterTestHelper;
import counter.counter_api.ICounter;
import counter.counter_android_messenger.CounterParcelable;
import counter.counter_impl.CounterService;


import counter.counter_api.ICounterEventListener;
import counter.counter_android_service.ICounterServiceProvider;
import counter.counter_api.ICounter;
import counter.counter_api.AbstractCounter;
import counter.counter_android_messenger.CounterMessageType;


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

interface ICounterMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class CounterServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private CounterServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private ICounterEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private ICounter backendServiceMock = mock(ICounter.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private ICounterServiceProvider ServiceProvider = mock(ICounterServiceProvider.class);
    private ICounterMessageGetter clientMessagesStorage = mock(ICounterMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, CounterMessageType.UNREGISTER_CLIENT.ordinal());
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

    Handler createClientHandlerMock(ICounterMessageGetter messageGetterMock)
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
        customTypes.customTypes_api.Vector3D initvector = new customTypes.customTypes_api.Vector3D();
        //TODO fill fields
        when(backendServiceMock.getVector()).thenReturn(initvector);
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D initextern_vector = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
        //TODO fill fields
        when(backendServiceMock.getExternVector()).thenReturn(initextern_vector);
        customTypes.customTypes_api.Vector3D init_elementvectorArray = new customTypes.customTypes_api.Vector3D();
        // todo fill if is struct
        customTypes.customTypes_api.Vector3D[] initvectorArray = new customTypes.customTypes_api.Vector3D[]{ init_elementvectorArray } ;
        when(backendServiceMock.getVectorArray()).thenReturn(initvectorArray);
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D init_elementextern_vectorArray = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
        // todo fill if is struct
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] initextern_vectorArray = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]{ init_elementextern_vectorArray } ;
        when(backendServiceMock.getExternVectorArray()).thenReturn(initextern_vectorArray);


        Message registerMsg = Message.obtain(null, CounterMessageType.REGISTER_CLIENT.ordinal());
        registerMsg.getData().putString("connectionID", id);
        registerMsg.replyTo = messenger;
    
        try {
            mServiceMessenger.send(registerMsg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock, times(1)).getVector();
        inOrderBackendService.verify(backendServiceMock, times(1)).getExternVector();
        inOrderBackendService.verify(backendServiceMock, times(1)).getVectorArray();
        inOrderBackendService.verify(backendServiceMock, times(1)).getExternVectorArray();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(CounterMessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        
			customTypes.customTypes_api.Vector3D receivedvector = data.getParcelable("vector", customTypes.customTypes_android_messenger.Vector3DParcelable.class).getVector3D();
        
			org.apache.commons.math3.geometry.euclidean.threed.Vector3D receivedextern_vector = data.getParcelable("extern_vector", externTypes.externTypes_android_messenger.MyVector3DParcelable.class).getMyVector3D();
        
            customTypes.customTypes_api.Vector3D[] receivedvectorArray =  customTypes.customTypes_android_messenger.Vector3DParcelable.unwrapArray((customTypes.customTypes_android_messenger.Vector3DParcelable[])data.getParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.class));
        
            org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] receivedextern_vectorArray =  externTypes.externTypes_android_messenger.MyVector3DParcelable.unwrapArray((externTypes.externTypes_android_messenger.MyVector3DParcelable[])data.getParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.class));
        
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
        // assertEquals(receivedvector, initvector);
        // assertEquals(receivedextern_vector, initextern_vector);
        // assertEquals(receivedvectorArray, initvectorArray);
        // assertEquals(receivedextern_vectorArray, initextern_vectorArray);

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

        testedServiceAdapterIntent = new Intent(mMockContext, CounterServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(CounterServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(ServiceProvider.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(ServiceProvider);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<ICounterEventListener> eventListnerCaptor = ArgumentCaptor.forClass(ICounterEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
    @Test
    public void onReceivevectorPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, CounterMessageType.PROP_Vector.getValue());
        Bundle data = new Bundle();
        customTypes.customTypes_api.Vector3D testvector = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
		data.putParcelable("vector", new customTypes.customTypes_android_messenger.Vector3DParcelable(testvector));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setVector( any(customTypes.customTypes_api.Vector3D.class));
	    
    }

    @Test
     public void whenNotifiedvector()
    {
        customTypes.customTypes_api.Vector3D testvector = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();

        testedAdapterAsEventListener.onVectorChanged(testvector);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(CounterMessageType.SET_Vector.getValue(), response.what);
        Bundle data = response.getData();

        
			customTypes.customTypes_api.Vector3D receivedvector = data.getParcelable("vector", customTypes.customTypes_android_messenger.Vector3DParcelable.class).getVector3D();

        assertEquals(receivedvector, testvector);
    }
    @Test
    public void onReceiveextern_vectorPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, CounterMessageType.PROP_ExternVector.getValue());
        Bundle data = new Bundle();
		org.apache.commons.math3.geometry.euclidean.threed.Vector3D testextern_vector = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
		data.putParcelable("extern_vector", new externTypes.externTypes_android_messenger.MyVector3DParcelable(testextern_vector));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setExternVector( any(org.apache.commons.math3.geometry.euclidean.threed.Vector3D.class));
	    
    }

    @Test
     public void whenNotifiedextern_vector()
    {
		org.apache.commons.math3.geometry.euclidean.threed.Vector3D testextern_vector = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);

        testedAdapterAsEventListener.onExternVectorChanged(testextern_vector);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(CounterMessageType.SET_ExternVector.getValue(), response.what);
        Bundle data = response.getData();

        
			org.apache.commons.math3.geometry.euclidean.threed.Vector3D receivedextern_vector = data.getParcelable("extern_vector", externTypes.externTypes_android_messenger.MyVector3DParcelable.class).getMyVector3D();

        assertEquals(receivedextern_vector, testextern_vector);
    }
    @Test
    public void onReceivevectorArrayPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, CounterMessageType.PROP_VectorArray.getValue());
        Bundle data = new Bundle();
        customTypes.customTypes_api.Vector3D[] testvectorArray = new customTypes.customTypes_api.Vector3D[1];
        testvectorArray[0] = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
		data.putParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(testvectorArray));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setVectorArray( any(customTypes.customTypes_api.Vector3D[].class));
	    
    }

    @Test
     public void whenNotifiedvectorArray()
    {
        customTypes.customTypes_api.Vector3D[] testvectorArray = new customTypes.customTypes_api.Vector3D[1];
        testvectorArray[0] = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();

        testedAdapterAsEventListener.onVectorArrayChanged(testvectorArray);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(CounterMessageType.SET_VectorArray.getValue(), response.what);
        Bundle data = response.getData();

        
            customTypes.customTypes_api.Vector3D[] receivedvectorArray =  customTypes.customTypes_android_messenger.Vector3DParcelable.unwrapArray((customTypes.customTypes_android_messenger.Vector3DParcelable[])data.getParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.class));

        assertEquals(receivedvectorArray, testvectorArray);
    }
    @Test
    public void onReceiveextern_vectorArrayPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, CounterMessageType.PROP_ExternVectorArray.getValue());
        Bundle data = new Bundle();
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] testextern_vectorArray = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[1];
		testextern_vectorArray[0] = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
		data.putParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(testextern_vectorArray));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setExternVectorArray( any(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[].class));
	    
    }

    @Test
     public void whenNotifiedextern_vectorArray()
    {
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] testextern_vectorArray = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[1];
		testextern_vectorArray[0] = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);

        testedAdapterAsEventListener.onExternVectorArrayChanged(testextern_vectorArray);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(CounterMessageType.SET_ExternVectorArray.getValue(), response.what);
        Bundle data = response.getData();

        
            org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] receivedextern_vectorArray =  externTypes.externTypes_android_messenger.MyVector3DParcelable.unwrapArray((externTypes.externTypes_android_messenger.MyVector3DParcelable[])data.getParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.class));

        assertEquals(receivedextern_vectorArray, testextern_vectorArray);
    }
    @Test
    public void whenNotifiedvalueChanged()
    {
        customTypes.customTypes_api.Vector3D testvector = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
		org.apache.commons.math3.geometry.euclidean.threed.Vector3D testextern_vector = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
        customTypes.customTypes_api.Vector3D[] testvectorArray = new customTypes.customTypes_api.Vector3D[1];
        testvectorArray[0] = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] testextern_vectorArray = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[1];
		testextern_vectorArray[0] = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);

        testedAdapterAsEventListener.onValueChanged(testvector, testextern_vector, testvectorArray, testextern_vectorArray);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(CounterMessageType.SIG_ValueChanged.getValue(), response.what);
        Bundle data = response.getData();
        
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
        
			customTypes.customTypes_api.Vector3D receivedvector = data.getParcelable("vector", customTypes.customTypes_android_messenger.Vector3DParcelable.class).getVector3D();
        assertEquals(receivedvector, testvector);
        
			org.apache.commons.math3.geometry.euclidean.threed.Vector3D receivedextern_vector = data.getParcelable("extern_vector", externTypes.externTypes_android_messenger.MyVector3DParcelable.class).getMyVector3D();
        assertEquals(receivedextern_vector, testextern_vector);
        
            customTypes.customTypes_api.Vector3D[] receivedvectorArray =  customTypes.customTypes_android_messenger.Vector3DParcelable.unwrapArray((customTypes.customTypes_android_messenger.Vector3DParcelable[])data.getParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.class));
        assertEquals(receivedvectorArray, testvectorArray);
        
            org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] receivedextern_vectorArray =  externTypes.externTypes_android_messenger.MyVector3DParcelable.unwrapArray((externTypes.externTypes_android_messenger.MyVector3DParcelable[])data.getParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.class));
        assertEquals(receivedextern_vectorArray, testextern_vectorArray);
}

    @Test
    public void onincrementRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, CounterMessageType.RPC_IncrementReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		org.apache.commons.math3.geometry.euclidean.threed.Vector3D testvec = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
		data.putParcelable("vec", new externTypes.externTypes_android_messenger.MyVector3DParcelable(testvec));
		org.apache.commons.math3.geometry.euclidean.threed.Vector3D returnedValue = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);


        when(backendServiceMock.increment( any(org.apache.commons.math3.geometry.euclidean.threed.Vector3D.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).increment( any(org.apache.commons.math3.geometry.euclidean.threed.Vector3D.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(CounterMessageType.RPC_IncrementResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());
		org.apache.commons.math3.geometry.euclidean.threed.Vector3D receivedByClient = resp_data.getParcelable("result", externTypes.externTypes_android_messenger.MyVector3DParcelable.class).getMyVector3D();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onincrementArrayRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, CounterMessageType.RPC_IncrementArrayReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] testvec = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[1];
		testvec[0] = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
		data.putParcelableArray("vec", externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(testvec));
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] returnedValue = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[1];
		returnedValue[0] = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);


        when(backendServiceMock.incrementArray( any(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[].class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).incrementArray( any(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[].class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(CounterMessageType.RPC_IncrementArrayResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] receivedByClient =  externTypes.externTypes_android_messenger.MyVector3DParcelable.unwrapArray((externTypes.externTypes_android_messenger.MyVector3DParcelable[])resp_data.getParcelableArray("result", externTypes.externTypes_android_messenger.MyVector3DParcelable.class));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void ondecrementRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, CounterMessageType.RPC_DecrementReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        customTypes.customTypes_api.Vector3D testvec = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
		data.putParcelable("vec", new customTypes.customTypes_android_messenger.Vector3DParcelable(testvec));
        customTypes.customTypes_api.Vector3D returnedValue = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();


        when(backendServiceMock.decrement( any(customTypes.customTypes_api.Vector3D.class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).decrement( any(customTypes.customTypes_api.Vector3D.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(CounterMessageType.RPC_DecrementResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
		customTypes.customTypes_api.Vector3D receivedByClient = resp_data.getParcelable("result", customTypes.customTypes_android_messenger.Vector3DParcelable.class).getVector3D();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void ondecrementArrayRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, CounterMessageType.RPC_DecrementArrayReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        customTypes.customTypes_api.Vector3D[] testvec = new customTypes.customTypes_api.Vector3D[1];
        testvec[0] = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
		data.putParcelableArray("vec", customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(testvec));
        customTypes.customTypes_api.Vector3D[] returnedValue = new customTypes.customTypes_api.Vector3D[1];
        returnedValue[0] = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();


        when(backendServiceMock.decrementArray( any(customTypes.customTypes_api.Vector3D[].class))).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).decrementArray( any(customTypes.customTypes_api.Vector3D[].class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(CounterMessageType.RPC_DecrementArrayResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
        customTypes.customTypes_api.Vector3D[] receivedByClient =  customTypes.customTypes_android_messenger.Vector3DParcelable.unwrapArray((customTypes.customTypes_android_messenger.Vector3DParcelable[])resp_data.getParcelableArray("result", customTypes.customTypes_android_messenger.Vector3DParcelable.class));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
