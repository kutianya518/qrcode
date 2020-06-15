package com.unis;

import com.unis.pojo.LogoConfig;
import com.unis.pojo.QrcodeFont;
import com.unis.utils.QrcodeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class QrcodeTest {
    public static final String content = "http://www.4dface.com.cn/snxf20200611";//二维码里面的内容
    public static final String logoPath = "C:/Users/lgf/Desktop/qrcode/logo/xdzx.png";//logo路径

    public static final String qrcodePath = "C:/Users/lgf/Desktop/qrcode/qrcode.jpg";//单纯二维码存放地址
    public static final String logoQrcodePath = "C:/Users/lgf/Desktop/qrcode/logoQrcode.jpg";//有logo二维码存放地址
    public static final String textQrcodePath = "C:/Users/lgf/Desktop/qrcode/室内消防.jpg";//有文字有logo二维码存放地址
    public static void main(String args[]) {
        try {

            QrcodeUtils.drawQrcode(qrcodePath, content, 400, 400);
            QrcodeUtils.addLogoToQRCode(qrcodePath, logoPath, logoQrcodePath, new LogoConfig());

            //二维码文字的位置
            List<QrcodeFont> qrCodeFontList = new ArrayList<QrcodeFont>();

            QrcodeFont font1 = new QrcodeFont();
            //设置字体在画布的位置
            font1.setStartX(55);
            font1.setStartY(390);
            font1.setText("消防系统：室内消防");
            qrCodeFontList.add(font1);

           /* QrcodeFont font2 = new QrcodeFont();
            font2.setStartX(10);
            font2.setStartY(50);
            font2.setText("品牌：苹果");
            qrCodeFontList.add(font2);

            QrcodeFont font3 = new QrcodeFont();
            font3.setStartX(10);
            font3.setStartY(100);
            font3.setText("型号：iPhone XS max");
            qrCodeFontList.add(font3);

            QrcodeFont font4 = new QrcodeFont();
            font4.setStartX(10);
            font4.setStartY(150);
            font4.setText("规格：黑色");
            qrCodeFontList.add(font4);
*/
            QrcodeUtils.pressText(logoQrcodePath, textQrcodePath, 400, 450, qrCodeFontList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
