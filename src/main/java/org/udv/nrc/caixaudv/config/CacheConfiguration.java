package org.udv.nrc.caixaudv.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(org.udv.nrc.caixaudv.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.Produto.class.getName(), jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.Produto.class.getName() + ".itensCompras", jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.ItemCompra.class.getName(), jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.Compra.class.getName(), jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.Compra.class.getName() + ".itensCompras", jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.SessaoCaixa.class.getName(), jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.SessaoCaixa.class.getName() + ".compras", jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.SessaoCaixa.class.getName() + ".ressarcimentos", jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.Conta.class.getName(), jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.Conta.class.getName() + ".ressarcimentos", jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.Conta.class.getName() + ".compras", jcacheConfiguration);
            cm.createCache(org.udv.nrc.caixaudv.domain.Ressarcimento.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
