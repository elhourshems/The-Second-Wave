package com.thesecondwave.store.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin")
public class AdminController {
    @Operation(summary = "Admin welcome message", description = "Returns a simple hello response reserved for admin users.", tags = {"Admin"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Request succeeded."),
        @ApiResponse(responseCode = "400", description = "Bad request."),
        @ApiResponse(responseCode = "404", description = "Resource not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello Admin!";
    }
}
