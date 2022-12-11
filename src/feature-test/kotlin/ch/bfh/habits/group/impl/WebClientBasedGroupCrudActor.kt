package ch.bfh.habits.group.impl

import ch.bfh.habits.auth.impl.WebClientBasedAuthCrudActor
import ch.bfh.habits.dtos.group.GroupDTO
import ch.bfh.habits.entities.Group
import ch.bfh.habits.exceptions.BadRequestException
import ch.bfh.habits.exceptions.EntityNotFoundException
import ch.bfh.habits.group.GroupCrudActor
import org.assertj.core.api.Assertions
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.returnResult
import reactor.core.publisher.Mono

class WebClientBasedGroupCrudActor( private val webClient: WebTestClient, authWebClient: WebTestClient) : WebClientBasedAuthCrudActor(authWebClient),
    GroupCrudActor {
    override fun getsAllGroups(): List<Group> {
        val result = webClient.get()
            .uri("/api/groups/")
            .header("Authorization", token)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody<List<Group>>()
            .returnResult()

        return result.responseBody!!
    }

    override fun createsGroup(groupDTO: GroupDTO): Group {
        val result = webClient.post()
            .uri("/api/group")
            .header("Authorization", token)
            .body(Mono.just(groupDTO), GroupDTO::class.java)
            .exchange()
            .expectStatus().isCreated
            .expectBody<Group>()
            .returnResult()

        return result.responseBody!!
    }

    override fun seesGroupExists(groupId: Long): Boolean {
        return webClient.get()
            .uri("/api/group/$groupId")
            .header("Authorization", token)
            .exchange()
            .returnResult<Any>()
            .status
            .is2xxSuccessful
    }

    override fun getsGroup(groupId: Long): Group {
        val result = webClient.get()
            .uri("/api/group/$groupId")
            .header("Authorization", token)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBody<Group>()
            .returnResult()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Group id = $groupId")

        Assertions.assertThat(result.status.is2xxSuccessful).isTrue
        return result.responseBody!!
    }

    override fun deletesGroup(groupId: Long) {
        val result = webClient.delete()
            .uri("/api/group/$groupId")
            .header("Authorization", token)
            .exchange()
            .returnResult<Any>()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Group id = $groupId")

        Assertions.assertThat(result.status).isEqualTo(HttpStatus.NO_CONTENT)
    }

    override fun updatesGroup(groupId: Long, groupDTO: GroupDTO): Group {
        val result = webClient.put()
            .uri("/api/group/$groupId")
            .header("Authorization", token)
            .body(Mono.just(groupDTO), GroupDTO::class.java)
            .exchange()
            .expectBody<Group>()
            .returnResult()

        if (result.status == HttpStatus.NOT_FOUND)
            throw EntityNotFoundException("Group id = $groupId")
        else if (result.status == HttpStatus.BAD_REQUEST)
            throw BadRequestException("Group id = $groupId")

        Assertions.assertThat(result.status.is2xxSuccessful).isTrue
        return result.responseBody!!
    }
}
