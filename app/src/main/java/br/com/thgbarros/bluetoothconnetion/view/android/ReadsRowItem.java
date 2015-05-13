package br.com.thgbarros.bluetoothconnetion.view.android;

/**
 * Created by thiago on 13/05/15.
 */
public class ReadsRowItem {
    private String title;
    private String value;
    private String unitOfMeasure;

    public ReadsRowItem(String title, String value, String unitOfMeasure){
        this.title = title;
        this.value = value;
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    @Override
    public String toString() {
        return title + "[" + value + " " + unitOfMeasure + "]";
    }
}
