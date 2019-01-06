import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class Reader {
    public int fileReader() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("json.txt"));
            JSONObject jsonObject = (JSONObject) obj;
            String andares = (String) jsonObject.get("Andares");
            return Integer.parseInt(andares);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public JSONArray andaresReader() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("movimentos.txt"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray andares = (JSONArray) jsonObject.get("Andares visitados");
            return andares;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}