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

import tbSimple.tbSimple_api.ISimpleArrayInterfaceEventListener;
import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_api.AbstractSimpleArrayInterface;
import tbSimple.tbSimple_api.RemoteOperationException;
import tbSimple.tbSimple_android_messenger.SimpleArrayInterfaceMessageType;
import tbSimple.tbSimple_android_messenger.Conversions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;


public class SimpleArrayInterfaceClient extends AbstractSimpleArrayInterface implements ServiceConnection
{
	private static final String TAG = "SimpleArrayInterfaceClient";

	private final Context mApplicationContext;

    private boolean mIsBoundToService = false;
    private Messenger mServiceMessenger = null;
    private Messenger mClientMessenger = null;
    private String mConnectionId;
    private ClientHandler mClientHandler = new ClientHandler();

    private final Map<Integer, Consumer<Bundle>> mpendingCalls = new ConcurrentHashMap<>();
    AtomicInteger callIdsGetter = new AtomicInteger(0);
    private List<Boolean> m_propBool = new ArrayList<>();
    private List<Integer> m_propInt = new ArrayList<>();
    private List<Integer> m_propInt32 = new ArrayList<>();
    private List<Long> m_propInt64 = new ArrayList<>();
    private List<Float> m_propFloat = new ArrayList<>();
    private List<Float> m_propFloat32 = new ArrayList<>();
    private List<Double> m_propFloat64 = new ArrayList<>();
    private List<String> m_propString = new ArrayList<>();
    private String m_propReadOnlyString = new String();


