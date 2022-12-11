package ch.bfh.habits.services

import ch.bfh.habits.dtos.group.GroupDTO
import ch.bfh.habits.dtos.group.GroupEntityBuilder
import ch.bfh.habits.entities.Group
import ch.bfh.habits.entities.Habit
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.repositories.GroupDAO
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GroupService(private val groupDAO: GroupDAO) {
    @Transactional
    fun getAllGroups(userId: Long): List<Group> {
        return groupDAO.findAllByUserId(userId)
    }

    @Transactional
    fun newGroup(groupDTO: GroupDTO, userId: Long): Group {
        val newGroup = GroupEntityBuilder.createGroupEntityFromDTO(groupDTO, userId)
        return groupDAO.save(newGroup)
    }

    @Transactional
    fun getGroup(id: Long, userId: Long): Group {
        return groupDAO.findByUserIdAndId(userId, id) ?: throw EntityNotFoundException("Group not found or not owned by user")
    }

    @Transactional
    fun deleteGroup(id: Long, userId: Long) {
        return groupDAO.delete(getGroup(id, userId))
    }

    @Transactional
    fun updateGroup(id: Long, groupDTO: GroupDTO, userId: Long): Group {
        val currentGroup = groupDAO.findByUserIdAndId(userId, id) ?: throw EntityNotFoundException("Group not found or not owned by user")
        GroupEntityBuilder.applyGroupDtoToEntity(groupDTO, currentGroup)
        return currentGroup
    }
}
