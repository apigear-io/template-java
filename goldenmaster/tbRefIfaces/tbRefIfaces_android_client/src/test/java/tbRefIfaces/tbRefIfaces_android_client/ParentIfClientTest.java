//TODO later// Copyright Epic Games, Inc. All Rights Reserved.
package tbRefIfaces.tbRefIfaces_android_client;

import tbRefIfaces.tbRefIfaces_android_client.ParentIfClient;

//import message type and parcelabe types
import tbRefIfaces.tbRefIfaces_api.TbRefIfacesTestHelper;

import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;
import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_api.AbstractParentIf;
import tbRefIfaces.tbRefIfaces_android_messenger.ParentIfMessageType;

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


interface IParentIfClientMessageGetter
{
    public void getMessage(Message msg);
}


@Config(sdk = 33, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ParentIfClientTest
{

    @Mock
    private Context mMockContext;
   
    private ParentIfClient testedClient;
    private IParentIfEventListener listenerMock = mock(IParentIfEventListener.class);
    private Messenger mServiceMessenger;
    private Messenger mClientMessenger;
    private Handler mServiceHandler ;
    private String mTestConnectionID1 = "MyTestClient";
    InOrder inOrderServiceMessenger;
    InOrder inOrderEventListener;
   
    private IParentIfClientMessageGetter serviceMessagesStorage = mock(IParentIfClientMessageGetter.class);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
    @After
    public void tearDown() {

        testedClient.unbindFromService();

        Robolectric.flushForegroundThreadScheduler();

        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(ParentIfMessageType.UNREGISTER_CLIENT.getValue(), register_msg.what);
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedClient.removeEventListener(listenerMock);
    }

    Handler createServiceHandlerMock(IParentIfClientMessageGetter messageGetterMock)
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


        testedClient = new ParentIfClient(mMockContext, mTestConnectionID1);
        testedClient.addEventListener(listenerMock);
        ComponentName componentName = new ComponentName("tbRefIfaces.tbRefIfaces_android_service", "tbRefIfaces.tbRefIfaces_android_service.ParentIfServiceAdapter");
        testedClient.onServiceConnected(componentName, serviceBinder);

        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message register_msg = messageCaptor.getValue();
        assertEquals(ParentIfMessageType.REGISTER_CLIENT.getValue(), register_msg.what);
        mClientMessenger = register_msg.replyTo;
        assertEquals(mTestConnectionID1, register_msg.getData().getString("connectionID", ""));

        inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedClient._isReady());
    }

    @Test
    public void onInitReceive()  throws RemoteException
    {
    //PREPARE message

        Message msg = Message.obtain(null, ParentIfMessageType.INIT.getValue());
        Bundle data = new Bundle();
        ISimpleLocalIf testlocalIf = TbRefIfacesTestHelper.makeTestSimpleLocalIf(null);
		data.putParcelable("localIf", new SimpleLocalIfParcelable(testlocalIf));
        ISimpleLocalIf[] testlocalIfList = new ISimpleLocalIf[1];
        testlocalIfList[0] = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new ISimpleLocalIf[]{});
		data.putParcelableArray("localIfList", SimpleLocalIfParcelable.wrapArray(testlocalIfList));
        tbIfaceimport.tbIfaceimport_api.IEmptyIf testimportedIf = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(null);
		data.putParcelable("importedIf", new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(testimportedIf));
        tbIfaceimport.tbIfaceimport_api.IEmptyIf[] testimportedIfList = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[1];
        testimportedIfList[0] = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_api.IEmptyIf[]{});
		data.putParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(testimportedIfList));

    //setup mock expectations
        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        // Make sure test data is properly filled and in case of extern serialization is in place.
        //inOrderEventListener.verify(listenerMock,times(1)).onLocalIfChanged(any(ISimpleLocalIf.class));
        
        // Make sure test data is properly filled and in case of extern serialization is in place.
        //inOrderEventListener.verify(listenerMock,times(1)).onLocalIfListChanged(any(ISimpleLocalIf[].class));
        
        // Make sure test data is properly filled and in case of extern serialization is in place.
        //inOrderEventListener.verify(listenerMock,times(1)).onImportedIfChanged(any(tbIfaceimport.tbIfaceimport_api.IEmptyIf.class));
        
        // Make sure test data is properly filled and in case of extern serialization is in place.
        //inOrderEventListener.verify(listenerMock,times(1)).onImportedIfListChanged(any(tbIfaceimport.tbIfaceimport_api.IEmptyIf[].class));
    }
    @Test
    public void onReceivelocalIfPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ParentIfMessageType.SET_LocalIf.getValue());
        Bundle data = new Bundle();
        ISimpleLocalIf testlocalIf = TbRefIfacesTestHelper.makeTestSimpleLocalIf(null);
		data.putParcelable("localIf", new SimpleLocalIfParcelable(testlocalIf));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        // Make sure test data is properly filled and in case of extern serialization is in place.
        //inOrderEventListener.verify(listenerMock,times(1)).onLocalIfChanged(any(ISimpleLocalIf.class));	    
    }
    
    /*
    @Test
     public void setPropertyRequestlocalIf()
    {
        ISimpleLocalIf testlocalIf = TbRefIfacesTestHelper.makeTestSimpleLocalIf(null);

        testedClient.setLocalIf(testlocalIf);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.PROP_LocalIf.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
        
			ISimpleLocalIf receivedlocalIf = data.getParcelable("localIf", SimpleLocalIfParcelable.class).getSimpleLocalIf();
        assertEquals(receivedlocalIf, testlocalIf);
    }
    
    */
    @Test
    public void onReceivelocalIfListPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ParentIfMessageType.SET_LocalIfList.getValue());
        Bundle data = new Bundle();
        ISimpleLocalIf[] testlocalIfList = new ISimpleLocalIf[1];
        testlocalIfList[0] = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new ISimpleLocalIf[]{});
		data.putParcelableArray("localIfList", SimpleLocalIfParcelable.wrapArray(testlocalIfList));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        // Make sure test data is properly filled and in case of extern serialization is in place.
        //inOrderEventListener.verify(listenerMock,times(1)).onLocalIfListChanged(any(ISimpleLocalIf[].class));	    
    }
    
    /*
    @Test
     public void setPropertyRequestlocalIfList()
    {
        ISimpleLocalIf[] testlocalIfList = new ISimpleLocalIf[1];
        testlocalIfList[0] = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new ISimpleLocalIf[]{});

        testedClient.setLocalIfList(testlocalIfList);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.PROP_LocalIfList.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
        
            ISimpleLocalIf[] receivedlocalIfList =  SimpleLocalIfParcelable.unwrapArray((SimpleLocalIfParcelable[])data.getParcelableArray("localIfList", SimpleLocalIfParcelable.class));
        assertEquals(receivedlocalIfList, testlocalIfList);
    }
    
    */
    @Test
    public void onReceiveimportedIfPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ParentIfMessageType.SET_ImportedIf.getValue());
        Bundle data = new Bundle();
        tbIfaceimport.tbIfaceimport_api.IEmptyIf testimportedIf = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(null);
		data.putParcelable("importedIf", new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(testimportedIf));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        // Make sure test data is properly filled and in case of extern serialization is in place.
        //inOrderEventListener.verify(listenerMock,times(1)).onImportedIfChanged(any(tbIfaceimport.tbIfaceimport_api.IEmptyIf.class));	    
    }
    
    /*
    @Test
     public void setPropertyRequestimportedIf()
    {
        tbIfaceimport.tbIfaceimport_api.IEmptyIf testimportedIf = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(null);

        testedClient.setImportedIf(testimportedIf);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.PROP_ImportedIf.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
        
			tbIfaceimport.tbIfaceimport_api.IEmptyIf receivedimportedIf = data.getParcelable("importedIf", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class).getEmptyIf();
        assertEquals(receivedimportedIf, testimportedIf);
    }
    
    */
    @Test
    public void onReceiveimportedIfListPropertyChangeTest() throws RemoteException {
        // Create and send message
        Message msg = Message.obtain(null, ParentIfMessageType.SET_ImportedIfList.getValue());
        Bundle data = new Bundle();
        tbIfaceimport.tbIfaceimport_api.IEmptyIf[] testimportedIfList = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[1];
        testimportedIfList[0] = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_api.IEmptyIf[]{});
		data.putParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(testimportedIfList));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        // Make sure test data is properly filled and in case of extern serialization is in place.
        //inOrderEventListener.verify(listenerMock,times(1)).onImportedIfListChanged(any(tbIfaceimport.tbIfaceimport_api.IEmptyIf[].class));	    
    }
    
    /*
    @Test
     public void setPropertyRequestimportedIfList()
    {
        tbIfaceimport.tbIfaceimport_api.IEmptyIf[] testimportedIfList = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[1];
        testimportedIfList[0] = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_api.IEmptyIf[]{});

        testedClient.setImportedIfList(testimportedIfList);
        Robolectric.flushForegroundThreadScheduler();
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        assertEquals(ParentIfMessageType.PROP_ImportedIfList.getValue(), response.what);
        Bundle data = response.getData();
		data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
        
            tbIfaceimport.tbIfaceimport_api.IEmptyIf[] receivedimportedIfList =  tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray((tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[])data.getParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class));
        assertEquals(receivedimportedIfList, testimportedIfList);
    }
    
    */
    @Test
    public void whenNotifiedlocalIfSignal() throws RemoteException
    {

        Message msg = Message.obtain(null, ParentIfMessageType.SIG_LocalIfSignal.getValue());
        Bundle data = new Bundle();
        ISimpleLocalIf testparam = TbRefIfacesTestHelper.makeTestSimpleLocalIf(null);
		data.putParcelable("param", new SimpleLocalIfParcelable(testparam));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onLocalIfSignal( any(ISimpleLocalIf.class));

}
    @Test
    public void whenNotifiedlocalIfSignalList() throws RemoteException
    {

        Message msg = Message.obtain(null, ParentIfMessageType.SIG_LocalIfSignalList.getValue());
        Bundle data = new Bundle();
        ISimpleLocalIf[] testparam = new ISimpleLocalIf[1];
        testparam[0] = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new ISimpleLocalIf[]{});
		data.putParcelableArray("param", SimpleLocalIfParcelable.wrapArray(testparam));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onLocalIfSignalList( any(ISimpleLocalIf[].class));

}
    @Test
    public void whenNotifiedimportedIfSignal() throws RemoteException
    {

        Message msg = Message.obtain(null, ParentIfMessageType.SIG_ImportedIfSignal.getValue());
        Bundle data = new Bundle();
        tbIfaceimport.tbIfaceimport_api.IEmptyIf testparam = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(null);
		data.putParcelable("param", new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(testparam));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onImportedIfSignal( any(tbIfaceimport.tbIfaceimport_api.IEmptyIf.class));

}
    @Test
    public void whenNotifiedimportedIfSignalList() throws RemoteException
    {

        Message msg = Message.obtain(null, ParentIfMessageType.SIG_ImportedIfSignalList.getValue());
        Bundle data = new Bundle();
        tbIfaceimport.tbIfaceimport_api.IEmptyIf[] testparam = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[1];
        testparam[0] = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_api.IEmptyIf[]{});
		data.putParcelableArray("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(testparam));

        msg.setData(data);
        mClientMessenger.send(msg);
        Robolectric.flushForegroundThreadScheduler();
        
        inOrderEventListener.verify(listenerMock,times(1)).onImportedIfSignalList( any(tbIfaceimport.tbIfaceimport_api.IEmptyIf[].class));

}


    public void onlocalIfMethodRequest() throws RemoteException {

        // Execute method
        ISimpleLocalIf testparam = TbRefIfacesTestHelper.makeTestSimpleLocalIf(null);
        ISimpleLocalIf expectedResult = TbRefIfacesTestHelper.makeTestSimpleLocalIf(null);

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<ISimpleLocalIf> resFuture = testedClient.localIfMethodAsync(testparam);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(ParentIfMessageType.RPC_LocalIfMethodReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
        
			ISimpleLocalIf receivedparam = data.getParcelable("param", SimpleLocalIfParcelable.class).getSimpleLocalIf();
        assertEquals(receivedparam, testparam);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, ParentIfMessageType.RPC_LocalIfMethodResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new SimpleLocalIfParcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onlocalIfMethodListRequest() throws RemoteException {

        // Execute method
        ISimpleLocalIf[] testparam = new ISimpleLocalIf[1];
        testparam[0] = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new ISimpleLocalIf[]{});
        ISimpleLocalIf[] expectedResult = new ISimpleLocalIf[1];
        expectedResult[0] = TbRefIfacesTestHelper.makeTestSimpleLocalIf(new ISimpleLocalIf[]{});

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<ISimpleLocalIf[]> resFuture = testedClient.localIfMethodListAsync(testparam);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(ParentIfMessageType.RPC_LocalIfMethodListReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
        data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
        
            ISimpleLocalIf[] receivedparam =  SimpleLocalIfParcelable.unwrapArray((SimpleLocalIfParcelable[])data.getParcelableArray("param", SimpleLocalIfParcelable.class));
        assertEquals(receivedparam, testparam);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, ParentIfMessageType.RPC_LocalIfMethodListResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelableArray("result", SimpleLocalIfParcelable.wrapArray(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onimportedIfMethodRequest() throws RemoteException {

        // Execute method
        tbIfaceimport.tbIfaceimport_api.IEmptyIf testparam = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(null);
        tbIfaceimport.tbIfaceimport_api.IEmptyIf expectedResult = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(null);

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf> resFuture = testedClient.importedIfMethodAsync(testparam);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(ParentIfMessageType.RPC_ImportedIfMethodReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
    // all structs (even from other modules) are known at compile time (see gradle files) and share same PathClassLoader, any class loader provides access to it.
        data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
        
			tbIfaceimport.tbIfaceimport_api.IEmptyIf receivedparam = data.getParcelable("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class).getEmptyIf();
        assertEquals(receivedparam, testparam);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, ParentIfMessageType.RPC_ImportedIfMethodResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelable("result", new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }


    public void onimportedIfMethodListRequest() throws RemoteException {

        // Execute method
        tbIfaceimport.tbIfaceimport_api.IEmptyIf[] testparam = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[1];
        testparam[0] = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_api.IEmptyIf[]{});
        tbIfaceimport.tbIfaceimport_api.IEmptyIf[] expectedResult = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[1];
        expectedResult[0] = tbIfaceimport.tbIfaceimport_api.TbIfaceimportTestHelper.makeTestEmptyIf(new tbIfaceimport.tbIfaceimport_api.IEmptyIf[]{});

        AtomicBoolean receivedResp = new AtomicBoolean(false);
        CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf[]> resFuture = testedClient.importedIfMethodListAsync(testparam);

        resFuture.thenAccept(result -> {
            assertEquals(expectedResult, result);
            receivedResp.set(true);
        });
        Robolectric.flushForegroundThreadScheduler();

        // Expect msg to be sent.
        inOrderServiceMessenger.verify(serviceMessagesStorage, times(1)).getMessage(messageCaptor.capture());


        Message method_request = messageCaptor.getValue();
        assertEquals(ParentIfMessageType.RPC_ImportedIfMethodListReq.getValue(), method_request.what);
        Bundle data = method_request.getData();
        
    // all structs (even from other modules) are known at compile time (see gradle files) and share same PathClassLoader, any class loader provides access to it.
        data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
        
            tbIfaceimport.tbIfaceimport_api.IEmptyIf[] receivedparam =  tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray((tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[])data.getParcelableArray("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class));
        assertEquals(receivedparam, testparam);
        int returnedCallId = data.getInt("callId", -1);

        //Prepare response

        Message msg = Message.obtain(null, ParentIfMessageType.RPC_ImportedIfMethodListResp.getValue());

        Bundle result_data = new Bundle();
		result_data.putInt("callId", returnedCallId);
		result_data.putParcelableArray("result", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(expectedResult));

        msg.setData(result_data);
        method_request.replyTo.send(msg);
        Robolectric.flushForegroundThreadScheduler();

        assertTrue(receivedResp.get());

    }

}
