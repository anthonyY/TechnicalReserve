package an.com.unionpay;

/**
 * @author Anthony
 *         createTime 2016/9/1.
 * @version 1.0
 */
public enum Mode {

    TEST("01"),
    ONLINE("00");

    Mode(String value){
        this.value = value;
    }
    private String value;
    public String getValue() {
        return this.value;
    }
}
