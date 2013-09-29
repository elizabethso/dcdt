package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.dns.conf.DnsServerConfig;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class DnsServer implements InitializingBean, Runnable {
    @Autowired
    private ThreadPoolTaskExecutor dnsSocketServerTaskExecutor;

    @Autowired
    private List<DnsSocketServer<?, ?, ?>> dnsSocketServers;
    
    private DnsServerConfig dnsServerConfig;

    public DnsServer() {
    }
    
    public DnsServer(DnsServerConfig dnsServerConfig) {
        this.dnsServerConfig = dnsServerConfig;
    }

    @Override
    public void run() {
        this.start();
    }

    public void start() {
        for (DnsSocketServer<?, ?, ?> dnsSocketServer : this.dnsSocketServers)
        {
            this.dnsSocketServerTaskExecutor.execute(dnsSocketServer);
        }
    }

    public void stop() {
        for (DnsSocketServer<?, ?, ?> dnsSocketServer : this.dnsSocketServers) {
            dnsSocketServer.stop();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        for (DnsSocketServer<?, ?, ?> dnsSocketServer : this.dnsSocketServers)
        {
            dnsSocketServer.setDnsServerConfig(this.dnsServerConfig);
        }
    }
    
    @Bean(name = "dnsUdpSocketServer", autowire = Autowire.BY_TYPE)
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    protected DnsUdpSocketServer getDnsUdpSocketServer()
    {
        return new DnsUdpSocketServer();
    }
    
    @Bean(name = "dnsTcpSocketServer", autowire = Autowire.BY_TYPE)
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    protected DnsTcpSocketServer getDnsTcpSocketServer()
    {
        return new DnsTcpSocketServer();
    }
    
    public void setDnsServerConfig(DnsServerConfig dnsServerConfig)
    {
        this.dnsServerConfig = dnsServerConfig;
    }
}