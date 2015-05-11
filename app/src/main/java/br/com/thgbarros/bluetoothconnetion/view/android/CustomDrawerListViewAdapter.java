package br.com.thgbarros.bluetoothconnetion.view.android;

/**
 * Created by thiagobarros on 10/05/15.
 */
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.thgbarros.bluetoothconnetion.R;

public class CustomDrawerListViewAdapter extends ArrayAdapter<RowItem> {

    private Context context;

    public CustomDrawerListViewAdapter(Context context, int resourceId, List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            holder = new ViewHolder();
            holder.textViewDescricao = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.textViewDescricao.setText(rowItem.getDescricao());
        holder.imageView.setImageResource(rowItem.getImageId());

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textViewDescricao;
    }
}