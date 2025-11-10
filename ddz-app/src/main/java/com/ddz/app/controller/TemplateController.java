package com.ddz.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/template") // 控制器级别的路径映射
public class TemplateController {

    /**
     * 访问首页模板
     * URL: http://localhost:8088/ddz-test/template/index
     * 对应模板: classpath:/templates/index.html
     */
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("title", "DDZ测试平台 - 模板方式");
        model.addAttribute("welcome", "欢迎使用Thymeleaf模板引擎");
        model.addAttribute("features", Arrays.asList(
                "动态数据渲染",
                "模板布局复用",
                "条件判断和循环",
                "国际化支持"
        ));
        return "index"; // 返回模板名称，Thymeleaf会自动添加前缀和后缀
    }

    /**
     * 用户资料页面
     * URL: http://localhost:8088/ddz-test/template/user/123
     * 对应模板: classpath:/templates/user/profile.html
     */
    @GetMapping("/user/{id}")
    public String userProfile(@PathVariable String id, Model model) {
        // 模拟用户数据
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("name", "测试用户" + id);
        user.put("email", "user" + id + "@ddz.com");
        user.put("role", "测试员");

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "用户资料 - " + id);

        return "user/profile"; // 返回子目录下的模板
    }

    /**
     * 产品列表页面
     * URL: http://localhost:8088/ddz-test/template/products
     * 对应模板: classpath:/templates/product/list.html
     */
    @GetMapping("/products")
    public String productList(Model model) {
        // 模拟产品数据
        List<Map<String, Object>> products = Arrays.asList(
                createProduct("P001", "DDZ核心框架", "企业级Java开发框架", 999.00),
                createProduct("P002", "DDZ工具集", "常用开发工具集合", 299.00),
                createProduct("P003", "DDZ测试套件", "完整的测试解决方案", 499.00)
        );

        model.addAttribute("products", products);
        model.addAttribute("totalProducts", products.size());

        return "product/list";
    }

    private Map<String, Object> createProduct(String id, String name, String desc, double price) {
        Map<String, Object> product = new HashMap<>();
        product.put("id", id);
        product.put("name", name);
        product.put("description", desc);
        product.put("price", price);
        return product;
    }

}
