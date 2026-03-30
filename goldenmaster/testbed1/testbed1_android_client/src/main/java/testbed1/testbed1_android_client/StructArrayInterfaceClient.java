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
import testbed1.testbed1_android_messenger.Conversions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;


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
    private List<StructBool> m_propBool = new ArrayList<>();
    private List<StructInt> m_propInt = new ArrayList<>();
    private List<StructFloat> m_propFloat = new ArrayList<>();
    private List<StructString> m_propString = new ArrayList<>();
    private List<Enum0> m_propEnum = new ArrayList<>();


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
			        
                    
                    List<StructBool> propBool = Conversions.toList(StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("propBool", StructBoolParcelable.class)));
				    onPropBool(propBool);
                    
                    List<StructInt> propInt = Conversions.toList(StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("propInt", StructIntParcelable.class)));
				    onPropInt(propInt);
                    
                    List<StructFloat> propFloat = Conversions.toList(StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("propFloat", StructFloatParcelable.class)));
				    onPropFloat(propFloat);
                    
                    List<StructString> propString = Conversions.toList(StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("propString", StructStringParcelable.class)));
				    onPropString(propString);
                    
                    List<Enum0> propEnum = Conversions.toList(Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("propEnum", Enum0Parcelable.class)));
				    onPropEnum(propEnum);

                    break;
                }
			    case SET_PropBool:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructBoolParcelable.class.getClassLoader());

                    
                    List<StructBool> propBool = Conversions.toList(StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("propBool", StructBoolParcelable.class)));

				    onPropBool(propBool);
				    break;
			    }
			    case SET_PropInt:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructIntParcelable.class.getClassLoader());

                    
                    List<StructInt> propInt = Conversions.toList(StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("propInt", StructIntParcelable.class)));

				    onPropInt(propInt);
				    break;
			    }
			    case SET_PropFloat:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructFloatParcelable.class.getClassLoader());

                    
                    List<StructFloat> propFloat = Conversions.toList(StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("propFloat", StructFloatParcelable.class)));

				    onPropFloat(propFloat);
				    break;
			    }
			    case SET_PropString:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructStringParcelable.class.getClassLoader());

                    
                    List<StructString> propString = Conversions.toList(StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("propString", StructStringParcelable.class)));

				    onPropString(propString);
				    break;
			    }
			    case SET_PropEnum:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(Enum0Parcelable.class.getClassLoader());

                    
                    List<Enum0> propEnum = Conversions.toList(Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("propEnum", Enum0Parcelable.class)));

				    onPropEnum(propEnum);
				    break;
			    }
			    // TODO params may be different structs from different modules, there should be a custom class loader
			    // with a list of class loaders required for this message
			    // IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
			    case SIG_SigBool: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructBoolParcelable.class.getClassLoader());
                
                    List<StructBool> paramBool = Conversions.toList(StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("paramBool", StructBoolParcelable.class)));
				    onSigBool(paramBool);
				    break;
			    }
			    case SIG_SigInt: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructIntParcelable.class.getClassLoader());
                
                    List<StructInt> paramInt = Conversions.toList(StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("paramInt", StructIntParcelable.class)));
				    onSigInt(paramInt);
				    break;
			    }
			    case SIG_SigFloat: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructFloatParcelable.class.getClassLoader());
                
                    List<StructFloat> paramFloat = Conversions.toList(StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("paramFloat", StructFloatParcelable.class)));
				    onSigFloat(paramFloat);
				    break;
			    }
			    case SIG_SigString: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructStringParcelable.class.getClassLoader());
                
                    List<StructString> paramString = Conversions.toList(StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("paramString", StructStringParcelable.class)));
				    onSigString(paramString);
				    break;
			    }
			    case SIG_SigEnum: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(Enum0Parcelable.class.getClassLoader());
                
                    List<Enum0> paramEnum = Conversions.toList(Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("paramEnum", Enum0Parcelable.class)));
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
    public void setPropBool(List<StructBool> propBool)
    {
        Log.i(TAG, "request setPropBool called "+ propBool);
        if (!m_propBool.equals(propBool))
        {
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.PROP_PropBool.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelableArray("propBool", StructBoolParcelable.wrapArray(Conversions.toArray(propBool, new StructBool[0])));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropBool(List<StructBool> propBool)
    {
        Log.i(TAG, "value received from service for PropBool ");
        if (!m_propBool.equals(propBool))
        {
            m_propBool = new ArrayList<>(propBool);
            firePropBoolChanged(propBool);
        }

    }

    @Override
    public List<StructBool> getPropBool()
    {
        Log.i(TAG, "request getPropBool called, returning local");
        return new ArrayList<>(m_propBool);
    }

  
    @Override
    public void setPropInt(List<StructInt> propInt)
    {
        Log.i(TAG, "request setPropInt called "+ propInt);
        if (!m_propInt.equals(propInt))
        {
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.PROP_PropInt.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelableArray("propInt", StructIntParcelable.wrapArray(Conversions.toArray(propInt, new StructInt[0])));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropInt(List<StructInt> propInt)
    {
        Log.i(TAG, "value received from service for PropInt ");
        if (!m_propInt.equals(propInt))
        {
            m_propInt = new ArrayList<>(propInt);
            firePropIntChanged(propInt);
        }

    }

    @Override
    public List<StructInt> getPropInt()
    {
        Log.i(TAG, "request getPropInt called, returning local");
        return new ArrayList<>(m_propInt);
    }

  
    @Override
    public void setPropFloat(List<StructFloat> propFloat)
    {
        Log.i(TAG, "request setPropFloat called "+ propFloat);
        if (!m_propFloat.equals(propFloat))
        {
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.PROP_PropFloat.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelableArray("propFloat", StructFloatParcelable.wrapArray(Conversions.toArray(propFloat, new StructFloat[0])));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropFloat(List<StructFloat> propFloat)
    {
        Log.i(TAG, "value received from service for PropFloat ");
        if (!m_propFloat.equals(propFloat))
        {
            m_propFloat = new ArrayList<>(propFloat);
            firePropFloatChanged(propFloat);
        }

    }

    @Override
    public List<StructFloat> getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called, returning local");
        return new ArrayList<>(m_propFloat);
    }

  
    @Override
    public void setPropString(List<StructString> propString)
    {
        Log.i(TAG, "request setPropString called "+ propString);
        if (!m_propString.equals(propString))
        {
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.PROP_PropString.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelableArray("propString", StructStringParcelable.wrapArray(Conversions.toArray(propString, new StructString[0])));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropString(List<StructString> propString)
    {
        Log.i(TAG, "value received from service for PropString ");
        if (!m_propString.equals(propString))
        {
            m_propString = new ArrayList<>(propString);
            firePropStringChanged(propString);
        }

    }

    @Override
    public List<StructString> getPropString()
    {
        Log.i(TAG, "request getPropString called, returning local");
        return new ArrayList<>(m_propString);
    }

  
    @Override
    public void setPropEnum(List<Enum0> propEnum)
    {
        Log.i(TAG, "request setPropEnum called "+ propEnum);
        if (!m_propEnum.equals(propEnum))
        {
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.PROP_PropEnum.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelableArray("propEnum", Enum0Parcelable.wrapArray(Conversions.toArray(propEnum, new Enum0[0])));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropEnum(List<Enum0> propEnum)
    {
        Log.i(TAG, "value received from service for PropEnum ");
        if (!m_propEnum.equals(propEnum))
        {
            m_propEnum = new ArrayList<>(propEnum);
            firePropEnumChanged(propEnum);
        }

    }

    @Override
    public List<Enum0> getPropEnum()
    {
        Log.i(TAG, "request getPropEnum called, returning local");
        return new ArrayList<>(m_propEnum);
    }

  
    // methods


    @Override
    public List<StructBool> funcBool(List<StructBool> paramBool) {
        CompletableFuture<List<StructBool>> resFuture = funcBoolAsync(paramBool);
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
    public  CompletableFuture<List<StructBool>> funcBoolAsync(List<StructBool> paramBool) {

    	Log.i(TAG, "Call on service funcBool  "+ " " + paramBool);
		Message msg = new Message();
		msg.what = StructArrayInterfaceMessageType.RPC_FuncBoolReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelableArray("paramBool", StructBoolParcelable.wrapArray(Conversions.toArray(paramBool, new StructBool[0])));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<List<StructBool>>  future = new CompletableFuture<>();
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
            
            List<StructBool> result = Conversions.toList(StructBoolParcelable.unwrapArray((StructBoolParcelable[])bundle.getParcelableArray("result", StructBoolParcelable.class)));
            Log.v(TAG, "resolve funcBool" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }


    @Override
    public List<StructInt> funcInt(List<StructInt> paramInt) {
        CompletableFuture<List<StructInt>> resFuture = funcIntAsync(paramInt);
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
    public  CompletableFuture<List<StructInt>> funcIntAsync(List<StructInt> paramInt) {

    	Log.i(TAG, "Call on service funcInt  "+ " " + paramInt);
		Message msg = new Message();
		msg.what = StructArrayInterfaceMessageType.RPC_FuncIntReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelableArray("paramInt", StructIntParcelable.wrapArray(Conversions.toArray(paramInt, new StructInt[0])));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<List<StructInt>>  future = new CompletableFuture<>();
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
            
            List<StructInt> result = Conversions.toList(StructIntParcelable.unwrapArray((StructIntParcelable[])bundle.getParcelableArray("result", StructIntParcelable.class)));
            Log.v(TAG, "resolve funcInt" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }


    @Override
    public List<StructFloat> funcFloat(List<StructFloat> paramFloat) {
        CompletableFuture<List<StructFloat>> resFuture = funcFloatAsync(paramFloat);
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
    public  CompletableFuture<List<StructFloat>> funcFloatAsync(List<StructFloat> paramFloat) {

    	Log.i(TAG, "Call on service funcFloat  "+ " " + paramFloat);
		Message msg = new Message();
		msg.what = StructArrayInterfaceMessageType.RPC_FuncFloatReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelableArray("paramFloat", StructFloatParcelable.wrapArray(Conversions.toArray(paramFloat, new StructFloat[0])));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<List<StructFloat>>  future = new CompletableFuture<>();
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
            
            List<StructFloat> result = Conversions.toList(StructFloatParcelable.unwrapArray((StructFloatParcelable[])bundle.getParcelableArray("result", StructFloatParcelable.class)));
            Log.v(TAG, "resolve funcFloat" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }


    @Override
    public List<StructString> funcString(List<StructString> paramString) {
        CompletableFuture<List<StructString>> resFuture = funcStringAsync(paramString);
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
    public  CompletableFuture<List<StructString>> funcStringAsync(List<StructString> paramString) {

    	Log.i(TAG, "Call on service funcString  "+ " " + paramString);
		Message msg = new Message();
		msg.what = StructArrayInterfaceMessageType.RPC_FuncStringReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelableArray("paramString", StructStringParcelable.wrapArray(Conversions.toArray(paramString, new StructString[0])));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<List<StructString>>  future = new CompletableFuture<>();
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
            
            List<StructString> result = Conversions.toList(StructStringParcelable.unwrapArray((StructStringParcelable[])bundle.getParcelableArray("result", StructStringParcelable.class)));
            Log.v(TAG, "resolve funcString" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }


    @Override
    public List<Enum0> funcEnum(List<Enum0> paramEnum) {
        CompletableFuture<List<Enum0>> resFuture = funcEnumAsync(paramEnum);
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
    public  CompletableFuture<List<Enum0>> funcEnumAsync(List<Enum0> paramEnum) {

    	Log.i(TAG, "Call on service funcEnum  "+ " " + paramEnum);
		Message msg = new Message();
		msg.what = StructArrayInterfaceMessageType.RPC_FuncEnumReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelableArray("paramEnum", Enum0Parcelable.wrapArray(Conversions.toArray(paramEnum, new Enum0[0])));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<List<Enum0>>  future = new CompletableFuture<>();
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
            
            List<Enum0> result = Conversions.toList(Enum0Parcelable.unwrapArray((Enum0Parcelable[])bundle.getParcelableArray("result", Enum0Parcelable.class)));
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
    public void onSigBool(List<StructBool> paramBool)
    {
        Log.i(TAG, "onSigBool  received from service");
        fireSigBool(paramBool);
    }
    public void onSigInt(List<StructInt> paramInt)
    {
        Log.i(TAG, "onSigInt  received from service");
        fireSigInt(paramInt);
    }
    public void onSigFloat(List<StructFloat> paramFloat)
    {
        Log.i(TAG, "onSigFloat  received from service");
        fireSigFloat(paramFloat);
    }
    public void onSigString(List<StructString> paramString)
    {
        Log.i(TAG, "onSigString  received from service");
        fireSigString(paramString);
    }
    public void onSigEnum(List<Enum0> paramEnum)
    {
        Log.i(TAG, "onSigEnum  received from service");
        fireSigEnum(paramEnum);
    }
}
