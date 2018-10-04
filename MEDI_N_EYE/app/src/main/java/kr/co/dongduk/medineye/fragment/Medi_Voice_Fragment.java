package kr.co.dongduk.medineye.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Locale;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.activity.MediBarcodeResultActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Medi_Voice_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Medi_Voice_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Medi_Voice_Fragment extends Fragment implements TextToSpeech.OnInitListener {
    private static String title;
    private static int page;

    private OnFragmentInteractionListener mListener;

    final static int GOOGLE_STT = 100;


    /*TTS*/
    TextToSpeech _tts;
    boolean _ttsActive = false;
    String str;


    public Medi_Voice_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Main_Alarm_Fragment.
     * <p/>
     * Frag1 fragmentFirst = new Frag1();
     * Bundle args = new Bundle();
     * args.putInt("someInt", page);
     * args.putString("someTitle", title);
     * args.putBoolean("isAM",isAM);
     * fragmentFirst.setArguments(args);
     * return fragmentFirst;
     */
    // TODO: Rename and change types and number of parameters
    public static Medi_Voice_Fragment newInstance(int param1, String param2) {
        Medi_Voice_Fragment fragment = new Medi_Voice_Fragment();
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

        Log.d("Medi_Voice_Fragment", "Medi_Voice_Fragment");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //여기에 click 넣어서

        final View v = inflater.inflate(R.layout.fragment_medi_voice, container, false);

        Log.d("Medi_Voice_Fragment", "onCreateView");

        /*************** Android Fragment onCreateView with Gestures  ****************/

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        // *******   더블 탭 : 연속 두번 터치시 발생   ***********
                        //
                        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);         //intent 생성
                        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName());   //음성인식을 호출한 패키지
                        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");                     //음성인식 언어 설정
                        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "말을 하세요.");                  //사용자에게 보여 줄 글자

                        startActivityForResult(i, GOOGLE_STT);
                        return false;
                    }


                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        // *****   한번 터치가 확실 할 경우 발생 된다. DoubleTap이 아님!   ******
                        //TTS
                        str = "음성검색";
                        _ttsActive = true;
                        _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);

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
    public void onResume() {
        super.onResume();
        try {
            _tts = new TextToSpeech(getContext(), this);
        } catch (Exception e) {
            _ttsActive = true;
            _tts.speak("다시시도해주세요", TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if (_tts.isLanguageAvailable(Locale.KOREAN) == TextToSpeech.LANG_AVAILABLE)
                _tts.setLanguage(Locale.KOREAN);
        } else if (initStatus == TextToSpeech.ERROR) {
            //Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    ArrayList<String> mResult;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && (requestCode == GOOGLE_STT)) {      //결과가 있으면
            String key = "";
            if (requestCode == GOOGLE_STT)               //구글음성인식이면
                key = RecognizerIntent.EXTRA_RESULTS;   //키값 설정         //결과를 다이얼로그로 출력.

            mResult = new ArrayList<>();
            mResult = data.getStringArrayListExtra(key);      //인식된 데이터 list 받아옴.
            String[] result = new String[mResult.size()];         //배열생성. 다이얼로그에서 출력하기 위해
            mResult.toArray(result);

//            btnStt.setText(mResult.get(0).toString());

            Log.d("*******STT 결과 : ", mResult.get(0).toString()); // 얘를 서버로 넘겨요
            Intent newintent = new Intent(getActivity(), MediBarcodeResultActivity.class);
            newintent.putExtra("type", 0);
            newintent.putExtra("data", mResult.get(0).toString());
            startActivity(newintent);
        } else {                                             //결과가 없으면 에러 메시지 출력
            String msg = null;


            //내가 만든 activity에서 넘어오는 오류 코드를 분류
            switch (resultCode) {
                case SpeechRecognizer.ERROR_AUDIO:
                    msg = "오디오 입력 중 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    msg = "단말에서 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    msg = "권한이 없습니다.";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    msg = "네트워크 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    msg = "일치하는 항목이 없습니다.";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    msg = "음성인식 서비스가 과부하 되었습니다.";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    msg = "서버에서 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    msg = "입력이 없습니다.";
                    break;
            }

//            if (msg != null)      //오류 메시지가 null이 아니면 메시지 출력
//                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy() {
        //Close the Text to Speech Library
        if(_tts != null) {

            _tts.stop();
            _tts.shutdown();
            Log.d("", "TTS Destroyed");
        }
        super.onDestroy();
    }
}