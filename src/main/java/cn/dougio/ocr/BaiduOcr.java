package cn.dougio.ocr;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BaiduOcr {

    private static final Logger logger = getLogger(BaiduOcr.class);

    @Autowired
    ClipBoardOperator clipBoardOperator;

    @Autowired
    AipOcr client;

    /**
     *
     * @param tmpLocation 临时文件的路径，用于暂存图片
     * @return 解析结果
     * @throws Exception 读取文件可能出现异常；图像识别网络操作可能出现异常
     */
    public JSONObject loadImage(String tmpLocation) throws Exception {

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        BufferedImage image1 = getBufferedImageFromClipboard(tmpLocation);

        JSONObject res = client.basicAccurateGeneral(getBytes(image1), new HashMap<>());
        logger.info(res.toString(2));
        return res;
    }

    /**
     *
     * @param tmpLocation 临时保存图片文件的路径
     * @return 图片信息流
     * @throws Exception 访问剪切板可能出现异常，保存图片文件时可能出现异常
     */
    public BufferedImage getBufferedImageFromClipboard(String tmpLocation) throws Exception {

        Image image = clipBoardOperator.loadImageFromClipboard();
        BufferedImage image1 = clipBoardOperator.getBufferedImage(image);

        File outputfile = new File(tmpLocation != null ? tmpLocation : "ocr_tmp.jpg");
        ImageIO.write(clipBoardOperator.getBufferedImage(image1), "jpg", outputfile);
        return image1;
    }


    public byte[] readImageFile(String ImageName) throws IOException {
        // open image
        File imgPath = new File(ImageName);
        BufferedImage bufferedImage = ImageIO.read(imgPath);

        return getBytes(bufferedImage);
    }

    /**
     *
     * @param bufferedImage
     * @return
     * @throws IOException
     */
    private byte[] getBytes(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        return imageInByte;
    }


}
