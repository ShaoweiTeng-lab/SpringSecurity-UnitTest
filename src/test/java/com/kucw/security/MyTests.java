package com.kucw.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MyTests {

    @Autowired
    private MockMvc mockMvc;



    @Test
    public void testHello2() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/hello")
                .with(httpBasic("test1@gmail.com","111")) ;

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }

    /**
     * 授權測試
     * */
    @Test
    public void testWelcome() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/welcome")
                .with(httpBasic("test1@gmail.com","111")) ;

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }


    @Test
    public void testWelcomeNotPass() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/welcome")
                .with(httpBasic("test2@gmail.com","222")) ;

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));
    }

    @WithMockUser(username = "mock" , roles = { "ADMIN" })
    @Test
    public void testWelcomeMock() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/welcome");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }

    /**
     * 測試 Cors Proflight Request
     * */
    @Test
    public void testCors() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .options("/hello")//送出option
                .header("Access-Control-Request-Method","GET")
                .header("Origin","http://www.example.com");//模擬 來源

        mockMvc.perform(requestBuilder)
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin","*"))
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().string("Access-Control-Allow-Methods","GET"))
                .andExpect(status().is(200));
    }
}
