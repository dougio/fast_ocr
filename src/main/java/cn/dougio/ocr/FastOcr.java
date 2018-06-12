package cn.dougio.ocr;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 调用 baidu 文字识别服务
 */
@Component
public class FastOcr {

    private static final Logger logger = getLogger(FastOcr.class);

    //设置APPID/AK/SK
    @Value("${baidu.appid}")
    String APP_ID;
    @Value("${baidu.appkey}")
    String API_KEY;
    @Value("${baidu.secretkey}")
    String SECRET_KEY;

    @Autowired
    ClipBoardOperator clipBoardOperator;

    public JSONObject loadImage(String tmpLocation) throws Exception {

        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 调用接口
//        String path = "/Users/houdg/tmp/tmp.log";
//        byte[] file = readImageFile(path);
//        JSONObject response2 = client.basicGeneral(file, new HashMap<String, String>());
//        System.out.println(response2.toString());

        Image image = clipBoardOperator.loadImageFromClipboard();
        BufferedImage image1 = clipBoardOperator.getBufferedImage(image);

        File outputfile = new File(tmpLocation != null ? tmpLocation : "ocr_tmp.jpg");
        ImageIO.write(clipBoardOperator.getBufferedImage(image1), "jpg", outputfile);

        JSONObject res = client.basicGeneral(getBytes(image1), new HashMap<>());
        logger.info(res.toString(2));
        return res;
    }


    public byte[] readImageFile(String ImageName) throws IOException {
        // open image
        File imgPath = new File(ImageName);
        BufferedImage bufferedImage = ImageIO.read(imgPath);

        return getBytes(bufferedImage);
    }

    private byte[] getBytes(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        return imageInByte;
    }


}
