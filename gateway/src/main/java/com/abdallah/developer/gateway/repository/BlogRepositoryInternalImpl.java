package com.abdallah.developer.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.abdallah.developer.gateway.domain.Blog;
import com.abdallah.developer.gateway.repository.rowmapper.BlogRowMapper;
import com.abdallah.developer.gateway.repository.rowmapper.PostRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Blog entity.
 */
@SuppressWarnings("unused")
class BlogRepositoryInternalImpl extends SimpleR2dbcRepository<Blog, Long> implements BlogRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PostRowMapper postMapper;
    private final BlogRowMapper blogMapper;

    private static final Table entityTable = Table.aliased("blog", EntityManager.ENTITY_ALIAS);
    private static final Table postTable = Table.aliased("post", "post");

    public BlogRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PostRowMapper postMapper,
        BlogRowMapper blogMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Blog.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.postMapper = postMapper;
        this.blogMapper = blogMapper;
    }

    @Override
    public Flux<Blog> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Blog> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = BlogSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PostSqlHelper.getColumns(postTable, "post"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(postTable)
            .on(Column.create("post_id", entityTable))
            .equals(Column.create("id", postTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Blog.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Blog> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Blog> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Blog process(Row row, RowMetadata metadata) {
        Blog entity = blogMapper.apply(row, "e");
        entity.setPost(postMapper.apply(row, "post"));
        return entity;
    }

    @Override
    public <S extends Blog> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
