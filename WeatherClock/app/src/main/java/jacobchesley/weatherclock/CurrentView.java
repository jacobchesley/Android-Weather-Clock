package jacobchesley.weatherclock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class CurrentView extends Fragment {

    View currentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View returnView = inflater.inflate(R.layout.activity_current_weather_main, container, false);
        currentView = returnView;
        return returnView;
    }

    public void UpdateTemperature(String temp) {
        final TextView temperature = (TextView)currentView.findViewById(R.id.current_temperature);
        temperature.setText(temp);
    }

    public void UpdateHumidity(String humid) {
        final TextView humidity = (TextView)currentView.findViewById(R.id.current_humidity);
        humidity.setText(humid);
    }

    public void UpdatePressure(String press) {
        final TextView pressure = (TextView)currentView.findViewById(R.id.current_pressure);
        pressure.setText(press);
    }

    public void UpdateWindspeed(String speed) {
        final TextView windspeed = (TextView)currentView.findViewById(R.id.current_windspeed);
        windspeed.setText(speed);
    }

    public void UpdateCloudCover(String cloudCover) {
        final TextView cloud = (TextView)currentView.findViewById(R.id.current_cloud);
        cloud.setText(cloudCover);
    }

    public void UpdateLocation(String currentLocation) {
        final TextView location = (TextView)currentView.findViewById(R.id.current_location);
        location.setText(currentLocation);
    }
}