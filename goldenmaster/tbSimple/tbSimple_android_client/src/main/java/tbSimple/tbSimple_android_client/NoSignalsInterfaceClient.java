//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_client;

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

import tbSimple.tbSimple_api.INoSignalsInterfaceEventListener;
import tbSimple.tbSimple_api.INoSignalsInterface;
import tbSimple.tbSimple_api.AbstractNoSignalsInterface;
import tbSimple.tbSimple_android_messenger.NoSignalsInterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;


public class NoSignalsInterfaceClient extends AbstractNoSignalsInterface implements ServiceConnection
{
	private static final String TAG = "NoSignalsInterfaceClient";

	private final Context mApplicationContext;

    private boolean mIsBoundToService = false;
    private Messenger mServiceMessenger = null;
    private Messenger mClientMessenger = null;
    private String mConnectionId;
    private ClientHandler mClientHandler = new ClientHandler();

    private final Map<Integer, Consumer<Bundle>> mpendingCalls = new ConcurrentHashMap<>();
    AtomicInteger callIdsGetter = new AtomicInteger(0);
    private boolean m_propBool = false;
    private int m_propInt = 0;


	public NoSignalsInterfaceClient(Context applicationContext, String connectionId)
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
     * Binds to a running service of type NoSignalsInterfaceServiceAdapter.
     *
     * @return true if binding was successful, false otherwise.
     */
    public boolean bindToService(String packageName)
    {
        Intent intent = new Intent();
        intent.setClassName(packageName, "tbSimple.tbSimple_android_service.NoSignalsInterfaceServiceAdapter");
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
            Message msg = Message.obtain(null, NoSignalsInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
            msg.getData().putString("connectionID", mConnectionId);
            mClientHandler.sendToService(msg);
            mApplicationContext.unbindService(this);
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
        Log.w(TAG, "onServiceDisconnected name=" + name);
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
            Message msg = Message.obtain(null, NoSignalsInterfaceMessageType.REGISTER_CLIENT.ordinal());
            msg.replyTo = mClientMessenger;
            msg.getData().putString("connectionID", mConnectionId);
            mClientHandler.sendToService(msg);
        }
    }

    private void doCleanupForUnbinding(String caller)
    {
        mServiceMessenger = null;
        mClientMessenger = null;
        mIsBoundToService = false;
        fire_readyStatusChanged(false);
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

		    switch (NoSignalsInterfaceMessageType.fromInteger(msg.what))
		    {
                case INIT:
                {
                    Bundle data = msg.getData();
                    
			        
                    
			        boolean propBool = data.getBoolean("propBool", false);
				    onPropBool(propBool);
                    
			        int propInt = data.getInt("propInt", 0);
				    onPropInt(propInt);

                    break;
                }
			    case SET_PropBool:
			    {
				    Bundle data = msg.getData();

                    
			        boolean propBool = data.getBoolean("propBool", false);

				    onPropBool(propBool);
				    break;
			    }
			    case SET_PropInt:
			    {
				    Bundle data = msg.getData();

                    
			        int propInt = data.getInt("propInt", 0);

				    onPropInt(propInt);
				    break;
			    }
			    // TODO params may be different structs from different modules, there should be a custom class loader 
			    // with a list of class loaders required for this message
			    // IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
			    case RPC_FuncVoidResp: {

				    Bundle data = msg.getData();
				    data.setClassLoader(VoidParcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received NoSignalsInterfaceMessageType.RPC_FuncVoidResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_FuncBoolResp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received NoSignalsInterfaceMessageType.RPC_FuncBoolResp , could not find pending call for " + msg.obj);
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
    public void setPropBool(boolean propBool)
    {
        Log.i(TAG, "request setPropBool called "+ propBool);
        if (m_propBool != propBool)
        {
			Message msg = new Message();
			msg.what = NoSignalsInterfaceMessageType.PROP_PropBool.getValue();
			Bundle data = new Bundle();
            
		        data.putBoolean("propBool", propBool);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropBool(boolean propBool)
    {
        Log.i(TAG, "value received from service for PropBool ");
        if (m_propBool != propBool)
        {
            m_propBool = propBool;
            firePropBoolChanged(propBool);
        }

    }

    @Override
    public boolean getPropBool()
    {
        Log.i(TAG, "request getPropBool called, returning local");
        return m_propBool;
    }

  
    @Override
    public void setPropInt(int propInt)
    {
        Log.i(TAG, "request setPropInt called "+ propInt);
        if (m_propInt != propInt)
        {
			Message msg = new Message();
			msg.what = NoSignalsInterfaceMessageType.PROP_PropInt.getValue();
			Bundle data = new Bundle();
            
		        data.putInt("propInt", propInt);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropInt(int propInt)
    {
        Log.i(TAG, "value received from service for PropInt ");
        if (m_propInt != propInt)
        {
            m_propInt = propInt;
            firePropIntChanged(propInt);
        }

    }

    @Override
    public int getPropInt()
    {
        Log.i(TAG, "request getPropInt called, returning local");
        return m_propInt;
    }

  
    // methods

   
    @Override
    public void funcVoid() {
        CompletableFuture<Void> resFuture = funcVoidAsync();
        try {
            resFuture.get();
            return;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Void> funcVoidAsync() {

    	Log.i(TAG, "Call on service funcVoid  ");
		Message msg = new Message();
		msg.what = NoSignalsInterfaceMessageType.RPC_FuncVoidReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
		msg.setData(data);
        msg.replyTo = mClientMessenger;
		mClientHandler.sendToService(msg);

        CompletableFuture<Void>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            future.complete(null);
            Log.v(TAG, "resolve funcVoid");
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);

        return future;
    }

   
    @Override
    public boolean funcBool(boolean paramBool) {
        CompletableFuture<Boolean> resFuture = funcBoolAsync(paramBool);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Boolean> funcBoolAsync(boolean paramBool) {

    	Log.i(TAG, "Call on service funcBool  "+ " " + paramBool);
		Message msg = new Message();
		msg.what = NoSignalsInterfaceMessageType.RPC_FuncBoolReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putBoolean("paramBool", paramBool);
		msg.setData(data);
        msg.replyTo = mClientMessenger;
		mClientHandler.sendToService(msg);

        CompletableFuture<Boolean>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            
		    boolean result = bundle.getBoolean("result", false);
            Log.v(TAG, "resolve funcBool" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);

        return future;
    }    

    @Override
    public boolean _isReady() {
        return mIsBoundToService  && mServiceMessenger != null;
    }

	// Should be called when message arrives
}
