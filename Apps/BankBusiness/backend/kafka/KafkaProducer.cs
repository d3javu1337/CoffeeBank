using System.Text.Json;
using Confluent.Kafka;

namespace backend.kafka;

public class KafkaProducer
{
    private readonly IProducer<Null, object> _kafkaProducer;

    public KafkaProducer(string bootstrapServers)
    {
        var conf = new ProducerConfig()
        {
            BootstrapServers = bootstrapServers
        };

        _kafkaProducer = new ProducerBuilder<Null, object>(conf)
            .SetValueSerializer(new KafkaJsonSerializer())
            .Build();
    }

    public async Task produce(string topic, object message)
    {
        await _kafkaProducer.ProduceAsync(
            topic, new Message<Null, object>
            {
                Value = message
            }
        );
    }
}

public class KafkaJsonSerializer : ISerializer<object>
{
    public byte[] Serialize(object data, SerializationContext context)
    {
        return JsonSerializer.SerializeToUtf8Bytes(data);
    }
}

public static class KafkaExtensions
{
    public static void AddProducer(this IServiceCollection services, IConfigurationSection configurationSection)
    {
        services.Configure<KafkaSettings>(configurationSection);
        services.AddSingleton<KafkaProducer>();
    }
}