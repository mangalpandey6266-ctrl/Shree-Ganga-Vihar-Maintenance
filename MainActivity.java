package com.shreegangavihar.maintenance;

import android.app.*;import android.os.*;import android.content.*;import android.net.Uri;import android.text.InputType;import android.view.*;import android.widget.*;import java.text.*;import java.util.*;

public class MainActivity extends Activity {
    SharedPreferences p; String[] flats=new String[72];
    @Override public void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_main);p=getSharedPreferences("data",0);buildFlats();
        Button r=findViewById(R.id.receiveBtn),s=findViewById(R.id.spendBtn),q=findViewById(R.id.pendingBtn),m=findViewById(R.id.monthlyBtn);
        r.setOnClickListener(v->entry(true));s.setOnClickListener(v->entry(false));q.setOnClickListener(v->pending(true));m.setOnClickListener(v->monthly());refresh();}
    void buildFlats(){int k=0;for(int i=29;i<=52;i++)flats[k++]=String.format("%03d",i);for(int i=129;i<=152;i++)flats[k++]=String.format("%03d",i);for(int i=229;i<=252;i++)flats[k++]=String.format("%03d",i);}
    String month(){return new SimpleDateFormat("yyyy-MM",Locale.US).format(new Date());}
    void entry(boolean received){
        LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);l.setPadding(30,10,30,10);
        Spinner flat=new Spinner(this);String[] choices=new String[73];choices[0]="Select Flat";System.arraycopy(flats,0,choices,1,72);flat.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,choices));
        EditText amt=new EditText(this);amt.setHint("Amount");amt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText remark=new EditText(this);remark.setHint(received?"Payment note":"Purpose");
        if(received)l.addView(flat);l.addView(amt);l.addView(remark);
        new AlertDialog.Builder(this).setTitle(received?"Receive Payment":"Add Expense").setView(l).setPositiveButton("SAVE",(d,w)->{
            if(amt.getText().toString().trim().isEmpty()){Toast.makeText(this,"Enter amount",Toast.LENGTH_SHORT).show();return;}
            double a=Double.parseDouble(amt.getText().toString());String rec=p.getString("records","");
            String flatNo=received?choices[flat.getSelectedItemPosition()]:"-";
            if(received&&flat.getSelectedItemPosition()==0){Toast.makeText(this,"Select flat number",Toast.LENGTH_SHORT).show();return;}
            String line=(received?"R":"S")+"|"+flatNo+"|"+a+"|"+month()+"|"+remark.getText().toString().replace("|","/")+"\n";
            p.edit().putString("records",rec+line).apply();refresh();Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
        }).setNegativeButton("CANCEL",null).show();
    }
    String pendingText(){String mon=month();HashSet<String> paid=new HashSet<>();for(String line:p.getString("records","").split("\\n")){String[]x=line.split("\\|",-1);if(x.length>3&&x[0].equals("R")&&x[3].equals(mon))paid.add(x[1]);}
        StringBuilder b=new StringBuilder("Shree Ganga Vihar Society\nMaintenance Pending – ").append(new SimpleDateFormat("MMMM yyyy",Locale.US).format(new Date())).append("\n\n");int n=0;
        for(String f:flats)if(!paid.contains(f)){b.append(f).append(" – ").append(Integer.parseInt(f)<100?"Ground":Integer.parseInt(f)<200?"First":"Second").append(" Floor\n");n++;}
        if(n==0)b.append("All maintenance received. 🎉\n");else b.append("\nPending flats: ").append(n);
        return b.toString();}
    void pending(boolean share){String text=pendingText();new AlertDialog.Builder(this).setTitle("Pending Maintenance").setMessage(text).setPositiveButton("WHATSAPP",(d,w)->shareWhatsApp(text)).setNegativeButton("CLOSE",null).show();}
    void shareWhatsApp(String text){try{Intent i=new Intent(Intent.ACTION_SEND);i.setType("text/plain");i.putExtra(Intent.EXTRA_TEXT,text);i.setPackage("com.whatsapp");startActivity(i);}catch(Exception e){Intent i=new Intent(Intent.ACTION_SEND);i.setType("text/plain");i.putExtra(Intent.EXTRA_TEXT,text);startActivity(Intent.createChooser(i,"Send reminder with"));}}
    void monthly(){HashMap<String,Double>in=new HashMap<>(),out=new HashMap<>();for(String line:p.getString("records","").split("\\n")){String[]x=line.split("\\|",-1);if(x.length>3){double a=Double.parseDouble(x[2]);if(x[0].equals("R"))in.put(x[3],in.getOrDefault(x[3],0d)+a);else out.put(x[3],out.getOrDefault(x[3],0d)+a);}}
        StringBuilder b=new StringBuilder();TreeSet<String> keys=new TreeSet<>();keys.addAll(in.keySet());keys.addAll(out.keySet());for(String k:keys)b.append(k).append("  Received ₹").append(String.format("%.2f",in.getOrDefault(k,0d))).append("  Spent ₹").append(String.format("%.2f",out.getOrDefault(k,0d))).append("\n");
        new AlertDialog.Builder(this).setTitle("Monthly Report").setMessage(b.length()==0?"No entries yet.":b.toString()).setPositiveButton("OK",null).show();}
    void refresh(){double in=0,out=0;for(String line:p.getString("records","").split("\\n")){String[]x=line.split("\\|",-1);if(x.length>2){double a=Double.parseDouble(x[2]);if(x[0].equals("R"))in+=a;else out+=a;}}((TextView)findViewById(R.id.summary)).setText("Received: ₹"+String.format("%.2f",in)+"\nSpent: ₹"+String.format("%.2f",out)+"\nBalance: ₹"+String.format("%.2f",in-out));}
}
