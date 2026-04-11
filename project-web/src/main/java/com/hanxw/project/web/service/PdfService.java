package com.hanxw.project.web.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;

@Service
public class PdfService {
    // 签名图片 Base64（从外部文件读取，只加载一次）
    private static final String SIGNATURE_BASE64;

    static {
        SIGNATURE_BASE64 = loadSignatureBase64();
    }

    /**
     * 从 resources/signatures/signature.txt 读取 Base64
     */
    private static String loadSignatureBase64() {
        try (InputStream is = PdfService.class.getClassLoader()
                .getResourceAsStream("signatures/signature.txt")) {

            if (is == null) {
                throw new RuntimeException("签名文件未找到！请确认路径：src/main/resources/signatures/signature.txt");
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line.trim());   // 去掉可能的换行
                }
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("加载签名Base64失败", e);
        }
    }

    public byte[] generateVerificationReport(Object data) throws Exception {
        String htmlContent = buildHtmlWithData(data);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder()
                    .withHtmlContent(htmlContent, null)
                    .toStream(outputStream);

            // ==================== 正确加载中文字体（推荐写法） ====================
            URL fontUrl = getClass().getClassLoader().getResource("fonts/Alibaba-PuHuiTi-Regular.ttf");
            if (fontUrl == null) {
                throw new RuntimeException("字体文件未找到！请确认 Alibaba-PuHuiTi-Regular.ttf 已放在 src/main/resources/fonts/ 目录下");
            }

            // 把 classpath 中的字体复制到临时文件（解决 jar 包和 target/classes 路径问题）
            File tempFontFile = File.createTempFile("Alibaba-PuHuiTi-Regular-", ".ttf");
            tempFontFile.deleteOnExit();  // JVM 退出时自动删除临时文件

            try (InputStream is = fontUrl.openStream();
                 FileOutputStream fos = new FileOutputStream(tempFontFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            // 关键：字体名称必须和 HTML CSS 中的 font-family 完全一致
            builder.useFont(tempFontFile, "Alibaba PuHuiTi");
            // =================================================================

            builder.run();
            return outputStream.toByteArray();
        }
    }

    private String buildHtmlWithData(Object data) {
        // 这里可以先定义上面的 HTML 字符串模板，然后用 data 替换占位符
        // 推荐方式：把上面 HTML 做成 resources/templates/verification-report.html
        // 然后用 Thymeleaf 渲染
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <title>抵押房产下户核验报告</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Alibaba PuHuiTi', sans-serif;\n" +
                "            font-size: 12px;\n" +
                "            margin: 25px;\n" +
                "            line-height: 1.55;\n" +
                "        }\n" +
                "        .title {\n" +
                "            text-align: center;\n" +
                "            font-size: 18px;\n" +
                "            font-weight: bold;\n" +
                "            margin: 15px 0 25px 0;\n" +
                "        }\n" +
                "        table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "            table-layout: fixed;\n" +
                "        }\n" +
                "        td {\n" +
                "            border: 1px solid #000;\n" +
                "            padding: 7px 6px;\n" +
                "            vertical-align: middle;\n" +
                "            word-break: break-all;\n" +
                "        }\n" +
                "        .center { text-align: center; }\n" +
                "        .left { text-align: left; }\n" +
                "        .red { color: red; }\n" +
                "        .section { \n" +
                "            background-color: #f0f0f0; \n" +
                "            font-weight: bold; \n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\t\t.signature { height: 65px; max-width: 200px; vertical-align: middle; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"title\">\n" +
                "        上海农商银行“个人经营快贷”业务<br/>\n" +
                "        抵押房产下户核验报告\n" +
                "    </div>\n" +
                "\n" +
                "    <table>\n" +
                "        <!-- 抵押物基本信息 -->\n" +
                "        <tr>\n" +
                "            <td colspan=\"5\" class=\"center section\">抵押物基本信息</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td class=\"center\">下户核验日期</td>\n" +
                "            <td class=\"center red\">[下户当天日期]</td>\n" +
                "            <td class=\"center\">抵押物类型</td>\n" +
                "            <td colspan=\"2\" class=\"center\">\n" +
                "                住宅/商铺/厂房/办公楼<br/>\n" +
                "                <span class=\"red\">根据产权证勾选</span>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td class=\"center\">抵押房产地址</td>\n" +
                "            <td colspan=\"4\" class=\"center red\">[产证上地址]</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td class=\"center\">小区名称</td>\n" +
                "            <td class=\"center red\">[填写小区名称]</td>\n" +
                "            <td class=\"center\">是否有电梯</td>\n" +
                "            <td colspan=\"2\" class=\"center\">\n" +
                "                □ 是　□ 否<br/>\n" +
                "                <span class=\"red\">根据实际情况勾选</span>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td class=\"center\">抵押房产所在楼层</td>\n" +
                "            <td class=\"center red\">[根据产权证上填写]</td>\n" +
                "            <td class=\"center\">房屋总楼层</td>\n" +
                "            <td colspan=\"2\" class=\"center red\">[根据产权证上填写]</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td class=\"center\">抵押房产状态</td>\n" +
                "            <td colspan=\"2\" class=\"center\">\n" +
                "                空置/自用/出租/其他请说明<br/>\n" +
                "                <span class=\"red\">根据实际情况勾选</span>\n" +
                "            </td>\n" +
                "            <td class=\"center\">备注：</td>\n" +
                "            <td class=\"center\">\n" +
                "                含有地下/花园/阁楼面积 XX 平方米<br/>\n" +
                "                <span class=\"red\">产证上有标明额外面积的需填写</span>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "\n" +
                "        <!-- 抵押物下户核验情况 -->\n" +
                "        <tr>\n" +
                "            <td colspan=\"5\" class=\"center section\">抵押物下户核验情况</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td colspan=\"4\" class=\"left\">\n" +
                "                1、是否已上门对抵押房产进行实地调查？\n" +
                "            </td>\n" +
                "            <td class=\"center\">□ 是　□ 否</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td colspan=\"4\" class=\"left\">\n" +
                "                2、抵押房产门头地址是否与抵押物产证地址一致？\n" +
                "            </td>\n" +
                "            <td class=\"center\">□ 是　□ 否</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td colspan=\"4\" class=\"left\">\n" +
                "                3、抵押房产是否已出租？<br/>\n" +
                "                如抵押物已出租，该抵押物的租户为：\n" +
                "            </td>\n" +
                "            <td class=\"center\">□ 是　□ 否</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td colspan=\"4\" class=\"left\">\n" +
                "                4、抵押房产是否存在重大结构变动？<br/>\n" +
                "                如存在重大结构变动，请简要描述：\n" +
                "            </td>\n" +
                "            <td class=\"center\">□ 是　□ 否</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td colspan=\"4\" class=\"left\">\n" +
                "                5、抵押房产处置时是否存在无法腾空的风险？<br/>\n" +
                "                如存在无法腾空的风险，请简要描述：\n" +
                "            </td>\n" +
                "            <td class=\"center\">□ 是　□ 否</td>\n" +
                "        </tr>\n" +
                "\n" +
                "        <!-- 结论部分 -->\n" +
                "        <tr>\n" +
                "            <td colspan=\"5\" class=\"center section\">下户核验意见及结论</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td colspan=\"5\" style=\"height:85px;\" class=\"left\">\n" +
                "                下户核验人员意见：<br/><br/>\n" +
                "                抵押物下户核验情况 \n" +
                "                <span class=\"red\">□ 符合　□ 不符合</span> \n" +
                "                个人经营快贷抵押物准入要求\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td colspan=\"2\">下户核验人员姓名：</td>\n" +
                "            <td colspan=\"3\" class=\"center\">\n" +
                "                <img src=\"data:image/png;base64,[SIGNATURE_BASE64]\" class=\"signature\" alt=\"签名\"/>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td colspan=\"5\" class=\"center\">\n" +
                "                本人承诺以上调查及填写信息真实有效。\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>"; // 粘贴上面的 HTML
        // 用 String.replace 或 MessageFormat 替换 [下户核验日期] 等占位符
        return html.replace("[SIGNATURE_BASE64]", SIGNATURE_BASE64);
    }
}
