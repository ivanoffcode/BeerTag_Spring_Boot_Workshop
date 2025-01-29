package com.example.beertag.repositories;

import com.example.beertag.entities.Beer;
import com.example.beertag.entities.Country;
import com.example.beertag.entities.Style;
import com.example.beertag.exceptions.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryRepositorySqlImpl implements CountryRepository{

    private final SessionFactory sessionFactory;

    @Autowired
    public CountryRepositorySqlImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Country> get() {
        try ( Session session = sessionFactory.openSession() ){
            Query<Country> query = session.createQuery("from Country ", Country.class);
            return query.list();
        }
    }

    @Override
    public Country get(int id) {
        try (Session session = sessionFactory.openSession()) {
            Country country = session.get(Country.class, id);
            if (country == null){
                throw new EntityNotFoundException("Country", id);
            }
            return country;
        }
    }

    @Override
    public Country get(String name) {
        try ( Session session = sessionFactory.openSession() ) {
            Query<Country> query = session.createQuery("from Country where name = :name", Country.class);
            query.setParameter("name", name);
            List<Country> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Country", "name", name);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(Country country) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(country);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Country country) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(country);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Country countryToDelete = get(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(countryToDelete);
            session.getTransaction().commit();
        }
    }
}
