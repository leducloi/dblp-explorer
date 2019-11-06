import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.*;
import java.lang.*;
import java.io.*;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

public class Explorer {
    private String keyword;
    private String input_name;
    private int n;
    private LinkedList<JsonObject> all_obj;
    private HashMap<String, Integer> all_tier;
    private HashMap<String, Integer> all_obj_line;
    InputStream in;

    Explorer(String key, String input_name){
        this.keyword = key;
        this.n = 1;
        this.input_name = input_name;
        this.all_obj = new LinkedList<JsonObject>();
        this.all_tier = new HashMap<String, Integer>();
        this.all_obj_line = new HashMap<String, Integer>();

    }

    public void store_1stObj(){
        try{
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            InputStream in = classLoader.getResourceAsStream(input_name);
            InputStreamReader inr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(inr);
            int num_line = 0;
            String line;
            while( (line = br.readLine()) != null){
                JsonElement json_line = new JsonParser().parse(line);
                JsonObject jobj = json_line.getAsJsonObject();

                JsonElement json_el = jobj.get("title");
                JsonElement json_id = jobj.get("id");
                String title = json_el.getAsString();
                String id = json_id.getAsString();

                boolean foundKey = title.contains(keyword);
                if (foundKey == true){
                    all_obj.add(jobj);
                    all_tier.put(id, 1);
                }
                all_obj_line.put(id, num_line);
                num_line++;
            }
//            line = br.readLine();
//            line = br.readLine();
//            // System.out.println(line);
//            JsonElement json_line = new JsonParser().parse(line);
//            JsonObject jobj = json_line.getAsJsonObject();
//            JsonElement id_el = jobj.get("id");
//            String id = id_el.getAsString();
//            System.out.println(id);
//            JsonArray json_ref = jobj.getAsJsonArray("references");
//            if (json_ref != null){
//                for (JsonElement all_ref : json_ref){
//                    String all_id = all_ref.getAsString();
//                    System.out.println(all_id);
//                }
//            }
        }
        catch(Exception event){
            event.printStackTrace();
        }

    }

    public void goThrough_1stTier(){
        for (JsonObject each_obj : all_obj){
            getNextTier(2, each_obj);
        }
    }

    public void getNextTier(int n, @NotNull JsonObject jobj ){
        JsonArray json_ref = jobj.getAsJsonArray("references");
        if (json_ref == null){
            return;
        }
        else{
            for (JsonElement all_ref : json_ref){
                String each_id = all_ref.getAsString();
                all_tier.put(each_id, n);
                System.out.println(all_obj_line.containsKey(each_id));
                if (all_obj_line.containsKey(each_id) == true){
                    int line_num = all_obj_line.get(each_id);
                    try{
                        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
                        String path = classLoader.getResource(input_name).getPath();
                        Stream<String> lines = Files.lines(Paths.get(path));
                        String line = lines.skip(line_num).findFirst().get();

                        JsonElement json_line = new JsonParser().parse(line);
                        JsonObject each_obj = json_line.getAsJsonObject();
                        n += 1;
                        getNextTier(n, each_obj);
                    }
                    catch (Exception event){
                        event.printStackTrace();
                    }
                }
            }
        }

    }

    public void print_allTier(){
        for (String key : all_tier.keySet()) {
            System.out.println(key + " " + all_tier.get(key));
        }
    }

    public static void main(String args[]){
        Scanner scan = new Scanner(System.in);
//        System.out.print("Enter the keyword: ");
//        String key = scan.nextLine();
//        System.out.print("Enter the name of .txt file in resources: ");
//        String input_name = scan.nextLine();
        String input_name = new String( "dblp_papers_v11_first_100_lines.txt");
        String key = new String("Remote");

        Explorer newExplorer = new Explorer(key, input_name);
        newExplorer.store_1stObj();
        newExplorer.goThrough_1stTier();
        newExplorer.print_allTier();

    }

}
