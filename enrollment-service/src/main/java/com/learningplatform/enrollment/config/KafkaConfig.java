package com.learningplatform.enrollment.config;

import com.learningplatform.enrollment.kafka.EnrollmentEvent;
import com.learningplatform.enrollment.kafka.LessonEvent;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;

import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    // ==========================================
    // Producer Configuration
    // ==========================================

    @Bean
    ProducerFactory<String, EnrollmentEvent> producerFactory(
            KafkaProperties props) {

        Map<String, Object> configs =
                new HashMap<>(props.buildProducerProperties());

        configs.put(
                org.apache.kafka.clients.producer.ProducerConfig
                        .KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);

        configs.put(
                org.apache.kafka.clients.producer.ProducerConfig
                        .VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    KafkaTemplate<String, EnrollmentEvent> kafkaTemplate(
            ProducerFactory<String, EnrollmentEvent> producerFactory) {

        return new KafkaTemplate<>(producerFactory);
    }

    // ==========================================
    // Consumer Configuration
    // ==========================================

    @Bean
    ConsumerFactory<String, LessonEvent> lessonConsumerFactory(
            KafkaProperties props) {

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
                "enrollment-service");

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
            LessonEvent> lessonKafkaListenerContainerFactory(
            ConsumerFactory<String, LessonEvent> lessonConsumerFactory) {

        ConcurrentKafkaListenerContainerFactory<
                String,
                LessonEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                lessonConsumerFactory);

        return factory;
    }

    // ==========================================
    // Topics
    // ==========================================

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

    @Bean
    NewTopic lessonCreatedTopic() {
        return TopicBuilder
                .name("lesson-created")
                .partitions(3)
                .replicas(1)
                .build();
    }
}