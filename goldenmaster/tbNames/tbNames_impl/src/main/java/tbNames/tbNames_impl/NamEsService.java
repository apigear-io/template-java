package tbNames.tbNames_impl;

import android.os.Messenger;
import android.util.Log;

import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_api.AbstractNamEs;
import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_api.EnumWithUnderScores;


import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.Arrays;


public class NamEsService extends AbstractNamEs {

    private final static String TAG = "NamEsService";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean m_Switch = false;
    private int m_SOME_PROPERTY = 0;
    private int m_Some_Poperty2 = 0;
    private EnumWithUnderScores m_enum_property = EnumWithUnderScores.FirstValue;

    public NamEsService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setSwitch(boolean Switch)
    {
        Log.i(TAG, "request setSwitch called ");
        if (m_Switch != Switch)
        {
            m_Switch = Switch;
            onSwitchChanged(m_Switch);
        }

    }

    @Override
    public boolean getSwitch()
    {
        Log.i(TAG, "request getSwitch called,");
        return m_Switch;
    }

  
    @Override
    public void setSomeProperty(int SOME_PROPERTY)
    {
        Log.i(TAG, "request setSomeProperty called ");
        if (m_SOME_PROPERTY != SOME_PROPERTY)
        {
            m_SOME_PROPERTY = SOME_PROPERTY;
            onSomePropertyChanged(m_SOME_PROPERTY);
        }

    }

    @Override
    public int getSomeProperty()
    {
        Log.i(TAG, "request getSomeProperty called,");
        return m_SOME_PROPERTY;
    }

  
    @Override
    public void setSomePoperty2(int Some_Poperty2)
    {
        Log.i(TAG, "request setSomePoperty2 called ");
        if (m_Some_Poperty2 != Some_Poperty2)
        {
            m_Some_Poperty2 = Some_Poperty2;
            onSomePoperty2Changed(m_Some_Poperty2);
        }

    }

    @Override
    public int getSomePoperty2()
    {
        Log.i(TAG, "request getSomePoperty2 called,");
        return m_Some_Poperty2;
    }

  
    @Override
    public void setEnumProperty(EnumWithUnderScores enum_property)
    {
        Log.i(TAG, "request setEnumProperty called ");
        if (m_enum_property != enum_property)
        {
            m_enum_property = enum_property;
            onEnumPropertyChanged(m_enum_property);
        }

    }

    @Override
    public EnumWithUnderScores getEnumProperty()
    {
        Log.i(TAG, "request getEnumProperty called,");
        return m_enum_property;
    }

  
    // methods

    @Override
    public void someFunction(boolean SOME_PARAM) {
        Log.i(TAG, "request method someFunction called, returnig default");
        return ;
    }

    @Override
    public  CompletableFuture<Void> someFunctionAsync(boolean SOME_PARAM) {
        return CompletableFuture.runAsync(
                () -> { someFunction(SOME_PARAM); },
                executor);
    }

    @Override
    public void someFunction2(boolean Some_Param) {
        Log.i(TAG, "request method someFunction2 called, returnig default");
        return ;
    }

    @Override
    public  CompletableFuture<Void> someFunction2Async(boolean Some_Param) {
        return CompletableFuture.runAsync(
                () -> { someFunction2(Some_Param); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    //In theory event listener interface
    private void onSwitchChanged(boolean newValue)
    {
         Log.i(TAG, "onSwitchChanged, will pass notification to all listeners");
         fireSwitchChanged(newValue);
    }
    private void onSomePropertyChanged(int newValue)
    {
         Log.i(TAG, "onSomePropertyChanged, will pass notification to all listeners");
         fireSomePropertyChanged(newValue);
    }
    private void onSomePoperty2Changed(int newValue)
    {
         Log.i(TAG, "onSomePoperty2Changed, will pass notification to all listeners");
         fireSomePoperty2Changed(newValue);
    }
    private void onEnumPropertyChanged(EnumWithUnderScores newValue)
    {
         Log.i(TAG, "onEnumPropertyChanged, will pass notification to all listeners");
         fireEnumPropertyChanged(newValue);
    }
    public void onSomeSignal(boolean SOME_PARAM)
    {
        Log.i(TAG, "onSomeSignal, will pass notification to all listeners");
        fireSomeSignal(SOME_PARAM);
    }
    public void onSomeSignal2(boolean Some_Param)
    {
        Log.i(TAG, "onSomeSignal2, will pass notification to all listeners");
        fireSomeSignal2(Some_Param);
    }

}