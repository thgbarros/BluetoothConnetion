package br.com.thgbarros.bluetoothconnetion.view.adapter.rowitem;

/**
 * Created by thiago on 18/05/15.
 */
public class TroubleRowItem {
    private String code;
    private String description;

    public TroubleRowItem(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return code + " [" + description + "]";
    }
}
