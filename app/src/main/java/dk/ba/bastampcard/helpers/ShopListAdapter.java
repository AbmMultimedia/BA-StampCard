package dk.ba.bastampcard.helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import dk.ba.bastampcard.R;
import dk.ba.bastampcard.model.Shop;

/**
 * Created by Anders on 04-11-2014.
 */
public class ShopListAdapter extends ArrayAdapter<Shop> {

    public ShopListAdapter(Context context, List<Shop> shops) {
        super(context, 0, shops);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Shop shop = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_shop, parent, false);
        }

        TextView shopName = (TextView) convertView.findViewById(R.id.list_item_shop_name);
        TextView shopDistance = (TextView) convertView.findViewById(R.id.list_item_shop_distance);

        shopName.setText(shop.getName());
        try {
            shopDistance.setText(Float.toString(shop.getDistance(getContext(), 1, 1)));
        } catch (IOException e) {
            Log.d(this.getClass().getName(), e.getMessage());
            e.printStackTrace();
        }

        return convertView;
    }
}
