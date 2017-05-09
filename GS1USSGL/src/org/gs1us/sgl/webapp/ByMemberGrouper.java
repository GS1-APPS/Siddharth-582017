package org.gs1us.sgl.webapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.gs1us.sgg.gbservice.api.GBAccountData;
import org.gs1us.sgl.memberservice.Member;
import org.gs1us.sgl.memberservice.MemberService;
import org.gs1us.sgl.memberservice.standalone.StandaloneMemberDao;

public class ByMemberGrouper<T extends GBAccountData>
{
    private StandaloneMemberDao m_memberDao;
    
    
    
    private Map<String,List<T>> m_map = new HashMap<>();
    
    public ByMemberGrouper(StandaloneMemberDao memberDao)
    {
        super();
        m_memberDao = memberDao;
    }

    public void add(T elt)
    {
        String gln = elt.getGBAccountGln();
        List<T> list = m_map.get(gln);
        if (list == null)
        {
            list = new ArrayList<>();
            m_map.put(gln, list);
        }
        list.add(elt);
    }
    
    public void addAll(Collection<? extends T> elts)
    {
        for (T elt : elts)
            add(elt);
    }
    
    public int size()
    {
        return m_map.size();
    }
    
    public List<Entry<T>> getGroups()
    {
        List<Entry<T>> result = new ArrayList<>(m_map.size());
        for (Map.Entry<String, List<T>> entry : m_map.entrySet())
        {
            String gln = entry.getKey();
            Member member = m_memberDao.getMemberByGln(gln);
            result.add(new Entry<T>(member, entry.getValue()));
        }
        Collections.sort(result, new Comparator<Entry<T>>()
        {

            @Override
            public int compare(Entry<T> o1, Entry<T> o2)
            {
                return o1.getMember().getCompanyName().compareTo(o2.getMember().getCompanyName());
            }
        });
        return result;
    }
    
    public static class Entry<T extends GBAccountData>
    {
        public Member m_member;
        public List<T> m_elts;
        private Entry(Member member, List<T> elts)
        {
            super();
            m_member = member;
            m_elts = elts;
        }
        public Member getMember()
        {
            return m_member;
        }
        public List<T> getElts()
        {
            return m_elts;
        }
        
        
    }
}
