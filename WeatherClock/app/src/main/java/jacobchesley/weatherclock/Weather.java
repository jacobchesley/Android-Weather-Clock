package jacobchesley.weatherclock;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class Weather {

    String weatherSite = "http://api.openweathermap.org/data/2.5/";
    String weatherIconSite = "http://openweathermap.org/img/w/";
    String APIKey = "";

    public static class WeatherData {

        WeatherData(){}
        public double temp;
        public double low;
        public double high;
        public double humidity;
        public double pressure;
        public double windSpeed;
        public double clouds;
        public int weatherId;
        public String weatherMain;
        public String weatherDescription;
        public Bitmap weatherIcon;
        public long time;
        public long sunrise;
        public long sunset;
        public String cityName;
        public long updateTime;

    }

    public enum TempUnits{
        FAHRENHEIT,
        CELSIUS,
        KELVIN
    }

    WeatherData now;
    TempUnits currentUnit;
    String zip;
    String country;
    ArrayList<WeatherData> forecastDaily;
    ArrayList<WeatherData> forecast;

    Weather(String zipCode, String countryCode, TempUnits units){
        zip = zipCode;
        country = countryCode;
        currentUnit = units;
        now = new WeatherData();
        forecastDaily = new ArrayList<WeatherData>();
        forecast = new ArrayList<WeatherData>();
    }

    double GetCurrentTemp(){ return ConvertTemp(now.temp); }

    double GetTodaysLowTemp(){ return ConvertTemp(now.low); }

    double GetTodaysHighTemp(){ return ConvertTemp(now.high); }

    double GetCurrentHumidity() { return now.humidity; }

    double GetCurrentWindspeed() { return ConvertWind(now.windSpeed); }

    double GetCurrentPressure() { return ConvertPressure(now.pressure); }

    double GetCurrentClouds() { return now.clouds; }

    String GetCurrentWeatherMain(){ return now.weatherMain; }

    String GetCurrentWeatherDescription(){ return now.weatherDescription; }

    Bitmap GetCurrentWeatherIcon(){ return now.weatherIcon; }

    long GetCurrentSunrise(){ return now.sunrise; }

    long GetCurrentSunset() { return now.sunset; }

    void SetLocation(String zipCode, String countryCode){
        zip = zipCode;
        country = countryCode;
    }

    String GetLocation() { return now.cityName; }

    long GetUpdateTime() { return now.updateTime; }

    void SetWeatherAPIKey(String apiKey){
       APIKey = apiKey;
    }

    void SetUnits(TempUnits units){
        currentUnit = units;
    }

    double ConvertTemp(double kelvin){

        switch (currentUnit){
            case KELVIN:
                return kelvin;

            case CELSIUS:
                return kelvin - 273.15;

            case FAHRENHEIT:
                return ((kelvin * (9.0/5.0)) - 459.67);

            default:
                return 0.0;
        }
    }

    double ConvertWind(double speed){
        switch (currentUnit) {
            case KELVIN:
                return speed;

            case CELSIUS:
                return speed;

            case FAHRENHEIT:
                return speed * 2.23694;

            default:
                return 0.0;
        }
    }

    double ConvertPressure(double pressure){
        switch (currentUnit) {
            case KELVIN:
                return pressure;

            case CELSIUS:
                return pressure;

            case FAHRENHEIT:
                return pressure * 0.02953;

            default:
                return 0.0;
        }
    }

    private static String read(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int cp;
        while ((cp = reader.read()) != -1) {
            stringBuilder.append((char) cp);
        }
        return stringBuilder.toString();
    }

    void UpdateWeatherData(){

        // Update current condition
        String url = weatherSite + "weather?appid=" + APIKey + "&zip=" + zip + "," + country;
        JSONObject json = new JSONObject();

        try {
            InputStream inputStream = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String jsonText = read(reader);
            json = new JSONObject(jsonText);
        }
        catch(Exception e){}

        try {
            JSONObject mainJson = (JSONObject)json.get("main");
            JSONObject weatherJson = (JSONObject)((JSONArray) json.get("weather")).get(0);
            JSONObject windJson = (JSONObject) json.get("wind");
            JSONObject cloudJson = (JSONObject) json.get("clouds");
            JSONObject sysJson = (JSONObject)json.get("sys");

            now.temp = mainJson.getDouble("temp");
            now.humidity = mainJson.getDouble("humidity");
            now.pressure = mainJson.getDouble("pressure");
            now.weatherId = weatherJson.getInt("id");
            now.weatherMain = weatherJson.getString("main");
            now.weatherDescription = weatherJson.getString("description");
            now.weatherDescription = now.weatherDescription.substring(0,1).toUpperCase() + now.weatherDescription.substring(1);
            now.windSpeed = windJson.getDouble("speed");
            now.clouds = cloudJson.getDouble("all");
            now.sunrise = sysJson.getLong("sunrise");
            now.sunset = sysJson.getLong("sunset");
            now.cityName = json.getString("name");
            now.updateTime = json.getLong("dt");

            String icon = weatherJson.getString("icon");
            // Get weather icon
            try{
                String iconUrl = weatherIconSite + icon + ".png";
                InputStream inputStream = (InputStream) new URL(iconUrl).getContent();
                now.weatherIcon = BitmapFactory.decodeStream(inputStream);
            }
            catch(Exception e){ }
        }
        catch(Exception e){}

        // Update 3 hour forecast
        url = weatherSite + "forecast?appid=" + APIKey + "&zip=" + zip + "," + country;

        try {
            InputStream inputStream = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String jsonText = read(reader);
            json = new JSONObject(jsonText);
        }
        catch(Exception e){
          //  Log.e("JSON INFO",e.getMessage());
        }

        try {
            JSONArray forecastJson = (JSONArray)json.get("list");

            forecast.clear();
            for(int i = 0; i < forecastJson.length(); i++){

                JSONObject nextForecast = (JSONObject)forecastJson.get(i);
                JSONObject mainJson = (JSONObject)nextForecast.get("main");
                JSONObject weatherJson = (JSONObject)((JSONArray)nextForecast.get("weather")).get(0);

                WeatherData newForecast = new WeatherData();
                newForecast.temp = mainJson.getDouble("temp");
                newForecast.humidity = mainJson.getDouble("humidity");
                newForecast.weatherId = weatherJson.getInt("id");
                newForecast.weatherMain = weatherJson.getString("main");
                newForecast.weatherDescription = weatherJson.getString("description");
                newForecast.weatherDescription = newForecast.weatherDescription.substring(0,1).toUpperCase() + newForecast.weatherDescription.substring(1);
                newForecast.time = nextForecast.getLong("dt");
                String icon = weatherJson.getString("icon");

                // Get weather icon
                try{
                    String iconUrl = weatherIconSite + icon + ".png";
                    InputStream inputStream = (InputStream) new URL(iconUrl).getContent();
                    newForecast.weatherIcon = BitmapFactory.decodeStream(inputStream);
                }
                catch(Exception e){ }

                forecast.add(newForecast);
            }
        }
        catch(JSONException e){
          //  Log.e("JSON INFO",e.getMessage());
        }


        // Update 7 day forecast
        url = weatherSite + "forecast/daily?appid=" + APIKey + "&zip=" + zip + "," + country;

        try {
            InputStream inputStream = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String jsonText = read(reader);
            json = new JSONObject(jsonText);
        }
        catch(Exception e){
            //  Log.e("JSON INFO",e.getMessage());
        }

        try {
            JSONArray forecastJson = (JSONArray)json.get("list");
            forecastDaily.clear();

            for(int i = 0; i < forecastJson.length(); i++){

                JSONObject dayJson = forecastJson.getJSONObject(i);
                JSONObject tempJson = dayJson.getJSONObject("temp");
                JSONObject weatherJson = (JSONObject)((JSONArray)dayJson.get("weather")).get(0);

                WeatherData newForecast = new WeatherData();
                newForecast.humidity = dayJson.getDouble("humidity");
                newForecast.weatherId = weatherJson.getInt("id");
                newForecast.weatherMain = weatherJson.getString("main");
                newForecast.weatherDescription = weatherJson.getString("description");
                newForecast.weatherDescription = newForecast.weatherDescription.substring(0,1).toUpperCase() + newForecast.weatherDescription.substring(1);
                newForecast.time = dayJson.getLong("dt");
                String icon = weatherJson.getString("icon");

                // Get weather icon
                try{
                    String iconUrl = weatherIconSite + icon + ".png";
                    InputStream inputStream = (InputStream) new URL(iconUrl).getContent();
                    newForecast.weatherIcon = BitmapFactory.decodeStream(inputStream);
                }
                catch(Exception e){ }


                Calendar nowCalendar = Calendar.getInstance();
                Calendar forecastCalendar = Calendar.getInstance();
                forecastCalendar.setTimeInMillis(newForecast.time * 1000);

                if(nowCalendar.get(Calendar.DATE) == forecastCalendar.get(Calendar.DATE)){
                    now.low = tempJson.getDouble("min");
                    now.high = tempJson.getDouble("max");
                }
            }
        }
        catch(JSONException e){  }
    }
}
