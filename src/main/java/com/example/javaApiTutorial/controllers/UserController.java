package com.example.javaApiTutorial.controllers;

import com.example.javaApiTutorial.entities.User;
import com.example.javaApiTutorial.exceptions.InvalidRequestBodyException;
import com.example.javaApiTutorial.pojo.ApiResponse;
import com.example.javaApiTutorial.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.function.Function;
import java.util.stream.Collectors;

@Api(description = "User Service")
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService usersService) {
        this.userService = usersService;
    }

    @ApiOperation( "find all users" )
    @GetMapping
    public ResponseEntity findAllUsers(){
        return ResponseEntity.status(200).body(userService.findAllUsers());
    }

    @ApiOperation( "find all users pageable" )
    @GetMapping("/page")
    public ResponseEntity findAllUsersPageable(@RequestParam(value = "page", defaultValue = "0") String page,
                                               @RequestParam(value = "size", defaultValue = "10") String size){
        Pageable pageable = PageRequest.of(Integer.valueOf(page), Integer.valueOf(size));
        return ResponseEntity.status(200).body(userService.findAllPageable(pageable));
    }

    @ApiOperation( "add users" )
    @PostMapping
    public ResponseEntity addUser(@RequestBody @Valid  User user, Errors errors){
        if (errors.hasErrors())
            throw new InvalidRequestBodyException( getValidationErrors.apply(errors) );
        return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON_UTF8).body(userService.addUser(user));
    }

    @ApiOperation( "find user by id" )
    @GetMapping("/{id}")
    public ResponseEntity findUser(@PathVariable Long id){
        return ResponseEntity.status(200).body(userService.findUser(id));
    }

    @ApiOperation( "update user by id")
    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody @Valid User users, Errors errors){
        if (errors.hasErrors())
            throw new InvalidRequestBodyException( getValidationErrors.apply(errors) );
        return ResponseEntity.status(200).body(userService.updateUser(id, users));
    }




    @ApiOperation( "delete user by id")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        if (userService.deleteUser(id))
            return ResponseEntity.status(200).body(ApiResponse.builder().code(200)
                    .status("OK").message("data at id "+id+" successfully deleted").build());
        return ResponseEntity.status(400).body(ApiResponse.builder().code(400).
                status("BAD REQUEST").message("something went wrong").build());
    }

    @ApiOperation( "search users by name query string")
    @GetMapping(value = "/name")
    public ResponseEntity findUserByName(@RequestParam(value = "firstName", required = false) String firstName,
                                         @RequestParam(value = "lastName", required = false) String lastName,
                                         @RequestParam(value = "middleName", required = false) String middleName,
                                         @RequestParam(value = "suffix", required = false) String suffix){
        return ResponseEntity.status(200).body(userService.findNameByQuery(firstName, middleName, lastName, suffix));
    }

    @ApiOperation( "search users by contact info query string")
    @GetMapping(value = "/contact")
    public ResponseEntity findUserByContactInfo(@RequestParam(value = "email", required = false) String email,
                                                @RequestParam(value = "mobile", required = false) String mobile,
                                                @RequestParam(value = "telephone", required = false) String telephone){
        return ResponseEntity.status(200).body(userService.findContactInfoByQuery(email, mobile, telephone));
    }

    @ApiOperation( "search users by address query string")
    @GetMapping(value = "/address")
    public ResponseEntity findUserByAddress(@RequestParam(value = "street", required = false) String street,
                                            @RequestParam(value = "city", required = false) String city,
                                            @RequestParam(value = "state", required = false) String state,
                                            @RequestParam(value = "zipCode", required = false) Integer zipCode){
        return ResponseEntity.status(200).body(userService.findAddressByQuery(street, city, state, zipCode));
    }

    private Function<Errors,String> getValidationErrors = err ->
            err.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
}