package {{dot .Module.Name}}.{{dot .Module.Name}}_android_messenger;

import {{dot .Module.Name}}.{{dot .Module.Name}}_api.{{Camel .Struct.Name}};
import android.os.Parcel;
import android.os.Parcelable;



//TODO imports - may need some struct from this or imported module

  public  class {{Camel .Struct.Name}}Parcelable implements Parcelable {

    public {{Camel .Struct.Name}} data;

    public {{Camel .Struct.Name}}Parcelable({{Camel .Struct.Name}} data) {
        this.data = data;
    }

    public {{Camel .Struct.Name}} get{{Camel .Struct.Name}}()
    {
        return new {{Camel .Struct.Name}}(data);
    }

    protected {{Camel .Struct.Name}}Parcelable(Parcel in) {

{{- range .Struct.Fields }}
{{- if .IsPrimitive }}
     {{javaType "" .}} l_{{camel .Name}} = in.read{{ ( Camel  (javaType "" .) ) }}();
{{- end }}
{{- end }}
        // TODO arrays in general
        // TODO add enums(Arrays) = MyEnumWrapper singleEnumWrapper = in.readParcelable(MyEnumWrapper.class.getClassLoader()); MyEnum singleEnum = singleEnumWrapper != null ? singleEnumWrapper.value : null;
        // TODO read other structs same as enums - they all should be parcelable 

        this.data = new {{Camel .Struct.Name}}(
            {{- range $idx, $m :=.Struct.Fields }}{{- if $idx}}, {{ end -}}l_{{camel .Name}}{{- end }});
    }

    public static final Creator<{{Camel .Struct.Name}}Parcelable> CREATOR = new Creator<{{Camel .Struct.Name}}Parcelable>() {
        @Override
        public {{Camel .Struct.Name}}Parcelable createFromParcel(Parcel in) {
            return new {{Camel .Struct.Name}}Parcelable(in);
        }

        @Override
        public {{Camel .Struct.Name}}Parcelable[] newArray(int size) {
            return new {{Camel .Struct.Name}}Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    {{- range .Struct.Fields }}
    {{- if .IsPrimitive }}
        dest.write{{ ( Camel  (javaType "" .) ) }}(data.{{camel .Name}});
    {{- end }}
    {{- end }}
        // TODO arrays in general
        // TODO add enums

    }
        public static {{Camel .Struct.Name}}Parcelable[] wrapArray({{Camel .Struct.Name}}[] structs) {
        if (structs == null) return null;
        {{Camel .Struct.Name}}Parcelable[] out = new {{Camel .Struct.Name}}Parcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new {{Camel .Struct.Name}}Parcelable(structs[i]);
        }
        return out;
    }

    public static {{Camel .Struct.Name}}[] unwrapArray({{Camel .Struct.Name}}Parcelable[] parcelables) {
        if (parcelables == null) return null;
        {{Camel .Struct.Name}}[] out = new {{Camel .Struct.Name}}[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].get{{Camel .Struct.Name}}();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
