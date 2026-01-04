package com.tree.service.base;

import com.tree.dto.PageResponse;
import com.tree.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Base service providing common CRUD operations.
 *
 * @param <E> Entity type
 * @param <R> Response DTO type
 * @param <Q> Request DTO type
 */
@Slf4j
public abstract class BaseService<E, R, Q> {

    protected abstract JpaRepository<E, UUID> getRepository();
    protected abstract String getEntityName();
    protected abstract R toResponse(E entity);
    protected abstract E createEntity(Q request);
    protected abstract E updateEntity(E entity, Q request);

    /**
     * Get all entities with pagination
     */
    public PageResponse<R> getAll(Pageable pageable) {
        Page<R> page = getRepository().findAll(pageable).map(this::toResponse);
        return PageResponse.from(page);
    }

    /**
     * Get all entities as list
     */
    public List<R> getAll() {
        return getRepository().findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get entity by ID
     */
    public R getById(UUID id) {
        E entity = findByIdOrThrow(id);
        return toResponse(entity);
    }

    /**
     * Find entity by ID or throw exception
     */
    protected E findByIdOrThrow(UUID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(), "id", id));
    }

    /**
     * Create new entity
     */
    @Transactional
    public R create(Q request) {
        log.info("Creating {}", getEntityName());
        E entity = createEntity(request);
        getRepository().save(entity);
        log.info("{} created: {}", getEntityName(), getId(entity));
        return toResponse(entity);
    }

    /**
     * Update existing entity
     */
    @Transactional
    public R update(UUID id, Q request) {
        log.info("Updating {} with id: {}", getEntityName(), id);
        E entity = findByIdOrThrow(id);
        entity = updateEntity(entity, request);
        getRepository().save(entity);
        log.info("{} updated: {}", getEntityName(), id);
        return toResponse(entity);
    }

    /**
     * Delete entity by ID
     */
    @Transactional
    public void delete(UUID id) {
        log.info("Deleting {} with id: {}", getEntityName(), id);
        if (!getRepository().existsById(id)) {
            throw new ResourceNotFoundException(getEntityName(), "id", id);
        }
        getRepository().deleteById(id);
        log.info("{} deleted: {}", getEntityName(), id);
    }

    /**
     * Check if entity exists by ID
     */
    public boolean existsById(UUID id) {
        return getRepository().existsById(id);
    }

    /**
     * Count all entities
     */
    public long count() {
        return getRepository().count();
    }

    /**
     * Get entity ID - override in subclass if entity has different ID field
     */
    protected Object getId(E entity) {
        try {
            return entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * Map page to response page
     */
    protected PageResponse<R> mapPage(Page<E> page) {
        return PageResponse.from(page.map(this::toResponse));
    }

    /**
     * Map page with custom mapper
     */
    protected <T> PageResponse<T> mapPage(Page<E> page, Function<E, T> mapper) {
        return PageResponse.from(page.map(mapper));
    }

    /**
     * Map list to response list
     */
    protected List<R> mapList(List<E> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
