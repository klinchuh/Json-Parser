package Parse;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class Parser {
    private ArrayList <ArrayList<String>> lineArray;
    public Parser() {
        lineArray = new ArrayList<>();
    }

    class Layer {
        Layer(String layerName, JSONObject layerObject) {
            this.layerName = layerName;
            this.layerObject = layerObject;
        }

        public String getLayerName() {
            return layerName;
        }

        public JSONObject getLayerObject() {
            return layerObject;
        }

        String layerName;
        JSONObject layerObject;
    }

    public void get(InputStream in, OutputStream out) {
        Scanner scanner = new Scanner(in);

        while(scanner.hasNextLine()) {
            char[] procArray = scanner.nextLine().toCharArray();
            StringBuilder tempString = new StringBuilder();
            lineArray.add(new ArrayList<>());

            for(char el: procArray) {
                if(el == ' ')
                    continue;

                if(el == '.' || el == '=') {
                    lineArray.get(lineArray.size() - 1).add(tempString.toString());
                    tempString.delete(0, tempString.length());
                } else {
                    tempString.append(el);
                }
            }

            lineArray.get(lineArray.size() - 1).add(tempString.toString());
        }

        Collections.sort(lineArray, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                for(int i = 0; i < Integer.min(o1.size() - 1, o2.size() - 1); i++) {
                    int res = o1.get(i).compareTo(o2.get(i));
                    if(res != 0) {
                        return res;
                    }
                }
                return o1.size() - o2.size();
            }
        });

        StringBuilder sbld;

        JSONObject total = new JSONObject();

        ArrayList <Layer> objectsStack = new ArrayList<>();

        lineArray.add(new ArrayList<>());


        for(ArrayList<String> nextLine: lineArray) {
            int idx = 0;
            for(; idx < Integer.min(nextLine.size() - 1, objectsStack.size()); idx++) {
                if(!nextLine.get(idx).equals(objectsStack.get(idx).getLayerName())) {
                    break;
                }
            }

            while(objectsStack.size() > 1 && objectsStack.size() > idx) {
                objectsStack.get(objectsStack.size() - 2).getLayerObject().put(
                        objectsStack.get(objectsStack.size() - 1).getLayerName(),
                        objectsStack.get(objectsStack.size() - 1).getLayerObject()
                );
                objectsStack.remove(objectsStack.size() - 1);
            }

            if(idx == 0 && objectsStack.size() > 0) {
                total.put(
                        objectsStack.get(0).getLayerName(),
                        objectsStack.get(0).getLayerObject()
                );
                objectsStack.remove(objectsStack.size() - 1);
            }


            for(; idx < nextLine.size() - 1; idx++) {
                objectsStack.add(new Layer(nextLine.get(idx), new JSONObject()));
            }
            //need to push last line
            if(nextLine.size() > 1) {
                objectsStack.get(objectsStack.size() - 1).getLayerObject().put("value", nextLine.get(nextLine.size() - 1));
            }
        }
        try {
            out.write(total.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
