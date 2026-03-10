//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package testbed1.testbed1_android_client;

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
import testbed1.testbed1_api.Enum0;
import testbed1.testbed1_android_messenger.Enum0Parcelable;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_android_messenger.StructBoolParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;

import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_api.AbstractStructArrayInterface;
import testbed1.testbed1_api.RemoteOperationException;
import testbed1.testbed1_android_messenger.StructArrayInterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;


public class StructArrayInterfaceClient extends AbstractStructArrayInterface implements ServiceConnection
{
	private static final String TAG = "StructArrayInterfaceClient";

	private final Context mApplicationContext;

    private boolean mIsBoundToService = false;
    private Messenger mServiceMessenger = null;
    private Messenger mClientMessenger = null;
    private String mConnectionId;
    private ClientHandler mClientHandler = new ClientHandler();

    private final Map<Integer, Consumer<Bundle>> mpendingCalls = new ConcurrentHashMap<>();
    AtomicInteger callIdsGetter = new AtomicInteger(0);
    private StructBool[] m_propBool = new StructBool[]{};
    private StructInt[] m_propInt = new StructInt[]{};
    private StructFloat[] m_propFloat = new StructFloat[]{};
    private StructString[] m_propString = new StructString[]{};
    private Enum0[] m_propEnum = new Enum0[]{};


	public StructArrayInterfaceClient(Context applicationContext, String connectionId)
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
     * Binds to a running service of type StructArrayInterfaceServiceAdapter.
     *
     * @return true if binding was successful, false otherwise.
     */
    public boolean bindToService(String packageName)
    {
        Intent intent = new Intent();
        intent.setClassName(packageName, "testbed1.testbed1_android_service.StructArrayInterfaceServiceAdapter");
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
            Message msg = Message.obtain(null, StructArrayInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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
            Message msg = Message.obtain(null, StructArrayInterfaceMessageType.REGISTER_CLIENT.ordinal());
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

		    switch (StructArrayInterfaceMessageType.fromInteger(msg.what))
		    {
                case INIT:
                {
                    Bundle data = msg.getData();
                    
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(StructBoolParcelable.class.getClassLoader());
			        
                    
                    StructBool[] propBool =  StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("propBool", StructBoolParcelable.class));
				    onPropBool(propBool);
                    
                    StructInt[] propInt =  StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("propInt", StructIntParcelable.class));
				    onPropInt(propInt);
                    
                    StructFloat[] propFloat =  StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("propFloat", StructFloatParcelable.class));
				    onPropFloat(propFloat);
                    
                    StructString[] propString =  StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("propString", StructStringParcelable.class));
				    onPropString(propString);
                    
