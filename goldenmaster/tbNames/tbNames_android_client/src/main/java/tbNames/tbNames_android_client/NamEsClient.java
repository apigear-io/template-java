//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbNames.tbNames_android_client;

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
import tbNames.tbNames_api.EnumWithUnderScores;
import tbNames.tbNames_android_messenger.EnumWithUnderScoresParcelable;

import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_api.AbstractNamEs;
import tbNames.tbNames_android_messenger.NamEsMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Arrays;


public class NamEsClient extends AbstractNamEs implements ServiceConnection
{
	private static final String TAG = "NamEsClient";

	private final Context mApplicationContext;

    private boolean mIsBoundToService = false;
    private Messenger mServiceMessenger = null;
    private Messenger mClientMessenger = null;
    private String mConnectionId;
    private ClientHandler mClientHandler = new ClientHandler();

    private final Map<Integer, Consumer<Bundle>> mpendingCalls = new ConcurrentHashMap<>();
    AtomicInteger callIdsGetter = new AtomicInteger(0);
    private boolean m_Switch = false;
    private int m_SOME_PROPERTY = 0;
    private int m_Some_Poperty2 = 0;
    private EnumWithUnderScores m_enum_property = EnumWithUnderScores.FirstValue;


	public NamEsClient(Context applicationContext, String connectionId)
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
     * Binds to a running service of type NamEsServiceAdapter.
     *
     * @return true if binding was successful, false otherwise.
     */
    public boolean bindToService(String packageName)
    {
        Intent intent = new Intent();
        intent.setClassName(packageName, "tbNames.tbNames_android_service.NamEsServiceAdapter");
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
            Message msg = Message.obtain(null, NamEsMessageType.UNREGISTER_CLIENT.ordinal());
            msg.getData().putString("connectionID", mConnectionId);
            mClientHandler.sendToService(msg);
            mApplicationContext.unbindService(this);
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
        Log.w(TAG, "onServiceDisconnected name=" + name);
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
            Message msg = Message.obtain(null, NamEsMessageType.REGISTER_CLIENT.ordinal());
            msg.replyTo = mClientMessenger;
            msg.getData().putString("connectionID", mConnectionId);
            mClientHandler.sendToService(msg);
        }
    }

    private void doCleanupForUnbinding(String caller)
    {
        mServiceMessenger = null;
        mClientMessenger = null;
        mIsBoundToService = false;
        fire_readyStatusChanged(false);
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

		    switch (NamEsMessageType.fromInteger(msg.what))
		    {
                case INIT:
                {
                    Bundle data = msg.getData();
                    
        data.setClassLoader(EnumWithUnderScoresParcelable.class.getClassLoader());
			        
                    
			        boolean Switch = data.getBoolean("Switch", false);
				    onSwitch(Switch);
                    
			        int SOME_PROPERTY = data.getInt("SOME_PROPERTY", 0);
				    onSomeProperty(SOME_PROPERTY);
                    
			        int Some_Poperty2 = data.getInt("Some_Poperty2", 0);
				    onSomePoperty2(Some_Poperty2);
                    
			        EnumWithUnderScores enum_property = data.getParcelable("enum_property", EnumWithUnderScoresParcelable.class).getEnumWithUnderScores();
				    onEnumProperty(enum_property);

                    break;
                }
			    case SET_Switch:
			    {
				    Bundle data = msg.getData();

                    
			        boolean Switch = data.getBoolean("Switch", false);

				    onSwitch(Switch);
				    break;
			    }
			    case SET_SomeProperty:
			    {
				    Bundle data = msg.getData();

                    
			        int SOME_PROPERTY = data.getInt("SOME_PROPERTY", 0);

				    onSomeProperty(SOME_PROPERTY);
				    break;
			    }
			    case SET_SomePoperty2:
			    {
				    Bundle data = msg.getData();

                    
			        int Some_Poperty2 = data.getInt("Some_Poperty2", 0);

				    onSomePoperty2(Some_Poperty2);
				    break;
			    }
			    case SET_EnumProperty:
			    {
				    Bundle data = msg.getData();
				    data.setClassLoader(EnumWithUnderScoresParcelable.class.getClassLoader());

                    
			        EnumWithUnderScores enum_property = data.getParcelable("enum_property", EnumWithUnderScoresParcelable.class).getEnumWithUnderScores();

				    onEnumProperty(enum_property);
				    break;
			    }
			    // TODO params may be different structs from different modules, there should be a custom class loader 
			    // with a list of class loaders required for this message
			    // IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
			    case SIG_SomeSignal: {

				    Bundle data = msg.getData();
                    
                
			        boolean SOME_PARAM = data.getBoolean("SOME_PARAM", false);
				    onSomeSignal(SOME_PARAM);
				    break;
			    }
			    case SIG_SomeSignal2: {

				    Bundle data = msg.getData();
                    
                
			        boolean Some_Param = data.getBoolean("Some_Param", false);
				    onSomeSignal2(Some_Param);
				    break;
			    }
			    case RPC_SomeFunctionResp: {

				    Bundle data = msg.getData();
                    
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received NamEsMessageType.RPC_SomeFunctionResp , could not find pending call for " + msg.obj);
                    }
				    break;

			    }
			    case RPC_SomeFunction2Resp: {

				    Bundle data = msg.getData();
                    
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received NamEsMessageType.RPC_SomeFunction2Resp , could not find pending call for " + msg.obj);
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
    public void setSwitch(boolean Switch)
    {
        Log.i(TAG, "request setSwitch called "+ Switch);
        if (m_Switch != Switch)
        {
			Message msg = new Message();
			msg.what = NamEsMessageType.PROP_Switch.getValue();
			Bundle data = new Bundle();
            
		        data.putBoolean("Switch", Switch);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onSwitch(boolean Switch)
    {
        Log.i(TAG, "value received from service for Switch ");
        if (m_Switch != Switch)
        {
            m_Switch = Switch;
            fireSwitchChanged(Switch);
        }

    }

    @Override
    public boolean getSwitch()
    {
        Log.i(TAG, "request getSwitch called, returning local");
        return m_Switch;
    }

  
    @Override
    public void setSomeProperty(int SOME_PROPERTY)
    {
        Log.i(TAG, "request setSomeProperty called "+ SOME_PROPERTY);
        if (m_SOME_PROPERTY != SOME_PROPERTY)
        {
			Message msg = new Message();
			msg.what = NamEsMessageType.PROP_SomeProperty.getValue();
			Bundle data = new Bundle();
            
		        data.putInt("SOME_PROPERTY", SOME_PROPERTY);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onSomeProperty(int SOME_PROPERTY)
    {
        Log.i(TAG, "value received from service for SomeProperty ");
        if (m_SOME_PROPERTY != SOME_PROPERTY)
        {
            m_SOME_PROPERTY = SOME_PROPERTY;
            fireSomePropertyChanged(SOME_PROPERTY);
        }

    }

    @Override
    public int getSomeProperty()
    {
        Log.i(TAG, "request getSomeProperty called, returning local");
        return m_SOME_PROPERTY;
    }

  
    @Override
    public void setSomePoperty2(int Some_Poperty2)
    {
        Log.i(TAG, "request setSomePoperty2 called "+ Some_Poperty2);
        if (m_Some_Poperty2 != Some_Poperty2)
        {
			Message msg = new Message();
			msg.what = NamEsMessageType.PROP_SomePoperty2.getValue();
			Bundle data = new Bundle();
            
		        data.putInt("Some_Poperty2", Some_Poperty2);
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onSomePoperty2(int Some_Poperty2)
    {
        Log.i(TAG, "value received from service for SomePoperty2 ");
        if (m_Some_Poperty2 != Some_Poperty2)
        {
            m_Some_Poperty2 = Some_Poperty2;
            fireSomePoperty2Changed(Some_Poperty2);
        }

    }

    @Override
    public int getSomePoperty2()
    {
        Log.i(TAG, "request getSomePoperty2 called, returning local");
        return m_Some_Poperty2;
    }

  
    @Override
    public void setEnumProperty(EnumWithUnderScores enum_property)
    {
        Log.i(TAG, "request setEnumProperty called "+ enum_property);
        if (m_enum_property != enum_property)
        {
			Message msg = new Message();
			msg.what = NamEsMessageType.PROP_EnumProperty.getValue();
			Bundle data = new Bundle();
            
		        data.putParcelable("enum_property", new EnumWithUnderScoresParcelable(enum_property));
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void onEnumProperty(EnumWithUnderScores enum_property)
    {
        Log.i(TAG, "value received from service for EnumProperty ");
        if (m_enum_property != enum_property)
        {
            m_enum_property = enum_property;
            fireEnumPropertyChanged(enum_property);
        }

    }

    @Override
    public EnumWithUnderScores getEnumProperty()
    {
        Log.i(TAG, "request getEnumProperty called, returning local");
        return m_enum_property;
    }

  
    // methods

   
    @Override
    public void someFunction(boolean SOME_PARAM) {
        CompletableFuture<Void> resFuture = someFunctionAsync(SOME_PARAM);
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
    public  CompletableFuture<Void> someFunctionAsync(boolean SOME_PARAM) {

    	Log.i(TAG, "Call on service someFunction  "+ " " + SOME_PARAM);
		Message msg = new Message();
		msg.what = NamEsMessageType.RPC_SomeFunctionReq.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putBoolean("SOME_PARAM", SOME_PARAM);
		msg.setData(data);
        msg.replyTo = mClientMessenger;
		mClientHandler.sendToService(msg);

        CompletableFuture<Void>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            future.complete(null);
            Log.v(TAG, "resolve SOME_FUNCTION");
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);

        return future;
    }

   
    @Override
    public void someFunction2(boolean Some_Param) {
        CompletableFuture<Void> resFuture = someFunction2Async(Some_Param);
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
    public  CompletableFuture<Void> someFunction2Async(boolean Some_Param) {

    	Log.i(TAG, "Call on service someFunction2  "+ " " + Some_Param);
		Message msg = new Message();
		msg.what = NamEsMessageType.RPC_SomeFunction2Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
        
		        data.putBoolean("Some_Param", Some_Param);
		msg.setData(data);
        msg.replyTo = mClientMessenger;
		mClientHandler.sendToService(msg);

        CompletableFuture<Void>  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {
            future.complete(null);
            Log.v(TAG, "resolve Some_Function2");
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);

        return future;
    }    

    @Override
    public boolean _isReady() {
        return mIsBoundToService  && mServiceMessenger != null;
    }

	// Should be called when message arrives
    public void onSomeSignal(boolean SOME_PARAM)
    {
        Log.i(TAG, "onSomeSignal  received from service");
        fireSomeSignal(SOME_PARAM);
    }
    public void onSomeSignal2(boolean Some_Param)
    {
        Log.i(TAG, "onSomeSignal2  received from service");
        fireSomeSignal2(Some_Param);
    }
}
