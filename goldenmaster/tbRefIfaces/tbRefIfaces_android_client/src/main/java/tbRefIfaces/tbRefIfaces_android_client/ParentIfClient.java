//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbRefIfaces.tbRefIfaces_android_client;

import android.content.ServiceConnection;
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
import android.content.ComponentName;
import android.util.Log;

//import message type and parcelabe types
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_android_messenger.SimpleLocalIfParcelable;

import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;
import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_api.AbstractParentIf;
import tbRefIfaces.tbRefIfaces_android_messenger.ParentIfMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;


public class ParentIfClient extends AbstractParentIf implements ServiceConnection
{
	private static final String TAG = "ParentIfClient";

	private final Context mApplicationContext;

    private boolean mIsBoundToService = false;
    private Messenger mServiceMessenger = null;
    private Messenger mClientMessenger = null;
    private String mConnectionId;
    private ClientHandler mClientHandler = new ClientHandler();

    private final Map<Integer, Consumer<Bundle>> mpendingCalls = new ConcurrentHashMap<>();
    AtomicInteger callIdsGetter = new AtomicInteger(0);
    private ISimpleLocalIf m_localIf = null;
    private ISimpleLocalIf[] m_localIfList = new ISimpleLocalIf[]{};
    private tbIfaceimport.tbIfaceimport_api.IEmptyIf m_importedIf = null;
    private tbIfaceimport.tbIfaceimport_api.IEmptyIf[] m_importedIfList = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[]{};


	public ParentIfClient(Context applicationContext, String connectionId)
	{
        assert (applicationContext != null);
        mApplicationContext = applicationContext;

        if (connectionId.isEmpty())
        {
            mConnectionId = UUID.randomUUID().toString();
        }
        else
        {
            mConnectionId = connectionId;
        }
        mClientMessenger = new Messenger(mClientHandler);
	}

	public boolean isBoundToService()
    {
        return mIsBoundToService;
    }


	/**
     * Binds to a running service of type ParentIfServiceAdapter.
     *
     * @return true if binding was successful, false otherwise.
     */
    public boolean bindToService(String packageName)
    {
        Intent intent = new Intent();
        intent.setClassName(packageName, "tbRefIfaces.tbRefIfaces_android_service.ParentIfServiceAdapter");
        intent.putExtra("connectionID", mConnectionId);
        Log.d(TAG, "Using context: " + mApplicationContext.getClass().getName());
        Log.d(TAG, "bindToService intent=" + intent + ", mServiceConnection=" + this);

        return mApplicationContext.bindService(intent, this,0 );
    }

