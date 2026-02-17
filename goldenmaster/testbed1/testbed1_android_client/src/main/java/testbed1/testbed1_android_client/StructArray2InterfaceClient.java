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
import testbed1.testbed1_api.StructBoolWithArray;
import testbed1.testbed1_android_messenger.StructBoolWithArrayParcelable;
import testbed1.testbed1_api.StructEnumWithArray;
import testbed1.testbed1_android_messenger.StructEnumWithArrayParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructFloatWithArray;
import testbed1.testbed1_android_messenger.StructFloatWithArrayParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructIntWithArray;
import testbed1.testbed1_android_messenger.StructIntWithArrayParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;
import testbed1.testbed1_api.StructStringWithArray;
import testbed1.testbed1_android_messenger.StructStringWithArrayParcelable;

import testbed1.testbed1_api.IStructArray2InterfaceEventListener;
import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_api.AbstractStructArray2Interface;
import testbed1.testbed1_android_messenger.StructArray2InterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;


public class StructArray2InterfaceClient extends AbstractStructArray2Interface implements ServiceConnection
{
	private static final String TAG = "StructArray2InterfaceClient";

	private final Context mApplicationContext;

    private boolean mIsBoundToService = false;
    private Messenger mServiceMessenger = null;
    private Messenger mClientMessenger = null;
    private String mConnectionId;
    private ClientHandler mClientHandler = new ClientHandler();

    private final Map<Integer, Consumer<Bundle>> mpendingCalls = new ConcurrentHashMap<>();
    AtomicInteger callIdsGetter = new AtomicInteger(0);
    private StructBoolWithArray m_propBool = new StructBoolWithArray();
    private StructIntWithArray m_propInt = new StructIntWithArray();
    private StructFloatWithArray m_propFloat = new StructFloatWithArray();
    private StructStringWithArray m_propString = new StructStringWithArray();
    private StructEnumWithArray m_propEnum = new StructEnumWithArray();


	public StructArray2InterfaceClient(Context applicationContext, String connectionId)
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
     * Binds to a running service of type StructArray2InterfaceServiceAdapter.
     *
     * @return true if binding was successful, false otherwise.
     */
    public boolean bindToService(String packageName)
    {
        Intent intent = new Intent();
        intent.setClassName(packageName, "testbed1.testbed1_android_service.StructArray2InterfaceServiceAdapter");
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
            Message msg = Message.obtain(null, StructArray2InterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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
            Message msg = Message.obtain(null, StructArray2InterfaceMessageType.REGISTER_CLIENT.ordinal());
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

		    switch (StructArray2InterfaceMessageType.fromInteger(msg.what))
		    {
                case INIT:
                {
                    Bundle data = msg.getData();
                    
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(StructBoolWithArrayParcelable.class.getClassLoader());
			        
                    
			        StructBoolWithArray propBool = data.getParcelable("propBool", StructBoolWithArrayParcelable.class).getStructBoolWithArray();
				    onPropBool(propBool);
                    
			        StructIntWithArray propInt = data.getParcelable("propInt", StructIntWithArrayParcelable.class).getStructIntWithArray();
				    onPropInt(propInt);
                    
			        StructFloatWithArray propFloat = data.getParcelable("propFloat", StructFloatWithArrayParcelable.class).getStructFloatWithArray();
				    onPropFloat(propFloat);
                    
			        StructStringWithArray propString = data.getParcelable("propString", StructStringWithArrayParcelable.class).getStructStringWithArray();
				    onPropString(propString);
                    
			        StructEnumWithArray propEnum = data.getParcelable("propEnum", StructEnumWithArrayParcelable.class).getStructEnumWithArray();
				    onPropEnum(propEnum);

                    break;
                }
			    case SET_PropBool:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructBoolWithArrayParcelable.class.getClassLoader());

                    
			        StructBoolWithArray propBool = data.getParcelable("propBool", StructBoolWithArrayParcelable.class).getStructBoolWithArray();

				    onPropBool(propBool);
				    break;
			    }
			    case SET_PropInt:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructIntWithArrayParcelable.class.getClassLoader());

                    
			        StructIntWithArray propInt = data.getParcelable("propInt", StructIntWithArrayParcelable.class).getStructIntWithArray();

