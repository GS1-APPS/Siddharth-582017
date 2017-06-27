package org.gs1us.sgl.memberservice.standalone.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.gs1us.sgl.memberservice.standalone.StandaloneMember;
import org.gs1us.sgl.memberservice.standalone.StandaloneMemberDao;
import org.gs1us.sgl.memberservice.standalone.StandaloneUser;

public class JpaStandaloneMemberDao extends StandaloneMemberDao
{
    @PersistenceContext
    private EntityManager m_entityManager;
    
    @Override
    public List<StandaloneMember> getAllMembers()
    {
        return getAllMembers("companyName", +1);
    }

    @Override
    public List<StandaloneMember> getAllMembers(String sortKey, int sortDirection)
    {
        CriteriaBuilder cb = m_entityManager.getCriteriaBuilder();
        CriteriaQuery<JpaStandaloneMember> query = cb.createQuery(JpaStandaloneMember.class);
        Root<JpaStandaloneMember> root = query.from(JpaStandaloneMember.class);
        if (sortKey != null)
        {
            if (sortDirection > 0)
                query.orderBy(cb.asc(root.get("m_" + sortKey)));
            else
                query.orderBy(cb.desc(root.get("m_" + sortKey)));
        }
        List<JpaStandaloneMember> result = m_entityManager.createQuery(query).getResultList();
        return new ArrayList<StandaloneMember>(result);
    }

    @Override
    public StandaloneMember createMember()
    {
        return new JpaStandaloneMember();
    }

    @Override
    public void updateMember(StandaloneMember member)
    {
        // For JPA, don't need to do anything if item is already persisted.
        // If it's not persisted, the id will be null
        if (member.getId() == null)
        {
            m_entityManager.persist(member);
        }
    }

    @Override
    public void deleteMember(StandaloneMember member)
    {
        m_entityManager.remove(member);
    }

    @Override
    public StandaloneMember getMember(String id)
    {
        JpaStandaloneMember member = m_entityManager.find(JpaStandaloneMember.class, JpaStandaloneMember.KEY_MAPPER.idToKey(id));
        return member;

    }
    
    @Override
    public StandaloneMember getMemberByGln(String gln)
    {
        return getMemberByField("m_gln", gln);

    }
    private StandaloneMember getMemberByField(String fieldname, String value)
    {
        CriteriaBuilder cb = m_entityManager.getCriteriaBuilder();
        CriteriaQuery<JpaStandaloneMember> query = cb.createQuery(JpaStandaloneMember.class);
        Root<JpaStandaloneMember> root = query.from(JpaStandaloneMember.class);
        query.where(cb.equal(root.get(fieldname), value));
        List<JpaStandaloneMember> result = m_entityManager.createQuery(query).getResultList();
        
        if (result == null || result.size() == 0)
            return null;
        else
            return result.get(0);

    }


}
