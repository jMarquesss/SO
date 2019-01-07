import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class Writer {

    private Reader reader = new Reader();

    public boolean escritorMovimentos(String movimentos) {
        JSONObject jsonObject = new JSONObject();
        JSONArray arrayAndar;
        if (reader.andaresReader() != null) {
            arrayAndar = reader.andaresReader();
        } else {
            arrayAndar = new JSONArray();
        }

        int i = 0;
        JSONObject andar = new JSONObject();
        andar.put("Andar", movimentos);
        arrayAndar.add(andar);

        jsonObject.put("Andares visitados", arrayAndar);
        jsonObject.put("Entradas", arrayAndar.size());
        try {
            FileWriter file = new FileWriter("movimentos.txt");
            file.write(jsonObject.toJSONString());
            file.close();
            return true;
        } catch (IOException ex) {
            return false;
        }

    }
}
