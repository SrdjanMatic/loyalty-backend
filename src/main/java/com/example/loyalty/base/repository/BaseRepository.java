package com.example.loyalty.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
    <P> Optional<P> findById(ID id, Class<P> projectionClass);
    <P> List<P> findAllBy(Class<P> projectionClass);
}
