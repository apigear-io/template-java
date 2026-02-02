//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2_android_client;

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

import testbed2.testbed2_api.IManyParamInterfaceEventListener;
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_api.AbstractManyParamInterface;
import testbed2.testbed2_android_messenger.ManyParamInterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;


public class ManyParamInterfaceClient extends AbstractManyParamInterface implements ServiceConnection
{
	private static final String TAG = "ManyParamInterfaceClient";

	private final Context mApplicationContext;

    private boolean mIsBoundToService = false;
    private Messenger mServiceMessenger = null;
    private Messenger mClientMessenger = null;
    private String mConnectionId;
    private ClientHandler mClientHandler = new ClientHandler();

    private final Map<Integer, Consumer<Bundle>> mpendingCalls = new ConcurrentHashMap<>();
    AtomicInteger callIdsGetter = new AtomicInteger(0);
    private int m_prop1 = 0;
    private int m_prop2 = 0;
    private int m_prop3 = 0;
    private int m_prop4 = 0;


	public ManyParamInterfaceClient(Context applicationContext, String connectionId)
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
     * Binds to a running service of type ManyParamInterfaceServiceAdapter.
     *
     * @return true if binding was successful, false otherwise.
     */
    public boolean bindToService(String packageName)
    {
        Intent intent = new Intent();
        intent.setClassName(packageName, "testbed2.testbed2_android_service.ManyParamInterfaceServiceAdapter");
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
            Message msg = Message.obtain(null, ManyParamInterfaceMessageType.UNREGISTER_CLIENT.ordinal());
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
            Message msg = Message.obtain(null, ManyParamInterfaceMessageType.REGISTER_CLIENT.ordinal());
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

		    switch (ManyParamInterfaceMessageType.fromInteger(msg.what))
		    {
                case INIT:
                {
                    Bundle data = msg.getData();
                    
			        
                    
			        int prop1 = data.getInt("prop1", 0);
				    onProp1(prop1);
                    
			        int prop2 = data.getInt("prop2", 0);
				    onProp2(prop2);
                    
			        int prop3 = data.getInt("prop3", 0);
				    onProp3(prop3);
                    
			        int prop4 = data.getInt("prop4", 0);
				    onProp4(prop4);

                    break;
                }
			    case SET_Prop1:
			    {
				    Bundle data = msg.getData();

                    
			        int prop1 = data.getInt("prop1", 0);

				    onProp1(prop1);
				    break;
			    }
			    case SET_Prop2:
			    {
				    Bundle data = msg.getData();

                    
			        int prop2 = data.getInt("prop2", 0);

				    onProp2(prop2);
				    break;
			    }
			    case SET_Prop3:
			    {
				    Bundle data = msg.getData();

                    
			        int prop3 = data.getInt("prop3", 0);

				    onProp3(prop3);
				    break;
			    }
			    case SET_Prop4:
			    {
				    Bundle data = msg.getData();

                    
			        int prop4 = data.getInt("prop4", 0);

				    onProp4(prop4);
				    break;
			    }
			    // TODO params may be different structs from different modules, there should be a custom class loader 
			    // with a list of class loaders required for this message
			    // IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
			    case SIG_Sig1: {

				    Bundle data = msg.getData();
                    
                
			        int param1 = data.getInt("param1", 0);
				    onSig1(param1);
				    break;
			    }
			    case SIG_Sig2: {

				    Bundle data = msg.getData();
                    
                
			        int param1 = data.getInt("param1", 0);
                
			        int param2 = data.getInt("param2", 0);
				    onSig2(param1, param2);
				    break;
			    }
			    case SIG_Sig3: {

				    Bundle data = msg.getData();
                    
                
			        int param1 = data.getInt("param1", 0);
                
			        int param2 = data.getInt("param2", 0);
                
			        int param3 = data.getInt("param3", 0);
				    onSig3(param1, param2, param3);
				    break;
			    }
			    case SIG_Sig4: {

				    Bundle data = msg.getData();
                    
                
			        int param1 = data.getInt("param1", 0);
                
			        int param2 = data.getInt("param2", 0);
                
			        int param3 = data.getInt("param3", 0);
                
			        int param4 = data.getInt("param4", 0);
				    onSig4(param1, param2, param3, param4);
				    break;
			    }
			    case RPC_Func1Resp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received ManyParamInterfaceMessageType.RPC_Func1Resp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_Func2Resp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received ManyParamInterfaceMessageType.RPC_Func2Resp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_Func3Resp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received ManyParamInterfaceMessageType.RPC_Func3Resp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_Func4Resp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received ManyParamInterfaceMessageType.RPC_Func4Resp , could not find pending call for " + msg.obj);
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
    public void setProp1(int prop1)
    {
        Log.i(TAG, "request setProp1 called "+ prop1);
        if (m_prop1 != prop1)
        {
			Message msg = new Message();
			msg.what = ManyParamInterfaceMessageType.PROP_Prop1.getValue();
			Bundle data = new Bundle();
            
		        data.putInt("prop1", prop1);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onProp1(int prop1)
    {
        Log.i(TAG, "value received from service for Prop1 ");
        if (m_prop1 != prop1)
        {
            m_prop1 = prop1;
            fireProp1Changed(prop1);
        }

    }

    @Override
    public int getProp1()
    {
        Log.i(TAG, "request getProp1 called, returning local");
        return m_prop1;
    }

  
    @Override
    public void setProp2(int prop2)
    {
        Log.i(TAG, "request setProp2 called "+ prop2);
        if (m_prop2 != prop2)
        {
			Message msg = new Message();
			msg.what = ManyParamInterfaceMessageType.PROP_Prop2.getValue();
			Bundle data = new Bundle();
            
		        data.putInt("prop2", prop2);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onProp2(int prop2)
    {
        Log.i(TAG, "value received from service for Prop2 ");
        if (m_prop2 != prop2)
        {
            m_prop2 = prop2;
            fireProp2Changed(prop2);
        }

    }

    @Override
    public int getProp2()
    {
        Log.i(TAG, "request getProp2 called, returning local");
        return m_prop2;
    }

  
    @Override
    public void setProp3(int prop3)
    {
        Log.i(TAG, "request setProp3 called "+ prop3);
        if (m_prop3 != prop3)
        {
			Message msg = new Message();
			msg.what = ManyParamInterfaceMessageType.PROP_Prop3.getValue();
			Bundle data = new Bundle();
            
		        data.putInt("prop3", prop3);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onProp3(int prop3)
    {
        Log.i(TAG, "value received from service for Prop3 ");
        if (m_prop3 != prop3)
        {
            m_prop3 = prop3;
            fireProp3Changed(prop3);
        }

    }

    @Override
    public int getProp3()
    {
        Log.i(TAG, "request getProp3 called, returning local");
        return m_prop3;
    }

  
    @Override
    public void setProp4(int prop4)
    {
        Log.i(TAG, "request setProp4 called "+ prop4);
        if (m_prop4 != prop4)
        {
			Message msg = new Message();
			msg.what = ManyParamInterfaceMessageType.PROP_Prop4.getValue();
			Bundle data = new Bundle();
            
		        data.putInt("prop4", prop4);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onProp4(int prop4)
    {
        Log.i(TAG, "value received from service for Prop4 ");
        if (m_prop4 != prop4)
        {
            m_prop4 = prop4;
            fireProp4Changed(prop4);
        }

    }

    @Override
    public int getProp4()
    {
        Log.i(TAG, "request getProp4 called, returning local");
        return m_prop4;
    }

  
    // methods

   
    @Override
    public int func1(int param1) {
        CompletableFuture<Integer> resFuture = func1Async(param1);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Integer> func1Async(int param1) {

    	Log.i(TAG, "Call on service func1  "+ " " + param1);
		Message msg = new Message();
		msg.what = ManyParamInterfaceMessageType.RPC_Func1Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putInt("param1", param1);
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Integer>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            
		    int result = bundle.getInt("result", 0);
            Log.v(TAG, "resolve func1" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public int func2(int param1, int param2) {
        CompletableFuture<Integer> resFuture = func2Async(param1, param2);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Integer> func2Async(int param1, int param2) {

    	Log.i(TAG, "Call on service func2  "+ " " + param1+ " " + param2);
		Message msg = new Message();
		msg.what = ManyParamInterfaceMessageType.RPC_Func2Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putInt("param1", param1);
        
		        data.putInt("param2", param2);
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Integer>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            
		    int result = bundle.getInt("result", 0);
            Log.v(TAG, "resolve func2" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public int func3(int param1, int param2, int param3) {
        CompletableFuture<Integer> resFuture = func3Async(param1, param2, param3);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Integer> func3Async(int param1, int param2, int param3) {

    	Log.i(TAG, "Call on service func3  "+ " " + param1+ " " + param2+ " " + param3);
		Message msg = new Message();
		msg.what = ManyParamInterfaceMessageType.RPC_Func3Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putInt("param1", param1);
        
		        data.putInt("param2", param2);
        
		        data.putInt("param3", param3);
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Integer>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            
		    int result = bundle.getInt("result", 0);
            Log.v(TAG, "resolve func3" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);
		mClientHandler.sendToService(msg);

        return future;
    }

   
    @Override
    public int func4(int param1, int param2, int param3, int param4) {
        CompletableFuture<Integer> resFuture = func4Async(param1, param2, param3, param4);
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  CompletableFuture<Integer> func4Async(int param1, int param2, int param3, int param4) {

    	Log.i(TAG, "Call on service func4  "+ " " + param1+ " " + param2+ " " + param3+ " " + param4);
		Message msg = new Message();
		msg.what = ManyParamInterfaceMessageType.RPC_Func4Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putInt("param1", param1);
        
		        data.putInt("param2", param2);
        
		        data.putInt("param3", param3);
        
		        data.putInt("param4", param4);
		msg.setData(data);
        msg.replyTo = mClientMessenger;

        CompletableFuture<Integer>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            
		    int result = bundle.getInt("result", 0);
            Log.v(TAG, "resolve func4" + result);
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
    public void onSig1(int param1)
    {
        Log.i(TAG, "onSig1  received from service");
        fireSig1(param1);
    }
    public void onSig2(int param1, int param2)
    {
        Log.i(TAG, "onSig2  received from service");
        fireSig2(param1, param2);
    }
    public void onSig3(int param1, int param2, int param3)
    {
        Log.i(TAG, "onSig3  received from service");
        fireSig3(param1, param2, param3);
    }
    public void onSig4(int param1, int param2, int param3, int param4)
    {
        Log.i(TAG, "onSig4  received from service");
        fireSig4(param1, param2, param3, param4);
    }
}
