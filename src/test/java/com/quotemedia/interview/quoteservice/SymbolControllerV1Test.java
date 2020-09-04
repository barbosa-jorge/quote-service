package com.quotemedia.interview.quoteservice;

import com.quotemedia.interview.quoteservice.dtos.QuoteResponseDTO;
import com.quotemedia.interview.quoteservice.entities.UserEntity;
import com.quotemedia.interview.quoteservice.exceptions.BadRequestException;
import com.quotemedia.interview.quoteservice.exceptions.QuoteNotFoundException;
import com.quotemedia.interview.quoteservice.repositories.UserRepository;
import com.quotemedia.interview.quoteservice.security.AuthenticationFilter;
import com.quotemedia.interview.quoteservice.services.QuoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SymbolControllerV1Test {

    private static final String URI_API_SYMBOLS = "/api/v1/symbols/{symbol}/quotes/latest";
    private static final String BID_VALUE = "0.67";
    private static final String ASK_VALUE = "0.81";
    private static final String SYMBOL_GOOG = "GOOG";
    private static final String VALID_SYMBOL_NO_QUOTES = "GOOD";
    private static final String INVALID_SYMBOL_LENGTH_3 = "ABC";
    private static final String INVALID_SYMBOL_LENGTH_7 = "ABCDEFG";
    private static final String INVALID_SYMBOL_EMPTY_SPACE = " ";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageSource messageSource;

    @MockBean
    private QuoteService quoteService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void givenValidSymbolWithQuotes_thenReturnLatestQuoteSuccessfully() throws Exception {

        QuoteResponseDTO quoteResponseDTO = createQuoteResponse(BID_VALUE, ASK_VALUE);
        given(quoteService.findLatestQuoteBySymbol(anyString())).willReturn(quoteResponseDTO);
        given(userRepository.findByEmail(anyString())).willReturn(getMockedUserEntity());

        String token = AuthenticationFilter.createToken("username");

        mockMvc.perform(get(URI_API_SYMBOLS, SYMBOL_GOOG).header("Authorization", "Bearer "+token))
                .andExpect(status().isOk())
                .andExpect(content().json(getMockedQuoteResponse()))
                .andReturn();
    }

    @Test
    public void givenInvalidEmptySymbol_thenReturnBadRequestResponse() throws Exception {

        given(quoteService.findLatestQuoteBySymbol(anyString())).willThrow(BadRequestException.class);
        given(userRepository.findByEmail(anyString())).willReturn(getMockedUserEntity());
        String token = AuthenticationFilter.createToken("username");

        mockMvc.perform(get(URI_API_SYMBOLS, INVALID_SYMBOL_EMPTY_SPACE)
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void givenInvalidSymbolWith3Chars_thenReturnBadRequestResponse() throws Exception {

        given(quoteService.findLatestQuoteBySymbol(anyString())).willThrow(BadRequestException.class);
        given(userRepository.findByEmail(anyString())).willReturn(getMockedUserEntity());
        String token = AuthenticationFilter.createToken("username");

        mockMvc.perform(get(URI_API_SYMBOLS, INVALID_SYMBOL_LENGTH_3)
                .header("Authorization", "Bearer "+token))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void givenInvalidSymbolWithMoreThan6Chars_thenReturnBadRequestResponse() throws Exception {

        given(quoteService.findLatestQuoteBySymbol(anyString())).willThrow(BadRequestException.class);
        given(userRepository.findByEmail(anyString())).willReturn(getMockedUserEntity());
        String token = AuthenticationFilter.createToken("username");

        mockMvc.perform(get(URI_API_SYMBOLS, INVALID_SYMBOL_LENGTH_7)
                .header("Authorization", "Bearer "+token))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void givenValidSymbolWithNoQuotes_thenReturnQuoteNotFoundResponse() throws Exception {

        given(quoteService.findLatestQuoteBySymbol(anyString())).willThrow(QuoteNotFoundException.class);

        given(userRepository.findByEmail(anyString())).willReturn(getMockedUserEntity());
        String token = AuthenticationFilter.createToken("username");

        mockMvc.perform(get(URI_API_SYMBOLS, VALID_SYMBOL_NO_QUOTES)
                .header("Authorization", "Bearer "+token))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    public void givenRequestWithoutAuthorizationToken_thenReturnForbiddenError() throws Exception {

        mockMvc.perform(get(URI_API_SYMBOLS, VALID_SYMBOL_NO_QUOTES))
                .andExpect(status().isForbidden())
                .andReturn();

    }

    private QuoteResponseDTO createQuoteResponse(String bid, String ask) {
        return new QuoteResponseDTO(new BigDecimal(bid), new BigDecimal(ask));
    }

    private String getMockedQuoteResponse() {
        return "{ bid: 0.67, ask: 0.81 }";
    }

    private Optional<UserEntity> getMockedUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId("userId");
        userEntity.setUsername("username");
        userEntity.setEmail("userame@test.com");
        userEntity.setEncryptedPassword("encryptedPassword");
        return Optional.ofNullable(userEntity);
    }

}