package edu.harvard.cscis71.curriculum.api;

import edu.harvard.cscis71.curriculum.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Tag(name="user", description = "User API")
public interface UserApi {

    default UserApiDelegate getDelegate() {
        return new UserApiDelegate() {};
    }

    @Operation(summary = "Create User", description = "Create a user", tags = { "user" })
    @ApiResponses(value = { @ApiResponse(description = "successful operation", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)), @Content(mediaType = "application/xml", schema = @Schema(implementation = User.class)) }) })
    @PostMapping(value = "/user", consumes = { "application/json", "application/xml", "application/x-www-form-urlencoded" })
    default ResponseEntity<Void> createUser(@Parameter(description = "created user") @Valid @RequestBody User user) {
        return getDelegate().createUser(user);
    }

    @Operation(summary = "Delete user", description = "This can only be done by the logged in user.", tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid username supplied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping(value = "/user/{username}")
    default ResponseEntity<Void> deleteUser(@Parameter(description = "The name that needs to be deleted", required = true) @PathVariable("username") String username) {
        return getDelegate().deleteUser(username);
    }

    @Operation(summary = "Get user by user name", tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)), @Content(mediaType = "application/xml", schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid username supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
    @GetMapping(value = "/user/{username}")
    default ResponseEntity<User> getUserByName(
            @Parameter(description = "The name that needs to be fetched. Use user1 for testing. ", required = true) @PathVariable("username") String username) {
        return getDelegate().getUserByName(username);
    }

    @Operation(summary = "Update user", description = "This can only be done by the logged in user.", tags = { "user" })
    @ApiResponses(value = @ApiResponse(description = "successful operation"))
    @PutMapping(value = "/user/{username}", consumes = { "application/json", "application/xml", "application/x-www-form-urlencoded" })
    default ResponseEntity<Void> updateUser(
            @Parameter(description = "name that need to be deleted", required = true, explode = Explode.FALSE, in = ParameterIn.PATH, name = "username", style = ParameterStyle.SIMPLE, schema = @Schema(type = "string")) @PathVariable("username") String username,
            @Parameter(description = "Update an existent user in the store") @Valid @RequestBody User user) {
        return getDelegate().updateUser(username, user);
    }
}
