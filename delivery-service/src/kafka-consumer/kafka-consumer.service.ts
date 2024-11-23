import { Injectable, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { Kafka, Consumer, EachMessagePayload } from 'kafkajs';
import { kafkaConfig } from './kafka-config';

@Injectable()
export class KafkaConsumerService implements OnModuleInit, OnModuleDestroy {
    private kafka: Kafka;
    private consumer: Consumer;

    constructor() {
        this.kafka = new Kafka({
            brokers: kafkaConfig.brokers,
        });

        this.consumer = this.kafka.consumer({ groupId: kafkaConfig.groupId });
    }

    async onModuleInit() {
        console.log('Connecting to Kafka...');
        await this.consumer.connect();

        // Subscribe to the topic
        await this.consumer.subscribe({ topic: kafkaConfig.topic, fromBeginning: true });

        // Run the consumer
        console.log(`Subscribed to topic: ${kafkaConfig.topic}`);
        await this.consumer.run({
            eachMessage: async (payload: EachMessagePayload) => {
                const { topic, partition, message } = payload;
                console.log(`Message received on topic "${topic}" | Partition: ${partition}`);
                console.log(`Message: ${message.value?.toString()}`);
                // Add message processing logic here
            },
        });
    }

    async onModuleDestroy() {
        console.log('Disconnecting from Kafka...');
        await this.consumer.disconnect();
    }
}