    /**
     * Unbinds from the service instance.
     */
    public void unbindFromService()
    {
        if (mIsBoundToService)
        {
            Log.v(TAG, "unbindFromService");
            Message msg = Message.obtain(null, ParentIfMessageType.UNREGISTER_CLIENT.ordinal());
            msg.getData().putString("connectionID", mConnectionId);
            mClientHandler.sendToService(msg);
            mApplicationContext.unbindService(this);
            doCleanupForUnbinding("unbindFromService");
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder serviceBinder)
    {
        Log.v(TAG, "onServiceConnected name=" + name + ", serviceBinder=" + serviceBinder);
        // Retrieve and use the Messenger
        mServiceMessenger = new Messenger(serviceBinder);
        mIsBoundToService = true;

        requestRegisterClient();
        fire_readyStatusChanged(true);
    }

    @Override
    public void onServiceDisconnected(ComponentName name)
    {
        Log.i(TAG, "onServiceDisconnected name=" + name);
        doCleanupForUnbinding("onServiceDisconnected name=" + name);
    }

    @Override
    public void onBindingDied(ComponentName name)
    {
        Log.w(TAG, "onBindingDied name=" + name);
        doCleanupForUnbinding("onBindingDied name=" + name);
        ServiceConnection.super.onBindingDied(name);
    }

    private void requestRegisterClient()
    {
        if (mClientMessenger != null)
        {
            Message msg = Message.obtain(null, ParentIfMessageType.REGISTER_CLIENT.ordinal());
            msg.replyTo = mClientMessenger;
            msg.getData().putString("connectionID", mConnectionId);
            mClientHandler.sendToService(msg);
        }
    }

    private void doCleanupForUnbinding(String caller)
    {
        Log.i(TAG, "doCleanupForUnbinding " + caller);
        mServiceMessenger = null;
        if (mIsBoundToService)
        {
            mIsBoundToService = false;
            fire_readyStatusChanged(false);
        }
    }


    class ClientHandler extends Handler {

        ClientHandler(){
            super(Looper.getMainLooper());
        }

	    public void sendToService(Message msg)
	    {
		    if (mServiceMessenger != null)
		    {
			    try
			    {
				    mServiceMessenger.send(msg);
			    } catch (RemoteException e)
			    {
				    Log.e(TAG, "Can't send message to service, looks like it is not correctly connected, make sure client has bind to service " + e);
			    }
		    }
	    }
	    @Override
	    public void handleMessage(Message msg)
	    {
		    Log.i(TAG, "Handle msg " + msg);

		    switch (ParentIfMessageType.fromInteger(msg.what))
		    {
                case INIT:
                {
                    Bundle data = msg.getData();
                    
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
			        
                    
			        ISimpleLocalIf localIf = data.getParcelable("localIf", SimpleLocalIfParcelable.class).getSimpleLocalIf();
				    onLocalIf(localIf);
                    
                    ISimpleLocalIf[] localIfList =  SimpleLocalIfParcelable.unwrapArray((SimpleLocalIfParcelable[])data.getParcelableArray("localIfList", SimpleLocalIfParcelable.class));
				    onLocalIfList(localIfList);
                    
			        tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf = data.getParcelable("importedIf", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class).getEmptyIf();
				    onImportedIf(importedIf);
                    
                    tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfList =  tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray((tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[])data.getParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class));
				    onImportedIfList(importedIfList);

                    break;
                }
			    case SET_LocalIf:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());

                    
			        ISimpleLocalIf localIf = data.getParcelable("localIf", SimpleLocalIfParcelable.class).getSimpleLocalIf();

				    onLocalIf(localIf);
				    break;
			    }
			    case SET_LocalIfList:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());

                    
                    ISimpleLocalIf[] localIfList =  SimpleLocalIfParcelable.unwrapArray((SimpleLocalIfParcelable[])data.getParcelableArray("localIfList", SimpleLocalIfParcelable.class));