				    onPropInt(propInt);
				    break;
			    }
			    case SET_PropFloat:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructFloatWithArrayParcelable.class.getClassLoader());

                    
			        StructFloatWithArray propFloat = data.getParcelable("propFloat", StructFloatWithArrayParcelable.class).getStructFloatWithArray();

				    onPropFloat(propFloat);
				    break;
			    }
			    case SET_PropString:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructStringWithArrayParcelable.class.getClassLoader());

                    
			        StructStringWithArray propString = data.getParcelable("propString", StructStringWithArrayParcelable.class).getStructStringWithArray();

				    onPropString(propString);
				    break;
			    }
			    case SET_PropEnum:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(StructEnumWithArrayParcelable.class.getClassLoader());

                    
			        StructEnumWithArray propEnum = data.getParcelable("propEnum", StructEnumWithArrayParcelable.class).getStructEnumWithArray();

				    onPropEnum(propEnum);
				    break;
			    }
			    // TODO params may be different structs from different modules, there should be a custom class loader 
			    // with a list of class loaders required for this message
			    // IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
			    case SIG_SigBool: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructBoolWithArrayParcelable.class.getClassLoader());
                
			        StructBoolWithArray paramBool = data.getParcelable("paramBool", StructBoolWithArrayParcelable.class).getStructBoolWithArray();
				    onSigBool(paramBool);
				    break;
			    }
			    case SIG_SigInt: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructIntWithArrayParcelable.class.getClassLoader());
                
			        StructIntWithArray paramInt = data.getParcelable("paramInt", StructIntWithArrayParcelable.class).getStructIntWithArray();
				    onSigInt(paramInt);
				    break;
			    }
			    case SIG_SigFloat: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructFloatWithArrayParcelable.class.getClassLoader());
                
			        StructFloatWithArray paramFloat = data.getParcelable("paramFloat", StructFloatWithArrayParcelable.class).getStructFloatWithArray();
				    onSigFloat(paramFloat);
				    break;
			    }
			    case SIG_SigString: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(StructStringWithArrayParcelable.class.getClassLoader());
                
			        StructStringWithArray paramString = data.getParcelable("paramString", StructStringWithArrayParcelable.class).getStructStringWithArray();
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
                        Log.v(TAG, "received StructArray2InterfaceMessageType.RPC_FuncBoolResp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received StructArray2InterfaceMessageType.RPC_FuncIntResp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received StructArray2InterfaceMessageType.RPC_FuncFloatResp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received StructArray2InterfaceMessageType.RPC_FuncStringResp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received StructArray2InterfaceMessageType.RPC_FuncEnumResp , could not find pending call for " + msg.obj);
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
    public void setPropBool(StructBoolWithArray propBool)
    {
        Log.i(TAG, "request setPropBool called "+ propBool);
        if ( (m_propBool != null && ! m_propBool.equals(propBool))
        || (m_propBool == null && propBool != null ))
        {
			Message msg = new Message();
			msg.what = StructArray2InterfaceMessageType.PROP_PropBool.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("propBool", new StructBoolWithArrayParcelable(propBool));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropBool(StructBoolWithArray propBool)
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
    public StructBoolWithArray getPropBool()
    {
        Log.i(TAG, "request getPropBool called, returning local");
        return m_propBool;
    }

  
    @Override
    public void setPropInt(StructIntWithArray propInt)
    {
        Log.i(TAG, "request setPropInt called "+ propInt);
        if ( (m_propInt != null && ! m_propInt.equals(propInt))
        || (m_propInt == null && propInt != null ))
        {
			Message msg = new Message();
			msg.what = StructArray2InterfaceMessageType.PROP_PropInt.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("propInt", new StructIntWithArrayParcelable(propInt));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropInt(StructIntWithArray propInt)
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
    public StructIntWithArray getPropInt()
    {
        Log.i(TAG, "request getPropInt called, returning local");
        return m_propInt;
    }

  
    @Override
    public void setPropFloat(StructFloatWithArray propFloat)
    {
        Log.i(TAG, "request setPropFloat called "+ propFloat);
        if ( (m_propFloat != null && ! m_propFloat.equals(propFloat))
        || (m_propFloat == null && propFloat != null ))
        {
			Message msg = new Message();
			msg.what = StructArray2InterfaceMessageType.PROP_PropFloat.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("propFloat", new StructFloatWithArrayParcelable(propFloat));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropFloat(StructFloatWithArray propFloat)
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
    public StructFloatWithArray getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called, returning local");
        return m_propFloat;
    }

  
    @Override
    public void setPropString(StructStringWithArray propString)
    {
        Log.i(TAG, "request setPropString called "+ propString);
        if ( (m_propString != null && ! m_propString.equals(propString))
        || (m_propString == null && propString != null ))
        {
			Message msg = new Message();
			msg.what = StructArray2InterfaceMessageType.PROP_PropString.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("propString", new StructStringWithArrayParcelable(propString));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropString(StructStringWithArray propString)
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
    public StructStringWithArray getPropString()
    {
        Log.i(TAG, "request getPropString called, returning local");
        return m_propString;
    }

  
    @Override
    public void setPropEnum(StructEnumWithArray propEnum)
    {
        Log.i(TAG, "request setPropEnum called "+ propEnum);
        if ( (m_propEnum != null && ! m_propEnum.equals(propEnum))
        || (m_propEnum == null && propEnum != null ))
        {
			Message msg = new Message();
			msg.what = StructArray2InterfaceMessageType.PROP_PropEnum.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("propEnum", new StructEnumWithArrayParcelable(propEnum));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropEnum(StructEnumWithArray propEnum)
    {
        Log.i(TAG, "value received from service for PropEnum ");
        if ( (m_propEnum != null && ! m_propEnum.equals(propEnum))
        || (m_propEnum == null && propEnum != null ))
        {
            m_propEnum = propEnum;
            firePropEnumChanged(propEnum);
        }

    }

    @Override
    public StructEnumWithArray getPropEnum()
    {
        Log.i(TAG, "request getPropEnum called, returning local");
        return m_propEnum;
    }

  
    // methods

   
    @Override
    public StructBool[] funcBool(StructBoolWithArray paramBool) {
        CompletableFuture<StructBool[]> resFuture = funcBoolAsync(paramBool);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<StructBool[]> funcBoolAsync(StructBoolWithArray paramBool) {

    	Log.i(TAG, "Call on service funcBool  "+ " " + paramBool);
		Message msg = new Message();
		msg.what = StructArray2InterfaceMessageType.RPC_FuncBoolReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("paramBool", new StructBoolWithArrayParcelable(paramBool));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<StructBool[]>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving funcBool with null");
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
    public StructInt[] funcInt(StructIntWithArray paramInt) {
        CompletableFuture<StructInt[]> resFuture = funcIntAsync(paramInt);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<StructInt[]> funcIntAsync(StructIntWithArray paramInt) {

    	Log.i(TAG, "Call on service funcInt  "+ " " + paramInt);
		Message msg = new Message();
		msg.what = StructArray2InterfaceMessageType.RPC_FuncIntReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("paramInt", new StructIntWithArrayParcelable(paramInt));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<StructInt[]>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving funcInt with null");
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
    public StructFloat[] funcFloat(StructFloatWithArray paramFloat) {
        CompletableFuture<StructFloat[]> resFuture = funcFloatAsync(paramFloat);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<StructFloat[]> funcFloatAsync(StructFloatWithArray paramFloat) {

    	Log.i(TAG, "Call on service funcFloat  "+ " " + paramFloat);
		Message msg = new Message();
		msg.what = StructArray2InterfaceMessageType.RPC_FuncFloatReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("paramFloat", new StructFloatWithArrayParcelable(paramFloat));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<StructFloat[]>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving funcFloat with null");
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
    public StructString[] funcString(StructStringWithArray paramString) {
        CompletableFuture<StructString[]> resFuture = funcStringAsync(paramString);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<StructString[]> funcStringAsync(StructStringWithArray paramString) {

    	Log.i(TAG, "Call on service funcString  "+ " " + paramString);
		Message msg = new Message();
		msg.what = StructArray2InterfaceMessageType.RPC_FuncStringReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("paramString", new StructStringWithArrayParcelable(paramString));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<StructString[]>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving funcString with null");
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
    public Enum0[] funcEnum(StructEnumWithArray paramEnum) {
        CompletableFuture<Enum0[]> resFuture = funcEnumAsync(paramEnum);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Enum0[]> funcEnumAsync(StructEnumWithArray paramEnum) {

    	Log.i(TAG, "Call on service funcEnum  "+ " " + paramEnum);
		Message msg = new Message();
		msg.what = StructArray2InterfaceMessageType.RPC_FuncEnumReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("paramEnum", new StructEnumWithArrayParcelable(paramEnum));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Enum0[]>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving funcEnum with null");
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
    public void onSigBool(StructBoolWithArray paramBool)
    {
        Log.i(TAG, "onSigBool  received from service");
        fireSigBool(paramBool);
    }
    public void onSigInt(StructIntWithArray paramInt)
    {
        Log.i(TAG, "onSigInt  received from service");
        fireSigInt(paramInt);
    }
    public void onSigFloat(StructFloatWithArray paramFloat)
    {
        Log.i(TAG, "onSigFloat  received from service");
        fireSigFloat(paramFloat);
    }
    public void onSigString(StructStringWithArray paramString)
    {
        Log.i(TAG, "onSigString  received from service");
        fireSigString(paramString);
    }
}
