package gov.hhs.onc.dcdt.mail.sender.impl;

import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.config.MailGatewayConfig;
import gov.hhs.onc.dcdt.mail.config.MailGatewayCredentialConfig;
import gov.hhs.onc.dcdt.mail.impl.MailInfoImpl;
import gov.hhs.onc.dcdt.mail.sender.MailTemplateSenderService;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.velocity.utils.ToolVelocityUtils;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;

public abstract class AbstractMailTemplateSenderService extends AbstractMailSenderService implements MailTemplateSenderService {
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    protected class TemplateMailPreparator implements MailPreparator {
        private ModelMap subjModel;
        private ModelMap textModel;

        public TemplateMailPreparator(@Nullable ModelMap subjModel, @Nullable ModelMap textModel) {
            this.subjModel = subjModel;
            this.textModel = textModel;
        }

        @Override
        public MailInfo prepareMail(MailInfo mailInfo) throws Exception {
            mailInfo.setOrigDate(new Date());

            String charsetName = mailInfo.getEncoding().getCharsetName();

            mailInfo.setFrom(AbstractMailTemplateSenderService.this.fromConfig.getMailAddress());

            if (AbstractMailTemplateSenderService.this.hasReplyToConfig()) {
                mailInfo.setReplyTo(AbstractMailTemplateSenderService.this.replyToConfig.getMailAddress());
            }

            mailInfo.setSubject(this.processTemplate(charsetName, AbstractMailTemplateSenderService.this.subjTemplateLoc, this.subjModel));
            mailInfo.setText(this.processTemplate(charsetName, AbstractMailTemplateSenderService.this.textTemplateLoc, this.textModel));

            return mailInfo;
        }

        private String processTemplate(String encName, String templateLoc, @Nullable ModelMap modelMap) {
            return StringUtils.trim(VelocityEngineUtils.mergeTemplateIntoString(AbstractMailTemplateSenderService.this.velocityEngine,
                StringUtils.appendIfMissing(templateLoc, ToolVelocityUtils.FILE_EXT_VM), encName, modelMap));
        }
    }

    protected VelocityEngine velocityEngine;
    protected InstanceMailAddressConfig fromConfig;
    protected String subjTemplateLoc;
    protected String textTemplateLoc;
    protected InstanceMailAddressConfig replyToConfig;

    protected void send(MailAddress toAddr, @Nullable ModelMap subjModel, @Nullable ModelMap textModel, List<MailPreparator> mailPreps)
        throws MessagingException {
        super.send(new MailInfoImpl(this.session, this.enc), this.fromConfig.getMailAddress(), toAddr, this.fromConfig.getGatewayConfig().getHeloName()
            .toString(true), ToolCollectionUtils.add(mailPreps, new TemplateMailPreparator(subjModel, textModel)));
    }

    @Nullable
    @Override
    protected ToolSmtpTransport buildTransport(MailInfo mailInfo, MailAddress fromAddr, MailAddress toAddr, String heloName) throws MessagingException {
        MailGatewayConfig gatewayConfig = this.fromConfig.getGatewayConfig();
        MailGatewayCredentialConfig gatewayCredConfig = fromConfig.getGatewayCredentialConfig();

        // noinspection ConstantConditions
        return this.buildTransport(mailInfo, gatewayConfig.getTransportProtocol(), gatewayConfig.getHost(true).getHostAddress(), gatewayConfig.getPort(),
            gatewayCredConfig.getId().toAddress(), gatewayCredConfig.getSecret(), fromAddr, toAddr, heloName);
    }

    @Override
    public InstanceMailAddressConfig getFromConfig() {
        return this.fromConfig;
    }

    @Override
    public void setFromConfig(InstanceMailAddressConfig fromConfig) {
        this.fromConfig = fromConfig;
    }

    @Override
    public boolean hasReplyToConfig() {
        return (this.replyToConfig != null);
    }

    @Nullable
    @Override
    public InstanceMailAddressConfig getReplyToConfig() {
        return this.replyToConfig;
    }

    @Override
    public void setReplyToConfig(@Nullable InstanceMailAddressConfig replyToConfig) {
        this.replyToConfig = replyToConfig;
    }

    @Override
    public String getSubjectTemplateLocation() {
        return this.subjTemplateLoc;
    }

    @Override
    public void setSubjectTemplateLocation(String subjTemplateLoc) {
        this.subjTemplateLoc = subjTemplateLoc;
    }

    @Override
    public String getTextTemplateLocation() {
        return this.textTemplateLoc;
    }

    @Override
    public void setTextTemplateLocation(String textTemplateLoc) {
        this.textTemplateLoc = textTemplateLoc;
    }

    @Override
    public VelocityEngine getVelocityEngine() {
        return this.velocityEngine;
    }

    @Override
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
}
