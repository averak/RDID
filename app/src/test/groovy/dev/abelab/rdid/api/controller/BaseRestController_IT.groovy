package dev.abelab.rdid.api.controller

import dev.abelab.rdid.BaseSpecification
import dev.abelab.rdid.api.response.ErrorResponse
import dev.abelab.rdid.exception.BaseException
import dev.abelab.rdid.helper.JsonConvertHelper
import dev.abelab.rdid.util.AuthUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.MultiValueMap
import org.springframework.web.context.WebApplicationContext

/**
 * RestController統合テストの基底クラス
 */
abstract class BaseRestController_IT extends BaseSpecification {

    private MockMvc mockMvc

    @Autowired
    WebApplicationContext webApplicationContext

    @Autowired
    AuthUtil authUtil

    /**
     * GET request
     *
     * @param path path
     *
     * @return HTTP request builder
     */
    MockHttpServletRequestBuilder getRequest(final String path) {
        return MockMvcRequestBuilders.get(path)
    }

    /**
     * POST request
     *
     * @param path path
     *
     * @return HTTP request builder
     */
    MockHttpServletRequestBuilder postRequest(final String path) {
        return MockMvcRequestBuilders.post(path)
    }

    /**
     * POST request (Form)
     *
     * @param path path
     * @param params query params
     *
     * @return HTTP request builder
     */
    MockHttpServletRequestBuilder postRequest(final String path, final MultiValueMap<String, String> params) {
        return MockMvcRequestBuilders.post(path) //
            .contentType(MediaType.APPLICATION_FORM_URLENCODED) //
            .params(params)
    }

    /**
     * POST request (JSON)
     *
     * @param path path
     * @param content request body
     *
     * @return HTTP request builder
     */
    MockHttpServletRequestBuilder postRequest(final String path, final Object content) {
        return MockMvcRequestBuilders.post(path) //
            .contentType(MediaType.APPLICATION_JSON_VALUE) //
            .content(JsonConvertHelper.convertObjectToJson(content))
    }

    /**
     * PUT request (JSON)
     *
     * @param path path
     * @param content request body
     *
     * @return HTTP request builder
     */
    MockHttpServletRequestBuilder putRequest(final String path, final Object content) {
        return MockMvcRequestBuilders.put(path) //
            .contentType(MediaType.APPLICATION_JSON_VALUE) //
            .content(JsonConvertHelper.convertObjectToJson(content))
    }

    /**
     * DELETE request
     *
     * @param path path
     *
     * @return HTTP request builder
     */
    MockHttpServletRequestBuilder deleteRequest(final String path) {
        return MockMvcRequestBuilders.delete(path)
    }

    /**
     * Execute request
     *
     * @param request HTTP request builder
     * @param status expected HTTP status
     *
     * @return MVC result
     */
    MvcResult execute(final MockHttpServletRequestBuilder request, final HttpStatus status) {
        final result = mockMvc.perform(request).andReturn()

        assert result.getResponse().getStatus() == status.value()
        return result
    }

    /**
     * Execute request / return response
     *
     * @param request HTTP request builder
     * @param status expected HTTP status
     * @param clazz response class
     *
     * @return response
     */
    def <T> T execute(final MockHttpServletRequestBuilder request, final HttpStatus status, final Class<T> clazz) {
        final result = mockMvc.perform(request).andReturn()

        assert result.getResponse().getStatus() == status.value()
        return JsonConvertHelper.convertJsonToObject(result.getResponse().getContentAsString(), clazz)
    }

    /**
     * Execute request / verify exception
     *
     * @param request HTTP request builder
     * @param exception expected exception
     *
     * @return error response
     */
    ErrorResponse execute(final MockHttpServletRequestBuilder request, final BaseException exception) {
        final result = mockMvc.perform(request).andReturn()
        final response = JsonConvertHelper.convertJsonToObject(result.getResponse().getContentAsString(), ErrorResponse.class)

        assert result.getResponse().getStatus() == exception.getHttpStatus().value()
        assert response.getCode() == exception.getErrorCode().getCode()
        return response
    }

    /**
     * ユーザのクレデンシャルを取得
     *
     * @param userId ユーザID
     */
    def getUserCredentials(final Integer userId) {
        return "Bearer " + this.authUtil.generateBearerToken(userId)
    }

    /**
     * setup before test case
     */
    def setup() {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(this.webApplicationContext)
            .addFilter(({ request, response, chain ->
                response.setCharacterEncoding("UTF-8");
                chain.doFilter(request, response);
            }))
            .build()
    }

}
