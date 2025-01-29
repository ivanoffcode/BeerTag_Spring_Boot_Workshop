package com.example.beertag.repositories;

import com.example.beertag.entities.Beer;
import com.example.beertag.entities.Style;
import com.example.beertag.exceptions.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StyleRepositorySqlImpl implements StyleRepository{

    private final SessionFactory sessionFactory;

    @Autowired
    public StyleRepositorySqlImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Style> get() {
        try ( Session session = sessionFactory.openSession() ){
            Query<Style> query = session.createQuery("from Style", Style.class);
            return query.list();
        }
    }

    @Override
    public Style get(int id) {
        try (Session session = sessionFactory.openSession()) {
            Style style = session.get(Style.class, id);
            if (style == null){
                throw new EntityNotFoundException("Style", id);
            }
            return style;
        }
    }

    @Override
    public Style get(String name) {
        try ( Session session = sessionFactory.openSession() ){
            Query<Style> query = session.createQuery("from Style where name = :name", Style.class);
            query.setParameter("name", name);
            List<Style> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Style", "name", name);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(Style style) {
        try(Session session = sessionFactory.openSession()) {
            session.save(style);
        }
    }

    @Override
    public void update(Style style) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(style);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Style styleToDelete = get(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(styleToDelete);
            session.getTransaction().commit();
        }
    }
}
