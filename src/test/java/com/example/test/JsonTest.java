package com.example.test;

import com.example.message.Message;
import com.example.message.MsgRX;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author: ming
 * @date: 2021/11/2 20:48
 */
public class JsonTest {

    @Test
    public void testJackson() throws InvalidProtocolBufferException {
        MsgRX msgRes = Message.newBuilder().getMsgRX()
                .newBuilderForType()
                .setSUsername("xixi")
                .setContent("hello world")
                .build();
        Message message = Message.newBuilder()
                .setMessageType(Message.MessageType.MSG_RX)
                .setMsgRX(msgRes)
                .build();
        String s = JsonFormat.printer().print(message);
        System.out.println(s);

        Message.Builder a = Message.newBuilder();
        JsonFormat.parser().merge(s,a);
        Message build = a.build();
        System.out.println(build);
    }

    @Test
    public void testMap(){
        HashMap<String, String> map = new HashMap<>();
        map.put("aaa","qqq");
        map.remove("aaa");
        System.out.println(map.containsKey("aaa"));
        System.out.println(map.get("aaa"));
    }
}
