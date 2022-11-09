package com.labs.drink.repository;

import com.labs.datastore.DataStore;
import com.labs.drink.entity.Kind;
import com.labs.repository.Repository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Repository for profession entity. Repositories should be used in business layer (e.g.: in services).
 */
@Dependent
public class KindRepository implements Repository<Kind, String> {

    /**
     * Underlying data store. In future should be replaced with database connection.
     */
    private DataStore store;

    /**
     * @param store data store
     */
    @Inject
    public KindRepository(DataStore store) {
        this.store = store;
    }


    @Override
    public Optional<Kind> find(String id) {
        return store.findKind(id);
    }

    @Override
    public List<Kind> findAll() {
        return store.findAllKinds();
    }

    @Override
    public void create(Kind entity) {
        store.createKind(entity);
    }

    @Override
    public void delete(Kind entity) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }

    @Override
    public void update(Kind entity) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }

}
