package app.myandroidlocations.com.myandroidlocationsapp.Utils.SharedPrefUtils;

import android.content.SharedPreferences;

public class SharedPrefHelper {

    private SharedPreferences.Editor editor;

    public SharedPrefHelper(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    public SharedPrefHelper keepString(String key, String value) {
        editor.putString(key, value);
        return this;
    }

    public SharedPrefHelper keepBool(String key, Boolean value) {
        editor.putBoolean(key, value);
        return this;
    }

    public SharedPrefHelper keepInt(String key, Integer value) {
        editor.putInt(key, value);
        return this;
    }

    public SharedPrefHelper keepFloat(String key, Float value) {
        editor.putFloat(key, value);
        return this;
    }

    public SharedPrefHelper keepLong(String key, Long value) {
        editor.putLong(key, value);
        return this;
    }

    public void save() {
        editor.commit();
    }

    public SharedPrefHelper delete(String key) {
        editor.remove(key);
        return this;
    }

    public SharedPrefHelper clear() {
        editor.clear();
        return this;
    }
}
