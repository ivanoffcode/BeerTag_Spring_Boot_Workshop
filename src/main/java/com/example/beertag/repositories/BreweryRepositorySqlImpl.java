package com.example.beertag.repositories;

import com.example.beertag.entities.Brewery;
import com.example.beertag.exceptions.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BreweryRepositorySqlImpl implements BreweryRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public BreweryRepositorySqlImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Brewery> get() {
        try ( Session session = sessionFactory.openSession() ){
            Query<Brewery> query = session.createQuery("from Brewery", Brewery.class);
            return query.list();
        }
    }

    @Override
    public Brewery get(int id) {
        try (Session session = sessionFactory.openSession()) {
            Brewery brewery = session.get(Brewery.class, id);
            if (brewery == null){
                throw new EntityNotFoundException("Brewery", id);
            }
            return brewery;
        }
    }

    @Override
    public Brewery get(String name) {
        try ( Session session = sessionFactory.openSession() ){
            Query<Brewery> query = session.createQuery("from Brewery where name = :name", Brewery.class);
            query.setParameter("name", name);
            List<Brewery> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Brewery", "name", name);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(Brewery brewery) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(brewery);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Brewery brewery) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(brewery);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Brewery breweryToDelete = get(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(breweryToDelete);
            session.getTransaction().commit();
        }
    }
}
