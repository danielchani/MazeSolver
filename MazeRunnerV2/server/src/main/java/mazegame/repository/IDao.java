package main.java.mazegame.repository;

import java.util.List;

public interface IDao<T> {
    void save(T model) throws Exception;
    T find(String id) throws Exception;
    void update(T model) throws Exception;
    void delete(String id) throws Exception;
    List<String> listIds() throws Exception;
}