package kr.co.dongduk.medineye.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.data.NFCData;

/**
 * Created by Owner on 2016-08-25.
 */
public class mNfcAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<NFCData> nfcList;
    private LayoutInflater inflater;

    public mNfcAdapter(Context context, int layout, ArrayList<NFCData> dataList) {
        this.context = context;
        this.layout = layout;
        this.nfcList = dataList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return nfcList.size();
    }

    @Override
    public Object getItem(int position) {
        return nfcList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //화면 만들기
        if(convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        //화면 항목찾기
        TextView NfcId = (TextView)convertView.findViewById(R.id.nfc_id);
        TextView Nfctext = (TextView)convertView.findViewById(R.id.nfc_text);

        //각 항목에 결합
        NfcId.setText(nfcList.get(position).getNFCId());


        if (nfcList.get(position).getNFCtext().toString().matches(".*payload.*")){
            Nfctext.setText("/sdcard/"+nfcList.get(position).getNFCId()+".mp4");
        } else {
            Nfctext.setText(nfcList.get(position).getNFCtext().toString());
        }
        notifyDataSetChanged(); // 갱신 확인

        return convertView;
    }
}
