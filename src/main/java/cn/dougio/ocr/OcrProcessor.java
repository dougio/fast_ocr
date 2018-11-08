package cn.dougio.ocr;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 */
@Component
public class OcrProcessor {

    private static final Logger logger = getLogger(OcrProcessor.class);

    public static final String OCR_TMP_JPG_PATH = "tmp/ocr_tmp.jpg";

    @Autowired
    BaiduOcr baiduOcr;

    /**
     *
     * @return
     */
    public String getTextFromImage() {
        JSONObject obj;
        try {
            obj = baiduOcr.loadImage(OCR_TMP_JPG_PATH);
        } catch (Exception e) {
            logger.error("Cannot load image to text for: {}", e.getLocalizedMessage());
            return null;
        }

        JSONArray array = (JSONArray) obj.get("words_result");
        Iterator<Object> iterator = array.iterator();

        return extractWordResults(iterator);
    }


    /**
     *
     * @param iterator
     * @return
     */
    private String extractWordResults(Iterator<Object> iterator) {
        StringBuffer sb = new StringBuffer();

        while (iterator.hasNext()) {
            JSONObject words = (JSONObject) iterator.next();
            String currentWords = words.get("words").toString();
            sb.append(currentWords + "\n");
        }
        return sb.toString();
    }

}
