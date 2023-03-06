package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.swing.*;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   public User getUserByModel(String model, int series) {
      TypedQuery<Car> q = sessionFactory.getCurrentSession()
              .createQuery("from Car where model = :model AND series = :series");
      q.setParameter("model", model);
      q.setParameter("series", series);
      List<Car> findCarList = q.getResultList();
      if(!findCarList.isEmpty()) {
         Car findCar = findCarList.get(0);
         List<User> userList = listUsers();
        User fu = userList.stream()
                 .filter(user -> user.getCar().equals(findCar))
                 .findAny()
                 .orElse(null);
         return fu;
      }
      return null;
   }

}
