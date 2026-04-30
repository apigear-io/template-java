// Copyright Epic Games, Inc. All Rights Reserved.

package tbEnum.tbEnum_android_service;

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
import tbEnum.tbEnum_android_service.EnumInterfaceServiceAdapter;
import tbEnum.tbEnum_android_service.IEnumInterfaceServiceProvider;

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
import tbEnum.tbEnum_android_service.IEnumInterfaceServiceProvider;
import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_api.AbstractEnumInterface;
import tbEnum.tbEnum_android_messenger.EnumInterfaceMessageType;


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

interface IEnumInterfaceMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class EnumInterfaceServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private EnumInterfaceServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private IEnumInterfaceEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private IEnumInterface backendServiceMock = mock(IEnumInterface.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private IEnumInterfaceServiceProvider ServiceProvider = mock(IEnumInterfaceServiceProvider.class);
    private IEnumInterfaceMessageGetter clientMessagesStorage = mock(IEnumInterfaceMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, EnumInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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

    Handler createClientHandlerMock(IEnumInterfaceMessageGetter messageGetterMock)
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
        Enum0 initprop0 = Enum0.Value1;
        when(backendServiceMock.getProp0()).thenReturn(initprop0);
        Enum1 initprop1 = Enum1.Value2;
        when(backendServiceMock.getProp1()).thenReturn(initprop1);
        Enum2 initprop2 = Enum2.Value1;
        when(backendServiceMock.getProp2()).thenReturn(initprop2);
        Enum3 initprop3 = Enum3.Value2;
        when(backendServiceMock.getProp3()).thenReturn(initprop3);


        Message registerMsg = Message.obtain(null, EnumInterfaceMessageType.REGISTER_CLIENT.ordinal());
        registerMsg.getData().putString("connectionID", id);
        registerMsg.replyTo = messenger;
    
        try {
            mServiceMessenger.send(registerMsg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock, times(1)).getProp0();
        inOrderBackendService.verify(backendServiceMock, times(1)).getProp1();
        inOrderBackendService.verify(backendServiceMock, times(1)).getProp2();
        inOrderBackendService.verify(backendServiceMock, times(1)).getProp3();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        
			Enum0 receivedprop0 = data.getParcelable("prop0", Enum0Parcelable.class).getEnum0();
        
			Enum1 receivedprop1 = data.getParcelable("prop1", Enum1Parcelable.class).getEnum1();
        
			Enum2 receivedprop2 = data.getParcelable("prop2", Enum2Parcelable.class).getEnum2();
        
			Enum3 receivedprop3 = data.getParcelable("prop3", Enum3Parcelable.class).getEnum3();
        
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(Enum0Parcelable.class.getClassLoader());
        assertEquals(receivedprop0, initprop0);
        assertEquals(receivedprop1, initprop1);
        assertEquals(receivedprop2, initprop2);
        assertEquals(receivedprop3, initprop3);

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

        testedServiceAdapterIntent = new Intent(mMockContext, EnumInterfaceServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(EnumInterfaceServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(ServiceProvider.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(ServiceProvider);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<IEnumInterfaceEventListener> eventListnerCaptor = ArgumentCaptor.forClass(IEnumInterfaceEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
    @Test
    public void onReceiveprop0PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, EnumInterfaceMessageType.PROP_Prop0.getValue());
        Bundle data = new Bundle();
		Enum0 testprop0 = Enum0.Value1;
		data.putParcelable("prop0", new Enum0Parcelable(testprop0));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setProp0(testprop0);
	    
    }

    @Test
     public void whenNotifiedprop0()
    {
		Enum0 testprop0 = Enum0.Value1;

        testedAdapterAsEventListener.onProp0Changed(testprop0);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.SET_Prop0.getValue(), response.what);
        Bundle data = response.getData();

        
			Enum0 receivedprop0 = data.getParcelable("prop0", Enum0Parcelable.class).getEnum0();

        assertEquals(receivedprop0, testprop0);
    }
    @Test
    public void onReceiveprop1PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, EnumInterfaceMessageType.PROP_Prop1.getValue());
        Bundle data = new Bundle();
		Enum1 testprop1 = Enum1.Value2;
		data.putParcelable("prop1", new Enum1Parcelable(testprop1));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setProp1(testprop1);
	    
    }

    @Test
     public void whenNotifiedprop1()
    {
		Enum1 testprop1 = Enum1.Value2;

        testedAdapterAsEventListener.onProp1Changed(testprop1);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.SET_Prop1.getValue(), response.what);
        Bundle data = response.getData();

        
			Enum1 receivedprop1 = data.getParcelable("prop1", Enum1Parcelable.class).getEnum1();

        assertEquals(receivedprop1, testprop1);
    }
    @Test
    public void onReceiveprop2PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, EnumInterfaceMessageType.PROP_Prop2.getValue());
        Bundle data = new Bundle();
		Enum2 testprop2 = Enum2.Value1;
		data.putParcelable("prop2", new Enum2Parcelable(testprop2));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setProp2(testprop2);
	    
    }

    @Test
     public void whenNotifiedprop2()
    {
		Enum2 testprop2 = Enum2.Value1;

        testedAdapterAsEventListener.onProp2Changed(testprop2);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.SET_Prop2.getValue(), response.what);
        Bundle data = response.getData();

        
			Enum2 receivedprop2 = data.getParcelable("prop2", Enum2Parcelable.class).getEnum2();

        assertEquals(receivedprop2, testprop2);
    }
    @Test
    public void onReceiveprop3PropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, EnumInterfaceMessageType.PROP_Prop3.getValue());
        Bundle data = new Bundle();
		Enum3 testprop3 = Enum3.Value2;
		data.putParcelable("prop3", new Enum3Parcelable(testprop3));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setProp3(testprop3);
	    
    }

    @Test
     public void whenNotifiedprop3()
    {
		Enum3 testprop3 = Enum3.Value2;

        testedAdapterAsEventListener.onProp3Changed(testprop3);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.SET_Prop3.getValue(), response.what);
        Bundle data = response.getData();

        
			Enum3 receivedprop3 = data.getParcelable("prop3", Enum3Parcelable.class).getEnum3();

        assertEquals(receivedprop3, testprop3);
    }
    @Test
    public void whenNotifiedsig0()
    {
		Enum0 testparam0 = Enum0.Value1;

        testedAdapterAsEventListener.onSig0(testparam0);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.SIG_Sig0.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(Enum0Parcelable.class.getClassLoader());
        
			Enum0 receivedparam0 = data.getParcelable("param0", Enum0Parcelable.class).getEnum0();
        assertEquals(receivedparam0, testparam0);
}
    @Test
    public void whenNotifiedsig1()
    {
		Enum1 testparam1 = Enum1.Value2;

        testedAdapterAsEventListener.onSig1(testparam1);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.SIG_Sig1.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(Enum1Parcelable.class.getClassLoader());
        
			Enum1 receivedparam1 = data.getParcelable("param1", Enum1Parcelable.class).getEnum1();
        assertEquals(receivedparam1, testparam1);
}
    @Test
    public void whenNotifiedsig2()
    {
		Enum2 testparam2 = Enum2.Value1;

        testedAdapterAsEventListener.onSig2(testparam2);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.SIG_Sig2.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(Enum2Parcelable.class.getClassLoader());
        
			Enum2 receivedparam2 = data.getParcelable("param2", Enum2Parcelable.class).getEnum2();
        assertEquals(receivedparam2, testparam2);
}
    @Test
    public void whenNotifiedsig3()
    {
		Enum3 testparam3 = Enum3.Value2;

        testedAdapterAsEventListener.onSig3(testparam3);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.SIG_Sig3.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(Enum3Parcelable.class.getClassLoader());
        
			Enum3 receivedparam3 = data.getParcelable("param3", Enum3Parcelable.class).getEnum3();
        assertEquals(receivedparam3, testparam3);
}

    @Test
    public void onfunc0Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, EnumInterfaceMessageType.RPC_Func0Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		Enum0 testparam0 = Enum0.Value1;
		data.putParcelable("param0", new Enum0Parcelable(testparam0));
        Enum0 returnedValue = Enum0.Value1;


        when(backendServiceMock.func0(testparam0)).thenReturn(returnedValue);

        msg.setData(data);
        msg.replyTo = clientReplyMessenger;
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).func0(testparam0);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.RPC_Func0Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(Enum0Parcelable.class.getClassLoader());
		Enum0 receivedByClient = resp_data.getParcelable("result", Enum0Parcelable.class).getEnum0();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onfunc1Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, EnumInterfaceMessageType.RPC_Func1Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		Enum1 testparam1 = Enum1.Value2;
		data.putParcelable("param1", new Enum1Parcelable(testparam1));
        Enum1 returnedValue = Enum1.Value2;


        when(backendServiceMock.func1(testparam1)).thenReturn(returnedValue);

        msg.setData(data);
        msg.replyTo = clientReplyMessenger;
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).func1(testparam1);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.RPC_Func1Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(Enum1Parcelable.class.getClassLoader());
		Enum1 receivedByClient = resp_data.getParcelable("result", Enum1Parcelable.class).getEnum1();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onfunc2Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, EnumInterfaceMessageType.RPC_Func2Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		Enum2 testparam2 = Enum2.Value1;
		data.putParcelable("param2", new Enum2Parcelable(testparam2));
        Enum2 returnedValue = Enum2.Value1;


        when(backendServiceMock.func2(testparam2)).thenReturn(returnedValue);

        msg.setData(data);
        msg.replyTo = clientReplyMessenger;
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).func2(testparam2);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.RPC_Func2Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(Enum2Parcelable.class.getClassLoader());
		Enum2 receivedByClient = resp_data.getParcelable("result", Enum2Parcelable.class).getEnum2();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onfunc3Request() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, EnumInterfaceMessageType.RPC_Func3Req.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
		Enum3 testparam3 = Enum3.Value2;
		data.putParcelable("param3", new Enum3Parcelable(testparam3));
        Enum3 returnedValue = Enum3.Value2;


        when(backendServiceMock.func3(testparam3)).thenReturn(returnedValue);

        msg.setData(data);
        msg.replyTo = clientReplyMessenger;
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).func3(testparam3);

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(EnumInterfaceMessageType.RPC_Func3Resp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(Enum3Parcelable.class.getClassLoader());
		Enum3 receivedByClient = resp_data.getParcelable("result", Enum3Parcelable.class).getEnum3();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
