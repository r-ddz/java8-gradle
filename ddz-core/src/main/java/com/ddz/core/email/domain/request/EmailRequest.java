package com.ddz.core.email.domain.request;

import cn.hutool.core.collection.CollUtil;
import com.ddz.core.email.domain.attachment.EmailAttachment;
import lombok.Data;

import java.util.List;

@Data
public class EmailRequest {
    /** 收件人 */
    private List<String> to;
    /** 抄送人 */
    private List<String> cc;
    /** 密送人 */
    private List<String> bcc;
    /** 主题 */
    private String subject;
    /** 内容 */
    private String content;
    /** 支持HTML */
    private boolean html = true;
    /** 附件 */
    private List<EmailAttachment> attachments;

    public void tryDeleteFile() {
        if (CollUtil.isNotEmpty(attachments)) {
            for (EmailAttachment attachment : attachments) {
                boolean shouldDelete = attachment.isBigFile()
                        && attachment.isAutoDeleteFile()
                        && attachment.getFile() != null
                        && attachment.getFile().exists();

                if (shouldDelete) {
                    try {
                        attachment.getFile().delete();
                    } catch (Exception e) {
                        // 记录删除失败的日志，但不中断其他文件的删除操作
                        // 可以根据实际需求使用具体的日志框架
                        // 捕获其他可能的异常
                    }
                }
            }
        }
    }




}
