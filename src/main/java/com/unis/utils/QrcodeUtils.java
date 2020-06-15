package com.unis.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.unis.pojo.LogoConfig;
import com.unis.pojo.QrcodeFont;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lgf
 * @create 2020/6/15
 * @desc
 */

public class QrcodeUtils {

    /**
     * 画二维码
     *
     * @param qrcodePath 存放二维码路径
     * @param content    二维码内容
     * @param height     二维码高度
     * @param width      二维码宽度
     */
    public static void drawQrcode(String qrcodePath, String content, int height, int width) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            //设置UTF-8， 防止中文乱码
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            //设置二维码四周白色区域的大小
            hints.put(EncodeHintType.MARGIN, 1);
            //设置二维码的容错性
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //画二维码，记得调用multiFormatWriter.encode()时最后要带上hints参数，不然上面设置无效
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            //qrcFile用来存放生成的二维码图片（无logo，无文字）
            File qrcodeFile = QrcodeUtils.findFile(qrcodePath);
            //开始画二维码
            MatrixToImageWriter.writeToFile(bitMatrix, "jpg", qrcodeFile);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给二维码图片添加Logo
     *
     * @param qrCodePath 二维码路径
     * @param logoPath   logo路径
     * @param logoConfig logo 配置文件
     */
    public static void addLogoToQRCode(String qrCodePath, String logoPath, String logoQrcodePath, LogoConfig logoConfig) {
        try {
            File qrCodeFile = new File(qrCodePath);
            File logoFile = new File(logoPath);

            if (!qrCodeFile.isFile() || !logoFile.isFile()) {
                System.out.print("file not find origial file !");
                System.exit(0);
            }

            //读取二维码图片，并构建绘图对象
            BufferedImage image = ImageIO.read(qrCodeFile);
            Graphics2D g = image.createGraphics();
            //读取logo文件
            BufferedImage logo = ImageIO.read(logoFile);
            //设置logo的位置
            int widthLogo = image.getWidth() / logoConfig.getLogoPart();
            int heightLogo = image.getWidth() / logoConfig.getLogoPart(); // 保持二维码是正方形的
            // 计算图片放置位置（居中）
            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - heightLogo) / 2;

            // 开始绘制图片
            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
            g.drawRoundRect(x, y, widthLogo, heightLogo, 10, 10);
            g.setStroke(new BasicStroke(logoConfig.getBorder()));
            g.setColor(logoConfig.getBorderColor());
            g.drawRect(x, y, widthLogo, heightLogo);
            g.dispose();
            ImageIO.write(image, "jpeg", new File(logoQrcodePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param qrcodePath     二维码路径
     * @param textQrcodePath 存放文字二维码路径
     * @param width          文字二维码宽度
     * @param height         文字二维码高度
     * @param qrcodeFontList 文字内容集合
     * @给二维码添加文字
     */
    public static void pressText(String qrcodePath, String textQrcodePath, int width, int height, List<QrcodeFont> qrcodeFontList) {

        try {
            File textQrcodeFile = findFile(qrcodePath);
            System.out.println(textQrcodeFile.length());
            Image src = ImageIO.read(textQrcodeFile);
            int imageW = src.getWidth(null);
            int imageH = src.getHeight(null);
            BufferedImage image = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.setColor(Color.WHITE);//设置笔刷白色
            g.fillRect(0, 0, 600, 600);//填充整个屏幕
            g.drawLine(0, 0, imageW, imageH);
            //x,y调整二维码的位置，imageW,imageH调整二维码图片的宽高
            g.drawImage(src, 25, 20, imageW-50, imageH-50, null);
            for (QrcodeFont qrcodeFont : qrcodeFontList) {
                g.setColor(qrcodeFont.getColor());
                g.setFont(new Font("粗体", qrcodeFont.getFontStyle(), qrcodeFont.getFontSize()));
                g.drawString(qrcodeFont.getText(), qrcodeFont.getStartX(), qrcodeFont.getStartY());
            }
            g.dispose();

            FileOutputStream out = new FileOutputStream(textQrcodePath);
            ImageIO.write(image, "JPEG", out);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
            System.out.println("image press success");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * @param filepath
     * @return
     * @查找文件（没有则创建文件）
     */
    public static File findFile(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }


}