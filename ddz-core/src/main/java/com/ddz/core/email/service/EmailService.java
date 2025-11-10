package com.ddz.core.email.service;

import com.ddz.core.email.domain.Response.EmailResponse;
import com.ddz.core.email.domain.request.EmailRequest;

public interface EmailService {

    EmailResponse sendEmail(EmailRequest request);

}
