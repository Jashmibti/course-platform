package com.learningplatform.reporting.config;

import com.learningplatform.reporting.kafka.CourseEvent;
import com.learningplatform.reporting.kafka.LessonEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import com.learningplatform.reporting.kafka.EnrollmentEvent;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    ConsumerFactory<String, LessonEvent>
    lessonConsumerFactory(KafkaProperties props) {

        Map<String, Object> configs =
                new HashMap<>(props.buildConsumerProperties());

        configs.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);

        configs.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);

        configs.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "reporting-service");

        JsonDeserializer<LessonEvent> deserializer =
                new JsonDeserializer<>(LessonEvent.class);

        deserializer.addTrustedPackages("*");
        deserializer.ignoreTypeHeaders();

        return new DefaultKafkaConsumerFactory<>(
                configs,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<
            String,
            LessonEvent>
    lessonKafkaListenerContainerFactory(
            ConsumerFactory<String, LessonEvent>
                    consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<
                String,
                LessonEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                consumerFactory);

        return factory;
    }

    @Bean
    NewTopic lessonCreatedTopic() {
        return TopicBuilder
                .name("lesson-created")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
        ConsumerFactory<String, CourseEvent>
        courseConsumerFactory(KafkaProperties props) {

        Map<String, Object> configs =
                new HashMap<>(props.buildConsumerProperties());

        configs.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);

        configs.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);

        configs.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "reporting-service");

        JsonDeserializer<CourseEvent> deserializer =
                new JsonDeserializer<>(CourseEvent.class);

        deserializer.addTrustedPackages("*");
        deserializer.ignoreTypeHeaders();

        return new DefaultKafkaConsumerFactory<>(
                configs,
                new StringDeserializer(),
                deserializer
        );
        }

        @Bean
public ConcurrentKafkaListenerContainerFactory<
        String,
        CourseEvent>
courseKafkaListenerContainerFactory(
        ConsumerFactory<String, CourseEvent>
                consumerFactory) {

    ConcurrentKafkaListenerContainerFactory<
            String,
            CourseEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();

    factory.setConsumerFactory(
            consumerFactory);

    return factory;
}

@Bean
ConsumerFactory<String, EnrollmentEvent>
enrollmentConsumerFactory(KafkaProperties props) {

    Map<String, Object> configs =
            new HashMap<>(props.buildConsumerProperties());

    configs.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class);

    configs.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            JsonDeserializer.class);

    configs.put(
            ConsumerConfig.GROUP_ID_CONFIG,
            "reporting-service");

    JsonDeserializer<EnrollmentEvent> deserializer =
            new JsonDeserializer<>(EnrollmentEvent.class);

    deserializer.addTrustedPackages("*");
    deserializer.ignoreTypeHeaders();

    return new DefaultKafkaConsumerFactory<>(
            configs,
            new StringDeserializer(),
            deserializer
    );
}
@Bean
public ConcurrentKafkaListenerContainerFactory<
        String,
        EnrollmentEvent>
enrollmentKafkaListenerContainerFactory(
        ConsumerFactory<String, EnrollmentEvent>
                enrollmentConsumerFactory) {

    ConcurrentKafkaListenerContainerFactory<
            String,
            EnrollmentEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();

    factory.setConsumerFactory(
            enrollmentConsumerFactory);

    return factory;
}
@Bean
NewTopic courseCreatedTopic() {
    return TopicBuilder
            .name("course-created")
            .partitions(3)
            .replicas(1)
            .build();
}

@Bean
NewTopic courseEnrolledTopic() {
    return TopicBuilder
            .name("course-enrolled")
            .partitions(3)
            .replicas(1)
            .build();
}

@Bean
NewTopic lessonCompletedTopic() {
    return TopicBuilder
            .name("lesson-completed")
            .partitions(3)
            .replicas(1)
            .build();
}

@Bean
NewTopic courseCompletedTopic() {
    return TopicBuilder
            .name("course-completed")
            .partitions(3)
            .replicas(1)
            .build();
}
}