package net.app.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author hu_xuanhua_hua
 * @ClassName: HttpUtils
 * @Description: TODO
 * @date 2018-12-10 16:13
 * @versoin 1.0
 **/
public class HttpUtils {
    public static String HttpPost(String param1,String param2,String url) throws Exception{
        Map<String,String> personMap = new HashMap<String,String>();
        personMap.put("kw",param1);
//        personMap.put("param1",param2);
        List<NameValuePair> list = new LinkedList<NameValuePair>();
        for(Map.Entry<String,String> entry:personMap.entrySet()){
            list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }
        HttpPost httpPost = new HttpPost(url);
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list,"utf-8");
        httpPost.setEntity(formEntity);
        HttpClient httpCient = HttpClients.createDefault();
        HttpResponse httpresponse = null;
        try{
            httpresponse = httpCient.execute(httpPost);
            HttpEntity httpEntity = httpresponse.getEntity();
            String response = EntityUtils.toString(httpEntity, "utf-8");
            JSONObject json = JSONObject.parseObject(response);
            return response;
        }catch(ClientProtocolException e){
            System.out.println("http请求失败，uri{},exception{}");
        }catch(IOException e){
            System.out.println("http请求失败，uri{},exception{}");
        }
        return null;
    }
}
