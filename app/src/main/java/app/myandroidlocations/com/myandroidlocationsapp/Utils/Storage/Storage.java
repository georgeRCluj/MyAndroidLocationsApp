package app.myandroidlocations.com.myandroidlocationsapp.Utils.Storage;

import android.content.SharedPreferences;

public class Storage {

    private SharedPreferences.Editor editor;

    public Storage(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    public Storage keepString(String key, String value) {
        editor.putString(key, value);
        return this;
    }

    public Storage keepBool(String key, Boolean value) {
        editor.putBoolean(key, value);
        return this;
    }

    public Storage keepInt(String key, Integer value) {
        editor.putInt(key, value);
        return this;
    }

    public Storage keepFloat(String key, Float value) {
        editor.putFloat(key, value);
        return this;
    }

    public Storage keepLong(String key, Long value) {
        editor.putLong(key, value);
        return this;
    }

    public void save() {
        editor.commit();
    }

    public Storage delete(String key) {
        editor.remove(key);
        return this;
    }

    public Storage clear() {
        editor.clear();
        return this;
    }
}
