package kr.co.dongduk.medineye.service;

import android.os.AsyncTask;

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

/**
 * Created by Owner on 2016-09-19.
 */
public class BarcodeConnectServer {

    private int type = -1;
    private String data = "";

    public BarcodeConnectServer (int type, String barcode){ this.type = type; this.data = barcode;}
    public int getType(){return this.type;}
    public String getData() { return data; }

    private class LongRunningGetIO extends AsyncTask<Void, Void, String> {
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

            if (getType() == 0) {
                addr = "http://drug.mfds.go.kr/admin/openapi/detailSearch.do?key=";
            }else if (getType() == 1){
                addr = "http://drug.mfds.go.kr/admin/openapi/detailSearch.do?bc=";
            }


            addr += getData();

            HttpGet httpGet = new HttpGet(addr);
            String text = null;

            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();

                text = getASCIIContentFromEntity(entity);
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
            return text;
        }


        protected void onPostExecute(String results) {
        }
    }

    //외부에서 이 메소드를 호출하면 서버 통신을 통해 나온 결과 값(xml형태)을 String으로 반환받는다.
    public String call() throws InterruptedException, ExecutionException
    {
        return new LongRunningGetIO().execute().get();
    }
}
