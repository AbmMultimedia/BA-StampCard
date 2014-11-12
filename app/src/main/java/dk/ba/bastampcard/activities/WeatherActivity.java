package dk.ba.bastampcard.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        longitude = location.getLongitude();
        latitude = location.getLatitude();

        WebView webViewJava = (WebView) findViewById(R.id.webView);
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

    public class WeatherLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.action_stamps:
                intent = new Intent(getApplicationContext(), StampActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_statistics:
                intent = new Intent(getApplicationContext(), StatisticsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_weather:
                intent = new Intent(getApplicationContext(), WeatherActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_purchase:
                intent = new Intent(getApplicationContext(), PurchaseActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_share:
                intent = new Intent(getApplicationContext(), ShareActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
