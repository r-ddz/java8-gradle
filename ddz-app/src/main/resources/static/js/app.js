// DDZ测试平台通用JS
$(document).ready(function() {
    console.log('DDZ测试平台加载完成');

    // 全局错误处理
    $(document).ajaxError(function(event, jqxhr, settings, thrownError) {
        console.error('AJAX请求失败:', settings.url, thrownError);
    });
});