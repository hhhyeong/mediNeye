package kr.co.dongduk.medineye.service.nfc;

/**
 * Created by Administrator on 2016-08-09.
 */

public interface ParsedRecord {

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_URI = 2;

    public int getType();

}
