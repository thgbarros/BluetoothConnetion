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
 * Created by thiago on 18/05/15.
 */
public class TroubleListViewAdapter extends ArrayAdapter<TroubleRowItem> {


    private Context context;

    public TroubleListViewAdapter(Context context, List<TroubleRowItem> items){
        super(context, R.layout.trouble_list_item, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        TroubleRowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.trouble_list_item, null);
            holder = new ViewHolder();
            holder.textViewCode = (TextView) convertView.findViewById(R.id.textViewTroubleCode);
            holder.textViewDescription = (TextView) convertView.findViewById(R.id.textViewTroubleDescription);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.textViewCode.setText(rowItem.getCode());
        holder.textViewDescription.setText(rowItem.getDescription());

        return convertView;
    }

    private class ViewHolder {
        TextView textViewCode;
        TextView textViewDescription;
    }



}
