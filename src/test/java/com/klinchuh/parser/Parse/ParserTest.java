package com.klinchuh.parser.Parse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.*;
import org.junit.rules.ExpectedException;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class ParserTest {
    private String input;
    private String output;
    private JSONParser jsonSimpleParser;
    private Boolean withoutAnswer;

    @Before
    public void  preLoad() {
        jsonSimpleParser = new JSONParser();
        input = "";
        output = "{}";
        withoutAnswer = false;
    }

    @After
    public void prcTest() {
        InputStream is = new ByteArrayInputStream(input.getBytes());
        OutputStream os = new ByteArrayOutputStream();
        Parser parser = new Parser();

        if(withoutAnswer)  {
            parser.get(is, os);
            try {
                JSONObject b = (JSONObject) jsonSimpleParser.parse(os.toString());
            } catch (ParseException e) {
                e.printStackTrace();
                Assert.fail("Not correct output JSON file");
            }
        } else {
            try {
                JSONObject b = (JSONObject) jsonSimpleParser.parse(output);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            parser.get(is, os);
            try {
                JSONObject b = (JSONObject) jsonSimpleParser.parse(os.toString());
                JSONObject a = (JSONObject) jsonSimpleParser.parse(output);
                Assert.assertEquals(a, b);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void getEmpty() {
        input = "";
        output = "{}";
    }
    @Test
    public void getOneElement() {
        input = "a = 1";
        output = "{\"a\":{\"value\":\"1\"}}";
    }
    @Test
    public void getFewFirstLevelElement() {
        input = "a = 1\nb=1\nc=3";
        output = "{\"a\":{\"value\":\"1\"}\"b\":{\"value\":\"1\"}\"c\":{\"value\":\"3\"}}";
    }
    @Test
    public void getFewSameLevelElement1() {
        input = "a=1\na=2\na=3";
        output = "{\"a\":{\"value\":\"3\"}}";
    }
    @Test
    public void getFewSameLevelElement2() {
        input = "xt.a=1\nxt.a=3\nxt.a=2";
        output = "{\"xt\":{\"a\":{\"value\":\"2\"}}}";
    }
    @Test
    public void getFewSameLevelElement3() {
        input = "a=2\na=3\na=1";
        output = "{\"a\":{\"value\":\"1\"}}";
    }
    @Test
    public void getFewSameLevelElement4() {
        input = "a=3\na=2\na=3";
        output = "{\"a\":{\"value\":\"3\"}}";
    }

    @Test
    public void getFewElement1() {
        input = "x.a=3\ny.a=2\nz.a=3";
        output = "{\"x\":{\"a\":{\"value\":\"3\"}} \"y\":{\"a\":{\"value\":\"2\"}} \"z\":{\"a\":{\"value\":\"3\"}}}";
    }
    @Test
    public void getFewElement2() {
        input = "x.a=4\nx.a=2\nz.a=3\nx = 2";
        output = "{\"x\":{\"a\":{\"value\":\"4\"} \"a\":{\"value\":\"2\"} \"value\":\"2\"} \"z\":{\"a\":{\"value\":\"3\"}}}";
    }
    @Test
    public void getFewElement3() {
        input = "number = 123\nobject.a = b\nobject.c = d\nobject.e = f\nstring = Hello World";
        output = "{\n" +
                "  \"number\": {" +
                "    \"value\":\"123\",\n }" +
                "  \"object\": {\n" +
                "    \"a\": {" +
                "     \"value\":\"b\"},\n" +
                "    \"c\": {" +
                "      \"value\":\"d\"},\n" +
                "    \"e\": {" +
                "      \"value\":\"f\"}\n" +
                "  },\n" +
                "  \"string\": {" +
                "    \"value\":\"HelloWorld\"}\n" +
                "}";
    }
    @Test
    public void getFewElement4() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < 1999; i++) {
            stringBuilder.append("a.");
        }
        stringBuilder.append("a = 23");
        input = stringBuilder.toString();

        stringBuilder.delete(0, stringBuilder.length());

        stringBuilder.append("{");
        for(int i = 0; i < 2000; i++) {
            stringBuilder.append("\"a\":{");
        }
        stringBuilder.append("\"value\":\"23\"");
        for(int i = 0; i < 2000; i++) {
            stringBuilder.append("}");
        }
        stringBuilder.append("}");
        output = stringBuilder.toString();
    }
    @Test
    public void getFewElement5() {
        StringBuilder stringBuilder = new StringBuilder(), total = new StringBuilder();
        for(int i = 0; i < 2000; i++) {
            stringBuilder.append("a");
            total.append(stringBuilder);
            total.append("=1\n");
            stringBuilder.append(".");
        }
        input = total.toString();
        stringBuilder.delete(0, stringBuilder.length());

        stringBuilder.append("{");
        for(int i = 0; i < 2000; i++) {
            stringBuilder.append("\"a\":{");
            if(i != 1999) {
                stringBuilder.append("\"value\":\"1\",");
            } else {
                stringBuilder.append("\"value\":\"1\"");
            }
        }
        for(int i = 0; i < 2000; i++) {
            stringBuilder.append("}");
        }

        stringBuilder.append("}");
        output = stringBuilder.toString();
    }

    @Test
    public void getFewWithoutAns1() {
        input = "===============";
        withoutAnswer = true;
    }
    @Test
    public void getFewWithoutAns2() {
        input = "v.fv.fv.f=fv.fv.fv-fvvfvf=fv=fv=1";
        withoutAnswer = true;
    }
    @Test
    public void getFewWithoutAns3() {
        input = "..................";
        withoutAnswer = true;
    }
    @Test
    public void getFewWithoutAns4() {
        input = "..................=2";
        withoutAnswer = true;
    }
    @Test
    public void getFewWithoutAns5() {
        input = ".==.=.=..=.=.=.=.=.=.=.";
        withoutAnswer = true;
    }
    @Test
    public void getFewWithoutAns6() {
        input = "cdfgnmhgfdsfgnhbvcd=...........";
        withoutAnswer = true;
    }
}