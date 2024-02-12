package moreno.joaquin.webdemo.model;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @Email
    @NotBlank(message = "Email must not be blank")
    private String email;


    @NotBlank(message = "Password must not be blank")
    private String password;
}
