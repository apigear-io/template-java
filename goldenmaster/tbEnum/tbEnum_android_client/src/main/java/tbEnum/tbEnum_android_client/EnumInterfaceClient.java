//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbEnum.tbEnum_android_client;

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
import tbEnum.tbEnum_api.Enum0;
import tbEnum.tbEnum_android_messenger.Enum0Parcelable;
import tbEnum.tbEnum_api.Enum1;
import tbEnum.tbEnum_android_messenger.Enum1Parcelable;
import tbEnum.tbEnum_api.Enum2;
import tbEnum.tbEnum_android_messenger.Enum2Parcelable;
import tbEnum.tbEnum_api.Enum3;
import tbEnum.tbEnum_android_messenger.Enum3Parcelable;

import tbEnum.tbEnum_api.IEnumInterfaceEventListener;
import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_api.AbstractEnumInterface;
import tbEnum.tbEnum_api.RemoteOperationException;
import tbEnum.tbEnum_android_messenger.EnumInterfaceMessageType;
import tbEnum.tbEnum_android_messenger.Conversions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;


public class EnumInterfaceClient extends AbstractEnumInterface implements ServiceConnection
{
	private static final String TAG = "EnumInterfaceClient";

	private final Context mApplicationContext;

    private boolean mIsBoundToService = false;
    private Messenger mServiceMessenger = null;
    private Messenger mClientMessenger = null;
    private String mConnectionId;
    private ClientHandler mClientHandler = new ClientHandler();

    private final Map<Integer, Consumer<Bundle>> mpendingCalls = new ConcurrentHashMap<>();
    AtomicInteger callIdsGetter = new AtomicInteger(0);
    private Enum0 m_prop0 = Enum0.Value0;
    private Enum1 m_prop1 = Enum1.Value1;
    private Enum2 m_prop2 = Enum2.Value2;
    private Enum3 m_prop3 = Enum3.Value3;


	public EnumInterfaceClient(Context applicationContext, String connectionId)
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
     * Binds to a running service of type EnumInterfaceServiceAdapter.
     *
     * @return true if binding was successful, false otherwise.
     */
    public boolean bindToService(String packageName)
    {
        Intent intent = new Intent();
        intent.setClassName(packageName, "tbEnum.tbEnum_android_service.EnumInterfaceServiceAdapter");
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
            Message msg = Message.obtain(null, EnumInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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
            Message msg = Message.obtain(null, EnumInterfaceMessageType.REGISTER_CLIENT.ordinal());
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

		    switch (EnumInterfaceMessageType.fromInteger(msg.what))
		    {
                case INIT:
                {
                    Bundle data = msg.getData();
                    
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(Enum0Parcelable.class.getClassLoader());
			        
                    
			        Enum0 prop0 = data.getParcelable("prop0", Enum0Parcelable.class).getEnum0();
				    onProp0(prop0);
                    
			        Enum1 prop1 = data.getParcelable("prop1", Enum1Parcelable.class).getEnum1();
				    onProp1(prop1);
                    
			        Enum2 prop2 = data.getParcelable("prop2", Enum2Parcelable.class).getEnum2();
				    onProp2(prop2);
                    
			        Enum3 prop3 = data.getParcelable("prop3", Enum3Parcelable.class).getEnum3();
				    onProp3(prop3);

                    break;
                }
			    case SET_Prop0:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(Enum0Parcelable.class.getClassLoader());

                    
			        Enum0 prop0 = data.getParcelable("prop0", Enum0Parcelable.class).getEnum0();

				    onProp0(prop0);
				    break;
			    }
			    case SET_Prop1:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(Enum1Parcelable.class.getClassLoader());

                    
			        Enum1 prop1 = data.getParcelable("prop1", Enum1Parcelable.class).getEnum1();

				    onProp1(prop1);
				    break;
			    }
			    case SET_Prop2:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(Enum2Parcelable.class.getClassLoader());

                    
			        Enum2 prop2 = data.getParcelable("prop2", Enum2Parcelable.class).getEnum2();

				    onProp2(prop2);
				    break;
			    }
			    case SET_Prop3:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(Enum3Parcelable.class.getClassLoader());

                    
			        Enum3 prop3 = data.getParcelable("prop3", Enum3Parcelable.class).getEnum3();

