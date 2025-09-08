<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="{{camel .System.Name }}_example">

    <application
        android:label="{{camel .System.Name }}_example"
        android:theme="@android:style/Theme.Material.Light">
        <activity android:name=".{{Camel .System.Name }}MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>
    </application>

</manifest>