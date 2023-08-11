package com.abdallah.developer.gateway.repository;

import com.abdallah.developer.gateway.domain.Blog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Blog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlogRepository extends ReactiveCrudRepository<Blog, Long>, BlogRepositoryInternal {
    @Query("SELECT * FROM blog entity WHERE entity.post_id = :id")
    Flux<Blog> findByPost(Long id);

    @Query("SELECT * FROM blog entity WHERE entity.post_id IS NULL")
    Flux<Blog> findAllWherePostIsNull();

    @Override
    <S extends Blog> Mono<S> save(S entity);

    @Override
    Flux<Blog> findAll();

    @Override
    Mono<Blog> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BlogRepositoryInternal {
    <S extends Blog> Mono<S> save(S entity);

    Flux<Blog> findAllBy(Pageable pageable);

    Flux<Blog> findAll();

    Mono<Blog> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Blog> findAllBy(Pageable pageable, Criteria criteria);

}