				    onProp3(prop3);
				    break;
			    }
			    // TODO params may be different structs from different modules, there should be a custom class loader
			    // with a list of class loaders required for this message
			    // IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
			    case SIG_Sig0: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(Enum0Parcelable.class.getClassLoader());
                
			        Enum0 param0 = data.getParcelable("param0", Enum0Parcelable.class).getEnum0();
				    onSig0(param0);
				    break;
			    }
			    case SIG_Sig1: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(Enum1Parcelable.class.getClassLoader());
                
			        Enum1 param1 = data.getParcelable("param1", Enum1Parcelable.class).getEnum1();
				    onSig1(param1);
				    break;
			    }
			    case SIG_Sig2: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(Enum2Parcelable.class.getClassLoader());
                
			        Enum2 param2 = data.getParcelable("param2", Enum2Parcelable.class).getEnum2();
				    onSig2(param2);
				    break;
			    }
			    case SIG_Sig3: {

				    Bundle data = msg.getData();
                    
        data.setClassLoader(Enum3Parcelable.class.getClassLoader());
                
			        Enum3 param3 = data.getParcelable("param3", Enum3Parcelable.class).getEnum3();
				    onSig3(param3);
				    break;
			    }
			    case RPC_Func0Resp: {

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
                        Log.v(TAG, "received EnumInterfaceMessageType.RPC_Func0Resp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_Func1Resp: {

				    Bundle data = msg.getData();
					data.setClassLoader(Enum1Parcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received EnumInterfaceMessageType.RPC_Func1Resp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_Func2Resp: {

				    Bundle data = msg.getData();
					data.setClassLoader(Enum2Parcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received EnumInterfaceMessageType.RPC_Func2Resp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_Func3Resp: {

				    Bundle data = msg.getData();
					data.setClassLoader(Enum3Parcelable.class.getClassLoader());
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received EnumInterfaceMessageType.RPC_Func3Resp , could not find pending call for " + msg.obj);
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
    public void setProp0(Enum0 prop0)
    {
        Log.i(TAG, "request setProp0 called "+ prop0);
        if (m_prop0 != prop0)
        {
			Message msg = new Message();
			msg.what = EnumInterfaceMessageType.PROP_Prop0.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("prop0", new Enum0Parcelable(prop0));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onProp0(Enum0 prop0)
    {
        Log.i(TAG, "value received from service for Prop0 ");
        if (m_prop0 != prop0)
        {
            m_prop0 = prop0;
            fireProp0Changed(prop0);
        }

    }

    @Override
    public Enum0 getProp0()
    {
        Log.i(TAG, "request getProp0 called, returning local");
        return m_prop0;
    }

  
    @Override
    public void setProp1(Enum1 prop1)
    {
        Log.i(TAG, "request setProp1 called "+ prop1);
        if (m_prop1 != prop1)
        {
			Message msg = new Message();
			msg.what = EnumInterfaceMessageType.PROP_Prop1.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("prop1", new Enum1Parcelable(prop1));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onProp1(Enum1 prop1)
    {
        Log.i(TAG, "value received from service for Prop1 ");
        if (m_prop1 != prop1)
        {
            m_prop1 = prop1;
            fireProp1Changed(prop1);
        }

    }

    @Override
    public Enum1 getProp1()
    {
        Log.i(TAG, "request getProp1 called, returning local");
        return m_prop1;
    }

  
    @Override
    public void setProp2(Enum2 prop2)
    {
        Log.i(TAG, "request setProp2 called "+ prop2);
        if (m_prop2 != prop2)
        {
			Message msg = new Message();
			msg.what = EnumInterfaceMessageType.PROP_Prop2.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("prop2", new Enum2Parcelable(prop2));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onProp2(Enum2 prop2)
    {
        Log.i(TAG, "value received from service for Prop2 ");
        if (m_prop2 != prop2)
        {
            m_prop2 = prop2;
            fireProp2Changed(prop2);
        }

    }

    @Override
    public Enum2 getProp2()
    {
        Log.i(TAG, "request getProp2 called, returning local");
        return m_prop2;
    }

  
    @Override
    public void setProp3(Enum3 prop3)
    {
        Log.i(TAG, "request setProp3 called "+ prop3);
        if (m_prop3 != prop3)
        {
			Message msg = new Message();
			msg.what = EnumInterfaceMessageType.PROP_Prop3.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("prop3", new Enum3Parcelable(prop3));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onProp3(Enum3 prop3)
    {
        Log.i(TAG, "value received from service for Prop3 ");
        if (m_prop3 != prop3)
        {
            m_prop3 = prop3;
            fireProp3Changed(prop3);
        }

    }

    @Override
    public Enum3 getProp3()
    {
        Log.i(TAG, "request getProp3 called, returning local");
        return m_prop3;
    }

  
    // methods


    @Override
    public Enum0 func0(Enum0 param0) {
        CompletableFuture<Enum0> resFuture = func0Async(param0);
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
    public  CompletableFuture<Enum0> func0Async(Enum0 param0) {

    	Log.i(TAG, "Call on service func0  "+ " " + param0);
		Message msg = new Message();
		msg.what = EnumInterfaceMessageType.RPC_Func0Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("param0", new Enum0Parcelable(param0));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Enum0>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                Log.w(TAG, "func0: received null bundle (service disconnected?)");
                future.completeExceptionally(new RemoteOperationException("service disconnected", RemoteOperationException.ERROR_SERVICE_DISCONNECTED));
                return;
            }
            if (bundle.getBoolean("error", false))
            {
                String errorMessage = bundle.getString("errorMessage", "unknown error");
                int errorCode = bundle.getInt("errorCode", RemoteOperationException.ERROR_UNKNOWN);
                Log.w(TAG, "func0 failed: " + errorMessage);
                future.completeExceptionally(new RemoteOperationException(errorMessage, errorCode));
                return;
            }
            
		    Enum0 result = bundle.getParcelable("result", Enum0Parcelable.class).getEnum0();
            Log.v(TAG, "resolve func0" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }


    @Override
    public Enum1 func1(Enum1 param1) {
        CompletableFuture<Enum1> resFuture = func1Async(param1);
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
    public  CompletableFuture<Enum1> func1Async(Enum1 param1) {

    	Log.i(TAG, "Call on service func1  "+ " " + param1);
		Message msg = new Message();
		msg.what = EnumInterfaceMessageType.RPC_Func1Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("param1", new Enum1Parcelable(param1));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Enum1>  future = new CompletableFuture<>();
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
            
		    Enum1 result = bundle.getParcelable("result", Enum1Parcelable.class).getEnum1();
            Log.v(TAG, "resolve func1" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }


    @Override
    public Enum2 func2(Enum2 param2) {
        CompletableFuture<Enum2> resFuture = func2Async(param2);
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
    public  CompletableFuture<Enum2> func2Async(Enum2 param2) {

    	Log.i(TAG, "Call on service func2  "+ " " + param2);
		Message msg = new Message();
		msg.what = EnumInterfaceMessageType.RPC_Func2Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("param2", new Enum2Parcelable(param2));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Enum2>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                Log.w(TAG, "func2: received null bundle (service disconnected?)");
                future.completeExceptionally(new RemoteOperationException("service disconnected", RemoteOperationException.ERROR_SERVICE_DISCONNECTED));
                return;
            }
            if (bundle.getBoolean("error", false))
            {
                String errorMessage = bundle.getString("errorMessage", "unknown error");
                int errorCode = bundle.getInt("errorCode", RemoteOperationException.ERROR_UNKNOWN);
                Log.w(TAG, "func2 failed: " + errorMessage);
                future.completeExceptionally(new RemoteOperationException(errorMessage, errorCode));
                return;
            }
            
		    Enum2 result = bundle.getParcelable("result", Enum2Parcelable.class).getEnum2();
            Log.v(TAG, "resolve func2" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }


    @Override
    public Enum3 func3(Enum3 param3) {
        CompletableFuture<Enum3> resFuture = func3Async(param3);
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
    public  CompletableFuture<Enum3> func3Async(Enum3 param3) {

    	Log.i(TAG, "Call on service func3  "+ " " + param3);
		Message msg = new Message();
		msg.what = EnumInterfaceMessageType.RPC_Func3Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putParcelable("param3", new Enum3Parcelable(param3));
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Enum3>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            if (bundle == null)
            {
                Log.w(TAG, "func3: received null bundle (service disconnected?)");
                future.completeExceptionally(new RemoteOperationException("service disconnected", RemoteOperationException.ERROR_SERVICE_DISCONNECTED));
                return;
            }
            if (bundle.getBoolean("error", false))
            {
                String errorMessage = bundle.getString("errorMessage", "unknown error");
                int errorCode = bundle.getInt("errorCode", RemoteOperationException.ERROR_UNKNOWN);
                Log.w(TAG, "func3 failed: " + errorMessage);
                future.completeExceptionally(new RemoteOperationException(errorMessage, errorCode));
                return;
            }
            
		    Enum3 result = bundle.getParcelable("result", Enum3Parcelable.class).getEnum3();
            Log.v(TAG, "resolve func3" + result);
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
    public void onSig0(Enum0 param0)
    {
        Log.i(TAG, "onSig0  received from service");
        fireSig0(param0);
    }
    public void onSig1(Enum1 param1)
    {
        Log.i(TAG, "onSig1  received from service");
        fireSig1(param1);
    }
    public void onSig2(Enum2 param2)
    {
        Log.i(TAG, "onSig2  received from service");
        fireSig2(param2);
    }
    public void onSig3(Enum3 param3)
    {
        Log.i(TAG, "onSig3  received from service");
        fireSig3(param3);
    }
}
