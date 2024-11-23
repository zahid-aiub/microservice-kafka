export const kafkaConfig = {
    clientId: 'order-delivery-consumer',
    brokers: ['localhost:9092'],
    groupId: 'order-delivery-group',
    topic: 'order_topic',
};
