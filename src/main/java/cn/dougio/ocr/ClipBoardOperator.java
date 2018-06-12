package cn.dougio.ocr;

import org.springframework.stereotype.Component;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;

/**
 * 从剪贴板交互工具类
 */
@Component
public class ClipBoardOperator {

    /**
     *
     * @return
     * @throws Exception
     */
    Image loadImageFromClipboard()
            throws Exception {
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            return (Image) transferable.getTransferData(DataFlavor.imageFlavor);
        } else {
            return null;
        }
    }

    /**
     *
     * @param image
     * @return
     */
    BufferedImage getBufferedImage(Image image) {
        if (image == null) {
            throw new RuntimeException("DATA ERROR: Image NOT found in clipboard");
        }

        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //Graphics2D g2d = bi.createGraphics();
        //g2d.drawImage(image, 0, 0, null);

        Graphics2D bGr = bi.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();

        return bi;
    }

    /**
     *
     * @param s
     */
    public void writeTextToClipboard(String s) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(s);
        clipboard.setContents(transferable, null);
    }
}
