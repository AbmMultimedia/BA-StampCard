package dk.ba.bastampcard.helpers;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
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
    private double latitude;
    private double longitude;

    public ShopListAdapter(Context context, List<Shop> shops, double latitude, double longitude) {
        super(context, 0, shops);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Shop shop = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_shop, parent, false);
        }

        convertView.setTag(shop.getId());
        Log.d("SET TAG: ", Integer.toString(shop.getId()));

        TextView shopName = (TextView) convertView.findViewById(R.id.list_item_shop_name);
        TextView shopDistance = (TextView) convertView.findViewById(R.id.list_item_shop_distance);

        shopName.setText(shop.getName());

        if(latitude == 0 || longitude == 0)
        {
            shopDistance.setText(R.string.GpsNotReady);
        }
        else {
            float distance = shop.getDistance(latitude, longitude);
            if (distance > 1000) {
                distance = distance / 1000;
                shopDistance.setText(Float.toString(distance) + " km");
            } else {
                shopDistance.setText(Float.toString(distance) + " m");
            }
        }

        return convertView;
    }
}
