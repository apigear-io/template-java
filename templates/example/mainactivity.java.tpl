package {{camel .System.Name }}_example;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class {{Camel .System.Name }}MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StringBuilder sb = new StringBuilder();
        {{- $withAndorid := .Features.android }}

        {{- range .System.Modules}}
        {{$moduleName := camel .Name}}
        {{- range .Interfaces}}
        {{- if $withAndorid}}
        {{$moduleName}}.{{$moduleName}}_android_client.{{Camel .Name}}Client {{$moduleName}}_{{camel .Name}}_client =  new {{$moduleName}}.{{$moduleName}}_android_client.{{Camel .Name}}Client(this.getApplicationContext(), "conn_{{$moduleName}}_{{camel .Name}}_client");
        sb.append("Made instance of {{$moduleName}}.{{$moduleName}}_android_client.{{Camel .Name}}Client\n");
        {{$moduleName}}.{{$moduleName}}_android_service.{{Camel .Name}}ServiceAdapter {{$moduleName}}_{{camel .Name}}_service =  new {{$moduleName}}.{{$moduleName}}_android_service.{{Camel .Name}}ServiceAdapter();
        sb.append("Made instance of {{$moduleName}}.{{$moduleName}}_android_service.{{Camel .Name}}ServiceAdapter\n");
        {{- end}}
        {{$moduleName}}.{{$moduleName}}_impl.{{Camel .Name}}Service {{$moduleName}}_{{camel .Name}}_local_impl =  new {{$moduleName}}.{{$moduleName}}_impl.{{Camel .Name}}Service();
        sb.append("Made instance of {{$moduleName}}.{{$moduleName}}_impl.{{Camel .Name}}Service\n");
        {{- end }}
        {{- end }}

        // Show output on screen
        TextView tv = new TextView(this);
        tv.setText(sb.toString() + "\nPress Back to exit");
        setContentView(tv);

    }
}