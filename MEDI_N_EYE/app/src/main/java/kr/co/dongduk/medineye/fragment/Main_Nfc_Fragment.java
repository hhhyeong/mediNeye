package kr.co.dongduk.medineye.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.activity.NFCActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Main_Nfc_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Main_Nfc_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main_Nfc_Fragment extends Fragment implements TextToSpeech.OnInitListener{
    private static String title;
    private static int page;

    private OnFragmentInteractionListener mListener;
    //TTS
    TextToSpeech _tts;
    boolean _ttsActive = false;

    public Main_Nfc_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Main_Alarm_Fragment.
     *
     */
    // TODO: Rename and change types and number of parameters
    public static Main_Nfc_Fragment newInstance(int param1, String param2) {
        Main_Nfc_Fragment fragment = new Main_Nfc_Fragment();
        Bundle args = new Bundle();
        args.putInt("Page", page);
        args.putString("Title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt("Page");
            title = getArguments().getString("Title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //여기에 click 넣어서
        View v = inflater.inflate(R.layout.fragment_main_nfc, container, false);
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        // *******   더블 탭 : 연속 두번 터치시 발생   ***********
                        //화면전환
                        Intent intent = new Intent(getActivity(), NFCActivity.class);
                        startActivity(intent);
                        return false;
                    }


                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        // *****   한번 터치가 확실 할 경우 발생 된다. DoubleTap이 아님!   ******
                        //TTS
                        _ttsActive = true;
                        _tts.speak(getString(R.string.tts_nfc), TextToSpeech.QUEUE_FLUSH, null);

                        return false;
                    }
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        return false;
                    }
                    @Override
                    public void onLongPress(MotionEvent e) {
                        // 길게 탭 하였을 때
                    }

                    @Override
                    public boolean onDoubleTapEvent(MotionEvent e) {
                        // 더블 탭 : DOWN, UP, MOVE 이벤트가 모두 발생 된다,
                        return false;
                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        // 스크롤시 발생, 최초 위치와 나중위치의 거리를 확인 할 수 있는 distanceX, distanceY값이 들어
                        return false;
                    }

                    @Override
                    public void onShowPress(MotionEvent e) {
                        //아주 길게 눌렀을 경우 발생 된다.
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        // 한번 터치일 경우 발생, DoubleTap일 수도 있음 => 즉, 이 메소드로 구현시 싱글탭 기능과 중복되어 나타남.
                        return false;
                    }
                });

        /* Android Fragment onCreateView with Gestures (2) */
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
        return v;
    }
    private long btnPressTime = 0;

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume(){
        super.onResume();
        _tts = new TextToSpeech(getContext(), this);
    }

    @Override
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(_tts.isLanguageAvailable(Locale.KOREA)==TextToSpeech.LANG_AVAILABLE)
                _tts.setLanguage(Locale.KOREA);
            else if(_tts.isLanguageAvailable(Locale.KOREAN)==TextToSpeech.LANG_AVAILABLE)
                _tts.setLanguage(Locale.KOREAN);
            else if(_tts.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                _tts.setLanguage(Locale.US);
        }
        else if (initStatus == TextToSpeech.ERROR) {
//            Toast.makeText(getActivity(), "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
}