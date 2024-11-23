import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { KafkaConsumerModule } from './kafka-consumer/kafka-consumer.module';

@Module({
  imports: [KafkaConsumerModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
