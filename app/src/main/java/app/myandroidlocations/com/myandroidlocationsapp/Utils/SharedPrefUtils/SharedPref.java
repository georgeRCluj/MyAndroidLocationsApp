package app.myandroidlocations.com.myandroidlocationsapp.Utils.SharedPrefUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import static app.myandroidlocations.com.myandroidlocationsapp.Utils.SharedPrefUtils.Constants.SHARED_PREF_ROOT;

public class SharedPref {
    private static SharedPref instance;

    /**
     * Settings wrapped
     */
    private SharedPreferences settings;

    public SharedPref(Context context) {
        settings = context.getSharedPreferences(SHARED_PREF_ROOT, Context.MODE_PRIVATE);
    }

    public static SharedPref getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPref(context);
        }
        return instance;
    }

    /**
     * Edit local storage
     *
     * @return A storage session
     */
    public SharedPrefHelper edit() {
        return new SharedPrefHelper(settings.edit());
    }

    /**
     * Store the item under the passed key
     *
     * @param key  Key
     * @param item Value
     */
    public void store(String key, String item) {
        edit().keepString(key, item).save();
    }

    public void store(String key, Boolean value) {
        edit().keepBool(key, value).save();
    }

    public void store(String key, Integer value) {
        edit().keepInt(key, value);
    }

    public void store(String key, Float value) {
        edit().keepFloat(key, value);
    }

    /**
     * Get item from storage
     *
     * @param key Key
     * @return Value
     */
    public String getString(String key) {
        return settings.getString(key, null);
    }

    /**
     * Get item from storage
     *
     * @param key      Key
     * @param defaults Default value
     * @return Value
     */
    public String getString(String key, String defaults) {
        return settings.getString(key, defaults);
    }

    public Boolean getBool(String key) {
        return settings.getBoolean(key, false);
    }

    public Boolean getBool(String key, boolean defaults) {
        return settings.getBoolean(key, defaults);
    }

    public Integer getInt(String key) {
        return settings.getInt(key, 0);
    }

    public Float getFloat(String key) {
        return settings.getFloat(key, 0);
    }

    public Long getLong(String key) {
        return settings.getLong(key, 0);
    }

    /**
     * Store object under the passed key
     *
     * @param key    Key
     * @param object Value
     */
    public void store(String key, Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();
            store(key, Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
        } catch (IOException e) {
            //Was not able to serialize object
            e.printStackTrace();
        }
    }


    /**
     * Store object under the passed key
     *
     * @param key    Key
     * @param object Value
     */
    public void store(String key, List<Object> object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();
            store(key, Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
        } catch (IOException e) {
            //Was not able to serialize object
            e.printStackTrace();
        }
    }

    /**
     * Get object from storage
     *
     * @param key Key
     * @param <T> Generic type
     * @return Value
     */
    public <T extends Serializable> T getObject(String key) {
        String objectRepresentation = getString(key);
        if (objectRepresentation != null) {
            try {
                byte bytes[] = Base64.decode(objectRepresentation.getBytes(), Base64.DEFAULT);
                ObjectInputStream si = new ObjectInputStream(new ByteArrayInputStream(bytes));
                T object = (T) si.readObject();
                return object;
            } catch (IOException ioe) {
                //Was not able to de-serialize object
                ioe.printStackTrace();
            } catch (ClassNotFoundException cnfe) {
                //Did not find template representation
                cnfe.printStackTrace();
            } catch (ClassCastException cce) {
                //Wrong template
                cce.printStackTrace();
            }
        }
        return null;
    }

    public void clear(String key) {
        edit().delete(key).save();
    }

    public void clearAll() {
        edit().clear().save();
    }

    /**
     * Wrapper over the share preferences editor that takes care of encapsulating storage calls
     * into a session and persisting them only when save is called
     */
}
