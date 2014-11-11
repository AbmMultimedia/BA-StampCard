package dk.ba.bastampcard.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import dk.ba.bastampcard.R;

/**
 * Created by Benedicte on 09-11-2014.
 */
public class WeatherActivity extends Activity {

    private LocationListener locationListener;
    private LocationManager locationManager;
    private double longitude;
    private double latitude;

    WebView webViewJava;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        longitude = location.getLongitude();
        latitude = location.getLatitude();

        webViewJava = (WebView) findViewById(R.id.webView);
        showWeather();
    }

    private void showWeather()
    {
        webViewJava.loadUrl("http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&mode=html");
        webViewJava.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onResume(){
        super.onResume();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new WeatherLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,locationListener);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        locationManager.removeUpdates(locationListener);
        locationListener = null;
    }

    public class WeatherLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            showWeather();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            if(i == LocationProvider.AVAILABLE){}
            else if(i == LocationProvider.OUT_OF_SERVICE) {
                Toast.makeText(getApplicationContext(), "GPS is not working!", Toast.LENGTH_SHORT).show();
            }
            else if(i == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                Toast.makeText(getApplicationContext(), "GPS temporarily unavailable", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}
