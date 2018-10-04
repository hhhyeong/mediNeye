package kr.co.dongduk.medineye.data;

/**
 * Created by Owner on 2016-09-19.
 */
public class BarcodeData {

    private String name; //제품명
    private String description; // 성상. 고체, 액체 등
    private String company; //업체명
    private String categorizeList; //카테고리 소염제


    public BarcodeData(String name, String description, String company, String categorizeList) {
        super();
        this.name = name;
        this.description = description;
        this.company = company;
        this.categorizeList = categorizeList;
    }
    public BarcodeData() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCategorizeList() {
        return categorizeList;
    }

    public void setCategorizeList(String categorizeList) {
        this.categorizeList = categorizeList;
    }
}
