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

import tbSimple.tbSimple_api.ISimpleInterfaceEventListener;
import tbSimple.tbSimple_api.ISimpleInterface;
import tbSimple.tbSimple_api.AbstractSimpleInterface;
import tbSimple.tbSimple_android_messenger.SimpleInterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;


public class SimpleInterfaceClient extends AbstractSimpleInterface implements ServiceConnection
{
	private static final String TAG = "SimpleInterfaceClient";

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
    private int m_propInt32 = 0;
    private long m_propInt64 = 0L;
    private float m_propFloat = 0.0f;
    private float m_propFloat32 = 0.0f;
    private double m_propFloat64 = 0.0;
    private String m_propString = new String();


	public SimpleInterfaceClient(Context applicationContext, String connectionId)
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
     * Binds to a running service of type SimpleInterfaceServiceAdapter.
     *
     * @return true if binding was successful, false otherwise.
     */
    public boolean bindToService(String packageName)
    {
        Intent intent = new Intent();
        intent.setClassName(packageName, "tbSimple.tbSimple_android_service.SimpleInterfaceServiceAdapter");
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
            Message msg = Message.obtain(null, SimpleInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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
            Message msg = Message.obtain(null, SimpleInterfaceMessageType.REGISTER_CLIENT.ordinal());
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

		    switch (SimpleInterfaceMessageType.fromInteger(msg.what))
		    {
                case INIT:
                {
                    Bundle data = msg.getData();
                    
			        
                    
			        boolean propBool = data.getBoolean("propBool", false);
				    onPropBool(propBool);
                    
			        int propInt = data.getInt("propInt", 0);
				    onPropInt(propInt);
                    
			        int propInt32 = data.getInt("propInt32", 0);
				    onPropInt32(propInt32);
                    
			        long propInt64 = data.getLong("propInt64", 0L);
				    onPropInt64(propInt64);
                    
			        float propFloat = data.getFloat("propFloat", 0.0f);
				    onPropFloat(propFloat);
                    
			        float propFloat32 = data.getFloat("propFloat32", 0.0f);
				    onPropFloat32(propFloat32);
                    
			        double propFloat64 = data.getDouble("propFloat64", 0.0);
				    onPropFloat64(propFloat64);
                    
			        String propString = data.getString("propString", new String());
				    onPropString(propString);

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
			    case SET_PropInt32:
			    {
				    Bundle data = msg.getData();

                    
			        int propInt32 = data.getInt("propInt32", 0);

				    onPropInt32(propInt32);
				    break;
			    }
			    case SET_PropInt64:
			    {
				    Bundle data = msg.getData();

                    
			        long propInt64 = data.getLong("propInt64", 0L);

				    onPropInt64(propInt64);
				    break;
			    }
			    case SET_PropFloat:
			    {
				    Bundle data = msg.getData();

                    
			        float propFloat = data.getFloat("propFloat", 0.0f);

				    onPropFloat(propFloat);
				    break;
			    }
			    case SET_PropFloat32:
			    {
				    Bundle data = msg.getData();

                    
			        float propFloat32 = data.getFloat("propFloat32", 0.0f);

				    onPropFloat32(propFloat32);
				    break;
			    }
			    case SET_PropFloat64:
			    {
				    Bundle data = msg.getData();

                    
			        double propFloat64 = data.getDouble("propFloat64", 0.0);

				    onPropFloat64(propFloat64);
				    break;
			    }
			    case SET_PropString:
			    {
				    Bundle data = msg.getData();

                    
			        String propString = data.getString("propString", new String());

				    onPropString(propString);
				    break;
			    }
			    // TODO params may be different structs from different modules, there should be a custom class loader 
			    // with a list of class loaders required for this message
			    // IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
			    case SIG_SigBool: {

				    Bundle data = msg.getData();
                    
                
			        boolean paramBool = data.getBoolean("paramBool", false);
				    onSigBool(paramBool);
				    break;
			    }
			    case SIG_SigInt: {

				    Bundle data = msg.getData();
                    
                
			        int paramInt = data.getInt("paramInt", 0);
				    onSigInt(paramInt);
				    break;
			    }
			    case SIG_SigInt32: {

				    Bundle data = msg.getData();
                    
                
			        int paramInt32 = data.getInt("paramInt32", 0);
				    onSigInt32(paramInt32);
				    break;
			    }
			    case SIG_SigInt64: {

				    Bundle data = msg.getData();
                    
                
			        long paramInt64 = data.getLong("paramInt64", 0L);
				    onSigInt64(paramInt64);
				    break;
			    }
			    case SIG_SigFloat: {

				    Bundle data = msg.getData();
                    
                
			        float paramFloat = data.getFloat("paramFloat", 0.0f);
				    onSigFloat(paramFloat);
				    break;
			    }
			    case SIG_SigFloat32: {

				    Bundle data = msg.getData();
                    
                
			        float paramFloat32 = data.getFloat("paramFloat32", 0.0f);
				    onSigFloat32(paramFloat32);
				    break;
			    }
			    case SIG_SigFloat64: {

				    Bundle data = msg.getData();
                    
                
			        double paramFloat64 = data.getDouble("paramFloat64", 0.0);
				    onSigFloat64(paramFloat64);
				    break;
			    }
			    case SIG_SigString: {

				    Bundle data = msg.getData();
                    
                
			        String paramString = data.getString("paramString", new String());
				    onSigString(paramString);
				    break;
			    }
			    case RPC_FuncNoReturnValueResp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received SimpleInterfaceMessageType.RPC_FuncNoReturnValueResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_FuncNoParamsResp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received SimpleInterfaceMessageType.RPC_FuncNoParamsResp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received SimpleInterfaceMessageType.RPC_FuncBoolResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_FuncIntResp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received SimpleInterfaceMessageType.RPC_FuncIntResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_FuncInt32Resp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received SimpleInterfaceMessageType.RPC_FuncInt32Resp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_FuncInt64Resp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received SimpleInterfaceMessageType.RPC_FuncInt64Resp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_FuncFloatResp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received SimpleInterfaceMessageType.RPC_FuncFloatResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_FuncFloat32Resp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received SimpleInterfaceMessageType.RPC_FuncFloat32Resp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_FuncFloat64Resp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received SimpleInterfaceMessageType.RPC_FuncFloat64Resp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_FuncStringResp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received SimpleInterfaceMessageType.RPC_FuncStringResp , could not find pending call for " + msg.obj);
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
			msg.what = SimpleInterfaceMessageType.PROP_PropBool.getValue();
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
			msg.what = SimpleInterfaceMessageType.PROP_PropInt.getValue();
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

  
    @Override
    public void setPropInt32(int propInt32)
    {
        Log.i(TAG, "request setPropInt32 called "+ propInt32);
        if (m_propInt32 != propInt32)
        {
			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.PROP_PropInt32.getValue();
			Bundle data = new Bundle();
            
		        data.putInt("propInt32", propInt32);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropInt32(int propInt32)
    {
        Log.i(TAG, "value received from service for PropInt32 ");
        if (m_propInt32 != propInt32)
        {
            m_propInt32 = propInt32;
            firePropInt32Changed(propInt32);
        }

    }

    @Override
    public int getPropInt32()
    {
        Log.i(TAG, "request getPropInt32 called, returning local");
        return m_propInt32;
    }

  
    @Override
    public void setPropInt64(long propInt64)
    {
        Log.i(TAG, "request setPropInt64 called "+ propInt64);
        if (m_propInt64 != propInt64)
        {
			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.PROP_PropInt64.getValue();
			Bundle data = new Bundle();
            
		        data.putLong("propInt64", propInt64);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropInt64(long propInt64)
    {
        Log.i(TAG, "value received from service for PropInt64 ");
        if (m_propInt64 != propInt64)
        {
            m_propInt64 = propInt64;
            firePropInt64Changed(propInt64);
        }

    }

    @Override
    public long getPropInt64()
    {
        Log.i(TAG, "request getPropInt64 called, returning local");
        return m_propInt64;
    }

  
    @Override
    public void setPropFloat(float propFloat)
    {
        Log.i(TAG, "request setPropFloat called "+ propFloat);
        if (m_propFloat != propFloat)
        {
			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.PROP_PropFloat.getValue();
			Bundle data = new Bundle();
            
		        data.putFloat("propFloat", propFloat);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropFloat(float propFloat)
    {
        Log.i(TAG, "value received from service for PropFloat ");
        if (m_propFloat != propFloat)
        {
            m_propFloat = propFloat;
            firePropFloatChanged(propFloat);
        }

    }

    @Override
    public float getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called, returning local");
        return m_propFloat;
    }

  
    @Override
    public void setPropFloat32(float propFloat32)
    {
        Log.i(TAG, "request setPropFloat32 called "+ propFloat32);
        if (m_propFloat32 != propFloat32)
        {
			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.PROP_PropFloat32.getValue();
			Bundle data = new Bundle();
            
		        data.putFloat("propFloat32", propFloat32);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropFloat32(float propFloat32)
    {
        Log.i(TAG, "value received from service for PropFloat32 ");
        if (m_propFloat32 != propFloat32)
        {
            m_propFloat32 = propFloat32;
            firePropFloat32Changed(propFloat32);
        }

    }

    @Override
    public float getPropFloat32()
    {
        Log.i(TAG, "request getPropFloat32 called, returning local");
        return m_propFloat32;
    }

  
    @Override
    public void setPropFloat64(double propFloat64)
    {
        Log.i(TAG, "request setPropFloat64 called "+ propFloat64);
        if (m_propFloat64 != propFloat64)
        {
			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.PROP_PropFloat64.getValue();
			Bundle data = new Bundle();
            
		        data.putDouble("propFloat64", propFloat64);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropFloat64(double propFloat64)
    {
        Log.i(TAG, "value received from service for PropFloat64 ");
        if (m_propFloat64 != propFloat64)
        {
            m_propFloat64 = propFloat64;
            firePropFloat64Changed(propFloat64);
        }

    }

    @Override
    public double getPropFloat64()
    {
        Log.i(TAG, "request getPropFloat64 called, returning local");
        return m_propFloat64;
    }

  
    @Override
    public void setPropString(String propString)
    {
        Log.i(TAG, "request setPropString called "+ propString);
        if (m_propString != propString)
        {
			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.PROP_PropString.getValue();
			Bundle data = new Bundle();
            
		        data.putString("propString", propString);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropString(String propString)
    {
        Log.i(TAG, "value received from service for PropString ");
        if (m_propString != propString)
        {
            m_propString = propString;
            firePropStringChanged(propString);
        }

    }

    @Override
    public String getPropString()
    {
        Log.i(TAG, "request getPropString called, returning local");
        return m_propString;
    }

  
    // methods

   
    @Override
    public void funcNoReturnValue(boolean paramBool) {
        CompletableFuture<Void> resFuture = funcNoReturnValueAsync(paramBool);
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
    public  CompletableFuture<Void> funcNoReturnValueAsync(boolean paramBool) {

    	Log.i(TAG, "Call on service funcNoReturnValue  "+ " " + paramBool);
		Message msg = new Message();
		msg.what = SimpleInterfaceMessageType.RPC_FuncNoReturnValueReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putBoolean("paramBool", paramBool);
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Void>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            future.complete(null);
            Log.v(TAG, "resolve funcNoReturnValue");
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public boolean funcNoParams() {
        CompletableFuture<Boolean> resFuture = funcNoParamsAsync();
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Boolean> funcNoParamsAsync() {

    	Log.i(TAG, "Call on service funcNoParams  ");
		Message msg = new Message();
		msg.what = SimpleInterfaceMessageType.RPC_FuncNoParamsReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Boolean>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving funcNoParams with null");
                return;
            }
            
		    boolean result = bundle.getBoolean("result", false);
            Log.v(TAG, "resolve funcNoParams" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

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
		msg.what = SimpleInterfaceMessageType.RPC_FuncBoolReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putBoolean("paramBool", paramBool);
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Boolean>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving funcBool with null");
                return;
            }
            
		    boolean result = bundle.getBoolean("result", false);
            Log.v(TAG, "resolve funcBool" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public int funcInt(int paramInt) {
        CompletableFuture<Integer> resFuture = funcIntAsync(paramInt);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Integer> funcIntAsync(int paramInt) {

    	Log.i(TAG, "Call on service funcInt  "+ " " + paramInt);
		Message msg = new Message();
		msg.what = SimpleInterfaceMessageType.RPC_FuncIntReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putInt("paramInt", paramInt);
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Integer>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving funcInt with null");
                return;
            }
            
		    int result = bundle.getInt("result", 0);
            Log.v(TAG, "resolve funcInt" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public int funcInt32(int paramInt32) {
        CompletableFuture<Integer> resFuture = funcInt32Async(paramInt32);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Integer> funcInt32Async(int paramInt32) {

    	Log.i(TAG, "Call on service funcInt32  "+ " " + paramInt32);
		Message msg = new Message();
		msg.what = SimpleInterfaceMessageType.RPC_FuncInt32Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putInt("paramInt32", paramInt32);
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Integer>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving funcInt32 with null");
                return;
            }
            
		    int result = bundle.getInt("result", 0);
            Log.v(TAG, "resolve funcInt32" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public long funcInt64(long paramInt64) {
        CompletableFuture<Long> resFuture = funcInt64Async(paramInt64);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Long> funcInt64Async(long paramInt64) {

    	Log.i(TAG, "Call on service funcInt64  "+ " " + paramInt64);
		Message msg = new Message();
		msg.what = SimpleInterfaceMessageType.RPC_FuncInt64Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putLong("paramInt64", paramInt64);
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Long>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving funcInt64 with null");
                return;
            }
            
		    long result = bundle.getLong("result", 0L);
            Log.v(TAG, "resolve funcInt64" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public float funcFloat(float paramFloat) {
        CompletableFuture<Float> resFuture = funcFloatAsync(paramFloat);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Float> funcFloatAsync(float paramFloat) {

    	Log.i(TAG, "Call on service funcFloat  "+ " " + paramFloat);
		Message msg = new Message();
		msg.what = SimpleInterfaceMessageType.RPC_FuncFloatReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putFloat("paramFloat", paramFloat);
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Float>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving funcFloat with null");
                return;
            }
            
		    float result = bundle.getFloat("result", 0.0f);
            Log.v(TAG, "resolve funcFloat" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public float funcFloat32(float paramFloat32) {
        CompletableFuture<Float> resFuture = funcFloat32Async(paramFloat32);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Float> funcFloat32Async(float paramFloat32) {

    	Log.i(TAG, "Call on service funcFloat32  "+ " " + paramFloat32);
		Message msg = new Message();
		msg.what = SimpleInterfaceMessageType.RPC_FuncFloat32Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putFloat("paramFloat32", paramFloat32);
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Float>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving funcFloat32 with null");
                return;
            }
            
		    float result = bundle.getFloat("result", 0.0f);
            Log.v(TAG, "resolve funcFloat32" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public double funcFloat64(double paramFloat) {
        CompletableFuture<Double> resFuture = funcFloat64Async(paramFloat);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Double> funcFloat64Async(double paramFloat) {

    	Log.i(TAG, "Call on service funcFloat64  "+ " " + paramFloat);
		Message msg = new Message();
		msg.what = SimpleInterfaceMessageType.RPC_FuncFloat64Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putDouble("paramFloat", paramFloat);
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Double>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving funcFloat64 with null");
                return;
            }
            
		    double result = bundle.getDouble("result", 0.0);
            Log.v(TAG, "resolve funcFloat64" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public String funcString(String paramString) {
        CompletableFuture<String> resFuture = funcStringAsync(paramString);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<String> funcStringAsync(String paramString) {

    	Log.i(TAG, "Call on service funcString  "+ " " + paramString);
		Message msg = new Message();
		msg.what = SimpleInterfaceMessageType.RPC_FuncStringReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putString("paramString", paramString);
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<String>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving funcString with null");
                return;
            }
            
		    String result = bundle.getString("result", new String());
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
    public void onSigBool(boolean paramBool)
    {
        Log.i(TAG, "onSigBool  received from service");
        fireSigBool(paramBool);
    }
    public void onSigInt(int paramInt)
    {
        Log.i(TAG, "onSigInt  received from service");
        fireSigInt(paramInt);
    }
    public void onSigInt32(int paramInt32)
    {
        Log.i(TAG, "onSigInt32  received from service");
        fireSigInt32(paramInt32);
    }
    public void onSigInt64(long paramInt64)
    {
        Log.i(TAG, "onSigInt64  received from service");
        fireSigInt64(paramInt64);
    }
    public void onSigFloat(float paramFloat)
    {
        Log.i(TAG, "onSigFloat  received from service");
        fireSigFloat(paramFloat);
    }
    public void onSigFloat32(float paramFloat32)
    {
        Log.i(TAG, "onSigFloat32  received from service");
        fireSigFloat32(paramFloat32);
    }
    public void onSigFloat64(double paramFloat64)
    {
        Log.i(TAG, "onSigFloat64  received from service");
        fireSigFloat64(paramFloat64);
    }
    public void onSigString(String paramString)
    {
        Log.i(TAG, "onSigString  received from service");
        fireSigString(paramString);
    }
}
