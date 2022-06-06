package com.revature.reimburse.DAOs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> {

    void save(T obj) throws SQLException;

    void update(T obj) throws SQLException;

    void delete(T obj) throws SQLException;

    T getByID(String id) throws SQLException;

    T getObject(ResultSet rs)throws SQLException;


    List<T> getAll() throws SQLException;




}
