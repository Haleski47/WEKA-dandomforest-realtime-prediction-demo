import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.HashSet;
import java.io.*;


public class JsonTest {

    //读取json文件
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws  Exception{
        //设置json路径
//        String path = JsonTest.class.getClass().getResource("test_data.json").getPath();
//        String s = readJsonFile(path);
        String s = readJsonFile("test_data.json");
        JSONObject jobj = JSON.parseObject(s);
        String expected_intensity_filename = "expected_intensity_data.arff";
        String actual_intensity_filename = "actual_intensity_data.arff";
        //从json中读取各项数据
        JSONObject motions = jobj.getJSONObject("motion");//构建motion对象
        int actual_intensity = motions.getInteger("actual_intensity");
        int expected_intensity = motions.getInteger("expected_intensity");
        int motion_type = motions.getInteger("motion_type");

        JSONObject sequence = jobj.getJSONObject("sequence");//构建sequence对象
        long  startTime = sequence.getLong("startTime");
        long  endTime = sequence.getLong("endTime");
        JSONArray packageNum = sequence.getJSONArray("packageNum");

        JSONObject profile = jobj.getJSONObject("profile");//构建profile对象
        int age = profile.getInteger("age");
        int gender = profile.getInteger("gender");
        int height = profile.getInteger("height");
        String id = profile.getString("id");
        Double weight = profile.getDouble("weight");

        JSONObject physiological_signal = jobj.getJSONObject("physiological_signal");//构建physiological_signal对象
        JSONArray ecg_data = physiological_signal.getJSONArray("ecg_data");
        JSONArray ecg_source_data = physiological_signal.getJSONArray("ecg_source_data");
        JSONArray heart_rate = physiological_signal.getJSONArray("heart_rate");
        JSONArray temperature = physiological_signal.getJSONArray("temperature");
        //heart_rate_variability
        //respiration_rate

        int startIndex = jobj.getInteger("startIndex");
        int endIndex = jobj.getInteger("endIndex");

        try {
            //out1 对应的expected_intensity这个arff数据库；
            //out2对应actual_intensity这个arff数据库
            //arff的文件头
            BufferedWriter out1 = new BufferedWriter(new FileWriter("expected_intensity_data.arff"));
            out1.write("@relation 'expected_intensity_inference'\r\n");
            out1.write("@attribute expected_intensity numeric\r\n");
            out1.write("@attribute height numeric\r\n");
            out1.write("@attribute weight numeric\r\n");
            out1.write("@attribute age numeric\r\n");
            out1.write("@attribute gender numeric\r\n");
            out1.write("@attribute heart_rate numeric\r\n");
            out1.write("@attribute temperature numeric\r\n");
            out1.write("@data\r\n");
            //arff的文件头
            BufferedWriter out2 = new BufferedWriter(new FileWriter("actual_intensity_data.arff"));
            out2.write("@relation 'actual_intensity_inference'\r\n");
            out2.write("@attribute actual_intensity numeric\r\n");
            out2.write("@attribute height numeric\r\n");
            out2.write("@attribute weight numeric\r\n");
            out2.write("@attribute age numeric\r\n");
            out2.write("@attribute gender numeric\r\n");
            out2.write("@attribute heart_rate numeric\r\n");
            out2.write("@attribute temperature numeric\r\n");
            out2.write("@data\r\n");
            //从json内的信号数组中逐个提取信号，并写入两个数据集
            int max_size = Math.max(heart_rate.size(),temperature.size());
            int min_size = Math.min(heart_rate.size(),temperature.size());
            int hr_step, tem_step;
            for(int i=0; i < max_size - max_size%min_size; ++i){
                hr_step = i / (max_size /heart_rate.size());
                tem_step = (i + max_size%min_size/2) / (max_size /temperature.size());
                if((heart_rate.getInteger(hr_step)==-1) || (temperature.getDouble(tem_step)==-1)){
                    continue;
                }
                else{
                    out1.write(expected_intensity + ",");
                    out1.write(height + ",");
                    out1.write(weight + ",");
                    out1.write(age + ",");
                    out1.write(gender + ",");
                    out1.write(heart_rate.get(hr_step) + ",");
                    out1.write(temperature.get(tem_step) + "\r\n");

                    out2.write(actual_intensity + ",");
                    out2.write(height + ",");
                    out2.write(weight + ",");
                    out2.write(age + ",");
                    out2.write(gender + ",");
                    out2.write(heart_rate.get(hr_step) + ",");
                    out2.write(temperature.get(tem_step) + "\r\n");
                }
            }
            out1.close();
            out2.close();
            System.out.println("文件创建成功！");
        } catch (IOException e) {
        }

        //对提取到的两个arff数据文件去冗杂。
        BufferedReader actual_br = new BufferedReader(new FileReader(actual_intensity_filename));
        BufferedReader expected_br = new BufferedReader(new FileReader(expected_intensity_filename));
        PrintWriter actual_pw = new PrintWriter("new_" + actual_intensity_filename);
        PrintWriter expected_pw = new PrintWriter("new_" + expected_intensity_filename);

        String actual_line = actual_br.readLine();
        HashSet<String> actual_hs = new HashSet<String>();
        while(actual_line != null)
        {
            if(actual_hs.add(actual_line))
                actual_pw.println(actual_line);
            actual_line = actual_br.readLine();
        }

        String expected_line = expected_br.readLine();
        HashSet<String> expected_hs = new HashSet<String>();
        while(expected_line != null)
        {
            if(expected_hs.add(expected_line))
                expected_pw.println(expected_line);
            expected_line = expected_br.readLine();
        }
        actual_pw.flush();
        expected_pw.flush();

        actual_br.close();
        actual_pw.close();
        expected_br.close();
        expected_pw.close();
        System.out.println("File operation performed successfully");
    }
}