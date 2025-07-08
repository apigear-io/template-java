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
        return data;
    }

    protected {{Camel .Struct.Name}}Parcelable(Parcel in) {

{{- range .Struct.Fields }}
{{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
     {{javaType "" .}} l_{{camel .Name}} = in.read{{ ( Camel  (javaType "" .) ) }}();
{{- else if (eq .KindType "bool")}}
    {{javaType "" .}} l_{{camel .Name}} = in.readInt();
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
    {{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
        dest.write{{ ( Camel  (javaType "" .) ) }}(data.{{camel .Name}});
    {{- else if (eq .KindType "bool")}}
        dest.writeInt(data.{{camel .Name}});
    {{- end }}
    {{- end }}
        // TODO arrays in general
        // TODO add enums
        // TODO write other structs same as enums - they all should be parcelable 

    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
