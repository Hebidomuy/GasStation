package utils;

import com.google.gson.Gson;
import dto.Message;

public class JsonUtil {
    public static String toJson(Message message){
        Gson gson = new Gson();
        return gson.toJson(message);

    }
    public static Message fromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Message.class);

    }
}
