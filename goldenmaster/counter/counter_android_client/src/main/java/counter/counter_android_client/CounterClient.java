//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package counter.counter_android_client;

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

import counter.counter_api.ICounterEventListener;
import counter.counter_api.ICounter;
import counter.counter_api.AbstractCounter;
import counter.counter_android_messenger.CounterMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;


public class CounterClient extends AbstractCounter implements ServiceConnection
{
	private static final String TAG = "CounterClient";

	private final Context mApplicationContext;

    private boolean mIsBoundToService = false;
    private Messenger mServiceMessenger = null;
    private Messenger mClientMessenger = null;
    private String mConnectionId;
    private ClientHandler mClientHandler = new ClientHandler();

    private final Map<Integer, Consumer<Bundle>> mpendingCalls = new ConcurrentHashMap<>();
    AtomicInteger callIdsGetter = new AtomicInteger(0);
    private customTypes.customTypes_api.Vector3D m_vector = new customTypes.customTypes_api.Vector3D();
    private org.apache.commons.math3.geometry.euclidean.threed.Vector3D m_extern_vector = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
    private customTypes.customTypes_api.Vector3D[] m_vectorArray = new customTypes.customTypes_api.Vector3D[]{};
    private org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] m_extern_vectorArray = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]{};


	public CounterClient(Context applicationContext, String connectionId)
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
     * Binds to a running service of type CounterServiceAdapter.
     *
     * @return true if binding was successful, false otherwise.
     */
    public boolean bindToService(String packageName)
    {
        Intent intent = new Intent();
        intent.setClassName(packageName, "counter.counter_android_service.CounterServiceAdapter");
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
            Message msg = Message.obtain(null, CounterMessageType.UNREGISTER_CLIENT.ordinal());
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
            Message msg = Message.obtain(null, CounterMessageType.REGISTER_CLIENT.ordinal());
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

		    switch (CounterMessageType.fromInteger(msg.what))
		    {
                case INIT:
                {
                    Bundle data = msg.getData();
                    
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
			        
                    
			        customTypes.customTypes_api.Vector3D vector = data.getParcelable("vector", customTypes.customTypes_android_messenger.Vector3DParcelable.class).getVector3D();
				    onVector(vector);
                    
			        org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector = data.getParcelable("extern_vector", externTypes.externTypes_android_messenger.MyVector3DParcelable.class).getMyVector3D();
				    onExternVector(extern_vector);
                    
                    customTypes.customTypes_api.Vector3D[] vectorArray =  customTypes.customTypes_android_messenger.Vector3DParcelable.unwrapArray((customTypes.customTypes_android_messenger.Vector3DParcelable[])data.getParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.class));
				    onVectorArray(vectorArray);
                    
                    org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray =  externTypes.externTypes_android_messenger.MyVector3DParcelable.unwrapArray((externTypes.externTypes_android_messenger.MyVector3DParcelable[])data.getParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.class));
				    onExternVectorArray(extern_vectorArray);

                    break;
                }
			    case SET_Vector:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());

                    
			        customTypes.customTypes_api.Vector3D vector = data.getParcelable("vector", customTypes.customTypes_android_messenger.Vector3DParcelable.class).getVector3D();

				    onVector(vector);
				    break;
			    }
			    case SET_ExternVector:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());

                    
			        org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector = data.getParcelable("extern_vector", externTypes.externTypes_android_messenger.MyVector3DParcelable.class).getMyVector3D();

				    onExternVector(extern_vector);
				    break;
			    }
			    case SET_VectorArray:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());

                    
                    customTypes.customTypes_api.Vector3D[] vectorArray =  customTypes.customTypes_android_messenger.Vector3DParcelable.unwrapArray((customTypes.customTypes_android_messenger.Vector3DParcelable[])data.getParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.class));

				    onVectorArray(vectorArray);
				    break;
			    }
			    case SET_ExternVectorArray:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());

                    
                    org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray =  externTypes.externTypes_android_messenger.MyVector3DParcelable.unwrapArray((externTypes.externTypes_android_messenger.MyVector3DParcelable[])data.getParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.class));

				    onExternVectorArray(extern_vectorArray);
				    break;
			    }
			    // TODO params may be different structs from different modules, there should be a custom class loader 
			    // with a list of class loaders required for this message
			    // IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
			    case SIG_ValueChanged: {

				    Bundle data = msg.getData();
                    
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
                
			        customTypes.customTypes_api.Vector3D vector = data.getParcelable("vector", customTypes.customTypes_android_messenger.Vector3DParcelable.class).getVector3D();
                
			        org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector = data.getParcelable("extern_vector", externTypes.externTypes_android_messenger.MyVector3DParcelable.class).getMyVector3D();
                
                    customTypes.customTypes_api.Vector3D[] vectorArray =  customTypes.customTypes_android_messenger.Vector3DParcelable.unwrapArray((customTypes.customTypes_android_messenger.Vector3DParcelable[])data.getParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.class));
                
                    org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray =  externTypes.externTypes_android_messenger.MyVector3DParcelable.unwrapArray((externTypes.externTypes_android_messenger.MyVector3DParcelable[])data.getParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.class));
				    onValueChanged(vector, extern_vector, vectorArray, extern_vectorArray);
				    break;
			    }
			    case RPC_IncrementResp: {

				    Bundle data = msg.getData();
					data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received CounterMessageType.RPC_IncrementResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_IncrementArrayResp: {

				    Bundle data = msg.getData();
					data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received CounterMessageType.RPC_IncrementArrayResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_DecrementResp: {

				    Bundle data = msg.getData();
					data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received CounterMessageType.RPC_DecrementResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_DecrementArrayResp: {

				    Bundle data = msg.getData();
					data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received CounterMessageType.RPC_DecrementArrayResp , could not find pending call for " + msg.obj);
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
    public void setVector(customTypes.customTypes_api.Vector3D vector)
    {
        Log.i(TAG, "request setVector called "+ vector);
        if ( (m_vector != null && ! m_vector.equals(vector))
        || (m_vector == null && vector != null ))
        {
			Message msg = new Message();
			msg.what = CounterMessageType.PROP_Vector.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("vector", new customTypes.customTypes_android_messenger.Vector3DParcelable(vector));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onVector(customTypes.customTypes_api.Vector3D vector)
    {
        Log.i(TAG, "value received from service for Vector ");
        if ( (m_vector != null && ! m_vector.equals(vector))
        || (m_vector == null && vector != null ))
        {
            m_vector = vector;
            fireVectorChanged(vector);
        }

    }

    @Override
    public customTypes.customTypes_api.Vector3D getVector()
    {
        Log.i(TAG, "request getVector called, returning local");
        return m_vector;
    }

  
    @Override
    public void setExternVector(org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector)
    {
        Log.i(TAG, "request setExternVector called "+ extern_vector);
        if ( (m_extern_vector != null && ! m_extern_vector.equals(extern_vector))
        || (m_extern_vector == null && extern_vector != null ))
        {
			Message msg = new Message();
			msg.what = CounterMessageType.PROP_ExternVector.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("extern_vector", new externTypes.externTypes_android_messenger.MyVector3DParcelable(extern_vector));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onExternVector(org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector)
    {
        Log.i(TAG, "value received from service for ExternVector ");
        if ( (m_extern_vector != null && ! m_extern_vector.equals(extern_vector))
        || (m_extern_vector == null && extern_vector != null ))
        {
            m_extern_vector = extern_vector;
            fireExternVectorChanged(extern_vector);
        }

    }

    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D getExternVector()
    {
        Log.i(TAG, "request getExternVector called, returning local");
        return m_extern_vector;
    }

  
    @Override
    public void setVectorArray(customTypes.customTypes_api.Vector3D[] vectorArray)
    {
        Log.i(TAG, "request setVectorArray called "+ vectorArray);
        if (! Arrays.equals(m_vectorArray, vectorArray))
        {
			Message msg = new Message();
			msg.what = CounterMessageType.PROP_VectorArray.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(vectorArray));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onVectorArray(customTypes.customTypes_api.Vector3D[] vectorArray)
    {
        Log.i(TAG, "value received from service for VectorArray ");
        if (! Arrays.equals(m_vectorArray, vectorArray))
        {
            m_vectorArray = vectorArray;
            fireVectorArrayChanged(vectorArray);
        }

    }

    @Override
    public customTypes.customTypes_api.Vector3D[] getVectorArray()
    {
        Log.i(TAG, "request getVectorArray called, returning local");
        return m_vectorArray;
    }

  
    @Override
    public void setExternVectorArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray)
    {
        Log.i(TAG, "request setExternVectorArray called "+ extern_vectorArray);
        if (! Arrays.equals(m_extern_vectorArray, extern_vectorArray))
        {
			Message msg = new Message();
			msg.what = CounterMessageType.PROP_ExternVectorArray.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(extern_vectorArray));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onExternVectorArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray)
    {
        Log.i(TAG, "value received from service for ExternVectorArray ");
        if (! Arrays.equals(m_extern_vectorArray, extern_vectorArray))
        {
            m_extern_vectorArray = extern_vectorArray;
            fireExternVectorArrayChanged(extern_vectorArray);
        }

    }

    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] getExternVectorArray()
    {
        Log.i(TAG, "request getExternVectorArray called, returning local");
        return m_extern_vectorArray;
    }

  
    // methods

   
    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D increment(org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec) {
        CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> resFuture = incrementAsync(vec);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> incrementAsync(org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec) {

    	Log.i(TAG, "Call on service increment  "+ " " + vec);
		Message msg = new Message();
		msg.what = CounterMessageType.RPC_IncrementReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("vec", new externTypes.externTypes_android_messenger.MyVector3DParcelable(vec));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving increment with null");
                return;
            }
            
		    org.apache.commons.math3.geometry.euclidean.threed.Vector3D result = bundle.getParcelable("result", externTypes.externTypes_android_messenger.MyVector3DParcelable.class).getMyVector3D();
            Log.v(TAG, "resolve increment" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] incrementArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec) {
        CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]> resFuture = incrementArrayAsync(vec);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]> incrementArrayAsync(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec) {

    	Log.i(TAG, "Call on service incrementArray  "+ " " + vec);
		Message msg = new Message();
		msg.what = CounterMessageType.RPC_IncrementArrayReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelableArray("vec", externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(vec));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving incrementArray with null");
                return;
            }
            
            org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] result =  externTypes.externTypes_android_messenger.MyVector3DParcelable.unwrapArray((externTypes.externTypes_android_messenger.MyVector3DParcelable[])bundle.getParcelableArray("result", externTypes.externTypes_android_messenger.MyVector3DParcelable.class));
            Log.v(TAG, "resolve incrementArray" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public customTypes.customTypes_api.Vector3D decrement(customTypes.customTypes_api.Vector3D vec) {
        CompletableFuture<customTypes.customTypes_api.Vector3D> resFuture = decrementAsync(vec);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<customTypes.customTypes_api.Vector3D> decrementAsync(customTypes.customTypes_api.Vector3D vec) {

    	Log.i(TAG, "Call on service decrement  "+ " " + vec);
		Message msg = new Message();
		msg.what = CounterMessageType.RPC_DecrementReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("vec", new customTypes.customTypes_android_messenger.Vector3DParcelable(vec));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<customTypes.customTypes_api.Vector3D>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving decrement with null");
                return;
            }
            
		    customTypes.customTypes_api.Vector3D result = bundle.getParcelable("result", customTypes.customTypes_android_messenger.Vector3DParcelable.class).getVector3D();
            Log.v(TAG, "resolve decrement" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public customTypes.customTypes_api.Vector3D[] decrementArray(customTypes.customTypes_api.Vector3D[] vec) {
        CompletableFuture<customTypes.customTypes_api.Vector3D[]> resFuture = decrementArrayAsync(vec);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<customTypes.customTypes_api.Vector3D[]> decrementArrayAsync(customTypes.customTypes_api.Vector3D[] vec) {

    	Log.i(TAG, "Call on service decrementArray  "+ " " + vec);
		Message msg = new Message();
		msg.what = CounterMessageType.RPC_DecrementArrayReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelableArray("vec", customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(vec));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<customTypes.customTypes_api.Vector3D[]>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                future.complete(null);
                Log.v(TAG, "received null bundle, resolving decrementArray with null");
                return;
            }
            
            customTypes.customTypes_api.Vector3D[] result =  customTypes.customTypes_android_messenger.Vector3DParcelable.unwrapArray((customTypes.customTypes_android_messenger.Vector3DParcelable[])bundle.getParcelableArray("result", customTypes.customTypes_android_messenger.Vector3DParcelable.class));
            Log.v(TAG, "resolve decrementArray" + result);
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
    public void onValueChanged(customTypes.customTypes_api.Vector3D vector, org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector, customTypes.customTypes_api.Vector3D[] vectorArray, org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray)
    {
        Log.i(TAG, "onValueChanged  received from service");
        fireValueChanged(vector, extern_vector, vectorArray, extern_vectorArray);
    }
}
