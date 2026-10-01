package thuc.ute.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

@PersistenceContext
public class JpaConfig_24110349 {

	private static final EntityManagerFactory FACTORY =
			Persistence.createEntityManagerFactory("jpa-hibernate-sql");

	public static EntityManager getEntityManager() {
		return FACTORY.createEntityManager();
	}
}
