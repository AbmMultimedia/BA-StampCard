package dk.ba.bastampcard.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import dk.ba.bastampcard.R;

/**
 * Created by Benedicte on 09-11-2014.
 */
public class WeatherActivity extends Activity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        WebView webViewJava = (WebView) findViewById(R.id.webView);
        webViewJava.loadUrl("http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&mode=html");
        webViewJava.setWebViewClient(new WebViewClient());
    }
}
