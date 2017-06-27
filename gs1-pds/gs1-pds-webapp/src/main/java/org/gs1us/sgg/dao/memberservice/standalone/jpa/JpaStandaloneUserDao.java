package org.gs1us.sgg.dao.memberservice.standalone.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.gs1us.sgg.dao.memberservice.standalone.StandaloneUser;
import org.gs1us.sgg.dao.memberservice.standalone.StandaloneUserDao;



public class JpaStandaloneUserDao extends StandaloneUserDao
{
    @PersistenceContext(unitName="gs1ussglPersistenceUnit")
    private EntityManager m_entityManager;
    
    @Override
    public List<StandaloneUser> getAllUsers()
    {
        return getAllUsers(null, 0);
    }

    @Override
    public List<StandaloneUser> getAllUsers(String sortKey, int sortDirection)
    {
        CriteriaBuilder cb = m_entityManager.getCriteriaBuilder();
        CriteriaQuery<JpaStandaloneUser> query = cb.createQuery(JpaStandaloneUser.class);
        Root<JpaStandaloneUser> root = query.from(JpaStandaloneUser.class);
        Join<JpaStandaloneUser, JpaStandaloneMember> join = root.join("m_member");
        if (sortKey != null)
        {
            if (sortKey.equals("companyName"))
            {
                if (sortDirection > 0)
                    query.orderBy(cb.asc(join.get("m_" + sortKey)));
                else
                    query.orderBy(cb.desc(join.get("m_" + sortKey)));
           
            }
            else
            {
                if (sortDirection > 0)
                    query.orderBy(cb.asc(root.get("m_" + sortKey)));
                else
                    query.orderBy(cb.desc(root.get("m_" + sortKey)));
            }
        }
        List<JpaStandaloneUser> result = m_entityManager.createQuery(query).getResultList();
        // Note that entityStore and propertyMaster attributes are null, per the contract.
        return new ArrayList<StandaloneUser>(result);
    }

    /*
    @Override
    public StandaloneUser createUser()
    {
        return new JpaStandaloneUser();
    }

    @Override
    public void updateUser(StandaloneUser user)
    {
        // For JPA, don't need to do anything if item is already persisted.
        // If it's not persisted, the id will be null
        if (user.getId() == null)
        {
            m_entityManager.persist(user);
        }
    }

    @Override
    public void deleteUser(StandaloneUser user)
    {
        m_entityManager.remove(user);
    }

	*/
    
    @Override
    public StandaloneUser getUser(String id)
    {
        JpaStandaloneUser user = m_entityManager.find(JpaStandaloneUser.class, JpaStandaloneUser.KEY_MAPPER.idToKey(id));
        return user;

    }
	

    @Override
    public StandaloneUser getUserByUsername(String username)
    {
        // We need to treat usernames as case-insensitive!
        return getUserByField("m_username", username.toLowerCase());
    }

    @Override
    public StandaloneUser getUserByPasswordReset(String passwordReset)
    {
        return getUserByField("m_passwordReset", passwordReset);

    }
    
    private StandaloneUser getUserByField(String fieldname, String value)
    {
        CriteriaBuilder cb = m_entityManager.getCriteriaBuilder();
        CriteriaQuery<JpaStandaloneUser> query = cb.createQuery(JpaStandaloneUser.class);
        Root<JpaStandaloneUser> root = query.from(JpaStandaloneUser.class);
        query.where(cb.equal(root.get(fieldname), value));
        List<JpaStandaloneUser> result = m_entityManager.createQuery(query).getResultList();
        
        if (result == null || result.size() == 0)
            return null;
        else
            return result.get(0);

    }

}
