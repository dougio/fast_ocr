package cn.dougio.ocr;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;

import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 */
@SpringBootApplication
public class OcrApplication implements ApplicationRunner {

    private static final Logger logger = getLogger(OcrApplication.class);
    public static final String TMP_FOLDER = "tmp";

    @Autowired
    BaiduReader baiduReader;

    @Autowired
    ClipBoardOperator clipBoardOperator;

    @Autowired
    OcrProcessor ocrProcessor;

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

        String text = ocrProcessor.getTextFromImage();
        if (text == null) return;


        if (!args.containsOption("keepimg")) {
            // 文本写入剪贴板
            clipBoardOperator.writeTextToClipboard(text);
        }

        if (args.containsOption("read")) {
            logger.info("read the text in image");
            // 音频写入文件
            baiduReader.readText(text);
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

}
