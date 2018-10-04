package kr.co.dongduk.medineye.service;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

import kr.co.dongduk.medineye.data.PharmacyData;

public class PharmacyDataParser {

    PharmacyData pharmacyData;
	ArrayList<PharmacyData> pharmacyList;

	
	public void PullParserFromXML_MyPharmacy(String data){
		
		System.out.println(data);
		
		Boolean boolean_id = false, boolean_div = false, boolean_name = false,
				boolean_tel = false, boolean_addr = false, boolean_lon = false,
				boolean_lat = false, boolean_cnt = false, boolean_distance = false, 
				boolean_start = false, boolean_end = false;
		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            String sTag;
            parser.setInput(new StringReader(data));
            
            int eventType = parser.getEventType();
            
            while(eventType != XmlPullParser.END_DOCUMENT) {

                   switch(eventType) {

                   case XmlPullParser.START_DOCUMENT:            // 문서의 시작
                       pharmacyList = new ArrayList<PharmacyData>();
                       break;
                        
                   case XmlPullParser.END_DOCUMENT:        // 문서의 끝
                	   break;
                        
                   case XmlPullParser.START_TAG:                // 시작 태그를 만나면 이름을 살펴봐서 작업(아무 일도 안하거나 값을 읽어 저장
                       sTag = parser.getName();

                       if (sTag.equals("item"))
                           pharmacyData = new PharmacyData();
                       else if (sTag.equals("cnt"))
                           boolean_cnt = true;
                       else if (sTag.equals("distance"))
                           boolean_distance = true;
                       else if (sTag.equals("dutyAddr"))
                           boolean_addr = true;
                       else if (sTag.equals("dutyDiv"))
                           boolean_div = true;
                       else if (sTag.equals("dutyName"))
                           boolean_name = true;
                       else if (sTag.equals("dutyTel1"))
                           boolean_tel = true;
                       else if (sTag.equals("endTime"))
                           boolean_end = true;
                       else if (sTag.equals("hpid"))
                           boolean_id = true;
                       else if (sTag.equals("latitude"))
                           boolean_lat = true;
                       else if (sTag.equals("longitude"))
                           boolean_lon = true;
                       else if (sTag.equals("startTime"))
                           boolean_start = true;

                         break;
                        
                   case XmlPullParser.END_TAG:                   // End 태그를 만나면
                         sTag = parser.getName();
                         if (sTag.equalsIgnoreCase("item") && pharmacyData != null) {
                        	 pharmacyList.add(pharmacyData);
                         }
                         break;
                        
                   case XmlPullParser.TEXT:
                	   	if (boolean_cnt) {
                            pharmacyData.setCnt(Integer.parseInt(parser.getText()));
                            boolean_cnt = false;
                        }
                	   	else if (boolean_addr) {
                            pharmacyData.setDuty_addr(parser.getText());
                            boolean_addr = false;
                        }
                	   	else if (boolean_distance) {
                            pharmacyData.setDistance(Double.parseDouble(parser.getText()));
                            boolean_distance = false;
                        }
                	   	else if (boolean_div) {
                            pharmacyData.setDuty_div(parser.getText());
                            boolean_div = false;
                        }
                	   	else if (boolean_name) {
                            pharmacyData.setDuty_name(parser.getText());
                            boolean_name = false;
                        }
                	   	else if (boolean_tel) {
                            pharmacyData.setDuty_tel(parser.getText());
                            boolean_tel = false;
                        }
                	   	else if (boolean_end) {
                            pharmacyData.setEnd_time(Integer.parseInt(parser.getText()));
                            boolean_end = false;
                        }
                	   	else if (boolean_start) {
                            pharmacyData.setStart_time(Integer.parseInt(parser.getText()));
                            boolean_start = false;
                        }
                	   	else if (boolean_id) {
                            pharmacyData.setPharmacy_id(parser.getText());
                            boolean_id = false;
                        }
                	   	else if (boolean_lat) {
                            pharmacyData.setLatitude(Double.parseDouble(parser.getText()));
                            boolean_lat = false;
                        }
                	   	else if (boolean_lon) {
                            pharmacyData.setLongitude(Double.parseDouble(parser.getText()));
                            boolean_lon = false;
                        }

                         break;
                   }                             

                   eventType = parser.next();
            }
//            Log.i("PullXML", pharmacyList.get(0).getDuty_name());
            
		} catch(Exception ex) {
           ex.printStackTrace();
		}

	}

    public ArrayList<PharmacyData> getArrayList() {
        return pharmacyList;
    }

}
