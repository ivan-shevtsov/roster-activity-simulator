package roster.activity.simulator.generation;

import com.github.javafaker.Faker;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import roster.activity.simulator.bridge.AsyncMessagesProcessor;
import roster.activity.simulator.bridge.ConsoleBridge;
import roster.activity.simulator.bridge.domain.model.HttpBridge;
import roster.roster.domain.generator.SchoolFactory;
import roster.roster.domain.generator.SectionFactory;
import roster.roster.domain.generator.StudentFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class GenerationConfiguration implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Bean
    public Faker faker() {
        return new Faker();
    }

    @Bean
    public SchoolFactory schoolFactory(Faker faker) {
        return new SchoolFactory(faker);
    }

    @Bean
    public SchoolStrategy schoolStrategy(SchoolFactory schoolFactory) {
        return new SchoolStrategy(schoolFactory, applicationEventPublisher);
    }

    @Bean
    public StudentFactory studentFactory(Faker faker) {
        return new StudentFactory(faker);
    }

    @Bean
    public StudentStrategy studentStrategy(StudentFactory studentFactory) {
        return new StudentStrategy(studentFactory, applicationEventPublisher);
    }

    @Bean
    public SectionFactory sectionFactory(Faker faker) {
        return new SectionFactory(faker);
    }

    @Bean
    public SectionStrategy sectionStrategy(SectionFactory sectionFactory) {
        return new SectionStrategy(sectionFactory, applicationEventPublisher);
    }

    @Bean
    public ExecutorService poolForActivitiesGeneration() {
        ThreadPoolExecutorFactoryBean threadPoolExecutorFactoryBean = new ThreadPoolExecutorFactoryBean();
        threadPoolExecutorFactoryBean.setAllowCoreThreadTimeOut(false);
        threadPoolExecutorFactoryBean.setCorePoolSize(3);
        threadPoolExecutorFactoryBean.setMaxPoolSize(3);
        threadPoolExecutorFactoryBean.afterPropertiesSet();
        return threadPoolExecutorFactoryBean.getObject();
    }

    @Bean
    public StudentActivityStrategy studentActivityStrategy() {
        return new StudentActivityStrategy((ThreadPoolExecutor) poolForActivitiesGeneration(), applicationEventPublisher);
    }

    @Bean
    public ConsoleBridge csvBridge() {
        return new ConsoleBridge();
    }

    @Bean
    public HttpBridge httpBridge() {
        return new HttpBridge();
    }

    @Bean
    public AsyncMessagesProcessor activityMessagesProcessor() {
        ThreadPoolExecutorFactoryBean threadPoolExecutorFactoryBean = new ThreadPoolExecutorFactoryBean();
        threadPoolExecutorFactoryBean.setAllowCoreThreadTimeOut(false);
        threadPoolExecutorFactoryBean.setCorePoolSize(100);
        threadPoolExecutorFactoryBean.setMaxPoolSize(100);
        threadPoolExecutorFactoryBean.afterPropertiesSet();
        threadPoolExecutorFactoryBean.setQueueCapacity(100_000);

        return new AsyncMessagesProcessor(httpBridge(), threadPoolExecutorFactoryBean.getObject());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
