package goldenmaster_example;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GoldenmasterMainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StringBuilder sb = new StringBuilder();
        
        testbed2.testbed2_android_client.ManyParamInterfaceClient testbed2_manyParamInterface_client =  new testbed2.testbed2_android_client.ManyParamInterfaceClient(this.getApplicationContext(), "conn_testbed2_manyParamInterface_client");
        sb.append("Made instance of testbed2.testbed2_android_client.ManyParamInterfaceClient\n");
        testbed2.testbed2_android_service.ManyParamInterfaceServiceAdapter testbed2_manyParamInterface_service =  new testbed2.testbed2_android_service.ManyParamInterfaceServiceAdapter();
        sb.append("Made instance of testbed2.testbed2_android_service.ManyParamInterfaceServiceAdapter\n");
        testbed2.testbed2_impl.ManyParamInterfaceService testbed2_manyParamInterface_local_impl =  new testbed2.testbed2_impl.ManyParamInterfaceService();
        sb.append("Made instance of testbed2.testbed2_impl.ManyParamInterfaceService\n");
        testbed2.testbed2_android_client.NestedStruct1InterfaceClient testbed2_nestedStruct1Interface_client =  new testbed2.testbed2_android_client.NestedStruct1InterfaceClient(this.getApplicationContext(), "conn_testbed2_nestedStruct1Interface_client");
        sb.append("Made instance of testbed2.testbed2_android_client.NestedStruct1InterfaceClient\n");
        testbed2.testbed2_android_service.NestedStruct1InterfaceServiceAdapter testbed2_nestedStruct1Interface_service =  new testbed2.testbed2_android_service.NestedStruct1InterfaceServiceAdapter();
        sb.append("Made instance of testbed2.testbed2_android_service.NestedStruct1InterfaceServiceAdapter\n");
        testbed2.testbed2_impl.NestedStruct1InterfaceService testbed2_nestedStruct1Interface_local_impl =  new testbed2.testbed2_impl.NestedStruct1InterfaceService();
        sb.append("Made instance of testbed2.testbed2_impl.NestedStruct1InterfaceService\n");
        testbed2.testbed2_android_client.NestedStruct2InterfaceClient testbed2_nestedStruct2Interface_client =  new testbed2.testbed2_android_client.NestedStruct2InterfaceClient(this.getApplicationContext(), "conn_testbed2_nestedStruct2Interface_client");
        sb.append("Made instance of testbed2.testbed2_android_client.NestedStruct2InterfaceClient\n");
        testbed2.testbed2_android_service.NestedStruct2InterfaceServiceAdapter testbed2_nestedStruct2Interface_service =  new testbed2.testbed2_android_service.NestedStruct2InterfaceServiceAdapter();
        sb.append("Made instance of testbed2.testbed2_android_service.NestedStruct2InterfaceServiceAdapter\n");
        testbed2.testbed2_impl.NestedStruct2InterfaceService testbed2_nestedStruct2Interface_local_impl =  new testbed2.testbed2_impl.NestedStruct2InterfaceService();
        sb.append("Made instance of testbed2.testbed2_impl.NestedStruct2InterfaceService\n");
        testbed2.testbed2_android_client.NestedStruct3InterfaceClient testbed2_nestedStruct3Interface_client =  new testbed2.testbed2_android_client.NestedStruct3InterfaceClient(this.getApplicationContext(), "conn_testbed2_nestedStruct3Interface_client");
        sb.append("Made instance of testbed2.testbed2_android_client.NestedStruct3InterfaceClient\n");
        testbed2.testbed2_android_service.NestedStruct3InterfaceServiceAdapter testbed2_nestedStruct3Interface_service =  new testbed2.testbed2_android_service.NestedStruct3InterfaceServiceAdapter();
        sb.append("Made instance of testbed2.testbed2_android_service.NestedStruct3InterfaceServiceAdapter\n");
        testbed2.testbed2_impl.NestedStruct3InterfaceService testbed2_nestedStruct3Interface_local_impl =  new testbed2.testbed2_impl.NestedStruct3InterfaceService();
        sb.append("Made instance of testbed2.testbed2_impl.NestedStruct3InterfaceService\n");
        
        tbEnum.tbEnum_android_client.EnumInterfaceClient tbEnum_enumInterface_client =  new tbEnum.tbEnum_android_client.EnumInterfaceClient(this.getApplicationContext(), "conn_tbEnum_enumInterface_client");
        sb.append("Made instance of tbEnum.tbEnum_android_client.EnumInterfaceClient\n");
        tbEnum.tbEnum_android_service.EnumInterfaceServiceAdapter tbEnum_enumInterface_service =  new tbEnum.tbEnum_android_service.EnumInterfaceServiceAdapter();
        sb.append("Made instance of tbEnum.tbEnum_android_service.EnumInterfaceServiceAdapter\n");
        tbEnum.tbEnum_impl.EnumInterfaceService tbEnum_enumInterface_local_impl =  new tbEnum.tbEnum_impl.EnumInterfaceService();
        sb.append("Made instance of tbEnum.tbEnum_impl.EnumInterfaceService\n");
        
        tbNames.tbNames_android_client.NamEsClient tbNames_namEs_client =  new tbNames.tbNames_android_client.NamEsClient(this.getApplicationContext(), "conn_tbNames_namEs_client");
        sb.append("Made instance of tbNames.tbNames_android_client.NamEsClient\n");
        tbNames.tbNames_android_service.NamEsServiceAdapter tbNames_namEs_service =  new tbNames.tbNames_android_service.NamEsServiceAdapter();
        sb.append("Made instance of tbNames.tbNames_android_service.NamEsServiceAdapter\n");
        tbNames.tbNames_impl.NamEsService tbNames_namEs_local_impl =  new tbNames.tbNames_impl.NamEsService();
        sb.append("Made instance of tbNames.tbNames_impl.NamEsService\n");
        
        tbSame1.tbSame1_android_client.SameStruct1InterfaceClient tbSame1_sameStruct1Interface_client =  new tbSame1.tbSame1_android_client.SameStruct1InterfaceClient(this.getApplicationContext(), "conn_tbSame1_sameStruct1Interface_client");
        sb.append("Made instance of tbSame1.tbSame1_android_client.SameStruct1InterfaceClient\n");
        tbSame1.tbSame1_android_service.SameStruct1InterfaceServiceAdapter tbSame1_sameStruct1Interface_service =  new tbSame1.tbSame1_android_service.SameStruct1InterfaceServiceAdapter();
        sb.append("Made instance of tbSame1.tbSame1_android_service.SameStruct1InterfaceServiceAdapter\n");
        tbSame1.tbSame1_impl.SameStruct1InterfaceService tbSame1_sameStruct1Interface_local_impl =  new tbSame1.tbSame1_impl.SameStruct1InterfaceService();
        sb.append("Made instance of tbSame1.tbSame1_impl.SameStruct1InterfaceService\n");
        tbSame1.tbSame1_android_client.SameStruct2InterfaceClient tbSame1_sameStruct2Interface_client =  new tbSame1.tbSame1_android_client.SameStruct2InterfaceClient(this.getApplicationContext(), "conn_tbSame1_sameStruct2Interface_client");
        sb.append("Made instance of tbSame1.tbSame1_android_client.SameStruct2InterfaceClient\n");
        tbSame1.tbSame1_android_service.SameStruct2InterfaceServiceAdapter tbSame1_sameStruct2Interface_service =  new tbSame1.tbSame1_android_service.SameStruct2InterfaceServiceAdapter();
        sb.append("Made instance of tbSame1.tbSame1_android_service.SameStruct2InterfaceServiceAdapter\n");
        tbSame1.tbSame1_impl.SameStruct2InterfaceService tbSame1_sameStruct2Interface_local_impl =  new tbSame1.tbSame1_impl.SameStruct2InterfaceService();
        sb.append("Made instance of tbSame1.tbSame1_impl.SameStruct2InterfaceService\n");
        tbSame1.tbSame1_android_client.SameEnum1InterfaceClient tbSame1_sameEnum1Interface_client =  new tbSame1.tbSame1_android_client.SameEnum1InterfaceClient(this.getApplicationContext(), "conn_tbSame1_sameEnum1Interface_client");
        sb.append("Made instance of tbSame1.tbSame1_android_client.SameEnum1InterfaceClient\n");
        tbSame1.tbSame1_android_service.SameEnum1InterfaceServiceAdapter tbSame1_sameEnum1Interface_service =  new tbSame1.tbSame1_android_service.SameEnum1InterfaceServiceAdapter();
        sb.append("Made instance of tbSame1.tbSame1_android_service.SameEnum1InterfaceServiceAdapter\n");
        tbSame1.tbSame1_impl.SameEnum1InterfaceService tbSame1_sameEnum1Interface_local_impl =  new tbSame1.tbSame1_impl.SameEnum1InterfaceService();
        sb.append("Made instance of tbSame1.tbSame1_impl.SameEnum1InterfaceService\n");
        tbSame1.tbSame1_android_client.SameEnum2InterfaceClient tbSame1_sameEnum2Interface_client =  new tbSame1.tbSame1_android_client.SameEnum2InterfaceClient(this.getApplicationContext(), "conn_tbSame1_sameEnum2Interface_client");
        sb.append("Made instance of tbSame1.tbSame1_android_client.SameEnum2InterfaceClient\n");
        tbSame1.tbSame1_android_service.SameEnum2InterfaceServiceAdapter tbSame1_sameEnum2Interface_service =  new tbSame1.tbSame1_android_service.SameEnum2InterfaceServiceAdapter();
        sb.append("Made instance of tbSame1.tbSame1_android_service.SameEnum2InterfaceServiceAdapter\n");
        tbSame1.tbSame1_impl.SameEnum2InterfaceService tbSame1_sameEnum2Interface_local_impl =  new tbSame1.tbSame1_impl.SameEnum2InterfaceService();
        sb.append("Made instance of tbSame1.tbSame1_impl.SameEnum2InterfaceService\n");
        
        tbSame2.tbSame2_android_client.SameStruct1InterfaceClient tbSame2_sameStruct1Interface_client =  new tbSame2.tbSame2_android_client.SameStruct1InterfaceClient(this.getApplicationContext(), "conn_tbSame2_sameStruct1Interface_client");
        sb.append("Made instance of tbSame2.tbSame2_android_client.SameStruct1InterfaceClient\n");
        tbSame2.tbSame2_android_service.SameStruct1InterfaceServiceAdapter tbSame2_sameStruct1Interface_service =  new tbSame2.tbSame2_android_service.SameStruct1InterfaceServiceAdapter();
        sb.append("Made instance of tbSame2.tbSame2_android_service.SameStruct1InterfaceServiceAdapter\n");
        tbSame2.tbSame2_impl.SameStruct1InterfaceService tbSame2_sameStruct1Interface_local_impl =  new tbSame2.tbSame2_impl.SameStruct1InterfaceService();
        sb.append("Made instance of tbSame2.tbSame2_impl.SameStruct1InterfaceService\n");
        tbSame2.tbSame2_android_client.SameStruct2InterfaceClient tbSame2_sameStruct2Interface_client =  new tbSame2.tbSame2_android_client.SameStruct2InterfaceClient(this.getApplicationContext(), "conn_tbSame2_sameStruct2Interface_client");
        sb.append("Made instance of tbSame2.tbSame2_android_client.SameStruct2InterfaceClient\n");
        tbSame2.tbSame2_android_service.SameStruct2InterfaceServiceAdapter tbSame2_sameStruct2Interface_service =  new tbSame2.tbSame2_android_service.SameStruct2InterfaceServiceAdapter();
        sb.append("Made instance of tbSame2.tbSame2_android_service.SameStruct2InterfaceServiceAdapter\n");
        tbSame2.tbSame2_impl.SameStruct2InterfaceService tbSame2_sameStruct2Interface_local_impl =  new tbSame2.tbSame2_impl.SameStruct2InterfaceService();
        sb.append("Made instance of tbSame2.tbSame2_impl.SameStruct2InterfaceService\n");
        tbSame2.tbSame2_android_client.SameEnum1InterfaceClient tbSame2_sameEnum1Interface_client =  new tbSame2.tbSame2_android_client.SameEnum1InterfaceClient(this.getApplicationContext(), "conn_tbSame2_sameEnum1Interface_client");
        sb.append("Made instance of tbSame2.tbSame2_android_client.SameEnum1InterfaceClient\n");
        tbSame2.tbSame2_android_service.SameEnum1InterfaceServiceAdapter tbSame2_sameEnum1Interface_service =  new tbSame2.tbSame2_android_service.SameEnum1InterfaceServiceAdapter();
        sb.append("Made instance of tbSame2.tbSame2_android_service.SameEnum1InterfaceServiceAdapter\n");
        tbSame2.tbSame2_impl.SameEnum1InterfaceService tbSame2_sameEnum1Interface_local_impl =  new tbSame2.tbSame2_impl.SameEnum1InterfaceService();
        sb.append("Made instance of tbSame2.tbSame2_impl.SameEnum1InterfaceService\n");
        tbSame2.tbSame2_android_client.SameEnum2InterfaceClient tbSame2_sameEnum2Interface_client =  new tbSame2.tbSame2_android_client.SameEnum2InterfaceClient(this.getApplicationContext(), "conn_tbSame2_sameEnum2Interface_client");
        sb.append("Made instance of tbSame2.tbSame2_android_client.SameEnum2InterfaceClient\n");
        tbSame2.tbSame2_android_service.SameEnum2InterfaceServiceAdapter tbSame2_sameEnum2Interface_service =  new tbSame2.tbSame2_android_service.SameEnum2InterfaceServiceAdapter();
        sb.append("Made instance of tbSame2.tbSame2_android_service.SameEnum2InterfaceServiceAdapter\n");
        tbSame2.tbSame2_impl.SameEnum2InterfaceService tbSame2_sameEnum2Interface_local_impl =  new tbSame2.tbSame2_impl.SameEnum2InterfaceService();
        sb.append("Made instance of tbSame2.tbSame2_impl.SameEnum2InterfaceService\n");
        
        tbSimple.tbSimple_android_client.VoidInterfaceClient tbSimple_voidInterface_client =  new tbSimple.tbSimple_android_client.VoidInterfaceClient(this.getApplicationContext(), "conn_tbSimple_voidInterface_client");
        sb.append("Made instance of tbSimple.tbSimple_android_client.VoidInterfaceClient\n");
        tbSimple.tbSimple_android_service.VoidInterfaceServiceAdapter tbSimple_voidInterface_service =  new tbSimple.tbSimple_android_service.VoidInterfaceServiceAdapter();
        sb.append("Made instance of tbSimple.tbSimple_android_service.VoidInterfaceServiceAdapter\n");
        tbSimple.tbSimple_impl.VoidInterfaceService tbSimple_voidInterface_local_impl =  new tbSimple.tbSimple_impl.VoidInterfaceService();
        sb.append("Made instance of tbSimple.tbSimple_impl.VoidInterfaceService\n");
        tbSimple.tbSimple_android_client.SimpleInterfaceClient tbSimple_simpleInterface_client =  new tbSimple.tbSimple_android_client.SimpleInterfaceClient(this.getApplicationContext(), "conn_tbSimple_simpleInterface_client");
        sb.append("Made instance of tbSimple.tbSimple_android_client.SimpleInterfaceClient\n");
        tbSimple.tbSimple_android_service.SimpleInterfaceServiceAdapter tbSimple_simpleInterface_service =  new tbSimple.tbSimple_android_service.SimpleInterfaceServiceAdapter();
        sb.append("Made instance of tbSimple.tbSimple_android_service.SimpleInterfaceServiceAdapter\n");
        tbSimple.tbSimple_impl.SimpleInterfaceService tbSimple_simpleInterface_local_impl =  new tbSimple.tbSimple_impl.SimpleInterfaceService();
        sb.append("Made instance of tbSimple.tbSimple_impl.SimpleInterfaceService\n");
        tbSimple.tbSimple_android_client.SimpleArrayInterfaceClient tbSimple_simpleArrayInterface_client =  new tbSimple.tbSimple_android_client.SimpleArrayInterfaceClient(this.getApplicationContext(), "conn_tbSimple_simpleArrayInterface_client");
        sb.append("Made instance of tbSimple.tbSimple_android_client.SimpleArrayInterfaceClient\n");
        tbSimple.tbSimple_android_service.SimpleArrayInterfaceServiceAdapter tbSimple_simpleArrayInterface_service =  new tbSimple.tbSimple_android_service.SimpleArrayInterfaceServiceAdapter();
        sb.append("Made instance of tbSimple.tbSimple_android_service.SimpleArrayInterfaceServiceAdapter\n");
        tbSimple.tbSimple_impl.SimpleArrayInterfaceService tbSimple_simpleArrayInterface_local_impl =  new tbSimple.tbSimple_impl.SimpleArrayInterfaceService();
        sb.append("Made instance of tbSimple.tbSimple_impl.SimpleArrayInterfaceService\n");
        tbSimple.tbSimple_android_client.NoPropertiesInterfaceClient tbSimple_noPropertiesInterface_client =  new tbSimple.tbSimple_android_client.NoPropertiesInterfaceClient(this.getApplicationContext(), "conn_tbSimple_noPropertiesInterface_client");
        sb.append("Made instance of tbSimple.tbSimple_android_client.NoPropertiesInterfaceClient\n");
        tbSimple.tbSimple_android_service.NoPropertiesInterfaceServiceAdapter tbSimple_noPropertiesInterface_service =  new tbSimple.tbSimple_android_service.NoPropertiesInterfaceServiceAdapter();
        sb.append("Made instance of tbSimple.tbSimple_android_service.NoPropertiesInterfaceServiceAdapter\n");
        tbSimple.tbSimple_impl.NoPropertiesInterfaceService tbSimple_noPropertiesInterface_local_impl =  new tbSimple.tbSimple_impl.NoPropertiesInterfaceService();
        sb.append("Made instance of tbSimple.tbSimple_impl.NoPropertiesInterfaceService\n");
        tbSimple.tbSimple_android_client.NoOperationsInterfaceClient tbSimple_noOperationsInterface_client =  new tbSimple.tbSimple_android_client.NoOperationsInterfaceClient(this.getApplicationContext(), "conn_tbSimple_noOperationsInterface_client");
        sb.append("Made instance of tbSimple.tbSimple_android_client.NoOperationsInterfaceClient\n");
        tbSimple.tbSimple_android_service.NoOperationsInterfaceServiceAdapter tbSimple_noOperationsInterface_service =  new tbSimple.tbSimple_android_service.NoOperationsInterfaceServiceAdapter();
        sb.append("Made instance of tbSimple.tbSimple_android_service.NoOperationsInterfaceServiceAdapter\n");
        tbSimple.tbSimple_impl.NoOperationsInterfaceService tbSimple_noOperationsInterface_local_impl =  new tbSimple.tbSimple_impl.NoOperationsInterfaceService();
        sb.append("Made instance of tbSimple.tbSimple_impl.NoOperationsInterfaceService\n");
        tbSimple.tbSimple_android_client.NoSignalsInterfaceClient tbSimple_noSignalsInterface_client =  new tbSimple.tbSimple_android_client.NoSignalsInterfaceClient(this.getApplicationContext(), "conn_tbSimple_noSignalsInterface_client");
        sb.append("Made instance of tbSimple.tbSimple_android_client.NoSignalsInterfaceClient\n");
        tbSimple.tbSimple_android_service.NoSignalsInterfaceServiceAdapter tbSimple_noSignalsInterface_service =  new tbSimple.tbSimple_android_service.NoSignalsInterfaceServiceAdapter();
        sb.append("Made instance of tbSimple.tbSimple_android_service.NoSignalsInterfaceServiceAdapter\n");
        tbSimple.tbSimple_impl.NoSignalsInterfaceService tbSimple_noSignalsInterface_local_impl =  new tbSimple.tbSimple_impl.NoSignalsInterfaceService();
        sb.append("Made instance of tbSimple.tbSimple_impl.NoSignalsInterfaceService\n");
        tbSimple.tbSimple_android_client.EmptyInterfaceClient tbSimple_emptyInterface_client =  new tbSimple.tbSimple_android_client.EmptyInterfaceClient(this.getApplicationContext(), "conn_tbSimple_emptyInterface_client");
        sb.append("Made instance of tbSimple.tbSimple_android_client.EmptyInterfaceClient\n");
        tbSimple.tbSimple_android_service.EmptyInterfaceServiceAdapter tbSimple_emptyInterface_service =  new tbSimple.tbSimple_android_service.EmptyInterfaceServiceAdapter();
        sb.append("Made instance of tbSimple.tbSimple_android_service.EmptyInterfaceServiceAdapter\n");
        tbSimple.tbSimple_impl.EmptyInterfaceService tbSimple_emptyInterface_local_impl =  new tbSimple.tbSimple_impl.EmptyInterfaceService();
        sb.append("Made instance of tbSimple.tbSimple_impl.EmptyInterfaceService\n");
        
        testbed1.testbed1_android_client.StructInterfaceClient testbed1_structInterface_client =  new testbed1.testbed1_android_client.StructInterfaceClient(this.getApplicationContext(), "conn_testbed1_structInterface_client");
        sb.append("Made instance of testbed1.testbed1_android_client.StructInterfaceClient\n");
        testbed1.testbed1_android_service.StructInterfaceServiceAdapter testbed1_structInterface_service =  new testbed1.testbed1_android_service.StructInterfaceServiceAdapter();
        sb.append("Made instance of testbed1.testbed1_android_service.StructInterfaceServiceAdapter\n");
        testbed1.testbed1_impl.StructInterfaceService testbed1_structInterface_local_impl =  new testbed1.testbed1_impl.StructInterfaceService();
        sb.append("Made instance of testbed1.testbed1_impl.StructInterfaceService\n");
        testbed1.testbed1_android_client.StructArrayInterfaceClient testbed1_structArrayInterface_client =  new testbed1.testbed1_android_client.StructArrayInterfaceClient(this.getApplicationContext(), "conn_testbed1_structArrayInterface_client");
        sb.append("Made instance of testbed1.testbed1_android_client.StructArrayInterfaceClient\n");
        testbed1.testbed1_android_service.StructArrayInterfaceServiceAdapter testbed1_structArrayInterface_service =  new testbed1.testbed1_android_service.StructArrayInterfaceServiceAdapter();
        sb.append("Made instance of testbed1.testbed1_android_service.StructArrayInterfaceServiceAdapter\n");
        testbed1.testbed1_impl.StructArrayInterfaceService testbed1_structArrayInterface_local_impl =  new testbed1.testbed1_impl.StructArrayInterfaceService();
        sb.append("Made instance of testbed1.testbed1_impl.StructArrayInterfaceService\n");
        testbed1.testbed1_android_client.StructArray2InterfaceClient testbed1_structArray2Interface_client =  new testbed1.testbed1_android_client.StructArray2InterfaceClient(this.getApplicationContext(), "conn_testbed1_structArray2Interface_client");
        sb.append("Made instance of testbed1.testbed1_android_client.StructArray2InterfaceClient\n");
        testbed1.testbed1_android_service.StructArray2InterfaceServiceAdapter testbed1_structArray2Interface_service =  new testbed1.testbed1_android_service.StructArray2InterfaceServiceAdapter();
        sb.append("Made instance of testbed1.testbed1_android_service.StructArray2InterfaceServiceAdapter\n");
        testbed1.testbed1_impl.StructArray2InterfaceService testbed1_structArray2Interface_local_impl =  new testbed1.testbed1_impl.StructArray2InterfaceService();
        sb.append("Made instance of testbed1.testbed1_impl.StructArray2InterfaceService\n");
        
        
        
        counter.counter_android_client.CounterClient counter_counter_client =  new counter.counter_android_client.CounterClient(this.getApplicationContext(), "conn_counter_counter_client");
        sb.append("Made instance of counter.counter_android_client.CounterClient\n");
        counter.counter_android_service.CounterServiceAdapter counter_counter_service =  new counter.counter_android_service.CounterServiceAdapter();
        sb.append("Made instance of counter.counter_android_service.CounterServiceAdapter\n");
        counter.counter_impl.CounterService counter_counter_local_impl =  new counter.counter_impl.CounterService();
        sb.append("Made instance of counter.counter_impl.CounterService\n");
        
        tbIfaceimport.tbIfaceimport_android_client.EmptyIfClient tbIfaceimport_emptyIf_client =  new tbIfaceimport.tbIfaceimport_android_client.EmptyIfClient(this.getApplicationContext(), "conn_tbIfaceimport_emptyIf_client");
        sb.append("Made instance of tbIfaceimport.tbIfaceimport_android_client.EmptyIfClient\n");
        tbIfaceimport.tbIfaceimport_android_service.EmptyIfServiceAdapter tbIfaceimport_emptyIf_service =  new tbIfaceimport.tbIfaceimport_android_service.EmptyIfServiceAdapter();
        sb.append("Made instance of tbIfaceimport.tbIfaceimport_android_service.EmptyIfServiceAdapter\n");
        tbIfaceimport.tbIfaceimport_impl.EmptyIfService tbIfaceimport_emptyIf_local_impl =  new tbIfaceimport.tbIfaceimport_impl.EmptyIfService();
        sb.append("Made instance of tbIfaceimport.tbIfaceimport_impl.EmptyIfService\n");
        
        tbRefIfaces.tbRefIfaces_android_client.SimpleLocalIfClient tbRefIfaces_simpleLocalIf_client =  new tbRefIfaces.tbRefIfaces_android_client.SimpleLocalIfClient(this.getApplicationContext(), "conn_tbRefIfaces_simpleLocalIf_client");
        sb.append("Made instance of tbRefIfaces.tbRefIfaces_android_client.SimpleLocalIfClient\n");
        tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfServiceAdapter tbRefIfaces_simpleLocalIf_service =  new tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfServiceAdapter();
        sb.append("Made instance of tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfServiceAdapter\n");
        tbRefIfaces.tbRefIfaces_impl.SimpleLocalIfService tbRefIfaces_simpleLocalIf_local_impl =  new tbRefIfaces.tbRefIfaces_impl.SimpleLocalIfService();
        sb.append("Made instance of tbRefIfaces.tbRefIfaces_impl.SimpleLocalIfService\n");
        tbRefIfaces.tbRefIfaces_android_client.ParentIfClient tbRefIfaces_parentIf_client =  new tbRefIfaces.tbRefIfaces_android_client.ParentIfClient(this.getApplicationContext(), "conn_tbRefIfaces_parentIf_client");
        sb.append("Made instance of tbRefIfaces.tbRefIfaces_android_client.ParentIfClient\n");
        tbRefIfaces.tbRefIfaces_android_service.ParentIfServiceAdapter tbRefIfaces_parentIf_service =  new tbRefIfaces.tbRefIfaces_android_service.ParentIfServiceAdapter();
        sb.append("Made instance of tbRefIfaces.tbRefIfaces_android_service.ParentIfServiceAdapter\n");
        tbRefIfaces.tbRefIfaces_impl.ParentIfService tbRefIfaces_parentIf_local_impl =  new tbRefIfaces.tbRefIfaces_impl.ParentIfService();
        sb.append("Made instance of tbRefIfaces.tbRefIfaces_impl.ParentIfService\n");

        // Show output on screen
        TextView tv = new TextView(this);
        tv.setText(sb.toString() + "\nPress Back to exit");
        setContentView(tv);

    }
}