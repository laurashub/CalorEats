package com.example.rose.caloreats;

//this entire file taken from fc5
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes;

import java.util.ArrayList;

/**
 * Created by thunt on 10/25/16.
 * Holds Maps...What else would it do?
 */

//modified from ^

public class MapHolder implements OnMapReadyCallback {
    /* Some from RedFetch some from this example:
    http://theoryapp.com/parse-json-in-java/
     */
    private float defaultZoom = 15.0f;
    private static class NameToLatLngTask extends AsyncTask<String, Object, LatLng> {
        public interface OnLatLngCallback {
            public void onLatLng(LatLng a);
        }

        OnLatLngCallback cb;
        Context context;

        URL geocoderURLBuilder(String address) {
            URL result = null;
            try {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("maps.googleapis.com")
                        .appendPath("maps")
                        .appendPath("api")
                        .appendPath("geocode")
                        .appendPath("json")
                        // NB: A key is not necessary, but if it exists, it needs the proper
                        // restrictions.  Oddly, in a URL like this
                        // https://console.developers.google.com/apis/credentials/key/3?project=famous-sunbeam-200419
                        // You have to add permissions in the API restrictions tab before it works.
                        .appendQueryParameter("key", context.getResources().getString(R.string.google_maps_key))
                        .appendQueryParameter("address", URLEncoder.encode(address, "UTF-8"));
                result = new URL(builder.build().toString());
            } catch (UnsupportedEncodingException e) {
                Log.e("Geocoder", "Encoding address: " + e.toString());
            } catch (MalformedURLException e) {
                Log.e("Geocoder", "Building URL: " + e.toString());
            }
            return result;
        }

        public NameToLatLngTask(Context ctx, String addr, OnLatLngCallback _cb) {
            context = ctx;
            execute(addr);
            cb = _cb;
        }

        protected LatLng latLngFromJsonString(String json) throws JSONException {
            JSONObject obj = new JSONObject(json);
            LatLng result = null;
            if (!obj.getString("status").equals("OK")) {
                Log.e("URLfetch", "returned status" + obj.getString("status"));
            } else {
                JSONObject loc = obj.getJSONArray("results").getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");
                double lat = loc.getDouble("lat");
                double lng = loc.getDouble("lng");
                result = new LatLng(lat, lng);
                Log.d("Geocoder", "got lat: " + lat + ", lng: " + lng);
            }
            return result;
        }

        @Override
        protected LatLng doInBackground(String... params) {
            assert(params.length > 1);
            String name = params[0];
            URL url;
            LatLng pos = null;

            try {
                Geocoder geo = new Geocoder(context);

                List<Address> addressList = geo.getFromLocationName(name, 1);

                if (addressList != null && addressList.size() > 0){
                    Address a = addressList.get(0);
                    pos = new LatLng(a.getLatitude(), a.getLongitude());
                }
                return pos;
            } catch (IOException e){

            }

            url = geocoderURLBuilder(name);
            if (url == null) {
                cancel(true);
                return null;
            }

            try {
                String result = null;
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.connect();

                if( urlConn.getContentType().startsWith("application/json") )
                    result = fetchJson(urlConn);
                else
                    Log.e("URLfetch", "Result has bad type (not json)");

                if (result != null)
                    pos = latLngFromJsonString(result);
            } catch (IOException e) {
                Log.e("URLfetch", e.toString());
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("JsonBuild", "JSON malformed");
            }

            if (pos == null) {
                cancel(false);
            }
            return pos;
        }

        protected String readStreamToString(InputStream in) throws IOException{
            int numRead;
            final int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            ByteArrayOutputStream outString = new ByteArrayOutputStream();

            while ((numRead = in.read(buffer)) != -1) {
                outString.write(buffer, 0, numRead);
                if (isCancelled()) {
                    return null;
                }
            }
            return new String(outString.toByteArray(), "UTF-8");
        }

        protected String fetchJson(HttpURLConnection conn) {
            InputStream in = null;
            String result = null;
            try {
                in = new BufferedInputStream(conn.getInputStream());
                result = readStreamToString(in);
                Log.d("fetchJson", "json " + result);
            } catch( IOException e ) {
                e.printStackTrace();
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(LatLng result) {
            cb.onLatLng(result);
        }

        @Override
        protected void onCancelled(LatLng result) {
            Log.e("NameToLatLng", "cancelled");
            // Callback does not expect a null value
            //cb.onLatLng(null);
        }
    }

    private GoogleMap gMap;
    private Context context;

    public MapHolder(Context ctx) {
        context = ctx;
    }

    public boolean warnIfNotReady() {
        if (gMap == null) {
            Toast.makeText(context, "No map yet.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) { gMap = googleMap; }

    public void showAddress(final ArrayList<String> addresses) {
        if (warnIfNotReady()) {
            System.out.println("NOT READY");
            return;
        }
        System.out.println("Getting LatLangs");

        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        //add all addresses to map
        for(final String address : addresses) {
            new NameToLatLngTask(context, address,
                    new NameToLatLngTask.OnLatLngCallback() {
                        @Override
                        public void onLatLng(LatLng a) {
                            System.out.println("Address: " + address + ", LatLang: " + a);

                            gMap.addMarker(new MarkerOptions().position(a));
                            if (addresses.size() == 1){
                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(a, defaultZoom));
                            } else {
                                builder.include(a);
                                LatLngBounds bounds = builder.build();
                                gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                            }

                        }
                    });
        }

    }

    public void getLatLngs(final LatLng myPos, ArrayList<String> addresses, final HashMap<String, LatLng> mappings, final SuggestionsFragment sugFrag){
        for(final String address : addresses) {

            new NameToLatLngTask(context, address,
                    new NameToLatLngTask.OnLatLngCallback() {
                        @Override
                        public void onLatLng(LatLng a) {
                            mappings.put(address, a);
                            sugFrag.checkReady(myPos, mappings);
                        }
                    });
        }
    }


}