	public SimpleArrayInterfaceClient(Context applicationContext, String connectionId)
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
     * Binds to a running service of type SimpleArrayInterfaceServiceAdapter.
     *
     * @return true if binding was successful, false otherwise.
     */
    public boolean bindToService(String packageName)
    {
        Intent intent = new Intent();
        intent.setClassName(packageName, "tbSimple.tbSimple_android_service.SimpleArrayInterfaceServiceAdapter");
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
            Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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
            Message msg = Message.obtain(null, SimpleArrayInterfaceMessageType.REGISTER_CLIENT.ordinal());
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

		    switch (SimpleArrayInterfaceMessageType.fromInteger(msg.what))
		    {
                case INIT:
                {
                    Bundle data = msg.getData();
                    
			        
                    
			        List<Boolean> propBool = Conversions.toList(data.getBooleanArray("propBool"));
				    onPropBool(propBool);
                    
			        List<Integer> propInt = Conversions.toList(data.getIntArray("propInt"));
				    onPropInt(propInt);
                    
			        List<Integer> propInt32 = Conversions.toList(data.getIntArray("propInt32"));
				    onPropInt32(propInt32);
                    
			        List<Long> propInt64 = Conversions.toList(data.getLongArray("propInt64"));
				    onPropInt64(propInt64);
                    
			        List<Float> propFloat = Conversions.toList(data.getFloatArray("propFloat"));
				    onPropFloat(propFloat);
                    
			        List<Float> propFloat32 = Conversions.toList(data.getFloatArray("propFloat32"));
				    onPropFloat32(propFloat32);
                    
			        List<Double> propFloat64 = Conversions.toList(data.getDoubleArray("propFloat64"));
				    onPropFloat64(propFloat64);
                    
			        List<String> propString = Conversions.toList(data.getStringArray("propString"));
				    onPropString(propString);
                    
			        String propReadOnlyString = data.getString("propReadOnlyString", new String());
				    onPropReadOnlyString(propReadOnlyString);

                    break;
                }
			    case SET_PropBool:
			    {
				    Bundle data = msg.getData();

                    
			        List<Boolean> propBool = Conversions.toList(data.getBooleanArray("propBool"));

				    onPropBool(propBool);
				    break;
			    }
			    case SET_PropInt:
			    {
				    Bundle data = msg.getData();

                    
			        List<Integer> propInt = Conversions.toList(data.getIntArray("propInt"));

				    onPropInt(propInt);
				    break;
			    }
			    case SET_PropInt32:
			    {
				    Bundle data = msg.getData();

                    
			        List<Integer> propInt32 = Conversions.toList(data.getIntArray("propInt32"));

				    onPropInt32(propInt32);
				    break;
			    }
			    case SET_PropInt64:
			    {
				    Bundle data = msg.getData();

                    
			        List<Long> propInt64 = Conversions.toList(data.getLongArray("propInt64"));

				    onPropInt64(propInt64);
				    break;
			    }
			    case SET_PropFloat:
			    {
				    Bundle data = msg.getData();

                    
			        List<Float> propFloat = Conversions.toList(data.getFloatArray("propFloat"));

				    onPropFloat(propFloat);
				    break;
			    }
			    case SET_PropFloat32:
			    {
				    Bundle data = msg.getData();

                    
			        List<Float> propFloat32 = Conversions.toList(data.getFloatArray("propFloat32"));

				    onPropFloat32(propFloat32);
				    break;
			    }
			    case SET_PropFloat64:
			    {
				    Bundle data = msg.getData();

                    
			        List<Double> propFloat64 = Conversions.toList(data.getDoubleArray("propFloat64"));

				    onPropFloat64(propFloat64);
				    break;
			    }
			    case SET_PropString:
			    {
				    Bundle data = msg.getData();

                    
			        List<String> propString = Conversions.toList(data.getStringArray("propString"));

				    onPropString(propString);
				    break;
			    }
			    case SET_PropReadOnlyString:
			    {
				    Bundle data = msg.getData();

                    
			        String propReadOnlyString = data.getString("propReadOnlyString", new String());

				    onPropReadOnlyString(propReadOnlyString);
				    break;
			    }
			    // TODO params may be different structs from different modules, there should be a custom class loader
			    // with a list of class loaders required for this message
			    // IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
			    case SIG_SigBool: {

				    Bundle data = msg.getData();
                    
                
			        List<Boolean> paramBool = Conversions.toList(data.getBooleanArray("paramBool"));
				    onSigBool(paramBool);
				    break;
			    }
			    case SIG_SigInt: {

				    Bundle data = msg.getData();
                    
                
			        List<Integer> paramInt = Conversions.toList(data.getIntArray("paramInt"));
				    onSigInt(paramInt);
				    break;
			    }
			    case SIG_SigInt32: {

				    Bundle data = msg.getData();
                    
                
			        List<Integer> paramInt32 = Conversions.toList(data.getIntArray("paramInt32"));
				    onSigInt32(paramInt32);
				    break;
			    }
			    case SIG_SigInt64: {

				    Bundle data = msg.getData();
                    
                
			        List<Long> paramInt64 = Conversions.toList(data.getLongArray("paramInt64"));
				    onSigInt64(paramInt64);
				    break;
			    }
			    case SIG_SigFloat: {

				    Bundle data = msg.getData();
                    
                
			        List<Float> paramFloat = Conversions.toList(data.getFloatArray("paramFloat"));
				    onSigFloat(paramFloat);
				    break;
			    }
			    case SIG_SigFloat32: {

				    Bundle data = msg.getData();
                    
                
			        List<Float> paramFloa32 = Conversions.toList(data.getFloatArray("paramFloa32"));
				    onSigFloat32(paramFloa32);
				    break;
			    }
			    case SIG_SigFloat64: {

				    Bundle data = msg.getData();
                    
                
			        List<Double> paramFloat64 = Conversions.toList(data.getDoubleArray("paramFloat64"));
				    onSigFloat64(paramFloat64);
				    break;
			    }
			    case SIG_SigString: {

				    Bundle data = msg.getData();
                    
                
			        List<String> paramString = Conversions.toList(data.getStringArray("paramString"));
				    onSigString(paramString);
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
                        Log.v(TAG, "received SimpleArrayInterfaceMessageType.RPC_FuncBoolResp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received SimpleArrayInterfaceMessageType.RPC_FuncIntResp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received SimpleArrayInterfaceMessageType.RPC_FuncInt32Resp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received SimpleArrayInterfaceMessageType.RPC_FuncInt64Resp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received SimpleArrayInterfaceMessageType.RPC_FuncFloatResp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received SimpleArrayInterfaceMessageType.RPC_FuncFloat32Resp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received SimpleArrayInterfaceMessageType.RPC_FuncFloat64Resp , could not find pending call for " + msg.obj);
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
                        Log.v(TAG, "received SimpleArrayInterfaceMessageType.RPC_FuncStringResp , could not find pending call for " + msg.obj);
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
    public void setPropBool(List<Boolean> propBool)
    {
        Log.i(TAG, "request setPropBool called "+ propBool);
        if (!m_propBool.equals(propBool))
        {
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.PROP_PropBool.getValue();
			Bundle data = new Bundle();
            
		        data.putBooleanArray("propBool", Conversions.toArray(propBool, new boolean[0]));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropBool(List<Boolean> propBool)
    {
        Log.i(TAG, "value received from service for PropBool ");
        if (!m_propBool.equals(propBool))
        {
            m_propBool = new ArrayList<>(propBool);
            firePropBoolChanged(propBool);
        }

    }

    @Override
    public List<Boolean> getPropBool()
    {
        Log.i(TAG, "request getPropBool called, returning local");
        return new ArrayList<>(m_propBool);
    }

  
    @Override
    public void setPropInt(List<Integer> propInt)
    {
        Log.i(TAG, "request setPropInt called "+ propInt);
        if (!m_propInt.equals(propInt))
        {
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.PROP_PropInt.getValue();
			Bundle data = new Bundle();
            
		        data.putIntArray("propInt", Conversions.toArray(propInt, new int[0]));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropInt(List<Integer> propInt)
    {
        Log.i(TAG, "value received from service for PropInt ");
        if (!m_propInt.equals(propInt))
        {
            m_propInt = new ArrayList<>(propInt);
            firePropIntChanged(propInt);
        }

    }

    @Override
    public List<Integer> getPropInt()
    {
        Log.i(TAG, "request getPropInt called, returning local");
        return new ArrayList<>(m_propInt);
    }

  
    @Override
    public void setPropInt32(List<Integer> propInt32)
    {
        Log.i(TAG, "request setPropInt32 called "+ propInt32);
        if (!m_propInt32.equals(propInt32))
        {
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.PROP_PropInt32.getValue();
			Bundle data = new Bundle();
            
		        data.putIntArray("propInt32", Conversions.toArray(propInt32, new int[0]));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropInt32(List<Integer> propInt32)
    {
        Log.i(TAG, "value received from service for PropInt32 ");
        if (!m_propInt32.equals(propInt32))
        {
            m_propInt32 = new ArrayList<>(propInt32);
            firePropInt32Changed(propInt32);
        }

    }

    @Override
    public List<Integer> getPropInt32()
    {
        Log.i(TAG, "request getPropInt32 called, returning local");
        return new ArrayList<>(m_propInt32);
    }

  
    @Override
    public void setPropInt64(List<Long> propInt64)
    {
        Log.i(TAG, "request setPropInt64 called "+ propInt64);
        if (!m_propInt64.equals(propInt64))
        {
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.PROP_PropInt64.getValue();
			Bundle data = new Bundle();
            
		        data.putLongArray("propInt64", Conversions.toArray(propInt64, new long[0]));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropInt64(List<Long> propInt64)
    {
        Log.i(TAG, "value received from service for PropInt64 ");
        if (!m_propInt64.equals(propInt64))
        {
            m_propInt64 = new ArrayList<>(propInt64);
            firePropInt64Changed(propInt64);
        }

    }

    @Override
    public List<Long> getPropInt64()
    {
        Log.i(TAG, "request getPropInt64 called, returning local");
        return new ArrayList<>(m_propInt64);
    }

  
    @Override
    public void setPropFloat(List<Float> propFloat)
    {
        Log.i(TAG, "request setPropFloat called "+ propFloat);
        if (!m_propFloat.equals(propFloat))
        {
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.PROP_PropFloat.getValue();
			Bundle data = new Bundle();
            
		        data.putFloatArray("propFloat", Conversions.toArray(propFloat, new float[0]));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropFloat(List<Float> propFloat)
    {
        Log.i(TAG, "value received from service for PropFloat ");
        if (!m_propFloat.equals(propFloat))
        {
            m_propFloat = new ArrayList<>(propFloat);
            firePropFloatChanged(propFloat);
        }

    }

    @Override
    public List<Float> getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called, returning local");
        return new ArrayList<>(m_propFloat);
    }

  
    @Override
    public void setPropFloat32(List<Float> propFloat32)
    {
        Log.i(TAG, "request setPropFloat32 called "+ propFloat32);
        if (!m_propFloat32.equals(propFloat32))
        {
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.PROP_PropFloat32.getValue();
			Bundle data = new Bundle();
            
		        data.putFloatArray("propFloat32", Conversions.toArray(propFloat32, new float[0]));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropFloat32(List<Float> propFloat32)
    {
        Log.i(TAG, "value received from service for PropFloat32 ");
        if (!m_propFloat32.equals(propFloat32))
        {
            m_propFloat32 = new ArrayList<>(propFloat32);
            firePropFloat32Changed(propFloat32);
        }

    }

    @Override
    public List<Float> getPropFloat32()
    {
        Log.i(TAG, "request getPropFloat32 called, returning local");
        return new ArrayList<>(m_propFloat32);
    }

  
    @Override
    public void setPropFloat64(List<Double> propFloat64)
    {
        Log.i(TAG, "request setPropFloat64 called "+ propFloat64);
        if (!m_propFloat64.equals(propFloat64))
        {
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.PROP_PropFloat64.getValue();
			Bundle data = new Bundle();
            
		        data.putDoubleArray("propFloat64", Conversions.toArray(propFloat64, new double[0]));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropFloat64(List<Double> propFloat64)
    {
        Log.i(TAG, "value received from service for PropFloat64 ");
        if (!m_propFloat64.equals(propFloat64))
        {
            m_propFloat64 = new ArrayList<>(propFloat64);
            firePropFloat64Changed(propFloat64);
        }

    }

    @Override
    public List<Double> getPropFloat64()
    {
        Log.i(TAG, "request getPropFloat64 called, returning local");
        return new ArrayList<>(m_propFloat64);
    }

  
    @Override
    public void setPropString(List<String> propString)
    {
        Log.i(TAG, "request setPropString called "+ propString);
        if (!m_propString.equals(propString))
        {
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.PROP_PropString.getValue();
			Bundle data = new Bundle();
            
		        data.putStringArray("propString", Conversions.toArray(propString, new String[0]));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropString(List<String> propString)
    {
        Log.i(TAG, "value received from service for PropString ");
        if (!m_propString.equals(propString))
        {
            m_propString = new ArrayList<>(propString);
            firePropStringChanged(propString);
        }

    }

    @Override
    public List<String> getPropString()
    {
        Log.i(TAG, "request getPropString called, returning local");
        return new ArrayList<>(m_propString);
    }

  
    @Override
    public void setPropReadOnlyString(String propReadOnlyString)
    {
        Log.i(TAG, "request setPropReadOnlyString called "+ propReadOnlyString);
        if (m_propReadOnlyString != propReadOnlyString)
        {
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.PROP_PropReadOnlyString.getValue();
			Bundle data = new Bundle();
            
		        data.putString("propReadOnlyString", propReadOnlyString);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onPropReadOnlyString(String propReadOnlyString)
    {
        Log.i(TAG, "value received from service for PropReadOnlyString ");
        if (m_propReadOnlyString != propReadOnlyString)
        {
            m_propReadOnlyString = propReadOnlyString;
            firePropReadOnlyStringChanged(propReadOnlyString);
        }

    }

    @Override
    public String getPropReadOnlyString()
    {
        Log.i(TAG, "request getPropReadOnlyString called, returning local");
        return m_propReadOnlyString;
    }

  
    // methods


    @Override
    public List<Boolean> funcBool(List<Boolean> paramBool) {
        CompletableFuture<List<Boolean>> resFuture = funcBoolAsync(paramBool);
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
    public  CompletableFuture<List<Boolean>> funcBoolAsync(List<Boolean> paramBool) {

    	Log.i(TAG, "Call on service funcBool  "+ " " + paramBool);
		Message msg = new Message();
		msg.what = SimpleArrayInterfaceMessageType.RPC_FuncBoolReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putBooleanArray("paramBool", Conversions.toArray(paramBool, new boolean[0]));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<List<Boolean>>  future = new CompletableFuture<>();
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
            
		    List<Boolean> result = Conversions.toList(bundle.getBooleanArray("result"));
            Log.v(TAG, "resolve funcBool" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }


    @Override
    public List<Integer> funcInt(List<Integer> paramInt) {
        CompletableFuture<List<Integer>> resFuture = funcIntAsync(paramInt);
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
    public  CompletableFuture<List<Integer>> funcIntAsync(List<Integer> paramInt) {

    	Log.i(TAG, "Call on service funcInt  "+ " " + paramInt);
		Message msg = new Message();
		msg.what = SimpleArrayInterfaceMessageType.RPC_FuncIntReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putIntArray("paramInt", Conversions.toArray(paramInt, new int[0]));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<List<Integer>>  future = new CompletableFuture<>();
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
            
		    List<Integer> result = Conversions.toList(bundle.getIntArray("result"));
            Log.v(TAG, "resolve funcInt" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }


    @Override
    public List<Integer> funcInt32(List<Integer> paramInt32) {
        CompletableFuture<List<Integer>> resFuture = funcInt32Async(paramInt32);
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
    public  CompletableFuture<List<Integer>> funcInt32Async(List<Integer> paramInt32) {

    	Log.i(TAG, "Call on service funcInt32  "+ " " + paramInt32);
		Message msg = new Message();
		msg.what = SimpleArrayInterfaceMessageType.RPC_FuncInt32Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putIntArray("paramInt32", Conversions.toArray(paramInt32, new int[0]));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<List<Integer>>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                Log.w(TAG, "funcInt32: received null bundle (service disconnected?)");
                future.completeExceptionally(new RemoteOperationException("service disconnected", RemoteOperationException.ERROR_SERVICE_DISCONNECTED));
                return;
            }
            if (bundle.getBoolean("error", false))
            {
                String errorMessage = bundle.getString("errorMessage", "unknown error");
                int errorCode = bundle.getInt("errorCode", RemoteOperationException.ERROR_UNKNOWN);
                Log.w(TAG, "funcInt32 failed: " + errorMessage);
                future.completeExceptionally(new RemoteOperationException(errorMessage, errorCode));
                return;
            }
            
		    List<Integer> result = Conversions.toList(bundle.getIntArray("result"));
            Log.v(TAG, "resolve funcInt32" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }


    @Override
    public List<Long> funcInt64(List<Long> paramInt64) {
        CompletableFuture<List<Long>> resFuture = funcInt64Async(paramInt64);
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
    public  CompletableFuture<List<Long>> funcInt64Async(List<Long> paramInt64) {

    	Log.i(TAG, "Call on service funcInt64  "+ " " + paramInt64);
		Message msg = new Message();
		msg.what = SimpleArrayInterfaceMessageType.RPC_FuncInt64Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putLongArray("paramInt64", Conversions.toArray(paramInt64, new long[0]));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<List<Long>>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                Log.w(TAG, "funcInt64: received null bundle (service disconnected?)");
                future.completeExceptionally(new RemoteOperationException("service disconnected", RemoteOperationException.ERROR_SERVICE_DISCONNECTED));
                return;
            }
            if (bundle.getBoolean("error", false))
            {
                String errorMessage = bundle.getString("errorMessage", "unknown error");
                int errorCode = bundle.getInt("errorCode", RemoteOperationException.ERROR_UNKNOWN);
                Log.w(TAG, "funcInt64 failed: " + errorMessage);
                future.completeExceptionally(new RemoteOperationException(errorMessage, errorCode));
                return;
            }
            
		    List<Long> result = Conversions.toList(bundle.getLongArray("result"));
            Log.v(TAG, "resolve funcInt64" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }


    @Override
    public List<Float> funcFloat(List<Float> paramFloat) {
        CompletableFuture<List<Float>> resFuture = funcFloatAsync(paramFloat);
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
    public  CompletableFuture<List<Float>> funcFloatAsync(List<Float> paramFloat) {

    	Log.i(TAG, "Call on service funcFloat  "+ " " + paramFloat);
		Message msg = new Message();
		msg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloatReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putFloatArray("paramFloat", Conversions.toArray(paramFloat, new float[0]));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<List<Float>>  future = new CompletableFuture<>();
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
            
		    List<Float> result = Conversions.toList(bundle.getFloatArray("result"));
            Log.v(TAG, "resolve funcFloat" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }


    @Override
    public List<Float> funcFloat32(List<Float> paramFloat32) {
        CompletableFuture<List<Float>> resFuture = funcFloat32Async(paramFloat32);
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
    public  CompletableFuture<List<Float>> funcFloat32Async(List<Float> paramFloat32) {

    	Log.i(TAG, "Call on service funcFloat32  "+ " " + paramFloat32);
		Message msg = new Message();
		msg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloat32Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putFloatArray("paramFloat32", Conversions.toArray(paramFloat32, new float[0]));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<List<Float>>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                Log.w(TAG, "funcFloat32: received null bundle (service disconnected?)");
                future.completeExceptionally(new RemoteOperationException("service disconnected", RemoteOperationException.ERROR_SERVICE_DISCONNECTED));
                return;
            }
            if (bundle.getBoolean("error", false))
            {
                String errorMessage = bundle.getString("errorMessage", "unknown error");
                int errorCode = bundle.getInt("errorCode", RemoteOperationException.ERROR_UNKNOWN);
                Log.w(TAG, "funcFloat32 failed: " + errorMessage);
                future.completeExceptionally(new RemoteOperationException(errorMessage, errorCode));
                return;
            }
            
		    List<Float> result = Conversions.toList(bundle.getFloatArray("result"));
            Log.v(TAG, "resolve funcFloat32" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }


    @Override
    public List<Double> funcFloat64(List<Double> paramFloat) {
        CompletableFuture<List<Double>> resFuture = funcFloat64Async(paramFloat);
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
    public  CompletableFuture<List<Double>> funcFloat64Async(List<Double> paramFloat) {

    	Log.i(TAG, "Call on service funcFloat64  "+ " " + paramFloat);
		Message msg = new Message();
		msg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloat64Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putDoubleArray("paramFloat", Conversions.toArray(paramFloat, new double[0]));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<List<Double>>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                Log.w(TAG, "funcFloat64: received null bundle (service disconnected?)");
                future.completeExceptionally(new RemoteOperationException("service disconnected", RemoteOperationException.ERROR_SERVICE_DISCONNECTED));
                return;
            }
            if (bundle.getBoolean("error", false))
            {
                String errorMessage = bundle.getString("errorMessage", "unknown error");
                int errorCode = bundle.getInt("errorCode", RemoteOperationException.ERROR_UNKNOWN);
                Log.w(TAG, "funcFloat64 failed: " + errorMessage);
                future.completeExceptionally(new RemoteOperationException(errorMessage, errorCode));
                return;
            }
            
		    List<Double> result = Conversions.toList(bundle.getDoubleArray("result"));
            Log.v(TAG, "resolve funcFloat64" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }


    @Override
    public List<String> funcString(List<String> paramString) {
        CompletableFuture<List<String>> resFuture = funcStringAsync(paramString);
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
    public  CompletableFuture<List<String>> funcStringAsync(List<String> paramString) {

    	Log.i(TAG, "Call on service funcString  "+ " " + paramString);
		Message msg = new Message();
		msg.what = SimpleArrayInterfaceMessageType.RPC_FuncStringReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putStringArray("paramString", Conversions.toArray(paramString, new String[0]));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<List<String>>  future = new CompletableFuture<>();
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
            
		    List<String> result = Conversions.toList(bundle.getStringArray("result"));
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
    public void onSigBool(List<Boolean> paramBool)
    {
        Log.i(TAG, "onSigBool  received from service");
        fireSigBool(paramBool);
    }
    public void onSigInt(List<Integer> paramInt)
    {
        Log.i(TAG, "onSigInt  received from service");
        fireSigInt(paramInt);
    }
    public void onSigInt32(List<Integer> paramInt32)
    {
        Log.i(TAG, "onSigInt32  received from service");
        fireSigInt32(paramInt32);
    }
    public void onSigInt64(List<Long> paramInt64)
    {
        Log.i(TAG, "onSigInt64  received from service");
        fireSigInt64(paramInt64);
    }
    public void onSigFloat(List<Float> paramFloat)
    {
        Log.i(TAG, "onSigFloat  received from service");
        fireSigFloat(paramFloat);
    }
    public void onSigFloat32(List<Float> paramFloa32)
    {
        Log.i(TAG, "onSigFloat32  received from service");
        fireSigFloat32(paramFloa32);
    }
    public void onSigFloat64(List<Double> paramFloat64)
    {
        Log.i(TAG, "onSigFloat64  received from service");
        fireSigFloat64(paramFloat64);
    }
    public void onSigString(List<String> paramString)
    {
        Log.i(TAG, "onSigString  received from service");
        fireSigString(paramString);
    }
}
