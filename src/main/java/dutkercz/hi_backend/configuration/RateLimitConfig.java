package dutkercz.hi_backend.configuration;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ClientSideConfig;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Value(value = "${spring.data.redis.host}")
    private String REDIS_HOST;
    @Value(value = "${spring.data.redis.port}")
    private Integer REDIS_PORT;

///Algumas definições:
    ///Bucket: representa o limite de requisições de determinado usuário ou IP. cada email vai ter o seu
    ///
    /// ProxyManager: gerencia os buckets e permite recuperá-los pelo identificador.
    ///
    /// Redis: armazena o estado dos buckets para que ele seja compartilhado entre instâncias.

    @Bean
/// ProxyManager<String>: responsável por gerenciar os buckets, utilizando uma chave do tipo String para identificar
    public ProxyManager<String> lettuceProxyManager(){
        //Aqui estamos criando um cliente Redis utilizando a biblioteca Lettuce.
       RedisClient redisClient = RedisClient.create("redis://" + REDIS_HOST + ":" + REDIS_PORT);

//Criando a conexão: estamos estabelecendo uma conexão com o Redis e definindo como os dados serão serializados
// chave=string / valor=array de bytes
//Essas informações são armazenadas em formato binário. por isso StringCodec.UTF8 Para converter as chaves em strings
// E ByteArrayCodec.INSTANCE para trabalhar com valores binários.
       StatefulRedisConnection<String, byte[]> connection =  redisClient.connect(
               RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

/// Aqui começamos a construir o gerenciador de buckets.
/// O getDefault() recupera as configurações padrão.
/// .withExpirationAfterWriteStrategy determina a estratégia de expiração dos dados armazenados no Redis.
       return LettuceBasedProxyManager.builderFor(connection).withClientSideConfig(
                ClientSideConfig.getDefault().withExpirationAfterWriteStrategy(
                        ExpirationAfterWriteStrategy
                        .basedOnTimeForRefillingBucketUpToMax(Duration.ofHours(1L))
                )).build();
       ///Exemplo prático:
       ///
       /// Imagine que você tenha um bucket com:
       /// - Capacidade máxima: 10 tokens.
       /// - Recuperação: 1 token a cada 10 minutos.
       /// - Tokens disponíveis atualmente: 2.
       /// Para recuperar os 8 tokens restantes, serão necessários 80 minutos.
       ///
       /// A estratégia considera esse tempo de recuperação e acrescenta a margem configurada de uma hora.
       ///
       /// Assim, o bucket não será removido do Redis antes de ter tempo suficiente para recuperar sua capacidade.
       ///
       /// Isso evita que um bucket seja apagado prematuramente e recriado com uma capacidade cheia
       /// permitindo que um usuário contorne o Rate Limiting.
    }
}
