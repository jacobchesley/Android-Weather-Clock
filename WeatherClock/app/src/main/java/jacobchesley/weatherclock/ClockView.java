package jacobchesley.weatherclock;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class ClockView extends Fragment{

    BackgroundGenerator background;
    boolean use24Hour = false;
    Weather.TempUnits weatherUnits = Weather.TempUnits.FAHRENHEIT;
    boolean forceWeatherUpdate = false;
    final Weather weather = new Weather("45244", "us", Weather.TempUnits.FAHRENHEIT);
    int updateInterval = 300;
    String unitsValue = "F";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        background = new BackgroundGenerator();
        StartClock();
        StartWeather();

        return inflater.inflate(R.layout.activity_clock_main, container, false);
    }

    void SetClockHourMinText(String text){

        // Get main clock display
        TextView clockMain = (TextView)getView().findViewById(R.id.clock_hour_min);

        // Set text view string if its not null
        if(clockMain != null){
            clockMain.setText(text);
        }
    }

    void SetSecondsText(String text){

        // Get main clock display
        TextView clockSeconds = (TextView)getView().findViewById(R.id.clock_seconds);

        // Set text view string if its not null
        if(clockSeconds != null){
            clockSeconds.setText(text);
        }
    }

    void SetAMPMText(String text){

        // Get main clock display
        TextView clockAMPM = (TextView)getView().findViewById(R.id.clock_am_pm);

        // Set text view string if its not null
        if(clockAMPM != null){
            clockAMPM.setText(text);
        }
    }

    void SetClockDateText(String text){

        // Get main clock display
        TextView clockDate = (TextView)getView().findViewById(R.id.clock_date);

        // Set text view string if its not null
        if(clockDate != null){
            clockDate.setText(text);
        }
    }

    void SetClockCurrentTempText(String text){

        // Get main clock display
        TextView currentTemp = (TextView)getView().findViewById(R.id.clock_current_temp);

        // Set text view string if its not null
        if(currentTemp != null){
            currentTemp.setText(text + '\u00B0');
        }
    }

    void SetClockLowTempText(String text){

        // Get main clock display
        TextView lowTemp = (TextView)getView().findViewById(R.id.clock_low_temp);

        // Set text view string if its not null
        if(lowTemp != null){
            lowTemp.setText(text + '\u00B0');
        }
    }

    void SetClockHighTempText(String text){

        // Get main clock display
        TextView highTemp = (TextView)getView().findViewById(R.id.clock_high_temp);

        // Set text view string if its not null
        if(highTemp != null){
            highTemp.setText(text + '\u00B0');
        }
    }

    void SetClockUnitsText(String text){

        // Get main clock display
        TextView units = (TextView)getView().findViewById(R.id.clock_units);
        unitsValue = text;
        // Set text view string if its not null
        if(units != null){
            units.setText(text);
        }
    }

    void SetClockWeather(String text){

        // Get main clock display
        TextView weather = (TextView)getView().findViewById(R.id.clock_weather);

        // Set text view string if its not null
        if(weather != null){
            weather.setText(text);
        }
    }

    void SetClockWeatherDescription(String text){

        // Get main clock display
        TextView weatherDesc = (TextView)getView().findViewById(R.id.clock_weather_desc);

        // Set text view string if its not null
        if(weatherDesc != null){
            weatherDesc.setText(text);
        }
    }

    void SetClockWeatherIcon(Bitmap icon){

        if(icon == null){ return; }

        ImageView iconView = (ImageView) getView().findViewById(R.id.clock_weather_icon);
        TextView weather = (TextView)getView().findViewById(R.id.clock_weather);

        int weatherHeight = weather.getHeight();
        icon = Bitmap.createScaledBitmap(icon, weatherHeight, weatherHeight, false);

        if(icon != null && iconView != null){

            iconView.getLayoutParams().width = icon.getWidth();
            iconView.getLayoutParams().height = icon.getHeight();
            iconView.setImageBitmap(icon);
            iconView.requestLayout();
        }
    }

    void SetClockBackground(int backgroundColor){
        // Get main clock display
        TextView weather = (TextView)getView().findViewById(R.id.clock_weather);

        if(weather != null){
            View root = weather.getRootView();
            root.setBackgroundColor(backgroundColor);
        }
    }

    void Set12Hour(){
        use24Hour = false;
    }

    void Set24Hour(){
        use24Hour = true;
    }

    void SetTempUnitsF(){
        SetClockUnitsText("F");
        weatherUnits = Weather.TempUnits.FAHRENHEIT;
        UpdateWeatherDisplay();
    }

    void SetTempUnitsC(){
        SetClockUnitsText("C");
        weatherUnits = Weather.TempUnits.CELSIUS;
        UpdateWeatherDisplay();
    }

    void SetTempUnitsK(){
        SetClockUnitsText("K");
        weatherUnits = Weather.TempUnits.KELVIN;
        UpdateWeatherDisplay();
    }

    void SetWeatherLocation(String location){
        weather.SetLocation(location, "us");
        forceWeatherUpdate = true;
    }

    void SetWeatherUpdateInterval(String updateIntervalStr){

        // Update interval entered in minutes, convert to seconds
        int updateIntervalInput = Integer.parseInt(updateIntervalStr);
        if(updateIntervalInput < 1){updateIntervalInput = 1;}
        updateIntervalInput  *= 60.0;
        updateInterval = updateIntervalInput;
    }

    void SetWeatherAPIKey(String apiKey){
        weather.SetWeatherAPIKey(apiKey);
    }
    void StartClock(){

        final boolean useWeekDate = true;

        final Handler handler = new Handler();
        Runnable clockRun = new Runnable() {
            public void run() {
                while (true) {

                    handler.post(new Runnable() {
                        public void run() {

                            Calendar calendar = Calendar.getInstance();

                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int minute = calendar.get(Calendar.MINUTE);
                            int second = calendar.get(Calendar.SECOND);
                            int month = calendar.get(Calendar.MONTH);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            int year = calendar.get(Calendar.YEAR);

                            String hourString = "";
                            String minuteString = "";
                            String secondString = "";
                            String monthString = Integer.toString(month + 1);
                            String dayString = Integer.toString(day);
                            String yearString = Integer.toString(year);
                            String monthName = (String) android.text.format.DateFormat.format("MMM", calendar);
                            String dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);

                            if (use24Hour) {
                                hourString = Integer.toString(hour);
                                if (hourString.length() == 1) {
                                    hourString = "0" + hourString;
                                }
                            } else {
                                if (hour == 0) {
                                    hourString = "12";
                                } else if (hour > 12) {
                                    hourString = Integer.toString(hour - 12);
                                } else {
                                    hourString = Integer.toString(hour);
                                }
                            }

                            // Append space to hour to mitigate shift when hour goes to 2 digit
                            if (hourString.length() < 2) {
                                hourString = " " + hourString;
                            }

                            // Append 0 to minute string if minutes < 10
                            if (minute < 10) {
                                minuteString = "0" + Integer.toString(minute);
                            } else {
                                minuteString = Integer.toString(minute);
                            }

                            // Append 0 to seconds string if minutes < 10
                            if (second < 10) {
                                secondString = "0" + Integer.toString(second);
                            } else {
                                secondString = Integer.toString(second);
                            }

                            // Set hours and minutes
                            SetClockHourMinText(hourString + ":" + minuteString);
                            SetSecondsText(secondString);

                            // Set AM or PM
                            if (hour < 12) {
                                SetAMPMText("AM");
                            } else {
                                SetAMPMText("PM");
                            }

                            if (useWeekDate) {
                                SetClockDateText(dayName + ", " + monthName + " " + dayString);
                            } else {
                                SetClockDateText(monthString + "/" + dayString + "/" + yearString);
                            }

                            SetClockBackground(background.GetBackgroundColor());
                        }
                    });

                    try {
                        Thread.sleep(50);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(clockRun).start();
    }

    void StartWeather(){

        final Handler handler = new Handler();

        Runnable weatherRun = new Runnable() {
            public void run() {

                while (true) {

                    weather.UpdateWeatherData();

                    handler.post(new Runnable(){
                        public void run() {
                            UpdateWeatherDisplay();
                        }
                    });


                    try {
                        for(int i = 0; i < updateInterval * 10; i++) {
                            Thread.sleep(100);

                            // Break and force weather update if needed (change of units or location)
                            if(forceWeatherUpdate){
                                forceWeatherUpdate = false;
                                weather.UpdateWeatherData();

                            }
                            handler.post(new Runnable() {
                                public void run() {
                                    UpdateWeatherDisplay();
                                }
                            });
                        }
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(weatherRun).start();
    }

    void UpdateWeatherDisplay(){

        weather.SetUnits(weatherUnits);
        double currentTempD = weather.GetCurrentTemp();
        int currentTemp = (int)Math.round(currentTempD);
        int lowTemp = (int) Math.round(weather.GetTodaysLowTemp());
        int highTemp = (int)Math.round(weather.GetTodaysHighTemp());
        int humidity = (int)weather.GetCurrentHumidity();
        double pressure = weather.GetCurrentPressure();
        int clouds = (int)weather.GetCurrentClouds();
        double windspeed = weather.GetCurrentWindspeed();

        String currentDString = String.format("%.2f", currentTempD);
        String currentString = Integer.toString(currentTemp);
        String lowString = Integer.toString(lowTemp);
        String highString = Integer.toString(highTemp);
        String weatherString = weather.GetCurrentWeatherMain();
        String weatherDescString = weather.GetCurrentWeatherDescription();
        Bitmap weatherIcon = weather.GetCurrentWeatherIcon();

        String humidityString = Integer.toString(humidity);
        String pressureString = String.format("%.2f", pressure);
        String cloudString = Integer.toString(clouds);
        String windspeedString = String.format("%.2f", windspeed);
        String locationString = weather.GetLocation();
        long updateTime = weather.GetUpdateTime();

        SetClockCurrentTempText(currentString);
        SetClockLowTempText(lowString);
        SetClockHighTempText(highString);
        SetClockWeather(weatherString);
        SetClockWeatherDescription(weatherDescString);
        SetClockWeatherIcon(weatherIcon);

        String windUnits = "m/sec";
        String pressureUnits = "hPa";

        if(weatherUnits == Weather.TempUnits.FAHRENHEIT){
            windUnits = "MPH";
            pressureUnits = "inHg";
        }

        int currentViewPos = -1;
        int settingsViewPos = -1;
        for(int i = 0; i <  getFragmentManager().getFragments().size(); i++){
            if( getFragmentManager().getFragments().get(i) instanceof CurrentView){
                currentViewPos = i;
            }
            if( getFragmentManager().getFragments().get(i) instanceof SettingsView){
                settingsViewPos = i;
            }
        }

        if(currentViewPos < 0 || settingsViewPos < 0){ return; }

        final CurrentView currentView = (CurrentView) getFragmentManager().getFragments().get(currentViewPos);
        currentView.UpdateTemperature(currentDString + '\u00B0' + unitsValue);
        currentView.UpdateHumidity(humidityString + " %");
        currentView.UpdatePressure(pressureString + " " + pressureUnits);
        currentView.UpdateWindspeed(windspeedString + " " + windUnits);
        currentView.UpdateCloudCover(cloudString + " %");
        currentView.UpdateLocation(locationString);

        final SettingsView settingsView = (SettingsView) getFragmentManager().getFragments().get(settingsViewPos);
        settingsView.UpdateUpdateTime(updateTime);

        background.SetSunTimes(weather.GetCurrentSunrise() * 1000, weather.GetCurrentSunset() * 1000);

        if(weatherString == null){
            return;
        }

        if(weatherString.toLowerCase().contains("cloud") && clouds < 76){
            background.SetWeatherCondition(BackgroundGenerator.Condition.Clear);
        }
        else if(weatherString.toLowerCase().contains("cloud") && clouds >= 76) {
            background.SetWeatherCondition(BackgroundGenerator.Condition.Cloudy);
        }
        else if(weatherString.toLowerCase().contains("rain") || weatherString.toLowerCase().contains("drizzle") || weatherString.toLowerCase().contains("snow")) {
            background.SetWeatherCondition(BackgroundGenerator.Condition.Rain);
        }
        else if(weatherString.toLowerCase().contains("storm")){
            background.SetWeatherCondition(BackgroundGenerator.Condition.Storm);
        }
        else if(weatherString.toLowerCase().contains("clear")){
            background.SetWeatherCondition(BackgroundGenerator.Condition.Clear);
        }
        else if(weatherString.toLowerCase().contains("mist")){
            background.SetWeatherCondition(BackgroundGenerator.Condition.Cloudy);
        }
        else if(weatherString.toLowerCase().contains("haze")){
            background.SetWeatherCondition(BackgroundGenerator.Condition.Cloudy);
        }
        else if(weatherString.toLowerCase().contains("fog")){
            background.SetWeatherCondition(BackgroundGenerator.Condition.Cloudy);
        }
        else{
            background.SetWeatherCondition(BackgroundGenerator.Condition.Severe);
        }
    }
}
