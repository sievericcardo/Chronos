package it.kernelpanic.chronos.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.kernelpanic.chronos.model.User;
import it.kernelpanic.chronos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * User controller class to handle rest api pattern for the users.
 * We use the controller to handle the various services for the user inside
 * the application.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService service;

    /**
     * Create a new user inside the database
     *
     * @param user object get from the request body that need to be added
     * @return true if it succeeded
     */
    @PostMapping(value = "/register")
    @ApiOperation("Create a new user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Boolean.class)})
    public boolean postUser(@RequestBody User user) {
        return service.add(user);
    }

    /**
     * Delete a user corresponding to a specific identifier
     *
     * @param id user identifier for the deletion
     * @return true if it succeeded
     */
    @DeleteMapping("/del/{id}")
    @ApiOperation("Delete a specific user")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Boolean.class)})
    public boolean delete(@PathVariable long id) {
        return service.delete(id);
    }
}
