import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestPush {
    @Test
    public void test1() {
        HashMap<String, String> stringStringHashMap1 = new HashMap<>();
        ArrayList<Map> maps = new ArrayList<>();

        stringStringHashMap1.put("1", "2");
        maps.add(stringStringHashMap1);
        stringStringHashMap1.put("3", "4");
        System.out.println(maps);
    }

    @Test
    public void test2() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        Integer process = 1;
        Long time = null;
        String status = "3";
//对象转json字符串 忽略null
        stringObjectHashMap.put("process", process);
        stringObjectHashMap.put("time", time);
        stringObjectHashMap.put("status", status);
        System.out.println(stringObjectHashMap);

        String s = JSON.toJSONString(stringObjectHashMap);
        System.out.println(s);

//json字符串转对象
        String s1 = stringObjectHashMap.toString();
        JSONObject jsonObject = JSON.parseObject(s1);
        System.out.println(jsonObject);


        //json格式不对抛异常
//        String s1 = "{'a':'b',1}";
//        JSONObject jsonObject = JSON.parseObject(s1);
//        System.out.println(jsonObject.toJSONString());
    }

}
