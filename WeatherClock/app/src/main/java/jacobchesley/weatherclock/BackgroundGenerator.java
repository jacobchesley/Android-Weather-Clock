package jacobchesley.weatherclock;

import android.graphics.Color;

import java.util.Calendar;

public class BackgroundGenerator {

    long sunUp;
    long sunDown;
    long now;

    enum Condition{
        Clear,
        Cloudy,
        Rain,
        Storm,
        Severe
    }

    public static class DefaultColors{
        public static int Severe = Color.argb(255, 175, 0, 0);

        public static int Night = Color.argb(255, 10, 05, 30);
        public static int DarkNight = Color.argb(255, 0, 0, 0);

        public static int ClearDay = Color.argb(255, 15, 35, 180);
        public static int CloudDay = Color.argb(255, 130, 130, 130);
        public static int RainDay = Color.argb(255, 90, 90, 90);
        public static int StormDay = Color.argb(255, 50, 50, 50);
    }

    Condition weather;

    BackgroundGenerator(){}

    void SetSunTimes(Long sunrise, Long sunset){
        sunUp = sunrise;
        sunDown = sunset;
    }
    void SetWeatherCondition(Condition newCondition){
        weather = newCondition;
    }

    int GetBackgroundColor(){

        Calendar nowCal = Calendar.getInstance();

        Calendar sunRiseCal = Calendar.getInstance();
        sunRiseCal.setTimeInMillis(sunUp + (1000 * 60 * 30));  // 30 minutes after sunrise

        Calendar sunSetCal = Calendar.getInstance();
        sunSetCal.setTimeInMillis(sunDown - (1000 * 60 * 30)); // 30 minute before sundown

        Calendar goldHour = Calendar.getInstance();
        goldHour.setTimeInMillis((sunUp - (1000 * 60 * 30))); // 30 minutes before sunrise

        Calendar blueHour = Calendar.getInstance();
        blueHour.setTimeInMillis((sunDown + (1000 * 60 * 30))); // 30 minutes after sunset

        if(weather == Condition.Severe){
            return DefaultColors.Severe;
        }

        // Night time
        if(nowCal.getTimeInMillis() < goldHour.getTimeInMillis() || nowCal.getTimeInMillis() > blueHour.getTimeInMillis()){

            // Dark sky (clouds at night)
            if(weather == Condition.Cloudy || weather == Condition.Rain || weather == Condition.Storm){ return DefaultColors.DarkNight; }
            else{ return DefaultColors.Night; }
        }

        // Gold Hour
        if(nowCal.getTimeInMillis() > goldHour.getTimeInMillis() && nowCal.getTimeInMillis() < sunRiseCal.getTimeInMillis()){

            // Transition night into day
            long totalTimeMillis = sunRiseCal.getTimeInMillis() - goldHour.getTimeInMillis();
            long currentTimePast = nowCal.getTimeInMillis() - goldHour.getTimeInMillis();
            double percent = totalTimeMillis / currentTimePast;

            int startRed = 0; int endRed;
            int startGreen = 0; int endGreen;
            int startBlue = 0; int endBlue;

            // Dark sky (clouds at night) - start color
            if(weather == Condition.Cloudy || weather == Condition.Rain || weather == Condition.Storm){
                startRed = Color.red(DefaultColors.DarkNight);
                startGreen = Color.green(DefaultColors.DarkNight);
                startBlue = Color.blue(DefaultColors.DarkNight);
            }
            else{
                startRed = Color.red(DefaultColors.Night);
                startGreen = Color.green(DefaultColors.Night);
                startBlue = Color.blue(DefaultColors.Night);
            }

            if(weather == Condition.Cloudy){
                endRed = Color.red(DefaultColors.CloudDay);
                endGreen = Color.green(DefaultColors.CloudDay);
                endBlue = Color.blue(DefaultColors.CloudDay);
            }
            else if(weather == Condition.Rain){
                endRed = Color.red(DefaultColors.RainDay);
                endGreen = Color.green(DefaultColors.RainDay);
                endBlue = Color.blue(DefaultColors.RainDay);
            }
            else if(weather == Condition.Storm){
                endRed = Color.red(DefaultColors.StormDay);
                endGreen = Color.green(DefaultColors.StormDay);
                endBlue = Color.blue(DefaultColors.StormDay);
            }
            else{
                endRed = Color.red(DefaultColors.ClearDay);
                endGreen = Color.green(DefaultColors.ClearDay);
                endBlue = Color.blue(DefaultColors.ClearDay);
            }

            double finalRed = ((1.0 - percent) * (double)startRed) + (percent * (double)endRed);
            double finalGreen = ((1.0 - percent) * (double)startGreen) + (percent * (double)endGreen);
            double finalBlue = ((1.0 - percent) * (double)startBlue) + (percent * (double)endBlue);

            return Color.argb(255, (int)finalRed, (int)finalGreen, (int)finalBlue);
        }

        // Day Time
        if(nowCal.getTimeInMillis() > sunRiseCal.getTimeInMillis() && nowCal.getTimeInMillis() < sunSetCal.getTimeInMillis()){
            if(weather == Condition.Cloudy) { return DefaultColors.CloudDay; }
            else if(weather == Condition.Rain) { return DefaultColors.RainDay; }
            else if(weather == Condition.Storm) { return DefaultColors.StormDay; }
            else{ return DefaultColors.ClearDay; }
        }

        // Blue Hour
        if(nowCal.getTimeInMillis() > sunSetCal.getTimeInMillis() && nowCal.getTimeInMillis() < blueHour.getTimeInMillis()){

            // Transition day into night
            long totalTimeMillis = blueHour.getTimeInMillis() - sunSetCal.getTimeInMillis();
            long currentTimePast = blueHour.getTimeInMillis() - nowCal.getTimeInMillis();
            double percent = (double)currentTimePast / (double)totalTimeMillis;


            int startRed = 0; int endRed;
            int startGreen = 0; int endGreen;
            int startBlue = 0; int endBlue;

            // Dark sky (clouds at night) - start color
            if(weather == Condition.Cloudy || weather == Condition.Rain || weather == Condition.Storm){
                endRed = Color.red(DefaultColors.DarkNight);
                endGreen = Color.green(DefaultColors.DarkNight);
                endBlue = Color.blue(DefaultColors.DarkNight);
            }
            else{
                endRed = Color.red(DefaultColors.Night);
                endGreen = Color.green(DefaultColors.Night);
                endBlue = Color.blue(DefaultColors.Night);
            }

            if(weather == Condition.Cloudy){
                startRed = Color.red(DefaultColors.CloudDay);
                startGreen = Color.green(DefaultColors.CloudDay);
                startBlue = Color.blue(DefaultColors.CloudDay);
            }
            else if(weather == Condition.Rain){
                startRed = Color.red(DefaultColors.RainDay);
                startGreen = Color.green(DefaultColors.RainDay);
                startBlue = Color.blue(DefaultColors.RainDay);
            }
            else if(weather == Condition.Storm){
                startRed = Color.red(DefaultColors.StormDay);
                startGreen = Color.green(DefaultColors.StormDay);
                startBlue = Color.blue(DefaultColors.StormDay);
            }
            else{
                startRed = Color.red(DefaultColors.ClearDay);
                startGreen = Color.green(DefaultColors.ClearDay);
                startBlue = Color.blue(DefaultColors.ClearDay);
            }

            double finalRed = (percent * (double)startRed) + ((1.0 - percent) * (double)endRed);
            double finalGreen = ( percent * (double)startGreen) + ((1.0 - percent) * (double)endGreen);
            double finalBlue = (percent * (double)startBlue) + ((1.0 - percent) * (double)endBlue);

            return Color.argb(255, (int)finalRed, (int)finalGreen, (int)finalBlue);
        }

        return 0;
    }


}
