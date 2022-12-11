package ch.bfh.habits.group

import ch.bfh.habits.auth.AuthCrudActor
import ch.bfh.habits.dtos.group.GroupDTO
import ch.bfh.habits.entities.Group

interface GroupCrudActor : AuthCrudActor {
    fun getsAllGroups(): List<Group>
    fun createsGroup(groupDTO: GroupDTO): Group
    fun seesGroupExists(groupId: Long): Boolean
    fun getsGroup(groupId: Long): Group
    fun deletesGroup(groupId: Long)
    fun updatesGroup(groupId: Long, groupDTO: GroupDTO): Group
}
