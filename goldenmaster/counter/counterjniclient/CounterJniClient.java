package counter.counterjniclient;

import counter.counter_api.ICounter;
import counter.counter_api.AbstractCounter;
import counter.counter_api.ICounterEventListener;
import counter.counter_api.RemoteOperationException;

import counter.counter_android_client.CounterClient;
import counter.counter_android_messenger.Conversions;
import android.content.Context;

import android.os.Bundle;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class CounterJniClient extends AbstractCounter implements ICounterEventListener
{

    private static final String TAG = "CounterJniClient";

    private CounterClient mMessengerClient = null;


    private static String ModuleName = "counter.counterjniservice.CounterJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient != null ? mMessengerClient._isReady() : false;
    }
    // Interface method — List types
    @Override
    public void setVector(customTypes.customTypes_api.Vector3D vector)
    {
        Log.i(TAG, "got request setVector" + (vector));
        mMessengerClient.setVector(vector);
    }
    @Override
    public customTypes.customTypes_api.Vector3D getVector()
    {
        Log.i(TAG, "got request getVector");
        return mMessengerClient.getVector();
    }


    
    // Interface method — List types
    @Override
    public void setExternVector(org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector)
    {
        Log.i(TAG, "got request setExternVector" + (extern_vector));
        mMessengerClient.setExternVector(extern_vector);
    }
    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D getExternVector()
    {
        Log.i(TAG, "got request getExternVector");
        return mMessengerClient.getExternVector();
    }


    
    // Interface method — List types
    @Override
    public void setVectorArray(List<customTypes.customTypes_api.Vector3D> vectorArray)
    {
        Log.i(TAG, "got request setVectorArray" + (vectorArray));
        mMessengerClient.setVectorArray(vectorArray);
    }
    // JNI entry point — array types for C++ compatibility
    public void setVectorArray(customTypes.customTypes_api.Vector3D[] vectorArray)
    {
        Log.i(TAG, "got JNI request setVectorArray");
        setVectorArray(Conversions.toList(vectorArray));
    }
    @Override
    public List<customTypes.customTypes_api.Vector3D> getVectorArray()
    {
        Log.i(TAG, "got request getVectorArray");
        return mMessengerClient.getVectorArray();
    }


    
    // Interface method — List types
    @Override
    public void setExternVectorArray(List<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> extern_vectorArray)
    {
        Log.i(TAG, "got request setExternVectorArray" + (extern_vectorArray));
        mMessengerClient.setExternVectorArray(extern_vectorArray);
    }
    // JNI entry point — array types for C++ compatibility
    public void setExternVectorArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray)
    {
        Log.i(TAG, "got JNI request setExternVectorArray");
        setExternVectorArray(Conversions.toList(extern_vectorArray));
    }
    @Override
    public List<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> getExternVectorArray()
    {
        Log.i(TAG, "got request getExternVectorArray");
        return mMessengerClient.getExternVectorArray();
    }


    
    // Interface method — List types
    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D increment(org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec)
    {
        Log.v(TAG, "Blocking callincrement - should not be used ");
        return mMessengerClient.increment(vec);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnIncrementResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void incrementAsync(String callId, org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec){
        Log.v(TAG, "non blocking call increment ");
        mMessengerClient.incrementAsync(vec).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "increment async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnIncrementResult(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> incrementAsync(org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.incrementAsync(vec);
    }
    // Interface method — List types
    @Override
    public List<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> incrementArray(List<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> vec)
    {
        Log.v(TAG, "Blocking callincrementArray - should not be used ");
        return mMessengerClient.incrementArray(vec);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnIncrementArrayResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void incrementArrayAsync(String callId, org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec){
        Log.v(TAG, "non blocking call incrementArray ");
        mMessengerClient.incrementArrayAsync(Conversions.toList(vec)).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "incrementArray async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnIncrementArrayResult(Conversions.toArray(result, new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<org.apache.commons.math3.geometry.euclidean.threed.Vector3D>> incrementArrayAsync(List<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> vec)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.incrementArrayAsync(vec);
    }
    // Interface method — List types
    @Override
    public customTypes.customTypes_api.Vector3D decrement(customTypes.customTypes_api.Vector3D vec)
    {
        Log.v(TAG, "Blocking calldecrement - should not be used ");
        return mMessengerClient.decrement(vec);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnDecrementResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void decrementAsync(String callId, customTypes.customTypes_api.Vector3D vec){
        Log.v(TAG, "non blocking call decrement ");
        mMessengerClient.decrementAsync(vec).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "decrement async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnDecrementResult(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<customTypes.customTypes_api.Vector3D> decrementAsync(customTypes.customTypes_api.Vector3D vec)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.decrementAsync(vec);
    }
    // Interface method — List types
    @Override
    public List<customTypes.customTypes_api.Vector3D> decrementArray(List<customTypes.customTypes_api.Vector3D> vec)
    {
        Log.v(TAG, "Blocking calldecrementArray - should not be used ");
        return mMessengerClient.decrementArray(vec);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnDecrementArrayResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void decrementArrayAsync(String callId, customTypes.customTypes_api.Vector3D[] vec){
        Log.v(TAG, "non blocking call decrementArray ");
        mMessengerClient.decrementArrayAsync(Conversions.toList(vec)).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "decrementArray async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnDecrementArrayResult(Conversions.toArray(result, new customTypes.customTypes_api.Vector3D[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<customTypes.customTypes_api.Vector3D>> decrementArrayAsync(List<customTypes.customTypes_api.Vector3D> vec)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.decrementArrayAsync(vec);
    }

    public boolean bind(Context ctx, String packageName, String connectionID){
        Log.v(TAG, "natice client: bind " + packageName);
        return initServiceConnection(ctx, packageName, connectionID);
    }

    public void unbind(){
        Log.v(TAG, "native client: unbind " + lastServicePackage);
        if (mMessengerClient != null)
        {
            mMessengerClient.unbindFromService();
        }
    }

    private boolean initServiceConnection(Context ctx, String servicePackage, String connectionID)
    {
        if (mMessengerClient == null)
        {
            mMessengerClient = new CounterClient(ctx, connectionID);
            Log.i(TAG, "client created ");
            mMessengerClient.addEventListener(this);
        }
        if (!lastServicePackage.equals(servicePackage) &&  mMessengerClient.isBoundToService()) {
            unbind();
        }
        lastServicePackage = servicePackage;
        boolean res = mMessengerClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bind " + res+": to "+lastServicePackage);
        return res;
    }

    @Override
    public void on_readyStatusChanged(boolean isReady) {
        Log.i(TAG, "Will call native Connection state changed "+isReady);
        nativeIsReady(isReady);
    }

    // Event listener — receives List from messenger client, converts to array for native
    @Override
    public void onVectorChanged(customTypes.customTypes_api.Vector3D newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnVectorChanged(newValue);
    }
    @Override
    public void onExternVectorChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnExternVectorChanged(newValue);
    }
    @Override
    public void onVectorArrayChanged(List<customTypes.customTypes_api.Vector3D> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnVectorArrayChanged(Conversions.toArray(newValue, new customTypes.customTypes_api.Vector3D[0]));
    }
    @Override
    public void onExternVectorArrayChanged(List<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnExternVectorArrayChanged(Conversions.toArray(newValue, new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[0]));
    }
    @Override
    public void onValueChanged(customTypes.customTypes_api.Vector3D vector, org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector, List<customTypes.customTypes_api.Vector3D> vectorArray, List<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> extern_vectorArray)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal valueChanged "+ " " + vector+ " " + extern_vector+ " " + vectorArray+ " " + extern_vectorArray);
        nativeOnValueChanged(vector, extern_vector, Conversions.toArray(vectorArray, new customTypes.customTypes_api.Vector3D[0]), Conversions.toArray(extern_vectorArray, new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[0]));
    }


    // Native declarations — array types for JNI compatibility
     private native void nativeOnVectorChanged(customTypes.customTypes_api.Vector3D vector);
     private native void nativeOnExternVectorChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector);
     private native void nativeOnVectorArrayChanged(customTypes.customTypes_api.Vector3D[] vectorArray);
     private native void nativeOnExternVectorArrayChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray);
    private native void nativeOnValueChanged(customTypes.customTypes_api.Vector3D vector, org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector, customTypes.customTypes_api.Vector3D[] vectorArray, org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray);
    private native void nativeOnIncrementResult(org.apache.commons.math3.geometry.euclidean.threed.Vector3D result, String callId);
    private native void nativeOnIncrementArrayResult(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] result, String callId);
    private native void nativeOnDecrementResult(customTypes.customTypes_api.Vector3D result, String callId);
    private native void nativeOnDecrementArrayResult(customTypes.customTypes_api.Vector3D[] result, String callId);
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    private native void nativeIsReady(boolean isReady);
}
