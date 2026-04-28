package dao;

public interface BaseDao<T> {
    T create(T entity);
}
