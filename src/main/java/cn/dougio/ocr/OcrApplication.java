package cn.dougio.ocr;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Iterator;

/**
 *
 */
@SpringBootApplication
public class OcrApplication {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext app = new SpringApplicationBuilder(OcrApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .headless(false).run(args);

        app.getEnvironment().getSystemProperties().put("endpoints.jmx.enabled", false);

        FastOcr fastOcr = (FastOcr) app.getBean("fastOcr");
        JSONObject obj = fastOcr.loadImage("ocr_tmp.jpg");

//        ClipBoardOperator clipBoardOperator = (ClipBoardOperator) app.getBean("clipBoardOperator");

        ClipBoardOperator clipBoardOperator = new ClipBoardOperator();

        JSONArray array = (JSONArray) obj.get("words_result");
        Iterator<Object> iterator = array.iterator();

        StringBuffer sb = new StringBuffer();

        while (iterator.hasNext()) {
            JSONObject words = (JSONObject) iterator.next();
            String currentWords = words.get("words").toString();
            sb.append(currentWords + "\n");
        }

        clipBoardOperator.writeTextToClipboard(sb.toString());
    }

}