				    onLocalIfList(localIfList);
				    break;
			    }
			    case SET_ImportedIf:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());

                    
			        tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf = data.getParcelable("importedIf", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class).getEmptyIf();

				    onImportedIf(importedIf);
				    break;
			    }
			    case SET_ImportedIfList:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());

                    
                    tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfList =  tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray((tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[])data.getParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class));

				    onImportedIfList(importedIfList);
				    break;
			    }
			    // TODO params may be different structs from different modules, there should be a custom class loader 
			    // with a list of class loaders required for this message
			    // IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
			    case SIG_LocalIfSignal: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
                
			        ISimpleLocalIf param = data.getParcelable("param", SimpleLocalIfParcelable.class).getSimpleLocalIf();
				    onLocalIfSignal(param);
				    break;
			    }
			    case SIG_LocalIfSignalList: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
                
                    ISimpleLocalIf[] param =  SimpleLocalIfParcelable.unwrapArray((SimpleLocalIfParcelable[])data.getParcelableArray("param", SimpleLocalIfParcelable.class));
				    onLocalIfSignalList(param);
				    break;
			    }
			    case SIG_ImportedIfSignal: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
                
			        tbIfaceimport.tbIfaceimport_api.IEmptyIf param = data.getParcelable("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class).getEmptyIf();
				    onImportedIfSignal(param);
				    break;
			    }
			    case SIG_ImportedIfSignalList: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
                
                    tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param =  tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray((tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[])data.getParcelableArray("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class));
				    onImportedIfSignalList(param);
				    break;
			    }
			    case RPC_LocalIfMethodResp: {

				    Bundle data = msg.getData();
					data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received ParentIfMessageType.RPC_LocalIfMethodResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_LocalIfMethodListResp: {

				    Bundle data = msg.getData();
					data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received ParentIfMessageType.RPC_LocalIfMethodListResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_ImportedIfMethodResp: {

				    Bundle data = msg.getData();
					data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received ParentIfMessageType.RPC_ImportedIfMethodResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_ImportedIfMethodListResp: {

				    Bundle data = msg.getData();
					data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received ParentIfMessageType.RPC_ImportedIfMethodListResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    default:
				    Log.e(TAG, "Receive Unsupported message: " + msg.what);
				    super.handleMessage(msg);
				    break;
		    }

	    }
    };
    @Override
    public void setLocalIf(ISimpleLocalIf localIf)
    {
        Log.i(TAG, "request setLocalIf called "+ localIf);
        if (m_localIf != localIf)
        {
			Message msg = new Message();
			msg.what = ParentIfMessageType.PROP_LocalIf.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("localIf", new SimpleLocalIfParcelable(localIf));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onLocalIf(ISimpleLocalIf localIf)
    {
        Log.i(TAG, "value received from service for LocalIf ");
        if (m_localIf != localIf)
        {
            m_localIf = localIf;
            fireLocalIfChanged(localIf);
        }

    }

    @Override
    public ISimpleLocalIf getLocalIf()
    {
        Log.i(TAG, "request getLocalIf called, returning local");
        return m_localIf;
    }

  
    @Override
    public void setLocalIfList(ISimpleLocalIf[] localIfList)
    {
        Log.i(TAG, "request setLocalIfList called "+ localIfList);
        if (! Arrays.equals(m_localIfList, localIfList))
        {
			Message msg = new Message();
			msg.what = ParentIfMessageType.PROP_LocalIfList.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelableArray("localIfList", SimpleLocalIfParcelable.wrapArray(localIfList));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onLocalIfList(ISimpleLocalIf[] localIfList)
    {
        Log.i(TAG, "value received from service for LocalIfList ");
        if (! Arrays.equals(m_localIfList, localIfList))
        {
            m_localIfList = localIfList;
            fireLocalIfListChanged(localIfList);
        }

    }

    @Override
    public ISimpleLocalIf[] getLocalIfList()
    {
        Log.i(TAG, "request getLocalIfList called, returning local");
        return m_localIfList;
    }

  
    @Override
    public void setImportedIf(tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf)
    {
        Log.i(TAG, "request setImportedIf called "+ importedIf);
        if (m_importedIf != importedIf)
        {
			Message msg = new Message();
			msg.what = ParentIfMessageType.PROP_ImportedIf.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("importedIf", new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(importedIf));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onImportedIf(tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf)
    {
        Log.i(TAG, "value received from service for ImportedIf ");
        if (m_importedIf != importedIf)
        {
            m_importedIf = importedIf;
            fireImportedIfChanged(importedIf);
        }

    }

    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf getImportedIf()
    {
        Log.i(TAG, "request getImportedIf called, returning local");
        return m_importedIf;
    }

  
    @Override
    public void setImportedIfList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfList)
    {
        Log.i(TAG, "request setImportedIfList called "+ importedIfList);
        if (! Arrays.equals(m_importedIfList, importedIfList))
        {
			Message msg = new Message();
			msg.what = ParentIfMessageType.PROP_ImportedIfList.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(importedIfList));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onImportedIfList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfList)
    {
        Log.i(TAG, "value received from service for ImportedIfList ");
        if (! Arrays.equals(m_importedIfList, importedIfList))
        {
            m_importedIfList = importedIfList;
            fireImportedIfListChanged(importedIfList);
        }

    }

    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf[] getImportedIfList()
    {
        Log.i(TAG, "request getImportedIfList called, returning local");
        return m_importedIfList;
    }

  
    // methods

   
    @Override
    public ISimpleLocalIf localIfMethod(ISimpleLocalIf param) {
        CompletableFuture<ISimpleLocalIf> resFuture = localIfMethodAsync(param);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<ISimpleLocalIf> localIfMethodAsync(ISimpleLocalIf param) {

    	Log.i(TAG, "Call on service localIfMethod  "+ " " + param);
		Message msg = new Message();
		msg.what = ParentIfMessageType.RPC_LocalIfMethodReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("param", new SimpleLocalIfParcelable(param));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<ISimpleLocalIf>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            
		    ISimpleLocalIf result = bundle.getParcelable("result", SimpleLocalIfParcelable.class).getSimpleLocalIf();
            Log.v(TAG, "resolve localIfMethod" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public ISimpleLocalIf[] localIfMethodList(ISimpleLocalIf[] param) {
        CompletableFuture<ISimpleLocalIf[]> resFuture = localIfMethodListAsync(param);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<ISimpleLocalIf[]> localIfMethodListAsync(ISimpleLocalIf[] param) {

    	Log.i(TAG, "Call on service localIfMethodList  "+ " " + param);
		Message msg = new Message();
		msg.what = ParentIfMessageType.RPC_LocalIfMethodListReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelableArray("param", SimpleLocalIfParcelable.wrapArray(param));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<ISimpleLocalIf[]>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            
            ISimpleLocalIf[] result =  SimpleLocalIfParcelable.unwrapArray((SimpleLocalIfParcelable[])bundle.getParcelableArray("result", SimpleLocalIfParcelable.class));
            Log.v(TAG, "resolve localIfMethodList" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIfMethod(tbIfaceimport.tbIfaceimport_api.IEmptyIf param) {
        CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf> resFuture = importedIfMethodAsync(param);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfMethodAsync(tbIfaceimport.tbIfaceimport_api.IEmptyIf param) {

    	Log.i(TAG, "Call on service importedIfMethod  "+ " " + param);
		Message msg = new Message();
		msg.what = ParentIfMessageType.RPC_ImportedIfMethodReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("param", new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(param));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            
		    tbIfaceimport.tbIfaceimport_api.IEmptyIf result = bundle.getParcelable("result", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class).getEmptyIf();
            Log.v(TAG, "resolve importedIfMethod" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfMethodList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param) {
        CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf[]> resFuture = importedIfMethodListAsync(param);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf[]> importedIfMethodListAsync(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param) {

    	Log.i(TAG, "Call on service importedIfMethodList  "+ " " + param);
		Message msg = new Message();
		msg.what = ParentIfMessageType.RPC_ImportedIfMethodListReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelableArray("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(param));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf[]>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            
            tbIfaceimport.tbIfaceimport_api.IEmptyIf[] result =  tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray((tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[])bundle.getParcelableArray("result", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class));
            Log.v(TAG, "resolve importedIfMethodList" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }    

    @Override
    public boolean _isReady() {
        return mIsBoundToService  && mServiceMessenger != null;
    }

	// Should be called when message arrives
    public void onLocalIfSignal(ISimpleLocalIf param)
    {
        Log.i(TAG, "onLocalIfSignal  received from service");
        fireLocalIfSignal(param);
    }
    public void onLocalIfSignalList(ISimpleLocalIf[] param)
    {
        Log.i(TAG, "onLocalIfSignalList  received from service");
        fireLocalIfSignalList(param);
    }
    public void onImportedIfSignal(tbIfaceimport.tbIfaceimport_api.IEmptyIf param)
    {
        Log.i(TAG, "onImportedIfSignal  received from service");
        fireImportedIfSignal(param);
    }
    public void onImportedIfSignalList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param)
    {
        Log.i(TAG, "onImportedIfSignalList  received from service");
        fireImportedIfSignalList(param);
    }
}
