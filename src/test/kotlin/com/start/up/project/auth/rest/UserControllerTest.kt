package com.start.up.project.auth.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.start.up.project.auth.entity.User
import com.start.up.project.auth.repository.UserRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.util.NestedServletException
import javax.transaction.Transactional


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
internal class UserControllerTest {

    @Autowired
    private val context: WebApplicationContext? = null

    @Autowired
    private val userRepository: UserRepository? = null

    val objectMapper =  ObjectMapper();

    private var mvc: MockMvc? = null

    @Before
    fun setup() {
        mvc = webAppContextSetup(context!!)
                .apply<DefaultMockMvcBuilder>(springSecurity())
                .build()
    }

    @WithMockUser("seth")
    @Test
    fun `should return current user when user exists` () {
        val user = User();
        user.username = "seth"
        userRepository!!.save(user);
        mvc!!.perform(post("/users/current/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("seth"));
    }

    @WithMockUser("not seth")
    @Test
    fun `should return invalid user when user does not exists` () {
        assertThrows<NestedServletException> { mvc!!.perform(post("/users/current/user")
                .contentType(MediaType.APPLICATION_JSON)) }
    }


    @Test
    fun `should sign up seth for valid password`() {
        val user = User()
        user.username = "seth"
        user.password = "pass"
        mvc!!.perform(post("/users/sign-up")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
        assertEquals(userRepository!!.findByUsername(user.username).username, user.username)
    }
}