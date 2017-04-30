package activitystarter.generation.converters;

import android.app.Activity;

import com.marcinmoskala.activitystarterparcelerargconverter.IsParcel;
import com.marcinmoskala.activitystarterparcelerargconverter.ParcelarArgConverter;

import org.parceler.Parcel;

import activitystarter.ActivityStarterConfig;
import activitystarter.Arg;

@ActivityStarterConfig(converters = {ParcelarArgConverter.class})
public class ActivityWithConverters extends Activity {
    @Arg
    StudentParcel studentParceler;

    @Parcel
    public static class StudentParcel implements IsParcel {

        private int id;
        private String name;
        private char grade;

        public StudentParcel() {
        }

        // Constructor
        public StudentParcel(int id, String name, char grade) {
            this.id = id;
            this.name = name;
            this.grade = grade;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public char getGrade() {
            return grade;
        }

        public void setGrade(char grade) {
            this.grade = grade;
        }
    }
}
