package wellnesters;

import javax.swing.SwingUtilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class App 
{
    public static void main( String[] args )
    {
        new WellNest();
        SwingUtilities.invokeLater(WellNest::new);
    }
}
