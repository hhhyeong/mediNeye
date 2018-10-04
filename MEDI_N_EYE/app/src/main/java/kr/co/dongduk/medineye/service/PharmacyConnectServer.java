package kr.co.dongduk.medineye.service;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class PharmacyConnectServer {
 	double latitude = 0.0;
	double longitude = 0.0;

	public PharmacyConnectServer(double lat, double lng){
		this.latitude = lat;
		this.longitude = lng;
	}
	private class LongRunningGetIO extends AsyncTask <Void, Void, String> {
		protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
			InputStream in = entity.getContent();

			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n>0) {
				byte[] b = new byte[4096];
				n =  in.read(b);
				if (n>0) out.append(new String(b, 0, n));
			}

			return out.toString();
		}

		@Override
		protected String doInBackground(Void... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();

			String addr = "";
			String api_key = "";
			API_KEY_STORAGE key_storage = new API_KEY_STORAGE();

//			addr += key_storage.getREQUEST_URL();

			String parameter = "";
			parameter = parameter + key_storage.getREQUEST_URL();
			parameter = parameter + "ServiceKey=" + key_storage.getAPI_KEY();
			parameter = parameter + "&" + "WGS84_LON=" + longitude;
			parameter = parameter + "&" + "WGS84_LAT=" + latitude;
			parameter = parameter + "&pageNo=1&numOfRows=10";
			Log.d("PARAMETER", parameter);
//	        api_key = "&ServiceKey=" + key_storage.getAPI_KEY();

			// http://openapi.e-gen.or.kr/openapi/service/rest/ErmctInsttInfoInqireService/getParmacyLcinfoInqire?ServiceKey=DCGhzkawVTj7P8KjYSL7ZhhmmDZOTXohLi8hV0RFJjE3DR%2FXDLKkNzPxRIm5nFWmgoZA5LsipqsSoh9lTzOIzg%3D%3D&WGS84_LON=127.026175&WGS84_LAT=37.627261&pageNo=1&numOfRows=10
//	        addr = addr + parameter + api_key;

			HttpGet httpGet = new HttpGet(parameter);//addr->parameter
			String text = null;

			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();

				text = getASCIIContentFromEntity(entity);
			} catch (IOException e) {
				return e.getLocalizedMessage();
			}
			return text;
		}

		protected void onPostExecute(String results) {
		}
	}

	public String getApiResult() throws InterruptedException, ExecutionException{
		return new LongRunningGetIO().execute().get();

	}


}
