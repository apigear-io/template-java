// Copyright Epic Games, Inc. All Rights Reserved.

package tbRefIfaces.tbRefIfaces_android_service;

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
import tbRefIfaces.tbRefIfaces_android_service.ParentIfServiceAdapter;
import tbRefIfaces.tbRefIfaces_android_service.IParentIfServiceProvider;

//import message type and parcelabe types
import tbRefIfaces.tbRefIfaces_api.TbRefIfacesTestHelper;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_android_messenger.SimpleLocalIfParcelable;
import tbRefIfaces.tbRefIfaces_impl.SimpleLocalIfService;
import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_android_messenger.ParentIfParcelable;
import tbRefIfaces.tbRefIfaces_impl.ParentIfService;


import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;
import tbRefIfaces.tbRefIfaces_android_service.IParentIfServiceProvider;
import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_api.AbstractParentIf;
import tbRefIfaces.tbRefIfaces_android_messenger.ParentIfMessageType;


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

interface IParentIfMessageGetter
{
    public void getMessage(Message msg);
}

@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ParentIfServiceAdapterTest 
{

    @Mock
    private Context mMockContext;
   
    private ParentIfServiceAdapter testedServiceAdapter;
    private Intent testedServiceAdapterIntent;
    private IParentIfEventListener testedAdapterAsEventListener;
    private Messenger mServiceMessenger;
    private IParentIf backendServiceMock = mock(IParentIf.class);
    InOrder inOrderBackendService;
    InOrder inOrderClientMessagesHandler;

    private Handler clientReplyHandler ;
    private Messenger clientReplyMessenger;
    private String mTestConnectionID1 = "MyTestClient";
   
    private IParentIfServiceProvider ServiceProvider = mock(IParentIfServiceProvider.class);
    private IParentIfMessageGetter clientMessagesStorage = mock(IParentIfMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    @After
    public void tearDown() {

        Message unregisterMsg = Message.obtain(null, ParentIfMessageType.UNREGISTER_CLIENT.ordinal());
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

    Handler createClientHandlerMock(IParentIfMessageGetter messageGetterMock)
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
        ISimpleLocalIf initlocalIf = new SimpleLocalIfService();
        //TODO fill fields
        when(backendServiceMock.getLocalIf()).thenReturn(initlocalIf);
        ISimpleLocalIf init_elementlocalIfList = new SimpleLocalIfService();
        // todo fill if is struct
        ISimpleLocalIf[] initlocalIfList = new ISimpleLocalIf[]{ init_elementlocalIfList } ;
        when(backendServiceMock.getLocalIfList()).thenReturn(initlocalIfList);
        tbIfaceimport.tbIfaceimport_api.IEmptyIf initimportedIf = new tbIfaceimport.tbIfaceimport_impl.EmptyIfService();
        //TODO fill fields
        when(backendServiceMock.getImportedIf()).thenReturn(initimportedIf);
        tbIfaceimport.tbIfaceimport_api.IEmptyIf init_elementimportedIfList = new tbIfaceimport.tbIfaceimport_impl.EmptyIfService();
        // todo fill if is struct
        tbIfaceimport.tbIfaceimport_api.IEmptyIf[] initimportedIfList = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[]{ init_elementimportedIfList } ;
        when(backendServiceMock.getImportedIfList()).thenReturn(initimportedIfList);


        Message registerMsg = Message.obtain(null, ParentIfMessageType.REGISTER_CLIENT.ordinal());
        registerMsg.getData().putString("connectionID", id);
        registerMsg.replyTo = messenger;
    
        try {
            mServiceMessenger.send(registerMsg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock, times(1)).getLocalIf();
        inOrderBackendService.verify(backendServiceMock, times(1)).getLocalIfList();
        inOrderBackendService.verify(backendServiceMock, times(1)).getImportedIf();
        inOrderBackendService.verify(backendServiceMock, times(1)).getImportedIfList();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.INIT.getValue(), response.what);
        Bundle data = response.getData();
        
			ISimpleLocalIf receivedlocalIf = data.getParcelable("localIf", SimpleLocalIfParcelable.class).getSimpleLocalIf();
        
            ISimpleLocalIf[] receivedlocalIfList =  SimpleLocalIfParcelable.unwrapArray((SimpleLocalIfParcelable[])data.getParcelableArray("localIfList", SimpleLocalIfParcelable.class));
        
			tbIfaceimport.tbIfaceimport_api.IEmptyIf receivedimportedIf = data.getParcelable("importedIf", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class).getEmptyIf();
        
            tbIfaceimport.tbIfaceimport_api.IEmptyIf[] receivedimportedIfList =  tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray((tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[])data.getParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class));
        
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
        // assertEquals(receivedlocalIf, initlocalIf);
        // assertEquals(receivedlocalIfList, initlocalIfList);
        // assertEquals(receivedimportedIf, initimportedIf);
        // assertEquals(receivedimportedIfList, initimportedIfList);

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

        testedServiceAdapterIntent = new Intent(mMockContext, ParentIfServiceAdapter.class);

        // Start the service
        testedServiceAdapter = Robolectric.buildService(ParentIfServiceAdapter.class, testedServiceAdapterIntent)
                                       .create().get();
        // Assert expected state
        assertNotNull(testedServiceAdapter);

        // Get Messenger instance from service
        IBinder binder = (IBinder)testedServiceAdapter.onBind(new Intent()) ;
        mServiceMessenger = new Messenger(binder);

        when(ServiceProvider.getServiceInstance()).thenReturn(backendServiceMock);
        testedServiceAdapter.setService(ServiceProvider);
		// service adapter should pass its member to backend to get notifications on changes.
        ArgumentCaptor<IParentIfEventListener> eventListnerCaptor = ArgumentCaptor.forClass(IParentIfEventListener.class);
        inOrderBackendService.verify(backendServiceMock, times(1)).addEventListener(eventListnerCaptor.capture());
        testedAdapterAsEventListener = eventListnerCaptor.getValue();

        // Register a fake client (the message handler form its service-connection)
        // All emitted signals and property changes are forwarded to it.
        registerFakeActivityClient(clientReplyMessenger, mTestConnectionID1);
    }
    @Test
    public void onReceivelocalIfPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ParentIfMessageType.PROP_LocalIf.getValue());
        Bundle data = new Bundle();
        ISimpleLocalIf testlocalIf = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new SimpleLocalIfService());
		data.putParcelable("localIf", new SimpleLocalIfParcelable(testlocalIf));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setLocalIf( any(ISimpleLocalIf.class));
	    
    }

    @Test
     public void whenNotifiedlocalIf()
    {
        ISimpleLocalIf testlocalIf = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new SimpleLocalIfService());

        testedAdapterAsEventListener.onLocalIfChanged(testlocalIf);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.SET_LocalIf.getValue(), response.what);
        Bundle data = response.getData();

        
			ISimpleLocalIf receivedlocalIf = data.getParcelable("localIf", SimpleLocalIfParcelable.class).getSimpleLocalIf();

        assertEquals(receivedlocalIf, testlocalIf);
    }
    @Test
    public void onReceivelocalIfListPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ParentIfMessageType.PROP_LocalIfList.getValue());
        Bundle data = new Bundle();
        ISimpleLocalIf[] testlocalIfList = new ISimpleLocalIf[1];
        testlocalIfList[0] = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new SimpleLocalIfService());
		data.putParcelableArray("localIfList", SimpleLocalIfParcelable.wrapArray(testlocalIfList));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setLocalIfList( any(ISimpleLocalIf[].class));
	    
    }

    @Test
     public void whenNotifiedlocalIfList()
    {
        ISimpleLocalIf[] testlocalIfList = new ISimpleLocalIf[1];
        testlocalIfList[0] = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new SimpleLocalIfService());

        testedAdapterAsEventListener.onLocalIfListChanged(testlocalIfList);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.SET_LocalIfList.getValue(), response.what);
        Bundle data = response.getData();

        
            ISimpleLocalIf[] receivedlocalIfList =  SimpleLocalIfParcelable.unwrapArray((SimpleLocalIfParcelable[])data.getParcelableArray("localIfList", SimpleLocalIfParcelable.class));

        assertEquals(receivedlocalIfList, testlocalIfList);
    }
    @Test
    public void onReceiveimportedIfPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ParentIfMessageType.PROP_ImportedIf.getValue());
        Bundle data = new Bundle();
        tbIfaceimport.tbIfaceimport_api.IEmptyIf testimportedIf = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_impl.EmptyIfService());
		data.putParcelable("importedIf", new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(testimportedIf));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setImportedIf( any(tbIfaceimport.tbIfaceimport_api.IEmptyIf.class));
	    
    }

    @Test
     public void whenNotifiedimportedIf()
    {
        tbIfaceimport.tbIfaceimport_api.IEmptyIf testimportedIf = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_impl.EmptyIfService());

        testedAdapterAsEventListener.onImportedIfChanged(testimportedIf);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.SET_ImportedIf.getValue(), response.what);
        Bundle data = response.getData();

        
			tbIfaceimport.tbIfaceimport_api.IEmptyIf receivedimportedIf = data.getParcelable("importedIf", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class).getEmptyIf();

        assertEquals(receivedimportedIf, testimportedIf);
    }
    @Test
    public void onReceiveimportedIfListPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ParentIfMessageType.PROP_ImportedIfList.getValue());
        Bundle data = new Bundle();
        tbIfaceimport.tbIfaceimport_api.IEmptyIf[] testimportedIfList = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[1];
        testimportedIfList[0] = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_impl.EmptyIfService());
		data.putParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(testimportedIfList));

        msg.setData(data);
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).setImportedIfList( any(tbIfaceimport.tbIfaceimport_api.IEmptyIf[].class));
	    
    }

    @Test
     public void whenNotifiedimportedIfList()
    {
        tbIfaceimport.tbIfaceimport_api.IEmptyIf[] testimportedIfList = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[1];
        testimportedIfList[0] = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_impl.EmptyIfService());

        testedAdapterAsEventListener.onImportedIfListChanged(testimportedIfList);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.SET_ImportedIfList.getValue(), response.what);
        Bundle data = response.getData();

        
            tbIfaceimport.tbIfaceimport_api.IEmptyIf[] receivedimportedIfList =  tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray((tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[])data.getParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class));

        assertEquals(receivedimportedIfList, testimportedIfList);
    }
    @Test
    public void whenNotifiedlocalIfSignal()
    {
        ISimpleLocalIf testparam = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new SimpleLocalIfService());

        testedAdapterAsEventListener.onLocalIfSignal(testparam);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.SIG_LocalIfSignal.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
        
			ISimpleLocalIf receivedparam = data.getParcelable("param", SimpleLocalIfParcelable.class).getSimpleLocalIf();
        assertEquals(receivedparam, testparam);
}
    @Test
    public void whenNotifiedlocalIfSignalList()
    {
        ISimpleLocalIf[] testparam = new ISimpleLocalIf[1];
        testparam[0] = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new SimpleLocalIfService());

        testedAdapterAsEventListener.onLocalIfSignalList(testparam);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.SIG_LocalIfSignalList.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
        
            ISimpleLocalIf[] receivedparam =  SimpleLocalIfParcelable.unwrapArray((SimpleLocalIfParcelable[])data.getParcelableArray("param", SimpleLocalIfParcelable.class));
        assertEquals(receivedparam, testparam);
}
    @Test
    public void whenNotifiedimportedIfSignal()
    {
        tbIfaceimport.tbIfaceimport_api.IEmptyIf testparam = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_impl.EmptyIfService());

        testedAdapterAsEventListener.onImportedIfSignal(testparam);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.SIG_ImportedIfSignal.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
        
			tbIfaceimport.tbIfaceimport_api.IEmptyIf receivedparam = data.getParcelable("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class).getEmptyIf();
        assertEquals(receivedparam, testparam);
}
    @Test
    public void whenNotifiedimportedIfSignalList()
    {
        tbIfaceimport.tbIfaceimport_api.IEmptyIf[] testparam = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[1];
        testparam[0] = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_impl.EmptyIfService());

        testedAdapterAsEventListener.onImportedIfSignalList(testparam);
        Robolectric.flushForegroundThreadScheduler();

        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.SIG_ImportedIfSignalList.getValue(), response.what);
        Bundle data = response.getData();
        
        data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
        
            tbIfaceimport.tbIfaceimport_api.IEmptyIf[] receivedparam =  tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray((tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[])data.getParcelableArray("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class));
        assertEquals(receivedparam, testparam);
}

    @Test
    public void onlocalIfMethodRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ParentIfMessageType.RPC_LocalIfMethodReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        ISimpleLocalIf testparam = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new SimpleLocalIfService());
		data.putParcelable("param", new SimpleLocalIfParcelable(testparam));
        ISimpleLocalIf returnedValue = TbRefIfacesTestHelper.makeTestSimpleLocalIf(null);


        when(backendServiceMock.localIfMethod( any(ISimpleLocalIf.class))).thenReturn(returnedValue);

        msg.setData(data);
        msg.replyTo = clientReplyMessenger;
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).localIfMethod( any(ISimpleLocalIf.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.RPC_LocalIfMethodResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
		ISimpleLocalIf receivedByClient = resp_data.getParcelable("result", SimpleLocalIfParcelable.class).getSimpleLocalIf();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onlocalIfMethodListRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ParentIfMessageType.RPC_LocalIfMethodListReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        ISimpleLocalIf[] testparam = new ISimpleLocalIf[1];
        testparam[0] = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new SimpleLocalIfService());
		data.putParcelableArray("param", SimpleLocalIfParcelable.wrapArray(testparam));
        ISimpleLocalIf[] returnedValue = new ISimpleLocalIf[1];
        returnedValue[0] = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new SimpleLocalIfService());


        when(backendServiceMock.localIfMethodList( any(ISimpleLocalIf[].class))).thenReturn(returnedValue);

        msg.setData(data);
        msg.replyTo = clientReplyMessenger;
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).localIfMethodList( any(ISimpleLocalIf[].class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.RPC_LocalIfMethodListResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
        ISimpleLocalIf[] receivedByClient =  SimpleLocalIfParcelable.unwrapArray((SimpleLocalIfParcelable[])resp_data.getParcelableArray("result", SimpleLocalIfParcelable.class));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onimportedIfMethodRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ParentIfMessageType.RPC_ImportedIfMethodReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        tbIfaceimport.tbIfaceimport_api.IEmptyIf testparam = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_impl.EmptyIfService());
		data.putParcelable("param", new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(testparam));
        tbIfaceimport.tbIfaceimport_api.IEmptyIf returnedValue = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(null);


        when(backendServiceMock.importedIfMethod( any(tbIfaceimport.tbIfaceimport_api.IEmptyIf.class))).thenReturn(returnedValue);

        msg.setData(data);
        msg.replyTo = clientReplyMessenger;
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).importedIfMethod( any(tbIfaceimport.tbIfaceimport_api.IEmptyIf.class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.RPC_ImportedIfMethodResp.getValue(), response.what);
        Bundle resp_data = response.getData();
		resp_data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
		tbIfaceimport.tbIfaceimport_api.IEmptyIf receivedByClient = resp_data.getParcelable("result", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class).getEmptyIf();

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

    @Test
    public void onimportedIfMethodListRequest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ParentIfMessageType.RPC_ImportedIfMethodListReq.getValue());
        Bundle data = new Bundle();

        int callId = 99;
        data.putInt("callId", callId);
        tbIfaceimport.tbIfaceimport_api.IEmptyIf[] testparam = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[1];
        testparam[0] = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_impl.EmptyIfService());
		data.putParcelableArray("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(testparam));
        tbIfaceimport.tbIfaceimport_api.IEmptyIf[] returnedValue = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[1];
        returnedValue[0] = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_impl.EmptyIfService());


        when(backendServiceMock.importedIfMethodList( any(tbIfaceimport.tbIfaceimport_api.IEmptyIf[].class))).thenReturn(returnedValue);

        msg.setData(data);
        msg.replyTo = clientReplyMessenger;
        mServiceMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        inOrderBackendService.verify(backendServiceMock,times(1)).importedIfMethodList( any(tbIfaceimport.tbIfaceimport_api.IEmptyIf[].class));

        //Now verify it was sent back to caller
        Robolectric.flushForegroundThreadScheduler();
        inOrderClientMessagesHandler.verify(clientMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.RPC_ImportedIfMethodListResp.getValue(), response.what);
        Bundle resp_data = response.getData();
        resp_data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
        tbIfaceimport.tbIfaceimport_api.IEmptyIf[] receivedByClient =  tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray((tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[])resp_data.getParcelableArray("result", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class));

        assertEquals(receivedByClient, returnedValue);
        assertEquals(callId, resp_data.getInt("callId", -1));
    }

}
