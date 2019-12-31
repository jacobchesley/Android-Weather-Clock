package jacobchesley.weatherclock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Calendar;

public class SettingsView extends Fragment {

    View settingsView;
    boolean use24Hour;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        use24Hour = false;
        super.onCreate(savedInstanceState);
        View returnView = inflater.inflate(R.layout.activity_settings_main, container, false);
        SetButtonHandlers(returnView);
        settingsView = returnView;
        GetPreferences(returnView);
        return returnView;
    }

    public ClockView GetClockView(){
        int clockViewPos = -1;
        for(int i = 0; i <  getFragmentManager().getFragments().size(); i++){
            if( getFragmentManager().getFragments().get(i) instanceof ClockView){
                clockViewPos = i;
                break;
            }
        }

        if(clockViewPos < 0){ return null; }

        final ClockView clockView = (ClockView) getFragmentManager().getFragments().get(clockViewPos);
        return clockView;
    }

    public void SetButtonHandlers(final View view){

        final Button clockButton = (Button) view.findViewById(R.id.settings_clock_button);
        final Button tempButton = (Button) view.findViewById(R.id.settings_temp_button);
        final EditText zipInput = (EditText) view.findViewById(R.id.settings_zip_input);
        final EditText intervalInput = (EditText) view.findViewById(R.id.settings_update_interval_input);
        final EditText keyInput = (EditText) view.findViewById(R.id.weather_key_input);

        final ClockView clockView = GetClockView();

        zipInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        intervalInput.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // Toggle 12/24 hour clock setting
        clockButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(clockButton.getText().toString().contains("12")){
                    clockButton.setText("24 Hour");
                    clockView.Set24Hour();
                    use24Hour = true;
                }
                else{
                    clockButton.setText("12 Hour");
                    clockView.Set12Hour();
                    use24Hour = false;
                }
                SetPreferences(view);
            }
        });

        // Toggle fahrenheit, celsius, kelvin temp units
        tempButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tempButton.getText().toString().contains("F")) {
                    tempButton.setText("C");
                    clockView.SetTempUnitsC();
                } else if (tempButton.getText().toString().contains("C")) {
                    tempButton.setText("K");
                    clockView.SetTempUnitsK();
                } else {
                    tempButton.setText("F");
                    clockView.SetTempUnitsF();
                }
                SetPreferences(view);
            }
        });

        // Change location zip code
        zipInput.setOnEditorActionListener(
            new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event != null &&
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (event == null || !event.isShiftPressed()) {
                            // Set zip code when done is pressed
                            clockView.SetWeatherLocation(zipInput.getText().toString());
                            SetPreferences(view);
                            return false;  // Hide keyboard after
                        }
                    }
                    return false; // pass on to other listeners.
                }
            }
        );

        // Change update interval
        intervalInput.setOnEditorActionListener(
            new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event != null &&
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (event == null || !event.isShiftPressed()) {
                            // Set zip code when done is pressed
                            clockView.SetWeatherUpdateInterval(intervalInput.getText().toString());
                            SetPreferences(view);
                            return false;  // Hide keyboard after
                        }
                    }
                    return false; // pass on to other listeners.
                }
            }
        );

        // Set open weather maps API Key
        keyInput.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                // Set zip code when done is pressed
                                clockView.SetWeatherAPIKey(keyInput.getText().toString());
                                SetPreferences(view);
                                return false;  // Hide keyboard after
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );
    }

    public void GetPreferences(View view){

        final Button clockButton = (Button) view.findViewById(R.id.settings_clock_button);
        final Button tempButton = (Button) view.findViewById(R.id.settings_temp_button);
        final EditText zipInput = (EditText) view.findViewById(R.id.settings_zip_input);
        final EditText intervalInput = (EditText) view.findViewById(R.id.settings_update_interval_input);
        final EditText keyInput = (EditText) view.findViewById(R.id.weather_key_input);
        final ClockView clockView = GetClockView();

        SharedPreferences preferences = getContext().getSharedPreferences("WEATHER_CLOCK_PREFS", 0);

        // Set stored API key
        String apiKeyPref = preferences.getString("WEATHER_API_KEY", "");
        keyInput.setText(apiKeyPref);
        clockView.SetWeatherAPIKey(apiKeyPref);

        // Set stored update interval
        String updateIntervalPref = preferences.getString("UPDATE_INTERVAL", "5");
        intervalInput.setText(updateIntervalPref);
        clockView.SetWeatherUpdateInterval(updateIntervalPref);

        // Set stored zip code
        String zipPref = preferences.getString("ZIP_CODE", "90210");
        zipInput.setText(zipPref);
        clockView.SetWeatherLocation(zipPref);

        // Set stored temperature units
        String tempUnitsPref = preferences.getString("TEMPERATURE_UNITS", "F");
        tempButton.setText(tempUnitsPref);
        if (tempButton.getText().toString().contains("F")) {
            tempButton.setText("C");
            clockView.SetTempUnitsC();
        } else if (tempButton.getText().toString().contains("C")) {
            tempButton.setText("K");
            clockView.SetTempUnitsK();
        } else {
            tempButton.setText("F");
            clockView.SetTempUnitsF();
        }

        // Set stored clock format
        String clockPref = preferences.getString("CLOCK_FORMAT", "12 Hour");
        clockButton.setText(clockPref);
        if(clockButton.getText().toString().contains("12")){
            clockView.Set12Hour();
            use24Hour = false;
        }
        else{
            clockView.Set24Hour();
            use24Hour = true;
        }
    }

    public void SetPreferences(View view){

        final Button clockButton = (Button) view.findViewById(R.id.settings_clock_button);
        final Button tempButton = (Button) view.findViewById(R.id.settings_temp_button);
        final EditText zipInput = (EditText) view.findViewById(R.id.settings_zip_input);
        final EditText intervalInput = (EditText) view.findViewById(R.id.settings_update_interval_input);
        final EditText keyInput = (EditText) view.findViewById(R.id.weather_key_input);

        SharedPreferences preferences = getContext().getSharedPreferences("WEATHER_CLOCK_PREFS", 0);
        SharedPreferences.Editor preferencesEditor = preferences.edit();

        preferencesEditor.putString("WEATHER_API_KEY", keyInput.getText().toString());
        preferencesEditor.putString("UPDATE_INTERVAL", intervalInput.getText().toString());
        preferencesEditor.putString("ZIP_CODE", zipInput.getText().toString());
        preferencesEditor.putString("TEMPERATURE_UNITS", tempButton.getText().toString());
        preferencesEditor.putString("TIME_FORMAT", clockButton.getText().toString());

        preferencesEditor.commit();
    }

    public void UpdateUpdateTime(long updateTime) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(updateTime * 1000);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String hourString = "";
        String minuteString = "";
        String amPm = "";

        if (use24Hour) {
            hourString = Integer.toString(hour);
            if (hourString.length() == 1) {
                hourString = "0" + hourString;
            }
            amPm = "";
        } else {
            if (hour == 0) {
                hourString = "12";
            } else if (hour > 12) {
                hourString = Integer.toString(hour - 12);

            } else {
                hourString = Integer.toString(hour);
            }
        }

        if(!use24Hour){
            if(hour < 12){
                amPm = "AM";
            }
            else{
                amPm = "PM";
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

        // Set hours, minutes, and date
        String dayString = Integer.toString(day);
        String monthName = (String) android.text.format.DateFormat.format("MMM", calendar);

        String updateTimeString = hourString + ":" + minuteString + " " + amPm + " - " + monthName + " " + dayString;

        final TextView lastUpdate = (TextView)settingsView.findViewById(R.id.settings_last_update);
        lastUpdate.setText(updateTimeString);
    }
}