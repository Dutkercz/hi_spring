package dutkercz.hi_backend.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class EmailRateLimiterService {
    ///Injeção do proxy que configurei
    private final ProxyManager<String> proxyManager;

    ///vai receber o email e tentar consumir um token do bucket
    /// O retorno é um ConsumptionProbe, que contém informações sobre o resultado da tentativa. Como por exemplo
    /// se conseguiu consumir, quantos tokens restam, quanto tento falta para poder consumir outro token
    public ConsumptionProbe tryConsume(String email){
        String normalizedEmail = email.trim().toLowerCase();
        String key = "rate_limiter:forgot_pw:email:"+normalizedEmail;

    ///O Supplier é uma interface funcional do Java que representa algo capaz de fornecer um objeto quando solicitado.
    ///Nesse caso, ele fornece uma configuração de bucket
    ///o ProxyManager recebe essa função e pode utilizá-la quando precisar criar a configuração
    ///inicial de um bucket que ainda não existe.
        Supplier<BucketConfiguration> configSupplier = () -> BucketConfiguration.builder()
                .addLimit(Bandwidth.builder()
                           //numero de tentativas
                           .capacity(1)
                            //renova a cada 30 minutos
                            .refillIntervally(1, Duration.ofMinutes(10))
                            .build()
                ).build();
        /// O ProxyManager utiliza a chave para verificar se já existe um bucket correspondente no Redis.
        /// Se não existir, cria um bucket utilizando a configuração fornecida pelo Supplier.
        Bucket bucket = proxyManager.builder().build(key, configSupplier);
        return bucket.tryConsumeAndReturnRemaining(1);
    }
}
