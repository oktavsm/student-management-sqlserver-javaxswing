package App;

public class ComboItem {
    private String key;   // misal: nim
    private String value; // misal: nama
    private String value1;
    private String value2;

    public ComboItem(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public ComboItem(String key, String value, String value1) {
        this.key = key;
        this.value = value;
        this.value1 = value1;
    }
    // public ComboItem(String key, String value, String value1) {
    //     this.key = key;
    //     this.value = value;
    //     this.value1 = value1;
    // }

    public String getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }
    public String getValue1() {
        return value1;
    }

    public String toString() {
        return key; 
}

}