package cn.dougio.ocr;

import com.baidu.aip.ocr.AipOcr;
import com.baidu.aip.speech.AipSpeech;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.Iterator;

import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 */
@SpringBootApplication
public class OcrApplication implements ApplicationRunner {

    private static final Logger logger = getLogger(OcrApplication.class);

    public static final String OCR_TMP_JPG_PATH = "tmp/ocr_tmp.jpg";
    public static final String TMP_FOLDER = "tmp";

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

    @Autowired
    BaiduOcr baiduOcr;

    @Autowired
    BaiduReader baiduReader;

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) {

        ConfigurableApplicationContext app = new SpringApplicationBuilder(OcrApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .headless(false).run(args);

        app.getEnvironment().getSystemProperties().put("endpoints.jmx.enabled", false);

    }

    @Override
    public void run(ApplicationArguments args) {

        logger.info("args.getOptionNames:\t{}", args.getOptionNames());

        mkTmpDir();

        if (args.containsOption("help")) {
            printHelp();
            return;
        }

        JSONObject obj;
        try {
            obj = baiduOcr.loadImage(OCR_TMP_JPG_PATH);
        } catch (Exception e) {
            logger.error("Cannot load image to text for: {}", e.getLocalizedMessage());
            return;
        }

        ClipBoardOperator clipBoardOperator = new ClipBoardOperator();

        JSONArray array = (JSONArray) obj.get("words_result");
        Iterator<Object> iterator = array.iterator();

        StringBuffer sb = extractWordResults(iterator);

        if (!args.containsOption("keepimg")) {
            // 文本写入剪贴板
            clipBoardOperator.writeTextToClipboard(sb.toString());
        }

        if (args.containsOption("read")) {
            logger.info("read the text in image");
            // 音频写入文件
            baiduReader.readText(sb.toString());
        }
    }

    /**
     * prepare temp folder to restore image
     */
    private void mkTmpDir() {
        File directory = new File(TMP_FOLDER);
        if (! directory.exists()){
            directory.mkdir();
        }
    }

    /**
     * help text
     */
    public void printHelp() {
        StringBuffer sb = new StringBuffer("usage: java -jar ocr.jar [--help][--read]");
        sb.append("       ln [-Ffhinsv] source_file ... target_dir");
        sb.append("       link source_file target_file");
        System.out.println(sb);
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
