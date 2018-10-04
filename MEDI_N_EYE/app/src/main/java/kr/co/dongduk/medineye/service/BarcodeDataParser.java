package kr.co.dongduk.medineye.service;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

import kr.co.dongduk.medineye.data.BarcodeData;

/**
 * Created by Owner on 2016-09-19.
 */
public class BarcodeDataParser {
    BarcodeData barcodeData;
    Context context;

    public void PullParserFromXML(String data) {
        boolean boolean_name = false, boolean_description = false, boolean_categorize = false, boolean_company = false;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            String sTag;
            parser.setInput(new StringReader(data));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:            // 문서의 시작
                        barcodeData = new BarcodeData();
                        break;

                    case XmlPullParser.END_DOCUMENT:        // 문서의 끝
                        break;

                    case XmlPullParser.START_TAG:                 // 시작 태그를 만나면 이름을 살펴봐서 작업(아무 일도 안하거나 값을 읽어 저장
                        sTag = parser.getName();

                        if (sTag.equals("ERROR")) {
                            Log.d("ERROR", "서버 에러 발생");
                            return;
                        }

                        if (sTag.equals("B1")) {
                            boolean_name = true;
                        } else if (sTag.equals("B2")) {
                            boolean_description = true;
                        } else if (sTag.equals("B3")) {
                            boolean_company = true;
                        } else if (sTag.equals("B5")) {
                            boolean_categorize = true;
                        }
                        break;

                    case XmlPullParser.END_TAG:                   // End 태그를 만나면
                        sTag = parser.getName();
                        if (sTag.equalsIgnoreCase("ITEM") && barcodeData != null) {
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (boolean_name) {
                            barcodeData.setName(parser.getText());
                            boolean_name = false;
                        } else if (boolean_description) {
                            barcodeData.setDescription(parser.getText());
                            boolean_description = false;
                        } else if (boolean_company) {
                            barcodeData.setCompany(parser.getText());
                            boolean_company = false;
                        } else if (boolean_categorize) {

                            String categoryInfo = parser.getText(); //리스트에 잘라 넣기 위해서 일단 String형태로 가져옴

                            //여기서 앞에 카테고리 번호 [114] 이런거 없애기 위한 추가 코드 설정
                            int i = categoryInfo.indexOf("]");
                            String resultString = categoryInfo.substring(i + 1);//이제 [114] 에서 ]부분까지 잘림. 뒤의 내용이 resultString에 들어감. ex) [115]소염제.두통.생리통 -> 소염제.두통.생리통 만 남음.
                            barcodeData.setCategorizeList(resultString);

                            boolean_categorize = false;

                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public BarcodeData getBarcodeData()
    {
        return barcodeData;
    }
}
