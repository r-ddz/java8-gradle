package com.ddz.core.email.domain.attachment;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.io.File;

@Data
public class EmailAttachment {

    /** 文件名 */
    private String fileName;
    /** 小文件 */
    private byte[] bytes;
    /** 是否是大文件 */
    private boolean bigFile;
    /** 大文件 */
    private File file;
    /** 大文件是否自动清理 */
    private boolean autoDeleteFile;

    public EmailAttachment(String fileName, byte[] bytes) {
        this.fileName = StrUtil.isNotBlank(fileName) ? fileName : "未命名文件";
        this.bytes = bytes;
        this.bigFile = false;
    }

    public EmailAttachment(String fileName, File file, boolean autoDeleteFile) {
        this.fileName = StrUtil.isNotBlank(fileName) ? fileName : "未命名文件";
        this.file = file;
        this.bigFile = true;
        this.autoDeleteFile = autoDeleteFile;
    }

}
