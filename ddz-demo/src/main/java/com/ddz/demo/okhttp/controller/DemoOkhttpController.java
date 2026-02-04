package com.ddz.demo.okhttp.controller;

import com.ddz.demo.okhttp.domain.UserDemoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/demo/okhttp")
public class DemoOkhttpController {

    /**
     * get 请求：简单请求
     */
    @GetMapping("/get/easy")
    public String getEasy(@RequestParam Long id,
                       String code) {
        return "【getEasy】 => id : " + id + "; code : " + code;
    }

    /**
     * get 请求：入参的不同表现形式
     */
    @GetMapping("/get/params") //
    public String getParams(
            @RequestParam String code,
            @RequestParam Map<String, String> params) {
        return "【getParams】 => code : " +  code +  "; params : " + params.toString();
    }

    /**
     * get 请求：入参的不同表现形式，更详细
     */
    @GetMapping("/get/full/{test}")
    public String getFull(@RequestParam("id") Long id,
                       @RequestParam("name") String code, // 参数名不一致，接收的是 name
                       @PathVariable String test,
                       @RequestHeader Map<String, String> headers,
                       HttpServletRequest request) {
        return "【getFull】 => id : " + id + "; code : " + code + "; test : " + test;
    }

    /**
     * post请求：测试JSON格式的POST请求
     */
    @PostMapping("/post/json")
    public String postJson(@RequestBody List<UserDemoDTO> dto, @RequestHeader(required = false) Map<String, String> headers) {
        return "【postJson】 => dto : " + dto.toString();
    }

    /**
     * post请求：测试JSON格式的POST请求（异步）
     */
    @PostMapping("/post/json/async")
    public String postJsonAsync(@RequestBody UserDemoDTO dto) {
        return "【postJsonAsync】 => dto : " + dto.toString();
    }

    /**
     * post请求：测试XML格式的POST请求
     */
    @PostMapping("/post/xml")
    public String postXml(@RequestBody UserDemoDTO dto) {
        return "【postXml】 => dto : " + dto.toString();
    }

    /**
     * post请求：测试表单格式的POST请求
     */
    @PostMapping("/post/form")
    public String postForm(@RequestParam Map<String, String> formData) {
        return "【postForm】 : " + formData.toString();
    }

    /**
     * post请求：测试文本格式的POST请求
     */
    @PostMapping("/post/text")
    public String postText(@RequestBody String text) {
        return "【postText】 : " + text;
    }


    /**
     * put请求：测试json格式的PUT请求
     */
    @PostMapping("/put/json")
    public String putJson(@RequestBody UserDemoDTO dto) {
        return "【putJson】 : " + dto.toString();
    }

    /**
     * delete请求：测试json格式的delete请求
     */
    @PostMapping("/delete/json")
    public String deleteJson(@RequestBody UserDemoDTO dto) {
        return "【deleteJson】 : " + dto.toString();
    }

    /**
     * 单文件上传测试
     */
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                                   @RequestParam(required = false) Map<String, String> formData,
                                   @RequestHeader(required = false) Map<String, String> headers
    ) {
        String str = "【uploadFile】 ===================================="; // 返回结果展示
        try {
            // 非空检查
            if (file.isEmpty()) {
                return "【uploadFile】 : " + "文件为空";
            }
            // 文件大小
            long fileSize = file.getSize();
            str += "\n\t文件大小：" + fileSize + "字节";
            // 原始文件名 【测试文件1.pdf】
            String originalFilename = file.getOriginalFilename();
            str += "\n\t原始文件名：" + originalFilename;
            // 移除路径信息，只保留文件名【测试文件1.pdf】
            String filename = Paths.get(originalFilename).getFileName().toString();
            str += "\n\t文件名：" + filename;
            // 文件类型【application/pdf】
            String fileType = file.getContentType();
            str += "\n\t文件类型：" + fileType;
            // 新文件路径【C:\Users\74066\Desktop\BBBBBB\】
            String newFilePath = formData.get("newFilePath");
            str += "\n\t新文件路径：" + newFilePath;
            // 保存到新文件夹
            file.transferTo(new File(newFilePath + "测试文件1-单文件上传测试-" + System.currentTimeMillis() + ".pdf"));
            return str + "\n【uploadFile】上传完成 ================================";
        } catch (Exception e) {
            e.printStackTrace();
            return str + "\n\t异常：================================";
        }
    }





}
