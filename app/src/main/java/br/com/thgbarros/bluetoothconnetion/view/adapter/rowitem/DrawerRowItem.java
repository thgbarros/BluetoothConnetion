package br.com.thgbarros.bluetoothconnetion.view.adapter.rowitem;

/**
 * Created by thiagobarros on 10/05/15.
 */
public class DrawerRowItem {
    private int imageId;
    private String descricao;

    public DrawerRowItem(int imageId, String descricao) {
        this.imageId = imageId;
        this.descricao = descricao;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
