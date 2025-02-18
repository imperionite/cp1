
package com.imperionite.cp1.dtos;

import lombok.*; // Lombok for cleaner code

// UserDto (Data Transfer Object for User)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
}
