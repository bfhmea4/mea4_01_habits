package ch.bfh.habits.dtos.group

import ch.bfh.habits.entities.Group

class GroupEntityBuilder private constructor() {

    companion object {
        fun createGroupEntityFromDTO(groupDTO: GroupDTO, userId: Long): Group {
            return Group(
                title = groupDTO.title ?: "",
                userId = userId
            )
        }

        fun applyGroupDtoToEntity(groupDTO: GroupDTO, groupEntity: Group) {
            groupEntity.title = groupDTO.title ?: groupEntity.title
        }
    }

}
