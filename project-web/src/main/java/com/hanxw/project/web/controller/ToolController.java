package com.hanxw.project.web.controller;

import com.hanxw.project.web.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/tool")
public class ToolController {

    @Autowired
    private PdfService pdfService;

    /**
     * 查询用户及其订单（原有接口）
     */
    @GetMapping("/generateVerificationReport")
    public String generateVerificationReport(
            HttpServletResponse response) throws Exception {
        // 1. 生成 PDF 字节
        byte[] pdfBytes = pdfService.generateVerificationReport("");

        // 2. 保存为临时文件（推荐放到临时目录）
        String projectRoot = System.getProperty("user.dir");   // 当前项目根目录
        File tempDir = new File(projectRoot, "temp/pdf");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        // 生成唯一文件名（防止并发冲突）
        String fileName = "verification_report_" + System.currentTimeMillis() + ".pdf";
        File tempFile = new File(tempDir, fileName);

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(pdfBytes);
        }

        // 3. 返回可访问的 URL（根据你的项目实际访问方式调整）
        // 方式一：如果有 nginx 静态资源代理（推荐）
        return "/temp/pdf/" + fileName;
    }
}
