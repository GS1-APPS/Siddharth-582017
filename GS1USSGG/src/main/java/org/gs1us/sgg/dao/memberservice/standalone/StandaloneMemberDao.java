package org.gs1us.sgg.dao.memberservice.standalone;

import java.util.List;

public abstract class StandaloneMemberDao
{
    public abstract List<StandaloneMember> getAllMembers();
    public abstract List<StandaloneMember> getAllMembers(String sortKey, int sortDirection);
    /* Commenting the create/update/delete member methods
    public abstract StandaloneMember createMember();
    public abstract void updateMember(StandaloneMember Member);
    public abstract void deleteMember(StandaloneMember Member);
    */
    public abstract StandaloneMember getMember(String id);
    public abstract StandaloneMember getMemberByGln(String gln);

}
