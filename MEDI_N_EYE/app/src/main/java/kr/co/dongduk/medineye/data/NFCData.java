package kr.co.dongduk.medineye.data;

/**
 * Created by Owner on 2016-08-25.
 */
public class NFCData {
    String NFCId;
    String NFCtext;

    public NFCData(String NFCId, String NFCtext) {
        this.NFCId = NFCId;
        this.NFCtext = NFCtext;
    }

    public String getNFCId() {
        return NFCId;
    }

    public void setNFCId(String NFCId) {
        this.NFCId = NFCId;
    }

    public String getNFCtext() {
        return NFCtext;
    }

    public void setNFCtext(String NFCtext) {
        this.NFCtext = NFCtext;
    }
}
