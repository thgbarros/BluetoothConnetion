package br.com.thgbarros.bluetoothconnetion.view.android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.thgbarros.bluetoothconnetion.R;

/**
 * Created by thiago on 13/05/15.
 */
public class CustomReadsListViewAdapter extends ArrayAdapter<ReadsRowItem> {

    private Context context;

    public CustomReadsListViewAdapter(Context context, int resourceId, List<ReadsRowItem> items){
        super(context, resourceId, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        ReadsRowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.read_list_item, null);
            holder = new ViewHolder();
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.textViewReadTitle);
            holder.textViewValue = (TextView) convertView.findViewById(R.id.textViewReadValue);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.textViewTitle.setText(rowItem.getTitle());
        holder.textViewValue.setText(rowItem.getValue() + " " + rowItem.getUnitOfMeasure());

        return convertView;
    }

    private class ViewHolder {
        TextView textViewTitle;
        TextView textViewValue;
    }

}
