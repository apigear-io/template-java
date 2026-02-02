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
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_android_messenger.StructBoolParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;

import testbed1.testbed1_api.IStructInterfaceEventListener;
import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_api.AbstractStructInterface;
import testbed1.testbed1_android_messenger.StructInterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;


public class StructInterfaceClient extends AbstractStructInterface implements ServiceConnection
{
	private static final String TAG = "StructInterfaceClient";

	private final Context mApplicationContext;

    private boolean mIsBoundToService = false;
    private Messenger mServiceMessenger = null;
    private Messenger mClientMessenger = null;
    private String mConnectionId;
    private ClientHandler mClientHandler = new ClientHandler();

    private final Map<Integer, Consumer<Bundle>> mpendingCalls = new ConcurrentHashMap<>();
    AtomicInteger callIdsGetter = new AtomicInteger(0);
    private StructBool m_propBool = new StructBool();
    private StructInt m_propInt = new StructInt();
    private StructFloat m_propFloat = new StructFloat();
    private StructString m_propString = new StructString();


	public StructInterfaceClient(Context applicationContext, String connectionId)
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
     * Binds to a running service of type StructInterfaceServiceAdapter.
     *
     * @return true if binding was successful, false otherwise.
     */
    public boolean bindToService(String packageName)
    {
        Intent intent = new Intent();
        intent.setClassName(packageName, "testbed1.testbed1_android_service.StructInterfaceServiceAdapter");
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
            Message msg = Message.obtain(null, StructInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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
            Message msg = Message.obtain(null, StructInterfaceMessageType.REGISTER_CLIENT.ordinal());
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

		    switch (StructInterfaceMessageType.fromInteger(msg.what))
		    {
                case INIT:
                {
                    Bundle data = msg.getData();
                    
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(StructBoolParcelable.class.getClassLoader());
			        
                    
			        StructBool propBool = data.getParcelable("propBool", StructBoolParcelable.class).getStructBool();
				    onPropBool(propBool);
                    
			        StructInt propInt = data.getParcelable("propInt", StructIntParcelable.class).getStructInt();
				    onPropInt(propInt);
                    
			        StructFloat propFloat = data.getParcelable("propFloat", StructFloatParcelable.class).getStructFloat();
				    onPropFloat(propFloat);
                    
			        StructString propString = data.getParcelable("propString", StructStringParcelable.class).getStructString();
				    onPropString(propString);

                    break;
                }
			    case SET_PropBool:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructBoolParcelable.class.getClassLoader());

                    
			        StructBool propBool = data.getParcelable("propBool", StructBoolParcelable.class).getStructBool();

				    onPropBool(propBool);
				    break;
			    }
			    case SET_PropInt:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructIntParcelable.class.getClassLoader());

                    
			        StructInt propInt = data.getParcelable("propInt", StructIntParcelable.class).getStructInt();

