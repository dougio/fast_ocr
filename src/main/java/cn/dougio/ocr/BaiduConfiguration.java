package cn.dougio.ocr;

import com.baidu.aip.ocr.AipOcr;
import com.baidu.aip.speech.AipSpeech;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaiduConfiguration {

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

    @Bean
    AipSpeech aipSpeech() {
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        return client;
    }

}
