package cn.dougio.ocr;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 通过语音合成，直接阅读 OCR 后的语音，方便读屏
 */
@Component
public class BaiduReader {
    private static final Logger logger = getLogger(BaiduReader.class);

    @Autowired
    AipSpeech aipSpeech;

    public void readText(String text) {

        String bip = "output.mp3";

        TtsResponse res = aipSpeech.synthesis(text, "zh", 1, null);
        byte[] data = res.getData();
        JSONObject res1 = res.getResult();
        if (data != null) {
            try {
                Util.writeBytesToFileSystem(data, bip);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (res1 != null) {
            System.out.println(res1.toString(2));
        }
        com.sun.javafx.application.PlatformImpl.startup(()->{});
        Media hit = new Media(new File(bip).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }
}
