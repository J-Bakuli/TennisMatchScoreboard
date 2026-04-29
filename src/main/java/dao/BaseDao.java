package dao;

public interface BaseDao<T> {
    T save(T entity);
}