				    onPropInt(propInt);
				    break;
			    }
			    case SET_PropFloat:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructFloatParcelable.class.getClassLoader());

                    
			        StructFloat propFloat = data.getParcelable("propFloat", StructFloatParcelable.class).getStructFloat();

				    onPropFloat(propFloat);
				    break;
			    }
			    case SET_PropString:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructStringParcelable.class.getClassLoader());

                    
			        StructString propString = data.getParcelable("propString", StructStringParcelable.class).getStructString();

				    onPropString(propString);
				    break;
			    }
			    // TODO params may be different structs from different modules, there should be a custom class loader 
			    // with a list of class loaders required for this message
			    // IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
			    case SIG_SigBool: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructBoolParcelable.class.getClassLoader());
                
			        StructBool paramBool = data.getParcelable("paramBool", StructBoolParcelable.class).getStructBool();
				    onSigBool(paramBool);
				    break;
			    }
			    case SIG_SigInt: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructIntParcelable.class.getClassLoader());
                
			        StructInt paramInt = data.getParcelable("paramInt", StructIntParcelable.class).getStructInt();
				    onSigInt(paramInt);
				    break;
			    }
			    case SIG_SigFloat: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructFloatParcelable.class.getClassLoader());
                
			        StructFloat paramFloat = data.getParcelable("paramFloat", StructFloatParcelable.class).getStructFloat();
				    onSigFloat(paramFloat);
				    break;
			    }
			    case SIG_SigString: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructStringParcelable.class.getClassLoader());
                
			        StructString paramString = data.getParcelable("paramString", StructStringParcelable.class).getStructString();
				    onSigString(paramString);
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
                        Log.v(TAG, "received StructInterfaceMessageType.RPC_FuncBoolResp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received StructInterfaceMessageType.RPC_FuncIntResp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received StructInterfaceMessageType.RPC_FuncFloatResp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received StructInterfaceMessageType.RPC_FuncStringResp , could not find pending call for " + msg.obj);
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
    public void setPropBool(StructBool propBool)
    {
        Log.i(TAG, "request setPropBool called "+ propBool);
        if ( (m_propBool != null && ! m_propBool.equals(propBool))
        || (m_propBool == null && propBool != null ))
        {
			Message msg = new Message();
			msg.what = StructInterfaceMessageType.PROP_PropBool.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("propBool", new StructBoolParcelable(propBool));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropBool(StructBool propBool)
    {
        Log.i(TAG, "value received from service for PropBool ");
        if ( (m_propBool != null && ! m_propBool.equals(propBool))
        || (m_propBool == null && propBool != null ))
        {
            m_propBool = propBool;
            firePropBoolChanged(propBool);
        }

    }

    @Override
    public StructBool getPropBool()
    {
        Log.i(TAG, "request getPropBool called, returning local");
        return m_propBool;
    }

  
    @Override
    public void setPropInt(StructInt propInt)
    {
        Log.i(TAG, "request setPropInt called "+ propInt);
        if ( (m_propInt != null && ! m_propInt.equals(propInt))
        || (m_propInt == null && propInt != null ))
        {
			Message msg = new Message();
			msg.what = StructInterfaceMessageType.PROP_PropInt.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("propInt", new StructIntParcelable(propInt));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropInt(StructInt propInt)
    {
        Log.i(TAG, "value received from service for PropInt ");
        if ( (m_propInt != null && ! m_propInt.equals(propInt))
        || (m_propInt == null && propInt != null ))
        {
            m_propInt = propInt;
            firePropIntChanged(propInt);
        }

    }

    @Override
    public StructInt getPropInt()
    {
        Log.i(TAG, "request getPropInt called, returning local");
        return m_propInt;
    }

  
    @Override
    public void setPropFloat(StructFloat propFloat)
    {
        Log.i(TAG, "request setPropFloat called "+ propFloat);
        if ( (m_propFloat != null && ! m_propFloat.equals(propFloat))
        || (m_propFloat == null && propFloat != null ))
        {
			Message msg = new Message();
			msg.what = StructInterfaceMessageType.PROP_PropFloat.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("propFloat", new StructFloatParcelable(propFloat));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropFloat(StructFloat propFloat)
    {
        Log.i(TAG, "value received from service for PropFloat ");
        if ( (m_propFloat != null && ! m_propFloat.equals(propFloat))
        || (m_propFloat == null && propFloat != null ))
        {
            m_propFloat = propFloat;
            firePropFloatChanged(propFloat);
        }

    }

    @Override
    public StructFloat getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called, returning local");
        return m_propFloat;
    }

  
    @Override
    public void setPropString(StructString propString)
    {
        Log.i(TAG, "request setPropString called "+ propString);
        if ( (m_propString != null && ! m_propString.equals(propString))
        || (m_propString == null && propString != null ))
        {
			Message msg = new Message();
			msg.what = StructInterfaceMessageType.PROP_PropString.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("propString", new StructStringParcelable(propString));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropString(StructString propString)
    {
        Log.i(TAG, "value received from service for PropString ");
        if ( (m_propString != null && ! m_propString.equals(propString))
        || (m_propString == null && propString != null ))
        {
            m_propString = propString;
            firePropStringChanged(propString);
        }

    }

    @Override
    public StructString getPropString()
    {
        Log.i(TAG, "request getPropString called, returning local");
        return m_propString;
    }

  
    // methods

   
    @Override
    public StructBool funcBool(StructBool paramBool) {
        CompletableFuture<StructBool> resFuture = funcBoolAsync(paramBool);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<StructBool> funcBoolAsync(StructBool paramBool) {

    	Log.i(TAG, "Call on service funcBool  "+ " " + paramBool);
		Message msg = new Message();
		msg.what = StructInterfaceMessageType.RPC_FuncBoolReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("paramBool", new StructBoolParcelable(paramBool));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<StructBool>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            
		    StructBool result = bundle.getParcelable("result", StructBoolParcelable.class).getStructBool();
            Log.v(TAG, "resolve funcBool" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public StructInt funcInt(StructInt paramInt) {
        CompletableFuture<StructInt> resFuture = funcIntAsync(paramInt);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<StructInt> funcIntAsync(StructInt paramInt) {

    	Log.i(TAG, "Call on service funcInt  "+ " " + paramInt);
		Message msg = new Message();
		msg.what = StructInterfaceMessageType.RPC_FuncIntReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("paramInt", new StructIntParcelable(paramInt));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<StructInt>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            
		    StructInt result = bundle.getParcelable("result", StructIntParcelable.class).getStructInt();
            Log.v(TAG, "resolve funcInt" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public StructFloat funcFloat(StructFloat paramFloat) {
        CompletableFuture<StructFloat> resFuture = funcFloatAsync(paramFloat);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<StructFloat> funcFloatAsync(StructFloat paramFloat) {

    	Log.i(TAG, "Call on service funcFloat  "+ " " + paramFloat);
		Message msg = new Message();
		msg.what = StructInterfaceMessageType.RPC_FuncFloatReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("paramFloat", new StructFloatParcelable(paramFloat));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<StructFloat>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            
		    StructFloat result = bundle.getParcelable("result", StructFloatParcelable.class).getStructFloat();
            Log.v(TAG, "resolve funcFloat" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public StructString funcString(StructString paramString) {
        CompletableFuture<StructString> resFuture = funcStringAsync(paramString);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<StructString> funcStringAsync(StructString paramString) {

    	Log.i(TAG, "Call on service funcString  "+ " " + paramString);
		Message msg = new Message();
		msg.what = StructInterfaceMessageType.RPC_FuncStringReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("paramString", new StructStringParcelable(paramString));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<StructString>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            
		    StructString result = bundle.getParcelable("result", StructStringParcelable.class).getStructString();
            Log.v(TAG, "resolve funcString" + result);
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
    public void onSigBool(StructBool paramBool)
    {
        Log.i(TAG, "onSigBool  received from service");
        fireSigBool(paramBool);
    }
    public void onSigInt(StructInt paramInt)
    {
        Log.i(TAG, "onSigInt  received from service");
        fireSigInt(paramInt);
    }
    public void onSigFloat(StructFloat paramFloat)
    {
        Log.i(TAG, "onSigFloat  received from service");
        fireSigFloat(paramFloat);
    }
    public void onSigString(StructString paramString)
    {
        Log.i(TAG, "onSigString  received from service");
        fireSigString(paramString);
    }
}
