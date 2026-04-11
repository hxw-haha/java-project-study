package com.hanxw.project.web.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;

@Service
public class PdfService {
    // 签名图片 Base64（从外部文件读取，只加载一次）
    private static final String SIGNATURE_BASE64;
    private static final String PDF_HTML_STRING;

    static {
        SIGNATURE_BASE64 = loadSignatureBase64();
        PDF_HTML_STRING = loadPdfHtmlString();
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

    /**
     * 从 resources/file/抵押房产下户核验报告.html 获取pdf string
     */
    private static String loadPdfHtmlString() {
        try (InputStream is = PdfService.class.getClassLoader()
                .getResourceAsStream("file/抵押房产下户核验报告.html")) {

            if (is == null) {
                throw new RuntimeException("pdf文件未找到！请确认路径：src/main/resources/file/抵押房产下户核验报告.html");
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
            throw new RuntimeException("加载pdf文件失败", e);
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
        return PDF_HTML_STRING.replace("[SIGNATURE_BASE64]", SIGNATURE_BASE64);
    }
}
