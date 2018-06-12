package cn.dougio.ocr;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Iterator;

/**
 *
 */
@SpringBootApplication
public class OcrApplication {

    //设置APPID/AK/SK
    @Value("${baidu.appid}")
    String APP_ID;
    @Value("${baidu.appkey}")
    String API_KEY;
    @Value("${baidu.secretkey}")
    String SECRET_KEY;

    @Bean
    AipOcr apiOcr() {
        return new AipOcr(APP_ID, API_KEY, SECRET_KEY);
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext app = new SpringApplicationBuilder(OcrApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .headless(false).run(args);

        app.getEnvironment().getSystemProperties().put("endpoints.jmx.enabled", false);

        BaiduOcr baiduOcr = (BaiduOcr) app.getBean("baiduOcr");
        JSONObject obj = baiduOcr.loadImage("ocr_tmp.jpg");

        ClipBoardOperator clipBoardOperator = new ClipBoardOperator();

        JSONArray array = (JSONArray) obj.get("words_result");
        Iterator<Object> iterator = array.iterator();

        StringBuffer sb = extractWordResults(iterator);
        clipBoardOperator.writeTextToClipboard(sb.toString());
    }

    /**
     *
     * @param iterator
     * @return
     */
    private static StringBuffer extractWordResults(Iterator<Object> iterator) {
        StringBuffer sb = new StringBuffer();

        while (iterator.hasNext()) {
            JSONObject words = (JSONObject) iterator.next();
            String currentWords = words.get("words").toString();
            sb.append(currentWords + "\n");
        }
        return sb;
    }

}
