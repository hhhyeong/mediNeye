package kr.co.dongduk.medineye.adapter;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.activity.PharmacyDetailActivity;
import kr.co.dongduk.medineye.data.PharmacyData;

/**
 * Created by SAMSUNG-PC on 2016-08-09.
 */
public class PharmacyAdapter extends BaseAdapter implements TextToSpeech.OnInitListener{

    private Context context;
    private int layout;
    private ArrayList<PharmacyData> pharmacyList;
    private LayoutInflater inflater;

    public PharmacyAdapter(Context context, int layout, ArrayList<PharmacyData> dataList) {
        this.context = context;
        this.layout = layout;
        this.pharmacyList = dataList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _tts = new TextToSpeech(context, this);
    }

    @Override
    public int getCount() {
        return pharmacyList.size();
    }

    @Override
    public Object getItem(int position) {
        return pharmacyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //화면 만들기
        if(convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        //화면 항목찾기
        TextView textPName = (TextView)convertView.findViewById(R.id.textViewPName);
        TextView textDis = (TextView)convertView.findViewById(R.id.textViewDis);
        //각 항목에 결합
        textPName.setText(pharmacyList.get(position).getDuty_name());
        String s = Double.toString(pharmacyList.get(position).getDistance()*1000);
        textDis.setText(s+"m");

        convertView.setTag("" + position);

        LinearLayout mainLayout = (LinearLayout)convertView.findViewById(R.id.mainLayout);
        final GestureDetector gestureDectector = new GestureDetector(convertView.getContext(), new GestureListener(position));
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDectector.onTouchEvent(event);
                return true;
            }
        });
        return convertView;

    }

    @Override
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(_tts.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                _tts.setLanguage(Locale.US);
        }
        else if (initStatus == TextToSpeech.ERROR) {
//                Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    public TextToSpeech _tts;
    public boolean _ttsActive = false;

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        int pos;

        public GestureListener(int position) { this.pos = position; }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // *****   한번 터치가 확실 할 경우 발생 된다. DoubleTap이 아님!   ******
            Log.d("Double_Tap", "Yes, single Clicked" +pos);
            _ttsActive = true;
            _tts.speak(pharmacyList.get(pos).getDuty_name(), TextToSpeech.QUEUE_FLUSH, null);
            return false;
        }

        public boolean onDoubleTap(MotionEvent e) {
            Intent intent = new Intent(context, PharmacyDetailActivity.class);
            intent.putExtra("data", pharmacyList.get(pos));
            context.startActivity(intent);
            Log.d("Double_Tap", "Yes, double Clicked " +pos);
            return true;
        }
    }
}