                    Enum0[] propEnum =  Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("propEnum", Enum0Parcelable.class));
				    onPropEnum(propEnum);

                    break;
                }
			    case SET_PropBool:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructBoolParcelable.class.getClassLoader());

                    
                    StructBool[] propBool =  StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("propBool", StructBoolParcelable.class));

				    onPropBool(propBool);
				    break;
			    }
			    case SET_PropInt:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructIntParcelable.class.getClassLoader());

                    
                    StructInt[] propInt =  StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("propInt", StructIntParcelable.class));

				    onPropInt(propInt);
				    break;
			    }
			    case SET_PropFloat:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructFloatParcelable.class.getClassLoader());

                    
                    StructFloat[] propFloat =  StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("propFloat", StructFloatParcelable.class));

				    onPropFloat(propFloat);
				    break;
			    }
			    case SET_PropString:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructStringParcelable.class.getClassLoader());

                    
                    StructString[] propString =  StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("propString", StructStringParcelable.class));

				    onPropString(propString);
				    break;
			    }
			    case SET_PropEnum:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(Enum0Parcelable.class.getClassLoader());

                    
                    Enum0[] propEnum =  Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("propEnum", Enum0Parcelable.class));

				    onPropEnum(propEnum);
				    break;
			    }
			    // TODO params may be different structs from different modules, there should be a custom class loader 
			    // with a list of class loaders required for this message
			    // IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
			    case SIG_SigBool: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructBoolParcelable.class.getClassLoader());
                
                    StructBool[] paramBool =  StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("paramBool", StructBoolParcelable.class));
				    onSigBool(paramBool);
				    break;
			    }
			    case SIG_SigInt: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructIntParcelable.class.getClassLoader());
                
                    StructInt[] paramInt =  StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("paramInt", StructIntParcelable.class));
				    onSigInt(paramInt);
				    break;
			    }
			    case SIG_SigFloat: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructFloatParcelable.class.getClassLoader());
                
                    StructFloat[] paramFloat =  StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("paramFloat", StructFloatParcelable.class));
				    onSigFloat(paramFloat);
				    break;
			    }
			    case SIG_SigString: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructStringParcelable.class.getClassLoader());
                
                    StructString[] paramString =  StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("paramString", StructStringParcelable.class));
				    onSigString(paramString);
				    break;
			    }
			    case SIG_SigEnum: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(Enum0Parcelable.class.getClassLoader());
                
                    Enum0[] paramEnum =  Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("paramEnum", Enum0Parcelable.class));
				    onSigEnum(paramEnum);
				    break;
			    }
			    case RPC_FuncBoolResp: {

				    Bundle data = msg.getData();
					data.setClassLoader(StructBoolParcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received StructArrayInterfaceMessageType.RPC_FuncBoolResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_FuncIntResp: {

				    Bundle data = msg.getData();
					data.setClassLoader(StructIntParcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received StructArrayInterfaceMessageType.RPC_FuncIntResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_FuncFloatResp: {

				    Bundle data = msg.getData();
					data.setClassLoader(StructFloatParcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received StructArrayInterfaceMessageType.RPC_FuncFloatResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_FuncStringResp: {

				    Bundle data = msg.getData();
					data.setClassLoader(StructStringParcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received StructArrayInterfaceMessageType.RPC_FuncStringResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_FuncEnumResp: {

				    Bundle data = msg.getData();
					data.setClassLoader(Enum0Parcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received StructArrayInterfaceMessageType.RPC_FuncEnumResp , could not find pending call for " + msg.obj);
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
    public void setPropBool(StructBool[] propBool)
    {
        Log.i(TAG, "request setPropBool called "+ propBool);
        if (! Arrays.equals(m_propBool, propBool))
        {
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.PROP_PropBool.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelableArray("propBool", StructBoolParcelable.wrapArray(propBool));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropBool(StructBool[] propBool)
    {
        Log.i(TAG, "value received from service for PropBool ");
        if (! Arrays.equals(m_propBool, propBool))
        {
            m_propBool = propBool;
            firePropBoolChanged(propBool);
        }

    }

    @Override
    public StructBool[] getPropBool()
    {
        Log.i(TAG, "request getPropBool called, returning local");
        return m_propBool;
    }

  
    @Override
    public void setPropInt(StructInt[] propInt)
    {
        Log.i(TAG, "request setPropInt called "+ propInt);
        if (! Arrays.equals(m_propInt, propInt))
        {
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.PROP_PropInt.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelableArray("propInt", StructIntParcelable.wrapArray(propInt));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropInt(StructInt[] propInt)
    {
        Log.i(TAG, "value received from service for PropInt ");
        if (! Arrays.equals(m_propInt, propInt))
        {
            m_propInt = propInt;
            firePropIntChanged(propInt);
        }

    }

    @Override
    public StructInt[] getPropInt()
    {
        Log.i(TAG, "request getPropInt called, returning local");
        return m_propInt;
    }

  
    @Override
    public void setPropFloat(StructFloat[] propFloat)
    {
        Log.i(TAG, "request setPropFloat called "+ propFloat);
        if (! Arrays.equals(m_propFloat, propFloat))
        {
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.PROP_PropFloat.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelableArray("propFloat", StructFloatParcelable.wrapArray(propFloat));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropFloat(StructFloat[] propFloat)
    {
        Log.i(TAG, "value received from service for PropFloat ");
        if (! Arrays.equals(m_propFloat, propFloat))
        {
            m_propFloat = propFloat;
            firePropFloatChanged(propFloat);
        }

    }

    @Override
    public StructFloat[] getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called, returning local");
        return m_propFloat;
    }

  
    @Override
    public void setPropString(StructString[] propString)
    {
        Log.i(TAG, "request setPropString called "+ propString);
        if (! Arrays.equals(m_propString, propString))
        {
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.PROP_PropString.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelableArray("propString", StructStringParcelable.wrapArray(propString));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropString(StructString[] propString)
    {
        Log.i(TAG, "value received from service for PropString ");
        if (! Arrays.equals(m_propString, propString))
        {
            m_propString = propString;
            firePropStringChanged(propString);
        }

    }

    @Override
    public StructString[] getPropString()
    {
        Log.i(TAG, "request getPropString called, returning local");
        return m_propString;
    }

  
    @Override
    public void setPropEnum(Enum0[] propEnum)
    {
        Log.i(TAG, "request setPropEnum called "+ propEnum);
        if (! Arrays.equals(m_propEnum, propEnum))
        {
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.PROP_PropEnum.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelableArray("propEnum", Enum0Parcelable.wrapArray(propEnum));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropEnum(Enum0[] propEnum)
    {
        Log.i(TAG, "value received from service for PropEnum ");
        if (! Arrays.equals(m_propEnum, propEnum))
        {
            m_propEnum = propEnum;
            firePropEnumChanged(propEnum);
        }

    }

    @Override
    public Enum0[] getPropEnum()
    {
        Log.i(TAG, "request getPropEnum called, returning local");
        return m_propEnum;
    }

  
    // methods

   
    @Override
    public StructBool[] funcBool(StructBool[] paramBool) {
        CompletableFuture<StructBool[]> resFuture = funcBoolAsync(paramBool);
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
    public  CompletableFuture<StructBool[]> funcBoolAsync(StructBool[] paramBool) {

    	Log.i(TAG, "Call on service funcBool  "+ " " + paramBool);
		Message msg = new Message();
		msg.what = StructArrayInterfaceMessageType.RPC_FuncBoolReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelableArray("paramBool", StructBoolParcelable.wrapArray(paramBool));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<StructBool[]>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                Log.w(TAG, "funcBool: received null bundle (service disconnected?)");
                future.completeExceptionally(new RemoteOperationException("service disconnected", RemoteOperationException.ERROR_SERVICE_DISCONNECTED));
                return;
            }
            if (bundle.getBoolean("error", false))
            {
                String errorMessage = bundle.getString("errorMessage", "unknown error");
                int errorCode = bundle.getInt("errorCode", RemoteOperationException.ERROR_UNKNOWN);
                Log.w(TAG, "funcBool failed: " + errorMessage);
                future.completeExceptionally(new RemoteOperationException(errorMessage, errorCode));
                return;
            }
            
            StructBool[] result =  StructBoolParcelable.unwrapArray((StructBoolParcelable[])bundle.getParcelableArray("result", StructBoolParcelable.class));
            Log.v(TAG, "resolve funcBool" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public StructInt[] funcInt(StructInt[] paramInt) {
        CompletableFuture<StructInt[]> resFuture = funcIntAsync(paramInt);
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
    public  CompletableFuture<StructInt[]> funcIntAsync(StructInt[] paramInt) {

    	Log.i(TAG, "Call on service funcInt  "+ " " + paramInt);
		Message msg = new Message();
		msg.what = StructArrayInterfaceMessageType.RPC_FuncIntReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelableArray("paramInt", StructIntParcelable.wrapArray(paramInt));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<StructInt[]>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                Log.w(TAG, "funcInt: received null bundle (service disconnected?)");
                future.completeExceptionally(new RemoteOperationException("service disconnected", RemoteOperationException.ERROR_SERVICE_DISCONNECTED));
                return;
            }
            if (bundle.getBoolean("error", false))
            {
                String errorMessage = bundle.getString("errorMessage", "unknown error");
                int errorCode = bundle.getInt("errorCode", RemoteOperationException.ERROR_UNKNOWN);
                Log.w(TAG, "funcInt failed: " + errorMessage);
                future.completeExceptionally(new RemoteOperationException(errorMessage, errorCode));
                return;
            }
            
            StructInt[] result =  StructIntParcelable.unwrapArray((StructIntParcelable[])bundle.getParcelableArray("result", StructIntParcelable.class));
            Log.v(TAG, "resolve funcInt" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public StructFloat[] funcFloat(StructFloat[] paramFloat) {
        CompletableFuture<StructFloat[]> resFuture = funcFloatAsync(paramFloat);
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
    public  CompletableFuture<StructFloat[]> funcFloatAsync(StructFloat[] paramFloat) {

    	Log.i(TAG, "Call on service funcFloat  "+ " " + paramFloat);
		Message msg = new Message();
		msg.what = StructArrayInterfaceMessageType.RPC_FuncFloatReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelableArray("paramFloat", StructFloatParcelable.wrapArray(paramFloat));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<StructFloat[]>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                Log.w(TAG, "funcFloat: received null bundle (service disconnected?)");
                future.completeExceptionally(new RemoteOperationException("service disconnected", RemoteOperationException.ERROR_SERVICE_DISCONNECTED));
                return;
            }
            if (bundle.getBoolean("error", false))
            {
                String errorMessage = bundle.getString("errorMessage", "unknown error");
                int errorCode = bundle.getInt("errorCode", RemoteOperationException.ERROR_UNKNOWN);
                Log.w(TAG, "funcFloat failed: " + errorMessage);
                future.completeExceptionally(new RemoteOperationException(errorMessage, errorCode));
                return;
            }
            
            StructFloat[] result =  StructFloatParcelable.unwrapArray((StructFloatParcelable[])bundle.getParcelableArray("result", StructFloatParcelable.class));
            Log.v(TAG, "resolve funcFloat" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public StructString[] funcString(StructString[] paramString) {
        CompletableFuture<StructString[]> resFuture = funcStringAsync(paramString);
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
    public  CompletableFuture<StructString[]> funcStringAsync(StructString[] paramString) {

    	Log.i(TAG, "Call on service funcString  "+ " " + paramString);
		Message msg = new Message();
		msg.what = StructArrayInterfaceMessageType.RPC_FuncStringReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelableArray("paramString", StructStringParcelable.wrapArray(paramString));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<StructString[]>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                Log.w(TAG, "funcString: received null bundle (service disconnected?)");
                future.completeExceptionally(new RemoteOperationException("service disconnected", RemoteOperationException.ERROR_SERVICE_DISCONNECTED));
                return;
            }
            if (bundle.getBoolean("error", false))
            {
                String errorMessage = bundle.getString("errorMessage", "unknown error");
                int errorCode = bundle.getInt("errorCode", RemoteOperationException.ERROR_UNKNOWN);
                Log.w(TAG, "funcString failed: " + errorMessage);
                future.completeExceptionally(new RemoteOperationException(errorMessage, errorCode));
                return;
            }
            
            StructString[] result =  StructStringParcelable.unwrapArray((StructStringParcelable[])bundle.getParcelableArray("result", StructStringParcelable.class));
            Log.v(TAG, "resolve funcString" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public Enum0[] funcEnum(Enum0[] paramEnum) {
        CompletableFuture<Enum0[]> resFuture = funcEnumAsync(paramEnum);
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
    public  CompletableFuture<Enum0[]> funcEnumAsync(Enum0[] paramEnum) {

    	Log.i(TAG, "Call on service funcEnum  "+ " " + paramEnum);
		Message msg = new Message();
		msg.what = StructArrayInterfaceMessageType.RPC_FuncEnumReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelableArray("paramEnum", Enum0Parcelable.wrapArray(paramEnum));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Enum0[]>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                Log.w(TAG, "funcEnum: received null bundle (service disconnected?)");
                future.completeExceptionally(new RemoteOperationException("service disconnected", RemoteOperationException.ERROR_SERVICE_DISCONNECTED));
                return;
            }
            if (bundle.getBoolean("error", false))
            {
                String errorMessage = bundle.getString("errorMessage", "unknown error");
                int errorCode = bundle.getInt("errorCode", RemoteOperationException.ERROR_UNKNOWN);
                Log.w(TAG, "funcEnum failed: " + errorMessage);
                future.completeExceptionally(new RemoteOperationException(errorMessage, errorCode));
                return;
            }
            
            Enum0[] result =  Enum0Parcelable.unwrapArray((Enum0Parcelable[])bundle.getParcelableArray("result", Enum0Parcelable.class));
            Log.v(TAG, "resolve funcEnum" + result);
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
    public void onSigBool(StructBool[] paramBool)
    {
        Log.i(TAG, "onSigBool  received from service");
        fireSigBool(paramBool);
    }
    public void onSigInt(StructInt[] paramInt)
    {
        Log.i(TAG, "onSigInt  received from service");
        fireSigInt(paramInt);
    }
    public void onSigFloat(StructFloat[] paramFloat)
    {
        Log.i(TAG, "onSigFloat  received from service");
        fireSigFloat(paramFloat);
    }
    public void onSigString(StructString[] paramString)
    {
        Log.i(TAG, "onSigString  received from service");
        fireSigString(paramString);
    }
    public void onSigEnum(Enum0[] paramEnum)
    {
        Log.i(TAG, "onSigEnum  received from service");
        fireSigEnum(paramEnum);
    }
}
