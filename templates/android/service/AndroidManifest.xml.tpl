<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="{{camel .Module.Name}}.android.service">

    <permission android:name="{{camel .Module.Name}}.android.service.PERMISSION_BIND"
        android:protectionLevel="normal" />

    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_DATA_SYNC" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <application
        android:allowBackup="true">
        
        {{- range .Module.Interfaces -}}
		<service
			android:name="{{camel .Module.Name}}.android.service.{{Camel .Name}}ServiceAdapter"
			android:enabled="true"
			android:exported="true">
		</service>
        {{- end }}
    </application>
</manifest>