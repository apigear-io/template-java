//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package {{camel .Module.Name}}.{{camel .Module.Name}}_impl;

import {{camel .Module.Name}}.{{camel .Module.Name}}_impl.{{Camel .Interface.Name}}Service;

//import message type and parcelabe types
{{- range .Module.Structs }}
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.{{Camel .Name}};
{{- end }}
{{- range .Module.Enums }}
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.{{Camel .Name}};
{{- end }}

import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }};

import android.util.Log;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.InOrder;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.RuntimeEnvironment;

import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;



@Config(sdk = 33, manifest = Config.NONE)
public class {{Camel .Interface.Name }}Test
{
       
    private {{Camel .Interface.Name }}Service testedObject;
    private I{{Camel .Interface.Name }}EventListener listenerMock = mock(I{{Camel .Interface.Name }}EventListener.class);
    InOrder inOrderEventListener;
   

    {{- $InterfaceName := Camel .Interface.Name}}
    @After
    public void tearDown() {

        //inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(false);

        testedObject.removeEventListener(listenerMock);
    }

    @Before
    public void setUp()
    {
        inOrderEventListener = inOrder(listenerMock);

        testedObject = new {{Camel .Interface.Name }}Service();
        testedObject.addEventListener(listenerMock);

        //inOrderEventListener.verify(listenerMock, times(1)).on_readyStatusChanged(true);
        assertTrue(testedObject._isReady());
    }

{{- range .Interface.Properties }}
//TODO do not add when a property is readonly
     @Test
     public void setProperty{{.Name}}()
    {
        {{- if and .IsPrimitive}}
        {{javaReturn "" . }} newValue = {{javaTestValue "" . }};
		{{- else }}
        {{javaReturn "" . }} newValue = {{javaDefault "" . }};
		{{- end }}

        testedObject.set{{Camel .Name}}(newValue);
        inOrderEventListener.verify(listenerMock,times(1)).on{{Camel .Name}}Changed(newValue);
	    
        assertEquals(testedObject.get{{Camel .Name}}(), newValue);
    }
{{- end}}

{{- range .Interface.Signals }}
    @Test
    public void on{{.Name}}()
    {
    {{- range .Params }}
        {{- if .IsPrimitive}}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
		{{- else }}
        {{javaReturn "" . }} {{javaVar . }} = {{javaDefault "" . }};
		{{- end }}
    {{- end }}

        testedObject.fire{{Camel .Name}}({{javaVars .Params}});
        inOrderEventListener.verify(listenerMock,times(1)).on{{Camel .Name}}({{javaVars .Params}});

}
{{- end}}


{{- range .Interface.Operations }}
    @Test
    public void on{{.Name}}Async() throws ExecutionException, InterruptedException
    {

        // Execute method
    {{- range .Params }}
    {{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
	{{- else }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaDefault "" . }};
	{{- end }}
	{{- end }}

        //set your expected result
        {{javaReturn "" .Return }} expectedResult = {{javaDefault "" .Return }};
		
        {{javaAsyncReturn "" .Return}} resFuture = testedObject.{{camel .Name}}Async({{javaVars .Params}});

        {{javaReturn "" .Return}} result = resFuture.get();
        assertEquals(expectedResult, result);

    }
     @Test
     public void on{{.Name}}(){

        // Execute method
    {{- range .Params }}
    {{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaTestValue "" . }};
	{{- else }}
        {{javaReturn "" . }} {{javaVar .}} = {{javaDefault "" . }};
	{{- end }}
	{{- end }}

        //set your expected result
        {{javaReturn "" .Return }} expectedResult = {{javaDefault "" .Return }};
		
        {{javaReturn "" .Return}} result = testedObject.{{camel .Name}}({{javaVars .Params}});
        assertEquals(expectedResult, result);

    }

{{- end}}

}
