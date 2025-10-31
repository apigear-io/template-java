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
import tbSimple.tbSimple_android_service.SimpleArrayInterfaceServiceAdapter;

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
import tbSimple.tbSimple_android_service.ISimpleArrayInterfaceServiceFactory;
import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_api.AbstractSimpleArrayInterface;
import tbSimple.tbSimple_android_messenger.SimpleArrayInterfaceMessageType;


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

interface ISimpleArrayInterfaceMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class SimpleArrayInterfaceServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private SimpleArrayInterfaceServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private ISimpleArrayInterfaceEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private ISimpleArrayInterface backendServiceMock = mock(ISimpleArrayInterface.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private ISimpleArrayInterfaceServiceFactory serviceFactory = mock(ISimpleArrayInterfaceServiceFactory.class);
    private ISimpleArrayInterfaceMessageGetter clientMessagesStorage = mock(ISimpleArrayInterfaceMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, SimpleArrayInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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

    Handler createClientHandlerMock(ISimpleArrayInterfaceMessageGetter messageGetterMock)
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
        boolean init_elementpropBool = true;
        // todo fill if is struct
        boolean[] initpropBool = new boolean[]{ init_elementpropBool } ;
        when(backendServiceMock.getPropBool()).thenReturn(initpropBool);
        int init_elementpropInt = 1;
        // todo fill if is struct
        int[] initpropInt = new int[]{ init_elementpropInt } ;
        when(backendServiceMock.getPropInt()).thenReturn(initpropInt);
        int init_elementpropInt32 = 1;
        // todo fill if is struct
        int[] initpropInt32 = new int[]{ init_elementpropInt32 } ;
        when(backendServiceMock.getPropInt32()).thenReturn(initpropInt32);
        long init_elementpropInt64 = 1L;
        // todo fill if is struct
        long[] initpropInt64 = new long[]{ init_elementpropInt64 } ;
        when(backendServiceMock.getPropInt64()).thenReturn(initpropInt64);
        float init_elementpropFloat = 1.0f;
        // todo fill if is struct
        float[] initpropFloat = new float[]{ init_elementpropFloat } ;
        when(backendServiceMock.getPropFloat()).thenReturn(initpropFloat);
        float init_elementpropFloat32 = 1.0f;
        // todo fill if is struct
        float[] initpropFloat32 = new float[]{ init_elementpropFloat32 } ;
        when(backendServiceMock.getPropFloat32()).thenReturn(initpropFloat32);
        double init_elementpropFloat64 = 1.0;
        // todo fill if is struct
        double[] initpropFloat64 = new double[]{ init_elementpropFloat64 } ;
        when(backendServiceMock.getPropFloat64()).thenReturn(initpropFloat64);
        String init_elementpropString = new String("xyz");
        // todo fill if is struct
        String[] initpropString = new String[]{ init_elementpropString } ;
        when(backendServiceMock.getPropString()).thenReturn(initpropString);
		String initpropReadOnlyString = new String("xyz");
        when(backendServiceMock.getPropReadOnlyString()).thenReturn(initpropReadOnlyString);


        Message registerMsg = Message.obtain(null, SimpleArrayInterfaceMessageType.REGISTER_CLIENT.ordinal());
        registerMsg.getData().putString("connectionID", id);
        registerMsg.replyTo = messenger;
    
        try {
            mServiceMessenger.send(registerMsg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropBool();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropInt();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropInt32();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropInt64();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropFloat();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropFloat32();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropFloat64();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropString();
        inOrderBackendService.verify(backendServiceMock, times(1)).getPropReadOnlyString();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        
			boolean[] receivedpropBool = data.getBooleanArray("propBool");
        
			int[] receivedpropInt = data.getIntArray("propInt");
        
			int[] receivedpropInt32 = data.getIntArray("propInt32");
        
			long[] receivedpropInt64 = data.getLongArray("propInt64");
        
			float[] receivedpropFloat = data.getFloatArray("propFloat");
        
			float[] receivedpropFloat32 = data.getFloatArray("propFloat32");
        
			double[] receivedpropFloat64 = data.getDoubleArray("propFloat64");
        
			String[] receivedpropString = data.getStringArray("propString");
        
			String receivedpropReadOnlyString = data.getString("propReadOnlyString", new String());
        
        assertEquals(receivedpropBool, initpropBool);
        assertEquals(receivedpropInt, initpropInt);
        assertEquals(receivedpropInt32, initpropInt32);
        assertEquals(receivedpropInt64, initpropInt64);
        assertEquals(receivedpropFloat, initpropFloat);
        assertEquals(receivedpropFloat32, initpropFloat32);
        assertEquals(receivedpropFloat64, initpropFloat64);
        assertEquals(receivedpropString, initpropString);
        assertEquals(receivedpropReadOnlyString, initpropReadOnlyString);

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

        testedServiceAdapterIntent = new Intent(mMockContext, SimpleArrayInterfaceServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(SimpleArrayInterfaceServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(serviceFactory.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(serviceFactory);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<ISimpleArrayInterfaceEventListener> eventListnerCaptor = ArgumentCaptor.forClass(ISimpleArrayInterfaceEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
    @Test
    public void onReceivepropBoolPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.PROP_PropBool.getValue());
        Bundle data = new Bundle();
        boolean[] testpropBool = new boolean[1];
        testpropBool[0] = true;
		data.putBooleanArray("propBool", testpropBool);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropBool(testpropBool);
	    
    }

    @Test
     public void whenNotifiedpropBool()
    {
        boolean[] testpropBool = new boolean[1];
        testpropBool[0] = true;

        testedAdapterAsEventListener.onPropBoolChanged(testpropBool);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SET_PropBool.getValue(), response.what);
        Bundle data = response.getData();

        
			boolean[] receivedpropBool = data.getBooleanArray("propBool");

        assertEquals(receivedpropBool, testpropBool);
    }
    @Test
    public void onReceivepropIntPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.PROP_PropInt.getValue());
        Bundle data = new Bundle();
        int[] testpropInt = new int[1];
        testpropInt[0] = 1;
		data.putIntArray("propInt", testpropInt);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropInt(testpropInt);
	    
    }

    @Test
     public void whenNotifiedpropInt()
    {
        int[] testpropInt = new int[1];
        testpropInt[0] = 1;

        testedAdapterAsEventListener.onPropIntChanged(testpropInt);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SET_PropInt.getValue(), response.what);
        Bundle data = response.getData();

        
			int[] receivedpropInt = data.getIntArray("propInt");

        assertEquals(receivedpropInt, testpropInt);
    }
    @Test
    public void onReceivepropInt32PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.PROP_PropInt32.getValue());
        Bundle data = new Bundle();
        int[] testpropInt32 = new int[1];
        testpropInt32[0] = 1;
		data.putIntArray("propInt32", testpropInt32);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropInt32(testpropInt32);
	    
    }

    @Test
     public void whenNotifiedpropInt32()
    {
        int[] testpropInt32 = new int[1];
        testpropInt32[0] = 1;

        testedAdapterAsEventListener.onPropInt32Changed(testpropInt32);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SET_PropInt32.getValue(), response.what);
        Bundle data = response.getData();

        
			int[] receivedpropInt32 = data.getIntArray("propInt32");

        assertEquals(receivedpropInt32, testpropInt32);
    }
    @Test
    public void onReceivepropInt64PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.PROP_PropInt64.getValue());
        Bundle data = new Bundle();
        long[] testpropInt64 = new long[1];
        testpropInt64[0] = 1L;
		data.putLongArray("propInt64", testpropInt64);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropInt64(testpropInt64);
	    
    }

    @Test
     public void whenNotifiedpropInt64()
    {
        long[] testpropInt64 = new long[1];
        testpropInt64[0] = 1L;

        testedAdapterAsEventListener.onPropInt64Changed(testpropInt64);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SET_PropInt64.getValue(), response.what);
        Bundle data = response.getData();

        
			long[] receivedpropInt64 = data.getLongArray("propInt64");

        assertEquals(receivedpropInt64, testpropInt64);
    }
    @Test
    public void onReceivepropFloatPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.PROP_PropFloat.getValue());
        Bundle data = new Bundle();
        float[] testpropFloat = new float[1];
        testpropFloat[0] = 1.0f;
		data.putFloatArray("propFloat", testpropFloat);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropFloat(testpropFloat);
	    
    }

    @Test
     public void whenNotifiedpropFloat()
    {
        float[] testpropFloat = new float[1];
        testpropFloat[0] = 1.0f;

        testedAdapterAsEventListener.onPropFloatChanged(testpropFloat);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SET_PropFloat.getValue(), response.what);
        Bundle data = response.getData();

        
			float[] receivedpropFloat = data.getFloatArray("propFloat");

        assertEquals(receivedpropFloat, testpropFloat);
    }
    @Test
    public void onReceivepropFloat32PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.PROP_PropFloat32.getValue());
        Bundle data = new Bundle();
        float[] testpropFloat32 = new float[1];
        testpropFloat32[0] = 1.0f;
		data.putFloatArray("propFloat32", testpropFloat32);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropFloat32(testpropFloat32);
	    
    }

    @Test
     public void whenNotifiedpropFloat32()
    {
        float[] testpropFloat32 = new float[1];
        testpropFloat32[0] = 1.0f;

        testedAdapterAsEventListener.onPropFloat32Changed(testpropFloat32);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SET_PropFloat32.getValue(), response.what);
        Bundle data = response.getData();

        
			float[] receivedpropFloat32 = data.getFloatArray("propFloat32");

        assertEquals(receivedpropFloat32, testpropFloat32);
    }
    @Test
    public void onReceivepropFloat64PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.PROP_PropFloat64.getValue());
        Bundle data = new Bundle();
        double[] testpropFloat64 = new double[1];
        testpropFloat64[0] = 1.0;
		data.putDoubleArray("propFloat64", testpropFloat64);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropFloat64(testpropFloat64);
	    
    }

    @Test
     public void whenNotifiedpropFloat64()
    {
        double[] testpropFloat64 = new double[1];
        testpropFloat64[0] = 1.0;

        testedAdapterAsEventListener.onPropFloat64Changed(testpropFloat64);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SET_PropFloat64.getValue(), response.what);
        Bundle data = response.getData();

        
			double[] receivedpropFloat64 = data.getDoubleArray("propFloat64");

        assertEquals(receivedpropFloat64, testpropFloat64);
    }
    @Test
    public void onReceivepropStringPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.PROP_PropString.getValue());
        Bundle data = new Bundle();
        String[] testpropString = new String[1];
        testpropString[0] = new String("xyz");
		data.putStringArray("propString", testpropString);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setPropString(testpropString);
	    
    }

    @Test
     public void whenNotifiedpropString()
    {
        String[] testpropString = new String[1];
        testpropString[0] = new String("xyz");

        testedAdapterAsEventListener.onPropStringChanged(testpropString);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SET_PropString.getValue(), response.what);
        Bundle data = response.getData();

        
			String[] receivedpropString = data.getStringArray("propString");

        assertEquals(receivedpropString, testpropString);
    }

    @Test
     public void whenNotifiedpropReadOnlyString()
    {
		String testpropReadOnlyString = new String("xyz");

        testedAdapterAsEventListener.onPropReadOnlyStringChanged(testpropReadOnlyString);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SET_PropReadOnlyString.getValue(), response.what);
        Bundle data = response.getData();

        
			String receivedpropReadOnlyString = data.getString("propReadOnlyString", new String());

        assertEquals(receivedpropReadOnlyString, testpropReadOnlyString);
    }
    @Test
    public void whenNotifiedsigBool()
    {
        boolean[] testparamBool = new boolean[1];
        testparamBool[0] = true;

        testedAdapterAsEventListener.onSigBool(testparamBool);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SIG_SigBool.getValue(), response.what);
        Bundle data = response.getData();
        
        
			boolean[] receivedparamBool = data.getBooleanArray("paramBool");
        assertEquals(receivedparamBool, testparamBool);
}
    @Test
    public void whenNotifiedsigInt()
    {
        int[] testparamInt = new int[1];
        testparamInt[0] = 1;

        testedAdapterAsEventListener.onSigInt(testparamInt);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SIG_SigInt.getValue(), response.what);
        Bundle data = response.getData();
        
        
			int[] receivedparamInt = data.getIntArray("paramInt");
        assertEquals(receivedparamInt, testparamInt);
}
    @Test
    public void whenNotifiedsigInt32()
    {
        int[] testparamInt32 = new int[1];
        testparamInt32[0] = 1;

        testedAdapterAsEventListener.onSigInt32(testparamInt32);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SIG_SigInt32.getValue(), response.what);
        Bundle data = response.getData();
        
        
			int[] receivedparamInt32 = data.getIntArray("paramInt32");
        assertEquals(receivedparamInt32, testparamInt32);
}
    @Test
    public void whenNotifiedsigInt64()
    {
        long[] testparamInt64 = new long[1];
        testparamInt64[0] = 1L;

        testedAdapterAsEventListener.onSigInt64(testparamInt64);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SIG_SigInt64.getValue(), response.what);
        Bundle data = response.getData();
        
        
			long[] receivedparamInt64 = data.getLongArray("paramInt64");
        assertEquals(receivedparamInt64, testparamInt64);
}
    @Test
    public void whenNotifiedsigFloat()
    {
        float[] testparamFloat = new float[1];
        testparamFloat[0] = 1.0f;

        testedAdapterAsEventListener.onSigFloat(testparamFloat);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SIG_SigFloat.getValue(), response.what);
        Bundle data = response.getData();
        
        
			float[] receivedparamFloat = data.getFloatArray("paramFloat");
        assertEquals(receivedparamFloat, testparamFloat);
}
    @Test
    public void whenNotifiedsigFloat32()
    {
        float[] testparamFloa32 = new float[1];
        testparamFloa32[0] = 1.0f;

        testedAdapterAsEventListener.onSigFloat32(testparamFloa32);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SIG_SigFloat32.getValue(), response.what);
        Bundle data = response.getData();
        
        
			float[] receivedparamFloa32 = data.getFloatArray("paramFloa32");
        assertEquals(receivedparamFloa32, testparamFloa32);
}
    @Test
    public void whenNotifiedsigFloat64()
    {
        double[] testparamFloat64 = new double[1];
        testparamFloat64[0] = 1.0;

        testedAdapterAsEventListener.onSigFloat64(testparamFloat64);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SIG_SigFloat64.getValue(), response.what);
        Bundle data = response.getData();
        
        
			double[] receivedparamFloat64 = data.getDoubleArray("paramFloat64");
        assertEquals(receivedparamFloat64, testparamFloat64);
}
    @Test
    public void whenNotifiedsigString()
    {
        String[] testparamString = new String[1];
        testparamString[0] = new String("xyz");

        testedAdapterAsEventListener.onSigString(testparamString);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.SIG_SigString.getValue(), response.what);
        Bundle data = response.getData();
        
        
			String[] receivedparamString = data.getStringArray("paramString");
        assertEquals(receivedparamString, testparamString);
}


    public void onfuncBoolRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncBoolReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        boolean[] testparamBool = new boolean[1];
        testparamBool[0] = true;
		data.putBooleanArray("paramBool", testparamBool);
        boolean[] returnedValue = new boolean[1];
        returnedValue[0] = true;


        when(backendServiceMock.funcBool(testparamBool)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcBool(testparamBool);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncBoolResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		boolean[] receivedByClient = resp_data.getBooleanArray("result");

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncIntRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncIntReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        int[] testparamInt = new int[1];
        testparamInt[0] = 1;
		data.putIntArray("paramInt", testparamInt);
        int[] returnedValue = new int[1];
        returnedValue[0] = 1;


        when(backendServiceMock.funcInt(testparamInt)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcInt(testparamInt);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncIntResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		int[] receivedByClient = resp_data.getIntArray("result");

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncInt32Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncInt32Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        int[] testparamInt32 = new int[1];
        testparamInt32[0] = 1;
		data.putIntArray("paramInt32", testparamInt32);
        int[] returnedValue = new int[1];
        returnedValue[0] = 1;


        when(backendServiceMock.funcInt32(testparamInt32)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcInt32(testparamInt32);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncInt32Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		int[] receivedByClient = resp_data.getIntArray("result");

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncInt64Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncInt64Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        long[] testparamInt64 = new long[1];
        testparamInt64[0] = 1L;
		data.putLongArray("paramInt64", testparamInt64);
        long[] returnedValue = new long[1];
        returnedValue[0] = 1L;


        when(backendServiceMock.funcInt64(testparamInt64)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcInt64(testparamInt64);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncInt64Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		long[] receivedByClient = resp_data.getLongArray("result");

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncFloatRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncFloatReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        float[] testparamFloat = new float[1];
        testparamFloat[0] = 1.0f;
		data.putFloatArray("paramFloat", testparamFloat);
        float[] returnedValue = new float[1];
        returnedValue[0] = 1.0f;


        when(backendServiceMock.funcFloat(testparamFloat)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcFloat(testparamFloat);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncFloatResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		float[] receivedByClient = resp_data.getFloatArray("result");

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncFloat32Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncFloat32Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        float[] testparamFloat32 = new float[1];
        testparamFloat32[0] = 1.0f;
		data.putFloatArray("paramFloat32", testparamFloat32);
        float[] returnedValue = new float[1];
        returnedValue[0] = 1.0f;


        when(backendServiceMock.funcFloat32(testparamFloat32)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcFloat32(testparamFloat32);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncFloat32Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		float[] receivedByClient = resp_data.getFloatArray("result");

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncFloat64Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncFloat64Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        double[] testparamFloat = new double[1];
        testparamFloat[0] = 1.0;
		data.putDoubleArray("paramFloat", testparamFloat);
        double[] returnedValue = new double[1];
        returnedValue[0] = 1.0;


        when(backendServiceMock.funcFloat64(testparamFloat)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcFloat64(testparamFloat);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncFloat64Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		double[] receivedByClient = resp_data.getDoubleArray("result");

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }


    public void onfuncStringRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.RPC_FuncStringReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        String[] testparamString = new String[1];
        testparamString[0] = new String("xyz");
		data.putStringArray("paramString", testparamString);
        String[] returnedValue = new String[1];
        returnedValue[0] = new String("xyz");


        when(backendServiceMock.funcString(testparamString)).thenReturn(returnedValue);

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).funcString(testparamString);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(SimpleArrayInterfaceMessageType.RPC_FuncStringResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		String[] receivedByClient = resp_data.getStringArray("result");

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
