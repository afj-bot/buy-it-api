package com.afj.solution.buyitapp.payload.request;

import java.io.Serial;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Tomash Gombosh
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "LoginRequest", description = "Login request model")
public class LoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -1882607329776008227L;

    @ApiModelProperty(
            name = "username",
            dataType = "String",
            value = "Username of the application User",
            example = "black",
            required = true
    )
    @NotEmpty
    private String username;

    @ApiModelProperty(
            name = "password",
            dataType = "String",
            value = "Password of the application User",
            example = "rest1234",
            required = true
    )
    @NotEmpty
    private String password;

    @Override
    public String toString() {
        return String.format("{ \"username\": \"%s\" }", this.getUsername());
    }
}
