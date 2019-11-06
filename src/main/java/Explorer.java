import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.*;
import java.lang.*;
import java.io.*;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

public class Explorer {
    private String keyword;
    private String input_name;
    private int n;
    private LinkedList<JsonObject> all_obj;
    private HashMap<Integer, Collection<String>> all_tier;
    private HashMap<String, Integer> all_obj_line;
    InputStream in;

    Explorer(String key, String input_name){
        this.keyword = key;
        this.n = 1;
        this.input_name = input_name;
        this.all_obj = new LinkedList<JsonObject>();
        this.all_tier = new HashMap<Integer, Collection<String>>();
        this.all_obj_line = new HashMap<String, Integer>();

    }

    public void store_1stObj(){
        try{
            BufferedReader br = new BufferedReader(new FileReader(input_name));
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
                    Collection<String> all_values = all_tier.get(1);
                    if (all_values==null) {
                        all_values = new ArrayList<String>();
                        all_tier.put(1, all_values);
                    }
                    all_values.add(id);
                }
                all_obj_line.put(id, num_line);
                num_line++;
            }
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
                Collection<String> all_values = all_tier.get(n);
                if (all_values==null) {
                    all_values = new ArrayList<String>();
                    all_tier.put(n, all_values);
                }
                all_values.add(each_id);
                if (all_obj_line.containsKey(each_id) == true){
                    int line_num = all_obj_line.get(each_id);
                    try{
                        Stream<String> lines = Files.lines(Paths.get(input_name));
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
        for (Integer key : all_tier.keySet()) {
            System.out.println(key + " " + all_tier.get(key));
        }
    }

    public static void main(String args[]){
        Scanner scan = new Scanner(System.in);
//        System.out.print("Enter the keyword: ");
//        String key = scan.nextLine();
//        System.out.print("Enter the name of .txt file in dblp-explorer folder: ");
//        String input_name = scan.nextLine();
        String input_name = new String("dblp_papers_v11_first_100_lines.txt");
        String key = new String("Remote");

        Explorer newExplorer = new Explorer(key, input_name);
        newExplorer.store_1stObj();
        newExplorer.goThrough_1stTier();
        newExplorer.print_allTier();

    }

}
