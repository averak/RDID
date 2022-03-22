package dev.abelab.rdid.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * Health check rest controller
 */
@Api(tags = "Health Check")
@RestController
@RequestMapping(path = "/api/health", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class HealthCheckRestController {

    /**
     * ヘルスチェックAPI
     */
    @ApiOperation( //
        value = "ヘルスチェック", //
        notes = "APIのヘルスチェックを行う。" //
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void healthCheck() {}

}
