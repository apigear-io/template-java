// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame1.tbSame1_android_client;

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
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_android_messenger.Struct1Parcelable;

import tbSame1.tbSame1_api.ISameStruct1InterfaceEventListener;
import tbSame1.tbSame1_api.ISameStruct1Interface;
import tbSame1.tbSame1_api.AbstractSameStruct1Interface;
import tbSame1.tbSame1_api.RemoteOperationException;
import tbSame1.tbSame1_android_messenger.SameStruct1InterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;


public class SameStruct1InterfaceClient extends AbstractSameStruct1Interface implements ServiceConnection
{
	private static final String TAG = "SameStruct1InterfaceClient";

	private final Context mApplicationContext;

    private boolean mIsBoundToService = false;
    private Messenger mServiceMessenger = null;
    private Messenger mClientMessenger = null;
    private String mConnectionId;
    private ClientHandler mClientHandler = new ClientHandler();

    private final Map<Integer, Consumer<Bundle>> mpendingCalls = new ConcurrentHashMap<>();
    AtomicInteger callIdsGetter = new AtomicInteger(0);
    private Struct1 m_prop1 = new Struct1();


	public SameStruct1InterfaceClient(Context applicationContext, String connectionId)
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
     * Binds to a running service of type SameStruct1InterfaceServiceAdapter.
     *
     * @return true if binding was successful, false otherwise.
     */
    public boolean bindToService(String packageName)
    {
        Intent intent = new Intent();
        intent.setClassName(packageName, "tbSame1.tbSame1_android_service.SameStruct1InterfaceServiceAdapter");
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
            Message msg = Message.obtain(null, SameStruct1InterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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
            Message msg = Message.obtain(null, SameStruct1InterfaceMessageType.REGISTER_CLIENT.ordinal());
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

        for (Consumer<Bundle> bundleConsumer : mpendingCalls.values())
        {
            bundleConsumer.accept(null);
        }
        mpendingCalls.clear();
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

		    switch (SameStruct1InterfaceMessageType.fromInteger(msg.what))
		    {
                case INIT:
                {
                    Bundle data = msg.getData();
                    
        data.setClassLoader(Struct1Parcelable.class.getClassLoader());
			        
                    
			        Struct1 prop1 = data.getParcelable("prop1", Struct1Parcelable.class).getStruct1();
				    onProp1(prop1);

                    break;
                }
			    case SET_Prop1:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(Struct1Parcelable.class.getClassLoader());

                    
			        Struct1 prop1 = data.getParcelable("prop1", Struct1Parcelable.class).getStruct1();

				    onProp1(prop1);
				    break;
			    }
			    // TODO params may be different structs from different modules, there should be a custom class loader 
			    // with a list of class loaders required for this message
			    // IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
			    case SIG_Sig1: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(Struct1Parcelable.class.getClassLoader());
                
			        Struct1 param1 = data.getParcelable("param1", Struct1Parcelable.class).getStruct1();
				    onSig1(param1);
				    break;
			    }
			    case RPC_Func1Resp: {

				    Bundle data = msg.getData();
					data.setClassLoader(Struct1Parcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received SameStruct1InterfaceMessageType.RPC_Func1Resp , could not find pending call for " + msg.obj);
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
    public void setProp1(Struct1 prop1)
    {
        Log.i(TAG, "request setProp1 called "+ prop1);
        if ( (m_prop1 != null && ! m_prop1.equals(prop1))
        || (m_prop1 == null && prop1 != null ))
        {
			Message msg = new Message();
			msg.what = SameStruct1InterfaceMessageType.PROP_Prop1.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("prop1", new Struct1Parcelable(prop1));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onProp1(Struct1 prop1)
    {
        Log.i(TAG, "value received from service for Prop1 ");
        if ( (m_prop1 != null && ! m_prop1.equals(prop1))
        || (m_prop1 == null && prop1 != null ))
        {
            m_prop1 = prop1;
            fireProp1Changed(prop1);
        }

    }

    @Override
    public Struct1 getProp1()
    {
        Log.i(TAG, "request getProp1 called, returning local");
        return m_prop1;
    }

  
    // methods

   
    @Override
    public Struct1 func1(Struct1 param1) {
        CompletableFuture<Struct1> resFuture = func1Async(param1);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new RuntimeException(cause);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Struct1> func1Async(Struct1 param1) {

    	Log.i(TAG, "Call on service func1  "+ " " + param1);
		Message msg = new Message();
		msg.what = SameStruct1InterfaceMessageType.RPC_Func1Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("param1", new Struct1Parcelable(param1));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Struct1>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                Log.w(TAG, "func1: received null bundle (service disconnected?)");
                future.completeExceptionally(new RemoteOperationException("service disconnected", RemoteOperationException.ERROR_SERVICE_DISCONNECTED));
                return;
            }
            if (bundle.getBoolean("error", false))
            {
                String errorMessage = bundle.getString("errorMessage", "unknown error");
                int errorCode = bundle.getInt("errorCode", RemoteOperationException.ERROR_UNKNOWN);
                Log.w(TAG, "func1 failed: " + errorMessage);
                future.completeExceptionally(new RemoteOperationException(errorMessage, errorCode));
                return;
            }
            
		    Struct1 result = bundle.getParcelable("result", Struct1Parcelable.class).getStruct1();
            Log.v(TAG, "resolve func1" + result);
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
    public void onSig1(Struct1 param1)
    {
        Log.i(TAG, "onSig1  received from service");
        fireSig1(param1);
    }
}
